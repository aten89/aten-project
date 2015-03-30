package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eapp.client.util.UsernameCache;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * DevDiscardForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevDiscardDealForm implements java.io.Serializable {

	/** 
	 * 
	 */
	private static final long serialVersionUID = -7482988067281749074L;
	
	// Fields

	private String id;
	private String regAccountID;
	private Date regTime;
	private String deviceType;
	private Double salePrice;
	private Date saleDate;
	private Integer formNO;
	private Integer sequenceYear;
	
	private Set<DiscardDealDevList> discardDealDevLists = new HashSet<DiscardDealDevList>(0);
	
	// Constructors

	/** default constructor */
	public DevDiscardDealForm() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegAccountID() {
		return regAccountID;
	}

	public void setRegAccountID(String regAccountID) {
		this.regAccountID = regAccountID;
	}

	public String getRegAccountDisplayName() {
		return UsernameCache.getDisplayName(regAccountID);
	}
	
	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceTypeDisplayName() {
		if (deviceType != null && !"".equals(deviceType)) {
			Map<String ,DataDictInfo> deviceTypeMap = SysCodeDictLoader.getInstance().getDeviceType();
			if(deviceTypeMap != null) {
				DataDictInfo dict = deviceTypeMap.get(deviceType);
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

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Integer getFormNO() {
		return formNO;
	}

	public void setFormNO(Integer formNO) {
		this.formNO = formNO;
	}

	public Integer getSequenceYear() {
		return sequenceYear;
	}

	public void setSequenceYear(Integer sequenceYear) {
		this.sequenceYear = sequenceYear;
	}

	public Set<DiscardDealDevList> getDiscardDealDevLists() {
		return discardDealDevLists;
	}

	public void setDiscardDealDevLists(Set<DiscardDealDevList> discardDealDevLists) {
		this.discardDealDevLists = discardDealDevLists;
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
		final DevDiscardDealForm other = (DevDiscardDealForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getViewCode() {
		return this.sequenceYear+SerialNoCreater.getIsomuxByNum(this.formNO.toString(),4);
	}
}