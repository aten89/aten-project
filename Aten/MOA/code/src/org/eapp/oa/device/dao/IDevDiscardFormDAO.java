package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.dto.DevDiscardQueryParameters;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.util.hibernate.ListPage;


import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevDiscardFormDAO extends IBaseHibernateDAO {

	public DevDiscardForm findById(java.lang.String id);

	public List<DevDiscardForm> findAll();
	
	public List<DevDiscardForm> findDraftDiscardForm(String draftsman, int formStatus);
	
	public List<DevDiscardForm> queryDealDiscardForm(String userId);
	
	public ListPage<DevDiscardForm> queryTrackDiscardForm(DevDiscardQueryParameters rqp, String userAccountId) ;
	
	public ListPage<DevDiscardForm> queryArchDiscardForm(DevDiscardQueryParameters rqp, String userAccountId);
	
	public ListPage<DevDiscardForm> queryAllArchedDiscardForm(DevDiscardQueryParameters rqp);
	
	/**
	 * 根据年份获取流水号
	 * @param sequenceYear 年份
	 * @return
	 */
	public Integer getMaxSequenceNo(Integer sequenceYear, Integer formType);

	/**
	 * 查询指定设备的已归档的报废列表
	 * @param deviceID
	 * @return
	 */
	public List<DiscardDevList> queryArchDevScrapListByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 获取指定设备的已归档的设备报废单
	 * @param deviceID
	 * @return
	 */
	public List<DevDiscardForm> queryArchDevDiscardFormDeviceID(String deviceID);
	
	/**
	 * 获取指定设备报废清单
	 * @param deviceID
	 * @return
	 */
	public DiscardDevList findDiscardDevListByID(String ID);
	
	public List<DevDiscardForm> queryDealDevDiscardFormByDeviceID(String deviceID,Integer formType);
}