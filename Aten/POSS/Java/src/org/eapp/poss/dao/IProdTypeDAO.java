package org.eapp.poss.dao;

import java.util.List;

import org.eapp.poss.hbean.ProdType;

public interface IProdTypeDAO extends IBaseHibernateDAO {
	
	ProdType findByID(String id);
	
	List<ProdType> findRootProdTypes();
	
	List<ProdType> findSubProdTypes(String parentID);

	boolean checkRepetition(String prodType, String parentID);

	boolean existsSubProdTypes(ProdType prodTypeEntity);

	void saveOrder(String parentID, String[] orderSubIDs);
}
