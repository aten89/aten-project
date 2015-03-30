package org.eapp.crm.rmi.hessian;

import org.eapp.util.hibernate.ListPage;


public interface ICustomerInfoPoint {
	
	public String txAddCust(String saleMan, String Name, String custProperty, String identityNum, String custStatus);
	
	public void txModifyCust(String id, String status, String identityNum);
	/**
	 * queryCustPage
	 * @param userID 用户ID
	 * @param customerName 客户名称
	 * @param tel 客户电话
	 * @param pageSize 页码
	 * @param pageNo 页数
	 * @param sortCol 排序列
	 * @param ascend 排序
	 * @return ListPage<CustInfo>
	 */
	public ListPage<CustInfo> queryCustPage(String userID, String customerName, String tel, Integer pageSize, Integer pageNo, String sortCol, boolean ascend );
	
	public String getCustomerIdentityNum(String id);
}
