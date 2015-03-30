package org.eapp.oa.kb.dao.impl;


import org.apache.commons.lang3.StringUtils;
import org.eapp.oa.kb.dao.IKnowledgeLogDAO;
import org.eapp.oa.kb.dto.KnowledgeLogQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 知识点日志dao
 * 
 */
public class KnowledgeLogDAO extends BaseHibernateDAO implements IKnowledgeLogDAO {

    @Override
    public ListPage<KnowledgeLog> queryKnowledgeLogListPage(KnowledgeLogQueryParameters queryParameters) {

        if (queryParameters == null) {
            throw new IllegalArgumentException("查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer("select log from KnowledgeLog as log where 1=1 ");
            if (StringUtils.isNotEmpty(queryParameters.getUserid())) {
                hql.append(" and log.userid = :userid ");
            }
            if (queryParameters.getBeginOperatetime() != null) {
                hql.append(" and log.operatetime >= :beginOperatetime ");
            }
            if (queryParameters.getEndOperatetime() != null) {
                hql.append(" and log.operatetime <= :endOperatetime ");
            }
            if (queryParameters.getKnowledgeid() != null) {
                hql.append(" and log.knowledgeid like :knowledgeid ");
                queryParameters.toArountParameter("knowledgeid");
            }
            if (queryParameters.getKnowledgetitle() != null) {
                hql.append(" and log.knowledgetitle like :knowledgetitle ");
                queryParameters.toArountParameter("knowledgetitle");
            }
            return (ListPage<KnowledgeLog>) new CommQuery<KnowledgeLog>().queryListPage(queryParameters,
                    queryParameters.appendOrders(hql, "log"), getSession());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ListPage<KnowledgeLog>();
        }
    }
}
