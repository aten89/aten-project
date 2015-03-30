/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.util.UsernameCache;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.rmi.hessian.CustInfo;
import org.eapp.crm.rmi.hessian.ICustomerInfoPoint;
import org.eapp.oa.rmi.hessian.FlowInfo;
import org.eapp.oa.rmi.hessian.IFlowConfigPoint;
import org.eapp.poss.blo.ICustRefundBiz;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.blo.ITaskCommentBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.ICustPaymentDAO;
import org.eapp.poss.dao.ICustRefundDAO;
import org.eapp.poss.dao.ITaskDAO;
import org.eapp.poss.dao.param.CustRefundQueryParameters;
import org.eapp.poss.dto.CustRefundDisposalProcDTO;
import org.eapp.poss.dto.PaymentDisposalProcDTO;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.hbean.CustRefund;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
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
public class CustRefundBiz implements ICustRefundBiz {
    
    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(CustRefundBiz.class);

    /**
     * 客服管理DAO接口
     */
    private ICustRefundDAO custRefundDAO;
    
    private ICustPaymentDAO custPaymentDAO;
    
    /**
     * 流程配置hessian接口
     */
    private IFlowConfigPoint flowConfigPoint;
    /**
     * 任务服务接口
     */
    private ITaskCommentBiz taskCommentBiz;
    /**
     * 任务服务接口
     */
    private ITaskBiz taskBiz;
    /**
     * 任务数据访问层接口
     */
    private ITaskDAO taskDAO;
    private ICustomerInfoPoint customerInfoPoint;
    
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  
    private void initCustRefund(CustRefund custRefund) throws PossException {
    	
    	//设置客户NAME
    	CustPayment custPayment = custPaymentDAO.findById(custRefund.getPaymentId());
		if(null != custPayment) {
			custRefund.setCustName(custPayment.getCustName());
		}
		
		//设置申请时间STR
		if(null != custRefund.getApplyTime()) {
			custRefund.setApplyTimeStr(df.format(custRefund.getApplyTime()));
		}
		
		Hibernate.initialize(custRefund.getTask());
		if(null != custRefund.getTask()) {
			Hibernate.initialize(custRefund.getTask().getTaskAssigns());
		}
		
    	Hibernate.initialize(custRefund.getCustRefundAttach());
		Hibernate.initialize(custRefund.getProdInfo());
		if(null != custRefund.getProdInfo()) {
			Hibernate.initialize(custRefund.getProdInfo().getCustPayments());
			Hibernate.initialize(custRefund.getProdInfo().getSupplier());
			Hibernate.initialize(custRefund.getProdInfo().getProdFaqs());
			Hibernate.initialize(custRefund.getProdInfo().getExpectYearYields());
			Hibernate.initialize(custRefund.getProdInfo().getProdPayDates());
			Hibernate.initialize(custRefund.getProdInfo().getMessages());
			Hibernate.initialize(custRefund.getProdInfo().getCustRefunds());
			Hibernate.initialize(custRefund.getProdInfo().getCustPayments());
		}
    }
    
    @Override
    public ListPage<CustRefund> queryCustRefundDraftList(
    		CustRefundQueryParameters qp) throws PossException {
    	ListPage<CustRefund> custRefunds = custRefundDAO.queryCustRefundDraftList(qp);
    	if(!CollectionUtils.isEmpty(custRefunds.getDataList())) {
    		for(CustRefund custRefund : custRefunds.getDataList()) {
    			initCustRefund(custRefund);
    			custRefund.setProdName(custRefund.getProdInfo().getProdName());
    			custRefund.setProdInfo(null);
    		}
    	}
    	return custRefunds;
    }
    
    @Override
    public ListPage<CustRefund> queryCustRefundList(CustRefundQueryParameters qp, List<String> userRoles) 
            throws PossException {
        
        ListPage<CustRefund> custRefunds = custRefundDAO.queryCustRefundList(qp, userRoles);
        if(!CollectionUtils.isEmpty(custRefunds.getDataList())) {
    		for(CustRefund custRefund : custRefunds.getDataList()) {
    			initCustRefund(custRefund);
    			custRefund.setProdName(custRefund.getProdInfo().getProdName());
    			custRefund.setProdInfo(null);
    		}
    	}
        return custRefunds;
    }
    
