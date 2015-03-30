package org.eapp.oa.device.blo;

import java.util.List;

import org.eapp.oa.device.dto.DevFlowProcessFormDTO;
import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.util.hibernate.ListPage;

/**
 * 设备的操作流程 业务逻辑接口
 */
public interface IDevFlowBiz {
	
	/**
	 * 查询设备的操作流程记录列表
	 * @param deviceID
	 * @return
	 */
	public List<DevFlowProcessFormDTO> queryAllDevFlowList(String deviceID);
	
	/**
	 * 查询代办列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> queryDealDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	/**
	 * 查询起草列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> queryDraftDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	/**
	 * 查询跟踪列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> queryTrackDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	/**
	 * 查询归档列表
	 * @param qp
	 * @return
	 */
	public ListPage<DeviceFlowView> queryArchDeviceFlowPage(DeviceFlowQueryParameters qp,String userID);
	
	public void txDealApproveTaskByFlowInstanceId(String flowInstanceId,String backBuyFlag,Integer type,String taskInstanceId, String comment, String transitionName);
	
	public void txDealRejectTaskByFlowInstanceId(String flowInstanceId,String buyType,String taskInstanceId,String comment,String transitionName);
}
