package org.eapp.oa.flow.blo;

import org.eapp.oa.flow.dto.FlowNotifyQueryParameters;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.util.hibernate.ListPage;

/**
 * <p>Title: </p>
 * <p>Description: 流程元数据业务逻辑接口层</p>
 * @author 
 * @version 1.0 
 */
public interface IFlowNotifyBiz {

	public FlowNotify getFlowNotifyById(String id);
	
	public FlowNotify txSetViewFlag(String id);
	
	public ListPage<FlowNotify> queryUserNotify(FlowNotifyQueryParameters parms, String userAccountId);
	
	public void addFlowNotifys(FlowNotify flowNotify);
	
	public void txUpdateNotifyStatus(String refFormID, int flowStatus);

}