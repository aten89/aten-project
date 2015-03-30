package org.eapp.oa.doc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.doc.dao.IDocFormDAO;
import org.eapp.oa.doc.dto.DocFormQueryParameters;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.flow.hbean.TaskAssign;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocFormDAO extends BaseHibernateDAO implements IDocFormDAO {
    private static final Log log = LogFactory.getLog(DocFormDAO.class);
    // property constants
    public static final String DRAFTSMAN = "draftsman";
    public static final String GROUPNAME = "groupname";
    public static final String DOCNUMBER = "docNumber";
    public static final String SUBMITTO = "submitto";
    public static final String COPYTO = "copyto";
    public static final String SUBJECT = "subject";
    public static final String FLOWINSTANCEID = "flowinstanceid";
    public static final String PASSED = "passed";
    public static final String URGENCY = "urgency";
    public static final String SECURITYCLASS = "securityclass";

    /*
     * (non-Javadoc)
     * 
     */
    public DocForm findById(java.lang.String id) {
        log.debug("getting DocForm instance with id: " + id);
        try {
            DocForm instance = (DocForm) getSession().get(DocForm.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<DocForm> findByExample(DocForm instance) {
        log.debug("finding DocForm instance by example");
        try {
            List<DocForm> results = getSession().createCriteria("DocForm").add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<DocForm> findByProperty(String propertyName, Object value) {
        log.debug("finding DocForm instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from DocForm as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByDraftsman(Object draftsman) {
        return findByProperty(DRAFTSMAN, draftsman);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByGroupname(Object groupname) {
        return findByProperty(GROUPNAME, groupname);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByDocNumber(Object docnumber) {
        return findByProperty(DOCNUMBER, docnumber);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findBySubmitto(Object submitto) {
        return findByProperty(SUBMITTO, submitto);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByCopyto(Object copyto) {
        return findByProperty(COPYTO, copyto);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findBySubject(Object subject) {
        return findByProperty(SUBJECT, subject);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByFlowinstanceid(Object flowinstanceid) {
        return findByProperty(FLOWINSTANCEID, flowinstanceid);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByPassed(Object passed) {
        return findByProperty(PASSED, passed);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findByUrgency(Object urgency) {
        return findByProperty(URGENCY, urgency);
    }

    /*
     * (non-Javadoc)
     * 
     */
    public List<DocForm> findBySecurityclass(Object securityclass) {
        return findByProperty(SECURITYCLASS, securityclass);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @SuppressWarnings("unchecked")
    public List<DocForm> findAll() {
        log.debug("finding all DocForm instances");
        try {
            String queryString = "from DocForm";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询存为草稿的公文
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DocForm> findDocForm(String userAccountId, int docStatus, int fileClass) {
        log.debug("findDocForm with userAccountId: " + userAccountId + " and docStatus: " + docStatus + " and fileClass: " + fileClass);
        if (userAccountId == null) {
            throw new IllegalArgumentException("非法参数");
        }
        try {
            Query query = getSession().createQuery(
					"from DocForm as df where df.draftsman=:userAccountId " +
					"and df.docStatus=:docStatus and df.fileClass=:fileClass order by df.draftDate desc");
            query.setString("userAccountId", userAccountId);
            query.setInteger("docStatus", docStatus);
            query.setInteger("fileClass", fileClass);
            List<DocForm> list = query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("findDocForm faild", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<DocForm> queryDealingDocForm(String userId, int fileClass) {
	    log.debug("queryDealingDocForm with userId: " + userId + " and fileClass: " + fileClass);
		String hql = "select re,t from DocForm as re, FlowConfig as fc, " +
		"Task as t left join t.taskAssigns as p where re.fileClass=:fileClass and fc.flowClass in (select dc.flowClass from DocClass as dc where dc.flowClass is not null) and fc.draftFlag='2' and " +
		"fc.flowKey=t.flowKey and t.formID=re.id and (t.taskState=:createState or t.taskState=:startState) " +
		"and ( (t.transactor=:userId) or " +
		"(p.assignKey=:userId and t.transactor is null and p.type=:type)" +
		") order by t.createTime";
        try {
            Query query = getSession().createQuery(hql);
            // query.setString("flowClass", SysConstants.FLOWCLASS_BX);
            query.setInteger("fileClass", fileClass);
            query.setString("userId", userId);
            query.setString("createState", TaskInstance.PROCESS_STATE_CREATE);
            query.setString("startState", TaskInstance.PROCESS_STATE_START);
            query.setString("type", TaskAssign.TYPE_USER);
            List<Object[]> list = query.list();
            List<DocForm> docForms = new ArrayList<DocForm>();
            for (Object[] o : list) {
                DocForm doc = (DocForm) o[0];
                doc.setTask((Task) o[1]);
                docForms.add(doc);
            }
            return docForms;
        } catch (RuntimeException re) {
            log.error("queryDealingDocForm faild", re);
            throw re;
        }
    }

    public ListPage<DocForm> queryTrackDocForm(DocFormQueryParameters rqp, String userAccountId, int fileClass) {
        log.debug("queryTrackDocForm with DocFormQueryParameters and userAccountId: " + userAccountId + " and fileClass: " + fileClass);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DocForm as re, Task as t where  t.formID=re.id and re.fileClass=:fileClass and (t.taskState=:endState " +
					"and t.transactor=:userId  or re.draftsman=:userId and re.docStatus=:status)  and re.archiveDate is null ");
            rqp.addParameter("fileClass", fileClass);
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            rqp.addParameter("status", DocForm.STATUS_APPROVAL);
            if (rqp.getSubject() != null) {
                hql.append(" and re.subject like :subject ");
                rqp.toArountParameter("subject");
            }
            if (rqp.getBeginDraftDate() != null) {
                hql.append(" and re.draftDate >= :beginDraftDate ");
            }
            if (rqp.getEndDraftDate() != null) {
                hql.append(" and re.draftDate <= :endDraftDate ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }

            return new CommQuery<DocForm>().queryListPage(rqp, "select distinct re " + rqp.appendOrders(hql, "re"),
                    "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryTrackDocForm", re);
            return new ListPage<DocForm>();
        }

    }

    public ListPage<DocForm> queryArchDocForm(DocFormQueryParameters rqp, String userAccountId, int fileClass) {
        log.debug("queryArchDocForm with DocFormQueryParameters and userAccountId: " + userAccountId + " and fileClass: " + fileClass);
        if (rqp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
					"from DocForm as re, Task as t where t.formID=re.id and t.taskState=:endState " +
					" and (t.transactor=:userId or re.draftsman=:userId) and re.fileClass=:fileClass and re.archiveDate is not null");
            rqp.addParameter("fileClass", fileClass);
            rqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            rqp.addParameter("userId", userAccountId);
            if (rqp.getSubject() != null) {
                hql.append(" and re.subject like :subject ");
                rqp.toArountParameter("subject");
            }
            if (rqp.getBeginArchDate() != null) {
                hql.append(" and re.archiveDate >= :beginArchDate ");
            }
            if (rqp.getEndArchDate() != null) {
                hql.append(" and re.archiveDate <= :endArchDate ");
            }
            if (rqp.getDocClassName() != null) {
                hql.append(" and re.docClassName = :docClassName ");
            }
            if (rqp.getPassed() != null) {
                hql.append(" and re.passed = :passed ");
            }
            if (rqp.getDraftsman() != null) {
                hql.append(" and re.draftsman = :draftsman ");
            }
            if (rqp.getGroupName() != null) {
                hql.append(" and re.groupName like :groupName ");
                rqp.toArountParameter("groupName");
            }

            return new CommQuery<DocForm>().queryListPage(rqp, "select distinct re " + rqp.appendOrders(hql, "re"),
                    "select count(distinct re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("queryArchDocForm faild", re);
            return new ListPage<DocForm>();
        }
    }
}
