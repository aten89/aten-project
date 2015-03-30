package org.eapp.crm.dao;
// default package

import java.io.Serializable;

/**
 * DAO基类
 * @author zsy
 *
 */
public interface IBaseHibernateDAO {
	
	/**
	 * 保存
	 * @param instance 实例
	 * @return 持久化实例
	 */
	Serializable save(Object instance);
	 
	 /**
	  * 持久化
	  * @param transientInstance 实例
	  */
	void persist(Object transientInstance);

	/**
	 * 删除
	 * @param instance 实例
	 */
	void delete(Object instance);
	
	/**
	 * 保存或更新
	 * @param instance 实例
	 */
	void saveOrUpdate(Object instance);
	
	/**
	 * 更新
	 * @param instance 实例
	 */
	void update(Object instance);
	
	/**
	 * 合并
	 * @param instance 实例
	 */
	Object merge(Object instance);
}