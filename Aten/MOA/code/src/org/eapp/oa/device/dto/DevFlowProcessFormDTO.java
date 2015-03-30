package org.eapp.oa.device.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eapp.client.util.UsernameCache;

/**
 * 设备流程操作 DTO
 * @author aGrainOfSand
 *
 */
public class DevFlowProcessFormDTO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1144308966121367628L;

	private String id;
	
	private String operator;
	
	private String operatorGroupName;

	private Date operateDate;

	private Integer operateType;
	
	private String remark;

	private String formNO;
	
	/** 
	 * 关联的表单ID
	 */
	private String refFormID;

	public static final int OPTTYPE_USE = 0;
	public static final int OPTTYPE_ALLOT_INSIDE = 1;
	public static final int OPTTYPE_MAINT = 2;
	public static final int OPTTYPE_SCRAP = 3;
	public static final int OPTTYPE_SCRAP_DISPOSE = 4;
	public static final int OPTTYPE_STORAGE = 5;
	public static final int OPTTYPE_ALLOT_DEPT = 6;
	public static final int OPTTYPE_ALLOT_BORROW = 7;
	public static final int OPTTYPE_PURCHASE = 8;
	public static final int OPTTYPE_LEAVE_DISPOSE = 9;
	
	
	public static Map<Integer, String> optTypeMap = new HashMap<Integer, String>();
	static {
		optTypeMap.put(OPTTYPE_USE, "领用");
		optTypeMap.put(OPTTYPE_ALLOT_INSIDE, "内部调拨");
		optTypeMap.put(OPTTYPE_ALLOT_DEPT, "部门间调拨");
		optTypeMap.put(OPTTYPE_ALLOT_BORROW, "外借调拨");
		optTypeMap.put(OPTTYPE_STORAGE, "调拨入库");
		optTypeMap.put(OPTTYPE_MAINT, "维修");
		optTypeMap.put(OPTTYPE_SCRAP, "报废");
		optTypeMap.put(OPTTYPE_SCRAP_DISPOSE, "报废处理");
		optTypeMap.put(OPTTYPE_LEAVE_DISPOSE, "离职处理");
	}
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorDisplayName() {
		return UsernameCache.getDisplayName(operator);
	}
	
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public String getOperateTypeDisplayName() {
		return optTypeMap.get(operateType);
	}
	
	public void setOperatorType(Integer operatorType) {
		this.operateType = operatorType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRefFormID() {
		return refFormID;
	}

	public void setRefFormID(String refFormID) {
		this.refFormID = refFormID;
	}

	public String getOperatorGroupName() {
		return operatorGroupName;
	}

	public void setOperatorGroupName(String operatorGroupName) {
		this.operatorGroupName = operatorGroupName;
	}

	public String getFormNO() {
		return formNO;
	}

	public void setFormNO(String formNO) {
		this.formNO = formNO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DevFlowProcessFormDTO other = (DevFlowProcessFormDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(DevFlowProcessFormDTO o) {
		return this.getOperateDate().compareTo(o.getOperateDate());
	}
}
