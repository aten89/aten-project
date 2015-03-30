package org.eapp.workflow.exe;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eapp.workflow.def.VariableDeclare;
import org.eapp.workflow.expression.IllegalExpressionException;

/**
 * 流程实例上下文变量实体
 * ContextVariables entity.
 * 
 * @author 林良益
 * 2008-09-01
 * @version 1.0
 */

public class ContextVariable implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1555047900612932765L;
	
	// Fields
	//物理ID
	String id;
	//关联的流程实例
	protected FlowInstance flowInstance;
	//变量字段名
	protected String name;
	//变量显示名称
	protected String displayName;
	//变量类型
	protected String type;
	//变量值
	protected String value;
	//变量的现实顺序——该顺序由具体的业务应用在需要的时候指定
	protected Integer displayOrder;

	// Constructors

	/** default constructor */
	public ContextVariable() {
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param value
	 */
	public ContextVariable(String name , String type , String value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	public ContextVariable(String name, Object obj) {
		this.name = name;
		if (obj instanceof Boolean) {
			this.type = VariableDeclare.DATATYPE_BOOLEAN;
			this.value = obj.toString();
		} else if (obj instanceof Integer) {
			this.type = VariableDeclare.DATATYPE_INT;
			this.value = obj.toString();
		} else if (obj instanceof Long) {
			this.type = VariableDeclare.DATATYPE_LONG;
			this.value = obj.toString();
		} else if (obj instanceof Float) {
			this.type = VariableDeclare.DATATYPE_FLOAT;
			this.value = obj.toString();
		} else if (obj instanceof Double) {
			this.type = VariableDeclare.DATATYPE_DOUBLE;
			this.value = obj.toString();
		} else if (obj instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.type = VariableDeclare.DATATYPE_DATE;
			this.value = sdf.format((Date)obj);
		} else {
			this.type = VariableDeclare.DATATYPE_STRING;
			this.value = obj.toString();
		}
	}

	public FlowInstance getFlowInstance() {
		return flowInstance;
	}


	public void setFlowInstance(FlowInstance flowInstance) {
		this.flowInstance = flowInstance;
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


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public Integer getDisplayOrder() {
		return displayOrder;
	}


	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public Object getTypeValue() {
		try {
			if (VariableDeclare.DATATYPE_STRING.equals(type)) {
				return value;
			} else if (VariableDeclare.DATATYPE_BOOLEAN.equals(type)) {
				return Boolean.valueOf(value);
			} else if (VariableDeclare.DATATYPE_INT.equals(type)) {
				return Integer.parseInt(value);
			} else if (VariableDeclare.DATATYPE_LONG.equals(type)) {
				return Long.parseLong(value);
			} else if (VariableDeclare.DATATYPE_FLOAT.equals(type)) {
				return Float.parseFloat(value);
			} else if (VariableDeclare.DATATYPE_DOUBLE.equals(type)) {
				return Double.parseDouble(value);
			} else if (VariableDeclare.DATATYPE_DATE.equals(type)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(value);
			}
		} catch (Exception e) {
			throw new IllegalExpressionException("表达式值转对像出错：" + value, e);
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
		if (!(obj instanceof ContextVariable))
			return false;
		final ContextVariable other = (ContextVariable) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 序列化上下文变量
	 * @return
	 */
	public String toXMLString(){
		StringBuffer xmlBuff = new StringBuffer();
		xmlBuff.append("<var id=\"").append(id).append("\" >");
		xmlBuff.append("<n>").append(name).append("</n>");
		if(displayName != null){
			xmlBuff.append("<d>").append(displayName).append("</d>");
		}
		xmlBuff.append("<t>").append(type).append("</t>");
		xmlBuff.append("<v>").append(value).append("</v>");
		if(displayOrder != null){
			xmlBuff.append("<o>").append(displayOrder).append("</o>");
		}
		xmlBuff.append("</var>\r");
		return xmlBuff.toString();
	}

}