
package org.eapp.oa.kb.dao;

import java.util.List;

import org.eapp.oa.lucene.IndexTask;
import org.eapp.oa.system.dao.IBaseHibernateDAO;

public interface IIndexTaskDAO extends IBaseHibernateDAO {
	
	/**
	 * 通过主键查找 返回IndexTask对象
	 */
	IndexTask findById(java.lang.String id);
	
	/**
	 * 
	 */
	List<IndexTask> findByExample(IndexTask instance);
	
	/**
	 * 查询所有数据信息 返回List<IndexTask>
	 */
	List<IndexTask> findAll();
	
	/**
	 * 查询给定数量的记录
	 * @param count
	 * @return
	 */
	List<IndexTask> findIndexTask(int count);
	
	/**
	 * 统计数量
	 * @return
	 */
	long countIndexTask();
}

