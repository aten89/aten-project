package org.eapp.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * ActorAccount entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ActorAccount implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3454629070220500879L;
	
	public static final String CHANGEPASSWORD_FALSE = "N";//不允许修改密码
	public static final String CHANGEPASSWORD_TRUE = "Y";//允许修改密码
	
	//Actor_ID VARCHAR2(36) not null －－接口帐号ID
	private String actorID;
	//AccountID_ VARCHAR2(100) not null －－帐号
	private String accountID;
	//DisplayName_ VARCHAR2(100) not null－－显示名称
	private String displayName;
	//Credence_ BLOB －－认证文件
	private String credence;
	//IsLock_ SMALLINT not null －－是否锁定
	private Boolean isLock;
	//ChangePasswordFlag_ CHAR(1) not null －－是否允许修改
	private String changePasswordFlag;
	//CreateDate_ DATE not null －－创建时间
	private Date createDate;
	//InvalidDate_ DATE －－失效时间
	private Date invalidDate;
	//IsLogicDelete_ SMALLINT not null －－删除标志
	private Boolean isLogicDelete = Boolean.FALSE;
	//Description_ VARCHAR2(1024) －－描述
	private String description;
	private Set<Service> services = new HashSet<Service>(0);
	private Set<Service> validServices = new HashSet<Service>(0);
	
	// Constructors

	/** default constructor */
	public ActorAccount() {
	}

	public ActorAccount(String actorID) {
		this.actorID = actorID;
	}
	
	/** minimal constructor */
	public ActorAccount(String accountID, String displayName, Boolean isLock,
			String changePasswordFlag, Date createDate, Boolean isLogicDelete) {
		this.accountID = accountID;
		this.displayName = displayName;
		this.isLock = isLock;
		this.changePasswordFlag = changePasswordFlag;
		this.createDate = createDate;
		this.isLogicDelete = isLogicDelete;
	}

	/** full constructor */
	public ActorAccount(String accountID, String displayName, String credence,
			Boolean isLock, String changePasswordFlag, Date createDate,
			Date invalidDate, Boolean isLogicDelete, String description,
			Set<Service> services) {
		this.accountID = accountID;
		this.displayName = displayName;
		this.credence = credence;
		this.isLock = isLock;
		this.changePasswordFlag = changePasswordFlag;
		this.createDate = createDate;
		this.invalidDate = invalidDate;
		this.isLogicDelete = isLogicDelete;
		this.description = description;
		this.services = services;
	}

	// Property accessors

	public String getActorID() {
		return this.actorID;
	}

	public void setActorID(String actorID) {
		this.actorID = actorID;
	}

	public String getAccountID() {
		return this.accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCredence() {
		return this.credence;
	}

	public void setCredence(String credence) {
		this.credence = credence;
	}

	public Boolean getIsLock() {
		return this.isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public String getChangePasswordFlag() {
		return this.changePasswordFlag;
	}

	public void setChangePasswordFlag(String changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}

	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public Boolean getIsLogicDelete() {
		return this.isLogicDelete;
	}

	public void setIsLogicDelete(Boolean isLogicDelete) {
		this.isLogicDelete = isLogicDelete;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JSON(serialize=false)
	public Set<Service> getServices() {
		return this.services;
	}
	
	public void setServices(Set<Service> services) {
		this.services = services;
	}
	
	@JSON(serialize=false)
	public Set<Service> getValidServices() {
		return validServices;
	}

	public void setValidServices(Set<Service> validServices) {
		this.validServices = validServices;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actorID == null) ? 0 : actorID.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		final ActorAccount other = (ActorAccount) obj;
		if (actorID == null) {
			if (other.actorID != null)
				return false;
		} else if (!actorID.equals(other.actorID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer(this.getClass().getName());
		str.append("[")
				.append("actorID=").append(getActorID())
				.append(",accountID=").append(getAccountID())
				.append(",displayName=").append(getDisplayName())
				.append(",isLock=").append(getIsLock())
				.append(",changePasswordFlag=").append(getChangePasswordFlag())
				.append(",createDate=").append(getCreateDate())
				.append(",invalidDate=").append(getInvalidDate())
				.append(",isLogicDelete=").append(getIsLogicDelete())
				.append(",description=").append(getDescription())
				.append("]");
		return str.toString();
	}
}