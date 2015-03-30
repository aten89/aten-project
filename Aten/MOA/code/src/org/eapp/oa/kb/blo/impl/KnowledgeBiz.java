/**
 * 
 */
package org.eapp.oa.kb.blo.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.address.blo.IAddressListBiz;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.kb.blo.IKnowledgeBiz;
import org.eapp.oa.kb.blo.IKnowledgeClassBiz;
import org.eapp.oa.kb.blo.IKnowledgeLogBiz;
import org.eapp.oa.kb.blo.ILabelLibBiz;
import org.eapp.oa.kb.dao.IKnowledgeClassAssignDAO;
import org.eapp.oa.kb.dao.IKnowledgeDAO;
import org.eapp.oa.kb.dao.IKnowledgeReplyDAO;
import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.dto.KnowledgeReplyQueryParameters;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.kb.hbean.KnowledgeAttachment;
import org.eapp.oa.kb.hbean.KnowledgeAttachmentId;
import org.eapp.oa.kb.hbean.KnowledgeClass;
import org.eapp.oa.kb.hbean.KnowledgeClassAssign;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.oa.kb.hbean.KnowledgeReply;
import org.eapp.oa.lucene.IndexTaskRunner;
import org.eapp.oa.lucene.builder.FinalKBIndexBuilder;
import org.eapp.oa.lucene.builder.TempKBIndexBuilder;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.Tools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailMessage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.util.HtmlUtils;


/**
 * 
 * 知识点相关业务层
 */
public class KnowledgeBiz implements IKnowledgeBiz {

    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(KnowledgeBiz.class);

    /**
     * 知识点DAO
     */
    private IKnowledgeDAO knowledgeDAO;

    /**
     * 知识分类业务接口
     */
    private IKnowledgeClassBiz knowledgeClassBiz;

    /**
     * 知识点回复DAO
     */
    private IKnowledgeReplyDAO knowledgeReplyDAO;

    /**
     * 知识分类授权DAO
     */
    private IKnowledgeClassAssignDAO knowledgeClassAssignDAO;

    /**
     * 知识点日志接口
     */
    private IKnowledgeLogBiz knowledgeLogBiz;

    /**
     * 标签管理业务逻辑接口
     */
    private ILabelLibBiz labelLibBiz;

    /**
     * oa 联系人信息业务逻辑接口
     */
    private IAddressListBiz addressListBiz;
   
    public void setLabelLibBiz(ILabelLibBiz labelLibBiz) {
        this.labelLibBiz = labelLibBiz;
    }

    public void setKnowledgeLogBiz(IKnowledgeLogBiz knowledgeLogBiz) {
        this.knowledgeLogBiz = knowledgeLogBiz;
    }
    
    public void setKnowledgeDAO(IKnowledgeDAO knowledgeDAO) {
        this.knowledgeDAO = knowledgeDAO;
    }

    public void setKnowledgeClassBiz(IKnowledgeClassBiz knowledgeClassBiz) {
        this.knowledgeClassBiz = knowledgeClassBiz;
    }

    public void setKnowledgeReplyDAO(IKnowledgeReplyDAO knowledgeReplyDAO) {
        this.knowledgeReplyDAO = knowledgeReplyDAO;
    }

    public void setKnowledgeClassAssignDAO(IKnowledgeClassAssignDAO knowledgeClassAssignDAO) {
        this.knowledgeClassAssignDAO = knowledgeClassAssignDAO;
    }

    public void setAddressListBiz(IAddressListBiz addressListBiz) {
        this.addressListBiz = addressListBiz;
    }

    @Override
    public Knowledge getKnowledgeById(String id) {
        Knowledge kb = knowledgeDAO.findById(id);
        return kb;
    }

    @Override
    public List<Knowledge> getKnowledgeByIds(List<String> ids) {
        if (ids == null) {
            return null;
        }
        List<Knowledge> kbs = new ArrayList<Knowledge>();
        for (String id : ids) {
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            Knowledge kb = getKnowledgeById(id);
            if (kb != null) {
                kbs.add(kb);
            }
        }
        return kbs;
    }

    @Override
    public ListPage<Knowledge> queryKnowledge(KnowledgeQueryParameters kqp) {
        ListPage<Knowledge> kbs = knowledgeDAO.queryKnowledge(kqp);
        return kbs;
    }


