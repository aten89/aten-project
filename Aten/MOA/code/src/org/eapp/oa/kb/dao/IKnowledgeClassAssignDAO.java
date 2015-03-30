
package org.eapp.oa.kb.dao;

import java.util.List;

import org.eapp.oa.kb.hbean.KnowledgeClassAssign;

import org.eapp.oa.system.dao.IBaseHibernateDAO;


public interface IKnowledgeClassAssignDAO extends IBaseHibernateDAO {

	/**
	 * 通过主键查找 返回Knowledgeclassassign对象
	 */
	public KnowledgeClassAssign findById(java.lang.String id);

	/**
	 * 
	 */
	public List<KnowledgeClassAssign> findByExample(KnowledgeClassAssign instance);

	/**
	 * 通过字段查找 返回List<Knowledgeclassassign>对象
	 */
	public List<KnowledgeClassAssign> findByProperty(String propertyName, Object value);

	/**
	 * 查询所有数据信息 返回List<Knowledgeclassassign>
	 */
	public List<KnowledgeClassAssign> findAll();
	
	/**
	 * 查询绑定的权限
	 * @param id
	 * @param key
	 * @return
	 */
	public List<KnowledgeClassAssign> findBindById(String id, int key) ;
	
	/**
	 * 删除权限绑定
	 * @param id
	 * @param key
	 */
	public void delBindById(String id, int key);
	
	/**
	 * 通过KnowledgeClass的id查找绑定的所有类型的权限
	 * @param id
	 * @return
	 */
	public List<KnowledgeClassAssign> findBindById(String id);
	/**
	 * 通过KnowledgeClass的id删除绑定的所有类型的权限
	 * @param id
	 * @return
	 */
	public void delBindById(String id) ;
	
	public List<KnowledgeClassAssign> findBindByIdAndFlag(String id, int key, int flag);
}

