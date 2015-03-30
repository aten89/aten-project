package org.eapp.oa.device.blo.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.dao.IDeviceAreaConfigDAO;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.flow.blo.IFlowConfigBiz;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.exception.OaException;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DeviceAreaConfigBiz implements IDeviceAreaConfigBiz {

	private IDeviceAreaConfigDAO deviceAreaConfigDAO;
	
	private IFlowConfigBiz flowConfigBiz;
	
	@Override
	public ListPage<AreaDeviceCfg> queryAllAreaDeviceCfgPage(QueryParameters qp) {
		ListPage<AreaDeviceCfg> page = deviceAreaConfigDAO.queryAllAreaDeviceCfgPage(qp);
		if(page.getDataList() == null || page.getDataList().isEmpty()) {
			return page;
		}
		for (AreaDeviceCfg areaDeviceCfg : page.getDataList()) {
			Hibernate.initialize(areaDeviceCfg.getDeviceClass());
//			Hibernate.initialize(areaDeviceCfg.getDeviceAcptCountCfgs());
			FlowConfig fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getAllocateFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
			if(fcs!=null){
				areaDeviceCfg.setAllocateFlowName(fcs.getFlowName());
			}
			fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getDiscardFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
			if(fcs!=null){
				areaDeviceCfg.setDiscardFlowName(fcs.getFlowName());
			}
			fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getUseApplyFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
			if(fcs!=null){
				areaDeviceCfg.setUseApplyFlowName(fcs.getFlowName());
			}
			fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getDimissionFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
			if(fcs!=null){
				areaDeviceCfg.setDimissionFlowName(fcs.getFlowName());
			}
		}
		return page;
	}
	
	@Override
	public AreaDeviceCfg txAddAreaDeviceCfg(AreaDeviceCfg areaDeviceCfg)
			throws OaException {
		this.isDeviceNoRepeat(areaDeviceCfg.getAreaCode(), areaDeviceCfg.getDeviceClass().getId());
		deviceAreaConfigDAO.save(areaDeviceCfg);
		return areaDeviceCfg;
	}
	@Override
	public AreaDeviceCfg txDelAreaDeviceCfg(String id) throws OaException {
		if (null == id) {
			throw new IllegalArgumentException("非法参数:区域设备配置ID不能为空！");
		}
		AreaDeviceCfg deviceCfg = deviceAreaConfigDAO.findById(id);
		if (deviceCfg == null) {
			throw new OaException("该区域设备类型配置不存在!");
		}
		deviceAreaConfigDAO.delete(deviceCfg);
		return deviceCfg;
	}
	@Override
	public AreaDeviceCfg txModifyAreaDeviceCfg(AreaDeviceCfg areaDeviceCfg)
			throws OaException {
		
		AreaDeviceCfg deviceCfg = deviceAreaConfigDAO.findById(areaDeviceCfg.getId());
		if (deviceCfg == null) {
			throw new OaException("该区域设备类型配置不存在!");
		}
		if(!(deviceCfg.getAreaCode()!=null && areaDeviceCfg.getAreaCode().equals(deviceCfg.getAreaCode()) && deviceCfg.getDeviceClass()!=null && deviceCfg.getDeviceClass().getId().equals(areaDeviceCfg.getDeviceClass().getId()))){
			this.isDeviceNoRepeat(areaDeviceCfg.getAreaCode(), areaDeviceCfg.getDeviceClass().getId());
		}
//		Hibernate.initialize(deviceCfg.getDeviceAcptCountCfgs());
//		deviceCfg.getDeviceAcptCountCfgs().clear();
		deviceCfg.setAreaCode(areaDeviceCfg.getAreaCode());
		deviceCfg.setDeviceClass(areaDeviceCfg.getDeviceClass());
		deviceCfg.setOrderPrefix(areaDeviceCfg.getOrderPrefix());
		deviceCfg.setRemark(areaDeviceCfg.getRemark());
		deviceCfg.setSeqNum(areaDeviceCfg.getSeqNum());
		deviceCfg.setSeparator(areaDeviceCfg.getSeparator());
		deviceCfg.setAllocateFlowKey(areaDeviceCfg.getAllocateFlowKey());
		deviceCfg.setUseApplyFlowKey(areaDeviceCfg.getUseApplyFlowKey());
		deviceCfg.setDiscardFlowKey(areaDeviceCfg.getDiscardFlowKey());
		deviceCfg.setDimissionFlowKey(areaDeviceCfg.getDimissionFlowKey());
//		deviceCfg.getDeviceAcptCountCfgs().addAll(areaDeviceCfg.getDeviceAcptCountCfgs());
		deviceCfg.setMainDevFlag(areaDeviceCfg.getMainDevFlag());
//		deviceCfg.setManyTimesFlag(areaDeviceCfg.getManyTimesFlag());
		deviceAreaConfigDAO.saveOrUpdate(deviceCfg);
		return deviceCfg;
	}
	/**
	 * 验证区域设备类别配置是否重复
	 * 
	 * @param name
	 * @throws OaException
	 */
	private void isDeviceNoRepeat(String areaCode,String classID) throws OaException {
		List<AreaDeviceCfg>  dreaDeviceCfg= deviceAreaConfigDAO.findByAreaCodeAndClassId(areaCode,classID);
		if (dreaDeviceCfg != null && dreaDeviceCfg.size() > 0) {
			throw new OaException("该地区设备类型不能重复!");
		}
	}
	
	@Override
	public AreaDeviceCfg getAreaDeviceCfg(String Id) throws OaException {
		AreaDeviceCfg areaDeviceCfg = deviceAreaConfigDAO.findById(Id);
		if (areaDeviceCfg == null) {
			throw new OaException("该区域设备类型配置不存在!");
		}
		Hibernate.initialize(areaDeviceCfg.getDeviceClass());
//		Map<String ,DataDictInfo> devUseTypeMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
		Map<String,DataDictInfo> map = SysCodeDictLoader.getInstance().getDeviceType();
		if (areaDeviceCfg.getDeviceClass()!=null && areaDeviceCfg.getDeviceClass().getDeviceType() != null && map.containsKey(areaDeviceCfg.getDeviceClass().getDeviceType())) {
			areaDeviceCfg.setDeviceTypeName(map.get(areaDeviceCfg.getDeviceClass().getDeviceType()).getDictName());
		}
//		Hibernate.initialize(areaDeviceCfg.getDeviceAcptCountCfgs());
//		Set<DeviceAcptCountCfg> deviceAcptCountCfgs = areaDeviceCfg.getDeviceAcptCountCfgs();
//		if(deviceAcptCountCfgs!=null && deviceAcptCountCfgs.size()>0){
//			String devPuerposeStr="";
//			String devPuerposeName="";
//			for (DeviceAcptCountCfg deviceAcptCountCfg : deviceAcptCountCfgs) {
//				if(devUseTypeMap.containsKey(deviceAcptCountCfg.getDevPurpose())){
//					devPuerposeName+=devUseTypeMap.get(deviceAcptCountCfg.getDevPurpose()).getDictName();
//					devPuerposeStr+=deviceAcptCountCfg.getDevPurpose()+";";
//					if(deviceAcptCountCfg.getManyTimesFlag()){
//						devPuerposeStr+=deviceAcptCountCfg.getDevPurpose()+"true;";
//						devPuerposeName+="（<span class=\"cRed\">可领用多次</span>）";
//					}else{
//						devPuerposeStr+=deviceAcptCountCfg.getDevPurpose()+"false;";
//					}
//					devPuerposeName+="<br/>";
//				}
//			}
//			areaDeviceCfg.setDevPerposeName(devPuerposeName);
//			areaDeviceCfg.setDevPerposes(devPuerposeStr);
//		}

		FlowConfig fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getAllocateFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
		if(fcs!=null){
			areaDeviceCfg.setAllocateFlowName(fcs.getFlowName());
		}
		fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getDiscardFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
		if(fcs!=null){
			areaDeviceCfg.setDiscardFlowName(fcs.getFlowName());
		}
		fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getUseApplyFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
		if(fcs!=null){
			areaDeviceCfg.setUseApplyFlowName(fcs.getFlowName());
		}
		fcs = flowConfigBiz.getFlowByFlowKey(areaDeviceCfg.getDimissionFlowKey(), FlowConfig.FLOW_FLAG_PUBLISHED);
		if(fcs!=null){
			areaDeviceCfg.setDimissionFlowName(fcs.getFlowName());
		}
		return areaDeviceCfg;
	}
	
	@Override
	public AreaDeviceCfg getAreaDeviceCfgByClassId(String areaCode,String classID)
			throws OaException {
		if (areaCode == null || "".equals(areaCode) ||
				classID == null || "".equals(classID)) {
			throw new IllegalArgumentException("参数错误");
		}
		List<AreaDeviceCfg> dreaDeviceCfg = deviceAreaConfigDAO.findByAreaCodeAndClassId(areaCode,classID);
		if(dreaDeviceCfg!=null && !dreaDeviceCfg.isEmpty()){
			AreaDeviceCfg deviceCfg = dreaDeviceCfg.get(0);
			Hibernate.initialize(deviceCfg.getDeviceClass());
//			Hibernate.initialize(deviceCfg.getDeviceAcptCountCfgs());
			return deviceCfg;
		}
		return null;
		
	}
	
	@Override
	public List<AreaDeviceCfg> queryAreaDeviceByAreaCode(String areaCode,String deviceType) {
		List<AreaDeviceCfg>  list= deviceAreaConfigDAO.findAreaDeviceByAreaCode(areaCode,deviceType);
		for (AreaDeviceCfg areaDeviceCfg : list) {
			Hibernate.initialize(areaDeviceCfg.getDeviceClass());
		}
		return list;
	}
	
	@Override
	public List<AreaDeviceCfg> queryAllAreaDevices() {
		List<AreaDeviceCfg> list = deviceAreaConfigDAO.findAll();
		return list;
	}
	
	public List<AreaDeviceCfg> getMainDevAreaDeviceCfgs() {
		List<AreaDeviceCfg> list = deviceAreaConfigDAO.findMainDevAreaDeviceCfgs();
		return list;
	}
	
	public IDeviceAreaConfigDAO getDeviceAreaConfigDAO() {
		return deviceAreaConfigDAO;
	}
	public void setDeviceAreaConfigDAO(IDeviceAreaConfigDAO deviceAreaConfigDAO) {
		this.deviceAreaConfigDAO = deviceAreaConfigDAO;
	}
	public IFlowConfigBiz getFlowConfigBiz() {
		return flowConfigBiz;
	}
	public void setFlowConfigBiz(IFlowConfigBiz flowConfigBiz) {
		this.flowConfigBiz = flowConfigBiz;
	}
	
//	public List<DeviceAcptCountCfg> getDeviceAcptCountCfgByClassId(
//			String areaCode, String classID, String purpose) throws OaException {
//		
//		return deviceAreaConfigDAO.getDeviceAcptCountCfgByClassId(areaCode, classID, purpose);
//	}
	
//	public boolean getManyTimeFlagByPurpose(String areaCode,String classID,String purpose) throws OaException{
//		List<DeviceAcptCountCfg> list = getDeviceAcptCountCfgByClassId(areaCode, classID, purpose);
//		if(!list.isEmpty()){
//			DeviceAcptCountCfg d = list.get(0);
//			return d.getManyTimesFlag();
//		}
//		return true;
//	}
	
	
}