    @Override
    public ListPage<CustRefund> queryArchiveCustRefundList(CustRefundQueryParameters qp) 
            throws PossException {
        
        ListPage<CustRefund> custRefunds = custRefundDAO.queryArchiveCustRefundList(qp);
        if(!CollectionUtils.isEmpty(custRefunds.getDataList())) {
    		for(CustRefund custRefund : custRefunds.getDataList()) {
    			initCustRefund(custRefund);
    			custRefund.setProdName(custRefund.getProdInfo().getProdName());
    			custRefund.setProdInfo(null);
    		}
    	}
        return custRefunds;
    }
    
    @Override
    public ListPage<CustRefund> queryTrackCustRefundList(CustRefundQueryParameters qp) throws PossException {
        ListPage<CustRefund> custRefunds = custRefundDAO.queryTrackCustRefundList(qp);
        if(!CollectionUtils.isEmpty(custRefunds.getDataList())) {
    		for(CustRefund custRefund : custRefunds.getDataList()) {
    			initCustRefund(custRefund);
    			Hibernate.initialize(custRefund.getTask().getTaskAssigns());
    			if(null != custRefund.getProdInfo()) {
    				custRefund.setProdName(custRefund.getProdInfo().getProdName());
    			}
    			custRefund.setProdInfo(null);
    		}
    	}
        return custRefunds;
    }

    @Override
    public CustRefund addCustRefund(String custRefundJson, SessionAccount user, Boolean flowFlag) throws PossException {
        CustRefund custRefund  = (CustRefund)JSON.parseObject(custRefundJson, CustRefund.class);
        CustPayment custPayment = custPaymentDAO.findById(custRefund.getPaymentId());
        if (custPayment == null) {
        	throw new PossException("划款单不存在");
        }
        if (custPayment.getTotalRefundAmount() + custRefund.getRefundAmount() > custPayment.getTransferAmount()) {
        	throw new PossException("累计退款金额大于划款金额");
        }
        //起草人
        custRefund.setProposer(user.getAccountID());
        custRefund.setProposerDept(user.getDeptNames());
//        custRefund.setApplyTime(new Date());
        custRefund.setFormStatus(CustRefund.PROCESS_STATUS_DRAFT);
        custRefundDAO.save(custRefund);
        if(flowFlag) {
        	txStartFlow(custRefund);
        }
        return custRefund;
    }
    
    @Override
    public CustRefund modifyCustRefund(String custRefundJson, SessionAccount user,
    		Boolean flowFlag) throws PossException {
    	CustRefund tranObject  = (CustRefund)JSON.parseObject(custRefundJson, CustRefund.class);
    	if(StringUtils.isEmpty(tranObject.getId())) {
    		throw new PossException("modifyCustRefund failed : 退款ID不能为空");
    	}
    	CustPayment custPayment = custPaymentDAO.findById(tranObject.getPaymentId());
        if (custPayment == null) {
        	throw new PossException("划款单不存在");
        }
        if (custPayment.getTotalRefundAmount() + tranObject.getRefundAmount() > custPayment.getTransferAmount()) {
        	throw new PossException("累计退款金额大于划款金额");
        }
    	
    	CustRefund persObject = this.getCustRefundById(tranObject.getId());
    	BeanUtils.copyProperties(tranObject, persObject, 
    			new String[]{
    				"proposer", 
    				"proposerDept", 
//    				"applyTime", 
    				"archiveDate", 
    				"passed", 
    				"formStatus", 
    				"custRefundAttach"});
    	tranObject.setId(null);
    	custRefundDAO.update(persObject);
    	if(flowFlag) {
        	txStartFlow(persObject);
        }
    	return persObject;
    }
    
    @Override
    public void txBatchUploadCustRefundAttachment(String id,
    		String[] deletedFiles, Collection<Attachment> custRefundAttachments)
    		throws PossException {
    	CustRefund custRefund = this.getCustRefundById(id);
    	//先删除前台传过来要删除的附件
    	txDeleteCustRefundFilesIfNecessary(deletedFiles, custRefund);
    	//保存许可证附件
    	txSaveCustRefundAttachment(custRefundAttachments, custRefund);
    }
    
