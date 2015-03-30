/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IContractHandDAO;
import org.eapp.poss.dao.param.ContractHandQueryParameters;
import org.eapp.poss.dto.ContractHandStat;
import org.eapp.poss.hbean.ContractHand;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

public class ContractHandDAO extends BaseHibernateDAO implements IContractHandDAO {

    private static Log logger = LogFactory.getLog(ContractHandDAO.class);
    
	@Override
	public ContractHand findById(String id) {
		return (ContractHand) getSession().get(ContractHand.class, id);
	}
	
	@Override
	public ContractHand findByProdId(String prodId) {
		Query query = getSession().createQuery("from ContractHand as b where b.prodInfo.id=:prodId");
		query.setString("prodId", prodId);
		query.setMaxResults(1);
		return (ContractHand)query.uniqueResult();
	}
	
	@Override
	public ContractHandStat findContractHandStat(String prodId, String orgName) {
		Query query = getSession().createQuery("select new org.eapp.poss.dto.ContractHandStat(sum(b.signNums), sum(b.blankNums), sum(b.invalidNums)) " +
				"from ContractHand as b where b.prodInfo.id=:prodId and b.orgName=:orgName and b.checkStatus=:checkStatus");
		query.setString("prodId", prodId);
		query.setString("orgName", orgName);
		query.setInteger("checkStatus", ContractHand.CHECK_STATUS_YES);
		return (ContractHandStat)query.uniqueResult();
	}
	
	@Override
	public void deleteAll(String prodId, String orgName) {
		String hql = "delete from ContractHand as b where b.prodInfo.id=:prodId";
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

	@Override
	public ListPage<ContractHandStat> findStatPage(ContractHandQueryParameters qp) {
		String selObj = "select new org.eapp.poss.dto.ContractHandStat(s.prodInfo.id, s.prodInfo.prodName, s.orgName, count(s))";
		StringBuffer hql = new StringBuffer();
        hql.append(" from ContractHand as s where s.prodInfo.prodStatus <> :prodStatus ");
        qp.addParameter("prodStatus", ProdInfo.PROD_STATUS_STATUS_FOUND);

        if (qp.getProdId() != null) {
        	hql.append(" and s.prodInfo.id=:prodId");
        }
        if (qp.getProdName() != null) {
        	hql.append(" and s.prodInfo.prodName like :prodName");
        	qp.toArountParameter("prodName");
        }
        if (qp.getOrgName() != null) {
        	hql.append(" and s.orgName=:orgName");
        }
        if (qp.getCheckStatus() != null) {
        	hql.append(" and s.checkStatus=:checkStatus");
        }
        String groupHql = " group by s.prodInfo.id, s.prodInfo.prodName, s.orgName ";
        try {
            return new CommQuery<ContractHandStat>().queryListPage(qp, 
            		selObj + qp.appendOrders(hql, "s") + groupHql,
            		"select count(distinct s.prodInfo.id)" + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("query findPage failed:", re);
            throw re;
        }
	}
	
	@Override
	public ListPage<ContractHand> findPage(ContractHandQueryParameters qp) {
		StringBuffer hql = new StringBuffer();
        hql.append(" from ContractHand as s where s.prodInfo.prodStatus <> :prodStatus ");
        qp.addParameter("prodStatus", ProdInfo.PROD_STATUS_STATUS_FOUND);

        if (qp.getProdId() != null) {
        	hql.append(" and s.prodInfo.id=:prodId");
        }
        if (qp.getProdName() != null) {
        	hql.append(" and s.prodInfo.prodName like :prodName");
        	qp.toArountParameter("prodName");
        }
        if (qp.getOrgName() != null) {
        	hql.append(" and s.orgName=:orgName");
        }
        if (qp.getCheckStatus() != null) {
        	hql.append(" and s.checkStatus=:checkStatus");
        }
        try {
            return new CommQuery<ContractHand>().queryListPage(qp, qp.appendOrders(hql, "s"), getSession());
        } catch (RuntimeException re) {
            logger.debug("query findPage failed:", re);
            throw re;
        }
	}
    
}
