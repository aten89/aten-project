/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IConfirmExtendDAO;
import org.eapp.poss.dao.param.ConfirmExtendQueryParameters;
import org.eapp.poss.hbean.ConfirmExtend;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

public class ConfirmExtendDAO extends BaseHibernateDAO implements IConfirmExtendDAO {

    private static Log logger = LogFactory.getLog(ConfirmExtendDAO.class);
    
	@Override
	public ConfirmExtend findById(String id) {
		return (ConfirmExtend) getSession().get(ConfirmExtend.class, id);
	}
	
	@Override
	public ListPage<ConfirmExtend> findPage(ConfirmExtendQueryParameters qp) {
		StringBuffer hql = new StringBuffer();
        hql.append(" from ConfirmExtend as s where 1=1 ");
        if (qp.getProdId() != null) {
        	hql.append(" and s.prodInfo.id=:prodId");
        }
        if (qp.getProdName() != null) {
        	hql.append(" and s.prodInfo.prodName like :prodName");
        	qp.toArountParameter("prodName");
        }
        if (qp.getOrgName() != null) {
        	hql.append(" and s.orgName like :orgName");
        	qp.toArountParameter("orgName");
        }
        try {
            return new CommQuery<ConfirmExtend>().queryListPage(qp, qp.appendOrders(hql, "s"), getSession());
        } catch (RuntimeException re) {
            logger.debug("query findPage failed:", re);
            throw re;
        }
	}
	@Override
	public void deleteAll(String prodId, String orgName) {
		String hql = "delete from ConfirmExtend as b where b.prodInfo.id=:prodId";
		if (StringUtils.isNotBlank(orgName)) {
			hql += " and b.orgName=:orgName";
		}
		Query query = getSession().createQuery(hql);
		query.setString("prodId", prodId);
		if (StringUtils.isNotBlank(orgName)) {
			query.setString("orgName", orgName);
		}
		query.executeUpdate();
	}
}