    /**
     * 同步删除前台删除的附件
     * @param deletedFiles
     * @param refuncNotice
     */
    private void txDeleteCustRefundFilesIfNecessary(String[] deletedFiles, CustRefund custRefund) {
        // 取得前台的删除的附件列表
        if (deletedFiles != null) {
            List<String> delFileList = Arrays.asList(deletedFiles);
            // 取得计划该客户已经上传的许可证附件
            Set<Attachment> existAttachments = custRefund.getCustRefundAttach();
            if (!CollectionUtils.isEmpty(existAttachments)) {
            	for(Attachment attachement : existAttachments) {
            		if (delFileList.contains(attachement.getDisplayName() + attachement.getFileExt())) {
            			custRefund.getCustRefundAttach().remove(attachement);
            		}
            	}
            	custRefundDAO.update(custRefund);
            }
        }
    }
    
    /**
     * 保存附件
     * @param refuncNoticeAttachments
     * @param refuncNotice
     */
    private void txSaveCustRefundAttachment(Collection<Attachment> custRefundAttachments, CustRefund custRefund) {
        if (!CollectionUtils.isEmpty(custRefundAttachments)) {
            // 保存许可证附件
        	for(Attachment attachment : custRefundAttachments) {
        		custRefundDAO.save(attachment);
        	}
        	
        	// 附件与退款须知绑定
        	custRefund.getCustRefundAttach().addAll(custRefundAttachments);
        	custRefundDAO.saveOrUpdate(custRefund);
        }

    }
    
    public static void main(String[] args) {
    	CustRefund web = new CustRefund();
    	CustRefund per = new CustRefund();
    	web.setId("1");
    	web.setApplyTime(new Date());
    	web.setFlowTitle("webwebweb");
    	Set<Attachment> at = new HashSet<Attachment>(0);
    	at.add(new Attachment());
    	at.add(new Attachment());
    	at.add(new Attachment());
    	web.getCustRefundAttach().addAll(at);
    	
    	per.setId("1");
    	per.setFlowTitle("perperper");
    	per.setCustId("ppp");
    	BeanUtils.copyProperties(web, per, new String[]{"custRefundAttach", "custId"});
    	System.out.println(1);
	}
    
    
    
    private CustRefund txStartFlow(CustRefund custRefund) throws PossException {
        WfmContext context = null;
        try {
            context = WfmConfiguration.getInstance().getWfmContext();
            // 设置流程上下文变量中，并启动流程
            Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
            // 把表单ID
            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID, ContextVariable.DATATYPE_STRING,
            		custRefund.getId());
            contextVariables.put(cv.getName(), cv);
            // 起草人
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
            		custRefund.getProposer());
            contextVariables.put(cv.getName(), cv);
            // 取得流程相关信息
            // 取流程Key
            List<FlowInfo> flowInfos = flowConfigPoint.getFlowInfos(CustRefund.REFUND_FLOW_KEY);
            if (flowInfos == null || flowInfos.isEmpty()) {
                throw new PossException("未配置划款流程!");
            }
            FlowInfo flowInfo = flowInfos.get(0);
            // 获取流程类别
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_FLOWCLASS, ContextVariable.DATATYPE_STRING,
                    flowInfo.getFlowClass());
            contextVariables.put(cv.getName(), cv);
            
            // 任务描述
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING, custRefund.getProposerName() + "的退款单");
            contextVariables.put(cv.getName(), cv);

            // 指定审批人
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO, ContextVariable.DATATYPE_STRING,
            		custRefund.getApprover());
            contextVariables.put(cv.getName(), cv);

            
            // 核查人
