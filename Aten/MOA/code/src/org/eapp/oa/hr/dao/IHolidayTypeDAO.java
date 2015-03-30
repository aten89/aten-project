package org.eapp.oa.hr.dao;

import java.util.List;

import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IHolidayTypeDAO extends IBaseHibernateDAO {

	public HolidayType findById(java.lang.String id);

	public List<HolidayType> findByExample(HolidayType instance);
	
	public boolean checkHolidayName(String holidayName);
	
	public List<HolidayType> findAll();
	
	public HolidayType findByHolidayName(String holidayName);
}