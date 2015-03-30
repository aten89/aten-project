package org.eapp.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IActionDAO;
import org.eapp.dao.param.ActionQueryParameters;
import org.eapp.hbean.Action;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;


/**
 * 动作数据访问层
 * @version
 */

public class ActionDAO extends BaseHibernateDAO implements IActionDAO {
	private static final Log log = LogFactory.getLog(ActionDAO.class);

//	@Override
//	public void deleteById(String actionID) {
//		log.debug("deleting Action actionID");
//		Action action = this.findById(actionID);
//		if(action == null){
//			throw new IllegalArgumentException("数据库中不存在动作ID:"+actionID);
//		} else {
//			this.delete(action);
//		}
//		log.debug("delete actionID successful");	
//	}

	public Action findById(java.lang.String id) {
		log.debug("getting Action instance with id: " + id);
		Action instance = (Action) getSession().get("org.eapp.hbean.Action", id);
		return instance;
	}
	
	@Override
	public Action findByActionKey(String actionKey) {
		Query queryObject = getSession().createQuery("from Action as model where model.actionKey= :actionKey");
		queryObject.setString("actionKey", actionKey);
		queryObject.setMaxResults(1);
		return (Action)queryObject.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Action> findByName(String name) {
		Query queryObject = getSession().createQuery("from Action as model where model.name= :name");
		queryObject.setString("name", name);
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	public List<Action> findAll() {
		log.debug("finding all Action instances");
		String queryString = "from Action";
		Query queryObject = getSession().createQuery(queryString);
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Action> findExcludeActions(String moduleId, String name) {
		log.debug("listExcludeActionByModule moduleId=" + moduleId);
		String queryString = null;
		Query queryObject = null;
		if (StringUtils.isBlank(name)) {
			queryString = "from Action as ac where not exists " +
					"(from ModuleAction as ma where ac.actionID = ma.action.actionID and ma.module.moduleID = :moduleID)";
			queryObject = getSession().createQuery(queryString);
			queryObject.setString("moduleID", moduleId);
		} else {
			queryString = "from Action as ac where ac.name like :name and not exists " +
					"(from ModuleAction as ma where ac.actionID = ma.action.actionID and ma.module.moduleID = :moduleID)";
			queryObject = getSession().createQuery(queryString);
			queryObject.setString("name", "%" + name + "%");
			queryObject.setString("moduleID", moduleId);
		}
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Action> findByModuleID(String moduleID) {
		Query query = getSession().createQuery("select a from Action as a left join a.moduleActions as m where m.module.moduleID=:moduleID");
		query.setString("moduleID", moduleID);
		return query.list();
	}

	@Override
	public ListPage<Action> queryActions(ActionQueryParameters aqp) {
		if (aqp == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select action from Action as action where 1=1");
			if (aqp.getActionName() != null) {
				hql.append(" and action.name like :name");
				aqp.toArountParameter("name");
			}
			if (aqp.getActionKey() != null) {
				hql.append(" and action.actionKey like :actionKey");
				aqp.toArountParameter("actionKey");
			}
			return new CommQuery<Action>().queryListPage(aqp, 
					aqp.appendOrders(hql, "action" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<Action>();
		}
	}
	
}