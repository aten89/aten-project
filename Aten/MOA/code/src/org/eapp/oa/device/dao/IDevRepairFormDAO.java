package org.eapp.oa.device.dao;

import java.util.List;

import org.eapp.oa.device.dto.DeviceSimpleQueryParameters;
import org.eapp.oa.device.hbean.DevRepairForm;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDevRepairFormDAO extends IBaseHibernateDAO {

	public DevRepairForm findById(java.lang.String id);

	public List<DevRepairForm> findByExample(DevRepairForm instance);

	public List<DevRepairForm> findByProperty(String propertyName, Object value);

	public List<DevRepairForm> findByRepairFormNo(Object repairFormNo);

	public List<DevRepairForm> findByAccountId(Object accountId);

	public List<DevRepairForm> findByGroupName(Object groupName);

	public List<DevRepairForm> findByStatus(Object status);

	public List<DevRepairForm> findByCreateTime(Object createTime);

	public List<DevRepairForm> findByBudgetMoney(Object budgetMoney);

	public List<DevRepairForm> findByRemark(Object remark);

	public List<DevRepairForm> findAll();
	
	/**
	 * 查找指定设备的维护记录列表
	 * @param id
	 * @return
	 */
	public List<DevRepairForm> findByDeviceID(String deviceID, Boolean createTimeOrder);
	
	/**
	 * 分页查询
	 * 
	 * @param qp
	 * @return
	 */
	public ListPage<DevRepairForm> queryDevRepairForm(DeviceSimpleQueryParameters qp);
	/**
	 * 根据年份获取流水号
	 * @param sequenceYear 年份
	 * @return
	 */
	public Integer getMaxSequenceNo(Integer sequenceYear);

}