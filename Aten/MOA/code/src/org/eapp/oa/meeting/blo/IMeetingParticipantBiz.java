package org.eapp.oa.meeting.blo;

import java.util.Date;
import java.util.List;

import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.system.exception.OaException;


/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public interface IMeetingParticipantBiz {

	/**
	 * 新增参会人员信息
	 * 
	 * @param meetingInfo
	 * @param participant
	 * @param email
	 * @param name
	 * @param type
	 * @return
	 * @throws OaException
	 */
	public MeetingParticipant txAddMeetingParticipant(MeetingInfo meetingInfo,
			String participant, String email, String name, Integer type)
			throws OaException;

	/**
	 * 删除参会人员信息
	 * 
	 * @param id
	 * @return
	 * @throws OaException
	 */
	public MeetingParticipant txDelMeetingParticipant(String id)
			throws OaException;

	/**
	 * 根据时间段查找参会人员，判断此人员是否在同一时间段内已有会议
	 * 
	 * @param beginTime
	 * @param EndTime
	 * @return
	 */
	public List<MeetingParticipant> findParticipantByTime(Date beginTime,
			Date EndTime);
}
