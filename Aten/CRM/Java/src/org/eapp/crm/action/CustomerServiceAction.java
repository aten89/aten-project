package org.eapp.crm.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.ICustomerServiceBiz;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.util.Tools;
import org.eapp.util.hibernate.ListPage;

/**
 * 客服管理ACTION
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-12	黄云耿	新建
 * </pre>
 */
public class CustomerServiceAction extends BaseAction {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1708293559618728889L;
    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustomerServiceAction.class);

    /**
     * 客服管理业务逻辑层接口
     */
    private ICustomerServiceBiz customerServiceBiz;
    /**
     * 页码
     */
    private int pageNo;
    /**
     * 每页数
     */
    private int pageSize;
    /**
     * 当前排序列
     */
    private String sortCol;
    /**
     * true:升序;false:降序
     */
    private boolean ascend;
    /**
     * 客户名称
     */
    private String custName;
    /**
     * 客户状态
     */
    private String status;
    /**
     * 电话
     */
    private String tel;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 推荐产品
     */
    private String recommendProduct;
    /**
     * 查询客户列表
     */
    private ListPage<CustomerInfo> listPage;
    /**
     * 
     */
    private String multipleCustomerStatus;
    private Date bgnSubmitTime;
    private Date endSubmitTime;
    private String customerManage;
    private String saleGroupId;
    private Date bgnVistTime;
    private Date endVistTime;
 
  
    /**
     * 根据当前客服人员过滤销售人员对应的客户列表
     * 
     * @return 操作结果
     * 
     *         <pre>
     * 修改日期     修改人 修改原因
     * 2014-4-28    黄云耿 新建
     * </pre>
     */
    public String queryCustomerInfoList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
          
            // 客户状态
            if (StringUtils.isNotEmpty(status)) {
                qp.addParameter("status", status);
            }
            // 多个客户状态
            if (StringUtils.isNotEmpty(multipleCustomerStatus)) {
                List<String> multipleStatus = Arrays.asList(multipleCustomerStatus.split(","));
                qp.addParameter("multipleStatus", multipleStatus);
            }
            // 客户名称
            if (StringUtils.isNotEmpty(custName)) {
                qp.addParameter("custName", custName);
            }
            // 客户经理
            if (StringUtils.isNotEmpty(customerManage)) {
                qp.setSaleMan(customerManage);
            }
            // 电话
            if (StringUtils.isNotEmpty(tel)) {
            	qp.setTel(tel);
            }
            // 邮箱
            if (StringUtils.isNotEmpty(email)) {
            	qp.setEmail(email);
            }
            // 推荐产品
            if (StringUtils.isNotEmpty(recommendProduct)) {
            	qp.setRecommendProduct(recommendProduct);
            }
            if (bgnSubmitTime != null) {
                qp.setBgnSubmitTime(bgnSubmitTime);
            }
            if (endSubmitTime != null) {
                qp.setEndSubmitTime(endSubmitTime);
            }
            if (bgnVistTime != null) {
                qp.setBgnVistTime(bgnVistTime);
            }
            if (endVistTime != null) {
                qp.setEndVistTime(endVistTime);
            }
            // 部门
            if (StringUtils.isNotEmpty(saleGroupId)) {
                qp.setSaleGroupId(saleGroupId);
            }
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            listPage = customerServiceBiz.queryCustomerInfoList(qp, user);
            if (listPage!=null && listPage.getDataList()!=null && !listPage.getDataList().isEmpty()) {
                for (CustomerInfo cust : listPage.getDataList()) {
                	if (!user.getAccountID().equals(cust.getSaleMan())) {
                		cust.setTel(Tools.screenPhoneNumber(cust.getTel()));
                	}
                }
            }
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryCustomerInfoList failed: ", e);
            return error("系统错误");
        }
    }
    
   
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortCol(String sortCol) {
        this.sortCol = sortCol;
    }
    
    public ListPage<CustomerInfo> getListPage() {
        return listPage;
    }

    public void setCustomerServiceBiz(ICustomerServiceBiz customerServiceBiz) {
        this.customerServiceBiz = customerServiceBiz;
    }

    /**
     * set the custName to set
     * @param custName the custName to set
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

	/**
     * set the tel to set
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * set the email to set
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * set the recommendProduct to set
     * @param recommendProduct the recommendProduct to set
     */
    public void setRecommendProduct(String recommendProduct) {
        this.recommendProduct = recommendProduct;
    }

    /**
     * set the status to set
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void setMultipleCustomerStatus(String multipleCustomerStatus) {
        this.multipleCustomerStatus = multipleCustomerStatus;
    }

    public void setBgnSubmitTime(Date bgnSubmitTime) {
        this.bgnSubmitTime = bgnSubmitTime;
    }

    public void setEndSubmitTime(Date endSubmitTime) {
        this.endSubmitTime = endSubmitTime;
    }

    public void setCustomerManage(String customerManage) {
        this.customerManage = customerManage;
    }

    public void setSaleGroupId(String saleGroupId) {
        this.saleGroupId = saleGroupId;
    }

	public void setBgnVistTime(Date bgnVistTime) {
		this.bgnVistTime = bgnVistTime;
	}

	public void setEndVistTime(Date endVistTime) {
		this.endVistTime = endVistTime;
	}
    
}
