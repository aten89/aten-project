package org.eapp.oa.device.hbean;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DevDiscardForm entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DevDiscardForm extends DevFlowApplyProcess implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7482988067281749074L;
	
	// Fields
	public static String DEAL_TYPE_MOVEBACK="SCRAP_DISPOSE_MOVEBACK";//报废：退库
	public static String DEAL_TYPE_TAKE="SCRAP_DISPOSE_TAKE";//报废：回购
	public static String DEAL_TYPE_LOSE="SCRAP_DISPOSE_LOSE";//报废：丢失
	public static String DEAL_TYPE_TAKEAWAY="SCRAP_DISPOSE_TAKEAWAY";//报废：拿走
	
	public static final Integer FORM_TYPE_DISCARD_NORMAL = 0;//正常报废
	public static final Integer FORM_TYPE_DISCARD_LEAVE = 1;//离职报废
	
	public static String LEAVE_DEAL_TYPE_TOSTORAGE = "LEAVE_DISPOSE_TOSTORAGE";//离职：退库
	public static String LEAVE_DEAL_TYPE_TAKE = "LEAVE_DISPOSE_TAKE";//离职：拿走
	public static String LEAVE_DEAL_TYPE_BACKBUY = "LEAVE_DISPOSE_BACKBUY";//离职：离职回购
	
	private String id;
	private DevValidateForm devValidateForm;
	private Double workYear;
	private Set<DiscardDevList> discardDevLists = new HashSet<DiscardDevList>(0);
	private Date enterCompanyDate;
	private Integer formType;
	
	
	private transient DiscardDevList discardDevice;
	
	private transient String dealType;
	
	// Constructors

	/** default constructor */
	public DevDiscardForm() {
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

	public Double getWorkYear() {
		return workYear;
	}

	public void setWorkYear(Double workYear) {
		this.workYear = workYear;
	}

	public Set<DiscardDevList> getDiscardDevLists() {
		return discardDevLists;
	}

	public void setDiscardDevLists(Set<DiscardDevList> discardDevLists) {
		this.discardDevLists = discardDevLists;
	}

	public DiscardDevList getDiscardDevice() {
		return discardDevice;
	}

	public void setDiscardDevice(DiscardDevList discardDevice) {
		this.discardDevice = discardDevice;
	}

	public Date getEnterCompanyDate() {
		return enterCompanyDate;
	}

	public void setEnterCompanyDate(Date enterCompanyDate) { 
		this.enterCompanyDate = enterCompanyDate;
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
		final DevDiscardForm other = (DevDiscardForm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public Integer getFormType() {
		return formType;
	}

	public void setFormType(Integer formType) {
		this.formType = formType;
	}

}