package org.eapp.oa.info.hbean;

/**
 * InfoLayoutAssign entity.
 * Description:类别授权
 * @author MyEclipse Persistence Tools
*/

public class InfoLayoutAssign implements java.io.Serializable {

	public static final int TYPE_USER = 0;//用户
	public static final int TYPE_GROUP = 1;//群组
	public static final int TYPE_POST = 2;//职位
	
	public static final int FLAG_PUBLISH = 0;//发布受权
	public static final int FLAG_INFOMAN = 1;//管理受权
	
	
	private static final long serialVersionUID = 8625264011663929272L;
	
	
	//ID_,VARCHAR2(36),不能为空                             --主键ID
	private String id;
	//InfoLayoutID_,VARCHAR2(36)                        --信息类别
	private InfoLayout infoLayout;
	//Type_,CHAR                            			--授权类型
	private int type;
	//AssignKey_,VARCHAR2(128)                          --授权键值
	private String assignKey;

	private int flag;//发布权限还是管理权限

	
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
	 * @return the infoLayout
	 */
	public InfoLayout getInfoLayout() {
		return infoLayout;
	}

	/**
	 * @param infoLayout the infoLayout to set
	 */
	public void setInfoLayout(InfoLayout infoLayout) {
		this.infoLayout = infoLayout;
	}

	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the assignKey
	 */
	public String getAssignKey() {
		return assignKey;
	}

	/**
	 * @param assignKey the assignKey to set
	 */
	public void setAssignKey(String assignKey) {
		this.assignKey = assignKey;
	}
	public InfoLayoutAssign(){}
	public InfoLayoutAssign(String id, InfoLayout infoLayout, int type,
			String assignKey) {
		super();
		this.id = id;
		this.infoLayout = infoLayout;
		this.type = type;
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
		final InfoLayoutAssign other = (InfoLayoutAssign) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}