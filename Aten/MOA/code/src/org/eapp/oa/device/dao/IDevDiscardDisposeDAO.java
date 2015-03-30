package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.hbean.DevDiscardDealForm;
import org.eapp.oa.device.hbean.DiscardDealDevList;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevDiscardDisposeDAO extends IBaseHibernateDAO {

	public DevDiscardDealForm findById(java.lang.String id);
	/**
	 * 根据年份获取流水号
	 * @param sequenceYear 年份
	 * @return
	 */
	public Integer getMaxSequenceNo(Integer sequenceYear);
	
	/**
	 * 查找指定设备的报废处理单列表
	 * @param deviceID
	 * @param regTimeOrder
	 * @return
	 */
	public List<DiscardDealDevList> queryDevScrapDisposeListByDeviceID(String deviceID, Boolean regTimeOrder);
	
	/**
	 * 根据设备id查询报废处理单
	 * @param deviceID
	 * @return
	 * @throws OaException
	 */
	public List<DevDiscardDealForm> queryDevDiscardDealFormByDeviceID(String deviceID);
}