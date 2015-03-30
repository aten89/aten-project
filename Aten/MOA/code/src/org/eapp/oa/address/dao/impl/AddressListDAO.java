/*
 * @(#) AddressListDAO.java 1.0 09/11/05 
 */
package org.eapp.oa.address.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.address.dao.IAddressListDAO;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.hibernate.Query;


public class AddressListDAO extends BaseHibernateDAO implements IAddressListDAO{
	private static final Log log = LogFactory.getLog( AddressListDAO.class );
		
	
	@Override
	public AddressList findById(String id) {
		log.debug("getting AddressList instance with id: " + id);
		try {
			AddressList addrList = (AddressList)getSession().get(AddressList.class, id);
			return addrList;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}		
	}

	@SuppressWarnings("unchecked")
	@Override
	public AddressList findByAccountId(String accountId) {
		log.debug( "finding AddressList instance by user's account id:" + accountId);
		try{			
			String queryString = "from AddressList as al where al.userAccountId=:accountId";
			Query query = getSession().createQuery( queryString );
			query.setParameter( "accountId", accountId);
			List<AddressList> addrLists = query.list();
			if( addrLists == null || addrLists.size() == 0 ) return null;
			return addrLists.get(0);
		} catch (RuntimeException e) {
			log.error("find by user's account id fail.", e);
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AddressList> queryAddressList(List<String> accountIDs) {
		if (accountIDs == null || accountIDs.isEmpty())  {
			return null;
		}
		String queryString = "from AddressList as al where al.userAccountId in (:accountIDs) order by al.userAccountId";
		Query query = getSession().createQuery( queryString );
		query.setParameterList("accountIDs", accountIDs);
		return query.list();
	}	
	
	@SuppressWarnings("unchecked")
	public List<AddressList> findAllAddressList(){
		String queryString = "select al from AddressList as al";
		Query query = getSession().createQuery( queryString );
		return (List<AddressList>)query.list();
	}
	
	public String findEmailByAccountId(String accountId){
		if (accountId == null || "".equals(accountId))  {
			return null;
		}
		String queryString = "select al.Email_ from OA_ADDRESSLIST al where al.userAccountId_=?";
		Query query = getSession().createSQLQuery(queryString);
		query.setParameter(0, accountId);
		return (String)query.uniqueResult();
	}
}