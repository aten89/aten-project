package org.eapp.oa.device.blo.impl;

import java.util.Calendar;
import java.util.List;

import org.eapp.oa.device.blo.IDevRepairFormBiz;
import org.eapp.oa.device.dao.IDevRepairFormDAO;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.dto.DeviceSimpleQueryParameters;
import org.eapp.oa.device.hbean.DevRepairForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Hibernate;

import org.eapp.oa.system.exception.OaException;

public class DevRepairFormBiz implements IDevRepairFormBiz {
	private IDevRepairFormDAO devRepairFormDAO;
	private IDeviceDAO deviceDAO;
	public void setDeviceDAO(IDeviceDAO deviceDAO) {
		this.deviceDAO = deviceDAO;
	}
	public void setDevRepairFormDAO(IDevRepairFormDAO devRepairFormDAO) {
		this.devRepairFormDAO = devRepairFormDAO;
	}

	@Override
	public DevRepairForm getDevRepairFormById(String id) {
		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		DevRepairForm form = devRepairFormDAO.findById(id);
		if (form != null) {
			Hibernate.initialize(form.getDevice());
		}
		return form;
	}
	public List<DevRepairForm> findByDeviceID(String deviceID, Boolean createTimeOrder){
		return devRepairFormDAO.findByDeviceID(deviceID, createTimeOrder);
		
	}
	@Override
	public ListPage<DevRepairForm> queryDevRepairForm(
			DeviceSimpleQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		return devRepairFormDAO.queryDevRepairForm(qp);
	}

	@Override
	public DevRepairForm txAddDevRepairForm(DevRepairForm devRepairForm) throws OaException {
		if(devRepairForm == null || devRepairForm.getDevice()==null) {
			throw new IllegalArgumentException("非法参数！");
		}
		Device device = deviceDAO.findById(devRepairForm.getDevice().getId());
		if (device == null) {
			throw new IllegalArgumentException("非法参数！");
		}
		Calendar calendar =Calendar.getInstance();
		Integer no= devRepairFormDAO.getMaxSequenceNo(calendar.get(Calendar.YEAR));
		devRepairForm.setSequenceYear(calendar.get(Calendar.YEAR));
		devRepairForm.setRepairDeviceNO(no+1);
		devRepairFormDAO.save(devRepairForm);
		return devRepairForm;
	}
	
	@Override
	public void txModifyDevRepairForm(DevRepairForm devRepairForm) throws OaException {
		if(devRepairForm == null) {
			throw new IllegalArgumentException("非法参数！");
		}
		Device device = deviceDAO.findById(devRepairForm.getDevice().getId());
		if (devRepairForm == null) {
			throw new IllegalArgumentException("非法参数！");
		}
		
//		if (devRepairForm.getStatus() == DevRepairForm.STATUS_REPAIRED) {
//			if (!device.equals(devRepairForm.getDevice())) {//原来设备转为正常
//				throw new OaException("该维修单结束，不能修改设备");
//			}
//		}
		
		if (!device.equals(devRepairForm.getDevice())) {//原来设备转为正常
			//if (device.getStatus() != Device.STATUS_NORMAL) {
				//throw new IllegalArgumentException("该设备状态不可维修");
			//}
			//devRepairForm.getDevice().setStatus(Device.STATUS_NORMAL);
			deviceDAO.update(devRepairForm.getDevice());
			devRepairForm.setDevice(device);
			//device.setStatus(Device.STATUS_MAINTAIN);  //设备为维护状态
			deviceDAO.update(device);
		} 
		
//		if (!repairFormNo.equals(devRepairForm.getRepairFormNo())) {
//			checkRepairFormNo(repairFormNo);//是否重复
//		}
//		
//		devRepairForm.setRepairFormNo(repairFormNo);
//		devRepairForm.setAccountId(accountId);
//		String groupName = TransformTool.getDisplayGroupName(accountId);
//		devRepairForm.setGroupName(groupName);
//		
//		devRepairForm.setBudgetMoney(budgetMoney);
//		devRepairForm.setReason(reason);
//		if (remark != null) {
////			devRepairForm.setStatus(DevRepairForm.STATUS_REPAIRED);
//			devRepairForm.setRemark(remark);
//			devRepairForm.getDevice().setStatus(Device.STATUS_NORMAL);  //设备为正常状态
//		}
		devRepairFormDAO.save(devRepairForm);
	}

	@Override
	public void txDelDevRepairForm(String id) {
		if (id == null) {
			return;
		}
		DevRepairForm drf = devRepairFormDAO.findById(id);
		if (drf == null) {
			return;
		}
		//drf.getDevice().setStatus(Device.STATUS_NORMAL);//改为正常状态
		deviceDAO.update(drf.getDevice());
		devRepairFormDAO.delete(drf); 
	}

	
	
	public void checkRepairFormNo(String repairFormNo) throws OaException {
		List<DevRepairForm> devRepairForms = devRepairFormDAO.findByRepairFormNo(repairFormNo);
		if (devRepairForms != null && devRepairForms.size() > 0) {
			throw new OaException("维修单号不能重复");
		}
	}

	@Override
	public void txWriteRemark(String id, String remark) {
		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		DevRepairForm devRepairForm = getDevRepairFormById(id);
		Device device = devRepairForm.getDevice();
		//device.setStatus(Device.STATUS_NORMAL);  //设备为正常状态
//		devRepairForm.setStatus(DevRepairForm.STATUS_REPAIRED); //维修单为已修状态 
		devRepairForm.setRemark(remark);
		
		deviceDAO.update(device);
		devRepairFormDAO.update(devRepairForm);
	}

}
