
package org.eapp.oa.kb.dao;

import java.util.List;

import org.eapp.oa.kb.dto.KnowledgeReplyQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeReply;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;


public interface IKnowledgeReplyDAO extends IBaseHibernateDAO {
	/**
	 * 通过主键查找 返回Knowledgereply对象
	 */
	public KnowledgeReply findById(java.lang.String id);

	/**
	 * 
	 */
	public List<KnowledgeReply> findByExample(KnowledgeReply instance);

	/**
	 * 通过字段查找 返回List<Knowledgereply>对象
	 */
	public List<KnowledgeReply> findByProperty(String propertyName, Object value);

	/**
	 * 查询所有数据信息 返回List<Knowledgereply>
	 */
	public List<KnowledgeReply> findAll();
	
	/**
	 * 查询回复
	 * @param krqp
	 * @return
	 */
	public ListPage<KnowledgeReply> queryKnowledgeReply(KnowledgeReplyQueryParameters krqp);

}

