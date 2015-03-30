package org.eapp.oa.device.blo.impl;

import java.util.List;
import java.util.Set;

import org.eapp.oa.device.blo.IDeviceCheckItemBiz;
import org.eapp.oa.device.dao.IDeviceCheckItemDAO;
import org.eapp.oa.device.dao.IDeviceClassDAO;
import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.hibernate.Hibernate;

import org.eapp.oa.system.exception.OaException;

public class DeviceCheckItemBiz implements IDeviceCheckItemBiz {

	private IDeviceCheckItemDAO deviceCheckItemDAO;
	private IDeviceClassDAO deviceClassDAO;
	
	@Override
	public DeviceCheckItem getById(String id) {
		DeviceCheckItem deviceCheckItem = deviceCheckItemDAO.findById(id);
		return deviceCheckItem;
	}

	@Override
	public ListPage<DeviceClass> queryDeviceClassPage(QueryParameters qp) {
		QueryParameters deviceClassQueryParameters = new QueryParameters();
		deviceClassQueryParameters.addOrder("name", true);
		ListPage<DeviceClass> deviceClassPage = deviceClassDAO.findCheckItemDeviceClass(deviceClassQueryParameters);
		if (deviceClassPage != null) {
			if (deviceClassPage.getDataList() != null) {
				for (DeviceClass deviceClass : deviceClassPage.getDataList()) {
					initDeviceCheckItems(deviceClass);
				}
			}
		}
		return deviceClassPage;
	}

	private void initDeviceCheckItems(DeviceClass deviceClass) {
		if (deviceClass == null) {
			return;
		}
		Set<DeviceCheckItem> deviceCheckItems = deviceClass.getDeviceCheckItems();
		if (deviceCheckItems == null || deviceCheckItems.size() == 0) {
			return;
		}
		String deviceCheckItemStr = "";
		for (DeviceCheckItem deviceCheckItem : deviceCheckItems) {
			if (deviceCheckItemStr != null && !"".equals(deviceCheckItemStr)) {
				deviceCheckItemStr += "；";
			}
			deviceCheckItemStr += deviceCheckItem.getItemName();
			/*
			if (deviceCheckItem != null) {
				deviceCheckItemStr += "□" + deviceCheckItem.getItemName();
			}
			*/
			deviceClass.setDeviceCheckItemRemark(deviceCheckItem.getRemark());
		}
		deviceClass.setDeviceCheckItemStr(deviceCheckItemStr);
	}
	
	public DeviceClass queryCheckItemDeviceClass(String deviceClassID) {
		DeviceClass deviceClass = deviceClassDAO.findById(deviceClassID);
		Hibernate.initialize(deviceClass.getDeviceCheckItems());
		initDeviceCheckItems(deviceClass);
		return deviceClass;
	}
	
	@Override
	public void txDelByDeviceClassID(String id) {
		if (id == null || "".equals(id)) {
			throw new IllegalArgumentException("参数错误");
		}
		List<DeviceCheckItem> deviceCheckItems = deviceCheckItemDAO.findByDeviceClassID(id);
		if (deviceCheckItems != null) {
			for (DeviceCheckItem deviceCheckItem : deviceCheckItems) {
				deviceCheckItemDAO.delete(deviceCheckItem);
			}
		}
	}

	@Override
	public void txModifyDeviceCheckItem(String deviceClassID, String remark,
			List<String> itemNames) throws OaException {
		if (deviceClassID == null || "".equals(deviceClassID)) {
			throw new OaException("设备类别为空");
		}
		if (itemNames == null || itemNames.size() == 0) {
			throw new OaException("设备检查项未配置");
		}
		List<DeviceCheckItem> deviceCheckItems = deviceCheckItemDAO.findByDeviceClassID(deviceClassID);
		if (deviceCheckItems != null) {
			//先删除旧数据
			for (DeviceCheckItem deviceCheckItem : deviceCheckItems) {
				deviceCheckItemDAO.delete(deviceCheckItem);
			}
		}
		//保存新数据
		txSaveDeviceCheckItem(deviceClassID, remark, itemNames);
	}

	@Override
	public void txSaveDeviceCheckItem(String deviceClassID, String remark, List<String> itemNames) throws OaException {
		List<DeviceCheckItem> deviceCheckItems = deviceCheckItemDAO.findByDeviceClassID(deviceClassID);
		if (deviceCheckItems != null && deviceCheckItems.size() > 0) {
			throw new OaException("该设备类别的检查项已配置，不能再次配置！");
		}
		if (itemNames != null && itemNames.size() > 0) {
			int i = 0;
			for (String itemName : itemNames) {
				DeviceCheckItem deviceCheckItem = new DeviceCheckItem();
				deviceCheckItem.setItemName(itemName);
				DeviceClass deviceClass = deviceClassDAO.findById(deviceClassID);
				deviceCheckItem.setDeviceClass(deviceClass);
				deviceCheckItem.setDisplayOrder(i);
				deviceCheckItem.setRemark(remark);
				deviceCheckItemDAO.save(deviceCheckItem);
				i++;
			}
		}
	}

	@Override
	public List<DeviceCheckItem> queryCheckItemByDeviceClassId(
			String deviceClassID) {
		return deviceCheckItemDAO.findByDeviceClassID(deviceClassID);
	}
	
	public void setDeviceCheckItemDAO(IDeviceCheckItemDAO deviceCheckItemDAO) {
		this.deviceCheckItemDAO = deviceCheckItemDAO;
	}
	
	public void setDeviceClassDAO(IDeviceClassDAO deviceClassDAO) {
		this.deviceClassDAO = deviceClassDAO;
	}
}
