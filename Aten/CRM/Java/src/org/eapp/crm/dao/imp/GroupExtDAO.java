package org.eapp.crm.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.dao.IGroupExtDAO;
import org.eapp.crm.dao.param.GroupExtQueryParameters;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.GroupExt;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

public class GroupExtDAO extends BaseHibernateDAO implements IGroupExtDAO {

    private static final Log log = LogFactory.getLog(GroupExtDAO.class);
    
    @Override
    public GroupExt findById(String id) throws CrmException {
        try {
            return (GroupExt) getSession().get(GroupExt.class, id);
        } catch (RuntimeException re) {
            log.error("findById failed: ", re);
            throw re;
        }
    }

    @Override
    public ListPage<GroupExt> queryGroupExt(GroupExtQueryParameters qp) {
        if (qp == null) {
            throw new IllegalArgumentException();
        }

        try {
            StringBuffer hql = new StringBuffer();

            hql.append("select r from GroupExt as r where 1=1");

            if (qp.getGroupId() != null) {
                hql.append(" and r.groupId=:groupId");
            }

            if (qp.getBusinessType() != null) {
                hql.append(" and r.businessType=:businessType");
            }
            
            hql.append(" order by r.groupName");

            return new CommQuery<GroupExt>().queryListPage(qp, qp.appendOrders(hql, "r"), getSession());
        } catch (RuntimeException re) {
            log.error("query listPage error", re);
        }

        return new ListPage<GroupExt>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eapp.crm.dao.IGroupExtDAO#findByBusinessType(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<GroupExt> findByBusinessType(String businessType) throws CrmException {
        log.debug("find GroupExts by businessType :" + businessType);
        StringBuffer queryStr = new StringBuffer();
        queryStr.append(" from GroupExt g where 1=1 ");
        if (StringUtils.isNotEmpty(businessType)) {
            queryStr.append(" and g.businessType = :businessType ");
        }
        try {
            Query query = getSession().createQuery(queryStr.toString());
            if (StringUtils.isNotEmpty(businessType)) {
                query.setParameter("businessType", businessType);
            }
            return (List<GroupExt>) query.list();
        } catch (RuntimeException re) {
            log.error("find GroupExts by businessType failed: ", re);
            throw re;
        }
    }

}
