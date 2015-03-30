package org.eapp.poss.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.poss.blo.ICustPaymentBiz;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.dao.param.CustPaymentQueryParameters;
import org.eapp.poss.dto.CustPaymentSelect;
import org.eapp.poss.dto.PaymentDisposalProcDTO;
import org.eapp.poss.dto.ProdInfoSelect;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.poss.util.XMLResponse;
import org.eapp.util.hibernate.ListPage;

import com.alibaba.fastjson.JSON;


/**
 * 客户划款登记ACTION
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	黄云耿	新建
 * </pre>
 */
public class CustPaymentAction extends BaseAction {
    
    /**
     * 
     */
    private static final long serialVersionUID = -16205565932769179L;

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustPaymentAction.class);

    /**
     * 客户划款业务逻辑层接口
     */
    private ICustPaymentBiz custPaymentBiz;
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
     * 查询客户划款登记列表
     */
    private ListPage<CustPayment> listPage;
    /**
     * 客户划款JSON
     */
    private String paymentJson;
    /**
     * 流程跳转url
     */
    private String viewUrl;
    /**
     * 任务实例id
     */
    private String tiid;
    private String taskid;
    /**
     * 划款ID
     */
    private String id;
    /**
     * 流程任务逻辑层接口
     */
    private ITaskBiz taskBiz;
    /**
     * 流程步骤标志
     */
    private String flag;
    /**
     * 步骤集
     */
    private List<String> transitions;
    /**
     * 客户划款
     */
    private CustPayment custPayment;
    /**
     * 流程步骤名称
     */
    private String transition;
    /**
     * 审批意见
     */
    private String comment;
    /**
     * 任务处理意见ID
     */
    private String taskCommentId;
    /**
     * 划款处理过程
     */
    private List<PaymentDisposalProcDTO> disposalProcDTOs;
    /**
     * 当前用户
     */
    private String userAccountID;
    /**
     * 当前用户显示名称
     */
    private String userDisplayName;
    
    private File attachment;
    
    private String extName;// 升级包说明文件扩展名
    
    private String displayName;// 升级包说明文件名
    private Date bgnTransferDate;
    private Date endTransferDate;
    private String custId;
    private String prodId;
    private String payType;
    private String custName;
 
//    private String igoreUserFlag;
    
    private String saleManId;
    private String formStatus;
  
    /**
     * 查询待办客户划款列表
     * @return 操作结果
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-20	黄云耿	新建
     * </pre>
     */
    public String queryCustPaymentList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }

        try {
            CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            if (StringUtils.isNotEmpty(flag) && flag.equals("true")) {
                qp.setStandardFlag(true);
            } 
            if (StringUtils.isNotEmpty(flag) && flag.equals("false")) {
                qp.setStandardFlag(false);
            } 
            if (bgnTransferDate != null) {
                qp.setBgnTransferDate(bgnTransferDate);
            }
            if (endTransferDate != null) {
                qp.setEndTransferDate(endTransferDate);
            } 
            if (StringUtils.isNotEmpty(custId)) {
                qp.setCustId(custId);
            } 
            if (StringUtils.isEmpty(custId) && StringUtils.isNotEmpty(custName)) {
                qp.setCustName(custName);
            } 
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            } 
            if (StringUtils.isNotEmpty(payType)) {
                qp.setPayType(payType);
            } 
            
            List<String> userRoles = new ArrayList<String>(0);
            List<Name> roles = user.getRoles();
            if(roles != null && !roles.isEmpty()) {
                for(Name role : roles) {
                    userRoles.add(role.getName());
                }
            }
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            userAccountID = user.getAccountID();
            userDisplayName = user.getDisplayName();
            listPage = custPaymentBiz.queryCustPaymentList(qp, userRoles);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryCustPaymentList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询划款归档列表
     * @return 划款归档列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public String queryArchiveCustPaymentList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
          
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            
            if (StringUtils.isNotEmpty(flag) && flag.equals("true")) {
                qp.setStandardFlag(true);
            } 
            if (StringUtils.isNotEmpty(flag) && flag.equals("false")) {
                qp.setStandardFlag(false);
            } 
            
            if (bgnTransferDate != null) {
                qp.setBgnTransferDate(bgnTransferDate);
            }
            if (endTransferDate != null) {
                qp.setEndTransferDate(endTransferDate);
            } 
            if (StringUtils.isNotEmpty(custId)) {
                qp.setCustId(custId);
            } 
            if (StringUtils.isEmpty(custId) && StringUtils.isNotEmpty(custName)) {
                qp.setCustName(custName);
            } 
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            } 
            if (StringUtils.isNotEmpty(payType)) {
                qp.setPayType(payType);
            } 
            
