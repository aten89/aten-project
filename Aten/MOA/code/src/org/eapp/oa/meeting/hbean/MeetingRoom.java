package org.eapp.oa.meeting.hbean;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * MeetingRoom entity.Description:会议室资料
 * 
 * @author sds
 */
@SuppressWarnings("unchecked")
public class MeetingRoom implements java.io.Serializable {

	private static final long serialVersionUID = 474912373525522123L;

	// Fields
    
	// ID_,VARCHAR2(36),不能为空     --主键ID
	private String id;
	//NAME_,VARCHAR2(128)          --名称
	private String name;
	//SEATNUM_,INTEGER             --座位数
	private Integer seatNum;
	//ENVIRONMENT_,VARCHAR2(128)   --环境
	private String environment;
	//POWERJACKNUM_,INTEGER        --电源插口数
	private Integer powerNum;
	//CABLENUM_,INTEGER            --网线数
	private Integer cableNum;
	//LINENUM_,INTEGER             --电话线数
	private Integer lineNum;
	//PHONENUMBER_,VARCHAR2(128)   --电话号码
	private String phoneNumber;
	//STATUS_,SMALLINT             --是否可被预订
	private Boolean status;
	//REMARK_,VARCHAR2(2048)       --备注
	private String remark;
	//DisplayOrder_,INTEGER        --排序
	private Integer displayOrder;
	//AreaCode_,VARCHAR2(128)	   --所属地区
	private String areaCode;	   
	private Set<MeetingInfo> meetingInfos = new HashSet<MeetingInfo>(0);
	// Constructors

	/** default constructor */
	public MeetingRoom() {
	}

	/** full constructor */
	public MeetingRoom(String name, Integer seatNum, String environment,
			Integer powerNum, Integer cableNum, Integer lineNum, String phoneNumber,
			Boolean status, String remark, Set<MeetingInfo> meetingInfos,Integer displayOrder) {
		this.name = name;
		this.seatNum = seatNum;
		this.environment = environment;
		this.powerNum = powerNum;
		this.cableNum = cableNum;
		this.lineNum = lineNum;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.remark = remark;
		this.meetingInfos = meetingInfos;
		this.displayOrder = displayOrder;
	}

	// Property accessors

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Integer getPowerNum() {
		return powerNum;
	}

	public void setPowerNum(Integer powerNum) {
		this.powerNum = powerNum;
	}

	public Integer getCableNum() {
		return cableNum;
	}

	public void setCableNum(Integer cableNum) {
		this.cableNum = cableNum;
	}

	public Integer getLineNum() {
		return lineNum;
	}

	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<MeetingInfo> getMeetingInfos() {
		return meetingInfos;
	}

	public void setMeetingInfos(Set<MeetingInfo> meetingInfos) {
		this.meetingInfos = meetingInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MeetingInfo other = (MeetingInfo) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.append("[");
		sb.append("id=");
		sb.append(getId());
		sb.append(",");
		sb.append("name=");
		sb.append(getName());
		sb.append(",");
		sb.append("areaName=");
		sb.append(getAreaName());
		sb.append(",");
		sb.append("seatNum=");
		sb.append(getSeatNum());
		sb.append(",");
		sb.append("environment=");
		sb.append(getEnvironment());
		sb.append(",");
		sb.append("powerNum=");
		sb.append(getPowerNum());
		sb.append(",");
		sb.append("cableNum=");
		sb.append(getCableNum());
		sb.append(",");
		sb.append("lineNum=");
		sb.append(getLineNum());
		sb.append(",");
		sb.append("phoneNumber=");
		sb.append(getPhoneNumber());
		sb.append(",");
		sb.append("status=");
		sb.append(getStatus());
		sb.append(",");
		sb.append("remark=");
		sb.append(getRemark());
		sb.append(",");
		sb.append("meetingInfos=");
		sb.append(getMeetingInfos());
		sb.append("]");

		return sb.toString();
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
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
}