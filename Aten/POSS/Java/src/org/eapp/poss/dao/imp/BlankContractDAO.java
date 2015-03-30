/**
 * 
 */
package org.eapp.poss.dao.imp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IBlankContractDAO;
import org.eapp.poss.dao.param.BlankContractQueryParameters;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.ContractRegDetail;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

public class BlankContractDAO extends BaseHibernateDAO implements IBlankContractDAO {

    private static Log logger = LogFactory.getLog(BlankContractDAO.class);
    
	@Override
	public BlankContract findById(String id) {
		return (BlankContract) getSession().get(BlankContract.class, id);
	}
	
	@Override
	public ContractRegDetail findDetailById(String detailId) {
		return (ContractRegDetail) getSession().get(ContractRegDetail.class, detailId);
	}
	
	@Override
	public BlankContract findByProdId(String prodId) {
		Query query = getSession().createQuery("from BlankContract as b where b.prodInfo.id=:prodId");
		query.setString("prodId", prodId);
		query.setMaxResults(1);
		return (BlankContract)query.uniqueResult();
	}

	@Override
	public ListPage<BlankContract> findPage(BlankContractQueryParameters qp) {
		StringBuffer hql = new StringBuffer();
        hql.append(" from BlankContract as s where s.prodInfo.prodStatus <> :prodStatus ");
        qp.addParameter("prodStatus", ProdInfo.PROD_STATUS_STATUS_FOUND);
//        if (qp.getProdStatus() != null) {
//        	hql.append(" and s.prodInfo.prodStatus=:prodStatus");
//        }
        if (qp.getProdId() != null) {
        	hql.append(" and s.prodInfo.id=:prodId");
        }
        if (qp.getProdName() != null) {
        	hql.append(" and s.prodInfo.prodName like :prodName");
        	qp.toArountParameter("prodName");
        }
        try {
            return new CommQuery<BlankContract>().queryListPage(qp, qp.appendOrders(hql, "s"), getSession());
        } catch (RuntimeException re) {
            logger.debug("query findPage failed:", re);
            throw re;
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContractRegDetail> findContractRegDetails(String contractId) {
		Query query = getSession().createQuery("from ContractRegDetail as b where b.blankContract.id=:contractId order by b.regDate");
		query.setString("contractId", contractId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdInfo> findProdInfos() {
		Query query = getSession().createQuery("select b.prodInfo from BlankContract as b  where b.prodInfo.prodStatus <> :prodStatus");
		query.setString("prodStatus", ProdInfo.PROD_STATUS_STATUS_FOUND);
		return query.list();
	}
    
}
