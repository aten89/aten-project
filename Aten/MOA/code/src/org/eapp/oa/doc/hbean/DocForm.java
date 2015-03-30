package org.eapp.oa.doc.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * DocForm entity. Description:发文表单
 * 
 * @author songdingsong
 */

public class DocForm implements java.io.Serializable {
	

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6302621314345539326L;
	
	public static final int STATUS_UNPUBLISH = 0;//未发布
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_PUBLISH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	
	public static final int DIS_FILE = 0;//公文
	public static final int COM_FILE = 1;//普通文件
	
	// ID_,VARCHAR2(36),不能为空     --主键ID
	private String id;
	// Draftsman_,VARCHAR2(128)    --起草人
	private String draftsman;
	// GroupName_,VARCHAR2(128)    --所属机构
	private String groupName;
	//DocClass_					   --公文分类
	private String docClassName;
	// DraftDate_,TIMESTAMP        --起草时间
	private Date draftDate;
	// DocNumber_,VARCHAR2(256)    --发文编号
	private String docNumber;
	// SubmitTo_,VARCHAR2(512)     --主送单位
	private String submitTo;
	// CopyTo_,VARCHAR2(512)       --抄送单位
	private String copyTo;
	// Subject_,VARCHAR2(256)      --标题
	private String subject;
	// BodyDraftDoc_,VARCHAR2(36)  --正文草稿
	private Attachment bodyDraftDoc;
	// BodyDoc_,VARCHAR2(36)       --正文文档
	private Attachment bodyDoc;
	// FlowInstanceID_,VARCHAR2(36)--流程实例
	private String flowInstanceId;
	// Passed_,SMALLINT            --是否生效
	private Boolean passed;
	// Formstatus_				   --公文状态
	private Integer docStatus;
	// ArchiveDate_,TIMESTAMP      --归档时间
	private Date archiveDate;
	// Urgency_,VARCHAR2(128)      --缓急程度
	private String urgency;
	// SecurityClass_,VARCHAR2(128)--密级
	private String securityClass;
	// CopyDocFormID_,VARCHAR2(36) --副本发文
	private String copyDocFormId;   //参见OutlayItem
	// SIGNGROUPNAMES_, varchar2(512) --会签部门
	private String signGroupNames; 
	//FILECLASS_,INTEGER               --文件类别
	private Integer fileClass;
	
	private Set<Attachment> attachments = new HashSet<Attachment>(0);
	
	private Set<Information> informations = new HashSet<Information>(0);
	
	// Constructors
	private transient Task task;

	
	/** default constructor */
	public DocForm() {
	}

	public Integer getFileClass() {
		return fileClass;
	}

	public void setFileClass(Integer fileClass) {
		this.fileClass = fileClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDraftsman() {
		return draftsman;
	}

	public void setDraftsman(String draftsman) {
		this.draftsman = draftsman;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getDraftDate() {
		return draftDate;
	}

	public void setDraftDate(Date draftDate) {
		this.draftDate = draftDate;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getSubmitTo() {
		return submitTo;
	}

	public void setSubmitTo(String submitTo) {
		this.submitTo = submitTo;
	}

	public String getCopyTo() {
		return copyTo;
	}

	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Attachment getBodyDraftDoc() {
		return bodyDraftDoc;
	}

	public void setBodyDraftDoc(Attachment bodyDraftDoc) {
		this.bodyDraftDoc = bodyDraftDoc;
	}

	public Attachment getBodyDoc() {
		return bodyDoc;
	}

	public void setBodyDoc(Attachment bodyDoc) {
		this.bodyDoc = bodyDoc;
	}

	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getSecurityClass() {
		return securityClass;
	}

	public void setSecurityClass(String securityClass) {
		this.securityClass = securityClass;
	}

/*	public Set<DocForm> getDocForms() {
		return docForms;
	}

	public void setDocForms(Set<DocForm> docForms) {
		this.docForms = docForms;
	}*/

	public Integer getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(Integer docStatus) {
		this.docStatus = docStatus;
	}

	public String getDocClassName() {
		return docClassName;
	}

	public void setDocClassName(String docClassName) {
		this.docClassName = docClassName;
	}
	
	public String getDraftsmanName(){
		return UsernameCache.getDisplayName(draftsman);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DocForm other = (DocForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

	public String getCopyDocFormId() {
		return copyDocFormId;
	}

	public void setCopyDocFormId(String copyDocFormId) {
		this.copyDocFormId = copyDocFormId;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Set<Information> getInformations() {
		return informations;
	}

	public void setInformations(Set<Information> informations) {
		this.informations = informations;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	public String getBodyDraftDocUrl() {
		if (bodyDraftDoc == null) {
			return null;
		}
		return FileDispatcher.getAbsPath(bodyDraftDoc.getFilePath());
	}
	public String getBodyDocUrl() {
		if (bodyDoc == null) {
			return null;
		}
		return FileDispatcher.getAbsPath(bodyDoc.getFilePath());
	}
	
	public String getSignGroupNames() {
		return signGroupNames;
	}

	public void setSignGroupNames(String signGroupNames) {
		this.signGroupNames = signGroupNames;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("passed=");
		sb.append(getPassed());
		sb.append(",");
		sb.append("archiveDate=");
		sb.append(getArchiveDate());
		sb.append(",");
		sb.append("draftDate=");
		sb.append(getDraftDate());
		sb.append(",");
		sb.append("docStatus=");
		sb.append(getDocStatus());
		sb.append(",");
		sb.append("copyDocFormId  =");
		sb.append(getCopyDocFormId());
		sb.append(",");
		sb.append("copyTo=");
		sb.append(getCopyTo());
		sb.append(",");
		sb.append("docClassName=");
		sb.append(getDocClassName());
		sb.append(",");
		sb.append("docNumber=");
		sb.append(getDocNumber());
		sb.append(",");
		sb.append("draftsman=");
		sb.append(getDraftsman());
		sb.append(",");
		sb.append("flowInstanceId=");
		sb.append(getFlowInstanceId());
		sb.append(",");
		sb.append("groupName=");
		sb.append(getGroupName());
		sb.append(",");
		sb.append("id=");
		sb.append(getId());
		sb.append(",");
		sb.append("securityClass=");
		sb.append(getSecurityClass());
		sb.append(",");
		sb.append("subject=");
		sb.append(getSubject());
		sb.append(",");
		sb.append("submitTo=");
		sb.append(getSubmitTo());
		sb.append(",");
		sb.append("urgency=");
		sb.append(getUrgency());
		sb.append(",");
		sb.append("signGroupNames=");
		sb.append(getSignGroupNames());
		sb.append("]");
		return sb.toString();
	}

}