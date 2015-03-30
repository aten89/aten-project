package org.eapp.oa.hr.dao;

import java.util.Date;
import java.util.List;

import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;


public interface IHolidayApplyDAO extends IBaseHibernateDAO {

	public HolidayApply findById(java.lang.String id);

	public List<HolidayApply> findByExample(HolidayApply instance);

	public List<HolidayApply> findHolidayApplys(String userAccountId, int formStatus);
	
	public List<HolidayApply> getDealingHolidayApply(String userAccountId, List<String> userRoles);
	
	public ListPage<HolidayApply> getTrackOrArchHolidayApply(HolidayQueryParameters rqp,
			String userAccountId,Boolean isArch);
	
	public String getMaxID();
	
	public List<HolidayApply> findByDeptAndDate(Date startTime , Date endTime,String dept);

	public List<HolidayDetail> findByTime(Date startTime, Date endTime);

	public List<HolidayDetail> findHoliday4PreMonth(Date startTime, Date endTime);
	
	/**
	 * 获取未归档的请假单申请
	 * @return
	 */
	public List<HolidayApply> findUnArchivedHolidayApply();
	
	/**
	 * 
	 * @param formID
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<HolidayDetail> findHolidayDetailByTime(String formID,Date startTime, Date endTime);
	
	public List<HolidayDetail> getHolidayIsApply(Date startTime, Date endTime,String userId);
	
	public List<HolidayDetail> getHolidayIsCancel(Date startTime, Date endTime,String sourceId);
	
	public List<HolidayDetail> findUserArchHolidays(String userAccountId, Date startDate);
	
	public ListPage<HolidayDetail> findHolidayDetail(HolidayQueryParameters rpq);
}