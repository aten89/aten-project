/**
 * 
 */
package org.eapp.workflow.def;

import java.util.Date;

/**
 * 流程中表达式用到的上下文变量申明
 * @author 卓诗垚
 * @version Oct 8, 2008
 * @version 1.0
 */
public class VariableDeclare implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2298786946456113415L;
	
	//数据类型
	
	//字符窜	DATATYPE_STRING
	public static final String DATATYPE_STRING  = "DATATYPE_STRING";
	//布尔类	DATATYPE_BOOLEAN
	public static final String DATATYPE_BOOLEAN  = "DATATYPE_BOOLEAN";
	//整数 DATATYPE_INT
	public static final String DATATYPE_INT  = "DATATYPE_INT";
	//长整数 DATATYPE_LONG
	public static final String DATATYPE_LONG  = "DATATYPE_LONG";
	//浮点数 DATATYPE_FLOAT
	public static final String DATATYPE_FLOAT  = "DATATYPE_FLOAT";
	//双精度浮点 DATATYPE_DOUBLE
	public static final String DATATYPE_DOUBLE  = "DATATYPE_DOUBLE";
	//日期时间 DATATYPE_DATE 
	public static final String DATATYPE_DATE  = "DATATYPE_DATE";
	
	String id;
	protected String name;//变量名称
	protected String displayName;//显示名称
	protected String type;//变量类型
	protected Boolean notNull;//非空
	
	public VariableDeclare() {
		
	}
	
	public VariableDeclare(String name, String type, Boolean notNull, String displayName) {
		this.name = name;
		this.type = type;
		this.displayName = displayName;
		this.notNull = notNull;
	}
	
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getNotNull() {
		return notNull;
	}
	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}
	
	/**
	 * 取得默认值
	 * @return
	 */
	public Object getTypeDefaultValue() {
		if (DATATYPE_STRING.equals(type)) {
			return " ";
		} else if (DATATYPE_BOOLEAN.equals(type)) {
			return Boolean.FALSE;
		} else if (DATATYPE_INT.equals(type)) {
			return new Integer(1);
		} else if (DATATYPE_LONG.equals(type)) {
			return new Long(1);
		} else if (DATATYPE_FLOAT.equals(type)) {
			return new Float(1);
		} else if (DATATYPE_DOUBLE.equals(type)) {
			return new Double(1);
		} else if (DATATYPE_DATE.equals(type)) {
			return new Date();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VariableDeclare))
			return false;
		final VariableDeclare other = (VariableDeclare) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}
