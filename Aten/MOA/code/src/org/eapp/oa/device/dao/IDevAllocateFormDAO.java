package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.dto.DevAllocateQueryParameters;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevAllocateFormDAO extends IBaseHibernateDAO {

	public DevAllocateForm findById(java.lang.String id);

	public List<DevAllocateForm> findByExample(DevAllocateForm instance);

	public List<DevAllocateForm> findByProperty(String propertyName,
			Object value);

	public List<DevAllocateForm> findByMoveType(Object moveType);

	public List<DevAllocateForm> findByOutGroupName(Object outGroupName);

	public List<DevAllocateForm> findByOutAccountId(Object outAccountId);

	public List<DevAllocateForm> findByInGroupName(Object inGroupName);

	public List<DevAllocateForm> findByInAccountId(Object inAccountId);

	public List<DevAllocateForm> findByMoveDate(Object moveDate);

	public List<DevAllocateForm> findByFlowInstanceId(Object flowInstanceId);

	public List<DevAllocateForm> findByPassed(Object passed);

	public List<DevAllocateForm> findByArchiveDate(Object archiveDate);

	public List<DevAllocateForm> findByMoveReason(Object reason);

	public List<DevAllocateForm> findAll();
	
	/**
	 * 查询调拨单
	 * @param userAccountId
	 * @param formStatus
	 * @return
	 */
	public List<DevAllocateForm> findAllocateForm(String userAccountId, int formStatus);
	
	/**
	 * 待办列表
	 * @param userId
	 * @return
	 */
	public List<DevAllocateForm> queryDealingAllocateForm(String userId);
	
	/**
	 * 跟踪列表
	 * @param rqp
	 * @param userAccountId
	 * @return
	 */
	public ListPage<DevAllocateForm> queryTrackAllocateForm(DevAllocateQueryParameters rqp, String userAccountId) ;
	
	/**
	 * 归档列表
	 * @param rqp
	 * @param userAccountId
	 * @return
	 */
	public ListPage<DevAllocateForm> queryArchAllocateForm(DevAllocateQueryParameters rqp,
			String userAccountId);
	
	/**
	 * 查询列表
	 * @param rqp
	 * @return
	 */
	public ListPage<DevAllocateForm> queryAllocateForm(DevAllocateQueryParameters rqp) ;
	
	/**
	 * 根据年份获取流水号
	 * @param sequenceYear 年份
	 * @return
	 */
	public Integer getMaxSequenceNo(Integer sequenceYear);

	/**
	 * 查找指定设备的已归档调拨记录列表
	 * @param deviceID
	 * @return
	 */
	public List<DevAllocateList> queryArchDevAllotListByDeviceID(String deviceID, Boolean archiveDateOrder);
	
	/**
	 * 查找指定设备的正在处理中的处理单
	 * @param deviceID
	 * @return
	 */
	public List<DevAllocateForm> queryDealDevAllocateFormByDeviceID(String deviceID);
	
	public List<DevAllocateList> getDeviceDealDevAllocateByPurpose(String deviceType, String areaCode, String deviceClass,String userId);
	
	public List<DevAllocateForm> getDevAllocateFormDealDevAllocateByPurpose(String deviceType, String areaCode, String deviceClass, String userId);

}