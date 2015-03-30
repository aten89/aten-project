package org.eapp.crm.blo.imp;

import org.eapp.crm.blo.ITelPartAreaBiz;
import org.eapp.crm.dao.ITelPartAreaDAO;
import org.eapp.crm.hbean.TelPartArea;

public class TelPartAreaBiz implements ITelPartAreaBiz {

    private ITelPartAreaDAO telPartAreaDAO;

	public void setTelPartAreaDAO(ITelPartAreaDAO telPartAreaDAO) {
		this.telPartAreaDAO = telPartAreaDAO;
	}

	@Override
	public TelPartArea getByTelPart(String telPart) {
		return telPartAreaDAO.findByTelPart(telPart);
	}
    
}
