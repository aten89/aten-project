package org.eapp.oa.device.blo.impl;

import java.util.Calendar;
import java.util.List;

import org.eapp.oa.device.blo.IDevDiscardDisposeBiz;
import org.eapp.oa.device.dao.IDevDiscardDisposeDAO;
import org.eapp.oa.device.hbean.DevDiscardDealForm;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.device.hbean.DiscardDealDevList;
import org.hibernate.Hibernate;


import org.eapp.oa.system.exception.OaException;

public class DevDiscardDisposeBiz implements IDevDiscardDisposeBiz {

	private IDevDiscardDisposeDAO devDiscardDisposeDAO;

	@Override
	public DevDiscardDealForm txSaveForm(DevDiscardDealForm form)
			throws OaException {
		if (form == null) {
			return null;
		}
		//设置编号
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int no = devDiscardDisposeDAO.getMaxSequenceNo(year);
		form.setFormNO(++no);//编号序列号
		form.setSequenceYear(year);//编号年份
		
		if (form.getDiscardDealDevLists() != null && form.getDiscardDealDevLists().size() > 0) {
			for (DiscardDealDevList discardDealDevList : form.getDiscardDealDevLists()) {
				if (discardDealDevList != null && discardDealDevList.getDevice() != null) {
					DeviceCurStatusInfo statusInfo = discardDealDevList.getDevice().getDeviceCurStatusInfo();
					statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_SCRAP_DISPOSEED);
					devDiscardDisposeDAO.update(statusInfo);
				}
			}
		}
		
		devDiscardDisposeDAO.save(form);
		return form;
	}
	
	@Override
	public List<DevDiscardDealForm> queryDevDiscardDealFormByDeviceID(String deviceID) {
		List<DevDiscardDealForm> list = devDiscardDisposeDAO.queryDevDiscardDealFormByDeviceID(deviceID);
		if(!list.isEmpty()){
			for (DevDiscardDealForm devDiscardDealForm : list) {
				Hibernate.initialize(devDiscardDealForm.getDiscardDealDevLists());
				if(!devDiscardDealForm.getDiscardDealDevLists().isEmpty()){
					for (DiscardDealDevList dealDevList : devDiscardDealForm.getDiscardDealDevLists()) {
						Hibernate.initialize(dealDevList.getDevice());
					}
				}
			}
		}
		
		return list;
	}


	public void setDevDiscardDisposeDAO(IDevDiscardDisposeDAO devDiscardDisposeDAO) {
		this.devDiscardDisposeDAO = devDiscardDisposeDAO;
	}

	@Override
	public DevDiscardDealForm getById(String id) {
		DevDiscardDealForm form = devDiscardDisposeDAO.findById(id);
		if (form != null && form.getDiscardDealDevLists() != null && form.getDiscardDealDevLists().size() > 0) {
			for (DiscardDealDevList list : form.getDiscardDealDevLists()) {
				if (list != null) {
					Hibernate.initialize(list.getDevice());
				}
			}
		}
		return form;
	}

}
