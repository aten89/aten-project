/**
 * 
 */
package org.eapp.oa.reimburse.blo.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.reimburse.blo.IReimbursementBiz;
import org.eapp.oa.reimburse.dao.IReimFlowConfDAO;
import org.eapp.oa.reimburse.dao.IReimbursementDAO;
import org.eapp.oa.reimburse.dto.ReimbursementQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.reimburse.hbean.ReimFlowConf;
import org.eapp.oa.reimburse.hbean.ReimItem;
import org.eapp.oa.reimburse.hbean.Reimbursement;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;


/**
 * 报销单业务逻辑层
 * 
 * @author zsy
 * @version Nov 25, 2008
 */
public class ReimbursementBiz implements IReimbursementBiz {
    
    /**
     * 日志
     */
//	private static Log log = LogFactory.getLog(ReimbursementBiz.class);
	/**
	 * 报销单数据访问层接口
	 */
	private IReimbursementDAO reimbursementDAO;
	private IReimFlowConfDAO  reimFlowConfDAO;
	private ITaskDAO taskDAO;
	/**
	 * 通讯录模块中的我的资料子模块的业务逻辑处理接口
	 */
//	private IAddressListBiz addressListBiz;
	
	/**
	 * noCreater
	 */
	private SerialNoCreater noCreater;
	
	/**
     * 设置 reimbursementDAO
     * @param reimbursementDAO the reimbursementDAO to set
     */
	public void setReimbursementDAO(IReimbursementDAO reimbursementDAO) {
		this.reimbursementDAO = reimbursementDAO;
	}
	public void setReimFlowConfDAO(IReimFlowConfDAO reimFlowConfDAO) {
		this.reimFlowConfDAO = reimFlowConfDAO;
	}
	
	public void setTaskDAO(ITaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	private String createReimbursementID() {
		if (noCreater == null) {
			noCreater = new SerialNoCreater(reimbursementDAO.getMaxReimbursementID());
		}
		return noCreater.createNo();
	}
	
	@Override
    public List<Reimbursement> getReimbursements(String userAccountId, int formStatus) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		return reimbursementDAO.findReimbursements(userAccountId, formStatus);
	}
	
    @Override
	public Reimbursement getReimbursementById(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Reimbursement draf = reimbursementDAO.findById(id);
		if (draf != null) {
			draf.getReimItems().size();
		}
		Set<ReimItem> reimItemDrafts =  draf.getReimItems();
		if(reimItemDrafts!= null && reimItemDrafts.size() >0){
			for(ReimItem r:reimItemDrafts){
				r.getOutlayLists().size();
			}
		}
		return draf;
	}
	
	@Override
	public Reimbursement txAddOrModifyReimbursement(Reimbursement draft, String user) {
		if (draft == null) {
			throw new IllegalArgumentException();
		}
		if (draft.getId() == null) {
			draft.setId(createReimbursementID());
			draft.setApplyDate(new Timestamp(System.currentTimeMillis()));
			draft.setApplicant(user);
//			draft.setApplyDept(user.getGroupNames());
			draft.setFormStatus(Reimbursement.STATUS_DRAFT);
			
			setOutlayListRef(draft);
			
			reimbursementDAO.save(draft);
		} else {
			Reimbursement old = reimbursementDAO.findById(draft.getId());
			//不变的字段
			draft.setApplyDate(old.getApplyDate());
			draft.setApplicant(old.getApplicant());
//			draft.setApplyDept(old.getApplyDept());
			draft.setFormStatus(old.getFormStatus());
			draft.setPassed(old.getPassed());
			draft.setArchiveDate(old.getArchiveDate());
			draft.setFlowInstanceId(old.getFlowInstanceId());
			
			setOutlayListRef(draft);
			
			reimbursementDAO.merge(draft);
		}
		return draft;
	}
	
