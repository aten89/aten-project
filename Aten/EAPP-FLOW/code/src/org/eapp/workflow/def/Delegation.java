/**
 * 
 */
package org.eapp.workflow.def;

/**
 * 
 * @author 卓诗垚
 * @version Sep 25, 2008
 * @version 2.0
 */
public class Delegation implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5263793815189940297L;

	String id;
	//表达式
	protected String expression;
	//处理类
	protected String className;
	
	public Delegation() {
		
	}
	public Delegation(String expression, String className){
		this.expression = expression;
		this.className = className;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
		if (!(obj instanceof Delegation))
			return false;
		final Delegation other = (Delegation) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}	
	
}
