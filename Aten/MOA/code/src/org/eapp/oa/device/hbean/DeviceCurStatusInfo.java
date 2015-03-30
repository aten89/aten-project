package org.eapp.oa.device.hbean;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

// default package

/**
 * DeviceCurStatusInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DeviceCurStatusInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2107596817569891854L;

	// Fields

	public static final int STATUS_DELETE = 0; //已删除
	public static final int STATUS_NORMAL = 1; //正常
	public static final int STATUS_MAINTAIN = 2; //维修
	public static final int STATUS_UNAPPROVE = 3;//入库未验收
	public static final int STATUS_LEAVEBUY = 4;//离职回购
	public static final int STATUS_LEAVEDONATE  = 5;//离职赠送
	public static final int STATUS_TAKE = 6;//回购
	public static final int STATUS_SCRAP_UNDISPOSE = 7;//报废未处理
	public static final int STATUS_SCRAP_DISPOSEED = 8;//报废已处理
	public static final int STATUS_BORROW = 9;//外借
	public static final int STATUS_LOST = 14;//丢失
	public static final int STATUS_LEAVE_TAKE = 10;//离职拿走
	
	public static final int APPROVE_TYPE_PURCH  = 0;//申购审批中
	public static final int APPROVE_TYPE_USE  = 1;//领用审批中
	public static final int APPROVE_TYPE_ALLOT = 2;//调拨审批中
	public static final int APPROVE_TYPE_SCRAP = 3;//报废审批中
	public static final int APPROVE_TYPE_LEAVE = 4;//离职审批中
	
	public static final int ISUSING_NO = 0;//未领用
	public static final int ISUSING_YES = 1;//已领用
	public static final int ISUSING_UNDISPOSE = 2;//报废未处理
	public static final int ISUSING_DISPOSEED = 3;//报废已处理
	
	public static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	public static Map<Integer, String> approveTypeMap = new HashMap<Integer, String>();
	static{		
		statusMap.put(STATUS_DELETE, "已删除");
		statusMap.put(STATUS_NORMAL, "正常");
		statusMap.put(STATUS_MAINTAIN, "维修");
		statusMap.put(STATUS_UNAPPROVE, "入库未验收");
		statusMap.put(STATUS_LEAVEBUY, "离职回购");
		statusMap.put(STATUS_LEAVEDONATE, "离职赠送");
		statusMap.put(STATUS_TAKE, "回购");
		statusMap.put(STATUS_SCRAP_UNDISPOSE, "待变卖");
		statusMap.put(STATUS_SCRAP_DISPOSEED, "已变卖");
		statusMap.put(STATUS_BORROW, "外借");
		statusMap.put(STATUS_LOST, "丢失");
		statusMap.put(STATUS_LEAVE_TAKE, "拿走");
	}	
	
	static{		
		approveTypeMap.put(APPROVE_TYPE_USE, "领用审批中");
		approveTypeMap.put(APPROVE_TYPE_ALLOT, "调拨审批中");
		approveTypeMap.put(APPROVE_TYPE_SCRAP, "报废审批中");
		approveTypeMap.put(APPROVE_TYPE_LEAVE, "离职审批中");
	}	
	
	public static Map<Integer, String> statusSelMap = new HashMap<Integer, String>();
	static{		
		statusSelMap.put(STATUS_NORMAL, "正常");
		statusSelMap.put(STATUS_LEAVEBUY, "离职回购");
		statusSelMap.put(STATUS_LEAVEDONATE, "离职赠送");
		statusSelMap.put(STATUS_TAKE, "回购");
		statusSelMap.put(STATUS_SCRAP_UNDISPOSE, "待变卖");
		statusSelMap.put(STATUS_SCRAP_DISPOSEED, "已变卖");
		statusSelMap.put(STATUS_BORROW, "外借");
		statusSelMap.put(STATUS_LOST, "丢失");
		statusSelMap.put(STATUS_LEAVE_TAKE, "拿走");
		
	}
	
	public static Map<Integer, String> dealMap = new HashMap<Integer, String>();
	static{		
		dealMap.put(STATUS_TAKE, "报废已处理");
		dealMap.put(STATUS_SCRAP_UNDISPOSE, "报废未处理");
		dealMap.put(STATUS_BORROW, "报废未处理");
		dealMap.put(STATUS_SCRAP_DISPOSEED, "报废已处理");
		dealMap.put(STATUS_LEAVEBUY, "报废已处理");
		dealMap.put(STATUS_LEAVEDONATE, "报废已处理");
		dealMap.put(STATUS_LOST, "报废已处理");
		
	}	
	
	private String id;
	private Device device;
	private String owner;
	private Integer deviceCurStatus;
	private Date statusUptDate;
	private String groupName;
	private String areaCode; //工作所在地
	private String purpose;
	private Integer approveType;
	private String formID;
	
	// Constructors

	/** default constructor */
	public DeviceCurStatusInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getDeviceCurStatus() {
		return deviceCurStatus;
	}

	public void setDeviceCurStatus(Integer deviceCurStatus) {
		this.deviceCurStatus = deviceCurStatus;
	}

	public String getDeviceCurStatusStr() {
		return statusMap.get(deviceCurStatus);
	}
	
	public String getOwnerName() {
		return UsernameCache.getDisplayName(this.owner);
	}

	public Date getStatusUptDate() {
		return statusUptDate;
	}

	public void setStatusUptDate(Date statusUptDate) {
		this.statusUptDate = statusUptDate;
	}

	public Integer getApproveType() {
		return approveType;
	}

	public void setApproveType(Integer approveType) {
		this.approveType = approveType;
	}

	public String getApproveTypeDisplayName() {
		return approveTypeMap.get(approveType);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DeviceCurStatusInfo other = (DeviceCurStatusInfo) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null && this.areaCode != null) {
			DataDictInfo dict = areaMap.get(this.areaCode);
			if(dict != null) {
				String dictKey = dict.getDictName();
				if(dictKey != null) {
					return dictKey;
				}
			}
		}
		return "";
	}

	public String getPurpose() {
		return purpose;
	}
//	public String getPurposeName() {
//		Map<String ,DataDictInfo> devUseTypeMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
//		if(devUseTypeMap != null) {
//			DataDictInfo dict = devUseTypeMap.get(this.purpose);
//			if(dict != null) {
//				String dictKey = dict.getDictName();
//				if(dictKey != null) {
//					return dictKey;
//				}
//			}
//		}
//		return "";
//	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getFormID() {
		return formID;
	}

	public void setFormID(String formID) {
		this.formID = formID;
	}

}