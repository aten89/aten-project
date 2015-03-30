package org.eapp.oa.doc.blo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eapp.oa.doc.dto.DocFormQueryParameters;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;


/**
*
* @author TimLin 
* @version 创建时间：May 4, 2009 
* 
*/
public interface IDocFormBiz {
	
//	public void txDealApproveAssign(String taskInstanceId, String comment,String transitionName,String users);
	/**
	 * 根据状态查询标记起草的公文表单
	 * @param userAccountId
	 * @param docStatus
	 * @return
	 */
	public List<DocForm> getDocForm(String userAccountId, int docStatus,int fileClass);
	
	/**
	 * @author TimLin 
	 * @return DocForm
	 * 根据id获取docForm
	 */
	public DocForm getDocFormById(String id);
	/**
	 * 删除公文
	 * @param String id
	 * @param String accountId
	 */
	void deleteDocForm(String id, String accountId) throws OaException;
	
	/**
	 * 添加公文草稿
	 * @param String docId
	 * @param Attachment am
	 * @return void
	 */
	public void addContentDoc(String docId, Attachment am);
	
	/**
	 * 获取公文草稿
	 * @param String docId
	 * @return Attachment
	 */
	public Attachment getContentDoc(String docId) ;
	
	/**
	 * 更新公文发布的板块信息
	 * @param String docId
	 * @param String contentDir
	 * @param String indexFileName
	 * @param List<String> infoLayouts
	 * @return void
	 */
	public void txUpdateContentUrl(String docId, String contentDir,String indexFileName, String[] infoLayouts) throws IOException;
	
	/**
	 * 获取公文附件
	 * @param String docId
	 * @return Set<Attachment>
	 */
	public Set<Attachment> getInfoAttachments(String docId) ;
	
	/**
	 * 获取公文附件
	 * @param String docId
	 * @param String[] deletedFiles
	 * @param List<Attachment> files
	 * @return void
	 */
	public void txUpdateAttachment(String docId, String[] deletedFiles, List<Attachment> files) ;
	
	/**
	 * 添加或修改公文表单
	 * @param String docFormId
 	 * @param String usreAccountId
 	 * @param String subject
 	 * @param String groupName,
 	 * @param String toDept
 	 * @param String ccDept
 	 * @param String docClassName
 	 * @param String docSecurity, 
	 * @param String docUrgency
	 * @param String[] deletedFiles
	 * @param List<Attachment> files
	 * @return DocForm
	 */
	public DocForm addOrModifyDocForm(String docFormId, String usreAccountId, String subject,  String groupName,
			String toDept, String ccDept, String docClassName, String docSecurity, String docUrgency, String signGroupNames,int fileClass) throws OaException;
	/**
	 * 启动流程
	 * @param String id
	 * @param String flowKey
	 * @return void
	 */
	public DocForm txStartFlow(String id, String flowKey, String signGroupNames,String users) throws OaException;
	
	/**
	 * 查询待办公文
	 * @param String userId
	 * @return List<DocForm>
	 */
	public List<DocForm> queryDealingDocFrom(String userId,int fileClass);
	
	/**
	 * 通过公文表单查询任务
	 * @param String formId
	 * @return List<Task>
	 */
	public List<Task> getEndedTasks(String formId);
	
	/**
	 * 跟踪公文
	 * @param SysMessageQueryParameters qp
	 * @param String userAccountId
	 * @return ListPage
	 */
	public ListPage<DocForm> getTrackDocForm(DocFormQueryParameters qp, String userAccountId,int fileClass) ;
	
	/**
	 * 归档公文
	 * @param SysMessageQueryParameters qp
	 * @param String userAccountId
	 * @return ListPage
	 */
	public ListPage<DocForm> getArchDocForm(DocFormQueryParameters qp,
			String userAccountId,int fileClass);
	/**
	 * 通过copyId查找公文
	 * @param String docFormId
	 * @return List<DocForm>
	 */
	public List<DocForm> getDocFormByCopyId(String docFormId);
	
	/**
	 * 公文Copy
	 * @param String docFormId
	 * @param String accountId
	 * @return DocForm
	 */
	public DocForm txCopyDocForm(String docFormId, String accountId) throws OaException,IOException;
	
	/**
	 * 公文作废
	 * @param String docFormId
	 * @return void
	 */
	public void txCancellationDocForm(String docFormId)throws OaException ;
	
	/**
	 * 正式文档发布
	 * @param String docId 
	 * @param Attachment am 
	 * @param String headerTmpId
	 * @param String docNum 
	 * @param String headerStr
	 * @return void
	 */
	public void addFinalContentDoc(String docId, Attachment am, String headerStr) throws OaException;
	
	/**
	 * 通过公文字号查询公文
	 * @param String numStr
	 * @return List<DocForm>
	 */
	public List<DocForm> getDocFormByDocNum(String numStr);
	
//	public List<Task> getDocTasks(String formId);
	
	/**
	 * 强制结束某个流程，将文件置为“作废”状态
	 * @param docFormId
	 * @param comment
	 */
	public void txForceEnd(String docFormId, String comment);
}
