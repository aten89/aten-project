package org.eapp.oa.device.dao;

import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IDeviceFlowDAO extends IBaseHibernateDAO  {
	/**
	 * 查询代办列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> findDealDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	
	/**
	 * 查询起草列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> findDraftDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	/**
	 * 查询跟踪列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> findTrackDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	/**
	 * 查询归档列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> findArchDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
}
