package org.eapp.oa.meeting.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.meeting.dao.IMeetingRoomDAO;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public class MeetingRoomDAO extends BaseHibernateDAO implements IMeetingRoomDAO {
	private static final Log log = LogFactory.getLog(MeetingRoomDAO.class);

	public MeetingRoom findById(java.lang.String id) {
		log.debug("getting MeetingRoom instance with id: " + id);
		try {
			MeetingRoom instance = (MeetingRoom) getSession().get(
					MeetingRoom.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MeetingRoom> findByAreaCode(String areaCode) {
		log.debug("finding MeetingRoom by areaCode instances");
		try {
			String queryString = "from MeetingRoom l where 1=1" ;
			if(areaCode!=null && !areaCode.trim().equals("")){
				queryString+=" and l.areaCode=:areaCode";
			}
			queryString+=" order by l.areaCode,l.displayOrder";
			Query queryObject = getSession().createQuery(queryString);
			if(areaCode!=null && !areaCode.trim().equals("")){
				queryObject.setParameter("areaCode", areaCode);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find MeetingRoom by areaCode failed", re);
			throw re;
		}
	}

	@Override
	public int getDisplayOrder() {
		try {
			String queryString = "select max(l.displayOrder) from MeetingRoom l";
			Query queryObject = getSession().createQuery(queryString);
			Integer value = (Integer) queryObject.uniqueResult();
			if (value == null) {
				return 0;
			}
			return value.intValue();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public int findSameMeetingRoomNameNum(String areaCode, String roomName) {
		log.debug("finding MeetingRoom repeat");
		try {
			String queryString = "select count(*) from MeetingRoom r where r.areaCode=:areaCode and name=:roomName";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter("areaCode", areaCode);
			queryObject.setParameter("roomName", roomName);
			Long count = (Long) queryObject.uniqueResult();
			return count.intValue();
		} catch (RuntimeException re) {
			log.error("find MeetingRoom repeat failed", re);
			throw re;
		}
	}
}