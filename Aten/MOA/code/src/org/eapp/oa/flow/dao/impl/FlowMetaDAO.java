package org.eapp.oa.flow.dao.impl;

import java.util.List;

import org.eapp.oa.flow.dao.IFlowMetaDAO;
import org.eapp.oa.flow.hbean.FlowMeta;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 * A data access object (DAO) providing persistence and search support for
 * FlowMeta entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FlowMetaDAO extends BaseHibernateDAO implements IFlowMetaDAO {
	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(FlowMetaDAO.class);
	
	public static final String FLOW_CATEGORY = "flowCategory";

	@Override
	public FlowMeta findById(java.lang.String id) {
		log.debug("getting FlowMeta instance with id: " + id);
		FlowMeta instance = (FlowMeta) getSession().get(
				FlowMeta.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	private List<FlowMeta> findByProperty(String propertyName, Object value) {
		log.debug("finding FlowMeta instance with property: " + propertyName
				+ ", value: " + value);
	
		String queryString = "from FlowMeta as model where model."
				+ propertyName + "= ?";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setParameter(0, value);
		return queryObject.list();
	}
	
	@Override
	public List<FlowMeta> findByFlowCategory(Object flowCategory) {
		return findByProperty(FLOW_CATEGORY, flowCategory);
	}
}