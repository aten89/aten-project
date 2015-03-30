/**
 * 
 */
package org.eapp.oa.kb.blo;

import java.util.Collection;
import java.util.List;

import org.eapp.oa.kb.dto.KnowledgeQueryParameters;
import org.eapp.oa.kb.dto.KnowledgeReplyQueryParameters;
import org.eapp.oa.kb.hbean.Knowledge;
import org.eapp.oa.kb.hbean.KnowledgeReply;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;


/**
 * 知识点业务接口
 */
public interface IKnowledgeBiz {

    /**
     * 通过Id查找
     * 
     * @param id 知识点id
     * @return 知识点
     */
    public Knowledge getKnowledgeById(String id);

    /**
     * 通过ids列表查找
     * 
     * @param ids idsList
     * @return 知识点翻页对象List
     */
    public List<Knowledge> getKnowledgeByIds(List<String> ids);

    /**
     * 翻页查询
     * 
     * @param kqp 查询参数
     * @return 知识点翻页对象
     */
    public ListPage<Knowledge> queryKnowledge(KnowledgeQueryParameters kqp);


    /**
     * 删除知识
     * 
     * @param id 知识点id
     * @param userid 用户id
     * @return 知识点
     * @throws OaException oa异常
     */
    public Knowledge txDelKnowledge(String id, String userid) throws OaException;

    /**
     * 保存评论
     * 
     * @param id 知识点id
     * @param content 内容
     * @param userAccantId 用户id
     * @param groupName 用户组
     * @throws OaException 异常
     */
    public void txSaveKnowledgeReply(String id, String content, String userAccantId, String groupName)
            throws OaException;

    /**
     * 保存评论
     * 
     * @param reply 知识点评论
     * @throws OaException 系统异常
     */
    public void txSaveKnowledgeReply(KnowledgeReply reply) throws OaException;

    /**
     * 批量保存评论
     * 
     * @param knowledgeReplyList 知识点回复列表
     * @throws OaException oa自定义异常
     */
    public void txSaveKnowledgeReplys(List<KnowledgeReply> knowledgeReplyList) throws OaException;

    /**
     * 知识点回复列表
     * 
     * @param krqp 查询条件类
     * @return 知识点回复列表分页对象
     */
    public ListPage<KnowledgeReply> queryKnowledgeReply(KnowledgeReplyQueryParameters krqp);

    /**
     * 上传附件
     * 
     * @param referId 知识点id
     * @param deletedFiles 删除的文件
     * @param files 附件对象
     */
    public void txUpdateAttachment(String referId, String[] deletedFiles, List<Attachment> files);

    /**
     * 获取附件列表
     * 
     * @param referId 知识点id
     * @return 附件列表
     */
    public List<Attachment> getAttachments(String referId);

    /**
     * 上传内容附件
     * 
     * @param referId 知识点id
     * @param files 附件对象list
     */
    public void txUpdateContentAttachment(String referId, List<Attachment> files);

    /**
     * 获取内容附件列表
     * 
     * @param referId 知识点id
     * @param attType 附件类型
     * @return 内容附件集合
     */
    public Collection<Attachment> getContentAttachments(String referId);

    /**
     * 删除附件
     * 
     * @param imgId 附件id
     * @param referId 知识点id
     * @param attType 附件类型
     */
    public void txDelContentAttachment(String imgId, String referId);

    /**
     * 临时库转正式库
     * 
     * @param idArray 知识点id数组
     * @param parentId 知识库分类id
     * @return 转变的知识点集合
     */
    public List<Knowledge> txChangeToFinal(String[] idArray, String parentId);

    /**
     * 变换类别
     * 
     * @param idArray 知识点id数组
     * @param parentId 知识点分类id
     * @return 转变的知识点集合
     * @throws OaException oa异常
     */
    public void txChangeClass(String[] idArray, String parentId) throws OaException;

    /**
     * 知识库改动邮件通知
     * 
     * @param id 知识分类授权id
     * @param key 授权类型
     * @param flag 类别标志
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendKnowledgeEmail(String id, int key, int flag, String subject, String content);


    /**
     * 知识库高级搜索
     * 
     * @param kqp 查询条件
     * @return 知识点分页对象
     */
    public ListPage<Knowledge> getPageList(KnowledgeQueryParameters kqp);

    /**
     * 通过知识点回复id删除知识点回复
     * 
     * @param knowledgeReplyId 知识点回复id
     * @return 知识点回复
     */
    public KnowledgeReply txDeleteKnowledgeReply(String knowledgeReplyId);

    /**
     * copy知识点
     * 
     * @param idArray 知识点数组
     * @param parentId 知识点分类id
     * @throws OaException oa异常
     */
    public void txCopyKnowledge(String[] idArray, String parentId) throws OaException;

    /**
     * 更新附件 不删除附件文件 留着备份历史
     * 
     * @param referId
     * @param deletedFiles
     * @param files
     */
    void txUpdateAtta(String referId, String[] deletedFiles, List<Attachment> files);
    
    /**
     * 新增
     * @param classId
     * @param subject
     * @param label
     * @param remark
     * @param content
     * @param firstType
     * @param secondType
     * @param status
     * @param publisher
     * @param groupName
     * @param imgMap
     * @return
     * @throws Exception
     */
    Knowledge txAddKnowledge(String classId, String subject, String label, String remark, 
    		String content, String firstType, String secondType, int status, String publisher, String groupName,
    		Collection<Attachment> imgMap) throws Exception;
    
    /**
     * 修改
     * @param id
     * @param subject
     * @param label
     * @param remark
     * @param content
     * @param firstType
     * @param secondType
     * @param publisher
     * @param imgMap
     * @return
     * @throws Exception
     */
    Knowledge txModifyKnowledge(String id, String subject, String label, String remark, 
    		String content, String firstType, String secondType, String publisher,
    		Collection<Attachment> imgMap) throws Exception;
}
