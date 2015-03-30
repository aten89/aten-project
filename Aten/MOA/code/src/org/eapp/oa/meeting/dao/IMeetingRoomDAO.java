package org.eapp.oa.meeting.dao;

import java.util.List;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public interface IMeetingRoomDAO extends IBaseHibernateDAO {

	public MeetingRoom findById(java.lang.String id);

	/**
	 * 根据区域代码搜索会议室数
	 * @param areaCode
	 * @return
	 */
	
	public List<MeetingRoom> findByAreaCode(String areaCode);
	
	
	/**
	 * 查找存在同一会议室的记录数
	 * @param areaCode
	 * @param roomName
	 * @return
	 */
	public int findSameMeetingRoomNameNum(String areaCode, String roomName);
	
	//自定义抽象方法
	
	public int getDisplayOrder();

}