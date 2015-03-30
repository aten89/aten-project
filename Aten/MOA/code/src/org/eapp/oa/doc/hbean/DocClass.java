package org.eapp.oa.doc.hbean;

import java.util.HashSet;
import java.util.Set;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * DocClass entity.
 * Description:公文类别
 * @author songdingsong
 */

public class DocClass implements java.io.Serializable {
	public static final int TYPE_USER = 0;//用户
	public static final int TYPE_GROUP = 1;//群组
	public static final int TYPE_POST = 2;//职位
	
	public static final int FLAG_PUBLISH = 0;//发布受权
	public static final int FLAG_INFOMAN = 1;//管理受权
	
	public static final int DIS_FILE = 0;//公文
	public static final int COM_FILE = 1;//普通文件
//	
//	public static final int NO_ASSIGN = 0;//不需要
//	public static final int YES_ASSIGN = 1;//需要
//	public static final int OR_ASSIGN = 2;//可选
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5122033941859046902L;
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//Name_,VARCHAR2(128)                              --名称
	private String name;
	//FlowClass_,VARCHAR2(36)                          --流程类别
	private String flowClass;
	//DisplayOrder_,INTEGER                            --排序
	private Integer displayOrder;
	//Description_,VARCHAR2(1024)                      --描述
	private String description;
	//BodyTemplate_,VARCHAR2(36)                       --正文模板
	private Attachment bodyTemplate;
	//FILECLASS_,INTEGER                            --文件类别
	private Integer fileClass;
	//ASSIGNTYPE_,INTEGER                            --是否制定审批
//	private Integer assignType;
	
	private Set<DocClassAssign> docClassAssigns = new HashSet<DocClassAssign>(0);
	
	// Constructors

	/** default constructor */
	public DocClass() {
	}

//
//	public Integer getAssignType() {
//		return assignType;
//	}
//
//	public void setAssignType(Integer assignType) {
//		this.assignType = assignType;
//	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlowClass() {
		return flowClass;
	}

	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBodyTemplate(Attachment bodyTemplate) {
		this.bodyTemplate = bodyTemplate;
	}
	
	public Attachment getBodyTemplate() {
		return bodyTemplate;
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
		final DocClass other = (DocClass) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// 其它定义的方法
	public String getFlowClassName(){
		SysCodeDictLoader sys = SysCodeDictLoader.getInstance();
		DataDictInfo d = sys.getFlowClassByKey(flowClass);
		if( d == null){
			return null;
		}
		return d.getDictName();
	}
	
	public String getBodyTemplateUrl() {
		if (bodyTemplate == null) {
			return null;
		}
		return FileDispatcher.getAbsPath(bodyTemplate.getFilePath());
	}

	public Set<DocClassAssign> getDocClassAssigns() {
		return docClassAssigns;
	}

	public void setDocClassAssigns(Set<DocClassAssign> docClassAssigns) {
		this.docClassAssigns = docClassAssigns;
	}

	public Integer getFileClass() {
		return fileClass;
	}

	public void setFileClass(Integer fileClass) {
		this.fileClass = fileClass;
	}
}