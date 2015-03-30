package org.eapp.poss.blo;

import org.eapp.poss.dao.param.SupplierQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Supplier;
import org.eapp.util.hibernate.ListPage;

public interface ISupplierBiz {

	void addSupplier(String supplier, String remark) throws PossException;

	ListPage<Supplier> querySupplierListPage(SupplierQueryParameters qp) throws PossException;

	void modifySupplier(String id, String supplier, String remark) throws PossException;

	void deleteSupplier(String id) throws PossException;
}
