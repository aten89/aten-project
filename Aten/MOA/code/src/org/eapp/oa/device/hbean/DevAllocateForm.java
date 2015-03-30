package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * DevAllocateForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevAllocateForm extends DevFlowApplyProcess implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -509409488739721948L;
	
	public static final int STATUS_UNPUBLISH = 0;//未发布
	public static final int STATUS_APPROVAL = 1;//审批中
	public static final int STATUS_PUBLISH = 2;//已发布
	public static final int STATUS_CANCELLATION = 3;//作废
	
	public static String MOVE_TYPE_STORAGE = "IN_STORAGE";
	public static String MOVE_TYPE_DEPT = "ALLOT_DEPT";
	public static String MOVE_TYPE_INSIDE = "ALLOT_INSIDE";
	public static String MOVE_TYPE_BORROW = "ALLOT_BORROW";
	// Fields

	private String id;
	private DevValidateForm devValidateForm;
	private String moveType;
	private String inGroupName;
	private String inAccountID;
	private Date moveDate;
	private String reason;
	private Set<DevAllocateList> devAllocateLists = new HashSet<DevAllocateList>(0);
	private String refDevUseFormID;//关联的设备领用单ID
	private transient DevAllocateList devAllocateDevice;
	private transient String areaName;
	
	// Constructors

	/** default constructor */
	public DevAllocateForm() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevValidateForm getDevValidateForm() {
		return devValidateForm;
	}

	public void setDevValidateForm(DevValidateForm devValidateForm) {
		this.devValidateForm = devValidateForm;
	}

	public String getMoveType() {
		return moveType;
	}

	public void setMoveType(String moveType) {
		this.moveType = moveType;
	}

	public String getMoveTypeDisplayName() {
		if (this.moveType != null && !"".equals(this.moveType)) {
			Map<String ,DataDictInfo> buyTypeMap = SysCodeDictLoader.getInstance().getAllotTypeMap();
			if(buyTypeMap != null) {
				DataDictInfo dict = buyTypeMap.get(this.moveType);
				if(dict != null) {
					String dictKey = dict.getDictName();
					if(dictKey != null) {
						return dictKey;
					}
				}
			}
		}
		return "";
	}
	
	public String getInGroupName() {
		return inGroupName;
	}

	public void setInGroupName(String inGroupName) {
		this.inGroupName = inGroupName;
	}

	public String getInAccountID() {
		return inAccountID;
	}

	public void setInAccountID(String inAccountID) {
		this.inAccountID = inAccountID;
	}
	public String getInApplicantDisplayName() {
		return UsernameCache.getDisplayName(inAccountID);
	}
	public Date getMoveDate() {
		return moveDate;
	}

	public void setMoveDate(Date moveDate) {
		this.moveDate = moveDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Set<DevAllocateList> getDevAllocateLists() {
		return devAllocateLists;
	}

	public void setDevAllocateLists(Set<DevAllocateList> devAllocateLists) {
		this.devAllocateLists = devAllocateLists;
	}

	public DevAllocateList getDevAllocateDevice() {
		return devAllocateDevice;
	}

	public void setDevAllocateDevice(DevAllocateList devAllocateDevice) {
		this.devAllocateDevice = devAllocateDevice;
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
		final DevAllocateForm other = (DevAllocateForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getRefDevUseFormID() {
		return refDevUseFormID;
	}

	public void setRefDevUseFormID(String refDevUseFormID) {
		this.refDevUseFormID = refDevUseFormID;
	}

}