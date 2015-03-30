package org.eapp.oa.meeting.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.meeting.dao.IMeetingInfoDAO;
import org.eapp.oa.meeting.dto.MeetingInfoQueryParameters;
import org.eapp.oa.meeting.hbean.MeetingAttachment;
import org.eapp.oa.meeting.hbean.MeetingAttachmentId;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public class MeetingInfoDAO extends BaseHibernateDAO implements IMeetingInfoDAO {
	private static final Log log = LogFactory.getLog(MeetingInfoDAO.class);

	public MeetingInfo findById(java.lang.String id) {
		log.debug("getting MeetingInfo instance with id: " + id);
		try {
			MeetingInfo instance = (MeetingInfo) getSession().get(
					MeetingInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}


	@Override
	public ListPage<MeetingInfo> queryMeetingInfo(MeetingInfoQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}

		try {
			//"select m from MeetingInfo m join m.participants as mp where 1=1";
			StringBuffer hql = new StringBuffer(
					"from MeetingInfo m left join m.participants as mp where 1=1");
			// 拼接查询条件
			if (qp.getInputBeginDate() != null) {
				hql.append(" and m.beginTime >:inputBeginDate");
			}
			if (qp.getInputEndDate() != null) {
				hql.append(" and m.endTime <:inputEndDate");
			}
			if (qp.getAreaCode() != null) {
				hql.append(" and m.meetingRoom.areaCode = :areaCode");
			}
			if (qp.getRoomId() != null) {
				hql.append(" and m.meetingRoom.id =:roomId");
			}
			if (qp.getTheme() != null) {
				hql.append(" and m.theme like :theme");
				qp.toArountParameter("theme");
			}
			
			if (qp.getApplyMan() != null) {
				hql.append(" and m.applyMan = :applyMan");
			}
			if (qp.getParticipant() != null) {
				hql.append(" and mp.participant = :participant");
			}
			
			if (qp.isStart() != null) {
				if (qp.isStart()) {
					hql.append(" and m.beginTime<:currentTime");
					Timestamp currentTime = new Timestamp(new Date().getTime());
					qp.addParameter("currentTime", currentTime);
				} else {
					hql.append(" and m.beginTime>:currentTime");
					Timestamp currentTime = new Timestamp((new Date()).getTime());
					qp.addParameter("currentTime", currentTime);
				}
				qp.setStart(null);
			}
			StringBuffer sHql = new StringBuffer("select m1 from MeetingInfo as m1 where m1.id in (select distinct (m.id) ");
			sHql.append(hql).append(")");
			return new CommQuery<MeetingInfo>().queryListPage(qp, 
					qp.appendOrders(sHql, "m1"), 
					"select count(distinct m) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<MeetingInfo>();
		}
	}

	@Override
	public MeetingAttachment findMeetingAttachmentById(MeetingAttachmentId meetingAttachmentId) {
		try {
			MeetingAttachment instance = (MeetingAttachment) getSession().get(
					MeetingAttachment.class, meetingAttachmentId);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<MeetingInfo> getMeetRoomOrderTimes(String id, String roomId, Timestamp beginTime, Timestamp endTime){
		String hql ="from MeetingInfo as m where m.meetingRoom.id =:roomId and m.beginTime<:endTime and m.endTime >:beginTime";
		if (id != null) {
			hql += " and m.id <> :id";
		}
		try{
			Query queryObject = getSession().createQuery(hql);
			queryObject.setString("roomId", roomId);
			queryObject.setTimestamp("endTime", endTime);
			queryObject.setTimestamp("beginTime", beginTime);
			if (id != null) {
				queryObject.setString("id", id);
			}
//			Long value = (Long)queryObject.uniqueResult();
//			if(value == null){
//				return 0;
//			}
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("getCountByKnowledgeClass failed", re);
			throw re;
		}
	}
	
	public ListPage<MeetingInfo> queryMeetingOrderInfo(MeetingInfoQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException("非法参数:QP为空");
		}

		try {
			//"select m from MeetingInfo m join m.participants as mp where 1=1";
			StringBuffer hql = new StringBuffer(
					"from MeetingInfo m left join m.participants as mp where 1=1");
			// 拼接查询条件
			if (qp.getInputBeginDate() != null) {
				hql.append(" and m.beginTime <:inputEndDate");
			}
			if (qp.getInputEndDate() != null) {
				hql.append(" and m.endTime >:inputBeginDate");
			}
			StringBuffer sHql = new StringBuffer("select m1 from MeetingInfo as m1 where m1.id in (select distinct (m.id) ");
			sHql.append(hql).append(")");
			return new CommQuery<MeetingInfo>().queryListPage(qp, 
					qp.appendOrders(sHql, "m1"), 
					"select count(distinct m) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			re.printStackTrace();
			return new ListPage<MeetingInfo>();
		}
	}

	@Override
	public int ModifyMeetingRoomToNull(MeetingRoom meetingRoom) {
		if (meetingRoom == null) {
			throw new IllegalArgumentException("非法参数:meetingRoom为空");
		}
		try {
			String hql = "update MeetingInfo mi set mi.meetingRoom = null where mi.meetingRoom =:meetingRoom";
			Query queryObject = getSession().createQuery(hql);
			queryObject.setParameter("meetingRoom", meetingRoom);
			return queryObject.executeUpdate();
		} catch (RuntimeException re) {
			re.printStackTrace();
			return 0;
		}
		
	}
	
}