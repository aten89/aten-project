package org.eapp.crm.blo;

import org.eapp.crm.hbean.TelPartArea;

public interface ITelPartAreaBiz {

	/**
     * 通过电话号段获取归属信息
     * @throws CrmException 异常
     */
    public TelPartArea getByTelPart(String telPart);
}
