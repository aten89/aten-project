package org.eapp.oa.reimburse.dao;

import java.util.List;
import java.util.Map;

import org.eapp.util.hibernate.ListPage;


import org.eapp.oa.reimburse.dto.OutlayListQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IOutlayListDAO extends IBaseHibernateDAO {
	public List<OutlayList> getOutlayListByReimItemID(String id);
	public List<OutlayList> checkOutlayList(String accountId);
	public OutlayList findById(java.lang.String id);

	public List<OutlayList> findByExample(OutlayList instance);

	public List<OutlayList> findByProperty(String propertyName,
			Object value);

	public List<OutlayList> findByOutlayCategory(Object outlayCategory);

	public List<OutlayList> findByOutlayName(Object outlayName);

	public List<OutlayList> findByCustomName(Object customName);

	public List<OutlayList> findByDocumetNum(Object documetNum);

	public List<OutlayList> findByOutlaySum(Object outlaySum);

	public List<OutlayList> findByDescription(Object description);

	public List<OutlayList> findAll();
	
	public ListPage<OutlayList> queryOutlayListDAO(OutlayListQueryParameters oqp);
	public ListPage<OutlayList> queryOutlayListDAO(OutlayListQueryParameters oqp,String financeId);
	/**
	 * 统计费用明细总金额
	 * @param oqp
	 * @return
	 */
	public double findStatOutlay(OutlayListQueryParameters oqp);
	public double findStatOutlay(OutlayListQueryParameters oqp,String financeId);
	public Map<String,Double> countReimNumOfProject(String custName, String projectCode); 
	public List<OutlayList> findOutlayList(String name,String dictKey, String budgetID);
	
	/**
	 * 查询归属于某个预算的报销明细
	 * @param budgetId
	 * @return
	 */
	public List<OutlayList> findOutlayListReim(String name,String budgetId);
}