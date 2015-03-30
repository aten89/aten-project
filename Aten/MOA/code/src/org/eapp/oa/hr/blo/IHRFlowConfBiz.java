/**
 * 
 */
package org.eapp.oa.hr.blo;

import java.util.List;

import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.system.exception.OaException;


/**
 * 报销明细业务逻辑
 */
public interface IHRFlowConfBiz {
	
	public List<HRFlowConf> getAllHolidayFlowConfs();
	
	public HRFlowConf getHolidayFlowConf(String id);
	
	public HRFlowConf addHolidayFlowConf(String groupName, String holidayFlowKey, String canHolidayFlowKey, 
			String entryFlowKey, String resignFlowKey, String transferFlowKey, String positiveFlowKey, String desc) throws OaException;

	public HRFlowConf deleteHolidayFlowConf(String id);
	
	public HRFlowConf modifyHolidayFlowConf(String id, String groupName, String holidayFlowKey, String canHolidayFlowKey, 
			String entryFlowKey, String resignFlowKey, String transferFlowKey, String positiveFlowKey, String desc) throws OaException;
}
