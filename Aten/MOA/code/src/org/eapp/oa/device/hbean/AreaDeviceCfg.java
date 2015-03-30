package org.eapp.oa.device.hbean;

import java.util.HashMap;
import java.util.Map;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

// default package

/**
 * DeviceNo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AreaDeviceCfg implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8327773346628446976L;

	// Fields
	public static final int FLOW_TYPE_DEVPURCHASE = 0;
	public static final int FLOW_TYPE_DEVUSE = 1;
	public static final int FLOW_TYPE_DEVALLOT = 2;
	public static final int FLOW_TYPE_DEVSCRAP = 3;
	public static final int FLOW_TYPE_DEVDIMISSION = 4;
	public static Map<Integer, String> flowMap = new HashMap<Integer, String>();
	static {
		flowMap.put(FLOW_TYPE_DEVPURCHASE, "申购流程");
		flowMap.put(FLOW_TYPE_DEVUSE, "领用流程");
		flowMap.put(FLOW_TYPE_DEVALLOT, "调拨流程");
		flowMap.put(FLOW_TYPE_DEVSCRAP, "报废流程");
		flowMap.put(FLOW_TYPE_DEVDIMISSION, "离职流程");
	}
	
	private String id;
	private DeviceClass deviceClass;
	private String orderPrefix;
	private String orderPostfix;
	private String separator;
	private Integer seqNum;
	private String areaCode;
	private String purchaseFlowKey;
	private String useApplyFlowKey;
	private String allocateFlowKey;
	private String discardFlowKey;
	private String dimissionFlowKey;
	private String remark;
	private Boolean mainDevFlag;
//	private Set<DeviceAcptCountCfg> deviceAcptCountCfgs = new HashSet<DeviceAcptCountCfg>(0);
	
	private transient String purchaseFlowName;
	private transient String useApplyFlowName;
	private transient String allocateFlowName;
	private transient String discardFlowName;
	private transient String deviceTypeName;
	private transient String dimissionFlowName;
//	private transient String devPerposeName;
//	private transient String devPerposes;
	
	
	
	// Constructors

	/** default constructor */
	public AreaDeviceCfg() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	public String getOrderPrefix() {
		return orderPrefix;
	}

	public void setOrderPrefix(String orderPrefix) {
		this.orderPrefix = orderPrefix;
	}

	public String getOrderPostfix() {
		return orderPostfix;
	}

	public void setOrderPostfix(String orderPostfix) {
		this.orderPostfix = orderPostfix;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Integer getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPurchaseFlowKey() {
		return purchaseFlowKey;
	}

	public void setPurchaseFlowKey(String purchaseFlowKey) {
		this.purchaseFlowKey = purchaseFlowKey;
	}

	public String getUseApplyFlowKey() {
		return useApplyFlowKey;
	}

	public void setUseApplyFlowKey(String useApplyFlowKey) {
		this.useApplyFlowKey = useApplyFlowKey;
	}

	public String getAllocateFlowKey() {
		return allocateFlowKey;
	}

	public void setAllocateFlowKey(String allocateFlowKey) {
		this.allocateFlowKey = allocateFlowKey;
	}

	public String getDiscardFlowKey() {
		return discardFlowKey;
	}

	public void setDiscardFlowKey(String discardFlowKey) {
		this.discardFlowKey = discardFlowKey;
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
		final AreaDeviceCfg other = (AreaDeviceCfg) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getAreaName() {
		Map<String ,DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		if(areaMap != null) {
			DataDictInfo dict = areaMap.get(getAreaCode());
			if(dict != null) {
				return dict.getDictName();
			}
		}
		return "";
	}
	
	public String getPurchaseFlowName() {
		return purchaseFlowName;
	}

	public void setPurchaseFlowName(String purchaseFlowName) {
		this.purchaseFlowName = purchaseFlowName;
	}

	public String getUseApplyFlowName() {
		return useApplyFlowName;
	}

	public void setUseApplyFlowName(String useApplyFlowName) {
		this.useApplyFlowName = useApplyFlowName;
	}

	public String getAllocateFlowName() {
		return allocateFlowName;
	}

	public void setAllocateFlowName(String allocateFlowName) {
		this.allocateFlowName = allocateFlowName;
	}

	public String getDiscardFlowName() {
		return discardFlowName;
	}

	public void setDiscardFlowName(String discardFlowName) {
		this.discardFlowName = discardFlowName;
	}

	public String getViewCode() {
		String codeStr="";
		for (int i=0; i<this.seqNum; i++) {
			if(i==this.seqNum-1){
				codeStr+="1";
			}else{
				codeStr+="0";
			}
		}
		if(this.separator!=null){
			return this.orderPrefix+this.separator+codeStr;
		}else{
			return this.orderPrefix+codeStr;
		}
	}

	public String getDimissionFlowKey() {
		return dimissionFlowKey;
	}

	public void setDimissionFlowKey(String dimissionFlowKey) {
		this.dimissionFlowKey = dimissionFlowKey;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	public Boolean getMainDevFlag() {
		return mainDevFlag;
	}

	public void setMainDevFlag(Boolean mainDevFlag) {
		this.mainDevFlag = mainDevFlag;
	}

	public String getDimissionFlowName() {
		return dimissionFlowName;
	}

	public void setDimissionFlowName(String dimissionFlowName) {
		this.dimissionFlowName = dimissionFlowName;
	}

//	public void setDevPerposeName(String devPerposeName) {
//		this.devPerposeName = devPerposeName;
//	}

//	public Set<DeviceAcptCountCfg> getDeviceAcptCountCfgs() {
//		return deviceAcptCountCfgs;
//	}
//
//	public void setDeviceAcptCountCfgs(Set<DeviceAcptCountCfg> deviceAcptCountCfgs) {
//		this.deviceAcptCountCfgs = deviceAcptCountCfgs;
//	}

//	public String getDevPerposes() {
//		return devPerposes;
//	}
//
//	public void setDevPerposes(String devPerposes) {
//		this.devPerposes = devPerposes;
//	}
//
//	public String getDevPerposeName() {
//		return devPerposeName;
//	}
}