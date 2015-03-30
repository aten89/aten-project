package org.eapp.oa.device.blo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.device.blo.IDevFlowBiz;
import org.eapp.oa.device.blo.IDevRepairFormBiz;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.dao.IDevAllocateFormDAO;
import org.eapp.oa.device.dao.IDevDiscardDisposeDAO;
import org.eapp.oa.device.dao.IDeviceFlowDAO;
import org.eapp.oa.device.dto.DevFlowProcessFormDTO;
import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.DevRepairForm;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.device.hbean.DiscardDealDevList;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;

import org.eapp.oa.system.config.SysConstants;

public class DevFlowBiz implements IDevFlowBiz {

	private IDeviceApplyBiz deviceApplyBiz;
	private IDevRepairFormBiz devRepairFormBiz;
	private IDeviceDiscardBiz deviceDiscardBiz;
	private IDevAllocateFormDAO devAllocateFormDAO;
	private IDevDiscardDisposeDAO devDiscardDisposeDAO;
	private IDeviceFlowDAO deviceFlowDAO;
	private ITaskDAO taskDAO;
	
	public void setDeviceApplyBiz(IDeviceApplyBiz deviceApplyBiz) {
		this.deviceApplyBiz = deviceApplyBiz;
	}

	public void setDevRepairFormBiz(IDevRepairFormBiz devRepairFormBiz) {
		this.devRepairFormBiz = devRepairFormBiz;
	}

	public void setDeviceDiscardBiz(IDeviceDiscardBiz deviceDiscardBiz) {
		this.deviceDiscardBiz = deviceDiscardBiz;
	}

	public void setDevAllocateFormDAO(IDevAllocateFormDAO devAllocateFormDAO) {
		this.devAllocateFormDAO = devAllocateFormDAO;
	}

	public void setDevDiscardDisposeDAO(IDevDiscardDisposeDAO devDiscardDisposeDAO) {
		this.devDiscardDisposeDAO = devDiscardDisposeDAO;
	}
	
	public void setDeviceFlowDAO(IDeviceFlowDAO deviceFlowDAO) {
		this.deviceFlowDAO = deviceFlowDAO;
	}

