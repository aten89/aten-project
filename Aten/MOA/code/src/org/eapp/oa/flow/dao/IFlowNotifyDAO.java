package org.eapp.oa.flow.dao;

import java.util.List;

import org.eapp.oa.flow.dto.FlowNotifyQueryParameters;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.eapp.oa.system.dao.IBaseHibernateDAO;
import org.eapp.util.hibernate.ListPage;


public interface IFlowNotifyDAO extends IBaseHibernateDAO {

	public FlowNotify findById(String id);
	
	public ListPage<FlowNotify> queryUserNotify(FlowNotifyQueryParameters parms, String userAccountId);
	
	public void updateNotifyStatus(String refFormID, int flowStatus);
	
	public List<FlowNotify> findEndNotify(String refFormID);
}