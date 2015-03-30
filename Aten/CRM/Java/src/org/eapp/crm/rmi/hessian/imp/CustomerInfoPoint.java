package org.eapp.crm.rmi.hessian.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.crm.blo.ICustomerInfoBiz;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.rmi.hessian.CustInfo;
import org.eapp.crm.rmi.hessian.ICustomerInfoPoint;
import org.eapp.util.hibernate.ListPage;

import com.caucho.hessian.server.HessianServlet;

public class CustomerInfoPoint extends HessianServlet implements ICustomerInfoPoint {
    /**
     * serialVersionUID
     */
	private static final long serialVersionUID = 1806025575193264289L;
	/**
	 * 客户业务逻辑访问接口
	 */
	private ICustomerInfoBiz customerInfoBiz;
	
	
    public void setCustomerInfoBiz(ICustomerInfoBiz customerInfoBiz) {
        this.customerInfoBiz = customerInfoBiz;
    }

    @Override
    public String txAddCust(String saleMan, String Name, String custProperty, String identityNum, String custStatus) {
        
        return customerInfoBiz.txAddCust(saleMan, Name, custProperty, identityNum, custStatus);
        
    }

    @Override
    public void txModifyCust(String id, String status, String identityNum) {
        customerInfoBiz.txModifyCust(id, status, identityNum);
        
    }
    
    @Override
    public String getCustomerIdentityNum(String id) {
        CustomerInfo customerInfo = customerInfoBiz.findCustomerInfoById(id);
        return customerInfo.getIdentityNum();
        
    }

	/* (non-Javadoc)
	 * @see org.eapp.crm.rmi.hessian.ICustomerInfoPoint#queryCustPage(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, boolean)
	 */
	@Override
	public ListPage<CustInfo> queryCustPage(String userID, String customerName,
			String tel, Integer pageSize, Integer pageNo, String sortCol,
			boolean ascend) {
		CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
        if (StringUtils.isNotEmpty(userID)) {
            qp.addParameter("saleMan", userID);
        }
        // 客户名称
        if (StringUtils.isNotEmpty(customerName)) {
            qp.addParameter("custName", customerName);
        }
        // 电话
        if (StringUtils.isNotEmpty(tel)) {
            qp.addParameter("tel", tel);
        }
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        // 增加对排序列的处理
        if (StringUtils.isNotEmpty(sortCol)) {
            qp.addOrder(sortCol, ascend);
        }
        
        ListPage<CustomerInfo> list = customerInfoBiz.queryCustomerInfoList(qp);
        
        ListPage<CustInfo> resultList = new ListPage<CustInfo>();
        List<CustInfo> custList = new ArrayList<CustInfo>();
        if (list != null && list.getDataList() != null) {
        	for (CustomerInfo cust : list.getDataList()) {
    			CustInfo c = new CustInfo();
    			c.setId(cust.getId());
    			c.setCustName(cust.getCustName());
    			c.setTel(cust.getTel());
    			c.setRecommendProduct(cust.getRecommendProduct());
    			custList.add(c);
    		}
        	resultList.setCurrentPageNo(list.getCurrentPageNo());
        	resultList.setCurrentPageSize(list.getCurrentPageSize());
        	resultList.setTotalCount(list.getTotalCount());
        	resultList.setDataList(custList);
        }
        return resultList;
	}
    
	
}
