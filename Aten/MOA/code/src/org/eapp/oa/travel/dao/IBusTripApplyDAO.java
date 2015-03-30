package org.eapp.oa.travel.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.oa.travel.hbean.BusTripApplyDetail;
import org.eapp.util.hibernate.ListPage;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IBusTripApplyDAO extends IBaseHibernateDAO {

	public BusTripApply findById(java.lang.String id);
	
	public List<BusTripApply> findBusTripApplys(String userAccountId, int formStatus);
	
	public List<BusTripApply> getDealingBusTripApply(String userAccount);
	
	public ListPage<BusTripApply> getTrackOrArchTripApply(BusTripQueryParameters rqp,String userAccount,boolean isArch);
	
	public String getMaxID();
	
	public List<BusTripApplyDetail> getTripIsApply(Date startTime,Date endTime,String userAccount,String id);
	
	public List<BusTripApplyDetail> getSourceApply(Date startTime,Date endTime,String sourceId);
	
	public ListPage<BusTripApply> queryArchTripApply(BusTripQueryParameters rqp,Timestamp startDate,Timestamp endDate);
	
	public List<BusTripApplyDetail> getArchDetails(String id,String applicant, Timestamp startDate,
			Timestamp endDate,String applyDept);
}
