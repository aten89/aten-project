/**
 * 
 */
package org.eapp.util.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QueryParameters
 * 对web的表单查询参数的简单封装，
 * 包函查询条件、排序条件、翻页页码、页容量
 * @author zhuosiyao , linliangyi 
 * @version 3.0
 */
public class QueryParameters implements Serializable {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 4857170242267099841L;
	/**
	 * 查询所有，不翻页
	 */
	public static final int ALL_PAGE_SIZE = 0;
	/**
	 * 查询条件
	 */
	private Map<String, Object> parameters = new HashMap<String, Object>();
	/**
	 * 排序条件
	 */
	private List<Order> orders = new ArrayList<Order>();
	/**
	 * 页码
	 */
	private int pageNo = 1;
	/**
	 * 页容量
	 */
	private int pageSize = 0;
	
	/**
	 * 取得查询条件
	 * @return 封装查询条件的Map
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * 设置查询条件
	 * @param parameters 封装查询条件的Map
	 */
	protected void setParameters(Map<String, Object> parameters) {
		if (parameters != null) {
			this.parameters = new HashMap<String, Object>(parameters);
		} else {
			this.parameters  = new HashMap<String, Object>();
		}
	}

	/**
	 * 取得排序条件
	 * @return 封装排序条件的List
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置排序条件
	 * @param orders 封装排序条件的List
	 */
	public void setOrders(List<Order> orders) {
			this.orders = orders;
	}

	/**
	 * 取得查询结果翻页页码
	 * @return 查询结果翻页页码
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置页码
	 * @param pageNo 查询结果翻页页码
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 取得每页记录数
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页记录数
	 * @param pageSize 每页记录数
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 添加查询条件参数
	 * @param name 名称，表单字段名称
	 * @param value 值，表单字段值
	 */
	public void addParameter(String name, Object value) {
		parameters.put(name, value);
	}
	
	/**
	 * 称称参数
	 * @param name 参数名
	 */
	public void removeParameter(String name) {
		parameters.remove(name);
	}
	
	/**
	 * 获取参数
	 * @param name 参数名
	 * @return 参数值
	 */
	public Object getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * 转成前缀匹配
	 * @param name 参数名
	 */
	public void toPrefixParameter(String name) {
		Object o = getParameter(name);
		if (o == null) {
			return;
		}
		if (o instanceof String) {
			addParameter(name, (String) o + "%");
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 转成后缀匹配
	 * @param name 参数名
	 */
	public void toPostfixParameter(String name) {
		Object o = getParameter(name);
		if (o == null) {
			return;
		}
		if (o instanceof String) {
			addParameter(name, "%" + (String) o);
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 转成前后缀匹配
	 * @param name 参数名
	 */
	public void toArountParameter(String name) {
		Object o = getParameter(name);
		if (o == null) {
			return;
		}
		if (o instanceof String) {
			addParameter(name, "%" + (String) o + "%");
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * 添加排序条件参数
	 * @param fieldName 字段名，通常要与Hibernate Bean属性名一致
	 * @param ascend  true 升序 : false 降序
	 */
	public void addOrder(String fieldName, boolean ascend) {
		if (fieldName == null) {
			throw new IllegalArgumentException();
		}
		if (ascend) {
			orders.add(new Order(fieldName, Order.ASC));
		} else {
			orders.add(new Order(fieldName, Order.DESC));
		}
	}
	
	/**
	 * 拼接Order by语句
	 * @param hql HQL
	 * @param asName 查询别名
	 * @return HQL
	 */
	public String appendOrders(StringBuffer hql, String asName) {
		if (hql == null) {
			throw new IllegalArgumentException();
		}
		if (asName == null) {
			asName = "";
		}
		if (orders != null && orders.size() > 0) {
			hql.append(" order by");
			for (Order order : orders) {
				hql.append(" ").append(asName).append(".").append(order.getFieldName())
						.append(" ").append(order.getType()).append(",");
			}
			hql.deleteCharAt(hql.length() - 1);
		}
		return hql.toString();
	}
	
	/**
	 * 排序对象，描述数据库字段的排序
	 * @author linly
	 * May 23, 2008
	 */
	public class Order implements Serializable {
		/**
		 * 序列
		 */
		private static final long serialVersionUID = 2197905467998049601L;
		/**
		 * 升序排列
		 */
		public static final String ASC = "asc";
		/**
		 * 降序排列
		 */
		public static final String DESC = "desc";
		/**
		 * 字段名
		 */
		private String fieldName;
		/**
		 * 排序类型
		 */
		private String type;

		/**
		 * 构造
		 */
		Order() {
			
		}
		/**
		 * 构造
		 * @param fileName 字段名
		 */
		Order(String fileName) {
			this(fileName, ASC);
		}

		/**
		 * 构造
		 * @param fieldName 字段名
		 * @param type 排序
		 */
		Order(String fieldName, String type) {
			this.fieldName = fieldName;
			this.type = type;
		}
		
		/**
		 * 获取字段名
		 * @return 字段名
		 */
		public String getFieldName() {
			return fieldName;
		}
		void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getType() {
			return type;
		}
		void setType(String type) {
			this.type = type;
		}
	}
}
