package org.eapp.oa.meeting.blo;

import java.util.List;

import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.exception.OaException;


/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public interface IMeetingRoomBiz {

	/**
	 * 新增会议室
	 * 
	 * @param areaCode 所属地区
	 * @param name
	 * @param seatNum
	 * @param environment
	 * @param powerNum
	 * @param cableNum
	 * @param lineNum
	 * @param phoneNumber
	 * @param status
	 * @param remark
	 * @return
	 * @throws OaException
	 */
	public MeetingRoom txAddMeetingRoom(String areaCode, String name, Integer seatNum,
			String environment, Integer powerNum, Integer cableNum,
			Integer lineNum, String phoneNumber, Boolean status, String remark)
			throws OaException;

	/**
	 * 删除会议室
	 * 
	 * @param id
	 * @return
	 * @throws OaException
	 */
	public MeetingRoom txDelMeetingRoom(String id) throws OaException;

	/**
	 * 修改会议室
	 * 
	 * @param areaCode 所属地区
	 * @param name
	 * @param seatNum
	 * @param environment
	 * @param powerNum
	 * @param cableNum
	 * @param lineNum
	 * @param phoneNumber
	 * @param status
	 * @param remark
	 * @return
	 * @throws OaException
	 */
	public MeetingRoom txModifyMeetingRoom(String id, String areaCode, String name,
			Integer seatNum, String environment, Integer powerNum,
			Integer cableNum, Integer lineNum, String phoneNumber,
			Boolean status, String remark) throws OaException;

	/**
	 * 查询会议室
	 * @param areaCode 所属地区
	 * @return
	 */
	public List<MeetingRoom> getMeetingRoomByAreaCode(String areaCode) throws OaException ;
	
	/**
	 * 排序
	 * 
	 * @param MeetingRoomIds
	 */
	public void txSaveOrder(String[] MeetingRoomIds);
	
	/**
	 * 根据id获得MeetingRoom
	 * 
	 * @param id
	 * @return
	 */
	public MeetingRoom getMeetingRoomById(String id); 
}
