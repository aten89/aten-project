package org.eapp.oa.doc.hbean;

/**
 * DocClassAssign entity.
 * Description:公文类别授权
 * @author songdingsong
 */

public class DocClassAssign implements java.io.Serializable {

	public static final int TYPE_USER = 0;//用户
	public static final int TYPE_GROUP = 1;//群组
	public static final int TYPE_POST = 2;//职位
	
	
	private static final long serialVersionUID = 9087410332806073489L;
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//DocClassID_,VARCHAR2(512)                        --公文类别
	private DocClass docClass;
	//Type_,CHAR                                       --授权类型
	private int type;
	//AssignKey_,VARCHAR2(128)                         --授权键值
	private String assignKey;
	

	// Constructors

	/** default constructor */
	public DocClassAssign() {
	}


	// Property accessors
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DocClass getDocClass() {
		return docClass;
	}

	public void setDocClass(DocClass docClass) {
		this.docClass = docClass;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAssignKey() {
		return assignKey;
	}

	public void setAssignKey(String assignKey) {
		this.assignKey = assignKey;
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
		final DocClassAssign other = (DocClassAssign) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}