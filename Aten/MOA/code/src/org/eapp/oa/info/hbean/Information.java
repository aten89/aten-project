package org.eapp.oa.info.hbean;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * InfoLayoutAssign entity.
 * Description:信息
 * @author MyEclipse Persistence Tools
*/

public class Information implements java.io.Serializable {

	public static final int DISPLAYMODE_URL = 0;//内容地址
	public static final int DISPLAYMODE_CONTENT = 1;//直接显示内容
//	public static final int DISPLAYMODE_CSMURL = 2; //CSM链接地址
	
	public static final int STATUS_UNPUBLISH = 0;//未发布
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_PUBLISH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	
	
	public static final int PROPERTY_TOTOP = 0;//置顶
	public static final int PROPERTY_COMMON = 1;//普通
	public static final int PROPERTY_SHIELD = 2;//屏蔽
	public static final int PROPERTY_DELETE = 3;//删除
	
	private static Map<Integer , String> statusNamingMap = new HashMap<Integer , String>();
	private static Map<Integer , String> propertyNamingMap = new HashMap<Integer , String>();
	static{
		statusNamingMap.put(STATUS_UNPUBLISH, "未发布");
		statusNamingMap.put(STATUS_APPROVAL, "审批中");
		statusNamingMap.put(STATUS_PUBLISH, "已发布");
		
		propertyNamingMap.put(PROPERTY_TOTOP, "置顶");
		propertyNamingMap.put(PROPERTY_COMMON, "普通");
		propertyNamingMap.put(PROPERTY_SHIELD, "屏蔽");
	}
	private static final long serialVersionUID = 8625264011663929272L;
	
	
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//SUBJECT_,VARCHAR2(256)                        	--标题
	private String subject;
	//INFOLAYOUT_,VARCHAR2(128)                         --板块
	private String infoLayout;
	//INFOCLASS_,VARCHAR2(128)                          --信息分类
	private String infoClass;					
	//CONTENT_,CLOB                          			--内容文件
	private String content;	
	//CONTENTURL_,VARCHAR2(512)                         --内容地址
	private String contentUrl;
	//DISPLAYMODE_,INTEGER                       		--显示模式
	private Integer displayMode;
	//INFOSTATUS_,INTEGER                       		--信息状态
	private Integer infoStatus;
	//INFOPROPERTY_,INTEGER                       		--信息属性
	private Integer infoProperty;
	//GROUPNAME_,VARCHAR2(128)                          --所属机构
	private String groupName;	
	//DRAFTSMAN_,VARCHAR2(128)                          --起草人
	private String draftsMan;
	//DRAFTDATE_,VARCHAR2(128)                          --起草时间
	private Date draftDate;
	//INVALIDDATE_,VARCHAR2(128)                        --失效时间
	private Date invalidDate;//word控件档住时间选择器，去除不用
	//PUBLICDATE_,VARCHAR2(128)                         --发布时间
	private Date publicDate;
	
	private String subjectColor; 
		
	private Set<Attachment> attachments = new HashSet<Attachment>(0);
	
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
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}



	/**
	 * @return the infoLayout
	 */
	public String getInfoLayout() {
		return infoLayout;
	}

	/**
	 * @param infoLayout the infoLayout to set
	 */
	public void setInfoLayout(String infoLayout) {
		this.infoLayout = infoLayout;
	}

	/**
	 * @return the infoClass
	 */
	public String getInfoClass() {
		return infoClass;
	}

	/**
	 * @param infoClass the infoClass to set
	 */
	public void setInfoClass(String infoClass) {
		this.infoClass = infoClass;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the contentUrl
	 */
	public String getContentUrl() {
		return contentUrl;
	}

	/**
	 * @param contentUrl the contentUrl to set
	 */
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getContentAccUrl() {
		if (contentUrl == null) {
			return null;
		}
		return FileDispatcher.getAbsPath(contentUrl);
	}
			
	/**
	 * @return the displayMode
	 */
	public Integer getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode the displayMode to set
	 */
	public void setDisplayMode(Integer displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @return the infoStatus
	 */
	public Integer getInfoStatus() {
		return infoStatus;
	}

	/**
	 * @param infoStatus the infoStatus to set
	 */
	public void setInfoStatus(Integer infoStatus) {
		this.infoStatus = infoStatus;
	}

	/**
	 * @return the infoProperty
	 */
	public Integer getInfoProperty() {
		return infoProperty;
	}

	/**
	 * @param infoProperty the infoProperty to set
	 */
	public void setInfoProperty(Integer infoProperty) {
		this.infoProperty = infoProperty;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the draftsMan
	 */
	public String getDraftsMan() {
		return draftsMan;
	}

	/**
	 * @param draftsMan the draftsMan to set
	 */
	public void setDraftsMan(String draftsMan) {
		this.draftsMan = draftsMan;
	}

	public String getDraftsManName() {
		return UsernameCache.getDisplayName(draftsMan);
	}
	
	/**
	 * @return the draftDate
	 */
	public Date getDraftDate() {
		return draftDate;
	}

	/**
	 * @param draftDate the draftDate to set
	 */
	public void setDraftDate(Date draftDate) {
		this.draftDate = draftDate;
	}

	/**
	 * @return the invalidDate
	 */
	public Date getInvalidDate() {
		return invalidDate;
	}

	/**
	 * @param invalidDate the invalidDate to set
	 */
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	/**
	 * @return the publicDate
	 */
	public Date getPublicDate() {
		return publicDate;
	}

	/**
	 * @param publicDate the publicDate to set
	 */
	public void setPublicDate(Date publicDate) {
		this.publicDate = publicDate;
	}

	/**
	 * @return the attachments
	 */
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	public String getInfoStatusName(){
		return statusNamingMap.get(infoStatus);
	}
	
	public String getInfoPropertyName(){
		return propertyNamingMap.get(infoProperty);
	}
	
	public String getSubjectColor() {
		return subjectColor;
	}

	public void setSubjectColor(String subjectColor) {
		this.subjectColor = subjectColor;
	}

	public Information(){}
	
	public Information(String id, String subject, String infoLayout,
			String infoClass, String content, String contentUrl,
			Integer displayMode, Integer infoStatus,
			Integer infoProperty, String groupName, String draftsMan,
			Date draftDate, Date invalidDate, Date publicDate,
			Set<Attachment> attachments) {
		super();
		this.id = id;
		this.subject = subject;
		this.infoLayout = infoLayout;
		this.infoClass = infoClass;
		this.content = content;
		this.contentUrl = contentUrl;
		this.displayMode = displayMode;
		this.infoStatus = infoStatus;
		this.infoProperty = infoProperty;
		this.groupName = groupName;
		this.draftsMan = draftsMan;
		this.draftDate = draftDate;
		this.invalidDate = invalidDate;
		this.publicDate = publicDate;
		this.attachments = attachments;
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
		final Information other = (Information) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}