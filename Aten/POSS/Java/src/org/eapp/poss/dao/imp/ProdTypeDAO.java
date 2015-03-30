package org.eapp.poss.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IProdTypeDAO;
import org.eapp.poss.hbean.ProdType;
import org.hibernate.Query;

public class ProdTypeDAO extends BaseHibernateDAO implements IProdTypeDAO {

	private static final Log log = LogFactory.getLog(ProdTypeDAO.class);
	
	@Override
	public ProdType findByID(String id) {
		log.debug("getting ProdType instance with id: " + id);
	    ProdType instance = (ProdType)getSession().get(
	      "org.eapp.poss.hbean.ProdType", id);
	    return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdType> findRootProdTypes() {
		log.debug("finding root ProdType instances");
	    String queryString = "select p,p.subProdTypes.size from ProdType as p where p.parentProdType is null";
	    queryString = queryString + " order by p.displayOrder,p.prodType";
	    Query queryObject = getSession().createQuery(queryString);
	    
	    List<Object[]> list = queryObject.list();
	    return buildProdTypes(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdType> findSubProdTypes(String parentID) {
		log.debug("finding sub ProdType instances");
	    String queryString = "select p,p.subProdTypes.size from ProdType as p where p.parentProdType.id=:parentID";
	    queryString = queryString + " order by p.displayOrder,p.prodType";
	    Query queryObject = getSession().createQuery(queryString).setString("parentID", parentID);
	    
	    List<Object[]> list = queryObject.list();
	    
	    return buildProdTypes(list);
	}
	
	private List<ProdType> buildProdTypes(List<Object[]> list) {
		List<ProdType> prodTypes = new ArrayList<ProdType>(list.size());
	    ProdType prodType = null;
	    Integer count = null;
	    for (Object[] item : list) {
	      prodType = (ProdType)item[0];
	      count = (Integer)item[1];
	      prodType.setHasSubProdType((count != null) && (count.longValue() > 0L));
	      prodTypes.add(prodType);
	    }
	    return prodTypes;
	}

	@Override
	public boolean checkRepetition(String prodType, String parentID) {
		if (StringUtils.isBlank(prodType)) {
			return true;
		}
		
	    try {
	    	String hql = "select count(*) from ProdType as p where p.prodType=:prodType";
	    	if (StringUtils.isNotBlank(parentID)) {
	    		hql += " and p.parentProdType.id=:parentID";
	    	} else {
	    		hql += " and p.parentProdType.id is null";
	    	}
	    	Query query = getSession().createQuery(hql);
	     	query.setString("prodType", prodType);
	     	if (StringUtils.isNotBlank(parentID)) {
	     		query.setString("parentID", parentID);
	     	}
	     	Long count = (Long)query.uniqueResult();
	     	if ((count != null) && (count.longValue() > 0L)) {
	     		return true;
	     	}
	    }catch (RuntimeException re) {
	      log.error("attach failed", re);
	    }

	    return false;
	}

	@Override
	public boolean existsSubProdTypes(ProdType prodTypeEntity) {
		Long count = (Long)getSession().createFilter(prodTypeEntity.getSubProdTypes(), "select count(*) ").uniqueResult();
	    return count.intValue() > 0;
	}

	@Override
	public void saveOrder(String parentID, String[] orderSubIDs) {
		if ((orderSubIDs == null) || (orderSubIDs.length < 1)) {
		      return;
		}
	    Query query = null;
	    if (StringUtils.isNotBlank(parentID)) {
	    	query = getSession().createQuery("update ProdType as p set p.displayOrder=:order where p.id=:prodTypeID and p.parentProdType.id=:parentID");

	    	query.setString("parentID", parentID);
	    } else {
	    	query = getSession().createQuery("update ProdType as p set p.displayOrder=:order where p.id=:prodTypeID and p.parentProdType is null");
	    }

	    int order = 1;
	    for (int i = 0; i < orderSubIDs.length; i++) {
	    	if (StringUtils.isBlank(orderSubIDs[i])) {
	    		continue;
	    	}
	    	query.setInteger("order", order)
	        	.setString("prodTypeID", orderSubIDs[i]);
	    	if (query.executeUpdate() > 0)
	    		order++;
	    }
	}
}