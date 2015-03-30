package org.eapp.oa.device.hbean;

// default package

/**
 * DiscardDealDevList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DiscardDealDevList implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6586151138947674425L;
	// Fields

	private String id;
	private DevDiscardDealForm devDiscardDealForm;
	private Device device;
	private Integer displayOrder;
	// Constructors

	/** default constructor */
	public DiscardDealDevList() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevDiscardDealForm getDevDiscardDealForm() {
		return devDiscardDealForm;
	}

	public void setDevDiscardDealForm(DevDiscardDealForm devDiscardDealForm) {
		this.devDiscardDealForm = devDiscardDealForm;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
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
		final DiscardDealDevList other = (DiscardDealDevList) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}