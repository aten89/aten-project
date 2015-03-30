package org.eapp.oa.address.hbean;

import java.io.Serializable;
import java.util.Date;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.util.TransformTool;
import org.eapp.util.web.upload.FileDispatcher;



public class AddressList implements Serializable, Comparable<AddressList>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3188142949761929618L;

	// 主键
	private String id;

	//帐号id
	private String userAccountId;
	
	//工号
	private String employeeNumber;
	
	//座位号
	private String seatNumber;
	
	//入司时间
	private Date userEnterCorpDate;
	
	//移动电话
	private String userMobile;
	
	//办公电话
	private String userOfficeTel;
	
	//E-mail
	private String userEmail;

	//QQ
	private String userQQ;

	//用户msn帐号
	private String userMSN;
	
	
	//昵称
	private String userNickName;
	
	//性别
	private String userSex;
	
	//出生日期
	private Date userBirthDate;
	
	//民族
	private String userNativePlace;
	
	//籍贯
	private String userNation;
	
	//通讯地址
	private String userCommAddr;
	
	//通讯地址邮编
	private String zipCode;
	
	//家庭住址
	private String userHomeAddr;
	
	//家庭电话
	private String userHomeTel;
	
	//备注
	private String remark;
	
	//照片,存照片路径
	private String userPhoto;
	
	/**
	 * default constructor
	 */
	public AddressList(){
		
	}

	
	public AddressList(String id, String userAccountId, String employeeNumber,
			String seatNumber, Date userEnterCorpDate, String userMobile,
			String userOfficeTel, String userEmail, String userQQ,
			String userMSN, String userNickName, String userSex,
			Date userBirthDate, String userNativePlace, String userNation,
			String userCommAddr, String zipCode, String userHomeAddr, String userHomeTel,
			String remark, String userPhoto) {
		this.id = id;
		this.userAccountId = userAccountId;
		this.employeeNumber = employeeNumber;
		this.seatNumber = seatNumber;
		this.userEnterCorpDate = userEnterCorpDate;
		this.userMobile = userMobile;
		this.userOfficeTel = userOfficeTel;
		this.userEmail = userEmail;
		this.userQQ = userQQ;
		this.userMSN = userMSN;
		this.userNickName = userNickName;
		this.userSex = userSex;
		this.userBirthDate = userBirthDate;
		this.userNativePlace = userNativePlace;
		this.userNation = userNation;
		this.userCommAddr = userCommAddr;
		this.zipCode = zipCode;
		this.userHomeAddr = userHomeAddr;
		this.userHomeTel = userHomeTel;
		this.remark = remark;
		this.userPhoto = userPhoto;
	}


	public String getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(String id) {
		this.id = id;
	}

	public String getUserAccountId() {
		return userAccountId;
	}
	public String getUserName() {
		return UsernameCache.getDisplayName(userAccountId);
	}
	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Date getUserEnterCorpDate() {
		return userEnterCorpDate;
	}

	public void setUserEnterCorpDate(Date userEnterCorpDate) {
		this.userEnterCorpDate = userEnterCorpDate;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserOfficeTel() {
		return userOfficeTel;
	}

	public void setUserOfficeTel(String userOfficeTel) {
		this.userOfficeTel = userOfficeTel;
	}

	public String getUserEmail() {
//		if (userEmail == null) {
//			userEmail = TransformTool.getEmail(userAccountId);
//		}
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserQQ() {
		return userQQ;
	}

	public void setUserQQ(String userQQ) {
		this.userQQ = userQQ;
	}

	public String getUserMSN() {
		return userMSN;
	}

	public void setUserMSN(String userMSN) {
		this.userMSN = userMSN;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public Date getUserBirthDate() {
		return userBirthDate;
	}

	public void setUserBirthDate(Date userBirthDate) {
		this.userBirthDate = userBirthDate;
	}

	public String getUserNativePlace() {
		return userNativePlace;
	}

	public void setUserNativePlace(String userNativePlace) {
		this.userNativePlace = userNativePlace;
	}

	public String getUserNation() {
		return userNation;
	}

	public void setUserNation(String userNation) {
		this.userNation = userNation;
	}

	public String getUserCommAddr() {
		return userCommAddr;
	}

	public void setUserCommAddr(String userCommAddr) {
		this.userCommAddr = userCommAddr;
	}

	
	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getUserHomeAddr() {
		return userHomeAddr;
	}

	public void setUserHomeAddr(String userHomeAddr) {
		this.userHomeAddr = userHomeAddr;
	}

	public String getUserHomeTel() {
		return userHomeTel;
	}

	public void setUserHomeTel(String userHomeTel) {
		this.userHomeTel = userHomeTel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserPhoto() {
		return userPhoto;
	}
	
	public String getUserPhotoPath() {
		return FileDispatcher.getAbsPath(userPhoto);
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getUserDeptName() {
		return TransformTool.getDisplayGroupName(userAccountId);
	}

	public String getUserPostName() {
		return TransformTool.getDisplayPostName(userAccountId);
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userAccountId == null) ? 0 : userAccountId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AddressList other = (AddressList) obj;
		if (userAccountId == null) {
			if (other.userAccountId != null)
				return false;
		} else if (!userAccountId.equals(other.userAccountId))
			return false;
		return true;
	}

	public int compareTo(AddressList al){
		return this.userAccountId.compareTo(al.userAccountId);
	}	
}