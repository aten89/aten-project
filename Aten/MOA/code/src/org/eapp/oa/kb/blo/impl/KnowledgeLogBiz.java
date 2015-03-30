package org.eapp.oa.kb.blo.impl;

import java.util.Date;

import org.eapp.oa.kb.blo.IKnowledgeLogBiz;
import org.eapp.oa.kb.dao.IKnowledgeLogDAO;
import org.eapp.oa.kb.dto.KnowledgeLogQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.util.hibernate.ListPage;


public class KnowledgeLogBiz implements IKnowledgeLogBiz {

    /**
     * 知识点日志dao
     */
    private IKnowledgeLogDAO knowledgeLogDAO;


    public void setKnowledgeLogDAO(IKnowledgeLogDAO knowledgeLogDAO) {
        this.knowledgeLogDAO = knowledgeLogDAO;
    }

    @Override
    public ListPage<KnowledgeLog> getKnowledgeLogListPage(KnowledgeLogQueryParameters queryParameters) {
        if (queryParameters == null) {
            throw new IllegalArgumentException();
        }
        return knowledgeLogDAO.queryKnowledgeLogListPage(queryParameters);
    }

    @Override
    public KnowledgeLog addKnowledgeLog(String userid, String type, String knowledgeid, String knowledgetitle) {

        KnowledgeLog knowledgeLog = new KnowledgeLog();

        knowledgeLog.setUserid(userid);
        knowledgeLog.setType(type);
        knowledgeLog.setKnowledgeid(knowledgeid);
        knowledgeLog.setKnowledgetitle(knowledgetitle);
        knowledgeLog.setOperatetime(new Date());

        knowledgeLogDAO.save(knowledgeLog);

        return knowledgeLog;
    }
}
