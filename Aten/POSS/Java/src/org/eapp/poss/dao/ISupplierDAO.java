package org.eapp.poss.dao;

import org.eapp.poss.dao.param.SupplierQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Supplier;
import org.eapp.util.hibernate.ListPage;

public interface ISupplierDAO extends IBaseHibernateDAO {
	
	Supplier findById(String id) throws PossException;
	
	ListPage<Supplier> querySupplierListPage(SupplierQueryParameters qp) throws PossException;
	
}