//            cv = new ContextVariable(SysConstants.FLOW_VARNAME_EXAMINER, ContextVariable.DATATYPE_STRING, "");
//            contextVariables.put(cv.getName(), cv);
            
            FlowInstance flowInstance = context.newFlowInstance(flowInfo.getFlowKey(), contextVariables);
            flowInstance.signal();
            context.save(flowInstance);
            
            // 设置表单视图的ID
            custRefund.setFlowInstanceId(flowInstance.getId());
            custRefund.setFormStatus(CustRefund.PROCESS_STATUS_DEAL);
            custRefundDAO.saveOrUpdate(custRefund);
        } catch (Exception e) {
            context.rollback();
            log.error("txStartFlow failed: ", e);
            throw new PossException(e.getMessage());
        } finally {
            context.close();
        }
        return custRefund;
    }
    
    @Override
    public CustRefund getCustRefundById(String id) throws PossException {
    	CustRefund result = custRefundDAO.findById(id);
    	initCustRefund(result);
        return result;
    }
    
    @Override
    public void txRejectCustRefund(String id, String tiid, String transition, String taskCommentId, String comment)
            throws PossException {
        WfmContext context = null;
        try {
            
//            CustRefund custRefund = custRefundDAO.findById(id);

            context = WfmConfiguration.getInstance().getWfmContext();
            TaskInstance ti = context.getTaskInstance(tiid);
            // 设置处理人 环境变量
//            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
//                    ContextVariable.DATATYPE_STRING, custRefund.getProposer());

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
            log.error("退款确认处理失败！");
            throw e;
        } finally {
            context.close();
        }
        
    }
    
    @Override
    public Map<String, String> queryFlowVarialbleMap(String tiid) throws PossException {
    	WfmContext context = null;
    	Map<String, String> resultMap = new HashMap<String, String>(0);
    	try {
    		context = WfmConfiguration.getInstance().getWfmContext();
            TaskInstance ti = context.getTaskInstance(tiid);
            Map<String, ContextVariable> contextVarMap = ti.getFlowInstance().getContextVariables();
            Collection<ContextVariable> contextVarCol = contextVarMap.values();
            Iterator<ContextVariable> it = contextVarCol.iterator();
            while(it.hasNext()) {
            	ContextVariable var = it.next();
            	resultMap.put(var.getName(), var.getValue());
            }
            
            //初始化审批人名称
            if(resultMap.containsKey("appointTo")) {
            	String appointTo = resultMap.get("appointTo");
            	String appointToName = UsernameCache.getDisplayName(appointTo);
            	resultMap.put("appointToName", appointToName);
            }
    	} catch (RuntimeException e) {
            context.rollback();
            log.error("退款确认处理失败！");
            throw e;
        } finally {
            context.close();
        }
    	return resultMap;
    }
    
    @Override
    public void txApproveCustRefund(String id, String tiid, String transition,
    		String taskCommentId, String comment) throws PossException {
    	WfmContext context = null;
        try {
            
//            CustRefund custRefund = custRefundDAO.findById(id);
//            Hibernate.initialize(custRefund.getProdInfo());
//          //对应的产品划款=产品划款-退款
//            ProdInfo prodInfo = custRefund.getProdInfo();
//            prodInfo.setTransferAmount(prodInfo.getTransferAmount() - custRefund.getRefundAmount());
//            if(prodInfo.getTransferAmount() < 0) {
//            	throw new PossException("退款不能超过该产品的划款金额");
//            }
//            custRefundDAO.update(custRefund);
            
            context = WfmConfiguration.getInstance().getWfmContext();
            TaskInstance ti = context.getTaskInstance(tiid);
            // 设置处理人 环境变量
//            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
//                    ContextVariable.DATATYPE_STRING, custRefund.getProposer());

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
            log.error("退款确认处理失败！");
            throw e;
        } finally {
            context.close();
        }
    }
    
    @Override
    public void txDealTask(String id, String taskInstanceId, String taskCommentId, String transition,
            String comment) throws PossException {
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
    
    @Override
    public void modifyCustRefundAndEndTransition(CustRefund custRefund,
    		String tiid, String taskCommentId, String transition, String comment)
    		throws PossException {
    	try {
    		CustRefund perCustRefund = custRefundDAO.findById(custRefund.getId());
    		perCustRefund.setApprover(custRefund.getApprover());
            
            Assert.notNull(perCustRefund.getFlowInstanceId());
            // 修改审批人
            ContextVariable variable = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
                    ContextVariable.DATATYPE_STRING, perCustRefund.getApprover());
            
            txDealTask(perCustRefund.getId(), tiid, taskCommentId, transition, comment);
            
            taskBiz.txUpdateContextVariable(perCustRefund.getFlowInstanceId(), variable);
        } catch (Exception e) {
            log.error("modifyPaymentAndEndTransition failed :", e);   
        }
    }
    
    @Override
    public CustRefund txAgainCommitCustRefund(CustRefund custRefund, String tiid,
    		String taskCommentId, String transition, String comment)
    		throws PossException {
    	try {
    		
    		if(StringUtils.isEmpty(custRefund.getId())) {
        		throw new PossException("modifyCustRefund failed : 退款ID不能为空");
        	}
        	
        	CustRefund persObject = this.getCustRefundById(custRefund.getId());
        	BeanUtils.copyProperties(custRefund, persObject, 
        			new String[]{
        				"proposer", 
        				"proposerDept", 
//        				"applyTime", 
        				"flowInstanceId", 
        				"archiveDate", 
        				"passed", 
        				"formStatus", 
        				"custRefundAttach"});
        	custRefund.setId(null);
        	custRefundDAO.update(persObject);
    		
            Assert.notNull(persObject.getFlowInstanceId());
            // 修改审批人
            ContextVariable variable = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
                    ContextVariable.DATATYPE_STRING, custRefund.getApprover());
            
            txDealTask(persObject.getId(), tiid, taskCommentId, transition, comment);
            
            taskBiz.txUpdateContextVariable(persObject.getFlowInstanceId(), variable);
            return persObject;
        } catch (Exception e) {
            log.error("modifyPaymentAndEndTransition failed :", e);   
            return custRefund;
        }
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
    public List<CustRefundDisposalProcDTO> csGetDisposalProc(String id) throws PossException {
        List<CustRefundDisposalProcDTO> disposalProcs = new ArrayList<CustRefundDisposalProcDTO>();
        CustRefundDisposalProcDTO disposalProc = null;
        List<Task> tasks = taskDAO.findTasks(id);
        if (tasks != null) {
            for (Task t : tasks) {
                if (t != null) {
                    disposalProc = new CustRefundDisposalProcDTO();
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
    public void txArchiveCustRefund(String formId) throws PossException {
        CustRefund custRefund = custRefundDAO.findById(formId);
        custRefund.setArchiveDate(new Date());
        custRefund.setFormStatus(CustRefund.PROCESS_STATUS_ARCH);
        
        Hibernate.initialize(custRefund.getProdInfo());
        //对应的产品划款=产品划款-退款
        ProdInfo prodInfo = custRefund.getProdInfo();
        prodInfo.setTransferAmount(prodInfo.getTransferAmount() - custRefund.getRefundAmount());
        if(prodInfo.getTransferAmount() < 0) {
        	throw new PossException("退款不能超过该产品的划款金额");
        }
        custRefundDAO.update(custRefund);
        //
        CustPayment custPayment = custPaymentDAO.findById(custRefund.getPaymentId());
        if (custPayment == null) {
        	throw new PossException("划款单不存在");
        }
        double totalRefundAmount = custPayment.getTotalRefundAmount() + custRefund.getRefundAmount();//本次退款后累计金额
        if (totalRefundAmount > custPayment.getTransferAmount()) {
        	throw new PossException("累计退款金额大于划款金额");
        }
        //更新划款单的累计退款金额
        custPayment.setTotalRefundAmount(totalRefundAmount);
        custPaymentDAO.update(custPayment);
        
        //如果已全部退款修改客户状态
        if (totalRefundAmount == custPayment.getTransferAmount()) {
        	//如果其它划款单也都全部退款，转为潜在客户状态
        	if (!custPaymentDAO.checkCustPayment(custPayment.getCustId(), custPayment.getId())) {
            	customerInfoPoint.txModifyCust(custPayment.getCustId(), CustInfo.POTENTIAL_STATUS, custPayment.getIdentityNum());//"5"正式客户状态
        	}
        }
    }
    
    @Override
    public void txInvalidCustRefund(String formId) throws PossException {
        CustRefund cstPayment = custRefundDAO.findById(formId);
        cstPayment.setArchiveDate(new Date());
        cstPayment.setFormStatus(CustRefund.PROCESS_STATUS_INVALID);
        custRefundDAO.update(cstPayment);
    }
    
    
    public void setCustRefundDAO(ICustRefundDAO custRefundDAO) {
        this.custRefundDAO = custRefundDAO;
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

//	public void setProdInfoBiz(IProdInfoBiz prodInfoBiz) {
//		this.prodInfoBiz = prodInfoBiz;
//	}

	public void setCustPaymentDAO(ICustPaymentDAO custPaymentDAO) {
		this.custPaymentDAO = custPaymentDAO;
	}
	
	public void setCustomerInfoPoint(ICustomerInfoPoint customerInfoPoint) {
        this.customerInfoPoint = customerInfoPoint;
    }
}
