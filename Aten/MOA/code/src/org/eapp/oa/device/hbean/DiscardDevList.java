package org.eapp.oa.device.hbean;
import java.util.Date;
import java.util.Map;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.rpc.dto.DataDictInfo;

// default package

/**
 * DiscardDevList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DiscardDevList implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6586151138947674425L;
	// Fields

	private String id;
	private DevDiscardForm devDiscardForm;
	private Device device;
	private String reason;
	private String dealType;
	private String discardType;
	private Date discardDate;
	private Double remaining;
	private Double depreciation;
	private Boolean buyFlag;
	private Double buyPrice;
	private Double noBuyPrice;
	private Date inDate;
	private Integer displayOrder;
	private Date planPayDate;
	// Constructors

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	/** default constructor */
	public DiscardDevList() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevDiscardForm getDevDiscardForm() {
		return devDiscardForm;
	}

	public void setDevDiscardForm(DevDiscardForm devDiscardForm) {
		this.devDiscardForm = devDiscardForm;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealTypeDisplayName() {
		Map<String ,DataDictInfo> dealTypeMap = null;
		if (DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue() == this.devDiscardForm.getFormType()) {
			//正常报废
			dealTypeMap = SysCodeDictLoader.getInstance().getScrapDisposeTypeMap();
		} else {
			//离职报废
			dealTypeMap = SysCodeDictLoader.getInstance().getLeaveDealTypeMap();
		}
		if (this.dealType != null && !"".equals(this.dealType)) {
			if(dealTypeMap != null) {
				DataDictInfo dict = dealTypeMap.get(this.dealType);
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

	public String getDiscardType() {
		return discardType;
	}

	public void setDiscardType(String discardType) {
		this.discardType = discardType;
	}

	public String getDiscardTypeDisplayName() {
		if (this.discardType != null && !"".equals(this.discardType)) {
			Map<String ,DataDictInfo> deviceTypeMap = SysCodeDictLoader.getInstance().getScrapTypeMap();
			if(deviceTypeMap != null) {
				DataDictInfo dict = deviceTypeMap.get(this.discardType);
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
	
	public Date getDiscardDate() {
		return discardDate;
	}

	public Date getPlanPayDate() {
		return planPayDate;
	}

	public void setPlanPayDate(Date planPayDate) {
		this.planPayDate = planPayDate;
	}

	public void setDiscardDate(Date discardDate) {
		this.discardDate = discardDate;
	}

	public Double getRemaining() {
		return remaining;
	}

	public void setRemaining(Double remaining) {
		this.remaining = remaining;
	}

	public Double getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(Double depreciation) {
		this.depreciation = depreciation;
	}

	public Boolean getBuyFlag() {
		return buyFlag;
	}

	public void setBuyFlag(Boolean buyFlag) {
		this.buyFlag = buyFlag;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Double getNoBuyPrice() {
		return noBuyPrice;
	}

	public void setNoBuyPrice(Double noBuyPrice) {
		this.noBuyPrice = noBuyPrice;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
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
		final DiscardDevList other = (DiscardDevList) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}