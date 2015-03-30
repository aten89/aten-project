/**
 * 
 */
package org.eapp.oa.hr.blo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.hr.blo.IHolidayApplyBiz;
import org.eapp.oa.hr.blo.IHolidayTypeBiz;
import org.eapp.oa.hr.dao.IHRFlowConfDAO;
import org.eapp.oa.hr.dao.IHolidayApplyDAO;
import org.eapp.oa.hr.dto.HolidayQueryParameters;
import org.eapp.oa.hr.hbean.HRFlowConf;
import org.eapp.oa.hr.hbean.HolidayApply;
import org.eapp.oa.hr.hbean.HolidayDetail;
import org.eapp.oa.hr.hbean.HolidayType;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.ExcelExportTools;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;

/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class HolidayApplyBiz implements IHolidayApplyBiz {
	/**
     * log
     */
    public static final Log log = LogFactory.getLog(HolidayApplyBiz.class);

    private IHolidayApplyDAO holidayApplyDAO;
    private IHRFlowConfDAO  hrFlowConfDAO;
    private ITaskDAO taskDAO;
    
    private IHolidayTypeBiz holidayTypeBiz;
    
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

    public void setHolidayTypeBiz(IHolidayTypeBiz holidayTypeBiz) {
		this.holidayTypeBiz = holidayTypeBiz;
	}

	public void setHolidayApplyDAO(IHolidayApplyDAO holidayApplyDAO) {
        this.holidayApplyDAO = holidayApplyDAO;
    }
    
    private String createHolidayApplyID() {
        if (noCreater == null) {
            noCreater = new SerialNoCreater(holidayApplyDAO.getMaxID());
        }
        return noCreater.createNo();
    }

	@Override
	public HolidayApply getHolidayApplyById(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		HolidayApply holi = holidayApplyDAO.findById(id);
		if (holi != null) {
			holi.getHolidayDetail().size();
		}
		return holi;
	}

	@Override
	public List<HolidayApply> getHolidayApplys(String userAccountId, int formStatus) {
		if (userAccountId == null) {
			throw new IllegalArgumentException();
		}
		return holidayApplyDAO.findHolidayApplys(userAccountId, formStatus);
	}

	@Override
	public HolidayApply txAddOrModifyHolidayApplyDraft(HolidayApply draf, String user) throws OaException {
		if (draf == null) {
			throw new IllegalArgumentException();
		}
		if (draf.getId() == null) {
			draf.setId(createHolidayApplyID());
			draf.setApplyDate(new Timestamp(System.currentTimeMillis()));
			draf.setApplicant(user);
//			draft.setApplyDept(user.getGroupNames());
			draf.setApplyStatus(HolidayApply.STATUS_DRAFT);
			draf.setCancelFlag(false);
			setDetailRef(draf);
			
			holidayApplyDAO.save(draf);
		} else {
			HolidayApply old = holidayApplyDAO.findById(draf.getId());
			//不变的字段
			draf.setApplyDate(old.getApplyDate());
			draf.setApplicant(old.getApplicant());
			draf.setApplyStatus(old.getApplyStatus());
			draf.setPassed(old.getPassed());
			draf.setArchiveDate(old.getArchiveDate());
			draf.setFlowInstanceId(old.getFlowInstanceId());
			draf.setCancelFlag(old.getCancelFlag());
			setDetailRef(draf);
			
			holidayApplyDAO.merge(draf);
		}
		return draf;
	}
	
	private void setDetailRef(HolidayApply draft){
//		System.out.println(draft.getId()+ "=" + draft.getRegional()+ "=" + draft.getApplyDept()+ "=" + draft.getIsSpecial()+ "=" + draft.getSpecialReason()+ "=" + draft.getTotalDays()+ "=" + draft.getAppointTo());
		if (draft.getHolidayDetail() != null) {
			double totalDays = 0;
			for (HolidayDetail id : draft.getHolidayDetail()) {
				id.setHolidayApply(draft);
				totalDays += id.getDays();
//				System.out.println("\t" +id.getId()+ "=" + id.getHolidayName() + "=" + id.getStartDate() + "=" + id.getStartTime()+ "=" + id.getEndDate()+ "=" + id.getEndTime()+ "=" + id.getDays()+ "=" + id.getRemark());
			}
			draft.setTotalDays(totalDays);
		}
	}

	@Override
	public HolidayApply txDelHolidayApply(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		HolidayApply holi = holidayApplyDAO.findById(id);
		if (holi != null) {
			holidayApplyDAO.delete(holi);
		}
		return holi;
	}

	@Override
	public HolidayApply txStartFlow(HolidayApply draft, String user) throws OaException {
		Map<String, Double> holiMap = new HashMap<String, Double>();
		for (HolidayDetail detail : draft.getHolidayDetail()) {
    		Double days = holiMap.get(detail.getHolidayName());
    		if (days == null) {
    			days = 0d;
    		}
    		days += detail.getDays();
     		holiMap.put(detail.getHolidayName(), days);
    	}
		
		for (String holidayName : holiMap.keySet()) {
			HolidayType type = holidayTypeBiz.getHolidayTypeByName(holidayName);
			if (type == null) {
				throw new OaException("不存在假期：" + holidayName);
			}
			if (type.getMaxDays() != null && type.getMaxDays() < holiMap.get(holidayName)) {
				throw new OaException(holidayName + "单次请假天数不能超过" + type.getMaxDays() + "天");
			}
		}
		
		HolidayApply rei = txAddOrModifyHolidayApplyDraft(draft, user);
    	rei.setApplyStatus(HolidayApply.STATUS_APPROVAL);
    	
    	
    	String flowKey = null;
    	HRFlowConf rfc = hrFlowConfDAO.findByGroupName(draft.getApplyDept());
    	if (rfc != null) {
    		flowKey = rfc.getHolidayFlowKey();
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

			// 是否特批
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_ISSPECIAL, ContextVariable.DATATYPE_STRING,
					rei.getIsSpecial() ? "0" : "1");
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
		
		hrFlowConfDAO.merge(rei);
		return rei;
	}
	
	@Override
	public HolidayApply txStartCancelFlow(String holiId, List<HolidayDetail> details, String user) throws OaException {
		HolidayApply rei = holidayApplyDAO.findById(holiId);
		if (rei == null) {
			throw new IllegalArgumentException("记录不存在");
		}
		if (!user.equals(rei.getApplicant())) {
			throw new OaException("非本人不能申请销假");
		}
		
    	Map<String, HolidayDetail> detailMap = new HashMap<String, HolidayDetail>();
    	for (HolidayDetail d : rei.getHolidayDetail()) {
    		detailMap.put(d.getId(), d);
    	}
    	double totalCancelDays = 0;
    	for (HolidayDetail detail : details) {
//    		System.out.println(detail.getId() + "==" + detail.getCancelDays() + "==" + detail.getCancelRemark());
    		HolidayDetail od = detailMap.get(detail.getId());
    		if (od != null) {
    			if (detail.getCancelDays() == null) {
    				continue;
    			}
    			if (od.getDays() < detail.getCancelDays()) {
    				throw new OaException("销假天数不能大于请假天数：" + od.getHolidayName() + od.getDays() + "天");
    			}
    			od.setCancelDays(detail.getCancelDays());
    			od.setCancelRemark(detail.getCancelRemark());
    			totalCancelDays += detail.getCancelDays();
    		}
		}
    	if (totalCancelDays <= 0) {
    		throw new OaException("总销假天数为0");
    	}
    	rei.setCancelFlag(true);
		rei.setApplyStatus(HolidayApply.STATUS_CANCELAPPROVAL);
    	holidayApplyDAO.update(rei);
    	
    	String flowKey = null;
    	HRFlowConf rfc = hrFlowConfDAO.findByGroupName(rei.getApplyDept());
    	if (rfc != null) {
    		flowKey = rfc.getCanHolidayFlowKey();
    	}
    	if (flowKey == null) {
    		throw new OaException("未配置部门的销假流程:" + rei.getApplyDept());
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
							+ "的销假单");
			contextVariables.put(cv.getName(), cv);
			
			// 天数
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPLY_DAYS, ContextVariable.DATATYPE_DOUBLE,
					rei.getTotalDays().toString());
			contextVariables.put(cv.getName(), cv);

			// 是否特批
			cv = new ContextVariable(SysConstants.FLOW_VARNAME_ISSPECIAL, ContextVariable.DATATYPE_STRING,
					rei.getIsSpecial() ? "0" : "1");
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
    public void txModifyHolidayApply(String formId, boolean arch) {
        HolidayApply holidayApply = holidayApplyDAO.findById(formId);
        holidayApply.setArchiveDate(new Date());
        if (arch) {
        	// 设置Passed
        	holidayApply.setPassed(true);
	        // 设置状态
        	holidayApply.setApplyStatus(HolidayApply.STATUS_ARCH);
        } else {
	        // 设置Passed
        	holidayApply.setPassed(false);
	        // 设置状态
        	holidayApply.setApplyStatus(HolidayApply.STATUS_CANCELLATION);
        }
        holidayApplyDAO.update(holidayApply);
    }
    
    @Override
    public void txModifyCancelHolidayApply(String formId, boolean arch) {
        HolidayApply holidayApply = holidayApplyDAO.findById(formId);
        holidayApply.setApplyStatus(HolidayApply.STATUS_ARCH);//原表单都是归档
        if (arch) {
        	holidayApply.setArchiveDate(new Date());//更新归档时间
        	// 设置状态
            holidayApply.setCancelFlag(true);//销假
        } else {
        	holidayApply.setCancelFlag(false);//还原
        	//清除明细中的销假天数与说明
        	for (HolidayDetail d : holidayApply.getHolidayDetail()) {
        		d.setCancelDays(null);
        		d.setCancelRemark(null);
        	}
        }
	    

        holidayApplyDAO.update(holidayApply);
    }

    @Override
    public List<HolidayApply> getDealingHolidayApply(String userAccount, List<String> userRoles) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        List<HolidayApply> holidayApply = holidayApplyDAO.getDealingHolidayApply(userAccount, userRoles);
        return holidayApply;
    }

    @Override
    public List<Task> getEndedTasks(String id) {
        HolidayApply ha = holidayApplyDAO.findById(id);
        if (ha == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(ha.getFlowInstanceId());
    }
    
    public ListPage<HolidayApply> getTrackHolidayApply(HolidayQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        if (rpq != null) {
            rpq.addOrder("applyDate", false);
        }
        ListPage<HolidayApply> page = holidayApplyDAO.getTrackOrArchHolidayApply(rpq, userAccount, false);
        if (page != null && page.getDataList() != null) {
            List<HolidayApply> list = page.getDataList();
            for (HolidayApply r : list) {// 取得最后结束的一个任务
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
                Hibernate.initialize(r.getHolidayDetail());
            }
        }
        return page;
    }

    public ListPage<HolidayApply> getArchHolidayApply(HolidayQueryParameters rpq, String userAccount) {
        if (userAccount == null) {
            throw new IllegalArgumentException();
        }
        ListPage<HolidayApply> holidayApply = holidayApplyDAO.getTrackOrArchHolidayApply(rpq, userAccount, true);
        return holidayApply;
    }
    
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, HolidayApply holi) throws OaException {
    	HolidayApply rei = this.txAddOrModifyHolidayApplyDraft(holi, null);
		
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

			// 是否特批
			var = new ContextVariable(SysConstants.FLOW_VARNAME_ISSPECIAL, ContextVariable.DATATYPE_STRING,
					rei.getIsSpecial() ? "0" : "1");
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
    public Map<String, Double> getUserHolidayStat(String userAccountId) {
    	Map<String, Double> holiMap = new HashMap<String, Double>();
    	
    	Calendar now = Calendar.getInstance();
    	int year = now.get(Calendar.YEAR);
    	now.clear();
    	now.set(year, 0, 1);//重置到今年1月1日
    	Date startDate = now.getTime();
    	
    	List<HolidayDetail> holiDetails = holidayApplyDAO.findUserArchHolidays(userAccountId, startDate);
    	for (HolidayDetail detail : holiDetails) {
    		HolidayApply apply = detail.getHolidayApply();
    		Double days = holiMap.get(detail.getHolidayName());
    		if (days == null) {
    			days = 0d;
    		}
    		double _days = detail.getDays();
    		if (detail.getStartDate().before(startDate)) {
    			//有跨年的，重新计算属于今年的天数
    			_days = holidayTypeBiz.getDaysOfHoliday(detail.getHolidayName(), startDate, HolidayDetail.MORNING, detail.getEndDate(), detail.getEndTime());
    		}
    		
    		days += _days;//累计请假天数
    		if (apply.getCancelFlag() != null && apply.getCancelFlag()) {
    			double cancelDays = detail.getCancelDays() == null ? 0 : detail.getCancelDays();
    			if (cancelDays > _days) {
    				//重新计算后，销假天数有可能大于请假天数
    				days -= _days;
    			} else {
    				days -= cancelDays;//减去销假天数
    			}
    		}
     		holiMap.put(detail.getHolidayName(), days);
    	}
    	return holiMap;
    }
    
    @Override
    public ListPage<HolidayDetail> getHolidayDetail(HolidayQueryParameters rpq) {

        ListPage<HolidayDetail> details = holidayApplyDAO.findHolidayDetail(rpq);
        return details;
    }
    
    @Override
    public void csExportHolidayDetail(HolidayQueryParameters rqp, String filePath) throws IOException, OaException {
    	if (rqp == null || filePath == null) {
            throw new IllegalArgumentException();
        }
        // 设置查询一次为500条数据
		rqp.setPageSize(0);
        // 取得数据
		ListPage<HolidayDetail> listPage = this.getHolidayDetail(rqp);
        if (listPage.getTotalCount() <= 0) {
            throw new OaException("查询结果为空!");
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        ExcelExportTools tools = new ExcelExportTools(wb);
        // 标题样式
        HSSFCellStyle cellStyle = tools.getCellStyle(null, true, true);
        // 普通内容样式
        HSSFCellStyle contentCellStyle = tools.getCellStyle(null, false, false);

        List<HolidayDetail> data = listPage.getDataList();
        
        // 创建一个工作簿
        HSSFSheet sheet = wb.createSheet("员工请假信息");
        
        // 创建标题列
        HSSFRow row = sheet.createRow((short) 0);
        String[] titles = new String[]{"单号","请假人","所属部门","人员隶属","假期类型","请假时间","请假天数","销假天数","附加说明"};
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titles[i]));
            cell.setCellStyle(cellStyle);
        }
        
        // 把数据写入Sheet
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (HolidayDetail r : data) {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            //单号
            HSSFCell cell = row.createCell(0);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getHolidayApply().getId())));
            //请假人
            cell = row.createCell(1);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getHolidayApply().getApplicantName())));
            //所属部门
            cell = row.createCell(2);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getHolidayApply().getApplyDept())));
            //人员隶属
            cell = row.createCell(3);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getHolidayApply().getRegionalName())));
            //假期类型
            cell = row.createCell(4);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getHolidayName())));
            //请假时间
            cell = row.createCell(5);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString("从 " + sdf.format(r.getStartDate()) + r.getStartTimeStr() + " 到 " + sdf.format(r.getEndDate()) + r.getEndTimeStr()));
            //请假天数
            cell = row.createCell(6);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getDays())));
            //销假天数
            cell = row.createCell(7);
            cell.setCellStyle(contentCellStyle);
            String cancelDays = "";
            if (HolidayApply.STATUS_CANCELAPPROVAL != r.getHolidayApply().getApplyStatus() && r.getHolidayApply().getCancelFlag()) {
            	cancelDays = DataFormatUtil.noNullValue(r.getCancelDays());
            }
            cell.setCellValue(new HSSFRichTextString(cancelDays));
            //附加说明
            cell = row.createCell(8);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(r.getRemark())));
            
        }
        //自适应列宽
        for (int i = 0; i < titles.length; i++) {
        	sheet.autoSizeColumn((short)i, true);
        }
        
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
}