	@Override
	public List<DevFlowProcessFormDTO> queryAllDevFlowList(String deviceID) {
		List<DevFlowProcessFormDTO> devFlowProcessListDTOs = new ArrayList<DevFlowProcessFormDTO>();
		DevFlowProcessFormDTO devFlowProcessFormDTO = null;
		//申购记录
		PurchaseDevice purchaseDevice = deviceApplyBiz.getArchPurchaseDevByDeviceID(deviceID, null);
		if (purchaseDevice != null) {
			devFlowProcessFormDTO = new DevFlowProcessFormDTO();
			devFlowProcessFormDTO.setId(purchaseDevice.getDevPurchaseForm().getId());
			devFlowProcessFormDTO.setFormNO(purchaseDevice.getDevPurchaseForm().getFullFormNO());
			devFlowProcessFormDTO.setOperator(purchaseDevice.getDevPurchaseForm().getApplicant());
			devFlowProcessFormDTO.setOperateDate(purchaseDevice.getDevPurchaseForm().getArchiveDate());
			devFlowProcessFormDTO.setOperatorGroupName(purchaseDevice.getDevPurchaseForm().getApplyGroupName());
			String strReturnBackDate = DataFormatUtil.noNullValue(purchaseDevice.getReturnBackDate(),SysConstants.STANDARD_DATE_PATTERN);
			devFlowProcessFormDTO.setRemark("通过申购领用了该设备。");
			if (!"".equals(strReturnBackDate)) {
				devFlowProcessFormDTO.setRemark((devFlowProcessFormDTO.getRemark() == null ? "" : devFlowProcessFormDTO.getRemark()) + 
						"于" + strReturnBackDate + "归还");
			}
			devFlowProcessFormDTO.setOperatorType(DevFlowProcessFormDTO.OPTTYPE_PURCHASE);//申购
			devFlowProcessListDTOs.add(devFlowProcessFormDTO);
		}
		List<DevPurchaseList> devPurchaseLists = deviceApplyBiz.getArchDevUseListByDeviceID(deviceID, null);//领用，含调拨，不含调拨入库
		for (DevPurchaseList devPurchaseList: devPurchaseLists) {
			if (devPurchaseList != null) {
				devFlowProcessFormDTO = new DevFlowProcessFormDTO();
				devFlowProcessFormDTO.setId(devPurchaseList.getDevPurchaseForm().getId());
				devFlowProcessFormDTO.setFormNO(devPurchaseList.getDevPurchaseForm().getFullFormNO());
				devFlowProcessFormDTO.setOperator(devPurchaseList.getDevPurchaseForm().getApplicant());
				devFlowProcessFormDTO.setOperateDate(devPurchaseList.getDevPurchaseForm().getArchiveDate());
				
				devFlowProcessFormDTO.setOperatorGroupName(devPurchaseList.getDevPurchaseForm().getApplyGroupName());
				String strReturnBackDate = DataFormatUtil.noNullValue(devPurchaseList.getReturnBackDate(),SysConstants.STANDARD_DATE_PATTERN);
				if (!"".equals(strReturnBackDate)) {
					devFlowProcessFormDTO.setRemark("于" + strReturnBackDate + "归还");
				}
				devFlowProcessFormDTO.setOperatorType(DevFlowProcessFormDTO.OPTTYPE_USE);
				if (DevPurchaseForm.APPLY_TYPE_ALLOT == devPurchaseList.getDevPurchaseForm().getApplyType()) {
					//设置调拨单ID
					List<DevAllocateForm> refForms = devAllocateFormDAO.findByProperty("refDevUseFormID", devPurchaseList.getDevPurchaseForm().getId());
					devFlowProcessFormDTO.setRefFormID(refForms.get(0).getId());
					devFlowProcessFormDTO.setFormNO(refForms.get(0).getFullFormNO());
					if (DevAllocateForm.MOVE_TYPE_INSIDE.equals(refForms.get(0).getMoveType())) {
						devFlowProcessFormDTO.setOperatorType(DevFlowProcessFormDTO.OPTTYPE_ALLOT_INSIDE);
					} else if (DevAllocateForm.MOVE_TYPE_DEPT.equals(refForms.get(0).getMoveType())) {
						devFlowProcessFormDTO.setOperatorType(DevFlowProcessFormDTO.OPTTYPE_ALLOT_DEPT);
					}
				}
				devFlowProcessListDTOs.add(devFlowProcessFormDTO);
			}
		}
		
		//增加一条调拨入库的记录
		List<DevAllocateList> devAllocateLists = devAllocateFormDAO.queryArchDevAllotListByDeviceID(deviceID, null);//调拨
		for (DevAllocateList devAllocateList: devAllocateLists) {
			if (devAllocateList != null && devAllocateList.getDevAllocateForm() != null && 
					DevAllocateForm.MOVE_TYPE_STORAGE.equals(devAllocateList.getDevAllocateForm().getMoveType())) {
				//调拨入库
				devFlowProcessFormDTO = new DevFlowProcessFormDTO();
				devFlowProcessFormDTO.setId(devAllocateList.getDevAllocateForm().getId());
				devFlowProcessFormDTO.setFormNO(devAllocateList.getDevAllocateForm().getFullFormNO());
				devFlowProcessFormDTO.setOperator(devAllocateList.getDevAllocateForm().getApplicant());
				devFlowProcessFormDTO.setOperateDate(devAllocateList.getDevAllocateForm().getArchiveDate());
				devFlowProcessFormDTO.setOperatorGroupName(devAllocateList.getDevAllocateForm().getApplyGroupName());
				//如果是入库
				devFlowProcessFormDTO.setOperatorType(DevFlowProcessFormDTO.OPTTYPE_STORAGE);
				devFlowProcessListDTOs.add(devFlowProcessFormDTO);
			}
		}
		
		List<DevRepairForm> devRepairLists = devRepairFormBiz.findByDeviceID(deviceID, null);//维修
		for (DevRepairForm devRepairList: devRepairLists) {
			if (devRepairList != null) {
				devFlowProcessFormDTO = new DevFlowProcessFormDTO();
				devFlowProcessFormDTO.setId(devRepairList.getId());
				devFlowProcessFormDTO.setFormNO(devRepairList.getRepairDeviceCode());
				devFlowProcessFormDTO.setOperator(devRepairList.getAccountID());
				devFlowProcessFormDTO.setOperatorGroupName(devRepairList.getGroupName());
				devFlowProcessFormDTO.setOperateDate(devRepairList.getCreateTime());
				devFlowProcessFormDTO.setOperatorType(DevFlowProcessFormDTO.OPTTYPE_MAINT);
				devFlowProcessListDTOs.add(devFlowProcessFormDTO);
			}
		}
		
		List<DiscardDevList> discardDevLists = deviceDiscardBiz.getArchDevScrapListByDeviceID(deviceID, null);//报废
		for (DiscardDevList discardDevList: discardDevLists) {
			if (discardDevList != null) {
				devFlowProcessFormDTO = new DevFlowProcessFormDTO();
				devFlowProcessFormDTO.setId(discardDevList.getDevDiscardForm().getId());
				devFlowProcessFormDTO.setFormNO(discardDevList.getDevDiscardForm().getFullFormNO());
				devFlowProcessFormDTO.setOperator(discardDevList.getDevDiscardForm().getApplicant());
				devFlowProcessFormDTO.setOperatorGroupName(discardDevList.getDevDiscardForm().getApplyGroupName());
				devFlowProcessFormDTO.setOperateDate(discardDevList.getDevDiscardForm().getArchiveDate());
				int formType = discardDevList.getDevDiscardForm().getFormType().intValue();
				int optType = (formType == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue() ? DevFlowProcessFormDTO.OPTTYPE_SCRAP : DevFlowProcessFormDTO.OPTTYPE_LEAVE_DISPOSE);
				devFlowProcessFormDTO.setOperatorType(optType);
				List<DiscardDealDevList> devDiscardDealLists = devDiscardDisposeDAO.queryDevScrapDisposeListByDeviceID(deviceID, null);
				if (devDiscardDealLists != null && devDiscardDealLists.size() > 0) {
					//有报废处理单
					devFlowProcessFormDTO.setRefFormID(devDiscardDealLists.get(0).getDevDiscardDealForm().getId());
				}
				devFlowProcessListDTOs.add(devFlowProcessFormDTO);
			}
		}
		Collections.sort(devFlowProcessListDTOs, new Comparator<DevFlowProcessFormDTO>(){ 
	        public int compare(DevFlowProcessFormDTO o1, DevFlowProcessFormDTO o2){ 
	        	if (o1 != null && o1.getOperateDate() != null && o2 != null && o2.getOperateDate() != null) {
	        		return o1.getOperateDate().compareTo(o2.getOperateDate());
	        	}
	        	return -1;
	        } 
		});
		return devFlowProcessListDTOs;
	}

