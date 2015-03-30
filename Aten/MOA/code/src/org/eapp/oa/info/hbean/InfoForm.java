package org.eapp.oa.info.hbean;

import java.util.Date;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;


/**
 * InfoForm entity.
 * Description:信息审批单
 * @author MyEclipse Persistence Tools
*/

public class InfoForm implements java.io.Serializable {

	private static final long serialVersionUID = 6919073554411259148L;
	
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//CONTENTDOC_,VARCHAR2(36)                          --内容文件
	private Attachment contentDoc;	
	//FLOWINSTANCEID_,VARCHAR2(36)                      --流程实例
	private String flowInstanceId;
	//PASSED_,SMALLINT                                  --是否生效
	private Boolean passed;
	//ARCHIVEDATE_,TIMESTAMP                            --归档时间
	private Date archiveDate;
	//CopyInfoFormID_,VARCHAR2(36)						--拷贝源文件的ID
	private String copyInfoFormId;					
	
	private Information information;
	
	
	
//	private Set<Attachment> attachments = new HashSet<Attachment>(0);
	
	private transient Task task;//不是hibernate属性
	// Constructors

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the contentDoc
	 */
	public Attachment getContentDoc() {
		return contentDoc;
	}

	/**
	 * @param contentDoc the contentDoc to set
	 */
	public void setContentDoc(Attachment contentDoc) {
		this.contentDoc = contentDoc;
	}
	
	public String getContentDocUrl() {
		if (contentDoc == null) {
			return null;
		}
		return FileDispatcher.getAbsPath(contentDoc.getFilePath());
	}

	/**
	 * @return the flowInstanceId
	 */
	public String getFlowInstanceId() {
		return flowInstanceId;
	}

	/**
	 * @param flowInstanceId the flowInstanceId to set
	 */
	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	/**
	 * @return the passed
	 */
	public Boolean getPassed() {
		return passed;
	}

	/**
	 * @param passed the passed to set
	 */
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	/**
	 * @return the archiveDate
	 */
	public Date getArchiveDate() {
		return archiveDate;
	}

	/**
	 * @param archiveDate the archiveDate to set
	 */
	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}


	public Information getInformation() {
		return information;
	}

	public void setInformation(Information information) {
		this.information = information;
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
		final InfoForm other = (InfoForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the copyInfoFormId
	 */
	public String getCopyInfoFormId() {
		return copyInfoFormId;
	}

	/**
	 * @param copyInfoFormId the copyInfoFormId to set
	 */
	public void setCopyInfoFormId(String copyInfoFormId) {
		this.copyInfoFormId = copyInfoFormId;
	}

}