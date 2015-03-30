package org.eapp.oa.hr.blo;

import java.util.Date;
import java.util.List;

import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.system.exception.OaException;


public interface IHolidayTypeBiz{

	public HolidayType getByHolidayTypeId(java.lang.String id);
	
	public List<HolidayType> getAllHolidayTypes();
	
	public double getDaysOfHoliday(String holidayName, Date start, String startTime, Date end, String endTime);
	
//	public String getFlowKey(String holidayName);
	
	public HolidayType getHolidayTypeByName (String holidayName);
	
	public HolidayType deleteHolidayType(String id);
	
	public HolidayType addHolidayType(String holidayName, Double maxDays, String expression, 
			String description) throws OaException;
	
	public HolidayType modifyHolidayType(String id, String holidayName, Double maxDays, 
			String expression, String description) throws OaException;
			
	public HolidayType findByName(String name);
}