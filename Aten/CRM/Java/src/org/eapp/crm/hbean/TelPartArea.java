package org.eapp.crm.hbean;

/**
 * GroupExt entity. @author MyEclipse Persistence Tools
 */

public class TelPartArea implements java.io.Serializable {

	// Fields

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2782430941719688681L;
	
	public static final int TEL_PART_LENGTH = 7;//判断电话前面位数
	
	public static final TelPartArea UNKNOW_AREA = new TelPartArea("-1", "未知" , "未知");
	private String id;
	private String telPart;
	private String areaCode;
	private String cityName;
	private String telType;
	public TelPartArea(String areaCode,String cityName, String telType) {
		this.areaCode = areaCode;
		this.cityName = cityName;
		this.telType = telType;
	}
	
	public TelPartArea() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTelPart() {
		return telPart;
	}
	public void setTelPart(String telPart) {
		this.telPart = telPart;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getTelType() {
		return telType;
	}
	public void setTelType(String telType) {
		this.telType = telType;
	}

}