	@Override
	public ListPage<DeviceFlowView> queryDealDeviceFlowPage(
			DeviceFlowQueryParameters qp,String userID) {
		return deviceFlowDAO.findDealDeviceFlowPage(qp,userID);
	}

	@Override
	public ListPage<DeviceFlowView> queryArchDeviceFlowPage(
			DeviceFlowQueryParameters qp, String userID) {
		return deviceFlowDAO.findArchDeviceFlowPage(qp, userID);
	}

	@Override
	public ListPage<DeviceFlowView> queryDraftDeviceFlowPage(
			DeviceFlowQueryParameters qp, String userID) {
		return deviceFlowDAO.findDraftDeviceFlowPage(qp,userID);
	}

	@Override
	public ListPage<DeviceFlowView> queryTrackDeviceFlowPage(
			DeviceFlowQueryParameters qp, String userID) {
		ListPage<DeviceFlowView> page = deviceFlowDAO.findTrackDeviceFlowPage(qp, userID);
		if (page != null && page.getDataList() != null) {
			List<DeviceFlowView> list = page.getDataList();
			for (DeviceFlowView r : list) {//取得最后结束的一个任务
				List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceID());
				if (tasks != null && tasks.size()>0){
					Task task = tasks.get(0);
					r.setTask(task);
					String transactor = task.getTransactor() == null ? "" : task.getTransactor();
					if ("".equals(transactor)) {
						//Task中的transactor为空，则取TaskAssign表中的assignKey
						for (TaskAssign ta : task.getTaskAssigns()) {
							if (ta != null && ta.getAssignKey() != null) {
								if (transactor != null && !"".equals(transactor)) {
									transactor += ",";
								}
								transactor += UsernameCache.getDisplayName(ta.getAssignKey());
							}
						}
						task.setTransactor(transactor);
					}
				}
			}
		}
		return page;
	}

	public void txDealApproveTaskByFlowInstanceId(String flowInstanceId,String backBuyFlag,Integer type,String taskInstanceId, String comment, String transitionName) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			FlowInstance flowInstance = context.getFlowInstance(flowInstanceId);
			//设置流程上下文变量中，并启动流程 
//			Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
//			ContextVariable cv =null;
//			if(type==1){
//				//是否回购 默认为0
//				cv = new ContextVariable(SysConstants.FLOW_VARNAME_BACKBUYFLAG, 
//							ContextVariable.DATATYPE_STRING,  backBuyFlag);
//			}else{
//				//是否退库 默认为0
//				cv = new ContextVariable(SysConstants.FLOW_VARNAME_TOSTORAGEFLAG, 
//							ContextVariable.DATATYPE_STRING,  backBuyFlag);
//			}
//			
//			contextVariables.put(cv.getName(), cv); 
//			flowInstance.getContextVariables().remove(cv.getName());
//			flowInstance.getContextVariables().put(cv.getName(), cv);
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}
			if (comment != null) {
				comment = transitionName + "，" + comment;
			} else {
				comment = transitionName; 
			}
			ti.setComment(comment);
			if (transitionName != null) {
				ti.end(transitionName);
			} else {
				ti.end();
			}
			context.save(flowInstance);
			context.save(ti);
		}catch(RuntimeException e) {
			e.printStackTrace();
			context.rollback();
			throw e;
		} finally {
			context.close();
		}
	}

	public void setTaskDAO(ITaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	@Override
	public void txDealRejectTaskByFlowInstanceId(String flowInstanceId,
			String buyType, String taskInstanceId, String comment,String transitionName) {
		WfmContext context = WfmConfiguration.getInstance().getWfmContext();
		try {
			TaskInstance ti = context.getTaskInstance(taskInstanceId);
			if (ti == null) {
				throw new IllegalArgumentException();
			}
//			// 添加流程上下文变量，购买方式有改变
//			ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_BUYTYPE, 
//					ContextVariable.DATATYPE_STRING, buyType);
//			ti.getFlowInstance().addContextVariable(var);
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
			//modify by fangwenwei 添加抛出runtime异常 原：如果流程处理出现异常没有继续往外抛 导致流程与改方法保存事物不一致
            throw new RuntimeException("流程处理出现异常", e);
		} finally {
			context.close();
		}
	}

	

}
