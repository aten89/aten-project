package org.eapp.oa.meeting.blo.impl;

import java.util.Date;
import java.util.List;

import org.eapp.oa.meeting.blo.IMeetingParticipantBiz;
import org.eapp.oa.meeting.dao.IMeetingParticipantDAO;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.system.exception.OaException;

public class MeetingParticipantBiz implements IMeetingParticipantBiz {

	private IMeetingParticipantDAO meetingParticipantDAO;


	@Override
	public List<MeetingParticipant> findParticipantByTime(Date beginTime,
			Date EndTime) {
		// 查询时否某个人在同一时间段还有其他会议
		return null;
	}

	@Override
	public MeetingParticipant txAddMeetingParticipant(MeetingInfo meetingInfo,
			String participant, String email, String name, Integer type)
			throws OaException {
		if(type == null) {
			throw new IllegalArgumentException("非法参数:type参数为null");
		}
		MeetingParticipant person = new MeetingParticipant();
		if(MeetingParticipant.TYPE_IN_ADDRESS_BOOK == type) { //系统用户
			person.setParticipant(participant);
		} else if(MeetingParticipant.TYPE_NOT_IN_ADDRESS_BOOK == type) { //手动添加
			person.setName(name);
			person.setEmail(email);
		} else {
			throw new IllegalArgumentException("非法参数:type参数值非法");
		}
		person.setMeetingInfo(meetingInfo);
		person.setType(type);
		meetingParticipantDAO.save(person);
		
		return person;
	}

	@Override
	public MeetingParticipant txDelMeetingParticipant(String id)
			throws OaException {
		return null;
	}

	
	
	
	//setter method
	public void setMeetingParticipantDAO(
			IMeetingParticipantDAO meetingParticipantDAO) {
		this.meetingParticipantDAO = meetingParticipantDAO;
	}
}
