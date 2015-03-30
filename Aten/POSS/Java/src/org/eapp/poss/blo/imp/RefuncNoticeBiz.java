/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.client.util.UsernameCache;
import org.eapp.poss.blo.IRefuncNoticeBiz;
import org.eapp.poss.dao.IRefuncNoticeDAO;
import org.eapp.poss.dao.param.RefuncNoticeQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.RefuncNotice;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	钟华杰		新建
 * </pre>
 */
public class RefuncNoticeBiz implements IRefuncNoticeBiz {
    
    // dao
    private IRefuncNoticeDAO refuncNoticeDAO;
    
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IRefuncNoticeBiz#queryRefuncNoticeById(java.lang.String)
     */
    @Override
    public RefuncNotice queryRefuncNoticeById(String id) throws PossException {
    	if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：产品ID为空");
        }
    	RefuncNotice refuncNotice = refuncNoticeDAO.findById(id);
        if (refuncNotice == null) {
            throw new PossException("产品不存在");
        }
        Hibernate.initialize(refuncNotice.getReFuncNoticAttachments());
        
        //设置联系人名称
    	refuncNotice.setLinkmanName(UsernameCache.getDisplayName(refuncNotice.getLinkman()));
        return refuncNotice;
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IRefuncNoticeBiz#txAddRefuncNotice(org.eapp.poss.hbean.RefuncNotice)
     */
    @Override
    public RefuncNotice txAddRefuncNotice(RefuncNotice transRefuncNotice) throws PossException {
    	if (transRefuncNotice == null) {
            throw new IllegalArgumentException("非法参数：退款须知对象为空");
        }
        checkRefuncNotice(transRefuncNotice);
        transRefuncNotice.setCreateTime(new Date());
        refuncNoticeDAO.save(transRefuncNotice);
        return transRefuncNotice;
    }
    
