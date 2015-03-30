package org.eapp.oa.meeting.dto;

import java.util.Collection;
import java.util.List;

import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.dto.HTMLOptionsDTO;

public class MeetingRoomSelect extends HTMLOptionsDTO {
	
	private List<MeetingRoom> meetingRooms;
	private boolean isOrder;
	
	public MeetingRoomSelect(List<MeetingRoom> meetingRooms, boolean isOrder){
		this.meetingRooms=meetingRooms;
		this.isOrder = isOrder;
	}
	
	public Collection<MeetingRoom> getMeetingRooms() {
		return meetingRooms;
	}

	public void setMeetingRooms(List<MeetingRoom> meetingRooms) {
		this.meetingRooms = meetingRooms;
	}
	
	/**
	 * 格式：
	 * <div>8a283d001a52213f011a5255d2f60002**dsfdf</div>
	 * <div>zxt00001**datacenter</div>
	 * <div>004**matural</div>
	 */
	public String toString() {
		
		StringBuffer out = new StringBuffer();
		if (meetingRooms == null || meetingRooms.size() < 1) {
			return out.toString();
		}
		for (MeetingRoom g : meetingRooms) {
			if (isOrder && !g.getStatus()) {//过滤可被预订的
				continue;
			}
			out.append(createOption(g.getId(),g.getName()));
		}
		return out.toString();
	}
}
