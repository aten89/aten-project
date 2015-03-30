package org.eapp.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IServiceDAO;
import org.eapp.dao.param.ServiceQueryParameters;
import org.eapp.hbean.Service;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;

/**
 * @see org.eapp.hbean.Service
 * @author MyEclipse Persistence Tools
 */

public class ServiceDAO extends BaseHibernateDAO implements IServiceDAO {
	private static final Log log = LogFactory.getLog(ServiceDAO.class);
	// property constants
	public static final String SERVICE_NAME = "serviceName";
	public static final String IS_VALID = "isValid";
	public static final String DESCRIPTION = "description";


	@Override
	public Service findById(java.lang.String id) {
		log.debug("getting Service instance with id: " + id);
		Service instance = (Service) getSession().get(
				"org.eapp.hbean.Service", id);
		return instance;
	}
	
	@Override
	public ListPage<Service> queryService(ServiceQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select s from Service as s where 1=1");
			
			//拼接查询条件
			if (qp.getServiceName() != null) {
				hql.append(" and s.serviceName like :serviceName");
				qp.toArountParameter("serviceName");
			}
			if (qp.getIsValid() != null) {
				hql.append(" and s.isValid=:isValid");
			}
			return new CommQuery<Service>().queryListPage(qp, 
					qp.appendOrders(hql, "s" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<Service>();
		}
	}
	
	@Override
	public boolean existUserAccount(Service ser) {
		Long count = (Long)getSession().createFilter(ser.getActorAccounts(), "select count(*) where isLogicDelete=0").uniqueResult();
		return count.intValue() > 0;
	}
	
	@Override
	public boolean checkRepetition(String name) {
		if(StringUtils.isBlank(name)) {
			return true;
		}
		try {
			Query query = getSession().createQuery("select count(*) from Service as s where s.serviceName=:serviceName");
			query.setString("serviceName", name);
			Long count = (Long)query.uniqueResult();
			if (count != null && count.longValue() > 0) {
				return true;
			}
		} catch (RuntimeException re) {
			log.error("attach failed", re);
		//	throw re;
		}
		return false;
	}
}