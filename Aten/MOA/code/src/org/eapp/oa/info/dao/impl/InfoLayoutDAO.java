package org.eapp.oa.info.dao.impl;

// default package

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.info.dao.IInfoLayoutDAO;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Archives entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Archives
 * @author MyEclipse Persistence Tools
 */

public class InfoLayoutDAO extends BaseHibernateDAO implements IInfoLayoutDAO {

	private static final Log log = LogFactory.getLog(InfoLayoutDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String SUBJECT = "subject";
	public static final String RECEIVE_FROM = "receiveFrom";


	/* (non-Javadoc)
	 */
	public InfoLayout findById(java.lang.String id) {
		log.debug("getting InfoLayout instance with id: " + id);
		try {
			InfoLayout instance = (InfoLayout) getSession().get(InfoLayout.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<InfoLayout> findByExample(InfoLayout instance) {
		log.debug("finding InfoLayout instance by example");
		try {
			List<InfoLayout> results = getSession().createCriteria("InfoLayout")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<InfoLayout> findByProperty(String propertyName, Object value) {
		log.debug("finding InfoLayout instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from InfoLayout as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	public List<InfoLayout> findBySubject(Object subject) {
		return findByProperty(SUBJECT, subject);
	}

	/* (non-Javadoc)
	 */
	public List<InfoLayout> findByReceiveFrom(Object receiveFrom) {
		return findByProperty(RECEIVE_FROM, receiveFrom);
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<InfoLayout> findAll() {
		log.debug("finding all InfoLayout instances");
		try {
			String queryString = "from InfoLayout";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	
	public List<InfoLayout> findByName(Object name){
		return findByProperty(NAME, name);
	}
	
	public int getDisplayOrder(){
		try {
			String queryString = "select max(l.displayOrder) from InfoLayout l";
			Query queryObject = getSession().createQuery(queryString);
			Integer value = (Integer)queryObject.uniqueResult();
			if(value == null){
				return 0;
			}
			return value.intValue();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InfoLayout> findAssignLayout(String userAccountId,
			List<String> groupNames, List<String> postNames, int flag, String name) {
		if (userAccountId == null) {
			throw null;
		}
		//查找有权限
		boolean isGroup = groupNames != null && groupNames.size() > 0;
		boolean isPost = postNames != null && postNames.size() > 0;
		boolean hasName = name != null;
		try {
			StringBuffer queryString = new StringBuffer("select distinct(ica.infoLayout) from InfoLayoutAssign as ica");
			queryString.append(" where ica.flag=:flag and (");
			queryString.append("(ica.type=:utype and ica.assignKey=:userAccountId)");
			if (isGroup) {
				queryString.append(" or (ica.type=:gtype and ica.assignKey in (:groupNames))");
			}
			if (isPost) {
				queryString.append(" or (ica.type=:ptype and ica.assignKey in (:postNames))");
			}
			queryString.append(")");
			if (hasName) {
				queryString.append(" and ica.infoLayout.name=:name");
			}
			
			Query query = getSession().createQuery(queryString.toString());
			query.setInteger("flag", flag);
			query.setInteger("utype", InfoLayoutAssign.TYPE_USER);
			query.setString("userAccountId", userAccountId);
			
			if (isGroup) {
				query.setInteger("gtype", InfoLayoutAssign.TYPE_GROUP);
				query.setParameterList("groupNames", groupNames);
			}
			if (isPost) {
				query.setInteger("ptype", InfoLayoutAssign.TYPE_POST);
				query.setParameterList("postNames", postNames);
			}
			if (hasName) {
				query.setString("name", name);
			}
			return query.list();
		} catch (RuntimeException re) {
			log.error("delete binding by flow_key failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InfoLayout> findByDisplayOrder() {
		log.debug("finding all InfoLayout instances");
		try {
			String queryString = "from InfoLayout l order by l.displayOrder";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
}