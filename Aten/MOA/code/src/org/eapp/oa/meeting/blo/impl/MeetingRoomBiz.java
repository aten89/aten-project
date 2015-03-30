package org.eapp.oa.meeting.blo.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.oa.meeting.blo.IMeetingRoomBiz;
import org.eapp.oa.meeting.dao.IMeetingInfoDAO;
import org.eapp.oa.meeting.dao.IMeetingRoomDAO;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.exception.OaException;

public class MeetingRoomBiz implements IMeetingRoomBiz {

	private IMeetingRoomDAO meetingRoomDAO;
	private IMeetingInfoDAO meetingInfoDAO;
	

	@Override
	public List<MeetingRoom> getMeetingRoomByAreaCode(String areaCode) throws OaException{
		return meetingRoomDAO.findByAreaCode(areaCode);
	}

	@Override
	public MeetingRoom txAddMeetingRoom(String areaCode, String name, Integer seatNum,
			String environment, Integer powerNum, Integer cableNum,
			Integer lineNum, String phoneNumber, Boolean status, String remark)
			throws OaException {
		if (areaCode == null || "".equals(areaCode)) {
			throw new IllegalArgumentException("非法参数:所属地区不能为空！");
		}
		if (name == null || "".equals(name)) {
			throw new IllegalArgumentException("非法参数:会议室名称不能为空！");
		}
		// 判断会议室名称是否重复
		isMeetingRoomNameRepeat(areaCode, name);
		
		MeetingRoom meetingRoom = new MeetingRoom();
		meetingRoom.setAreaCode(areaCode);
		meetingRoom.setName(name);
		meetingRoom.setSeatNum(seatNum);
		meetingRoom.setEnvironment(environment);
		meetingRoom.setPowerNum(powerNum);
		meetingRoom.setCableNum(cableNum);
		meetingRoom.setLineNum(lineNum);
		meetingRoom.setPhoneNumber(phoneNumber);
		meetingRoom.setStatus(status);
		meetingRoom.setRemark(remark);
		//避免新增以后还得重新再排序。所以设置的序号要等于当前数据库里属于该区域的会议室的记录数+1
		List<MeetingRoom> rooms = meetingRoomDAO.findByAreaCode(areaCode);
		meetingRoom.setDisplayOrder(rooms == null ? 1 : rooms.size() + 1);
		meetingRoomDAO.save(meetingRoom);
		return meetingRoom;
	}

	@Override
	public MeetingRoom txDelMeetingRoom(String id) throws OaException {
		if (id == null) {
			return null;
		}
		MeetingRoom meetingRoom = meetingRoomDAO.findById(id);
		if (meetingRoom == null) {
			return null;
		}
		//将已存在的会议信息的会议室外键设为null
		meetingInfoDAO.ModifyMeetingRoomToNull(meetingRoom);
		
		meetingRoomDAO.delete(meetingRoom);
		return meetingRoom;
	}

	@Override
	public MeetingRoom txModifyMeetingRoom(String id, String areaCode, String name,
			Integer seatNum, String environment, Integer powerNum,
			Integer cableNum, Integer lineNum, String phoneNumber,
			Boolean status, String remark) throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		if (areaCode == null || "".equals(areaCode)) {
			throw new IllegalArgumentException("非法参数:所属地区不能为空！");
		}
		if (name == null || "".equals(name)) {
			throw new IllegalArgumentException("非法参数:会议室名称不能为空！");
		}
		MeetingRoom meetingRoom = meetingRoomDAO.findById(id);
		if (meetingRoom == null) {
			throw new OaException("会议室不存在");
		}
		
		// 判断会议室名称是否重复
		if (!areaCode.equals(meetingRoom.getAreaCode()) || !name.equals(meetingRoom.getName())) {
			isMeetingRoomNameRepeat(areaCode, name);
		}
		
		meetingRoom.setAreaCode(areaCode);
		meetingRoom.setName(name);
		meetingRoom.setSeatNum(seatNum);
		meetingRoom.setEnvironment(environment);
		meetingRoom.setPowerNum(powerNum);
		meetingRoom.setCableNum(cableNum);
		meetingRoom.setLineNum(lineNum);
		meetingRoom.setPhoneNumber(phoneNumber);
		meetingRoom.setStatus(status);
		meetingRoom.setRemark(remark);
		meetingRoomDAO.saveOrUpdate(meetingRoom);
		
		return meetingRoom;
	}
	

	@Override
	public void txSaveOrder(String[] meetingRoomIds) {
		int order = 1;
		for (int i = 0; i < meetingRoomIds.length; i++) {
			if (StringUtils.isBlank(meetingRoomIds[i])) {
				continue;
			}
			MeetingRoom meetingRoom = meetingRoomDAO.findById(meetingRoomIds[i]);
			if (meetingRoom == null) {
				continue;
			}
			meetingRoom.setDisplayOrder(order);
			meetingRoomDAO.update(meetingRoom);
			
			order++;
		}
	}
	

	/**
	 * 验证会议室名称是否重复
	 * 
	 * @param areaCode 所属地区
	 * @param name 会议室名称
	 * @throws OaException
	 */
	private void isMeetingRoomNameRepeat(String areaCode, String name) throws OaException {
		if (meetingRoomDAO.findSameMeetingRoomNameNum(areaCode, name) > 0) {
			throw new OaException("会议室名称不能重复!");
		}
	}
	
	@Override
	public MeetingRoom getMeetingRoomById(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		return meetingRoomDAO.findById(id);
	}
	
	
	//setter method
	public void setMeetingRoomDAO(IMeetingRoomDAO meetingRoomDAO) {
		this.meetingRoomDAO = meetingRoomDAO;
	}

	public void setMeetingInfoDAO(IMeetingInfoDAO meetingInfoDAO) {
		this.meetingInfoDAO = meetingInfoDAO;
	}

}
