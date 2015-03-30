/**
 * 
 */
package org.eapp.oa.hr.blo.impl;

import java.util.Date;
import java.util.List;

import org.eapp.oa.hr.blo.IHolidayTypeBiz;
import org.eapp.oa.hr.dao.IHolidayTypeDAO;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.system.exception.OaException;


/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class HolidayTypeBiz implements IHolidayTypeBiz {

	private IHolidayTypeDAO holidayTypeDAO;

	public void setHolidayTypeDAO(IHolidayTypeDAO holidayTypeDAO) {
		this.holidayTypeDAO = holidayTypeDAO;
	}

	@Override
	public HolidayType getByHolidayTypeId(String id) {
		return holidayTypeDAO.findById(id);
	}
	@Override
	public List<HolidayType> getAllHolidayTypes(){
		return holidayTypeDAO.findAll();
	}
	
	@Override
	public double getDaysOfHoliday(String holidayName, Date start, String startTime, Date end, String endTime) {
		HolidayType holType = holidayTypeDAO.findByHolidayName(holidayName);
		if (holType == null) {
			return 0;
		}
//		后期可以通过表达式计算天数，如扣除周未，法定假日待
//		holType.getExpression();
		double days = (long)(end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
		if (HolidayDetail.AFTERNOON.equals(startTime)) {
			days -= 0.5;//选上午减半天
		}
		if (HolidayDetail.AFTERNOON.equals(endTime)) {
			days += 1;//选下午加一天
		} else {
			days += 0.5;//选上午加半天
		}
		return days;
	}
	
//	public String getFlowKey(String holidayName){
//		return holidayTypeDAO.getFlowKey(holidayName);
//	}
	
	public HolidayType getHolidayTypeByName (String holidayName) {
		return holidayTypeDAO.findByHolidayName(holidayName);
	}
	
	
	public HolidayType deleteHolidayType(String id) {
		HolidayType ht = holidayTypeDAO.findById(id);
		if(ht != null){
			holidayTypeDAO.delete(ht);
		}
		return ht;
	}
	
	public HolidayType addHolidayType(String holidayName, Double maxDays, String expression, 
			String description) throws OaException {
		if (holidayName == null) {
            throw new IllegalArgumentException();
        }
        // 判断分类名称是否重复
        if (!holidayTypeDAO.checkHolidayName(holidayName)) {
        	throw new OaException("假期名称重复");
        }

        HolidayType conf = new HolidayType();
        conf.setHolidayName(holidayName);
        conf.setMaxDays(maxDays);
        conf.setExpression(expression);
        conf.setDescription(description);
        holidayTypeDAO.save(conf);
        return conf;
	}
	
	public HolidayType modifyHolidayType(String id, String holidayName, Double maxDays, 
			String expression, String description) throws OaException {
		if (id == null || holidayName == null) {
		    throw new IllegalArgumentException();
		}
		//根据ID查找信息参数设置
		HolidayType rfc = holidayTypeDAO.findById(id);
		if (rfc == null) {
		    throw new IllegalArgumentException("配置不存在");
		}
		// 判断分类名称是否重复
		if (!rfc.getHolidayName().equals(holidayName) && !holidayTypeDAO.checkHolidayName(holidayName)) {
			throw new OaException("假期名称不能重复");
		}
		rfc.setHolidayName(holidayName);
		rfc.setMaxDays(maxDays);
		rfc.setExpression(expression);
		rfc.setDescription(description);
        holidayTypeDAO.update(rfc);
		return rfc;
	}
	
	public HolidayType findByName(String name){
		return holidayTypeDAO.findByHolidayName(name);
	}
}
