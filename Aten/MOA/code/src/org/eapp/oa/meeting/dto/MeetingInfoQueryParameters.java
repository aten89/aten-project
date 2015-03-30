package org.eapp.oa.meeting.dto;

import java.sql.Timestamp;

import org.eapp.util.hibernate.QueryParameters;


/**
 * 会议信息查询
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public class MeetingInfoQueryParameters extends QueryParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	public void setId(String id) {
		this.addParameter("id", id);
	}

	public String getId() {
		return (String) this.getParameter("id");
	}

	//会议室所属地区
	public void setAreaCode(String areaCode) {
		if (areaCode != null && !"".equals(areaCode)) {
			this.addParameter("areaCode", areaCode);
		}
	}

	public String getAreaCode() {
		return (String) this.getParameter("areaCode");
	}
	
	// 输入的会议开始、结束时间 InputBeginDate、InputEndDate String FALSE FALSE FALSE
	public void setInputBeginDate(Timestamp inputBeginDate) {
		this.addParameter("inputBeginDate", inputBeginDate);
	}

	public Timestamp getInputBeginDate() {
		return (Timestamp) this.getParameter("inputBeginDate");
	}

	public void setInputEndDate(Timestamp inputEndDate) {
		this.addParameter("inputEndDate", inputEndDate);
	}

	public Timestamp getInputEndDate() {
		return (Timestamp) this.getParameter("inputEndDate");
	}

	//会议室ID
	public void setRoomId(String roomId){
		this.addParameter("roomId", roomId);
	}
	
	public String getRoomId(){
		return (String)this.getParameter("roomId");
	}
	
	//会议主题
	public void setTheme(String theme){
		this.addParameter("theme", theme);
	}
	
	public String getTheme() {
		return (String)this.getParameter("theme");
	}
	
	//参与人员
	public String getParticipant() {
		return (String)this.getParameter("participant");
	}
	
	public void setParticipant(String participant) {
		this.addParameter("participant", participant);
	}
	
	//申请人员
	public void setApplyMan(String applyMan) {
		this.addParameter("applyMan", applyMan);
	}
	
	public String getApplyMan() {
		return (String)this.getParameter("applyMan");
	}
	
	//是否已开始
	public Boolean isStart(){
		return (Boolean)this.getParameter("start");
	}
	public void setStart(Boolean start){
		this.addParameter("start", start);
	}
}
