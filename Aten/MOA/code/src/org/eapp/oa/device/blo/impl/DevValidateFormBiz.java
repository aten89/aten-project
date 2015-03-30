package org.eapp.oa.device.blo.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eapp.oa.device.blo.IDevValidateFormBiz;
import org.eapp.oa.device.dao.IDevValidateFormDAO;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DevicePropertyDetail;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.util.web.DataFormatUtil;
import org.hibernate.Hibernate;


public class DevValidateFormBiz implements IDevValidateFormBiz {

	private IDevValidateFormDAO devValidateFormDAO;
	private IDeviceDAO deviceDAO;

	public void setDevValidateFormDAO(IDevValidateFormDAO devValidateFormDAO) {
		this.devValidateFormDAO = devValidateFormDAO;
	}
	
	public void setDeviceDAO(IDeviceDAO deviceDAO) {
		this.deviceDAO = deviceDAO;
	}

	@Override
	public DevValidateForm txSaveDevValidateForm(String id, String deviceID,
			int valiType, Date inDate, String accountID,
			Date valiDate, boolean isMoney, double deductMoney, String remark,
			String valiDetailStr) {
		if (deviceID == null  || accountID == null) {
			throw new IllegalArgumentException("非法参数:参数不能为空！");
		}
		DevValidateForm devValidateForm = null;
		if (id == null) {
			devValidateForm = new DevValidateForm();
			Device device =  deviceDAO.findById(deviceID);
			if (device == null) {
				throw new IllegalArgumentException();
			}
			devValidateForm.setDevice(device);
		} else {
			devValidateForm = devValidateFormDAO.findById(id);
			if (devValidateForm == null) {
				throw new IllegalArgumentException("非法参数:设备报废单为空!");
			}
		}
		devValidateForm.setValiDate(valiDate);
		devValidateForm.setValiType(valiType);
		devValidateForm.setAccountID(accountID);
		devValidateForm.setRemark(remark);
		devValidateFormDAO.saveOrUpdate(devValidateForm);
		return devValidateForm;
	}

	@Override
	public DevValidateForm txDeleteDevValidateForm(String id) {

		if (id == null) {
			return null;
		}
		DevValidateForm devValidateForm = devValidateFormDAO.findById(id);
		if (devValidateForm == null) {
			return null;
		}
		devValidateFormDAO.delete(devValidateForm);
		return devValidateForm;
	}


	@Override
	public DevValidateForm getDevValidateFormById(String id) {
		DevValidateForm devValidateForm = devValidateFormDAO.findById(id);
		if(devValidateForm!=null){
			Hibernate.initialize(devValidateForm.getDeviceValiDetails());
		}
		
		return devValidateForm;
	}

	public DevValidateForm getDevValidateFormByPurchaseDevId(String purchaseDevID) {
		DevValidateForm devValidateForm = devValidateFormDAO.findByPurchaseDevId(purchaseDevID);
		if(devValidateForm!=null){
			Hibernate.initialize(devValidateForm.getDeviceValiDetails());
		}
		//初始化设备编号
		if (devValidateForm.getPurchaseDevice().getDeviceID() != null) {
			Device device = deviceDAO.findById(devValidateForm.getPurchaseDevice().getDeviceID());
			devValidateForm.getPurchaseDevice().setDeviceNo(device.getDeviceNO());
		}
		if (devValidateForm.getPurchaseDevice() != null) {
			PurchaseDevice d = devValidateForm.getPurchaseDevice();
			StringBuffer strCfgs = new StringBuffer();
			StringBuffer strCfgsStr = new StringBuffer();
			if (!Hibernate.isInitialized(d.getDevicePropertyDetails())) {
				Hibernate.initialize(d.getDevicePropertyDetails());
			}
			Set<DevicePropertyDetail> devicePropertyDetails = d.getDevicePropertyDetails();
			if (devicePropertyDetails != null) {
				for (DevicePropertyDetail prop : devicePropertyDetails) {
					if (prop != null) {
						String pn = DataFormatUtil.noNullValue(prop.getPropertyName());
						String pv = DataFormatUtil.noNullValue(prop.getPropertyValue());
						strCfgs.append(pn + ":" + pv + "\r\n");
						strCfgsStr.append(pn + ":" + pv + ";");
					}
				}
			}
			d.setConfigListStr(strCfgsStr.toString());
			d.setConfigList(strCfgs.toString());
		}
		
		return devValidateForm;
	}
	
	@Override
	public List<DevValidateForm> getDevValidateForms(String deviceID, Integer valiType) {
		if (deviceID == null) {
			throw new IllegalArgumentException();
		}
		List<DevValidateForm> list = devValidateFormDAO.findByDeviceIdAndValiType(valiType, deviceID);
		if(!list.isEmpty()){
			for (DevValidateForm devValidateForm : list) {
				Hibernate.initialize(devValidateForm.getDeviceValiDetails());
			}
		}
		return list;
	}
	
	public int getCurrentYearValidFormNO(int validType, int year) {
		return devValidateFormDAO.findCurrentYearValidFormNO(validType, year);
	}
}