    @Override
    public void txSaveKnowledgeReply(KnowledgeReply reply) throws OaException {
        if (reply == null) {
            throw new OaException("该知识点的不允许回复");
        }
        if (reply.getKnowledge() != null) {
            Knowledge knowledge = knowledgeDAO.findById(reply.getKnowledge().getId());
            if (knowledge == null) {
                throw new OaException("评论所属知识点不存在");
            }
            reply.setKnowledge(knowledge);
            knowledgeReplyDAO.save(reply);
        }
    }

    @Override
    public void txSaveKnowledgeReplys(List<KnowledgeReply> knowledgeReplyList) throws OaException {
        for (KnowledgeReply knowledgeReply : knowledgeReplyList) {
            this.txSaveKnowledgeReply(knowledgeReply);
        }
    }

    @Override
    public void sendKnowledgeEmail(String id, int key, int flag, String subject, String content) {
        List<KnowledgeClassAssign> kcas = knowledgeClassAssignDAO.findBindByIdAndFlag(id, key, flag);
        StringBuffer toAddress = new StringBuffer();
        if (key == KnowledgeClassAssign.TYPE_USER) {
            for (KnowledgeClassAssign kca : kcas) {
                // 联系人信息不为空,取邮箱
                AddressList c = addressListBiz.getByAccountId(kca.getAssignKey());
                if (c != null && StringUtils.isNotEmpty(c.getUserEmail())) {
                    toAddress.append(c.getUserEmail());
                    toAddress.append(",");
                }
            }
        }
        if (toAddress.length() > 0) {
            MailMessage msg = new MailMessage(toAddress.toString(), subject, content);
            // 发送
            JMailProxy.daemonSend(msg);
        }
    }

