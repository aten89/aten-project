package org.eapp.oa.meeting.dao;

import java.util.List;

import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public interface IMeetingParticipantDAO extends IBaseHibernateDAO {

	public MeetingParticipant findById(java.lang.String id);

	public List<MeetingParticipant> findAll();

}