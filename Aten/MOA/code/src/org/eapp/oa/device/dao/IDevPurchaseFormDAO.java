package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DevPurchaseForm;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevPurchaseFormDAO extends IBaseHibernateDAO {

	public DevPurchaseForm findById(java.lang.String id);

	public List<DevPurchaseForm> findByExample(DevPurchaseForm instance);

	public List<DevPurchaseForm> findByProperty(String propertyName,
			Object value);

	public List<DevPurchaseForm> findByBargainNo(Object bargainNo);

	public List<DevPurchaseForm> findByAccountId(Object accountId);

	public List<DevPurchaseForm> findByGroupName(Object groupName);

	public List<DevPurchaseForm> findByBuyDate(Object buyDate);

	public List<DevPurchaseForm> findByProjectName(Object projectName);

	public List<DevPurchaseForm> findByBuyMode(Object buyMode);

	public List<DevPurchaseForm> findByBuyPurpose(Object buyPurpose);

	public List<DevPurchaseForm> findByBugetMoney(Object budgetMoney);

	public List<DevPurchaseForm> findByDeliveryDate(Object deliveryDate);

	public List<DevPurchaseForm> findByFlowInstanceId(Object flowInstanceId);

	public List<DevPurchaseForm> findByPassed(Object passed);

	public List<DevPurchaseForm> findByArchiveDate(Object archiveDate);

	public List<DevPurchaseForm> findByBuyExplain(Object buyExplain);

	public List<DevPurchaseForm> findAll();
	
	
}