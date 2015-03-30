/**
 * 
 */
package org.eapp.oa.travel.blo;

import java.util.List;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.travel.hbean.BusTripFlowConf;


/**
 * 业务逻辑
 */
public interface IBusTripFlowConfBiz {
	
	public List<BusTripFlowConf> getAllBusTripFlowConfs();
	
	public BusTripFlowConf getBusTripFlowConf(String id);
	
	public BusTripFlowConf addBusTripFlowConf(String groupName, String flowKey, String desc) throws OaException;

	public BusTripFlowConf deleteBusTripFlowConf(String id);
	
	public BusTripFlowConf modifyBusTripFlowConf(String id, String groupName, String flowKey, String desc) throws OaException;
}
