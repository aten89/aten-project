package org.eapp.oa.hr.hbean;

/**
 * HolidayType entity.
 * Description:假期种类
 * @author MyEclipse Persistence Tools
 */

public class HolidayType implements java.io.Serializable {

	private static final long serialVersionUID = -8588060832747658857L;
	//ID_,VARCHAR2(36),不能为空                                --主键ID
	private String id;
	//HolidayName_,VARCHAR2(128)		                  --假种名称
	private String holidayName;
	//MaxDays_,INTEGER                                    --单次最大天数
	private Double maxDays;
	//Expression_,TIMESTAMP                               --计算公式
	private String expression;
	private String description;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public Double getMaxDays() {
		return maxDays;
	}
	public void setMaxDays(Double maxDays) {
		this.maxDays = maxDays;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}