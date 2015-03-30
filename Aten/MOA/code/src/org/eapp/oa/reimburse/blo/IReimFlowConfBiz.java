/**
 * 
 */
package org.eapp.oa.reimburse.blo;

import java.util.List;

import org.eapp.oa.reimburse.hbean.ReimFlowConf;
import org.eapp.oa.system.exception.OaException;


/**
 * 报销明细业务逻辑
 */
public interface IReimFlowConfBiz {
	
	public List<ReimFlowConf> getAllReimFlowConfs();
	
	public ReimFlowConf getReimFlowConf(String id);
	
	public ReimFlowConf addReimFlowConf(String groupName, String flowKey, String desc) throws OaException;

	public ReimFlowConf deleteReimFlowConf(String id);
	
	public ReimFlowConf modifyReimFlowConf(String id, String groupName, String flowKey, String desc) throws OaException;
}
