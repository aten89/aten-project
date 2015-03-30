package org.eapp.poss.blo;

import java.util.List;

import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdType;

public interface IProdTypeBiz {

	ProdType getProdTypeByID(String id);

	List<ProdType> getSubProdTypes(String id);

	ProdType addProdType(String parentID, String prodType, String remark) throws PossException;

	ProdType modifyProdType(String prodTypeID, String parentID,
			String prodType, String remark) throws PossException;

	void deleteProdType(String prodTypeID) throws PossException;

	void modifyOrder(String parentID, String[] orderSubIDs);

}
