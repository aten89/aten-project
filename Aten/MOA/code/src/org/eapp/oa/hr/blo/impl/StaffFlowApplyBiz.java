/**
 * 
 */
package org.eapp.oa.hr.blo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eapp.client.hessian.GroupService;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.client.util.UsernameCache;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IEmployeeNumberCreater;
import org.eapp.oa.hr.blo.IStaffFlowApplyBiz;
import org.eapp.oa.hr.dao.IHRFlowConfDAO;
import org.eapp.oa.hr.dao.IStaffFlowApplyDAO;
import org.eapp.oa.hr.dto.StaffAssignInfo;
import org.eapp.oa.hr.dto.StaffFlowQueryParameters;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.hr.hbean.StaffFlowApply;
import org.eapp.oa.hr.hbean.StaffFlowQueryAssign;
import org.eapp.oa.hr.hbean.WorkExperience;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.ExcelExportTools;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.Tools;
import org.eapp.oa.system.util.TransformTool;
import org.eapp.rpc.dto.GroupInfo;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;

/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class StaffFlowApplyBiz implements IStaffFlowApplyBiz {
	/**
     * log
     */
    public static final Log log = LogFactory.getLog(StaffFlowApplyBiz.class);

    private IStaffFlowApplyDAO staffFlowApplyDAO;
    private IHRFlowConfDAO  hrFlowConfDAO;
    private ITaskDAO taskDAO;
    private IEmployeeNumberCreater employeeNumberCreater;
    private SerialNoCreater noCreater;
	
    /**
     * addressListBiz
     */
//    private IAddressListBiz addressListBiz;
    

    public void setHrFlowConfDAO(IHRFlowConfDAO hrFlowConfDAO) {
		this.hrFlowConfDAO = hrFlowConfDAO;
	}
    
    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
    
	public void setEmployeeNumberCreater(IEmployeeNumberCreater employeeNumberCreater) {
		this.employeeNumberCreater = employeeNumberCreater;
	}

	public void setStaffFlowApplyDAO(IStaffFlowApplyDAO staffFlowApplyDAO) {
        this.staffFlowApplyDAO = staffFlowApplyDAO;
    }
    
    private String createStaffFlowApplyID() {
        if (noCreater == null) {
            noCreater = new SerialNoCreater(staffFlowApplyDAO.getMaxID());
        }
        return noCreater.createNo();
    }

	@Override
	public StaffFlowApply getStaffFlowApplyById(String id) {
		if (StringUtils.isBlank(id)) {
			throw new IllegalArgumentException();
		}
		StaffFlowApply holi = staffFlowApplyDAO.findById(id);
		return holi;
	}
	
	@Override
	public StaffFlowApply getStaffFlowApplyByUserAccountID(String userAccountID) {
		if (StringUtils.isBlank(userAccountID)) {
			return null;
		}
		return staffFlowApplyDAO.findByUserAccountId(userAccountID);
	}
	
	@Override
	public StaffFlowApply getEntryApplyInfo(String userAccountID) {
		if (userAccountID == null) {
			throw new IllegalArgumentException();
		}
		return staffFlowApplyDAO.findByUserAccountId(userAccountID);
	}

	@Override
	public List<StaffFlowApply> getStaffFlowApplys(String userAccountId, int formStatus) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		return staffFlowApplyDAO.findStaffFlowApplys(userAccountId, formStatus);
	}

	@Override
	public StaffFlowApply txAddOrModifyStaffFlowApplyDraft(StaffFlowApply draf, String user) throws OaException {
		if (draf == null) {
			throw new IllegalArgumentException();
		}
		
		if (draf.getId() == null) {
			//新增
			if (draf.getApplyType() == StaffFlowApply.TYPE_ENTRY 
					&& !staffFlowApplyDAO.checkMaxEmployeeNumber(draf.getEmployeeNumber(), draf.getId())) {
				//入职时才要判断工号
				throw new OaException("工号不能重复");
			}
			
			draf.setId(createStaffFlowApplyID());
			draf.setApplyDate(new Timestamp(System.currentTimeMillis()));
			draf.setApplicant(user);
			if (draf.getApplyStatus() == null) {
				draf.setApplyStatus(StaffFlowApply.STATUS_DRAFT);
			}
			staffFlowApplyDAO.save(draf);
		} else {
			//修改
			StaffFlowApply old = staffFlowApplyDAO.findById(draf.getId());
			
			if (draf.getApplyType() == StaffFlowApply.TYPE_ENTRY 
					&& !draf.getEmployeeNumber().equals(old.getEmployeeNumber())//变动时判断
					&& !staffFlowApplyDAO.checkMaxEmployeeNumber(draf.getEmployeeNumber(), draf.getId())) {
				//入职时才要判断工号
				throw new OaException("工号不能重复");
			}
			

//			old.setCompanyArea(draf.getCompanyArea());
//			old.setGroupName(draf.getGroupName());
//			old.setUserAccountID(draf.getUserAccountID());
//			old.setPost(draf.getPost());
//			old.setEmployeeNumber(draf.getEmployeeNumber());
//			old.setUserName(draf.getUserName());
//			old.setSex(draf.getSex());
//			old.setIdCard(draf.getIdCard());
//			old.setBirthdate(draf.getBirthdate());
//			old.setDescription(draf.getDescription());
//			old.setEntryDate(draf.getEntryDate());
//			old.setResignDate(draf.getResignDate());
//			old.setStaffStatus(draf.getStaffStatus());
//			old.setGroupFullName(draf.getGroupFullName());
//			old.setLevel(draf.getLevel());
//			old.setFormalDate(draf.getFormalDate());
//			old.setAge(draf.getAge());
//			old.setEducation(draf.getEducation());
//			old.setFinishSchool(draf.getFinishSchool());
//			old.setProfessional(draf.getProfessional());
//			old.setDegree(draf.getDegree());
//			old.setContractType(draf.getContractType());
//			old.setContractStartDate(draf.getContractStartDate());
//			old.setContractEndDate(draf.getContractEndDate());
//			old.setMobile(draf.getMobile());
//			old.setOfficeTel(draf.getOfficeTel());
//			old.setEmail(draf.getEmail());
//			old.setPoliticalStatus(draf.getPoliticalStatus());
//			old.setNation(draf.getNation());
//			old.setNativePlace(draf.getNativePlace());
//			old.setHomeAddr(draf.getHomeAddr());
//			old.setZipCode(draf.getZipCode());
//			old.setDomicilePlace(draf.getDomicilePlace());
//			old.setDomicileType(draf.getDomicileType());
//			old.setMaritalStatus(draf.getMaritalStatus());
//			old.setHadKids(draf.getHadKids());
//			old.setSalesExperience(draf.getSalesExperience());
//			old.setFinancialExperience(draf.getFinancialExperience());
//			old.setFinancialQualification(draf.getFinancialQualification());
//			old.setWorkStartDate(draf.getWorkStartDate());
//			old.setSupervisor(draf.getSupervisor());
//			old.setRecruitmentType(draf.getRecruitmentType());
//			old.setRecommended(draf.getRecommended());
//			old.setSeniority(draf.getSeniority());
//			old.setEmergencyContact(draf.getEmergencyContact());
//			old.setEmergencyContactTel(draf.getEmergencyContactTel());
//			old.setBankCardNO(draf.getBankCardNO());
//			old.setBankType(draf.getBankType());
//			old.setTrainInfo(draf.getTrainInfo());
//			old.setSkillsInfo(draf.getSkillsInfo());
//			old.setResignType(draf.getResignType());
//			old.setResignReason(draf.getResignReason());
//			old.setResignDesc(draf.getResignDesc());
//			old.setAchievement(draf.getAchievement());
//			old.setProject(draf.getProject());
//			old.setTranStartDate(draf.getTranStartDate());
//			old.setTranEndDate(draf.getTranEndDate());
//			old.setTranCost(draf.getTranCost());
//			old.setPenalty(draf.getPenalty());
//			old.setStaffClass(draf.getStaffClass());
//			old.setWorkExperiences(draf.getWorkExperiences());
//
//			staffFlowApplyDAO.update(old);
//			return old;
			
			//不变的字段
			draf.setApplyType(old.getApplyType());
			draf.setApplyDate(old.getApplyDate());
			draf.setApplicant(old.getApplicant());
			draf.setApplyStatus(old.getApplyStatus());
			draf.setPassed(old.getPassed());
			draf.setArchiveDate(old.getArchiveDate());
			draf.setFlowInstanceId(old.getFlowInstanceId());
			
			staffFlowApplyDAO.merge(draf);
		}
		return draf;
	}

	@Override
	public StaffFlowApply txDelStaffFlowApply(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		StaffFlowApply holi = staffFlowApplyDAO.findById(id);
		if (holi != null) {
			staffFlowApplyDAO.delete(holi);
		}
		return holi;
	}

	@Override
	public StaffFlowApply txStartFlow(StaffFlowApply draft, String user) throws OaException {
		
		StaffFlowApply rei = txAddOrModifyStaffFlowApplyDraft(draft, user);
    	rei.setApplyStatus(StaffFlowApply.STATUS_APPROVAL);
    	
    	boolean isEntry = rei.getApplyType() == StaffFlowApply.TYPE_ENTRY;
    	String flowKey = null;
    	HRFlowConf rfc = hrFlowConfDAO.findByGroupName(draft.getGroupName());
    	if (rfc != null) {
    		flowKey = isEntry ? rfc.getEntryFlowKey() : rfc.getResignFlowKey();
    	}
    	if (flowKey == null) {
    		throw new OaException("未配置部门的" + (isEntry ? "入职" : "离职") + "流程:" + draft.getGroupName());
    	}
    	if (!isEntry) {
    		//离职时验证用户帐号
			try {
				UserAccountService uas = new UserAccountService();
				UserAccountInfo ua = uas.getUserAccount(draft.getUserAccountID());
				if (ua == null) {
	    			throw new OaException("系统不存在用户帐号：" + draft.getUserAccountID());
	    		}
			} catch (Exception e) {
				e.printStackTrace();
				throw new OaException(e.getMessage());
			}
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
			// 入/离职人
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_STAFFUSERACCOUNTID,
					ContextVariable.DATATYPE_STRING, rei.getUserAccountID());
			contextVariables.put(cv.getName(), cv);
			// 发起人
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_USERACCOUNTID,
					ContextVariable.DATATYPE_STRING, user);
			contextVariables.put(cv.getName(), cv);
			// 部门
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_GROUPNAME,
					ContextVariable.DATATYPE_STRING, rei.getGroupName());
			contextVariables.put(cv.getName(), cv);

			// 任务描述
			cv = new ContextVariable(
					SysConstants.FLOW_VARNAME_TASKDESCRIPTION,
					ContextVariable.DATATYPE_STRING, rei.getUserName()
							+ (isEntry ?  "的入职申请" : "的离职申请"));
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
		// 
		hrFlowConfDAO.merge(rei);
		return rei;
	}
	
	 // 废弃
    @Override
    public void txModifyStaffFlowApply(String formId, boolean arch) {
        StaffFlowApply staffFlowApply = staffFlowApplyDAO.findById(formId);
        staffFlowApply.setArchiveDate(new Date());
        if (arch) {
        	// 设置Passed
        	staffFlowApply.setPassed(true);
	        // 设置状态
        	staffFlowApply.setApplyStatus(StaffFlowApply.STATUS_ARCH);
        	if (staffFlowApply.getApplyType() == StaffFlowApply.TYPE_ENTRY) {
	        	//添加联系人
	        	 AddressList addrList = new AddressList();
	        	 addrList.setUserAccountId(staffFlowApply.getUserAccountID());
	             addrList.setEmployeeNumber(staffFlowApply.getEmployeeNumber());
	             addrList.setUserEnterCorpDate(staffFlowApply.getEntryDate());
	             addrList.setUserSex(staffFlowApply.getSex() == 0 ? "M" : "F");
	             addrList.setUserBirthDate(staffFlowApply.getBirthdate());
	             staffFlowApplyDAO.save(addrList);
        	} else {
        		//离职，将用户账号失效日期改为今天
        		try {
	        		UserAccountService uas = new UserAccountService();
	        		UserAccountInfo ui = uas.getUserAccount(staffFlowApply.getUserAccountID());
	        		uas.modifyUserAccount(ui.getAccountID(), ui.getDisplayName(), ui.getIsLock(), 
	        				new Date(), ui.getDescription());
        		} catch(Exception e) {
        			log.error("离职修改用户为失效失败", e);
        		}
        	}
        } else {
	        // 设置Passed
        	staffFlowApply.setPassed(false);
	        // 设置状态
        	staffFlowApply.setApplyStatus(StaffFlowApply.STATUS_CANCELLATION);
        }
        staffFlowApplyDAO.update(staffFlowApply);
    }
    
    @Override
    public List<StaffFlowApply> getDealingStaffFlowApply(String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        List<StaffFlowApply> StaffFlowApply = staffFlowApplyDAO.getDealingStaffFlowApply(userAccount);
        return StaffFlowApply;
    }

    @Override
    public List<Task> getEndedTasks(String id) {
        StaffFlowApply ha = staffFlowApplyDAO.findById(id);
        if (ha == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(ha.getFlowInstanceId());
    }
    
    public ListPage<StaffFlowApply> getTrackStaffFlowApply(StaffFlowQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        if (rpq != null) {
            rpq.addOrder("applyDate", false);
        }
        ListPage<StaffFlowApply> page = staffFlowApplyDAO.getTrackOrArchStaffFlowApply(rpq, userAccount, false);
        if (page != null && page.getDataList() != null) {
            List<StaffFlowApply> list = page.getDataList();
            for (StaffFlowApply r : list) {// 取得最后结束的一个任务
                List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceId());
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

    public ListPage<StaffFlowApply> getArchStaffFlowApply(StaffFlowQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        ListPage<StaffFlowApply> StaffFlowApply = staffFlowApplyDAO.getTrackOrArchStaffFlowApply(rpq, userAccount, true);
        return StaffFlowApply;
    }
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, StaffFlowApply holi) throws OaException {
    	this.txAddOrModifyStaffFlowApplyDraft(holi, null);
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}

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
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String staffId, String userAccountID) throws OaException {
    	StaffFlowApply staff = staffFlowApplyDAO.findById(staffId);
    	if (staff == null) {
    		throw new IllegalArgumentException();
    	}
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}

			ti.setComment(comment);
			if (transitionName != null) {
				ti.end(transitionName);
			} else {
				ti.end();
			}
			context.save(ti);
			UserAccountService uas = new UserAccountService();
			if (staff.getApplyType() == StaffFlowApply.TYPE_ENTRY) {
				if (userAccountID == null) {
					throw new OaException("预开帐号不能为空");
				}
	    		//添加系统帐号
	    		uas.addUserAccount(userAccountID, staff.getUserName(), false, null, null);
	    		GroupService gs = new GroupService();
	    		GroupInfo group = gs.getGroup(staff.getGroupName());
	    		if (group == null) {
	    			throw new OaException("部门不存在:" + staff.getGroupName());
	    		}
	    		gs.bindUser(group.getGroupID(), new String[]{userAccountID});
	    		
	    		staff.setUserAccountID(userAccountID);
	    		staffFlowApplyDAO.update(staff);
	    	} else {
	    		//删除系统账号
	    		uas.deleteUserAccounts(new String[]{staff.getUserAccountID()});
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			context.rollback();
			throw new OaException(e.getMessage());
		} finally {
			context.close();
		}
    }
	
    public String getMaxEmployeeNumber() {
    	String maxNO = staffFlowApplyDAO.queryMaxEmployeeNumber();
    	return employeeNumberCreater.createNextNO(maxNO);
    }
    
    public List<StaffFlowApply> getStaffFlowApplyByAccountIds(List<String> uids, int pageNo, int pageSize) throws OaException {
        if (uids == null) {
            throw new OaException("无查询数据");
        }
        int totalCount = uids.size();// 总记录数

        int firstResultIndex = (pageNo - 1) * pageSize;

        if (firstResultIndex > totalCount || totalCount == 0) {// 开始记录大于总记录数
            throw new OaException("无查询数据");
        }

        // 过滤翻页
        List<String> pageUids = new ArrayList<String>(pageSize);
        for (int i = firstResultIndex; pageSize != 0 && (i < firstResultIndex + pageSize && i < uids.size())
                || pageSize == 0 && i < uids.size(); i++) {
            pageUids.add(uids.get(i));
        }

        List<StaffFlowApply> staffFlows = staffFlowApplyDAO.queryStaffFlowApply(pageUids);
        for (String uid : pageUids) {
        	StaffFlowApply al = new StaffFlowApply();
            al.setUserAccountID(uid);
            if (!staffFlows.contains(al)) {
            	al.setUserName(UsernameCache.getDisplayName(uid));
            	al.setGroupName(TransformTool.getDisplayGroupName(uid));
            	staffFlows.add(al);
            }
        }

        // 排序
        if (staffFlows != null) {
            Collections.sort(staffFlows, new Comparator<StaffFlowApply>() {
                @Override
                public int compare(StaffFlowApply o1, StaffFlowApply o2) {
                    return o1.getUserAccountID().compareTo(o2.getUserAccountID());
                }

            });
        }

        return staffFlows;
    }
    
    public ListPage<StaffFlowApply> queryStaffFlowApply(StaffFlowQueryParameters rqp) {
    	ListPage<StaffFlowApply> page = staffFlowApplyDAO.queryStaffFlowApply(rqp);
    	if (page != null && page.getDataList() != null) {
    		for (StaffFlowApply sfa : page.getDataList()) {
    			if (sfa.getApplyType() == StaffFlowApply.TYPE_RESIGN) {
    				//如果是离职单，查找对应的入职单
    				sfa.setRefStaffFlowApply(staffFlowApplyDAO.findByUserAccountId(sfa.getUserAccountID()));
    			}
    		}
    	}
    	return page;
    }
    
    @Override
    public ListPage<StaffFlowApply> queryMyStaffFlowApplys(StaffFlowQueryParameters rqp, String userAccountID) {
    	List<StaffFlowQueryAssign> assigns = staffFlowApplyDAO.findStaffFlowQueryAssigns(userAccountID);
    	rqp.setUserAccountID(userAccountID);
    	List<String> groupNames = new ArrayList<String>();
		if (assigns != null && !assigns.isEmpty()) {
			for (StaffFlowQueryAssign as : assigns) {
				groupNames.add(as.getGroupName());
			}
		} else {
			//加个随机数，避免列表为空时SQL IN出错
			groupNames.add(Tools.generateUUID());
		}
		rqp.setGroupNames(groupNames);
    	return staffFlowApplyDAO.queryStaffFlowApply(rqp);
    }
    
    @Override
    public ListPage<StaffFlowApply> queryContractPrompt(StaffFlowQueryParameters rqp, int beforeDays) {
    	rqp.setApplyType(StaffFlowApply.TYPE_ENTRY);
    	Calendar now = Calendar.getInstance();
    	clertTime(now);
    	now.add(Calendar.DAY_OF_YEAR, beforeDays);
    	
    	rqp.setContractEndDate(now.getTime());
    	ListPage<StaffFlowApply> page = staffFlowApplyDAO.queryStaffFlowApply(rqp);
    	if (page != null && page.getDataList() != null) {
    		for (StaffFlowApply sfa : page.getDataList()) {
    			sfa.setStatDatys(statDays(sfa.getContractEndDate()));
    		}
    	}
    	return page;
    }
    
    @Override
    public ListPage<StaffFlowApply> queryFormalPrompt(StaffFlowQueryParameters rqp, int beforeDays) {
    	rqp.setApplyType(StaffFlowApply.TYPE_ENTRY);
    	Calendar now = Calendar.getInstance();
    	clertTime(now);
    	now.add(Calendar.DAY_OF_YEAR, beforeDays);
    	
    	rqp.setFormalDate(now.getTime());
    	ListPage<StaffFlowApply> page = staffFlowApplyDAO.queryStaffFlowApply(rqp);
    	if (page != null && page.getDataList() != null) {
    		for (StaffFlowApply sfa : page.getDataList()) {
    			sfa.setStatDatys(statDays(sfa.getFormalDate()));
    		}
    	}
    	return page;
    }
    
    @Override
    public ListPage<StaffFlowApply> queryBirthdayPrompt(StaffFlowQueryParameters rqp, int beforeDays) {
    	rqp.setApplyType(StaffFlowApply.TYPE_ENTRY);
    	Calendar now = Calendar.getInstance();
    	now.add(Calendar.DAY_OF_YEAR, beforeDays);
    	rqp.setBirthdate(now.getTime());
    	ListPage<StaffFlowApply> page = staffFlowApplyDAO.queryStaffFlowApply(rqp);
    	if (page != null && page.getDataList() != null) {
    		for (StaffFlowApply sfa : page.getDataList()) {
    			sfa.setStatDatys(statBirthDays(sfa.getBirthdate()));
    		}
    	}
    	return page;
    }
    
    /**
     * 清除时、分、秒
     * @param cal
     */
    private void clertTime(Calendar cal) {
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    }
    
    private int statDays(Date endDate) {
    	Calendar cal = Calendar.getInstance();
    	clertTime(cal);
    	long nowMil = cal.getTimeInMillis();
    	cal.setTime(endDate);
    	clertTime(cal);
    	long days = (cal.getTimeInMillis() - nowMil) / (1000 * 60 * 60 * 24);
    	return (int)days;
    }
    
    private int statBirthDays(Date endDate) {
    	Calendar cal = Calendar.getInstance();
    	clertTime(cal);
    	cal.clear(Calendar.YEAR);
    	long nowMil = cal.getTimeInMillis();
    	cal.setTime(endDate);
    	clertTime(cal);
    	cal.clear(Calendar.YEAR);
    	long days = (cal.getTimeInMillis() - nowMil) / (1000 * 60 * 60 * 24);
    	return (int)days;
    }

	@Override
	public void csExportStaffFlowApply(StaffFlowQueryParameters rqp,
			String expNameAndValue, String filePath) throws IOException, OaException {
		if (rqp == null || filePath == null || expNameAndValue == null) {
            throw new IllegalArgumentException();
        }
        // 设置查询一次为500条数据
		rqp.setPageSize(0);
        // 取得数据
		ListPage<StaffFlowApply> listPage = this.queryStaffFlowApply(rqp);
        if (listPage.getTotalCount() <= 0) {
            throw new OaException("查询结果为空!");
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        ExcelExportTools tools = new ExcelExportTools(wb);
        // 标题样式
        HSSFCellStyle cellStyle = tools.getCellStyle(null, true, true);
        // 普通内容样式
        HSSFCellStyle contentCellStyle = tools.getCellStyle(null, false, false);

        List<StaffFlowApply> data = listPage.getDataList();
        // 创建一个工作簿

        HSSFSheet sheet = wb.createSheet("员工基本信息");
        // 创建标题列

        String[] nameAndValue = expNameAndValue.split(";");
        String[] titles = new String[nameAndValue.length];
        String[] values = new String[nameAndValue.length];
        for (int i = 0; i < nameAndValue.length; i++) {
            String[] str = nameAndValue[i].split(",");
            titles[i] = str[0];
            values[i] = str[1];
        }
        HSSFRow row = sheet.createRow((short) 0);
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titles[i]));
            cell.setCellStyle(cellStyle);
        }
        // 把数据写入Sheet
        writeToSheet(sheet, data, values, contentCellStyle);
        File file = new File(filePath);
		//父目录
		File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
        	//文件夹创建失败
    		throw new IOException("文件夹创建失败");
    	}
        
        file.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
		
	}
	
	private void writeToSheet(HSSFSheet sheet, List<StaffFlowApply> data, String[] values, HSSFCellStyle contentStyle) {
        if (data == null || data.isEmpty()) {
        	return;
        }
        for (StaffFlowApply r : data) {
        	if (r.getApplyType() == StaffFlowApply.TYPE_RESIGN && r.getRefStaffFlowApply() != null) {
				//如果是离职单，列表显示入职单的信息
				r = r.getRefStaffFlowApply();
			}
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            for (int i = 0; i < values.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(contentStyle);
                if ("employeeNumber".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getEmployeeNumber())));
                } else if ("userName".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getUserName())));
                } else if ("sex".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(r.getSex() != null && r.getSex() == 0 ? "男" : "女"));
                } else if ("staffStatus".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getStaffStatus())));
                } else if ("groupName".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getGroupName())));
                } else if ("groupFullName".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getGroupFullName())));
                } else if ("post".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getPost())));
                } else if ("level".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getLevel())));
                } else if ("entryDate".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getEntryDate(), SysConstants.STANDARD_DATE_PATTERN)));
                } else if ("formalDate".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getFormalDate(), SysConstants.STANDARD_DATE_PATTERN)));
                } else if ("idCard".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getIdCard())));
                } else if ("birthdate".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getBirthdate(), SysConstants.STANDARD_DATE_PATTERN)));
                } else if ("age".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getAge())));
                } else if ("education".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getEducation())));
                } else if ("finishSchool".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getFinishSchool())));
                } else if ("professional".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getProfessional())));
                } else if ("degree".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getDegree())));
                } else if ("contractType".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getContractType())));
                } else if ("contractStartDate".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getContractStartDate(), SysConstants.STANDARD_DATE_PATTERN)));
                } else if ("contractEndDate".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getContractEndDate(), SysConstants.STANDARD_DATE_PATTERN)));
                } else if ("mobile".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getMobile())));
                } else if ("officeTel".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getOfficeTel())));
                } else if ("email".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getEmail())));
                } else if ("politicalStatus".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getPoliticalStatus())));
                } else if ("nation".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getNation())));
                } else if ("nativePlace".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getNativePlace())));
                } else if ("homeAddr".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getHomeAddr())));
                } else if ("zipCode".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getZipCode())));
                } else if ("domicilePlace".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getDomicilePlace())));
                } else if ("domicileType".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getDomicileType())));
                } else if ("maritalStatus".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getMaritalStatus())));
                } else if ("hadKids".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(r.getHadKids() == 1 ? "是" : "否"));
                } else if ("salesExperience".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(r.getSalesExperience() == 1 ? "是" : "否"));
                } else if ("financialExperience".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(r.getFinancialExperience() == 1 ? "是" : "否"));
                } else if ("financialQualification".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(r.getFinancialQualification() == 1 ? "是" : "否"));
                } else if ("workStartDate".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getWorkStartDate(), SysConstants.STANDARD_DATE_PATTERN)));
                } else if ("supervisor".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getSupervisor())));
                } else if ("recruitmentType".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getRecruitmentType())));
                } else if ("recommended".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getRecommended())));
                } else if ("seniority".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getSeniority())));
                } else if ("emergencyContact".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getEmergencyContact())));
                } else if ("emergencyContactTel".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getEmergencyContactTel())));
                } else if ("bankCardNO".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getBankCardNO())));
                } else if ("bankType".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getBankType())));
                } else if ("trainInfo".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getTrainInfo())));
                } else if ("skillsInfo".equals(values[i])) {
                    cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getSkillsInfo())));
                
                } else if ("workExperiences".equals(values[i])) {
                	StringBuffer works = new StringBuffer();
    				if (r.getWorkExperiences() != null) {
    					for (WorkExperience we : r.getWorkExperiences()) {
    						works.append(DataFormatUtil.noNullValue(we.getStartDate(), SysConstants.STANDARD_DATE_PATTERN)).append(" / ")
    						.append(DataFormatUtil.noNullValue(we.getEndDate(), SysConstants.STANDARD_DATE_PATTERN)).append(" / ")
    						.append(DataFormatUtil.noNullValue(we.getCompanyName())).append(" / ")
    						.append(DataFormatUtil.noNullValue(we.getPostName())).append(" / ")
    						.append(DataFormatUtil.noNullValue(we.getPostDuty())).append("<br/>");
    					}
    				}
    				cell.setCellValue(new HSSFRichTextString(works.toString()));
                }
            }
        }
        //自适应列宽
        for (int i = 0; i < values.length; i++) {
        	sheet.autoSizeColumn((short)i, true);
        }
    }
	
	
	public ListPage<StaffAssignInfo> getUserAccountIdsByDeptAndAccount(String userDeptName, String userAccountQueryString, 
			int pageNo, int pageSize) {
		ListPage<StaffAssignInfo> page = new ListPage<StaffAssignInfo>();
		try {
			
		 	UserAccountService uas = new UserAccountService();
		 	if (StringUtils.isBlank(userDeptName)) {
		 		userDeptName = null;
		 	}
		 	if (StringUtils.isBlank(userAccountQueryString)) {
		 		userAccountQueryString = null;
		 	}
		 	List<UserAccountInfo> users =  uas.queryUserAccounts(userDeptName, userAccountQueryString);
		 	if (users == null) {
	            return page;
	        }
	        int totalCount = users.size();// 总记录数
	        int firstResultIndex = (pageNo - 1) * pageSize;

	        if (firstResultIndex > totalCount || totalCount == 0) {// 开始记录大于总记录数
	            return page;
	        }
	        int endResultIndex = firstResultIndex + pageSize;
	        if (endResultIndex > totalCount) {
	        	endResultIndex = totalCount;
	        }
	        //翻页
		 	List<StaffAssignInfo> dataList = new ArrayList<StaffAssignInfo>();
		 	for (int i = firstResultIndex; i < endResultIndex; i++) {
		 		StaffAssignInfo si = new StaffAssignInfo();
		 		UserAccountInfo u = users.get(i);
		 		List<StaffFlowQueryAssign> assigns = staffFlowApplyDAO.findStaffFlowQueryAssigns(u.getAccountID());
		 		StringBuffer assignStr = new StringBuffer();
				if (assigns != null && !assigns.isEmpty()) {
					for (StaffFlowQueryAssign as : assigns) {
						assignStr.append(as.getGroupName()).append(",");
					}
				}
		 		si.setUser(u);
		 		si.setAssignValue(assignStr.toString());
		 		dataList.add(si);
		 	}
		 	
		 	
		 	page.setCurrentPageNo(pageNo);
		 	page.setCurrentPageSize(pageSize);
		 	page.setTotalCount(totalCount);
		 	page.setDataList(dataList);
		 	return page;
		 } catch (MalformedURLException e) {
		     log.error("getUserAccountByGroup faild", e);
		 } catch (RpcAuthorizationException e) {
		     log.error("getUserAccountByGroup faild", e);
		 }
		 return page;
	 }

	@Override
	public void txSaveStaffFlowAssign(String userAccountId, String[] groupNames) {
		staffFlowApplyDAO.deleteQueryAssign(userAccountId);
		if (groupNames == null) {
			return;
		}
		//排除重复
		Set<String> groupNameSet = new HashSet<String>();
		groupNameSet.addAll(Arrays.asList(groupNames));
		for (String groupName : groupNameSet) {
			if (StringUtils.isBlank(groupName)) {
				continue;
			}
			StaffFlowQueryAssign assign = new StaffFlowQueryAssign();
			assign.setGroupName(groupName);
			assign.setUserAccountID(userAccountId);
			staffFlowApplyDAO.save(assign);
		}
		
	}

//	@Override
//	public List<StaffFlowQueryAssign> queryStaffFlowQueryAssigns(String userAccountId) {
//		return staffFlowApplyDAO.findStaffFlowQueryAssigns(userAccountId);
//	}
}