	private void setOutlayListRef(Reimbursement draft){
//		System.out.println(draft.getId()+ "=" + draft.getFinance()+ "=" + draft.getPayee()+ "=" + draft.getCausa()+ "=" + draft.getLoanSum()+ "=" + draft.getReimbursementSum()+ "=" + draft.getAppointTo());
		if (draft.getReimItems() != null) {
			for (ReimItem id : draft.getReimItems()) {
				id.setReimbursement(draft);
//				System.out.println("\t" +id.getId()+ "=" + id.getRegionName() + "=" + id.getCustomName() + "=" + id.getCoterielList()+ "=" + id.getTravelPlace()+ "=" + id.getTravelBeginDate()+ "=" + id.getTravelEndDate());
				if (id.getOutlayLists() != null) {
					for (OutlayList od : id.getOutlayLists()) {
						od.setReimItem(id);
						od.setReimbursement(draft);
//						System.out.println("\t\t" +od.getId()+ "=" + od.getOutlayCategory() + "=" + od.getOutlayName() + "=" + od.getDocumetNum()+ "=" + od.getOutlaySum()+ "=" + od.getDescription());
					}
				}
			}
		}
	}
	
	@Override
	public Reimbursement txDeleteReimbursement(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Reimbursement rei = reimbursementDAO.findById(id);
		reimbursementDAO.delete(rei);
		return rei;
	}

	
	/**
	 * 启动报销流程。报销的草稿可以同时维护多个预算的报销，提交时，会拆分各个预算走各自的审批流程
	 * 启动时，需要费用类别报销的额度是否超出预算中对应费用类别的余额，超出了就不给予报销。
	 */
    @Override
	public Reimbursement txStartFlow(Reimbursement draft, String user) throws OaException {
    	Reimbursement rei = txAddOrModifyReimbursement(draft, user);
    	rei.setFormStatus(Reimbursement.STATUS_APPROVAL);
    	
    	
    	String flowKey = null;
    	ReimFlowConf rfc = reimFlowConfDAO.findByGroupName(draft.getApplyDept());
    	if (rfc != null) {
    		flowKey = rfc.getFlowKey();
    	}
    	if (flowKey == null) {
    		throw new OaException("未配置部门的报销流程:" + draft.getApplyDept());
    	}
    	
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			// 设置流程上下文变量中，并启动流程
            Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
         // 把表单ID
			ContextVariable cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_FORMID,
					ContextVariable.DATATYPE_STRING, rei.getId());
			contextVariables.put(cv.getName(), cv);
			// 发起人
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_USERACCOUNTID,
					ContextVariable.DATATYPE_STRING, user);
			contextVariables.put(cv.getName(), cv);
			// 部门
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_GROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getApplyDept());
			contextVariables.put(cv.getName(), cv);
			// 财务隶属
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_AREA,
					ContextVariable.DATATYPE_STRING, rei.getFinance());
			contextVariables.put(cv.getName(), cv);

			// 指定审批人
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
					ContextVariable.DATATYPE_STRING, rei.getAppointTo());
			contextVariables.put(cv.getName(), cv);
			// 受款人
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_PAYEE,
					ContextVariable.DATATYPE_STRING, rei.getPayee());
			contextVariables.put(cv.getName(), cv);

			// 任务描述
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_TASKDESCRIPTION,
					ContextVariable.DATATYPE_STRING, rei.getApplicantName()
							+ "的报销单");
			
			contextVariables.put(cv.getName(), cv);

			FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
			flowInstance.signal();
			context.save(flowInstance);
			// 设置表单视图的ID
			rei.setFlowInstanceId(flowInstance.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			context.rollback();
			throw new OaException(e.getMessage());
		} finally {
			context.close();
		}
		// 删除草稿
		reimbursementDAO.merge(rei);
		return rei;
	}
    
    @Override
    public void txModifyReim(String reimId, boolean arch) throws OaException {
        if (reimId == null) {
            throw new IllegalArgumentException("非法参数:infoFormId");
        }
        Reimbursement reim = reimbursementDAO.findById(reimId);
        // 设置归档时间
        reim.setArchiveDate(new Date());
        if (arch) {
        	// 设置Passed
	        reim.setPassed(true);
	        // 设置状态
	        reim.setFormStatus(Reimbursement.STATUS_ARCH);
        } else {
	        // 设置Passed
	        reim.setPassed(false);
	        // 设置状态
	        reim.setFormStatus(Reimbursement.STATUS_CANCELLATION);
        }
        reimbursementDAO.saveOrUpdate(reim);
    }
    
    @Override
	public ListPage<Reimbursement> getDealingReimbursement(ReimbursementQueryParameters rqp, String userAccountId) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		ListPage<Reimbursement> listPage = reimbursementDAO.queryDealingReimbursement(rqp, userAccountId);
		return listPage;
	}
    
    @Override
	public ListPage<Reimbursement> getTrackReimbursement(
			ReimbursementQueryParameters rqp, String userAccountId) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		if (rqp != null) {
			rqp.addOrder("applyDate", false);
		}
		ListPage<Reimbursement> page = reimbursementDAO.queryReimbursement(rqp,
				userAccountId, false);
		if (page != null && page.getDataList() != null) {
			List<Reimbursement> list = page.getDataList();
			// 取得最后结束的一个任务
			for (Reimbursement r : list) {
				if (r != null) {
					List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceId());
					StringBuffer sb = new StringBuffer();
					if (tasks != null && !tasks.isEmpty()){
						for (Task t :tasks){
						    if (t.getTaskState().equals(TaskInstance.PROCESS_STATE_CREATE)) {
						        sb.append("<b>"+t.getTransactorDisplayName()+"</b>");
						    } else {
						        sb.append(t.getTransactorDisplayName());
						    }
						}
						Task task = tasks.get(0);
						task.setTransactor(sb.toString());
						r.setTask(task);
					}
				}
			}
		}
		return page;
	}

    @Override
	public ListPage<Reimbursement> getArchReimbursement(
			ReimbursementQueryParameters rqp, String userAccountId) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		
		ListPage<Reimbursement> listPage = reimbursementDAO.queryReimbursement(rqp, userAccountId, true);
		return listPage;
	}

	@Override
	public List<Task> getEndedTasks(String formId) {
		Reimbursement rei = reimbursementDAO.findById(formId);
		if (rei == null) {
			throw new IllegalArgumentException("非法参数:formId");
		}
		return taskDAO.findEndedTasks(rei.getFlowInstanceId());
	}
	
	@Override
	public void txDealApproveTask(String taskInstanceId, String comment,
			String transitionName, Reimbursement reim) throws OaException {
		Reimbursement rei = txAddOrModifyReimbursement(reim, null);
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}

			// 财务隶属
			ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_AREA,
					ContextVariable.DATATYPE_STRING, rei.getFinance());
			ti.getFlowInstance().addContextVariable(var);

			// 指定审批人
			var = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
					ContextVariable.DATATYPE_STRING, rei.getAppointTo());
			ti.getFlowInstance().addContextVariable(var);

			// 受款人
			var = new ContextVariable(SysConstants.FLOW_VARNAME_PAYEE,
					ContextVariable.DATATYPE_STRING, rei.getPayee());
			ti.getFlowInstance().addContextVariable(var);

			ti.setComment(comment);
			if (transitionName != null) {
				ti.end(transitionName);
			} else {
				ti.end();
			}
			context.save(ti);
		} catch (Exception e) {
			e.printStackTrace();
			context.rollback();
			throw new OaException(e.getMessage());
		} finally {
			context.close();
		}
	}
	@Override
	public ListPage<Reimbursement> queryReimbursement(
			ReimbursementQueryParameters rqp) {
		if (rqp == null) {
			throw new IllegalArgumentException(
					"非法参数:ReimbursementQueryParameters参数为null");
		}
		ListPage<Reimbursement> reimbursPage = reimbursementDAO.queryReimbursement(rqp);
		return reimbursPage;
	}
}

