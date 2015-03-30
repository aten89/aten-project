/**
 * 
 */
package org.eapp.oa.hr.blo.impl;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.GroupService;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.ITransferApplyBiz;
import org.eapp.oa.hr.dao.IHRFlowConfDAO;
import org.eapp.oa.hr.dao.IStaffFlowApplyDAO;
import org.eapp.oa.hr.dao.ITransferApplyDAO;
import org.eapp.oa.hr.dto.TransferQueryParameters;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.hr.hbean.TransferApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;

/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class TransferApplyBiz implements ITransferApplyBiz {
	/**
     * log
     */
    public static final Log log = LogFactory.getLog(TransferApplyBiz.class);

    private ITransferApplyDAO transferApplyDAO;
    private IHRFlowConfDAO  hrFlowConfDAO;
    private ITaskDAO taskDAO;
    private IStaffFlowApplyDAO staffFlowApplyDAO;
    
    private SerialNoCreater noCreater;
    /**
     * 组织机构
     */
    private GroupService gs = new GroupService();
    
    public void setHrFlowConfDAO(IHRFlowConfDAO hrFlowConfDAO) {
		this.hrFlowConfDAO = hrFlowConfDAO;
	}
    
    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setStaffFlowApplyDAO(IStaffFlowApplyDAO staffFlowApplyDAO) {
		this.staffFlowApplyDAO = staffFlowApplyDAO;
	}

	public void setTransferApplyDAO(ITransferApplyDAO transferApplyDAO) {
		this.transferApplyDAO = transferApplyDAO;
	}

	private String createHolidayApplyID() {
        if (noCreater == null) {
            noCreater = new SerialNoCreater(transferApplyDAO.getMaxID());
        }
        return noCreater.createNo();
    }

	@Override
	public TransferApply getTransferApplyById(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		return transferApplyDAO.findById(id);
	}

	@Override
	public List<TransferApply> getTransferApplys(String userAccountId, int formStatus) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		return transferApplyDAO.findTransferApplys(userAccountId, formStatus);
	}

	@Override
	public TransferApply txAddOrModifyTransferApplyDraft(TransferApply draf, String user) throws OaException {
		if (draf == null) {
			throw new IllegalArgumentException();
		}
		if (draf.getId() == null) {
			draf.setId(createHolidayApplyID());
			draf.setApplyDate(new Timestamp(System.currentTimeMillis()));
			draf.setApplicant(user);
//			draft.setApplyDept(user.getGroupNames());
			draf.setApplyStatus(HolidayApply.STATUS_DRAFT);
			
			transferApplyDAO.save(draf);
		} else {
			TransferApply old = transferApplyDAO.findById(draf.getId());
			//不变的字段
			draf.setApplyDate(old.getApplyDate());
			draf.setApplicant(old.getApplicant());
			draf.setApplyStatus(old.getApplyStatus());
			draf.setPassed(old.getPassed());
			draf.setArchiveDate(old.getArchiveDate());
			draf.setFlowInstanceID(old.getFlowInstanceID());
			
			transferApplyDAO.merge(draf);
		}
		return draf;
	}
	
	@Override
	public TransferApply txDelTransferApply(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		TransferApply holi = transferApplyDAO.findById(id);
		if (holi != null) {
			transferApplyDAO.delete(holi);
		}
		return holi;
	}

	@Override
	public TransferApply txStartFlow(TransferApply draft, String user) throws OaException {
		TransferApply rei = txAddOrModifyTransferApplyDraft(draft, user);
    	rei.setApplyStatus(HolidayApply.STATUS_APPROVAL);
    	
    	String flowKey = null;
    	HRFlowConf rfc = hrFlowConfDAO.findByGroupName(draft.getTransferOutDept());
    	if (rfc != null) {
    		flowKey = rfc.getTransferFlowKey();
    	}
    	if (flowKey == null) {
    		throw new OaException("未配置部门的异动流程:" + draft.getTransferOutDept());
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
			// 调入部门
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_INGROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getTransferInDept());
			contextVariables.put(cv.getName(), cv);
			// 调出部门
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_OUTGROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getTransferOutDept());
			contextVariables.put(cv.getName(), cv);
			
			// 异动人
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_TRANUSERACCOUNTID,
					ContextVariable.DATATYPE_STRING, rei.getTransferUser());
			contextVariables.put(cv.getName(), cv);

			// 任务描述
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_TASKDESCRIPTION,
					ContextVariable.DATATYPE_STRING, rei.getTransferUserName()
							+ "的异动申请单");
			contextVariables.put(cv.getName(), cv);

			FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
			flowInstance.signal();
			context.save(flowInstance);
			// 设置表单视图的ID
			rei.setFlowInstanceID(flowInstance.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
			context.rollback();
			throw new OaException(e.getMessage());
		} finally {
			context.close();
		}
		
		transferApplyDAO.merge(rei);
		return rei;
	}

	 // 废弃
    @Override
    public void txModifyTransferApply(String formId, boolean arch) {
    	TransferApply holidayApply = transferApplyDAO.findById(formId);
        holidayApply.setArchiveDate(new Date());
        if (arch) {
        	// 设置Passed
        	holidayApply.setPassed(true);
	        // 设置状态
        	holidayApply.setApplyStatus(HolidayApply.STATUS_ARCH);
        	//修改部门与职位
        	try {
	        	String[] accountIDs = new String[]{holidayApply.getTransferUser()};
	        	GroupInfo og = gs.getGroup(holidayApply.getTransferOutDept());
	        	if (og != null) {
	        		gs.unBindUser(og.getGroupID(), accountIDs);
	        	}
	        	//
	        	GroupInfo ig = gs.getGroup(holidayApply.getTransferInDept());
	        	if (ig != null) {
	        		gs.bindUser(ig.getGroupID(), accountIDs);
	        	}
	        	
	        	StaffFlowApply staff = staffFlowApplyDAO.findByUserAccountId(holidayApply.getTransferUser());
	        	if (staff != null) {
	        		staff.setGroupName(holidayApply.getTransferInDept());
	        		
	                List<String> deptNameList = new ArrayList<String>();
                	deptNameList.add(ig.getGroupName());
                	getParentDept(ig, deptNameList );
                    String fullDeptName = "";
                    for (String dept : deptNameList) {
                    	fullDeptName += dept + "/";
        			}
                    if (StringUtils.isNotEmpty(fullDeptName)) {
                    	staff.setGroupFullName(fullDeptName.substring(0, fullDeptName.length()-1));
                    }
	                
	        		staff.setPost(holidayApply.getTransferInPost());
	        		staffFlowApplyDAO.update(staff);
	        	}
        	} catch(Exception e) {
        		log.error("异动归档，绑定部门出错", e);
        	}
        } else {
	        // 设置Passed
        	holidayApply.setPassed(false);
	        // 设置状态
        	holidayApply.setApplyStatus(HolidayApply.STATUS_CANCELLATION);
        }
        transferApplyDAO.update(holidayApply);
    }
    
    private void getParentDept(GroupInfo g, List<String> deptNameList) throws RpcAuthorizationException, MalformedURLException{
		if (g != null && StringUtils.isNotEmpty(g.getParentGroupID())) {
			GroupInfo group = gs.getGroupByID(g.getParentGroupID());
			deptNameList.add(group.getGroupName());
			getParentDept(group, deptNameList);
		}
	}
    
    @Override
    public List<Task> getEndedTasks(String id) {
    	TransferApply ha = transferApplyDAO.findById(id);
        if (ha == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(ha.getFlowInstanceID());
    }

    @Override
    public List<TransferApply> getDealingTransferApply(String userAccount, List<String> userRoles) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        List<TransferApply> holidayApply = transferApplyDAO.getDealingTransferApply(userAccount, userRoles);
        return holidayApply;
    }

    @Override
    public ListPage<TransferApply> getTrackTransferApply(TransferQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        if (rpq != null) {
            rpq.addOrder("applyDate", false);
        }
        ListPage<TransferApply> page = transferApplyDAO.getTrackOrArchTransferApply(rpq, userAccount, false);
        if (page != null && page.getDataList() != null) {
            List<TransferApply> list = page.getDataList();
            for (TransferApply r : list) {// 取得最后结束的一个任务
                List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceID());
                StringBuffer sb = new StringBuffer();
                if (tasks != null && tasks.size() > 0) {
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
        return page;
    }

    public ListPage<TransferApply> getArchTransferApply(TransferQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        ListPage<TransferApply> holidayApply = transferApplyDAO.getTrackOrArchTransferApply(rpq, userAccount, true);
        return holidayApply;
    }
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, TransferApply holi) throws OaException {
    	TransferApply rei = this.txAddOrModifyTransferApplyDraft(holi, null);
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}

			// 调入部门
			ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_INGROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getTransferInDept());
			ti.getFlowInstance().addContextVariable(var);

			// 调出部门
			var = new ContextVariable(SysConstants.FLOW_VARNAME_OUTGROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getTransferOutDept());
			ti.getFlowInstance().addContextVariable(var);

			// 异动人
			var = new ContextVariable(SysConstants.FLOW_VARNAME_TRANUSERACCOUNTID, ContextVariable.DATATYPE_STRING,
					rei.getTransferUser());
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
	
}
