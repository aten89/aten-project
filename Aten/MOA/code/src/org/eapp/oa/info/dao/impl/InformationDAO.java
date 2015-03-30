package org.eapp.oa.info.dao.impl;

// default package

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.info.dao.IInformationDAO;
import org.eapp.oa.info.dto.InformationQueryParameters;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
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

public class InformationDAO extends BaseHibernateDAO implements IInformationDAO {

	private static final Log log = LogFactory.getLog(InformationDAO.class);
	// property constants
	public static final String SUBJECT = "subject";
	public static final String RECEIVE_FROM = "receiveFrom";

	/* (non-Javadoc)
	 */
	public Information findById(java.lang.String id) {
		log.debug("getting Information instance with id: " + id);
		try {
			Information instance = (Information) getSession().get(Information.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<Information> findByExample(Information instance) {
		log.debug("finding Information instance by example");
		try {
			List<Information> results = getSession().createCriteria("Information")
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
	public List<Information> findByProperty(String propertyName, Object value) {
		log.debug("finding Information instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Information as model where model."
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
	public List<Information> findBySubject(Object subject) {
		return findByProperty(SUBJECT, subject);
	}

	/* (non-Javadoc)
	 */
	public List<Information> findByReceiveFrom(Object receiveFrom) {
		return findByProperty(RECEIVE_FROM, receiveFrom);
	}

	/* (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	public List<Information> findAll() {
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


	/* (non-Javadoc)
	 */
	@Override
	public ListPage<Information> queryInformation(InformationQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException("非法参数:查询参数对象为null");
		}
		
		try {
			StringBuffer hql = new StringBuffer("select ol from Information as ol where ol.infoProperty <> 3 ");//未删除的
			if( qp.getInfoStatus() != null){
				hql.append(" and ol.infoStatus = :infoStatus ");
			}
			if( qp.getSubject() != null){
				hql.append(" and ol.subject like :subject ");
				qp.toArountParameter("subject");
			}
			
			if( qp.getInfoClass() != null){
				hql.append(" and ol.infoClass = :infoClass ");
			}
			
			if( qp.getInfoLayout() != null){
				hql.append(" and ol.infoLayout = :infoLayout ");
			}			
			
			if( qp.getInfoPropertys() != null){
				hql.append(" and ol.infoProperty in (:infoProperty) ");
			}		
			return new CommQuery<Information>().queryListPage(qp, qp.appendOrders(hql, "ol") ,getSession());
			
		} catch(RuntimeException re) {
			re.printStackTrace();
			return new ListPage<Information>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findAllInfoLayout() {
		try {
			String queryString = "select distinct info.infoLayout from Information as info";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
}