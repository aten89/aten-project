package org.eapp.crm.hbean;

/**
 */

public class ReportNormConf implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3123148554247752512L;
	private String id;
	private String rptID;
	private String normCode;//
	private String normName;//
	private String normValue;//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRptID() {
		return rptID;
	}
	public void setRptID(String rptID) {
		this.rptID = rptID;
	}
	public String getNormCode() {
		return normCode;
	}
	public void setNormCode(String normCode) {
		this.normCode = normCode;
	}
	public String getNormName() {
		return normName;
	}
	public void setNormName(String normName) {
		this.normName = normName;
	}
	public String getNormValue() {
		return normValue;
	}
	public void setNormValue(String normValue) {
		this.normValue = normValue;
	}
	
}