/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.UsernameCache;
import org.eapp.comobj.SessionAccount;
import org.eapp.oa.rmi.hessian.FlowInfo;
import org.eapp.crm.rmi.hessian.CustInfo;
import org.eapp.crm.rmi.hessian.ICustomerInfoPoint;
import org.eapp.oa.rmi.hessian.IFlowConfigPoint;
import org.eapp.poss.blo.ICustPaymentBiz;
import org.eapp.poss.blo.IProdInfoBiz;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.blo.ITaskCommentBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.ICustPaymentDAO;
import org.eapp.poss.dao.ITaskDAO;
import org.eapp.poss.dao.param.CustPaymentQueryParameters;
import org.eapp.poss.dto.PaymentDisposalProcDTO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.poss.rmi.hessian.PaymentRecord;
import org.eapp.poss.rmi.hessian.TransactionRecord;
import org.eapp.poss.util.SerialNoCreater;
import org.eapp.poss.util.Tools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

/**
 * 客户信息业务逻辑层实现
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-4	黄云耿	新建
 * </pre>
 */
public class CustPaymentBiz implements ICustPaymentBiz {
    
    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(CustPaymentBiz.class);

    /**
     * 客服管理DAO接口
     */
    private ICustPaymentDAO custPaymentDAO;
    /**
     * 流程配置hessian接口
     */
    private IFlowConfigPoint flowConfigPoint;
    private ICustomerInfoPoint customerInfoPoint;
    /**
     * 任务服务接口
     */
    private ITaskCommentBiz taskCommentBiz;
    /**
     * 产品接口
     */
    private IProdInfoBiz prodInfoBiz;
    /**
     * 任务服务接口
     */
    private ITaskBiz taskBiz;
    /**
     * 任务数据访问层接口
     */
    private ITaskDAO taskDAO;
  
    @Override
    public ListPage<CustPayment> queryCustPaymentList(CustPaymentQueryParameters qp, List<String> userRoles) 
            throws PossException {
        
        ListPage<CustPayment> customerInfos = custPaymentDAO.queryCustPaymentList(qp, userRoles);
        //初始化划款显示数据
        initCustPayment(customerInfos);
        
        return customerInfos;
    }
    
    @Override
    public ListPage<TransactionRecord> queryTransactionRecordPage(CustPaymentQueryParameters qp) {
    	return custPaymentDAO.queryTransactionRecordPage(qp);
    }
    
    @Override
    public ListPage<PaymentRecord> queryPaymentRecordPage(CustPaymentQueryParameters qp) {
    	return custPaymentDAO.queryPaymentRecordPage(qp);
    }
    
    
    /**
     * 初始化划款显示数据
     * @param customerInfos
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-27	黄云耿	新建
     * </pre>
     * @throws PossException 
     */
    public void initCustPayment(ListPage<CustPayment> customerInfos) throws PossException{
        if(customerInfos != null && customerInfos.getDataList() != null){
            for(CustPayment cp : customerInfos.getDataList()){
                
//                Hibernate.initialize(cp.getPaymentReceipt());
                
//                Date transferDate = cp.getTransferDate();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                if(transferDate != null){
//                    String dateString = formatter.format(transferDate);
//                    cp.setTransferDateStr(dateString);
//                }
                if (StringUtils.isNotBlank(cp.getProdId())) {
	                ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(cp.getProdId());
	                cp.setProName(prodInfo.getProdName());
                }
                
//                Hibernate.initialize(cp.getTask());
                if(null != cp.getTask()) {
                    Hibernate.initialize(cp.getTask().getTaskAssigns());
                }
            }
        }
    }
    
