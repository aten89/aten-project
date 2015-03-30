package org.eapp.oa.device.blo.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import org.eapp.oa.device.blo.IDeviceClassAssignBiz;
import org.eapp.oa.device.dao.IDeviceClassAssignDAO;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignArea;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

public class DeviceClassAssignBiz implements IDeviceClassAssignBiz {
	private IDeviceClassAssignDAO deviceClassAssignDAO;
	
	
	public void setDeviceClassAssignDAO(IDeviceClassAssignDAO deviceClassAssignDAO) {
		this.deviceClassAssignDAO = deviceClassAssignDAO;
	}

	@Override
	public List<DeviceClassAssignDetail> getBindingGroups(String id, Integer flag) {
		if (id == null) {
			return null;
		}
		return deviceClassAssignDAO.findBindById(id, DeviceClassAssignDetail.TYPE_GROUP, flag);
	}

	@Override
	public List<DeviceClassAssignDetail> getBindingPosts(String id, Integer flag) {
		if (id == null) {
			return null;
		}
		return deviceClassAssignDAO.findBindById(id, DeviceClassAssignDetail.TYPE_POST, flag);
	}

	@Override
	public List<DeviceClassAssignDetail> getBindingUsers(String id, Integer flag) {
		if (id == null) {
			return null;
		}
		return deviceClassAssignDAO.findBindById(id, DeviceClassAssignDetail.TYPE_USER, flag);
	}

	@Override
	public List<DeviceClassAssign> queryDeviceClassAssignList() {
		List<DeviceClassAssign> list= deviceClassAssignDAO.findDeviceClassAssignList();
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		Map<String,DataDictInfo> map = SysCodeDictLoader.getInstance().getDeviceType();
		for (DeviceClassAssign deviceClassAssign : list) {
			if (deviceClassAssign.getDeviceType() != null && map.containsKey(deviceClassAssign.getDeviceType())) {
				deviceClassAssign.setDeviceTypeName(map.get(deviceClassAssign.getDeviceType()).getDictName());
			}
			Hibernate.initialize(deviceClassAssign.getDeviceClassAssignAreas());
			if(deviceClassAssign.getDeviceClassAssignAreas()!=null){
//				boolean flag=true;
				int i=0;
				for (DeviceClassAssignArea classAssignArea : deviceClassAssign.getDeviceClassAssignAreas()) {
					Hibernate.initialize(classAssignArea.getDeviceClass());
					++i;
					DeviceClass dc = classAssignArea.getDeviceClass();
					if(dc!=null){
//						if(flag){
//							deviceClassAssign.setClassIds(classAssignArea.getDeviceClass().getId());
//							deviceClassAssign.setClassNames(classAssignArea.getDeviceClass().getName()+"<br/>");
//						}else{
//							deviceClassAssign.setClassIds(deviceClassAssign.getClassIds()+";"+classAssignArea.getDeviceClass().getId());
//							deviceClassAssign.setClassNames(deviceClassAssign.getClassNames()+classAssignArea.getDeviceClass().getName()+" <br/>");
//						}
//						flag = false;
						if(i%2==0){
							deviceClassAssign.setClassIds(deviceClassAssign.getClassIds()+";"+classAssignArea.getDeviceClass().getId());
							deviceClassAssign.setClassNames(deviceClassAssign.getClassNames()+"；"+classAssignArea.getDeviceClass().getName()+"；<br/>");
						}else{
							if(i==1){
								deviceClassAssign.setClassIds(classAssignArea.getDeviceClass().getId());
								deviceClassAssign.setClassNames(classAssignArea.getDeviceClass().getName());
							}else{
								deviceClassAssign.setClassIds(deviceClassAssign.getClassIds()+";"+classAssignArea.getDeviceClass().getId());
								deviceClassAssign.setClassNames(deviceClassAssign.getClassNames()+classAssignArea.getDeviceClass().getName());
							}
							
						}
						
						
					}
				}
			}
			if(areaMap != null) {
				DataDictInfo dict = areaMap.get(deviceClassAssign.getAreaCode());
				if(dict != null) {
					String dictKey = dict.getDictName();
					if(dictKey != null) {
						deviceClassAssign.setAreaName(dictKey);
					}
				}
			}
			
		}
		return list;
	}

	@Override
	public void txBindingGroups(String id, String[] groupIDs, Integer flag)
			throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		deviceClassAssignDAO.delBindById(id, DeviceClassAssignDetail.TYPE_GROUP, flag);
		
