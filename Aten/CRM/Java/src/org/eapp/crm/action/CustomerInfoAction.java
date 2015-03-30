/**
 * 
 */
package org.eapp.crm.action;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.ICustomerAppointmentBiz;
import org.eapp.crm.blo.ICustomerInfoBiz;
import org.eapp.crm.config.SysConstants;
import org.eapp.crm.dao.param.CustomerInfoQueryParameters;
import org.eapp.crm.dto.AutoCompleteData;
import org.eapp.crm.dto.CustomerInfoSelect;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;
import org.eapp.crm.hbean.CustomerConsult;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ReturnVist;
import org.eapp.crm.system.util.web.HTMLResponse;
import org.eapp.crm.util.Tools;
import org.eapp.poss.rmi.hessian.IMessagePoint;
import org.eapp.poss.rmi.hessian.ITransactionPoint;
import org.eapp.poss.rmi.hessian.MessageInfo;
import org.eapp.poss.rmi.hessian.PaymentRecord;
import org.eapp.poss.rmi.hessian.TransactionRecord;
import org.eapp.util.hibernate.ListPage;

import com.alibaba.fastjson.JSON;

/**
 * 客户信息Action
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	    黄云耿	  新建
 * </pre>
 */
public class CustomerInfoAction extends BaseAction {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8844380855445011263L;

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustomerInfoAction.class);

    /**
     * 导入客户业务逻辑层接口
     */
    private ICustomerInfoBiz customerInfoBiz;
    /**
     * 预约记录业务逻辑层接口
     */
    private ICustomerAppointmentBiz customerAppointmentBiz;
    /**
     * 部门扩展信息接口
     */
