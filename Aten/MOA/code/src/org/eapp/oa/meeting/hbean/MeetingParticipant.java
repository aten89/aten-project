package org.eapp.oa.meeting.hbean;

import org.eapp.client.util.UsernameCache;

/**
 * MeetingParticipant entity. Description:参会人员
 * 
 * @author sds
 */

public class MeetingParticipant implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields
	
	public static final int TYPE_IN_ADDRESS_BOOK = 0; //系统用户
	public static final int TYPE_NOT_IN_ADDRESS_BOOK= 1; //手动添加
	
	
	// ID_,VARCHAR2(36),不能为空     --主键ID
	private String id;
	//MEETINGID_,VARCHAR2(36)      --会议ID
	private MeetingInfo meetingInfo;
	//PARTICIPANT_,VARCHAR2(128)   --参与者 以后有通讯录时，改为通讯录表的外键
	private String participant;
	//EMAIL_,VARCHAR2(64)          --邮箱地址
	private String email;
	//NAME_,VARCHAR2(128)          --姓名
	private String name;
	//TYPE_,SMALLINT               --类型 0：系统用户 1：手动添加的用户
	private Integer type;

	// Constructors

	/** default constructor */
	public MeetingParticipant() {
	}

	/** full constructor */
	public MeetingParticipant(MeetingInfo meetingInfo, String participant,
			String email, String name, Integer type) {
		this.meetingInfo = meetingInfo;
		this.participant = participant;
		this.email = email;
		this.name = name;
		this.type = type;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParticipant() {
		return this.participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
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
		final MeetingParticipant other = (MeetingParticipant) obj;
		if (id == null) {
	//		if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("id=");
		sb.append(getId());
		sb.append(",");
		sb.append("meetingInfo=");
		sb.append(getMeetingInfo());
		sb.append(",");
		sb.append("participant =");
		sb.append(getParticipant());
		sb.append(",");
		sb.append("email=");
		sb.append(getEmail());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("type=");
		sb.append(getType());
		sb.append("]");

		return sb.toString();
	}

	public MeetingInfo getMeetingInfo() {
		return meetingInfo;
	}

	public void setMeetingInfo(MeetingInfo meetingInfo) {
		this.meetingInfo = meetingInfo;
	}
	
	public String getParticipantName() {
		return UsernameCache.getDisplayName(participant);
	}
	
	public String getPersonName() {
		if (type == TYPE_IN_ADDRESS_BOOK) {
			return getParticipantName();
		} else if (type == TYPE_NOT_IN_ADDRESS_BOOK) {
			return getName();
		} else{
			return null;
		}
	}
	
	
	public String getAccountOrEmail() {
		if (type == TYPE_IN_ADDRESS_BOOK) {
			return getParticipant();
		} else if (type == TYPE_NOT_IN_ADDRESS_BOOK) {
			return getEmail();
		} else{
			return null;
		}
	}
	
}