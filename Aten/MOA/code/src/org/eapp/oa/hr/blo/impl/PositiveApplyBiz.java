/**
 * 
 */
package org.eapp.oa.hr.blo.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IPositiveApplyBiz;
import org.eapp.oa.hr.dao.IHRFlowConfDAO;
import org.eapp.oa.hr.dao.IStaffFlowApplyDAO;
import org.eapp.oa.hr.dao.IPositiveApplyDAO;
import org.eapp.oa.hr.dto.PositiveQueryParameters;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.hr.hbean.PositiveApply;
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
 * @author zsy
 * @version Jun 12, 2009
 */
public class PositiveApplyBiz implements IPositiveApplyBiz {
	/**
     * log
     */
    public static final Log log = LogFactory.getLog(PositiveApplyBiz.class);

    private IPositiveApplyDAO positiveApplyDAO;
    private IHRFlowConfDAO  hrFlowConfDAO;
    private ITaskDAO taskDAO;
    private IStaffFlowApplyDAO staffFlowApplyDAO;
    
    private SerialNoCreater noCreater;
    
    public void setHrFlowConfDAO(IHRFlowConfDAO hrFlowConfDAO) {
		this.hrFlowConfDAO = hrFlowConfDAO;
	}
    
    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
    
    public void setStaffFlowApplyDAO(IStaffFlowApplyDAO staffFlowApplyDAO) {
		this.staffFlowApplyDAO = staffFlowApplyDAO;
	}

	public void setPositiveApplyDAO(IPositiveApplyDAO positiveApplyDAO) {
		this.positiveApplyDAO = positiveApplyDAO;
	}

	private String createHolidayApplyID() {
        if (noCreater == null) {
            noCreater = new SerialNoCreater(positiveApplyDAO.getMaxID());
        }
        return noCreater.createNo();
    }

	@Override
	public PositiveApply getPositiveApplyById(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		return positiveApplyDAO.findById(id);
	}

	@Override
	public List<PositiveApply> getPositiveApplys(String userAccountId, int formStatus) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		return positiveApplyDAO.findPositiveApplys(userAccountId, formStatus);
	}

	@Override
	public PositiveApply txAddOrModifyPositiveApplyDraft(PositiveApply draf, String user) throws OaException {
		if (draf == null) {
			throw new IllegalArgumentException();
		}
		if (draf.getId() == null) {
			draf.setId(createHolidayApplyID());
			draf.setApplyDate(new Timestamp(System.currentTimeMillis()));
			draf.setApplicant(user);
//			draft.setApplyDept(user.getGroupNames());
			draf.setApplyStatus(HolidayApply.STATUS_DRAFT);
			
			positiveApplyDAO.save(draf);
		} else {
			PositiveApply old = positiveApplyDAO.findById(draf.getId());
			//不变的字段
			draf.setApplyDate(old.getApplyDate());
			draf.setApplicant(old.getApplicant());
			draf.setApplyStatus(old.getApplyStatus());
			draf.setPassed(old.getPassed());
			draf.setArchiveDate(old.getArchiveDate());
			draf.setFlowInstanceID(old.getFlowInstanceID());
			
			positiveApplyDAO.merge(draf);
		}
		return draf;
	}
	
	@Override
	public PositiveApply txDelPositiveApply(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		PositiveApply holi = positiveApplyDAO.findById(id);
		if (holi != null) {
			positiveApplyDAO.delete(holi);
		}
		return holi;
	}

	@Override
	public PositiveApply txStartFlow(PositiveApply draft, String user) throws OaException {
		PositiveApply rei = txAddOrModifyPositiveApplyDraft(draft, user);
    	rei.setApplyStatus(HolidayApply.STATUS_APPROVAL);
    	
    	String flowKey = null;
    	HRFlowConf rfc = hrFlowConfDAO.findByGroupName(draft.getDept());
    	if (rfc != null) {
    		flowKey = rfc.getPositiveFlowKey();
    	}
    	if (flowKey == null) {
    		throw new OaException("未配置部门的异动流程:" + draft.getDept());
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
					ContextVariable.DATATYPE_STRING, rei.getDept());
			contextVariables.put(cv.getName(), cv);
			
			// 转正人
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_POSITIVEACCOUNTID,
					ContextVariable.DATATYPE_STRING, rei.getPositiveUser());
			contextVariables.put(cv.getName(), cv);

			// 任务描述
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_TASKDESCRIPTION,
					ContextVariable.DATATYPE_STRING, rei.getPositiveUserName()
							+ "的转正申请单");
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
		
		positiveApplyDAO.merge(rei);
		return rei;
	}

	 // 废弃
    @Override
    public void txModifyPositiveApply(String formId, boolean arch) {
    	PositiveApply holidayApply = positiveApplyDAO.findById(formId);
        holidayApply.setArchiveDate(new Date());
        if (arch) {
        	// 设置Passed
        	holidayApply.setPassed(true);
	        // 设置状态
        	holidayApply.setApplyStatus(HolidayApply.STATUS_ARCH);
        	//修改员工基本信息为正式状态
        	StaffFlowApply staff = staffFlowApplyDAO.findByUserAccountId(holidayApply.getPositiveUser());
        	if (staff != null) {
        		staff.setStaffStatus(StaffFlowApply.STAFF_STATUS_FORMAL);
        		staff.setFormalDate(holidayApply.getFormalDate());
        		staffFlowApplyDAO.update(staff);
        	}

        } else {
	        // 设置Passed
        	holidayApply.setPassed(false);
	        // 设置状态
        	holidayApply.setApplyStatus(HolidayApply.STATUS_CANCELLATION);
        }
        positiveApplyDAO.update(holidayApply);
    }
    
    
    @Override
    public List<Task> getEndedTasks(String id) {
    	PositiveApply ha = positiveApplyDAO.findById(id);
        if (ha == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(ha.getFlowInstanceID());
    }

    @Override
    public List<PositiveApply> getDealingPositiveApply(String userAccount, List<String> userRoles) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        List<PositiveApply> holidayApply = positiveApplyDAO.getDealingPositiveApply(userAccount, userRoles);
        return holidayApply;
    }

    @Override
    public ListPage<PositiveApply> getTrackPositiveApply(PositiveQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        if (rpq != null) {
            rpq.addOrder("applyDate", false);
        }
        ListPage<PositiveApply> page = positiveApplyDAO.getTrackOrArchPositiveApply(rpq, userAccount, false);
        if (page != null && page.getDataList() != null) {
            List<PositiveApply> list = page.getDataList();
            for (PositiveApply r : list) {// 取得最后结束的一个任务
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

    public ListPage<PositiveApply> getArchPositiveApply(PositiveQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        ListPage<PositiveApply> holidayApply = positiveApplyDAO.getTrackOrArchPositiveApply(rpq, userAccount, true);
        return holidayApply;
    }
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, PositiveApply holi) throws OaException {
    	PositiveApply rei = this.txAddOrModifyPositiveApplyDraft(holi, null);
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}

			// 部门
			ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getDept());
			ti.getFlowInstance().addContextVariable(var);

			// 异动人
			var = new ContextVariable(SysConstants.FLOW_VARNAME_POSITIVEACCOUNTID, ContextVariable.DATATYPE_STRING,
					rei.getPositiveUser());
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
