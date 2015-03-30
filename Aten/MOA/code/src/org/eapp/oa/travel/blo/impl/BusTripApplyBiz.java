package org.eapp.oa.travel.blo.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.travel.blo.IBusTripApplyBiz;
import org.eapp.oa.travel.dao.IBusTripApplyDAO;
import org.eapp.oa.travel.dao.IBusTripFlowConfDAO;
import org.eapp.oa.travel.dto.BusTripQueryParameters;
import org.eapp.oa.travel.hbean.BusTripApply;
import org.eapp.oa.travel.hbean.BusTripApplyDetail;
import org.eapp.oa.travel.hbean.BusTripFlowConf;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;


public class BusTripApplyBiz implements IBusTripApplyBiz {

    private IBusTripApplyDAO busTripApplyDAO;
    private ITaskDAO taskDAO;
    private IBusTripFlowConfDAO busTripFlowConfDAO;
    private SerialNoCreater noCreater;

    public void setBusTripApplyDAO(IBusTripApplyDAO busTripApplyDAO) {
        this.busTripApplyDAO = busTripApplyDAO;
    }

    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setBusTripFlowConfDAO(IBusTripFlowConfDAO busTripFlowConfDAO) {
		this.busTripFlowConfDAO = busTripFlowConfDAO;
	}
    
    private String createBusTripApplyID() {
        if (noCreater == null) {
            noCreater = new SerialNoCreater(busTripApplyDAO.getMaxID());
        }
        return noCreater.createNo();
    }
    
    @Override
    public BusTripApply getBusTripApplyById(String id) {
        BusTripApply ha = busTripApplyDAO.findById(id);
        return ha;
    }
    
    @Override
    public List<BusTripApply> getBusTripApplys(String userAccountId, int formStatus) {
    	if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		return busTripApplyDAO.findBusTripApplys(userAccountId, formStatus);
    }
    
    @Override
	public BusTripApply txDelBusTripApply(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		BusTripApply holi = busTripApplyDAO.findById(id);
		if (holi != null) {
			busTripApplyDAO.delete(holi);
		}
		return holi;
	}
    
    @Override
    public BusTripApply txStartFlow(BusTripApply draft, String user) throws OaException {

    	BusTripApply rei = txAddOrModifyApplyDraft(draft, user);
    	rei.setApplyStatus(HolidayApply.STATUS_APPROVAL);
    	
    	
    	String flowKey = null;
    	BusTripFlowConf rfc = busTripFlowConfDAO.findByGroupName(draft.getApplyDept());
    	if (rfc != null) {
    		flowKey = rfc.getFlowKey();
    	}
    	if (flowKey == null) {
    		throw new OaException("未配置部门的请假流程:" + draft.getApplyDept());
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
			// 隶属
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_AREA,
					ContextVariable.DATATYPE_STRING, rei.getRegional());
			contextVariables.put(cv.getName(), cv);

			// 指定审批人
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
					ContextVariable.DATATYPE_STRING, rei.getAppointTo());
			contextVariables.put(cv.getName(), cv);

