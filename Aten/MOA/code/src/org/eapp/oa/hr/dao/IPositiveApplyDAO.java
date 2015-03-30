package org.eapp.oa.hr.dao;

import java.util.List;

import org.eapp.oa.hr.dto.PositiveQueryParameters;
import org.eapp.oa.hr.hbean.PositiveApply;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;


public interface IPositiveApplyDAO extends IBaseHibernateDAO {

	public PositiveApply findById(java.lang.String id);

	public List<PositiveApply> findPositiveApplys(String userAccountId, int formStatus);
	
	public List<PositiveApply> getDealingPositiveApply(String userAccountId, List<String> userRoles);
	
	public ListPage<PositiveApply> getTrackOrArchPositiveApply(PositiveQueryParameters rqp,
			String userAccountId,Boolean isArch);
	
	public String getMaxID();
}