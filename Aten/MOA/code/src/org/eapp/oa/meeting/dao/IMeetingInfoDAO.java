package org.eapp.oa.meeting.dao;

import java.sql.Timestamp;
import java.util.List;

import org.eapp.oa.meeting.dto.MeetingInfoQueryParameters;
import org.eapp.oa.meeting.hbean.MeetingAttachment;
import org.eapp.oa.meeting.hbean.MeetingAttachmentId;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public interface IMeetingInfoDAO extends IBaseHibernateDAO {

	public MeetingInfo findById(java.lang.String id);

	/**
	 * 分页查询
	 * 
	 * @param qp
	 * @return
	 */
	public ListPage<MeetingInfo> queryMeetingInfo(MeetingInfoQueryParameters qp);
	
	/**
	 * 通过ID查找附附
	 * @param meetingAttachmentId
	 * @return
	 */
	public MeetingAttachment findMeetingAttachmentById(MeetingAttachmentId meetingAttachmentId);
	
	public List<MeetingInfo> getMeetRoomOrderTimes(String id, String roomId, Timestamp beginTime, Timestamp endTime);
	
	public ListPage<MeetingInfo> queryMeetingOrderInfo(MeetingInfoQueryParameters qp) ;
	
	/**
	 * 删除会议室时将meetingInfo中的会议室外键设置为null
	 * 
	 * @param meetingRoom
	 */
	public int ModifyMeetingRoomToNull(MeetingRoom meetingRoom);
}