//            if (StringUtils.isNotEmpty(igoreUserFlag)) {
//            	qp.addParameter("igoreUserFlag", igoreUserFlag);
//            }
//            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            listPage = custPaymentBiz.queryArchiveCustPaymentList(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryArchiveCustPaymentList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询跟踪列表
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public String queryTrackCustPaymentList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
          
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            
            if (StringUtils.isNotEmpty(flag)) {
                qp.setStandardFlag(Boolean.valueOf(flag));
            } 
            
            if (bgnTransferDate != null) {
                qp.setBgnTransferDate(bgnTransferDate);
            }
            if (endTransferDate != null) {
                qp.setEndTransferDate(endTransferDate);
            } 
            if (StringUtils.isNotEmpty(custId)) {
                qp.setCustId(custId);
            } 
            if (StringUtils.isEmpty(custId) && StringUtils.isNotEmpty(custName)) {
                qp.setCustName(custName);
            } 
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            } 
            if (StringUtils.isNotEmpty(payType)) {
                qp.setPayType(payType);
            } 
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            listPage = custPaymentBiz.queryTrackCustPaymentList(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryTrackCustPaymentList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询划款归档列表
     * @return 划款归档列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public String queryAllCustPaymentList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
            
            if (StringUtils.isNotEmpty(flag) && flag.equals("true")) {
                qp.setStandardFlag(true);
            } 
            if (StringUtils.isNotEmpty(flag) && flag.equals("false")) {
                qp.setStandardFlag(false);
            } 
            
            if (bgnTransferDate != null) {
                qp.setBgnTransferDate(bgnTransferDate);
            }
            if (endTransferDate != null) {
                qp.setEndTransferDate(endTransferDate);
            } 

            if (StringUtils.isNotEmpty(custName)) {
                qp.setCustName(custName);
            } 
            if (StringUtils.isNotEmpty(saleManId)) {
                qp.setSaleManId(saleManId);
            } 
            if (StringUtils.isNotEmpty(formStatus)) {
                qp.setFormStatus(formStatus);
            } 
            if (StringUtils.isNotEmpty(prodId)) {
                qp.setProdId(prodId);
            } 
            if (StringUtils.isNotEmpty(payType)) {
                qp.setPayType(payType);
            }
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            listPage = custPaymentBiz.queryAllCustPaymentList(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryArchiveCustPaymentList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 初始化新增
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-7	黄云耿	新建
     * </pre>
     */
    public String initAddCustomerPay() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        try {
            custPayment = new CustPayment();
            custPayment.setTransferDate(new Date());
            if(flag.equals("addOldCustomerPay")){
                return "addOldCustomerPay";
            } else if(flag.equals("addNewCustomerPay")){
                return "addNewCustomerPay";
            }
            
            return error("该步骤不存在!");
        } catch (Exception e) {
            logger.error("initAddCustomerPay failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 新增客户划款
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-22	黄云耿	新建
     * </pre>
     */
    public String addCustPayment() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            custPaymentBiz.addCustPayment(paymentJson, user);
            
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("addCustPayment failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 从流程里读跳转路径
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	黄云耿	新建
     * </pre>
     */
    public String viewPayment() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        if (StringUtils.isEmpty(taskid)) {
        	return error("任务不存在");
        }
        try {
            
            List<String> userRoles = new ArrayList<String>();
    		List<Name> roles = user.getRoles();
    		if (roles != null && !roles.isEmpty()) {
    			for (Name role : roles) {
    				userRoles.add(role.getName());
    			}
    		}
    		
            viewUrl = taskBiz.txStartTask(taskid, user.getAccountID(), userRoles);
            viewUrl += "&tiid=" + tiid;
            
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("viewIssueInfo failed:", e);
            return error("系统错误");
        }
    }
    
    /**
     * 流程步骤处理页面
     * @return 页面视图
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	黄云耿	新建
     * </pre>
     */
    public String initPaymentApprove() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        try {
            if (StringUtils.isEmpty(flag)) {
                throw new PossException("参数不能为空");
            }
            if ("viewInfo".equals(flag)){
                if (StringUtils.isEmpty(id)) {
                    throw new PossException("参数不能为空");
                }
            } else{
                if (StringUtils.isEmpty(id) || StringUtils.isEmpty(tiid)) {
                    throw new PossException("参数不能为空");
                }
                transitions = taskBiz.getTaskTransitions(tiid);
            }
            custPayment = custPaymentBiz.getCustPaymentById(id, flag);
            disposalProcDTOs = custPaymentBiz.csGetDisposalProc(id);
            
            if ("uploadSlip".equals(flag)) {
                // 上传凭条
                return "uploadSlip";
            } else if ("confirmPayment".equals(flag)) {
                // 确认到款
                return "confirmPayment";
            } else if ("againModify".equals(flag)) {
                // 重新修改
                return "againModify";
            } else if ("paymentApprove".equals(flag)) {
                // 审批划款
                return "paymentApprove";
            } else if ("viewInfo".equals(flag)) {
                // 查看详情
                return "viewInfo";
            } else {
                return error("该流程步骤不存在!");
            }
        } catch (PossException e1) {
            return error(e1.getMessage());
        } catch (Exception e) {
            logger.error("initPaymentApprove failed:", e);
            return error("系统出错");
        }
    }
    
    /**
     * 确认到款
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	黄云耿	新建
     * </pre>
     */
    public String approvePayment() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        try {
            // 解析划款对象
            CustPayment custPayment  = (CustPayment)JSON.parseObject(paymentJson, CustPayment.class);
            
            custPaymentBiz.txApprovePayment(custPayment, tiid, transition, taskCommentId, comment);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("approvePayment failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 审批划款
     * @return
     *
     * <pre>
     * 修改日期     修改人 修改原因
     * 2014-5-24    黄云耿 新建
     * </pre>
     */
    public String approvePaymentLeader() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        try {
            custPaymentBiz.txApprovePaymentLeader(id, tiid, transition, taskCommentId, comment);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("approvePaymentLeader failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 处理流程任务
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-25	黄云耿	新建
     * </pre>
     */
    public String dealPayment() {
        try {
            custPaymentBiz.txDealTask(id, tiid, taskCommentId, transition, comment, flag);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("dealPayment failed: ", e);
            return error("系统错误");
        }
    }
    
    public String txModifyDealTask() {
        try {
            // 解析划款对象
            CustPayment custPayment  = (CustPayment)JSON.parseObject(paymentJson, CustPayment.class);
          
            custPaymentBiz.txModifyDealTask(custPayment, tiid, taskCommentId, transition, comment);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("dealPayment failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 重新提交 
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	黄云耿	新建
     * </pre>
     */
    public String modifyPaymentAndEndTransition() {
        try {
            // 解析划款对象
            CustPayment custPayment  = (CustPayment)JSON.parseObject(paymentJson, CustPayment.class);
            
            custPaymentBiz.modifyPaymentAndEndTransition(custPayment, tiid, taskCommentId, transition, comment);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("modifyPaymentAndEndTransition failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 初始化我的划款登记下拉框
     * 
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-25	钟华杰	新建
     * </pre>
     */
    public void initCustPaymentSel() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
    	if (user == null) {
            return;
        }
        try {
        	CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
        	// 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            
            // 客户ID
            if (StringUtils.isNotEmpty(custId)) {
            	qp.setCustId(custId);
            }
            
            // 产品ID
            if (StringUtils.isNotEmpty(prodId)) {
            	qp.setProdId(prodId);
            }
            
            qp.setPageSize(0);
            listPage = custPaymentBiz.queryArchiveCustPaymentListWithoutTask(qp);
            if (listPage == null || listPage.getDataList() == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new CustPaymentSelect(listPage.getDataList()).toString());
        } catch (Exception e) {
            logger.error("initCustpaymentSel failed: ", e);
            return;
        }
    }
    
    /**
     * 初始化客户划过款的产品下拉框
     */
    public void initCustProdSel() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
    	if (user == null) {
    		return;
    	}
    	try {
    		CustPaymentQueryParameters qp = new CustPaymentQueryParameters();
    		// 当前账号
    		if (StringUtils.isNotEmpty(user.getAccountID())) {
    			qp.addParameter("currentUserId", user.getAccountID());
    		}
    		
    		// 客户ID
    		if (StringUtils.isNotEmpty(custId)) {
    			qp.setCustId(custId);
    		}
    		
    		qp.setPageSize(0);
    		List<ProdInfo> resultList = custPaymentBiz.queryProdInfoList(qp);
    		if (resultList == null) {
    			return;
    		}
    		HTMLResponse.outputHTML(getResponse(), new ProdInfoSelect(resultList).toString());
    	} catch (Exception e) {
    		logger.error("initCustpaymentSel failed: ", e);
    		return;
    	}
    }
    
    public void saveAttachmentToTempDir() {
        try {
            if (attachment == null) {
                XMLResponse.outputStandardResponse(getResponse(), 0, "请上传附件！");
            } else if (attachment.length() <= 0) {
                XMLResponse.outputStandardResponse(getResponse(), 0, "不能上传0kb文件！");
            } else {
                Attachment attach = custPaymentBiz.saveAttachmentToTempDir(attachment, extName, displayName);
                // 把附件对象放入session中
                // String path = attachment.getAbsFilePath();
                String path = attach.getFilePath();
                XMLResponse.outputStandardResponse(getResponse(), 1, path);
            }

        } catch (Exception e) {
            logger.error("saveAttachmentToTempDir failed:", e);
        }
    }
    
    /**
     * 获取划款信息
     * @return
     */
    public String queryCustPaymentById() {
        try {
        	custPayment = custPaymentBiz.getCustPaymentById(id, null);
        	return success();
        } catch (Exception e) {
            logger.error("queryCustPaymentById failed: ", e);
            return error();
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
    
    public ListPage<CustPayment> getListPage() {
        return listPage;
    }

    public void setCustPaymentBiz(ICustPaymentBiz custPaymentBiz) {
        this.custPaymentBiz = custPaymentBiz;
    }

    public void setPaymentJson(String paymentJson) {
        this.paymentJson = paymentJson;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getTiid() {
        return tiid;
    }

    public void setTiid(String tiid) {
        this.tiid = tiid;
    }

    public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setId(String id) {
        this.id = id;
    }


    public void setTaskBiz(ITaskBiz taskBiz) {
        this.taskBiz = taskBiz;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<String> getTransitions() {
        return transitions;
    }

    public CustPayment getCustPayment() {
        return custPayment;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskCommentId() {
        return taskCommentId;
    }

    public void setTaskCommentId(String taskCommentId) {
        this.taskCommentId = taskCommentId;
    }

    public List<PaymentDisposalProcDTO> getDisposalProcDTOs() {
        return disposalProcDTOs;
    }

    public String getUserAccountID() {
        return userAccountID;
    }

    public void setUserAccountID(String userAccountID) {
        this.userAccountID = userAccountID;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setBgnTransferDate(Date bgnTransferDate) {
        this.bgnTransferDate = bgnTransferDate;
    }

    public void setEndTransferDate(Date endTransferDate) {
        this.endTransferDate = endTransferDate;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

//	public void setIgoreUserFlag(String igoreUserFlag) {
//		this.igoreUserFlag = igoreUserFlag;
//	}

	public void setSaleManId(String saleManId) {
		this.saleManId = saleManId;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

}