    @Override
    public ListPage<CustPayment> queryArchiveCustPaymentList(CustPaymentQueryParameters qp) 
            throws PossException {
        
        ListPage<CustPayment> customerInfos = custPaymentDAO.queryArchiveCustPaymentList(qp);
        
        //初始化划款显示数据
        initCustPayment(customerInfos);
        return customerInfos;
    }
    
    
    @Override
    public ListPage<CustPayment> queryArchiveCustPaymentListWithoutTask(
    		CustPaymentQueryParameters qp) throws PossException {
    	ListPage<CustPayment> customerInfos = custPaymentDAO.queryCustPaymentListWithoutTask(qp);
        
        //初始化划款显示数据
        initCustPayment(customerInfos);
        return customerInfos;
    }
    
    @Override
    public List<ProdInfo> queryProdInfoList(CustPaymentQueryParameters qp)
    		throws PossException {
    	List<ProdInfo> resultList = new ArrayList<ProdInfo>(0);
    	ListPage<CustPayment> customerInfos = custPaymentDAO.queryCustPaymentListWithoutTask(qp);
    	Map<String, String> idMap = new HashMap<String, String>();
    	if(!CollectionUtils.isEmpty(customerInfos.getDataList())) {
    		for(CustPayment custPayment : customerInfos.getDataList()) {
    			if(idMap.containsKey(custPayment.getProdId()))
    				continue;
    			ProdInfo tmp = new ProdInfo();
    			tmp.setId(custPayment.getProdId());
    			if (StringUtils.isNotBlank(custPayment.getProdId())) {
	    			ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(custPayment.getProdId());
	    			tmp.setProdName(prodInfo.getProdName());
    			}
    			resultList.add(tmp);
    			idMap.put(custPayment.getProdId(), custPayment.getProdId());
    		}
    	}
    	return resultList;
    }
    
    @Override
    public ListPage<CustPayment> queryTrackCustPaymentList(CustPaymentQueryParameters qp) throws PossException {
      
        ListPage<CustPayment> customerInfos = custPaymentDAO.queryTrackCustPaymentList(qp);
        //初始化划款显示数据
        initCustPayment(customerInfos);
        if(customerInfos != null && customerInfos.getDataList() != null){
            for(CustPayment cp : customerInfos.getDataList()){
                Task task = taskDAO.findDealingTasks(cp.getId());
                Hibernate.initialize(task.getTaskAssigns());
                cp.setTask(task);
            }
        }
        
        
        
        return customerInfos;
    }

    @Override
    public void addCustPayment(String paymentJson, SessionAccount user) throws PossException {
        CustPayment custPayment  = (CustPayment)JSON.parseObject(paymentJson, CustPayment.class);

        //起草人
        custPayment.setProposer(user.getAccountID());
        custPayment.setProposerDept(user.getDeptNames());
        custPayment.setApplyTime(new Date());
        custPaymentDAO.save(custPayment);
        
        //累加所选产品预约金额
        String proId = custPayment.getProdId();
        ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(proId);
        if(prodInfo.getTotalAppointmentAmount() == null){
            prodInfo.setTotalAppointmentAmount(0.0);
        }
        prodInfo.setTotalAppointmentAmount(prodInfo.getTotalAppointmentAmount() + custPayment.getAppointmentAmount());
        custPaymentDAO.update(prodInfo);
        
        txStartFlow(custPayment);
    }
    
    private CustPayment txStartFlow(CustPayment custPayment) throws PossException {
        WfmContext context = null;
        try {
            context = WfmConfiguration.getInstance().getWfmContext();
            // 设置流程上下文变量中，并启动流程
            Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
            // 把表单ID
            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID, ContextVariable.DATATYPE_STRING,
                    custPayment.getId());
            contextVariables.put(cv.getName(), cv);
            // 起草人
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                    custPayment.getProposer());
            contextVariables.put(cv.getName(), cv);
            // 取得流程相关信息
            // 取流程Key
            List<FlowInfo> flowInfos = null;
            if(!custPayment.getStandardFlag()){
                flowInfos = flowConfigPoint.getFlowInfos(CustPayment.PAYMENT_FLOW_KEY);
            } else {
                flowInfos = flowConfigPoint.getFlowInfos(CustPayment.FBPAYMENT_FLOW_KEY);
            }
            