//    private IGroupExtBiz groupExtBiz;
    /**
     * poss远程接口
     */
    private IMessagePoint messagePoint;
    
    private ITransactionPoint transactionPoint;
    
    // 输入参数
    /**
     * 客户经理
     */
    private String customerManage;
    /**
     * 客户状态
     */
    private String customerStatus;
    /**
     * 多个客户状态
     */
    private String multipleCustomerStatus;
    /**
     * 数据来源
     */
    private String dataSource;
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
     * 查询客户列表结果返回值
     */
    private ListPage<CustomerInfo> listPage;
    /**
     * 客户信息ID集
     */
    private String customerInfoIds;
    /**
     * 客户ID
     */
    private String customerId;
    /**
     * 咨询记录列表
     */
    private ListPage<CustomerConsult> consultList;
    /**
     * 回访记录列表
     */
    private ListPage<ReturnVist> visitList;
    /**
     * 预约记录列表
     */
    private ListPage<CustomerAppointment> appointmentList;
    /**
     * 咨询内容
     */
    private String consultContent;
    /**
     * 咨询记录ID
     */
    private String customerConsultId;
    /**
     * 回访记录ID
     */
    private String returnVistId;
    /**
     * 预约记录ID
     */
    private String customerAppointmentId;
    /**
     * 预约记录
     */
    private CustomerAppointment customerAppointment;
    /**
     * 预约记录JSON
     */
    private String customerAppointmentJson;
    /**
     * 预约时间
     */
    private Date appointmentTime;
    /**
     * 预约类型
     */
	private String appointmentType;
	/**
	 * 预约时机
	 */
	private Integer warnOpportunity;
	/**
	 * 预约备注
	 */
	private String remark;
    /**
     * 客户信息JSON
     */
    private String customerJson;
    
    private Integer memoMark;
    
    private Date bgnSubmitTime;
    private Date endSubmitTime;
    
    //输出
    /**
     * 客户信息
     */
    private CustomerInfo customer;
    
    /**
     * 回访记录
     */
    private ReturnVist returnVist;
    /**
     * 回访内容
     */
    private String returnVistContent;
    /**
     * 咨询内容
     */
    private String customerConsultContent;
    /**
     * 咨询记录
     */
    private CustomerConsult customerConsult;
    /**
     * 操作类型
     * 0:保存
     * 1:审批通过
     * 2:驳回修改
     */
    private Integer operType;
    /**
     * 查询标识
     */
    private String queryFlag;
    /**
     * 操作标识
     */
    private String flag;
    private String jsoncallback;
    private String htmlValue;
    private Boolean isTelEditable;
    private String hidTelStr;
    /**
     * 短信列表
     */
    private ListPage<MessageInfo> messageListPage;
    private ListPage<TransactionRecord> transactionRecordListPage;
    private ListPage<PaymentRecord> paymentRecordListPage;
    private List<AutoCompleteData> autoCompleteDatas;

    /**
     * 输入提示数据
     * @return
     */
    public String loadAutoCompleteData() {
    	SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
        // 多个客户状态
        String[] multipleStatus = null;
        if (StringUtils.isNotEmpty(multipleCustomerStatus)) {
            multipleStatus = multipleCustomerStatus.split(",");
        }
    	autoCompleteDatas = customerInfoBiz.queryAutoCompleteData(user.getAccountID(), multipleStatus, pageSize);
    	return success();
    }
    
    /**
     * 查询客户列表
     * 
     * @return 操作结果
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-28	黄云耿	新建
     * </pre>
     */
    public String queryCustomerInfoList() {
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
        try {
            CustomerInfoQueryParameters qp = createCustQueryParameters();
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            if(queryFlag == null) {
                //过滤销售人员自己负责的客户
                qp.addParameter("saleMan", user.getAccountID());
            } else {
                if (StringUtils.isEmpty(customerManage)) {
                    listPage = null;
                    return success();
                }
            }
            listPage = customerInfoBiz.queryCustomerInfoList(qp);
            if (listPage!=null && listPage.getDataList() != null && !listPage.getDataList().isEmpty()) {
                for (CustomerInfo cust : listPage.getDataList()) {
                	if (!user.getAccountID().equals(cust.getSaleMan())) {
                		cust.setTel(Tools.screenPhoneNumber(cust.getTel()));
                	}
                }
            }
            
            return success();
        } catch (Exception e) {
            logger.error("queryCustomerInfoList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询未提交客户
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-14	lhg		新建
     * </pre>
     */
    public String queryUnCommitCustList() {
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
//        if (StringUtils.isEmpty(dataSource)) {
//            return error("非法参数：数据来源为空");
//        }
        try {
            CustomerInfoQueryParameters qp = createCustQueryParameters();
            // 当前登录者的未提交客户
            qp.addParameter("saleMan", user.getAccountID());
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            listPage = customerInfoBiz.queryCustomerInfoList(qp);
            if (listPage != null && listPage.getDataList() != null && !listPage.getDataList().isEmpty()) {
				for (CustomerInfo cust : listPage.getDataList()) {
					if (!user.getAccountID().equals(cust.getSaleMan())) {
						cust.setTel(Tools.screenPhoneNumber(cust.getTel()));
					}
				}
			}
            return success();
        } catch (Exception e) {
            logger.error("queryUnCommitCustList failed: ", e);
            return error();
        }
    }
    
    /**
     * 初始化查询参数
     * @return CustomerInfoQueryParameters
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-14	lhg		新建
     * </pre>
     */
    private CustomerInfoQueryParameters createCustQueryParameters() {
        CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        if (StringUtils.isNotEmpty(customerManage)) {
            qp.addParameter("saleMan", customerManage);
        }
        // 客户状态
        if (StringUtils.isNotEmpty(customerStatus)) {
            qp.addParameter("status", customerStatus);
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
        // 电话
        if (StringUtils.isNotEmpty(tel)) {
            qp.addParameter("tel", tel);
        }
        // 邮箱
        if (StringUtils.isNotEmpty(email)) {
            qp.addParameter("email", email);
        }
        // 推荐产品
        if (StringUtils.isNotEmpty(recommendProduct)) {
            qp.addParameter("recommendProduct", recommendProduct);
        }
        if (bgnSubmitTime != null) {
            qp.addParameter("bgnSubmitTime", bgnSubmitTime);
        }
        if (endSubmitTime != null) {
            qp.setEndSubmitTime(endSubmitTime);
        }
        // 数据来源
        if (StringUtils.isNotEmpty(dataSource)) {
            qp.addParameter("dataSource", dataSource);
        }
        return qp;
    }

    /**
     * 导入我的客户
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-15	lhg		新建
     * </pre>
     */
    public String importMyCustomer() {
        // 获取当前登录用户
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
        try {
            customerInfoBiz.txImportMyCustomer(new File((String) getSession().getAttribute("filePath")),
                    user.getAccountID());
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (IOException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("importMyCustomer error", e);
            return error();
        }
    }

    /**
     * 删除客户
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-8	黄云耿	新建
     * </pre>
     */
    public String delCustomerInfo() {
    	try {
    	    if (StringUtils.isEmpty(customerId)) {
                throw new CrmException("参数异常：ID不能为空");
            }
    	    CustomerInfo cust = customerInfoBiz.txDeleteCustomerInfo(customerId);
    	    //日志
    	    ActionLogger.log(getRequest(), customerId, cust.toString());
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("delCustomerInfo failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 客户信息数据转移，改变其对应的客户经理
     * 
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-5	黄云耿	新建
     * </pre>
     * 
     * @return
     */
    public String changeSaleMan() {
        try {
        	String[] strArray = null;
            if (StringUtils.isNotEmpty(customerInfoIds)) {
                strArray = customerInfoIds.split(",");
            }
            customerInfoBiz.txChangeSaleMan(strArray, customerManage);
            return success("转移成功");
        } catch (CrmException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 查询客户对应的咨询记录
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-8	黄云耿	新建
     * </pre>
     */
    public String queryConsultRecordList() {
        try {
            CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            if (StringUtils.isNotEmpty(customerId)) {
                qp.setId(customerId);
            }
            
            consultList = customerInfoBiz.queryConsultRecordList(qp);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryConsultRecordList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询回访记录列表
     * @return 操作结果
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-8 黄云耿 新建
     * </pre>
     */
    public String queryVisitRecordList() {
        try {
            CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            if (StringUtils.isNotEmpty(customerId)) {
                qp.setId(customerId);
            }
            
            visitList = customerInfoBiz.queryVisitRecordList(qp);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryVisitRecordList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询预约列表
     * @return
     */
    public String queryAppointmentRecordList() {
    	// 获取当前登录用户
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
        try {
            CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            if (StringUtils.isNotEmpty(customerId)) {
                qp.setId(customerId);
            }
            
            qp.addParameter("createor", user.getAccountID());
            
            appointmentList = customerInfoBiz.queryAppointmentRecordList(qp);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryAppointmentRecordList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 获取划款记录
     * @return
     */
    public String queryTransferRecordList() {
    	try {
            CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            if (StringUtils.isNotEmpty(customerId)) {
                qp.setId(customerId);
            }
            
            // 调用POSS接口获取划款记录列表
            appointmentList = customerInfoBiz.queryAppointmentRecordList(qp);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryTransferRecordList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 新增客户咨询记录
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-9	黄云耿	新建
     * </pre>
     */
    public String addConsultRecord() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            if(customerConsultId == null || StringUtils.isEmpty(customerConsultId)){
                customerInfoBiz.addConsultRecord(customerId, consultContent, user.getAccountID());
            } else {
                customerInfoBiz.modifyConsultRecord(customerConsultId, consultContent);
            }
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("addConsultRecord failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 新增客户预约记录 
     * @return
     */
    public String addAppointmentRecord() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        CustomerAppointment customerAppointment  = (CustomerAppointment)JSON.parseObject(customerAppointmentJson, CustomerAppointment.class);
		try {
			customerAppointment = customerInfoBiz.addAppointmentRecord(customerId, customerAppointment, user.getAccountID());
			return success(customerAppointment.getId());
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("addAppointmentRecord2 failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 删除客户咨询记录
     * @return 操作结果
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-9 黄云耿   新建
     * </pre>
     */
    public String deleteConsultRecord() {
        try {
            if (StringUtils.isEmpty(customerConsultId)) {
                throw new CrmException("参数异常：ID不能为空");
            }
            customerInfoBiz.deleteConsultRecord(customerConsultId);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("deleteConsultRecord failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 删除回访记录
     * @return 操作结果
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-11	黄云耿	新建
     * </pre>
     */
    public String deleteVisitRecord() {
        try {
            if (StringUtils.isEmpty(returnVistId)) {
                throw new CrmException("参数异常：ID不能为空");
            }
            customerInfoBiz.deleteVisitRecord(returnVistId);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("deleteVisitRecord failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 删除预约记录
     * @return
     */
    public String deleteAppointmentRecord() {
        try {
            if (StringUtils.isEmpty(customerAppointmentId)) {
                throw new CrmException("参数异常：ID不能为空");
            }
            customerInfoBiz.deleteAppointmentRecord(customerAppointmentId);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("deleteAppointmentRecord failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 修改预约记录
     * @return
     */
    public String modifyAppointmentRecord() {
    	try {
    		CustomerAppointment cappointment  = (CustomerAppointment)JSON.parseObject(customerAppointmentJson, CustomerAppointment.class);
            if (StringUtils.isEmpty(cappointment.getId())) {
                throw new CrmException("参数异常：ID不能为空");
            }
            cappointment = customerInfoBiz.modifyCustomerAppointment(cappointment);
            return success(cappointment.getId());
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("modifyAppointmentRecord failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 修改客户状态
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-10	黄云耿	新建
     * </pre>
     */
    public String editorCustomerInfo() {
        try {
            if (StringUtils.isEmpty(customerId)) {
                throw new CrmException("参数异常：ID不能为空");
            }
            customerInfoBiz.txEditorCustomerInfo(customerId);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("editorCustomerInfo failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 初始化新增
     * @return 操作结果
     */
    public String initAddCustomer() {
        SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        if (StringUtils.isNotEmpty(customerManage) && "currentUser".equals(customerManage)) {
            customer = new CustomerInfo();
            customer.setSaleMan(user.getAccountID());
        }
		return success();
	}
    
    /**
     * 初始化新增
     * @return 操作结果
     */
    public String initModifyAppointmentRecord() {
    	try {
    		customerAppointment = customerAppointmentBiz.findCustomerAppointmentById(customerAppointmentId);
			return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("initModifyAppointmentRecord failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 新增
     * @return 操作结果
     */
    public String addCustomer() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	CustomerInfo cust  = (CustomerInfo)JSON.parseObject(customerJson, CustomerInfo.class);
		cust.setSaleMan(user.getAccountID());
    	try {
    		cust.setStatus(CustomerInfo.UNCOMMIT_STATUS);
			customer = customerInfoBiz.addCustomerInfo(cust);
			//日志
    	    ActionLogger.log(getRequest(), customer.getId(), customer.toString());
			return success(customer.getId());
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("addCustomer failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 初始化修改
     * @return 操作结果
     */
    public String initModifyCustomer() {
    	try {
			customer = customerInfoBiz.findCustomerInfoById(customerId);
			return success();
		} catch (Exception e) {
			logger.error("initModifyCustomer failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 初始化修改
     * @return 操作结果
     */
    public String initModifyCustomerDiv() {
    	try {
			customer = customerInfoBiz.findCustomerInfoById(customerId);
			return success();
		} catch (Exception e) {
			logger.error("initModifyCustomer failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 修改
     * @return 操作结果
     */
    public String modifyCustomer() {
    	try {
    		CustomerInfo cust  = (CustomerInfo)JSON.parseObject(customerJson, CustomerInfo.class);
        	if (cust == null || StringUtils.isEmpty(cust.getId())) {
                throw new CrmException("参数异常：customerId不能为空");
            }
        	if (cust == null || StringUtils.isEmpty(cust.getTel())) {
                throw new CrmException("参数异常：customerId不能为空");
            }
		    customerInfoBiz.modifyCustomer(cust);
		    //日志
    	    ActionLogger.log(getRequest(), cust.getId(), cust.toString());
			return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("modifyCustomer failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 初始化新增回访记录
     * @return 操作结果
     */
    public String initAddReturnVist() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	try {
    		isTelEditable = true;
			customer = customerInfoBiz.findCustomerInfoById(customerId);
			//屏蔽电话号码
    		if (customer!=null) {
    			if (!user.getAccountID().equals(customer.getSaleMan())) {
    				hidTelStr = Tools.screenPhoneNumber(customer.getTel());
    				//电话号码不可编辑
        			isTelEditable = false;
    			}
    		}
			return success();
		} catch (Exception e) {
			logger.error("initAddReturnVist failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 新增回访记录 
     * @return 操作结果
     */
    public String addReturnVist() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	CustomerInfo cust  = (CustomerInfo)JSON.parseObject(customerJson, CustomerInfo.class);
		try {
			returnVist = customerInfoBiz.addReturnVist(cust, returnVistContent, customerConsultContent, user.getAccountID(), operType);
			ActionLogger.log(getRequest(), cust.getId(), cust.toString());
			return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("addReturnVist failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 初始化修改回访记录 
     * @return 操作结果
     */
    public String initmodifyReturnVist() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	try {
    		isTelEditable = true;
			customer = customerInfoBiz.findCustomerInfoById(customerId);
			//屏蔽电话号码
    		if (customer!=null) {
    			if (!user.getAccountID().equals(customer.getSaleMan())) {
    				hidTelStr = Tools.screenPhoneNumber(customer.getTel());
    				//电话号码不可编辑
        			isTelEditable = false;
    			}
    		}
			returnVist = customerInfoBiz.findReturnVistById(returnVistId);
//			customerConsult = customerInfoBiz.findCustomerConsultById(customerConsultId);
			return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("initmodifyReturnVist failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 修改回访记录
     * @return 操作结果
     */
    public String modifyReturnVist() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	try {
    		CustomerInfo cust  = (CustomerInfo)JSON.parseObject(customerJson, CustomerInfo.class);
        	if (cust == null || StringUtils.isEmpty(cust.getId())) {
                throw new CrmException("参数异常：customerId不能为空");
            }
        	returnVist = customerInfoBiz.modifyReturnVist(cust, returnVistContent, customerConsultContent, returnVistId, user.getAccountID(), operType);
        	ActionLogger.log(getRequest(), cust.getId(), cust.toString());
        	return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("modifyReturnVist failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 提交
     * @return 操作结果
     */
    public String commitCustomer(){
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	try {
    		CustomerInfo cust  = (CustomerInfo)JSON.parseObject(customerJson, CustomerInfo.class);
        	if(flag != null && flag.equals("returnVisit")){
        	    cust.setStatus(CustomerInfo.RETURNVISIT_STATUS);
//        	    cust.setSubmitTime(new Date());
        	} else if(flag != null && flag.equals("fomal")){
        	    cust.setStatus(CustomerInfo.OFFICIAL_STATUS);
        	}
        	if(StringUtils.isEmpty(cust.getId())){
        		cust.setSaleMan(user.getAccountID());
        	}
		    customer = customerInfoBiz.modifyCustomer(cust);
			return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("modifyCustomer failed: ", e);
			return error("系统错误");
		}
    }
    
    /**
     * 初始化详情
     * @return 操作结果
     */
    public String initFrame() {
    	return success();
	}
    
    /**
     * 查看详情
     * @return 操作结果
     */
    public String viewCustomer() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	try {
    		customer = customerInfoBiz.findCustomerInfoById(customerId);
    		//屏蔽电话号码
    		if (customer!=null) {
    			if (!user.getAccountID().equals(customer.getSaleMan())) {
    				customer.setTel(Tools.screenPhoneNumber(customer.getTel()));
    			}
    		}
    		return success();
		} catch (Exception e) {
			logger.error("viewCustomer failed: ", e);
			return error(e.getMessage());
		}
    }
    
    /**
     * 获取客户信息
     * @return 操作结果
     */
    public String viewCustomerInfo() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        try {
            if(customerId == null || StringUtils.isEmpty(customerId)){
                return success();
            }
            customer = customerInfoBiz.findCustomerInfoById(customerId);
            if (customer!=null) {
    			if (!user.getAccountID().equals(customer.getSaleMan())) {
    				customer.setTel(Tools.screenPhoneNumber(customer.getTel()));
    			}
    		}
            return success();
        } catch (Exception e) {
            logger.error("getCustomerInfo failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询正式客户列表
     * @return 操作结果
     */
	public String queryFomalCustomerList() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
            CustomerInfoQueryParameters qp = new CustomerInfoQueryParameters();
            // 客户名称
            if (StringUtils.isNotEmpty(custName)) {
                qp.addParameter("custName", custName);
            }
            // 电话
            if (StringUtils.isNotEmpty(tel)) {
                qp.addParameter("tel", tel);
            }
            // 邮箱
            if (StringUtils.isNotEmpty(email)) {
                qp.addParameter("email", email);
            }
            // 推荐产品
            if (StringUtils.isNotEmpty(recommendProduct)) {
                qp.addParameter("recommendProduct", recommendProduct);
            }
            //客户状态为正式
            qp.addParameter("status", CustomerInfo.OFFICIAL_STATUS);
            //客户经理为当前用户
            qp.setSaleMan(user.getAccountID());
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            listPage = customerInfoBiz.queryCustomerInfoList(qp);
            if (listPage!=null && listPage.getDataList()!=null && !listPage.getDataList().isEmpty()) {
                for (CustomerInfo cust : listPage.getDataList()) {
                	if (!user.getAccountID().equals(cust.getSaleMan())) {
                		cust.setTel(Tools.screenPhoneNumber(cust.getTel()));
                	}
                }
            }
            return success();
        } catch (Exception e) {
            logger.error("queryFomalCustomerList failed: ", e);
            return error("系统错误");
        }
	}   
	
	/**
	 * 
	 * 客户下拉框
	 *
	 * <pre>
	 * 修改日期		修改人	修改原因
	 * 2014-5-24	黄云耿	新建
	 * </pre>
	 */
	public String initCustomerInfoSel() {
		SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
     // 多个客户状态
        String[] multipleStatus = null;
        if (StringUtils.isNotEmpty(multipleCustomerStatus)) {
            multipleStatus = multipleCustomerStatus.split(",");
        }
        try {
        	List<AutoCompleteData> custDatas = customerInfoBiz.queryAutoCompleteData(user.getAccountID(), multipleStatus, pageSize);
            String sHtml = "";
            if (custDatas != null) {
                sHtml = new CustomerInfoSelect(custDatas).toString();
            }
            if(jsoncallback != null){
                htmlValue = sHtml;
                return success();
            } else {
                HTMLResponse.outputHTML(getResponse(), sHtml);
                return null;
            }
        } catch (Exception e) {
            logger.error("initCustomerInfoSel failed: ", e);
            return error();
        }
    }
	
	public String queryTransactionRecordPage() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        if (StringUtils.isBlank(customerId)) {
        	return error("客户ID不能为空");
        }
		try {
//			if (StringUtils.isNotEmpty(customerId)) {
//				customer = customerInfoBiz.findCustomerInfoById(customerId);
//				if (customer == null) {
//					return error("该客户不存在！");
//				}
//			}
			transactionRecordListPage = transactionPoint.queryTransactionRecordPage(customerId, pageSize, pageNo, sortCol, ascend);
            return success();
        } catch (Exception e) {
            logger.error("queryFomalCustomerList failed: ", e);
            return error("系统错误");
        }
	}
	
	public String queryPaymentRecordPage() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
         	return error("请先登录");
        }
        if (StringUtils.isBlank(customerId)) {
        	return error("客户ID不能为空");
        }
		try {
			paymentRecordListPage = transactionPoint.queryPaymentRecordPage(customerId, pageSize, pageNo, sortCol, ascend);
            return success();
        } catch (Exception e) {
            logger.error("queryFomalCustomerList failed: ", e);
            return error("系统错误");
        }
	}
	
	public String queryMessageList() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
			if (StringUtils.isNotEmpty(customerId)) {
				customer = customerInfoBiz.findCustomerInfoById(customerId);
				if (customer == null) {
					return error("该客户不存在！");
				}
			}
            messageListPage = messagePoint.queryMessagePage(user.getAccountID(), customer.getTel(), pageSize, pageNo, sortCol, ascend);
            return success();
        } catch (Exception e) {
            logger.error("queryFomalCustomerList failed: ", e);
            return error("系统错误");
        }
	}
    
	public String commitFromView() {
		SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	try {
		    customer = customerInfoBiz.txCommitFromView(customerId);
			return success();
		} catch (CrmException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("commitFromView failed: ", e);
			return error("系统错误");
		}
	}
	
	public String changeMemoMark() {
    	try {
		    customerInfoBiz.txChangeMemoMark(customerId, memoMark);
			return success();
		} catch (Exception e) {
			logger.error("commitFromView failed: ", e);
			return error("系统错误");
		}
	}
	
    public void setCustomerManage(String customerManage) {
        this.customerManage = customerManage;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    /**
     * set the multipleCustomerStatus to set
     * @param multipleCustomerStatus the multipleCustomerStatus to set
     */
    public void setMultipleCustomerStatus(String multipleCustomerStatus) {
        this.multipleCustomerStatus = multipleCustomerStatus;
    }

    /**
     * set the dataSource to set
     * @param dataSource the dataSource to set
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public void setCustomerInfoBiz(ICustomerInfoBiz customerInfoBiz) {
        this.customerInfoBiz = customerInfoBiz;
    }

    public void setCustomerInfoIds(String customerInfoIds) {
        this.customerInfoIds = customerInfoIds;
    }

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

    public ListPage<CustomerConsult> getConsultList() {
        return consultList;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }

    public void setCustomerConsultId(String customerConsultId) {
        this.customerConsultId = customerConsultId;
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

	public ListPage<CustomerAppointment> getAppointmentList() {
		return appointmentList;
	}

	public CustomerAppointment getCustomerAppointment() {
		return customerAppointment;
	}

	public void setCustomerAppointment(CustomerAppointment customerAppointment) {
		this.customerAppointment = customerAppointment;
	}

	public Date getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Integer getWarnOpportunity() {
		return warnOpportunity;
	}

	public void setWarnOpportunity(Integer warnOpportunity) {
		this.warnOpportunity = warnOpportunity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the customer
	 */
	public CustomerInfo getCustomer() {
		return customer;
	}

	/**
	 * @param customerJson the customerJson to set
	 */
	public void setCustomerJson(String customerJson) {
		this.customerJson = customerJson;
	}
	

	public void setMemoMark(Integer memoMark) {
		this.memoMark = memoMark;
	}

	public void setBgnSubmitTime(Date bgnSubmitTime) {
		this.bgnSubmitTime = bgnSubmitTime;
	}

	public void setEndSubmitTime(Date endSubmitTime) {
		this.endSubmitTime = endSubmitTime;
	}
	    
	public String getCustomerAppointmentId() {
		return customerAppointmentId;
	}

	public void setCustomerAppointmentId(String customerAppointmentId) {
		this.customerAppointmentId = customerAppointmentId;
	}

	public void setCustomerAppointmentBiz(
			ICustomerAppointmentBiz customerAppointmentBiz) {
		this.customerAppointmentBiz = customerAppointmentBiz;
	}

    public ListPage<ReturnVist> getVisitList() {
        return visitList;
    }

    public void setReturnVistId(String returnVistId) {
        this.returnVistId = returnVistId;
    }

	/**
	 * @return the returnVistContent
	 */
	public String getReturnVistContent() {
		return returnVistContent;
	}

	/**
	 * @param returnVistContent the returnVistContent to set
	 */
	public void setReturnVistContent(String returnVistContent) {
		this.returnVistContent = returnVistContent;
	}

	/**
	 * @return the returnVist
	 */
	public ReturnVist getReturnVist() {
		return returnVist;
	}

	/**
	 * @param returnVist the returnVist to set
	 */
	public void setReturnVist(ReturnVist returnVist) {
		this.returnVist = returnVist;
	}

	/**
	 * @return the customerAppointmentJson
	 */
	public String getCustomerAppointmentJson() {
		return customerAppointmentJson;
	}

	/**
	 * @param customerAppointmentJson the customerAppointmentJson to set
	 */
	public void setCustomerAppointmentJson(String customerAppointmentJson) {
		this.customerAppointmentJson = customerAppointmentJson;
	}

	/**
	 * @return the customerConsultContent
	 */
	public String getCustomerConsultContent() {
		return customerConsultContent;
	}

	/**
	 * @param customerConsultContent the customerConsultContent to set
	 */
	public void setCustomerConsultContent(String customerConsultContent) {
		this.customerConsultContent = customerConsultContent;
	}

	/**
	 * @return the customerConsult
	 */
	public CustomerConsult getCustomerConsult() {
		return customerConsult;
	}

	/**
	 * @param customerConsult the customerConsult to set
	 */
	public void setCustomerConsult(CustomerConsult customerConsult) {
		this.customerConsult = customerConsult;
	}

	/**
	 * @return the operType
	 */
	public Integer getOperType() {
		return operType;
	}

	/**
	 * @param operType the operType to set
	 */
	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	/**
     * @param queryFlag the queryFlag to set
     */
    public void setQueryFlag(String queryFlag) {
        this.queryFlag = queryFlag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

	/**
	 * @param groupExtBiz the groupExtBiz to set
	 */
//	public void setGroupExtBiz(IGroupExtBiz groupExtBiz) {
//		this.groupExtBiz = groupExtBiz;
//	}

	/**
	 * @return the messageListPage
	 */
	public ListPage<MessageInfo> getMessageListPage() {
		return messageListPage;
	}

	/**
	 * @param messagePoint the messagePoint to set
	 */
	public void setMessagePoint(IMessagePoint messagePoint) {
		this.messagePoint = messagePoint;
	}
	
    public String getHtmlValue() {
        return htmlValue;
    }

    public void setJsoncallback(String jsoncallback) {
        this.jsoncallback = jsoncallback;
    }

	public void setTransactionPoint(ITransactionPoint transactionPoint) {
		this.transactionPoint = transactionPoint;
	}

	public ListPage<TransactionRecord> getTransactionRecordListPage() {
		return transactionRecordListPage;
	}

	public ListPage<PaymentRecord> getPaymentRecordListPage() {
		return paymentRecordListPage;
	}

	public List<AutoCompleteData> getAutoCompleteDatas() {
		return autoCompleteDatas;
	}

	public Boolean getIsTelEditable() {
		return isTelEditable;
	}

	public void setIsTelEditable(Boolean isTelEditable) {
		this.isTelEditable = isTelEditable;
	}

	public String getHidTelStr() {
		return hidTelStr;
	}

	public void setHidTelStr(String hidTelStr) {
		this.hidTelStr = hidTelStr;
	}
}