    @Override
    public Knowledge txDelKnowledge(String id, String userid) throws OaException {
        Knowledge know = knowledgeDAO.findById(id);
        if (know == null) {
            return null;
        }
        // 删除知识点附件
        if (know.getContentImgAttachments() != null) {
            for (Attachment am : know.getContentImgAttachments()) {
                KnowledgeAttachmentId attId = new KnowledgeAttachmentId(know, am);
                KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_CONTENT_IMG);
                knowledgeDAO.delete(att);
                knowledgeDAO.delete(am);
                FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(am.getFilePath()));
            }
        }
        if (know.getKnowledgeAttachments() != null) {
            for (Attachment am : know.getKnowledgeAttachments()) {
                KnowledgeAttachmentId attId = new KnowledgeAttachmentId(know, am);
                KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_ATTACH);
                knowledgeDAO.delete(att);
                knowledgeDAO.delete(am);
                FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(am.getFilePath()));
            }
        }
        
        knowledgeDAO.delete(know);
        if (StringUtils.isNotEmpty(userid)) {
            // 记录日志
            knowledgeLogBiz.addKnowledgeLog(userid, KnowledgeLog.TYPE_DELETE, know.getId(), know.getSubject());
        }
        // 删除该条知识点的索引
        updateKnowledgeIndex(know, 3);
        return know;
    }

    @Override
    public void txSaveKnowledgeReply(String id, String content, String userAccantId, String groupName)
            throws OaException {
        Knowledge knowledge = knowledgeDAO.findById(id);
        if (knowledge == null) {
            throw new OaException("该知识点的已不存在");
        }
        // if (knowledge.getProperty() == Knowledge.PROPERTY_NO_REPLY) {
        // throw new OaException("该知识点的不允许回复");
        // }

        if (StringUtils.isNotEmpty(content)) {
            content = HtmlUtils.htmlEscape(content);
            content = Tools.htmlEncode(content);
        }
        KnowledgeReply reply = new KnowledgeReply();
        reply.setGroupName(groupName);
        reply.setKnowledge(knowledge);
        reply.setReplyMan(userAccantId);
        reply.setReplyDate(new Date());
        reply.setContent(content);
        knowledgeReplyDAO.save(reply);
    }

    @Override
    public ListPage<KnowledgeReply> queryKnowledgeReply(KnowledgeReplyQueryParameters krqp) {
        return knowledgeReplyDAO.queryKnowledgeReply(krqp);
    }

    @Override
    public void txUpdateAttachment(String referId, String[] deletedFiles, List<Attachment> files) {
        Knowledge knowledge = knowledgeDAO.findById(referId);
        // 删除旧附件
        if (deletedFiles != null && deletedFiles.length > 0 && knowledge.getKnowledgeAttachments() != null
                && knowledge.getKnowledgeAttachments().size() > 0) {
            List<String> delFileList = Arrays.asList(deletedFiles);
            // 通过名称删除附件，前台控件只支持名称删除
            for (Attachment am : new ArrayList<Attachment>(knowledge.getKnowledgeAttachments())) {
                if (delFileList.contains(am.getDisplayName() + am.getFileExt())) {

                    KnowledgeAttachmentId attId = new KnowledgeAttachmentId(knowledge, am);
                    KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_ATTACH);
                    knowledgeDAO.delete(att);
                    knowledge.getKnowledgeAttachments().remove(am);
                    knowledgeDAO.delete(am);
                    // 删除文件
                    FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(am.getFilePath()));
                }
            }
        }
        for (Attachment am : files) {
            KnowledgeAttachmentId attId = new KnowledgeAttachmentId(knowledge, am);
            KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_ATTACH);
            knowledgeDAO.save(am);
            knowledgeDAO.save(att);
        }
        knowledge.getKnowledgeAttachments().addAll(files);
        knowledgeDAO.saveOrUpdate(knowledge);
    }

    @Override
    public void txUpdateAtta(String referId, String[] deletedFiles, List<Attachment> files) {
        Knowledge knowledge = knowledgeDAO.findById(referId);
        // 删除旧附件
        if (deletedFiles != null && deletedFiles.length > 0 && knowledge.getKnowledgeAttachments() != null
                && knowledge.getKnowledgeAttachments().size() > 0) {
            List<String> delFileList = Arrays.asList(deletedFiles);
            // 通过名称删除附件，前台控件只支持名称删除
            for (Attachment am : new ArrayList<Attachment>(knowledge.getKnowledgeAttachments())) {
                if (delFileList.contains(am.getDisplayName() + am.getFileExt())) {
                    KnowledgeAttachmentId attId = new KnowledgeAttachmentId(knowledge, am);
                    KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_ATTACH);
                    knowledgeDAO.delete(att);
                    knowledge.getKnowledgeAttachments().remove(am);
                    knowledgeDAO.delete(am);
                }
            }
        }
        for (Attachment am : files) {
            KnowledgeAttachmentId attId = new KnowledgeAttachmentId(knowledge, am);
            KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_ATTACH);
            knowledgeDAO.save(am);
            knowledgeDAO.save(att);
        }
        knowledge.getKnowledgeAttachments().addAll(files);
        knowledgeDAO.saveOrUpdate(knowledge);
    }

    @Override
    public List<Attachment> getAttachments(String referId) {
        Knowledge knowledge = knowledgeDAO.findById(referId);
        if (knowledge == null) {
            return null;
        }
        return new ArrayList<Attachment>(knowledge.getKnowledgeAttachments());
    }

    @Override
    public void txUpdateContentAttachment(String referId, List<Attachment> files) {
        Knowledge knowledge = knowledgeDAO.findById(referId);
        knowledge.getContentImgAttachments().addAll(files);
        knowledgeDAO.saveOrUpdate(knowledge);
    }

    @Override
    public Collection<Attachment> getContentAttachments(String referId) {
        if (referId == null) {
            throw new IllegalArgumentException();
        }
        Knowledge knowledge = knowledgeDAO.findById(referId);
        if (knowledge == null) {
            throw new IllegalArgumentException();
        }
        return knowledge.getContentImgAttachments();
    }

    @Override
    public void txDelContentAttachment(String imgId, String referId) {
        Knowledge knowledge = knowledgeDAO.findById(referId);
        Set<Attachment> imgSet = knowledge.getContentImgAttachments();
        Attachment delImgAtt = null;
        for (Attachment att : imgSet) {
            if (att.getId().equals(imgId)) {
                delImgAtt = att;
                break;
            }
        }
        if (delImgAtt != null) {
            KnowledgeAttachmentId attId = new KnowledgeAttachmentId(knowledge, delImgAtt);
            KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_CONTENT_IMG);
            knowledgeDAO.delete(att);
            FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(delImgAtt.getFilePath()));
            imgSet.remove(delImgAtt);
            knowledgeDAO.saveOrUpdate(knowledge);
            knowledgeDAO.delete(delImgAtt);
        }
    }

    @Override
    public List<Knowledge> txChangeToFinal(String[] idArray, String parentId) {
        if (idArray == null || parentId == null) {
            throw new IllegalArgumentException();
        }

        KnowledgeClass knowClass = knowledgeClassBiz.getKnowledgeClassById(parentId);
        if (knowClass == null) {
            throw new IllegalArgumentException();
        }
        List<Knowledge> kbs = new ArrayList<Knowledge>();
        for (String referId : idArray) {
            if (referId != null && referId.length() > 0) {
                Knowledge knowledge = knowledgeDAO.findById(referId);
                if (knowledge == null) {
                    continue;
                }
                knowledge.setKnowledgeClass(knowClass);
                knowledge.setStatus(Knowledge.STATUS_FINAL);
                knowledge.setProperty(Knowledge.PROPERTY_NO_REPLY);
                knowledge.setModifyDate(new Date());
                knowledgeDAO.update(knowledge);
                kbs.add(knowledge);
            }
        }
        return kbs;
    }

    @Override
    public void txChangeClass(String[] idArray, String parentId) throws OaException {
        if (idArray == null || parentId == null) {
            throw new IllegalArgumentException();
        }
        KnowledgeClass knowClass = knowledgeClassBiz.getKnowledgeClassById(parentId);
        if (knowClass == null) {
            throw new IllegalArgumentException();
        }
        for (String referId : idArray) {
            if (referId != null && referId.length() > 0) {
                Knowledge knowledge = knowledgeDAO.findById(referId);
                if (knowledge == null) {
                    throw new OaException("数据库数据错误!");
                }
                if (knowClass.equals(knowledge.getKnowledgeClass())) {
                    throw new OaException("知识类别未改变,不做移动!");
                }
                knowledge.setKnowledgeClass(knowClass);
                knowledge.setModifyDate(new Date());
                knowledgeDAO.update(knowledge);
                updateKnowledgeIndex(knowledge, 2);
            }
        }
    }


    @Override
    public ListPage<Knowledge> getPageList(KnowledgeQueryParameters kqp) {
        ListPage<Knowledge> list = this.queryKnowledge(kqp);
        List<Knowledge> dataList = new ArrayList<Knowledge>();
        if (list != null && list.getDataList() != null) {
            for (Knowledge knowledge : list.getDataList()) {
                String content = null;
                if (kqp.getSubject() != null) {
                    content = knowledge.getSubject();
                    content = content.replace(kqp.getSubject().substring(1, kqp.getSubject().length() - 1),
                            "<font class='klColor'>" + kqp.getSubject().substring(1, kqp.getSubject().length() - 1)
                                    + "</font>");

                    knowledge.setSubject(content);

                }
                if (kqp.getPublisher() != null) {
                    content = knowledge.getPublisherName();
                    content = content.replace(knowledge.getPublisherName(),
                            "<font class='klColor'>" + knowledge.getPublisherName() + "</font>");
                    knowledge.setPublisher(content);
                }
                dataList.add(knowledge);

            }
        }
        list.setDataList(dataList);
        return list;
    }


    @Override
    public KnowledgeReply txDeleteKnowledgeReply(String knowledgeReplyId) {
        KnowledgeReply knowledgeReply = knowledgeReplyDAO.findById(knowledgeReplyId);
        if (knowledgeReply != null) {
            knowledgeReplyDAO.delete(knowledgeReply);
        }
        return knowledgeReply;
    }


    @Override
    public void txCopyKnowledge(String[] idArray, String parentId) throws OaException {
        if (idArray == null || parentId == null) {
            throw new IllegalArgumentException();
        }
        KnowledgeClass knowClass = knowledgeClassBiz.getKnowledgeClassById(parentId);
        if (knowClass == null) {
            throw new IllegalArgumentException();
        }
        Knowledge nkb = null;
        for (String referId : idArray) {
            nkb = new Knowledge();
            if (referId != null && referId.length() > 0) {
                Knowledge knowledge = knowledgeDAO.findById(referId);
                if (knowledge == null) {
                    throw new OaException("数据库数据错误!");
                }
                if (knowClass.equals(knowledge.getKnowledgeClass())) {
                    throw new OaException("知识类别未改变,不做复制操作!");
                }
                nkb = copyKnowledge(knowledge);
                nkb.setKnowledgeClass(knowClass);
                nkb.setModifyDate(new Date());
                knowledgeDAO.saveOrUpdate(nkb);
            }
        }
    }

    /**
     * copy知识点
     * 
     * @param knowledge 要copy的知识点
     * @return copy后的知识点
     */
    private Knowledge copyKnowledge(Knowledge knowledge) {
        Knowledge kb = new Knowledge();
        BeanUtils.copyProperties(knowledge, kb, new String[] { "id", "knowledgeAttachments", "contentImgAttachments",
                "contentVideoAttachments", "knowfaultfeatures", "knowfunmods", "replys", "scores" });
        knowledgeDAO.save(kb);
        // 知识库附件拷贝
        Set<Attachment> knowledgeAttachments = knowledge.getKnowledgeAttachments();
        Set<Attachment> kbattachs = new HashSet<Attachment>(0);
        if (!knowledgeAttachments.isEmpty()) {
            Attachment att = null;
            for (Attachment am : knowledgeAttachments) {
                att = txSaveAttachment(am);
                KnowledgeAttachmentId attId = new KnowledgeAttachmentId(kb, att);
                KnowledgeAttachment katt = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_ATTACH);
                knowledgeDAO.saveOrUpdate(katt);
                kbattachs.add(att);
            }
            kb.setKnowledgeAttachments(kbattachs);
        }

        Set<Attachment> contentImgAttachments = knowledge.getContentImgAttachments();
        Set<Attachment> imgattachs = new HashSet<Attachment>(0);
        if (!contentImgAttachments.isEmpty()) {
            Attachment att = null;
            KnowledgeAttachmentId attId = null;
            for (Attachment am : contentImgAttachments) {
                att = txSaveAttachment(am);
                attId = new KnowledgeAttachmentId(kb, att);
                KnowledgeAttachment katt = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_CONTENT_IMG);
                knowledgeDAO.saveOrUpdate(katt);
                imgattachs.add(att);
            }
            kb.setContentImgAttachments(imgattachs);
        }

        // 知识回复拷贝
        Set<KnowledgeReply> replys = knowledge.getReplys();
        Set<KnowledgeReply> replySet = new HashSet<KnowledgeReply>(0);
        if (!replys.isEmpty()) {
            KnowledgeReply rep = null;
            for (KnowledgeReply reply : replys) {
                rep = new KnowledgeReply();
                BeanUtils.copyProperties(reply, rep, new String[] { "id", "knowledge" });
                rep.setKnowledge(kb);
                knowledgeDAO.saveOrUpdate(rep);
                replySet.add(rep);
            }
            kb.setReplys(replySet);
        }

        knowledgeDAO.saveOrUpdate(kb);
        
        updateKnowledgeIndex(knowledge, 1);
        return kb;
    }

    /**
     * copy附件
     * 
     * @param am 附件对象
     * @return 附件对象
     */
    private Attachment txSaveAttachment(Attachment am) {
        Attachment att = new Attachment();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
        String tarDir = SysConstants.KNOWLEDGE_ATTACHMENT_DIR + sdf.format(new Date());
        String fileName = SerialNoCreater.createUUID() + am.getFileExt();
        String path = tarDir + fileName;
        String srcPath = FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(am.getFilePath()));
        String tarPath = FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(tarDir));
        try {
        	FileUtils.copyFile(new File(srcPath), new File(tarPath, fileName));
//            FileUtils.copy(new File(srcPath), new File(tarPath), fileName);
        } catch (IOException e) {
            LOG.info("txSaveAttachment failed:", e);
        }
        att.setId(null);
        att.setFileExt(am.getFileExt());
        att.setFilePath(path);
        att.setDisplayName(am.getDisplayName());
        att.setSize(am.getSize());
        att.setUploadDate(new Date());
        knowledgeDAO.save(att);
        return att;
    }
    
    @Override
    public Knowledge txAddKnowledge(String classId, String subject, String label, String remark, 
    		String content, String firstType, String secondType, int status, String publisher, String groupName,
    		Collection<Attachment> imgMap) throws Exception {
    	
    	Knowledge kb = new Knowledge();
    	kb.setSubject(subject);
    	
    	kb.setRemark(remark);
    	kb.setContent(content);
    	kb.setStatus(status);
    	kb.setFirstType(firstType);
    	kb.setSecondType(secondType);
        kb.setProperty(Knowledge.PROPERTY_COMM);
    	kb.setDisplayMode(Knowledge.DISPLAYMODE_CONTENT);
    	Date now = new Date();
    	kb.setModifyDate(now);
    	kb.setPublishDate(now);
    	kb.setPublisher(publisher);
    	kb.setGroupName(groupName);
    	// 关键字(标签)
    	kb.setLabels(formatKnowledgeLabel(label));
    	
    	KnowledgeClass knowledgeClass = knowledgeClassBiz.getKnowledgeClassById(classId);
        if (knowledgeClass == null) {
            throw new OaException("知识库类别不存在");
        }
        kb.setKnowledgeClass(knowledgeClass);

        knowledgeDAO.save(kb);
        txModifyKnowledgeImage(kb, imgMap);
        
        //添加新增日志
        knowledgeLogBiz.addKnowledgeLog(publisher, KnowledgeLog.TYPE_ADD, kb.getId(), kb.getSubject());
        
        // 添加索引
        updateKnowledgeIndex(kb, 1);
    	return kb;
    }
    
    @Override
    public Knowledge txModifyKnowledge(String id, String subject, String label, String remark, 
    		String content, String firstType, String secondType, String publisher,
    		Collection<Attachment> imgMap) throws Exception {
    	
    	Knowledge kb = knowledgeDAO.findById(id);
     	if (kb == null) {
        	throw new OaException("该知识点不存在");
       	}
         
    	kb.setSubject(subject);
    	kb.setRemark(remark);
    	kb.setContent(content);
    	kb.setFirstType(firstType);
    	kb.setSecondType(secondType);
    	Date now = new Date();
    	kb.setModifyDate(now);
    	// 关键字(标签)
    	kb.setLabels(formatKnowledgeLabel(label));

        knowledgeDAO.update(kb);
        txModifyKnowledgeImage(kb, imgMap);
        
        //添加新增日志
        knowledgeLogBiz.addKnowledgeLog(publisher, KnowledgeLog.TYPE_MODIFY, kb.getId(), kb.getSubject());
        
        // 添加索引
        updateKnowledgeIndex(kb, 2);
    	return kb;
    }

    /**
     * 更新知识点索引
     * 
     * @param knowledge 需要更新索引的知识点
     * @param type 更新类型 1 新增 2 修改 3 删除
     */
    private void updateKnowledgeIndex(Knowledge knowledge, int type) {
        // 更新索引
        String documentType = (knowledge.getStatus() == Knowledge.STATUS_FINAL) ? FinalKBIndexBuilder.DOCUMENTTYPE
                : TempKBIndexBuilder.DOCUMENTTYPE;
        switch (type) {
            case 1:
                IndexTaskRunner.addIndex(knowledge.getId(), documentType);
                break;
            case 2:
                IndexTaskRunner.modifyIndex(knowledge.getId(), documentType);
                break;
            case 3:
                IndexTaskRunner.deleteIndex(knowledge.getId(), documentType);
                break;
        }
    }
    
    /**
     * 更新知识点的标签
     * 
     * @param know 需要更新标签的知识点
     */
    private String formatKnowledgeLabel(String label) {
    	if (label != null) {
            label = label.replaceAll("[,| |，]", " ");
            labelLibBiz.txAddOrModLabelLibByNames(label.split(" "));
        }
    	return label;
    }

    /**
     * 更新知识点的图片附件和音频附件
     * @param know 被修改的知识点
     * @param imgMap 图像附件
     * @param videoMap 音频附件
     * @return 被修改的知识点
     * @throws Exception 文件拷贝出现异常
     */
    private void txModifyKnowledgeImage(Knowledge know, Collection<Attachment> imgMap) throws Exception {
        if (imgMap != null && !imgMap.isEmpty()) {
        	
            Set<Attachment> set = know.getContentImgAttachments();
            if (set == null) {
                set = new HashSet<Attachment>();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            String pathDir = SysConstants.KNOWLEDGE_ATTACHMENT_DIR + sdf.format(new Date());
            
            //替换临时目录图片为正式目录
            String content = know.getContent().replaceAll(FileDispatcher.getTempAbsDir(), FileDispatcher.getAbsPath(pathDir));
            know.setContent(content);
            
            for (Attachment am : imgMap) {
            	String path = pathDir + am.getId() + am.getFileExt();
                FileUtil.moveFile(new File(FileDispatcher.getSaveDir(am.getFilePath())),
                        new File(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path))));
                am.setFilePath(path);
                am.setId(null);
                knowledgeDAO.save(am);
                KnowledgeAttachmentId attId = new KnowledgeAttachmentId(know, am);
                KnowledgeAttachment att = new KnowledgeAttachment(attId, KnowledgeAttachment.TYPE_CONTENT_IMG);
                knowledgeDAO.saveOrUpdate(att);
                set.add(am);
            }
            know.setContentImgAttachments(set);
        }
    }
}
