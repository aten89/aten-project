package org.eapp.crm.dao.imp;

import org.eapp.crm.dao.ITelPartAreaDAO;
import org.eapp.crm.hbean.TelPartArea;
import org.hibernate.Query;

public class TelPartAreaDAO extends BaseHibernateDAO implements ITelPartAreaDAO {
    
    @Override
    public TelPartArea findByTelPart(String telPart) {
    	Query query = getSession().createQuery("from TelPartArea as ta where ta.telPart=:telPart");
		query.setString("telPart", telPart);
		query.setMaxResults(1);
		return (TelPartArea)query.uniqueResult();
    }
}
