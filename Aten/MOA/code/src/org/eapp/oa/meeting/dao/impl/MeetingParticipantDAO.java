package org.eapp.oa.meeting.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.meeting.dao.IMeetingParticipantDAO;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.hibernate.Query;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public class MeetingParticipantDAO extends BaseHibernateDAO implements IMeetingParticipantDAO  {
    private static final Log log = LogFactory.getLog(MeetingParticipantDAO.class);

    public MeetingParticipant findById( java.lang.String id) {
        log.debug("getting MeetingParticipant instance with id: " + id);
        try {
            MeetingParticipant instance = (MeetingParticipant) getSession()
                    .get(MeetingParticipant.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

	@SuppressWarnings("unchecked")
	public List<MeetingParticipant> findAll() {
		log.debug("finding all MeetingParticipant instances");
		try {
			String queryString = "from MeetingParticipant";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}