/**
 * 
 */
package org.eapp.sys.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 生成UUID+当年年分的ID
 * @author zsy
 * @version
 */
public class LogIDGenerator implements IdentifierGenerator {

	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR) + uuid;
	}

}