		String assignFlag=null;
		String[] assigns=null;
		DeviceClassAssign deviceClassAssign = deviceClassAssignDAO.findDeviceClassAssignById(id);
		if(deviceClassAssign == null){
			throw new OaException("区域授权配置不存在!");
		}
		//如果要绑定的机构为空，直接返回
		if(groupIDs == null || groupIDs.length == 0){
			List<DeviceClassAssignDetail> list= deviceClassAssignDAO.findDeviceClassAssignDetail(id, flag);
			if(list==null || list.isEmpty()){
				assignFlag=deviceClassAssign.getAssignEdValue();
				assigns=assignFlag.split(",");
				if(assignFlag.indexOf(String.valueOf(flag))!=-1){
					if(assigns!=null && assigns.length<2){
						deviceClassAssign.setAssignEdValue("");
					}else{
						if(flag==DeviceClassAssignDetail.ASSIGN_SELECT){
							deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER));
						}else{
							deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_SELECT));
						}
						
					}
				}
				
				deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);
			}
			return;
		}
		
		//保存绑定机构
		for (String groupID : groupIDs) {
			DeviceClassAssignDetail wa = new DeviceClassAssignDetail();
			wa.setType(DeviceClassAssignDetail.TYPE_GROUP);
			wa.setAssignClass(flag);
			wa.setAssignKey(groupID);
			wa.setDeviceClassAssign(deviceClassAssign);
			deviceClassAssignDAO.save(wa);
		}
		assignFlag=deviceClassAssign.getAssignEdValue();
		if(assignFlag!=null){
			assigns=assignFlag.split(",");
			if(assigns!=null && assigns.length>0){
				if(assigns.length!=2){
					if(!assigns[0].equals(String.valueOf(flag))){
						deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER)+","+String.valueOf(DeviceClassAssignDetail.ASSIGN_SELECT));
					}
				}
			}else{
				deviceClassAssign.setAssignEdValue(String.valueOf(flag));
			}
		}else{
			deviceClassAssign.setAssignEdValue(String.valueOf(flag));
		}
		deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);

	}

	@Override
	public void txBindingPosts(String id, String[] postIDs, Integer flag)
			throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		
		String assignFlag=null;
		String[] assigns=null;
		deviceClassAssignDAO.delBindById(id, DeviceClassAssignDetail.TYPE_POST, flag);
		DeviceClassAssign deviceClassAssign = deviceClassAssignDAO.findDeviceClassAssignById(id);
		if(deviceClassAssign == null){
			throw new OaException("区域授权配置不存在!");
		}
		//如果要绑定的职位为空，直接返回
		if(postIDs == null || postIDs.length == 0){
			List<DeviceClassAssignDetail> list= deviceClassAssignDAO.findDeviceClassAssignDetail(id, flag);
			if(list==null || list.isEmpty()){
				assignFlag=deviceClassAssign.getAssignEdValue();
				assigns=assignFlag.split(",");
				if(assignFlag.indexOf(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER))!=-1){
					if(assigns!=null && assigns.length<2){
						deviceClassAssign.setAssignEdValue("");
					}else{
						if(flag==DeviceClassAssignDetail.ASSIGN_SELECT){
							deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER));
						}else{
							deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_SELECT));
						}
					}
				}
				
				deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);
			}
			return;
		}
		
		//保存绑定职位
		for (String postID : postIDs) {
			DeviceClassAssignDetail wa = new DeviceClassAssignDetail();
			wa.setType(DeviceClassAssignDetail.TYPE_POST);
			wa.setAssignClass(flag);
			wa.setAssignKey(postID);
			wa.setDeviceClassAssign(deviceClassAssign);
			deviceClassAssignDAO.save(wa);
		}
		assignFlag=deviceClassAssign.getAssignEdValue();
		if(assignFlag!=null){
			assigns=assignFlag.split(",");
			if(assigns!=null && assigns.length>0){
				if(assigns.length!=2){
					if(!assigns[0].equals(String.valueOf(flag))){
						deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER)+","+String.valueOf(DeviceClassAssignDetail.ASSIGN_SELECT));
					}
				}
			}else{
				deviceClassAssign.setAssignEdValue(String.valueOf(flag));
			}
		}else{
			deviceClassAssign.setAssignEdValue(String.valueOf(flag));
		}
		
		deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);

	}

	@Override
	public void txBindingUsers(String id, String[] userIDs, Integer flag)
			throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		String assignFlag=null;
		String[] assigns=null;
		deviceClassAssignDAO.delBindById(id, DeviceClassAssignDetail.TYPE_USER, flag);
		DeviceClassAssign deviceClassAssign = deviceClassAssignDAO.findDeviceClassAssignById(id);
		if(deviceClassAssign == null){
			throw new OaException("区域授权配置不存在!");
		}
		//如果要绑定的用户为空，直接返回
		if(userIDs == null || userIDs.length == 0){
			List<DeviceClassAssignDetail> list= deviceClassAssignDAO.findDeviceClassAssignDetail(id, flag);
			if(list==null || list.isEmpty()){
				assignFlag=deviceClassAssign.getAssignEdValue();
				assigns=assignFlag.split(",");
				if(assignFlag.indexOf(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER))!=-1){
					if(assigns!=null && assigns.length<2){
						deviceClassAssign.setAssignEdValue("");
					}else{
						if(flag==DeviceClassAssignDetail.ASSIGN_SELECT){
							deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER));
						}else{
							deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_SELECT));
						}
					}
				}
				
				deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);
			}
			return;
		}
		
		//保存绑定用户
		for (String userId : userIDs) {
			DeviceClassAssignDetail wa = new DeviceClassAssignDetail();
			wa.setType(DeviceClassAssignDetail.TYPE_USER);
			wa.setAssignClass(flag);
			wa.setAssignKey(userId);
			wa.setDeviceClassAssign(deviceClassAssign);
			deviceClassAssignDAO.save(wa);
		}
		assignFlag=deviceClassAssign.getAssignEdValue();
		if(assignFlag!=null){
			assigns=assignFlag.split(",");
			if(assigns!=null && assigns.length>0){
				if(assigns.length!=2){
					if(assigns[0]!=String.valueOf(flag)){
						deviceClassAssign.setAssignEdValue(String.valueOf(DeviceClassAssignDetail.ASSIGN_MANAGER)+","+String.valueOf(DeviceClassAssignDetail.ASSIGN_SELECT));
					}
				}
			}else{
				deviceClassAssign.setAssignEdValue(String.valueOf(flag));
			}
		}else{
			deviceClassAssign.setAssignEdValue(String.valueOf(flag));
		}
		deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);
		
	}

	@Override
	public void txDeleteDeviceClassAssign(String id) throws OaException {
		if (id==null) {
			throw new IllegalArgumentException("非法参数！");
		}
		DeviceClassAssign classAssign = deviceClassAssignDAO.findDeviceClassAssignById(id);
		if(classAssign==null){
			throw new OaException("未找设备类型区域授权配置");
		}
		deviceClassAssignDAO.delete(classAssign);
	}

	@Override
	public DeviceClassAssign txSaveDeviceClassAssign(DeviceClassAssign deviceClassAssign) {
		if (deviceClassAssign==null) {
			throw new IllegalArgumentException("非法参数！");
		}
		deviceClassAssignDAO.saveOrUpdate(deviceClassAssign);
		return deviceClassAssign;
	}

	@Override
	public DeviceClassAssign txUpdateDeviceClassAssign(DeviceClassAssign deviceClassAssign) throws OaException {
		if (deviceClassAssign==null) {
			throw new IllegalArgumentException("非法参数！");
		}
		DeviceClassAssign classAssign = deviceClassAssignDAO.findDeviceClassAssignById(deviceClassAssign.getId());
		if(classAssign==null){
			throw new OaException("未找设备类型区域授权配置");
		}
		classAssign.getDeviceClassAssignAreas().clear();
		classAssign.setAreaCode(deviceClassAssign.getAreaCode());
		classAssign.setRemark(deviceClassAssign.getRemark());
		classAssign.getDeviceClassAssignAreas().addAll(deviceClassAssign.getDeviceClassAssignAreas());
		classAssign.setDeviceType(deviceClassAssign.getDeviceType());
		deviceClassAssignDAO.saveOrUpdate(classAssign);
		return classAssign;
	}

	@Override
	public void txBatchDeltete(String[] ids) throws OaException {
		if(ids==null || ids.length==0){
			throw new IllegalArgumentException("非法参数:ID不能为空");
		}
		for (String id : ids) {
			DeviceClassAssign classAssign=deviceClassAssignDAO.findDeviceClassAssignById(id);
			if(classAssign==null){
				throw new OaException("未找设备类型区域授权配置");
			}
			deviceClassAssignDAO.delete(classAssign);
		}
		
	}

}
