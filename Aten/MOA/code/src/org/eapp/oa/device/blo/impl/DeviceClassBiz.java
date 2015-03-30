package org.eapp.oa.device.blo.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceClassBiz;
import org.eapp.oa.device.dao.IDeviceClassDAO;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCfgItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignArea;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.hibernate.Hibernate;

import org.eapp.oa.system.exception.OaException;

public class DeviceClassBiz implements IDeviceClassBiz {

	private IDeviceClassDAO deviceClassDAO;
	private IDeviceBiz deviceBiz;

	public void setDeviceClassDAO(IDeviceClassDAO deviceClassDAO) {
		this.deviceClassDAO = deviceClassDAO;
	}
	
	@Override
	public List<DeviceClass> getAllDeviceClass(String deviceTypeCode,String areaCode) {
		List<DeviceClass> list = deviceClassDAO.findByDeviceTypeCode(deviceTypeCode,areaCode);
		
		if(null != list) {
			for(DeviceClass dc : list) {
				Hibernate.initialize(dc.getAreaDeviceCfgs());
				for (AreaDeviceCfg areaDeviceCfg : dc.getAreaDeviceCfgs()) {
					if(areaCode!=null && areaCode.equals(areaDeviceCfg.getAreaCode())){
						dc.setMainDevFlag(areaDeviceCfg.getMainDevFlag());
						break;
					}
				}
				queryDeviceClassInfo(dc);
			}
		}
		return list;
	}
	
	@Override
	public List<DeviceClass> findAll(String deviceTypeCode) {
		List<DeviceClass> list = deviceClassDAO.findAll(deviceTypeCode);
		if(null != list) {
			for(DeviceClass dc : list) {
				queryDeviceClassInfo(dc);
			}
		}
		return list;
	}
	@Override
	public DeviceClass getDeviceClassById(String id) {
		if(null == id) {
			return null;
		}
		DeviceClass deviceClass = queryDeviceClassInfo(id);
		if(deviceClass==null){
			return null;
		}
		return deviceClass;
	}

	@Override
	public DeviceClass txAddDeviceClass(String classType, String name, String remark,String[] cfgItemIds)
			throws OaException {
		//判断是否重复
		isDeviceClassRepeat(classType,name);
		DeviceClass deviceClass = new DeviceClass();
		deviceClass.setName(name);
		deviceClass.setRemark(remark);
		deviceClass.setStatus(DeviceClass.STATUS_NORMAL);
		deviceClass.setDeviceType(classType);
		if(cfgItemIds!=null && cfgItemIds.length>0){
			Set<DeviceCfgItem> deviceCheckItems = new HashSet<DeviceCfgItem>();
			int i=0;
			for (String string : cfgItemIds) {
				DeviceCfgItem cfgItem = new DeviceCfgItem();
				cfgItem.setDeviceClass(deviceClass);
				cfgItem.setDisplayOrder(i++);
				DeviceProperty deviceProperty=new DeviceProperty();
				deviceProperty.setId(string);
				cfgItem.setDeviceProperty(deviceProperty);
				deviceCheckItems.add(cfgItem);
			}
			deviceClass.getDeviceCfgItems().addAll(deviceCheckItems);
		}
		deviceClassDAO.save(deviceClass);

		return deviceClass;
	}

	/**
	 * 验证设备分类是否重复
	 * 
	 * @param classKey
	 */
	private void isDeviceClassRepeat(String deviceType,String name) throws OaException{
		if(null == deviceType) {
			return;
		}
		List<DeviceClass> deviceClasses = deviceClassDAO.findByNameAndType(deviceType, name);
		if (deviceClasses != null && deviceClasses.size() > 0) {
			throw new OaException("同一资产类别下，设备名称不能重复!");
		}
	}

	@Override
	public DeviceClass txDelDeviceClass(String id)throws OaException {
		if(id == null) {
			return null;
		}
		
		DeviceClass deviceClass = deviceClassDAO.findById(id);
		if(deviceClass == null) {
			return null;
		}
		
		Hibernate.initialize(deviceClass.getAreaDeviceCfgs());
		if(deviceClass.getAreaDeviceCfgs()!=null && deviceClass.getAreaDeviceCfgs().size()>0){
			throw new OaException("该设备类别下已存在区域设备配置，不能删除!");
		}
		Hibernate.initialize(deviceClass.getDeviceCheckItems());
		if(deviceClass.getDeviceCheckItems()!=null && deviceClass.getDeviceCheckItems().size()>0){
			throw new OaException("该设备类别下已存在设备检查项，不能删除!");
		}
		Hibernate.initialize(deviceClass.getDeviceClassAssignAreas());
		if(deviceClass.getDeviceClassAssignAreas()!=null && deviceClass.getDeviceClassAssignAreas().size()>0){
			throw new OaException("该设备类别下已存在设备区域授权配置，不能删除!");
		}
		//判断该设备类别下是否存在设备信息 不存在直接删除；存在更改状态为0
		List<Device> list = deviceBiz.queryDeviceInfo(id);
		if(list!=null && list.size()>0){
			throw new OaException("该设备类别下已存在设备信息，不能删除!");
		}
		deviceClassDAO.delete(deviceClass);
		return deviceClass;
	}