    @Override
    public void txDelRefuncNotice(String id) throws PossException {
    	RefuncNotice perisRefuncNotice = this.queryRefuncNoticeById(id);
    	refuncNoticeDAO.delete(perisRefuncNotice);
    	// 删除对应的附件数据库记录、附件文件内容
    	if (perisRefuncNotice.getReFuncNoticAttachments() != null) {
    		for (Attachment atta : perisRefuncNotice.getReFuncNoticAttachments()) {
    			refuncNoticeDAO.delete(atta);
    			// 删除文件
    			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(atta.getFilePath()));
    			if (f != null) {
    				f.delete();
    			}
    		}
    	}
    	
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IRefuncNoticeBiz#txUpdateRefuncNotice(org.eapp.poss.hbean.RefuncNotice)
     */
    @Override
    public RefuncNotice txUpdateRefuncNotice(RefuncNotice transRefuncNotice) throws PossException {
        // TODO Auto-generated method stub
    	if (transRefuncNotice == null) {
            throw new IllegalArgumentException("非法参数：退款须知对象为空");
        }
        if (StringUtils.isEmpty(transRefuncNotice.getId())) {
            throw new IllegalArgumentException("非法参数：退款须知ID为空");
        }
//        RefuncNotice perisRefuncNotice = refuncNoticeDAO.findById(transRefuncNotice.getId());
        RefuncNotice perisRefuncNotice = this.queryRefuncNoticeById(transRefuncNotice.getId());
        if (perisRefuncNotice == null) {
            throw new PossException("退款须知不存在");
        }
        // TODO
        checkRefuncNotice(transRefuncNotice);
//        BeanUtils.copyProperties(perisRefuncNotice, transRefuncNotice, new String[] {"createTime"});
        
        perisRefuncNotice.setLinkman(transRefuncNotice.getLinkman());
        perisRefuncNotice.setRefundNotice(transRefuncNotice.getRefundNotice());
        perisRefuncNotice.setTrustCompany(transRefuncNotice.getTrustCompany());
        transRefuncNotice.setId(null);
        
        refuncNoticeDAO.saveOrUpdate(perisRefuncNotice);
        return perisRefuncNotice;
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IRefuncNoticeBiz#queryRefuncNoticeListPage(org.eapp.poss.dao.param.RefuncNoticeQueryParameters)
     */
    @Override
    public ListPage<RefuncNotice> queryRefuncNoticeListPage(RefuncNoticeQueryParameters qp) throws PossException {
    	if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        ListPage<RefuncNotice> refuncNoticeListPage = refuncNoticeDAO.findRefuncNoticeListPage(qp);
        if (refuncNoticeListPage != null && refuncNoticeListPage.getDataList() != null) {
            for (RefuncNotice refuncNotice : refuncNoticeListPage.getDataList()) {
                // init obj
                initRefuncNotice(refuncNotice);
            }
        }
        
        return refuncNoticeListPage;
    }
    
    /**
     * TODO
     * @param refuncNotice
     */
    private void initRefuncNotice(RefuncNotice refuncNotice) {
    	Hibernate.initialize(refuncNotice.getReFuncNoticAttachments());
    	refuncNotice.setAttachmentCount(CollectionUtils.isEmpty(refuncNotice.getReFuncNoticAttachments()) ? 0 : refuncNotice.getReFuncNoticAttachments().size());
    	refuncNotice.setCreateTimeStr(null == refuncNotice.getCreateTime() ? "" : df.format(refuncNotice.getCreateTime()));
    	
    	//设置联系人名称
    	refuncNotice.setLinkmanName(UsernameCache.getDisplayName(refuncNotice.getLinkman()));
    }
    
    private void checkRefuncNotice(RefuncNotice refuncNotice) throws PossException {
        // TODO
    }
    
    /*****************************************附件上传相关***************************************/
    @Override
    public void txBatchUploadRefuncNoticeAttachment(String id,
    		String[] deletedFiles,
    		Collection<Attachment> refuncNoticeAttachments)
    		throws PossException {
    	RefuncNotice refuncNotice = this.queryRefuncNoticeById(id);
    	//先删除前台传过来要删除的附件
    	txDeleteOldLicenceIfNecessary(deletedFiles, refuncNotice);
    	//保存许可证附件
    	txSaveRefuncNoticeAttachment(refuncNoticeAttachments, refuncNotice);
    }

    /**
     * 同步删除前台删除的附件
     * @param deletedFiles
     * @param refuncNotice
     */
    private void txDeleteOldLicenceIfNecessary(String[] deletedFiles, RefuncNotice refuncNotice) {
        // 取得前台的删除的附件列表
        if (deletedFiles != null) {
            List<String> delFileList = Arrays.asList(deletedFiles);
            // 取得计划该客户已经上传的许可证附件
            Set<Attachment> existAttachments = refuncNotice.getReFuncNoticAttachments();
            if (!CollectionUtils.isEmpty(existAttachments)) {
            	for(Attachment attachement : existAttachments) {
            		if (delFileList.contains(attachement.getDisplayName() + attachement.getFileExt())) {
            			refuncNotice.getReFuncNoticAttachments().remove(attachement);
            		}
            	}
            	refuncNoticeDAO.update(refuncNotice);
            }
        }
    }
    
    /**
     * 保存退款须知附件
     * @param refuncNoticeAttachments
     * @param refuncNotice
     */
    private void txSaveRefuncNoticeAttachment(Collection<Attachment> refuncNoticeAttachments, RefuncNotice refuncNotice) {
        if (!CollectionUtils.isEmpty(refuncNoticeAttachments)) {
            // 保存许可证附件
        	for(Attachment attachment : refuncNoticeAttachments) {
        		refuncNoticeDAO.save(attachment);
        	}
        	
        	// 附件与退款须知绑定
        	refuncNotice.getReFuncNoticAttachments().addAll(refuncNoticeAttachments);
        	refuncNoticeDAO.saveOrUpdate(refuncNotice);
        }

    }
    
    /**
     * set the RefuncNoticeDAO to set
     * @param RefuncNoticeDAO the RefuncNoticeDAO to set
     */
    public void setRefuncNoticeDAO(IRefuncNoticeDAO refuncNoticeDAO) {
        this.refuncNoticeDAO = refuncNoticeDAO;
    }

}
