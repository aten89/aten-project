package org.eapp.oa.kb.dao;


import org.eapp.oa.kb.dto.KnowledgeLogQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.IBaseHibernateDAO;
/**
 * 知识点日志dao接口
 */
public interface IKnowledgeLogDAO extends IBaseHibernateDAO {
	
    /**
     * 分页查询知识库操作日志
     * @param queryParameters 知识点日志查询对象
     * @return 知识点分页对象
     */
	public ListPage<KnowledgeLog> queryKnowledgeLogListPage(KnowledgeLogQueryParameters queryParameters);

}
