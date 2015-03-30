package org.eapp.poss.rmi.hessian;

import org.eapp.util.hibernate.ListPage;


public interface IMessagePoint {
	/**
	 * queryMessagePage
	 * @param userID 用户ID
	 * @param tel 客户电话
	 * @param pageSize 页码
	 * @param pageNo 页数
	 * @param sortCol 排序列
	 * @param ascend 排序
	 * @return ListPage<CustInfo>
	 */
	public ListPage<MessageInfo> queryMessagePage(String userID, String tel, Integer pageSize, Integer pageNo, String sortCol, boolean ascend);
}