	@Override
	public DeviceClass txModifyDeviceClass(String id, String classType, String name,String remark, String[] cfgItemIds)
			throws OaException {
		if (null == id) {
			throw new IllegalArgumentException("非法参数:设备类别ID不能为空！");
		}
		DeviceClass deviceClass = deviceClassDAO.findById(id);
		if(null == deviceClass) {
			return null;
		}
		// 判断分类名称是否重复
		if(deviceClass.getName()!=null && !deviceClass.getName().equals(name) && !deviceClass.getDeviceType().equals(classType)){
			isDeviceClassRepeat(classType,name);
		}
		Hibernate.initialize(deviceClass.getDeviceCfgItems());
		deviceClass.getDeviceCfgItems().clear();
		deviceClass.setName(name);
		deviceClass.setDeviceType(classType);
		deviceClass.setRemark(remark);
		if(cfgItemIds!=null && cfgItemIds.length>0){
			Set<DeviceCfgItem> deviceCheckItems = new HashSet<DeviceCfgItem>();
			int i=0;
			for (String string : cfgItemIds) {
				if(string==null ||string==""){
					continue;
				}
				DeviceCfgItem cfgItem = new DeviceCfgItem();
				cfgItem.setDeviceClass(deviceClass);
				cfgItem.setDisplayOrder(i++);
				
				DeviceProperty deviceProperty=new DeviceProperty();
				deviceProperty.setId(string);
				cfgItem.setDeviceProperty(deviceProperty);
				deviceCheckItems.add(cfgItem);
			}
			deviceClass.getDeviceCfgItems().addAll(deviceCheckItems);
		}
		deviceClassDAO.saveOrUpdate(deviceClass);
		return deviceClass;
	}

	@Override
	public List<DeviceCfgItem> queryDeviceCfgItem(String Id) {
		return deviceClassDAO.findDeviceCfgItem(Id);
	}
	public DeviceClass queryDeviceClassInfo(String id) {
		if(null == id) {
			return null;
		}
		DeviceClass deviceClass = deviceClassDAO.findById(id);
		Hibernate.initialize(deviceClass.getDeviceCfgItems());
		if(deviceClass.getDeviceCfgItems()!=null){
			boolean flag = false;
			String itemNames="";
			for (DeviceCfgItem deviceCfgItem : deviceClass.getDeviceCfgItems()) {
				if(deviceCfgItem.getDeviceProperty()!=null){
					if(flag){
						itemNames+="；"+deviceCfgItem.getDeviceProperty().getPropertyName();
					}else{
						itemNames+=deviceCfgItem.getDeviceProperty().getPropertyName();
					}
				
					flag = true;
				}
				deviceClass.setClassItemName(itemNames);
			}
		}
		return deviceClass;
	}
	private DeviceClass queryDeviceClassInfo(DeviceClass deviceClass) {
		if(null == deviceClass) {
			return null;
		}
		if(deviceClass.getDeviceCfgItems()!=null){
			boolean flag = false;
			String itemNames="";
			for (DeviceCfgItem deviceCfgItem : deviceClass.getDeviceCfgItems()) {
				Hibernate.initialize(deviceCfgItem.getDeviceProperty());
				if(deviceCfgItem.getDeviceProperty()!=null){
					if(flag){
						itemNames+="；"+deviceCfgItem.getDeviceProperty().getPropertyName();
					}else{
						itemNames+=deviceCfgItem.getDeviceProperty().getPropertyName();
					}
					flag = true;
				}
				deviceClass.setClassItemName(itemNames);
			}
		}
		return deviceClass;
	}
	@Override
	public ListPage<DeviceClass> queryDeviceClassPage(QueryParameters qp) {
		ListPage<DeviceClass> page=deviceClassDAO.findDeviceClassPage(qp);
		if(page.getDataList()!=null){
			for (DeviceClass deviceClass : page.getDataList()) {
				Hibernate.initialize(deviceClass.getDeviceCfgItems());
				queryDeviceClassInfo(deviceClass);
			}
		}
		return page;
	}

	@Override
	public List<DeviceClass> getAssignClass(String deviceType, String areaCode,
			Integer assgintype, String userAccountId, List<String> groupNames,
			List<String> postNames) {
		return deviceClassDAO.findAssignDeciveClass(deviceType, areaCode, assgintype, userAccountId, groupNames, postNames);
	}

	public IDeviceBiz getDeviceBiz() {
		return deviceBiz;
	}

	public void setDeviceBiz(IDeviceBiz deviceBiz) {
		this.deviceBiz = deviceBiz;
	}

	@Override
	public DeviceClass txUpdateDeviceClassStatus(String id) {
		if (null == id) {
			throw new IllegalArgumentException("非法参数:设备类别ID不能为空！");
		}
		DeviceClass deviceClass = deviceClassDAO.findById(id);
		if(null == deviceClass) {
			return null;
		}
		deviceClass.setStatus(DeviceClass.STATUS_NORMAL);
		deviceClassDAO.update(deviceClass);
		return deviceClass;
	}


	@Override
	public List<DeviceClass> getAssignClassSelect(List<String> deviceType,
			List<String> areaCode, Integer assgintype, String userAccountId,
			List<String> groupNames, List<String> postNames) {
		return deviceClassDAO.findAssignClass(deviceType, areaCode, assgintype, userAccountId, groupNames, postNames);
	}
	
	@Override
	public List<DeviceClassAssign> getAssignClass(List<String> deviceType,
			List<String> areaCode, Integer assgintype, String userAccountId,
			List<String> groupNames, List<String> postNames) {
		List<DeviceClassAssign> list = deviceClassDAO.findSelectAssignClass(deviceType, areaCode, assgintype, userAccountId, groupNames, postNames);
		for (DeviceClassAssign deviceClassAssign : list) {
			Hibernate.initialize(deviceClassAssign.getDeviceClassAssignAreas());
			if(deviceClassAssign.getDeviceClassAssignAreas()!=null){
				for (DeviceClassAssignArea devicArea : deviceClassAssign.getDeviceClassAssignAreas()) {
					Hibernate.initialize(devicArea.getDeviceClass());
				}
			}
		}
		return list;
	}
}