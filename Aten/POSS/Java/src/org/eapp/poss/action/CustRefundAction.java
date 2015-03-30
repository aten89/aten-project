package org.eapp.poss.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.poss.blo.ICustRefundBiz;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.param.CustRefundQueryParameters;
import org.eapp.poss.dto.CustRefundDisposalProcDTO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.CustRefund;
import org.eapp.poss.util.Tools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

import com.alibaba.fastjson.JSON;


/**
 * 客户退款登记ACTION
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	钟华杰	新建
 * </pre>
 */
public class CustRefundAction extends BaseAction {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4150056901721108090L;
    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustRefundAction.class);

    /**
     * 客户退款业务逻辑层接口
     */
    private ICustRefundBiz custRefundBiz;
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
     * 查询客户退款登记列表
     */
    private ListPage<CustRefund> listPage;
    /**
     * 客户退款JSON
     */
    private String custRefundJson;
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
     * 退款ID
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
     * 客户退款
     */
    private CustRefund custRefund;
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
     * 退款处理过程
     */
    private List<CustRefundDisposalProcDTO> disposalProcDTOs;
    /**
     * 当前用户
     */
    private String userAccountID;
    /**
     * 当前用户显示名称
     */
    private String userDisplayName;
    
    //申请时间
    private Date applyTimeBegin;
    private Date applyTimeEnd;
    private String custId;
    private String saleManId;
    private String prodId;
    private Boolean flowFlag;
    private String nowDate;
    
    /*******************文件上传相关***************/
    private String referid;
    private List<File> uploadFiles;				//上传文件
    private List<String> uploadFilesFileName;	// 上传文件的文件名
    private long maxUploadSize; 				// 最大上传量
    private String[] delFilenames;				// 要删除附件的名称
    
    private Set<Attachment> custRefundAttachments;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 
    private CustRefundQueryParameters createQueryParameters() {
    	CustRefundQueryParameters qp = new CustRefundQueryParameters();
    	if(null != applyTimeBegin) {
    		qp.setApplyTimeBegin(applyTimeBegin);
    	}
    	
    	if(null != applyTimeEnd) {
    		qp.setApplyTimeEnd(applyTimeEnd);
    	}
    	
    	if(StringUtils.isNotEmpty(custId)) {
    		qp.setCustId(custId);
    	}
    	
    	if(StringUtils.isNotEmpty(saleManId)) {
    		qp.setSaleManId(saleManId);
    	}
    	
    	if(StringUtils.isNotEmpty(prodId)) {
    		qp.setProdId(prodId);
    	}
    	
    	qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        // 增加对排序列的处理
        if (StringUtils.isNotEmpty(sortCol)) {
            qp.addOrder(sortCol, ascend);
        }
    	return qp;
    }
    
    /**
     * 查询草稿列表
     * @return
     */
    public String queryCustRefundDraftList() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }

        try {
            CustRefundQueryParameters qp = createQueryParameters();
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
            	qp.setCurrentUserId(user.getAccountID());
            }
            
            userAccountID = user.getAccountID();
            userDisplayName = user.getDisplayName();
            listPage = custRefundBiz.queryCustRefundDraftList(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryCustRefundList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 新增客户退款
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-22	钟华杰	新建
     * </pre>
     */
    public String addCustRefund() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
        	custRefund = custRefundBiz.addCustRefund(custRefundJson, user, flowFlag);
            
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("addCustRefund failed: ", e);
            return error("系统错误");
        }
    }
  
    /**
     * 初始化编辑客户草稿页面
     * @return
     */
    public String initEditCustRefund() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        try {
        	if(!StringUtils.isEmpty(id)) {
        		custRefund = custRefundBiz.getCustRefundById(id);
        	}
        	
        	userAccountID = user.getAccountID();
        	userDisplayName = user.getDisplayName();
        	
        	nowDate = df.format(new Date());
        	
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("initEditCustRefund failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 修改退款申请
     * @return
     */
    public String modifyCustRefund() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        try {
        	custRefund = custRefundBiz.modifyCustRefund(custRefundJson, user, flowFlag);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("modifyCustRefund failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 从流程里读跳转路径
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-23	钟华杰	新建
     * </pre>
     */
    public String viewCustRefund() {
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
     * 2014-5-23	钟华杰	新建
     * </pre>
     */
    public String initCustRefundApprove() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        try {
        	if(StringUtils.isEmpty(flag) || StringUtils.isEmpty(id)) {
        		throw new PossException("参数不能为空");
        	}
        	
        	custRefund = custRefundBiz.getCustRefundById(id);
            disposalProcDTOs = custRefundBiz.csGetDisposalProc(id);
            userAccountID = user.getAccountID();
            userDisplayName = user.getDisplayName();
            nowDate = df.format(new Date());
        	
        	if("custRefundApprove".equals(flag)) {
        		// 运营审批
        		if (StringUtils.isEmpty(tiid)) {
                    throw new PossException("参数不能为空");
                }
        		transitions = taskBiz.getTaskTransitions(tiid);
                return "custRefundApprove";
        	} else if("againModify".equals(flag)) {
        		// 驳回修改
        		if (StringUtils.isEmpty(tiid)) {
                    throw new PossException("参数不能为空");
                }
        		transitions = taskBiz.getTaskTransitions(tiid);
        		// 设置上一次的审批人
        		Map<String, String> resultMap = custRefundBiz.queryFlowVarialbleMap(tiid);
        		custRefund.setApprover(resultMap.get("appointTo"));
        		custRefund.setApproverName(resultMap.get("appointToName"));
        		
                return "againModify";
        	} else if("viewDetail".equals(flag)) {
        		// 查看详情
                return "viewDetail";
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
     * 查询待办客户退款列表
     * @return 操作结果
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-20	钟华杰	新建
     * </pre>
     */
    public String queryCustRefundDealList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }

        List<String> userRoles = new ArrayList<String>(0);
        List<Name> roles = user.getRoles();
        if(roles != null && !roles.isEmpty()) {
        	for(Name role : roles) {
        		userRoles.add(role.getName());
        	}
        }
        
        try {
            CustRefundQueryParameters qp = createQueryParameters();
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            
            userAccountID = user.getAccountID();
            userDisplayName = user.getDisplayName();
            listPage = custRefundBiz.queryCustRefundList(qp, userRoles);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryCustRefundList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询跟踪列表
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public String queryCustRefundTrackList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            CustRefundQueryParameters qp = createQueryParameters();
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            
            listPage = custRefundBiz.queryTrackCustRefundList(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryTrackCustRefundList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 查询退款归档列表
     * @return 退款归档列表
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public String queryCustRefundArchiveList() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
          return error("请先登录");
        }
        
        try {
            CustRefundQueryParameters qp = createQueryParameters();
            // 当前账号
            if (StringUtils.isNotEmpty(user.getAccountID())) {
                qp.addParameter("currentUserId", user.getAccountID());
            }
            
            listPage = custRefundBiz.queryArchiveCustRefundList(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryArchiveCustRefundList failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 驳回
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-24	钟华杰	新建
     * </pre>
     */
    public String rejectCustRefund() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        try {
            custRefundBiz.txRejectCustRefund(id, tiid, transition, taskCommentId, comment);
            return success();
        } catch (Exception e) {
            logger.error("approvePayment failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 审批通过
     * @return
     */
    public String approveCustRefund() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
    	if (user == null) {
    		return error("请先登录");
    	}
    	try {
    		custRefundBiz.txApproveCustRefund(id, tiid, transition, taskCommentId, comment);
    		return success();
    	} catch (Exception e) {
    		logger.error("approvePayment failed: ", e);
    		return error("系统错误");
    	}
    }
    
    /**
     * 重新提交（最新）
     * @return
     */
    public String againCommitCustRefund() {
        SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
        if (user == null) {
            return error("请先登录");
        }
        try {
        	// 解析退款对象
            CustRefund CustRefund  = (CustRefund)JSON.parseObject(custRefundJson, CustRefund.class);
            
            custRefund = custRefundBiz.txAgainCommitCustRefund(CustRefund, tiid, taskCommentId, transition, comment);
            return success();
        } catch (Exception e) {
            logger.error("approvePayment failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 重新提交 
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-26	钟华杰	新建
     * </pre>
     */
    public String modifyCustRefundAndEndTransition() {
        try {
            // 解析退款对象
            CustRefund CustRefund  = (CustRefund)JSON.parseObject(custRefundJson, CustRefund.class);
            custRefundBiz.modifyCustRefundAndEndTransition(CustRefund, tiid, taskCommentId, transition, comment);
            return success();
        } catch (Exception e) {
            logger.error("modifyPaymentAndEndTransition failed: ", e);
            return error("系统错误");
        }
    }
    
    /**
     * 处理流程任务
     * @return
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-25	钟华杰	新建
     * </pre>
     */
    public String dealCustRefund() {
        try {
            custRefundBiz.txDealTask(id, tiid, taskCommentId, transition, comment);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("dealPayment failed: ", e);
            return error(e.getMessage());
        }
    }
    
    /************附件上传相关*****************/
    /**
     * 根据退款须知ID加载对应的附件列表
     * @return
     */
    public String loadCustRefundAttachments() {
        try {
        	custRefundAttachments = custRefundBiz.getCustRefundById(referid).getCustRefundAttach();
            return success();
        } catch (Exception e) {
        	logger.error("loadRefuncNoticeAttachments failed:", e);
            return error("系统错误");
        }
    }
    
    /**
     * 上传退款须知附件（带业务）
     * @throws IOException
     */
    public String uploadCustRefundFile() throws IOException {
        try {
        	//先上传文件
            List<Attachment> files = uploadFiles();
            //对业务进行操作
            custRefundBiz.txBatchUploadCustRefundAttachment(referid, delFilenames, files);
            // HtmlResponse MessageDTO 不存在
//            planUpgradeCustomerService.txUploadlicenceAttachment(referid, delFilenames, files);
//            HTMLResponse.outputHTML(getResponse(), new MessageDTO("1", "保存成功").toString());
        } catch (PossException e) {
//            HTMLResponse.outputHTML(getResponse(), new MessageDTO("0", e.getMessage()).toString());
        	e.printStackTrace();
        } catch (Exception e) {
            logger.error("上传许可证失败", e);
            e.printStackTrace();
//            HTMLResponse.outputHTML(getResponse(), new MessageDTO("0", "上载文件失败").toString());
        }
        return success("1");
    }
    
    /**
     * 上传文件，并且转化为Attachment
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private List<Attachment> uploadFiles() throws PossException, FileNotFoundException, IOException {
        List<Attachment> files = new ArrayList<Attachment>();
        if (uploadFiles != null && uploadFiles.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            String dir = SysConstants.CUST_REFUND_DIR + sdf.format(new Date());
            for (int i = 0; i < uploadFiles.size(); i++) {
                String fileName = uploadFilesFileName.get(i);
                if (fileName == null || fileName.trim().equals("")) {
                    continue;
                }
                File file = uploadFiles.get(i);
                if (file.length() == 0) {
                    throw new PossException("文件“" + fileName + "”大小不能为空！");
                }
//                if (maxUploadSize > 0 && maxUploadSize < file.length()) {
//                    throw new PossException("文件“" + fileName + "”不能大于" + maxUploadSize / 1024 + "KB");
//                }
                Attachment am = new Attachment(fileName, file.length());
                String path = dir + Tools.generateUUID() + am.getFileExt();
                am.setFilePath(path);
                // 保存附件
                FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), new FileInputStream(file));
                files.add(am);
            }
        }
        return files;
    }
    
    
    /************************************get and set**************************************/
   
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortCol(String sortCol) {
        this.sortCol = sortCol;
    }
    
    public ListPage<CustRefund> getListPage() {
        return listPage;
    }

    public void setCustRefundBiz(ICustRefundBiz custRefundBiz) {
        this.custRefundBiz = custRefundBiz;
    }

    public void setCustRefundJson(String custRefundJson) {
        this.custRefundJson = custRefundJson;
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

    public CustRefund getCustRefund() {
        return custRefund;
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

    public List<CustRefundDisposalProcDTO> getDisposalProcDTOs() {
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

	public String getReferid() {
		return referid;
	}

	public void setReferid(String referid) {
		this.referid = referid;
	}

	public List<File> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<File> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public List<String> getUploadFilesFileName() {
		return uploadFilesFileName;
	}

	public void setUploadFilesFileName(List<String> uploadFilesFileName) {
		this.uploadFilesFileName = uploadFilesFileName;
	}

	public long getMaxUploadSize() {
		return maxUploadSize;
	}

	public void setMaxUploadSize(long maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	public String[] getDelFilenames() {
		return delFilenames;
	}

	public void setDelFilenames(String[] delFilenames) {
		this.delFilenames = delFilenames;
	}

	public Set<Attachment> getCustRefundAttachments() {
		return custRefundAttachments;
	}

	public void setCustRefundAttachments(Set<Attachment> custRefundAttachments) {
		this.custRefundAttachments = custRefundAttachments;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getSaleManId() {
		return saleManId;
	}

	public void setSaleManId(String saleManId) {
		this.saleManId = saleManId;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public void setApplyTimeBegin(Date applyTimeBegin) {
		this.applyTimeBegin = applyTimeBegin;
	}

	public void setApplyTimeEnd(Date applyTimeEnd) {
		this.applyTimeEnd = applyTimeEnd;
	}

	public void setFlowFlag(Boolean flowFlag) {
		this.flowFlag = flowFlag;
	}

	/**
	 * @return the nowDate
	 */
	public String getNowDate() {
		return nowDate;
	}

	/**
	 * @param nowDate the nowDate to set
	 */
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
}
