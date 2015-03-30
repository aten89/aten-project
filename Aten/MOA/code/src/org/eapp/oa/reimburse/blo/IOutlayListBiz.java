/**
 * 
 */
package org.eapp.oa.reimburse.blo;

import java.util.List;
import java.util.Map;

import org.eapp.oa.reimburse.dto.OutlayListQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.util.hibernate.ListPage;


/**
 * @author 林良益
 * 报销明细业务逻辑
 */
public interface IOutlayListBiz {
	
	public StringBuffer findConflict (List<OutlayList> list , List<OutlayList> allList ,String travelBeginDate,String travelEndDate);
	
	public List<OutlayList> getOutlayListByReimItemID(String id);
	
	public List<OutlayList> checkOutlayList(String accountId);
	/**
	 * 根据条件查询报销明细
	 * @param oqp
	 * @return
	 */
	public ListPage<OutlayList> queryOutlayList(OutlayListQueryParameters oqp);
	public ListPage<OutlayList> queryOutlayList(OutlayListQueryParameters oqp,String financeId);
	
	/**
	 * 统计费用明细总金额
	 * @param oqp
	 * @return
	 */
	public double getStatOutlay(OutlayListQueryParameters oqp);
	public double getStatOutlay(OutlayListQueryParameters oqp,String financeId);
	public List<OutlayList> getSubOutlayListByOutlayCategory(String outlayCategory,String dictKey, String budgetID);

	public Map<String, Double> getReimbursement(String custName, String projectCode);
	
}
