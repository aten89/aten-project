package org.eapp.oa.meeting.hbean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.hbean.Attachment;

/**
 * MeetingInfo entity. Description:会议信息
 * 
 * @author sds
 */
@SuppressWarnings("unchecked")
public class MeetingInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1039740478937567805L;

	// Fields
	//ID_,VARCHAR2(36),不能为空         --主键ID
	private String id;
	//ROOMID_,VARCHAR2(36)           --会议室
	private MeetingRoom meetingRoom;
	//APPLYMAN_,VARCHAR2(128),       --预订人员
	private String applyMan;
	//GROUPNAME_,VARCHAR2(128)       --预订人员部门
	private String groupName;
	//RESERVETIME_",TIMESTAMP        --预订操作时间
	private Date reserveTime;
	//THEME_, VARCHAR2(128)          --会议主题
	private String theme;
	//CONTENT_,CLOB                  --会议内容
	private String content;
	//BEGINTIME_,TIMESTAMP           --会议开始时间
	private Date beginTime;
	//ENDTIME_,TIMESTAMP             --会议结束时间
	private Date endTime;
	//REMARK_,VARCHAR2(2048)         --备注
	private String remark;

	private Set<Attachment> meetingAttas = new HashSet<Attachment>(0);               	// 会议资料附件
	private Set<Attachment> meetingMinutes = new HashSet<Attachment>(0);               	// 会议纪要
	private Set<Attachment> meetingContentImags = new HashSet<Attachment>(0); 			// 内容图片
	private Set<MeetingParticipant> participants = new HashSet<MeetingParticipant>(0); // 参会人员

	// Constructors

	/** default constructor */
	public MeetingInfo() {
	}

	// Property accessors

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

	public String getApplyMan() {
		return applyMan;
	}

	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Date reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Set<Attachment> getMeetingMinutes() {
		return meetingMinutes;
	}

	public void setMeetingMinutes(Set<Attachment> meetingMinutes) {
		this.meetingMinutes = meetingMinutes;
	}

	public Set<MeetingParticipant> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<MeetingParticipant> participants) {
		this.participants = participants;
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

	public Set<Attachment> getMeetingContentImags() {
		return meetingContentImags;
	}

	public void setMeetingContentImags(Set<Attachment> meetingContentImags) {
		this.meetingContentImags = meetingContentImags;
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
		final MeetingInfo other = (MeetingInfo) obj;
		if (id == null) {
			if (other.getId() != null)
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
		sb.append("meetingRoom=");
		sb.append(getMeetingRoom());
		sb.append(",");
		sb.append("applyMan =");
		sb.append(getApplyMan());
		sb.append(",");
		sb.append("groupName=");
		sb.append(getGroupName());
		sb.append(",");
		sb.append("reserveTime=");
		sb.append(getReserveTime());
		sb.append(",");
		sb.append("theme=");
		sb.append(getTheme());
		sb.append(",");
		sb.append("content=");
		sb.append(getContent());
		sb.append(",");
		sb.append("beginTime=");
		sb.append(getBeginTime());
		sb.append(",");
		sb.append("endTime=");
		sb.append(getEndTime());
		sb.append(",");
		sb.append("remark=");
		sb.append(getRemark());
		sb.append(",");
		sb.append("participants=");
		sb.append(getParticipants());
		sb.append(",");
		sb.append(",");
		sb.append("meetingMinutes=");
		sb.append(getMeetingMinutes());
		sb.append("]");

		return sb.toString();
	}

	public Set<Attachment> getMeetingAttas() {
		return meetingAttas;
	}

	public void setMeetingAttas(Set<Attachment> meetingAttas) {
		this.meetingAttas = meetingAttas;
	}

	public String getApplyManDisplayName() {
		return UsernameCache.getDisplayName(applyMan);
	}

	/**
	 * 是否为参会人员
	 * @param userAccountId
	 * @return
	 */
	public boolean isParticipant(String userAccountId) {
		if (participants == null) {
			return false;
		}
		for (MeetingParticipant mp : participants) {
			if (MeetingParticipant.TYPE_IN_ADDRESS_BOOK == mp.getType() 
					&& userAccountId.equals(mp.getParticipant())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 会议是否结束
	 * @return
	 */
	public boolean isMeetingEnd() {
		return  endTime.before(new Date());
	}
	
	/**
	 * 会议是否开始
	 * @return
	 */
	public boolean isMeetingBegin() {
		return  beginTime.after(new Date());
	}

}