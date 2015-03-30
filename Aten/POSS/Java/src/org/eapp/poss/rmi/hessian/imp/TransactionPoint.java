package org.eapp.poss.rmi.hessian.imp;


import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.ICustPaymentBiz;
import org.eapp.poss.dao.param.CustPaymentQueryParameters;
import org.eapp.poss.rmi.hessian.ITransactionPoint;
import org.eapp.poss.rmi.hessian.PaymentRecord;
import org.eapp.poss.rmi.hessian.TransactionRecord;
import org.eapp.util.hibernate.ListPage;

import com.caucho.hessian.server.HessianServlet;

public class TransactionPoint extends HessianServlet implements ITransactionPoint {

	private static final long serialVersionUID = 1226278565808224931L;
	
	/**
	 * 划款记录逻辑接口
	 */
	private ICustPaymentBiz custPaymentBiz;
	
	public void setCustPaymentBiz(ICustPaymentBiz custPaymentBiz) {
		this.custPaymentBiz = custPaymentBiz;
	}

	@Override
	public ListPage<TransactionRecord> queryTransactionRecordPage(String custId, Integer pageSize, Integer pageNo,
			String sortCol, boolean ascend) {
		CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
        if (StringUtils.isNotEmpty(custId)) {
            qp.setCustId(custId);
        }
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        
        return custPaymentBiz.queryTransactionRecordPage(qp);
	}

	@Override
	public ListPage<PaymentRecord> queryPaymentRecordPage(String custId, Integer pageSize, Integer pageNo,
			String sortCol, boolean ascend) {
		CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
        if (StringUtils.isNotEmpty(custId)) {
            qp.setCustId(custId);
        }
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        
        return custPaymentBiz.queryPaymentRecordPage(qp);
	}
}
