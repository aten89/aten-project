/**
 * 
 */
package org.eapp.poss.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IContractRequestDAO;
import org.eapp.poss.dao.param.ContractRequestQueryParameters;
import org.eapp.poss.dto.ContractRequestStat;
import org.eapp.poss.hbean.ContractRequest;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

public class ContractRequestDAO extends BaseHibernateDAO implements IContractRequestDAO {

    private static Log logger = LogFactory.getLog(ContractRequestDAO.class);
    
	@Override
	public ContractRequest findById(String id) {
		return (ContractRequest) getSession().get(ContractRequest.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContractRequest> findByProdId(String prodId) {
		Query query = getSession().createQuery("from ContractRequest as b where b.prodInfo.id=:prodId");
		query.setString("prodId", prodId);
		return query.list();
	}
	
	@Override
	public int findRegNums(String prodId, String orgName) {
		Query query = getSession().createQuery("select count(b) from ContractRequest as b where " +
				"b.prodInfo.id=:prodId and b.orgName=:orgName");
		query.setString("prodId", prodId);
		query.setString("orgName", orgName);
		Number count = (Number)query.uniqueResult();
		return count == null ? 0 : count.intValue();
	}
	
	@Override
	public int findExtendNums(String prodId, String orgName) {
		Query query = getSession().createQuery("select sum(b.extendNums) from ContractRequest as b where " +
				"b.prodInfo.id=:prodId and b.orgName=:orgName and b.regStatus<>:regStatus");
		query.setString("prodId", prodId);
		query.setString("orgName", orgName);
		query.setInteger("regStatus", ContractRequest.REG_STATUS_REGIEST);
		Number sum = (Number)query.uniqueResult();
		return sum == null ? 0 : sum.intValue();
	}
	
	
	@Override
	public void deleteAll(String prodId, String orgName) {
		String hql = "delete from ContractRequest as b where b.prodInfo.id=:prodId";
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
	public ListPage<ContractRequestStat> findStatPage(ContractRequestQueryParameters qp) {
		String selObj = "select new org.eapp.poss.dto.ContractRequestStat(s.prodInfo.id, s.prodInfo.prodName, s.orgName, count(s))";
		StringBuffer hql = new StringBuffer();
        hql.append(" from ContractRequest as s where s.prodInfo.prodStatus <> :prodStatus ");
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
        if (qp.getRegStatus() != null) {
        	hql.append(" and s.regStatus=:regStatus");
        }
        String groupHql = " group by s.prodInfo.id, s.prodInfo.prodName, s.orgName ";
        try {
            return new CommQuery<ContractRequestStat>().queryListPage(qp, 
            		selObj + qp.appendOrders(hql, "s") + groupHql,
            		"select count(distinct s.prodInfo.id)" + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("query findPage failed:", re);
            throw re;
        }
	}
	
	@Override
	public ListPage<ContractRequest> findPage(ContractRequestQueryParameters qp) {
		StringBuffer hql = new StringBuffer();
        hql.append(" from ContractRequest as s where s.prodInfo.prodStatus <> :prodStatus ");
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
        if (qp.getRegStatus() != null) {
        	hql.append(" and s.regStatus=:regStatus");
        }
        try {
            return new CommQuery<ContractRequest>().queryListPage(qp, qp.appendOrders(hql, "s"), getSession());
        } catch (RuntimeException re) {
            logger.debug("query findPage failed:", re);
            throw re;
        }
	}
    
}
