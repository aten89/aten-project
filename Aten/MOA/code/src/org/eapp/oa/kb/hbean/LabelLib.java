package org.eapp.oa.kb.hbean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// default package

/**
 * LabelLib entity.
 * Description:标签库
 * @author zfc
 */

public class LabelLib implements java.io.Serializable {

	

	private static final long serialVersionUID = -4439120124388942097L;
	
	public static final Integer NORMAL = 0 ; 
	
	private static Map<Integer , String> propertyMap = new HashMap<Integer , String>();
	
	static{
		propertyMap.put(NORMAL, "普通");
	}
	
	//ID_,VARCHAR2(36),不能为空                               --主键ID
	private String id;
	//Name_,VARCHAR2(256)                                --标签名称
	private String name;
	//Count_,NUMBER(6,2                                  --计数器
	private Long count;
	//Property_	SMALLINT							     --标签属性	
	private Integer property;
	//CreateDate_, TIMESTAMP                             --添加时间
	private Date createDate;
	
	
	public LabelLib(String id, String name, Long count, Integer property,
			Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.count = count;
		this.property = property;
		this.createDate = createDate;
	}
	public LabelLib(){}
	
	
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
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Integer getProperty() {
		return property;
	}
	public void setProperty(Integer property) {
		this.property = property;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getPropertyName(){
		return propertyMap.get(property);
	}
	
}