            if (flowInfos == null || flowInfos.isEmpty()) {
                throw new PossException("未配置划款流程!");
            }
            FlowInfo flowInfo = flowInfos.get(0);
            // 获取流程类别
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_FLOWCLASS, ContextVariable.DATATYPE_STRING,
                    flowInfo.getFlowClass());
            contextVariables.put(cv.getName(), cv);
            
            // 任务描述
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING, custPayment.getProposerName() + "的划款单");
            contextVariables.put(cv.getName(), cv);

            //区别非标预约
            if(!custPayment.getStandardFlag()){
                // 指定审批人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO, ContextVariable.DATATYPE_STRING,
                        custPayment.getApprover());
                contextVariables.put(cv.getName(), cv);
            } else {
             // 指定审批人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO, ContextVariable.DATATYPE_STRING,
                        "");
                contextVariables.put(cv.getName(), cv);
            }

            // 核查人
//            cv = new ContextVariable(SysConstants.FLOW_VARNAME_EXAMINER, ContextVariable.DATATYPE_STRING, "");
//            contextVariables.put(cv.getName(), cv);
            
            FlowInstance flowInstance = context.newFlowInstance(flowInfo.getFlowKey(), contextVariables);
            flowInstance.signal();
            context.save(flowInstance);
            
            // 设置表单视图的ID
            custPayment.setFlowInstanceId(flowInstance.getId());
            custPayment.setFormStatus(CustPayment.PROCESS_STATUS_DEAL);
        } catch (Exception e) {
            context.rollback();
            log.error("txStartFlow failed: ", e);
            throw new PossException(e.getMessage());
        } finally {
            context.close();
        }
        return custPayment;
    }
    
    @Override
    public CustPayment getCustPaymentById(String id, String flag) throws Exception {
        CustPayment cp = custPaymentDAO.findById(id);
        
        Hibernate.initialize(cp.getPaymentReceipt());
        
//        Date transferDate = cp.getTransferDate();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        if(transferDate != null){
//            String dateString = formatter.format(transferDate);
//            cp.setTransferDateStr(dateString);
//        }
        if (StringUtils.isNotBlank(cp.getProdId())) {
        	ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(cp.getProdId());
	        cp.setProName(prodInfo.getProdName());
	        cp.setProdStatus(prodInfo.getProdStatus());
	        if(prodInfo.getTransferAmount() == null){
	            prodInfo.setTransferAmount(0.0);
	        }
	        cp.setSyed(prodInfo.getSellAmount() - prodInfo.getTransferAmount());
	        cp.setYhked(prodInfo.getTransferAmount());
        }
        //加载审批人
        if (StringUtils.isNotEmpty(cp.getFlowInstanceId())) {
            String approver = taskBiz.getContextVariable(cp.getFlowInstanceId(),SysConstants.FLOW_VARNAME_APPOINTTO);
            String approverName = UsernameCache.getDisplayName(approver);
            
            cp.setApprover(approver);
            cp.setApproverName(approverName);
        }
        
        if(flag != null && "uploadSlip".equals(flag)){
            if(StringUtils.isNotEmpty(cp.getCustId())){
                String customerIdentityNum = customerInfoPoint.getCustomerIdentityNum(cp.getCustId());
                cp.setIdentityNum(customerIdentityNum);
            }
        }

        return cp;
    }
    
    @Override
    public void txApprovePayment(CustPayment custPayment, String tiid, String transition, String taskCommentId, String comment)
            throws PossException {
        WfmContext context = null;
        try {
            CustPayment perCustPayment = custPaymentDAO.findById(custPayment.getId());
            
            perCustPayment.setTransferAmount(custPayment.getTransferAmount());
            perCustPayment.setTransferAmountCapital(custPayment.getTransferAmountCapital());
            if(custPayment.getIdentityNum() != null){
                perCustPayment.setIdentityNum(custPayment.getIdentityNum());
            }
           
            if(custPayment.getPaymentReceipt() != null){
                saveAttachment(perCustPayment, custPayment.getPaymentReceipt());
            }
            
            custPaymentDAO.update(perCustPayment);
            
            context = WfmConfiguration.getInstance().getWfmContext();
            TaskInstance ti = context.getTaskInstance(tiid);
//            // 设置处理人 环境变量
//            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_PROCESSER,
//                    ContextVariable.DATATYPE_STRING, transactor);
//            ti.getFlowInstance().addContextVariable(cv);

            taskCommentBiz.txCommitTaskComment(1, taskCommentId, tiid, comment, transition);
            setTaskInstanceParams(ti, transition, comment);
            // 进入流程下一步
            if (transition == null) {
                ti.end();
            } else {
                ti.end(transition);
            }
            context.save(ti);
            
        } catch (RuntimeException e) {
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
        
    }
    
    private void saveAttachment(CustPayment perCustPayment, Attachment attachment) throws PossException {
        if (attachment != null) {
            if (StringUtils.isEmpty(attachment.getFilePath())) {
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            String dir = SysConstants.PROD_INFO_ATTACH_DIR + sdf.format(new Date());
            File file = new File(attachment.getFilePath());
            Attachment am = new Attachment(attachment.getFilePath(), file.length());
            String newPath = dir + Tools.generateUUID() + am.getFileExt();
            // 保存附件
            try {
                FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(newPath)), new FileInputStream(
                        file));
            } catch (IOException e) {
                throw new PossException("文件拷贝出错!!!");
            }
            am.setFilePath(newPath);
            am.setDisplayName(attachment.getDisplayName().replace("\\", ""));
            am.setUploadDate(new Date());
            custPaymentDAO.saveOrUpdate(am);
            
            perCustPayment.setPaymentReceipt(am);
        } else {
            perCustPayment.setPaymentReceipt(null);
        }
    }
    
    @Override
    public void txApprovePaymentLeader(String id, String tiid, String transition, String taskCommentId, String comment)
            throws PossException {
        WfmContext context = null;
        try {
            
//            CustPayment cp = custPaymentDAO.findById(id);
            //判断是否需要同步CRM客户信息
//            modifyCRMCustInfo(cp);
            
            context = WfmConfiguration.getInstance().getWfmContext();
            TaskInstance ti = context.getTaskInstance(tiid);

            taskCommentBiz.txCommitTaskComment(1, taskCommentId, tiid, comment, transition);
            setTaskInstanceParams(ti, transition, comment);
            // 进入流程下一步
            if (transition == null) {
                ti.end();
            } else {
                ti.end(transition);
            }
            context.save(ti);
            
        } catch (RuntimeException e) {
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
        
    }
    
    @Override
    public void txModifyDealTask(CustPayment custPayment, String taskInstanceId, String taskCommentId, String transition,
            String comment) throws PossException {
        
        CustPayment perCustPayment = custPaymentDAO.findById(custPayment.getId());

        perCustPayment.setTransferAmount(custPayment.getTransferAmount());
        perCustPayment.setTransferAmountCapital(custPayment.getTransferAmountCapital());
        if(custPayment.getIdentityNum() != null){
            perCustPayment.setIdentityNum(custPayment.getIdentityNum());
        }
        
        if(custPayment.getPaymentReceipt() != null){
            saveAttachment(perCustPayment, custPayment.getPaymentReceipt());
        }
       
        custPaymentDAO.update(perCustPayment);
        
//        //扣减所选产品预约金额
//        String proId = perCustPayment.getProdId();
//        ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(proId);
//        prodInfo.setTotalAppointmentAmount(prodInfo.getTotalAppointmentAmount() - perCustPayment.getAppointmentAmount());
//        custPaymentDAO.update(prodInfo);
        
        txDealTask(perCustPayment.getId(), taskInstanceId, taskCommentId, transition, comment, null);
    }
    
    @Override
    public void txDealTask(String id, String taskInstanceId, String taskCommentId, String transition,
            String comment, String flag) throws PossException {
        if (StringUtils.isEmpty(taskInstanceId)) {
            Task task = taskBiz.getDealingTasks(id);
            if (task != null) {
                taskInstanceId = task.getTaskInstanceId();
            }
        }
        if (StringUtils.isEmpty(taskInstanceId)) {
            throw new IllegalArgumentException("参数异常：任务实例ID不能为空");
        }
        
        // 保存审批意见
        taskCommentBiz.txCommitTaskComment(1, taskCommentId, taskInstanceId, comment, transition);
        taskBiz.txDealApproveTask(taskInstanceId, comment, transition);
    }
    
    /**
     * 设置任务实例的一些参数
     * 
     * @param ti 任务实例
     * @param transition 处理步骤
     * @param comment 处理意见
     * 
     *            <pre>
     * 修改日期    修改人    修改原因
     * 2013-7-25    fangwenwei    新建
     * </pre>
     */
    private void setTaskInstanceParams(TaskInstance ti, String transition, String comment) {
        StringBuffer commentBuffer = new StringBuffer();
        // 添加注释
        if (StringUtils.isNotEmpty(comment)) {
            commentBuffer.append(transition).append("，").append(comment);
        } else {
            commentBuffer.append(transition);
        }
        ti.setComment(commentBuffer.toString());
    }

    @Override
    public List<PaymentDisposalProcDTO> csGetDisposalProc(String id) throws PossException {
        List<PaymentDisposalProcDTO> disposalProcs = new ArrayList<PaymentDisposalProcDTO>();
        PaymentDisposalProcDTO disposalProc = null;
        List<Task> tasks = taskDAO.findTasks(id);
        if (tasks != null) {
            for (Task t : tasks) {
                if (t != null) {
                    disposalProc = new PaymentDisposalProcDTO();
                    disposalProc.setType(PaymentDisposalProcDTO.OBJECT_TASK);
                    disposalProc.setCreateTime(t.getCreateTime());
                    disposalProc.setObject(t);
                    disposalProcs.add(disposalProc);
                    String curActor = "";
                    if (t.getTransactor() == null) {
                        Set<TaskAssign> set = t.getTaskAssigns();
                        if (set != null) {
                            for (TaskAssign assign : set) {
                                if (curActor.length() != 0) {
                                    curActor += ",";
                                }
                                if (TaskAssign.TYPE_USER == assign.getType()) {
                                    curActor += assign.getTransactorDisplayName();
                                }
                            }
                        }
                    } else {
                        curActor += t.getTransactorDisplayName();
                    }
                    disposalProc.setCurActor(curActor);
                }
            }
        }

        Collections.sort(disposalProcs);
        return disposalProcs;
    }
    
    @Override
    public void txArchiveCustPayment(String formId) throws PossException {
        CustPayment cstPayment = custPaymentDAO.findById(formId);
        cstPayment.setArchiveDate(new Date());
        cstPayment.setFormStatus(CustPayment.PROCESS_STATUS_ARCH);
        custPaymentDAO.update(cstPayment);
        
        //累加所选产品的划款金额，扣除预约金额
        String proId = cstPayment.getProdId();
        if (StringUtils.isNotBlank(proId)) {
        	ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(proId);
	        if(prodInfo.getTransferAmount() == null){
	            prodInfo.setTransferAmount(0.0);
	        }
	        prodInfo.setTransferAmount(prodInfo.getTransferAmount() + cstPayment.getTransferAmount());  
	        //通过时就不要扣除预约金额
//	        prodInfo.setTotalAppointmentAmount(prodInfo.getTotalAppointmentAmount() - cstPayment.getAppointmentAmount());
	        
	        //到账金额 小于300万的金额，到账小额数+1
	        if (cstPayment.getTransferAmount() < 300) {
	        	int toAccountSmallAmount = 0;
	        	if (prodInfo.getToAccountSmallAmount() != null) {
	        		toAccountSmallAmount = prodInfo.getToAccountSmallAmount() + 1;
	        	}
	        	prodInfo.setToAccountSmallAmount(toAccountSmallAmount);
	        }
	        
	        custPaymentDAO.update(prodInfo);
        }
        
        //如果是输入的(新客户划款)，流程作废后进入我的待完善客户；
        if(cstPayment.getPayType().equals(CustPayment.NEW_CUST_PAYMENT)){
        	//新客户划款
        	if(StringUtils.isEmpty(cstPayment.getCustId())){
        		//说明该客户是手动新增的，需要新增该客户到CRM的待完善客户
             	String custId = customerInfoPoint.txAddCust(cstPayment.getSaleManId(), cstPayment.getCustName(), cstPayment.getCustProperty(), cstPayment.getIdentityNum(), CustInfo.TOPERFECT_STATUS);//"4"表示待完善客户
            	cstPayment.setCustId(custId);
                  //划款表单也同步记录该客户的ID
            	custPaymentDAO.update(cstPayment);
          	} else {
                //说明该客户是选择潜在新增的，需要更新该客户到CRM正式客户
            	customerInfoPoint.txModifyCust(cstPayment.getCustId(), CustInfo.OFFICIAL_STATUS, cstPayment.getIdentityNum());//"5"正式客户状态
          	}
        }
    }
    
    @Override
    public void txApproveRejectEvent(String formId) throws PossException {
//        CustPayment cstPayment = custPaymentDAO.findById(formId);
//        //扣减所选产品预约金额
//        String proId = cstPayment.getProdId();
//        ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(proId);
//        prodInfo.setTotalAppointmentAmount(prodInfo.getTotalAppointmentAmount() - cstPayment.getAppointmentAmount());
//        custPaymentDAO.update(prodInfo);
    }
    
    @Override
    public void txInvalidCustPayment(String formId) throws PossException {
        CustPayment cstPayment = custPaymentDAO.findById(formId);
        cstPayment.setArchiveDate(new Date());
        cstPayment.setFormStatus(CustPayment.PROCESS_STATUS_INVALID);
        custPaymentDAO.update(cstPayment);
        
        //扣减所选产品预约金额
        String proId = cstPayment.getProdId();
        if (StringUtils.isNotBlank(proId)) {
	        ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(proId);
	        prodInfo.setTotalAppointmentAmount(prodInfo.getTotalAppointmentAmount() - cstPayment.getAppointmentAmount());
	        custPaymentDAO.update(prodInfo);
        }
        
        //如果是输入的(新客户划款)，流程作废后进入我的客户草稿；
        if(cstPayment.getPayType().equals(CustPayment.NEW_CUST_PAYMENT)){
            if(StringUtils.isEmpty(cstPayment.getCustId())){
                customerInfoPoint.txAddCust(cstPayment.getSaleManId(), cstPayment.getCustName(), cstPayment.getCustProperty(), cstPayment.getIdentityNum(), CustInfo.UNCOMMIT_STATUS);//"0"表示未提交客户状态
            } 
        }
    }
    
    @Override
    public void modifyPaymentAndEndTransition(CustPayment custPayment, String tiid, String taskCommentId,
            String transition, String comment) throws PossException {
        try {
            CustPayment perCustPayment = custPaymentDAO.findById(custPayment.getId());
            
            //修改所选产品预约金额
            String proId = perCustPayment.getProdId();
            if (StringUtils.isNotBlank(proId)) {
            ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(proId);
	            if(prodInfo.getTotalAppointmentAmount() == null){
	                prodInfo.setTotalAppointmentAmount(0.0);
	            }
	            prodInfo.setTotalAppointmentAmount(prodInfo.getTotalAppointmentAmount() - perCustPayment.getAppointmentAmount() + custPayment.getAppointmentAmount());
	            custPaymentDAO.update(prodInfo);
            }
            modifyCustPayment(perCustPayment, custPayment);
            
            if(custPayment.getPaymentReceipt() != null){
                saveAttachment(perCustPayment, custPayment.getPaymentReceipt());
            }
            
            Assert.notNull(perCustPayment.getFlowInstanceId());
            
            ContextVariable variable = null;
            if(!perCustPayment.getStandardFlag()){
             // 修改审批人
                variable = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
                        ContextVariable.DATATYPE_STRING, perCustPayment.getApprover());
            } else {
             // 修改审批人
                variable = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
                        ContextVariable.DATATYPE_STRING, "");
            }
            
            custPaymentDAO.update(perCustPayment);
            
            txDealTask(perCustPayment.getId(), tiid, taskCommentId, transition, comment, null);
            
            taskBiz.txUpdateContextVariable(perCustPayment.getFlowInstanceId(), variable);
        } catch (Exception e) {
            log.error("modifyPaymentAndEndTransition failed :", e);   
        }
        
    }

    
    private void modifyCustPayment(CustPayment perCustPayment, CustPayment custPayment) {
        perCustPayment.setSaleManId(custPayment.getSaleManId());
        perCustPayment.setTransferDate(custPayment.getTransferDate());
        perCustPayment.setCustId(custPayment.getCustId());
        perCustPayment.setCustName(custPayment.getCustName());
        perCustPayment.setCustProperty(custPayment.getCustProperty());
        perCustPayment.setProdId(custPayment.getProdId());
        perCustPayment.setPayDepositFlag(custPayment.getPayDepositFlag());
        perCustPayment.setAppointmentAmount(custPayment.getAppointmentAmount());
        perCustPayment.setAppointmentAmountCapital(custPayment.getAppointmentAmountCapital());
        perCustPayment.setRemark(custPayment.getRemark());
        perCustPayment.setApprover(custPayment.getApprover());
        perCustPayment.setIdentityNum(custPayment.getIdentityNum());
    }
    
    @Override
    public Attachment saveAttachmentToTempDir(File attachment, String extName, String displayName) throws PossException {
        if (attachment.length() <= 0) {
            throw new PossException("不能上传0kb的文件!!!");
        }
        String fileName = SerialNoCreater.createUUID() + extName;
        // 这个是临时目录
        String fileFullName = FileDispatcher.getTempDir() + fileName;
        try {
            FileUtil.saveFile(fileFullName, new FileInputStream(attachment));
        } catch (IOException e) {
            log.error("saveFile failed: ", e);
            throw new PossException("文件拷贝出错!!!");
        }
        Attachment am = null;
        if (StringUtils.isNotEmpty(fileFullName)) {
            File file = new File(fileFullName);
            am = new Attachment(fileFullName, file.length());
            am.setDisplayName(displayName.replace("\\", ""));
            am.setUploadDate(new Date());
            am.setFilePath(fileFullName);
        }
        return am;
    }
    
    @Override
    public ListPage<CustPayment> queryAllCustPaymentList(CustPaymentQueryParameters iqp) throws PossException {
    	ListPage<CustPayment> customerInfos = custPaymentDAO.queryAllCustPaymentList(iqp);
    	initCustPayment(customerInfos);
    	return customerInfos;
    }
    

    public void setCustPaymentDAO(ICustPaymentDAO custPaymentDAO) {
        this.custPaymentDAO = custPaymentDAO;
    }

    public void setFlowConfigPoint(IFlowConfigPoint flowConfigPoint) {
        this.flowConfigPoint = flowConfigPoint;
    }

    public void setTaskCommentBiz(ITaskCommentBiz taskCommentBiz) {
        this.taskCommentBiz = taskCommentBiz;
    }

    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setTaskBiz(ITaskBiz taskBiz) {
        this.taskBiz = taskBiz;
    }

    public void setProdInfoBiz(IProdInfoBiz prodInfoBiz) {
        this.prodInfoBiz = prodInfoBiz;
    }

    public void setCustomerInfoPoint(ICustomerInfoPoint customerInfoPoint) {
        this.customerInfoPoint = customerInfoPoint;
    }

}