			// 任务描述
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_TASKDESCRIPTION,
					ContextVariable.DATATYPE_STRING, rei.getApplicantName()
							+ "的请假单");
			contextVariables.put(cv.getName(), cv);
			
			// 天数
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPLY_DAYS, ContextVariable.DATATYPE_DOUBLE,
					rei.getTotalDays().toString());
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
		
		busTripApplyDAO.merge(rei);
		return rei;
    }
    
    @Override
    public void txModifyTravel(String tripId, boolean arch) {
        if (tripId == null) {
            throw new IllegalArgumentException("非法参数:tripId");
        }
        BusTripApply reim = busTripApplyDAO.findById(tripId);
        // 设置归档时间
        reim.setArchiveDate(new Date());
        if (arch) {
        	// 设置Passed
	        reim.setPassed(true);
	        // 设置状态
	        reim.setApplyStatus(BusTripApply.STATUS_ARCH);
        } else {
	        // 设置Passed
	        reim.setPassed(false);
	        // 设置状态
	        reim.setApplyStatus(BusTripApply.STATUS_CANCELLATION);
        }
        busTripApplyDAO.saveOrUpdate(reim);
    }
	
    @Override
	public BusTripApply txAddOrModifyApplyDraft(BusTripApply draf, String user) throws OaException {
    	if (draf == null) {
			throw new IllegalArgumentException();
		}
		if (draf.getId() == null) {
			draf.setId(createBusTripApplyID());
			draf.setApplyDate(new Timestamp(System.currentTimeMillis()));
			draf.setApplicant(user);
//			draft.setApplyDept(user.getGroupNames());
			draf.setApplyStatus(HolidayApply.STATUS_DRAFT);
			setDetailRef(draf);
			
			busTripApplyDAO.save(draf);
		} else {
			BusTripApply old = busTripApplyDAO.findById(draf.getId());
			//不变的字段
			draf.setApplyDate(old.getApplyDate());
			draf.setApplicant(old.getApplicant());
			draf.setApplyStatus(old.getApplyStatus());
			draf.setPassed(old.getPassed());
			draf.setArchiveDate(old.getArchiveDate());
			draf.setFlowInstanceId(old.getFlowInstanceId());
			setDetailRef(draf);
			
			busTripApplyDAO.merge(draf);
		}
		return draf;
	}
    
	private void setDetailRef(BusTripApply draft){
		
		if (draft.getBusTripApplyDetail() != null) {
			double totalDays = 0;
			for (BusTripApplyDetail id : draft.getBusTripApplyDetail()) {
				id.setBusTripApply(draft);
				totalDays += id.getDays();
//				System.out.println("\t" +id.getId()+ "=" + id.getFromPlace() + "=" + id.getToPlace() + "=" + id.getStartDate()+ "=" + id.getEndDate()+ "=" + id.getDays()+ "=" + id.getCausa());
			}
			draft.setTotalDays(totalDays);
		}
//		System.out.println(draft.getId()+ "=" + draft.getRegional()+ "=" + draft.getApplyDept()+ "=" + draft.getBorrowSum()+ "=" + draft.getTermType()+ "=" + draft.getTotalDays()+ "=" + draft.getAppointTo());
	}
	
	@Override
    public List<BusTripApply> getDealingTripApply(String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        List<BusTripApply> page = busTripApplyDAO.getDealingBusTripApply(userAccount);
        return page;
    }

    @Override
    public ListPage<BusTripApply> getTrackTripApply(BusTripQueryParameters rqp, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        if (rqp != null) {
            rqp.addOrder("applyDate", false);
        }
        ListPage<BusTripApply> page = busTripApplyDAO.getTrackOrArchTripApply(rqp, userAccount, false);
        if (page != null && page.getDataList() != null) {
            List<BusTripApply> list = page.getDataList();
            for (BusTripApply r : list) {// 取得最后结束的一个任务
                List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceId());
                StringBuffer sb = new StringBuffer();
                if (tasks != null && tasks.size() > 0) {
                    for (Task t : tasks) {
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
                Hibernate.initialize(r.getBusTripApplyDetail());
            }
        }
        return page;
    }


    @Override
    public List<Task> getEndedTasks(String id) {
        BusTripApply ha = busTripApplyDAO.findById(id);
        if (ha == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(ha.getFlowInstanceId());
    }

    @Override
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, BusTripApply trip) throws OaException {
    	BusTripApply rei = txAddOrModifyApplyDraft(trip, null);
		
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}

			// 财务隶属
			ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_AREA,
					ContextVariable.DATATYPE_STRING, rei.getRegional());
			ti.getFlowInstance().addContextVariable(var);

			// 指定审批人
			var = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
					ContextVariable.DATATYPE_STRING, rei.getAppointTo());
			ti.getFlowInstance().addContextVariable(var);

			// 天数
			var = new ContextVariable(SysConstants.FLOW_VARNAME_APPLY_DAYS, ContextVariable.DATATYPE_DOUBLE,
					rei.getTotalDays().toString());
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
    public ListPage<BusTripApply> getArchTripApply(BusTripQueryParameters rqp, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        if (userAccount != null) {
            rqp.addOrder("archiveDate", false);
        }
        ListPage<BusTripApply> page = busTripApplyDAO.getTrackOrArchTripApply(rqp, userAccount, true);
        if (page != null && page.getDataList() != null) {
            List<BusTripApply> list = page.getDataList();
            for (BusTripApply r : list) {
                Hibernate.initialize(r.getBusTripApplyDetail());
            }
        }
        return page;
    }

    @Override
    public ListPage<BusTripApply> queryArchTripApply(BusTripQueryParameters rqp, Timestamp startDate, Timestamp endDate) {
        if (rqp != null) {
            rqp.addOrder("applyDate", false);
        }
        ListPage<BusTripApply> page = busTripApplyDAO.queryArchTripApply(rqp, startDate, endDate);
        if (page != null && page.getDataList() != null) {
            List<BusTripApply> list = page.getDataList();
            for (BusTripApply r : list) {
                Hibernate.initialize(r.getBusTripApplyDetail());
            }
        }
        return page;
    }
}
