package org.eapp.crm.dao;

import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.TelPartArea;

public interface ITelPartAreaDAO extends IBaseHibernateDAO {
    
    /**
     * 通过电话号段获取归属信息
     * @throws CrmException 异常
     */
    public TelPartArea findByTelPart(String telPart);

}
