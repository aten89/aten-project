package org.eapp.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IPortletDAO;
import org.eapp.dao.param.PortletQueryParameters;
import org.eapp.hbean.DefaultPortlet;
import org.eapp.hbean.Portlet;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

/**
 * @version 1.0
 */

public class PortletDAO extends BaseHibernateDAO implements IPortletDAO {
	private static final Log log = LogFactory.getLog(PortletDAO.class);

	@Override
	public Portlet findById(java.lang.String id) {
		log.debug("getting Portlet instance with id: " + id);
		Portlet instance = (Portlet) getSession().get(
				"org.eapp.hbean.Portlet", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Portlet> findByPortletName(String portletName) {
		Query queryObject = getSession().createQuery("from Portlet as model where model.portletName= :portletName");
		queryObject.setString("portletName", portletName);
		return queryObject.list();
	}

	@Override
	public ListPage<Portlet> queryPortlets(PortletQueryParameters queryParameters) {
		if (queryParameters == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select portlet from Portlet as portlet where 1=1");
			if(queryParameters.getPortletName() != null){
				hql.append(" and portlet.portletName like :portletName");
				queryParameters.toArountParameter("portletName");
			}
			return new CommQuery<Portlet>().queryListPage(queryParameters, 
					queryParameters.appendOrders(hql, "portlet" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<Portlet>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Portlet> findUseablePortletsByUser(String userID) {
		log.debug("findUseablePortletsByUser");
		String queryString = "from Portlet as pt where not exists (from UserPortlet as up where pt.portletID = up.id.portletID and up.id.userAccountID = :userAccountID)";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("userAccountID",userID);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Portlet> findDefaultPortlets() {
		String queryString = "select up.portlet from DefaultPortlet as up order by up.pageContainerID,up.positionIndex";
		Query query = getSession().createQuery(queryString);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Portlet> findDefaultPortlets(List<String> roleIds, String subSystemID) {
		String queryString = "select distinct(up) from DefaultPortlet as up join up.portlet.roles as r where r.roleID in (:roleIds) ";
		if (StringUtils.isNotBlank(subSystemID)) {
			queryString += " and up.portlet.subSystem.subSystemID=:subSystemID";
		}
		queryString += " order by up.pageContainerID,up.positionIndex";
		Query query = getSession().createQuery(queryString);
		query.setParameterList("roleIds", roleIds);
		if (StringUtils.isNotBlank(subSystemID)) {
			query.setString("subSystemID", subSystemID);
		}
		
		List<DefaultPortlet> dps = query.list();
		List<Portlet> ps = new ArrayList<Portlet>();
		if (dps != null) {
			for (DefaultPortlet dp : dps) {
				ps.add(dp.getPortlet());
			}
		}
		return ps;
	}
	
	@Override
	public void delAllDefaultPortlets() {
		log.debug("deleting Portlet instance");
		Query query = getSession().createQuery("delete DefaultPortlet");
		query.executeUpdate();
		log.debug("delete successful");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Portlet> queryPortlets(List<String> roleIds) {
		if (roleIds == null || roleIds.isEmpty()) {
			return null;
		}
		Query query = getSession().createQuery("select distinct(p) from Portlet as p join p.roles as r where r.roleID in (:roleIds)");
		query.setParameterList("roleIds", roleIds);
		return query.list();
	}

}