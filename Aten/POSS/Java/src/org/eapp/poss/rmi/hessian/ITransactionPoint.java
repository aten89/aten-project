package org.eapp.poss.rmi.hessian;

import org.eapp.util.hibernate.ListPage;


public interface ITransactionPoint {
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
	public ListPage<TransactionRecord> queryTransactionRecordPage(String custId, Integer pageSize, Integer pageNo, String sortCol, boolean ascend);
	
	/**
	 * 查询划款记录
	 * @param custId
	 * @param pageSize
	 * @param pageNo
	 * @param sortCol
	 * @param ascend
	 * @return
	 * @throws PossException
	 */
	public ListPage<PaymentRecord> queryPaymentRecordPage(String custId, Integer pageSize, Integer pageNo, String sortCol, boolean ascend);
}
