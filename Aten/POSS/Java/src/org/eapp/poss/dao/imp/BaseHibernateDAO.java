package org.eapp.poss.dao.imp;
// default package


import java.io.Serializable;

import org.eapp.poss.dao.IBaseHibernateDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * 
 * @author zsy
 *
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void delete(Object transientInstance) {
		getSession().delete(transientInstance);
	}

	@Override
	public Serializable save(Object persistentInstance) {
		return getSession().save(persistentInstance);
	}
	
	@Override
	public void persist(Object transientInstance) {
		getSession().persist(transientInstance);
	}

	@Override
	public void saveOrUpdate(Object instance) {
		getSession().saveOrUpdate(instance);
	}

	@Override
	public void update(Object instance) {
		getSession().update(instance);
	}
	
	@Override
	public Object merge(Object instance) {
		return getSession().merge(instance);
	}
	
}