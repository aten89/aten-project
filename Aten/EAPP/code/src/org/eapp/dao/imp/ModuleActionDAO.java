package org.eapp.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IModuleActionDAO;
import org.eapp.dao.param.ModuleActionQueryParameters;
import org.eapp.dto.ActionKey;
import org.eapp.hbean.ModuleAction;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

/**
 * 
 * @author zsy
 * @version
 */
public class ModuleActionDAO extends BaseHibernateDAO implements IModuleActionDAO {
	private static final Log log = LogFactory.getLog(ModuleActionDAO.class);
	
	@Override
	public ModuleAction findById(java.lang.String id) {
		log.debug("getting ModuleAction instance with id: " + id);
		ModuleAction instance = (ModuleAction) getSession().get(
				"org.eapp.hbean.ModuleAction", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ModuleAction> findByModuleID(String moduleID) {
		Query queryObject = getSession().createQuery("from ModuleAction as model where model.module.moduleID= :moduleID");
		queryObject.setString("moduleID", moduleID);
		return queryObject.list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void deleteByIds(String moduleID, List<String> ids) {
		log.debug("deleting ModuleAction instances ");
		if (StringUtils.isBlank(moduleID) || ids == null || ids.isEmpty()) {
			return;
		}
		
		String queryString = "from ModuleAction as model where model.module.moduleID=:moduleID and model.action.actionID in (:actionIDs)";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("moduleID", moduleID);
		queryObject.setParameterList("actionIDs", ids);
		List<ModuleAction> mas = queryObject.list();
		for (ModuleAction ma : mas){
			this.delete(ma);
		}
	}

	@Override
	public void setValid(String moduleID, String[] moduleActionIDs) {
		// 该模块的模块动作设为无效
		Query query = getSession().createQuery(
				"update ModuleAction as ma set ma.isValid=:isValid "
						+ "where ma.module.moduleID=:moduleID and ma.isValid!=:isValid").setBoolean(
				"isValid", false).setString("moduleID", moduleID);
		query.executeUpdate();
		//为空所有设为无效
		if (moduleActionIDs == null || moduleActionIDs.length <= 0) {
			return;
		}
		// 设为有效
		query = getSession()
				.createQuery(
						"update ModuleAction as ma set ma.isValid=:isValid "
								+ "where ma.module.moduleID=:moduleID and ma.moduleActionID in (:moduleActionID)")
				.setBoolean("isValid", true).setString("moduleID", moduleID).setParameterList(
						"moduleActionID", moduleActionIDs);
		query.executeUpdate();
	}

	@Override
	public void setRPC(String moduleID, String[] moduleActionIDs) {
		// 该模块其余的设为非服务
		Query query = getSession().createQuery(
				"update ModuleAction as ma set ma.isRPC=:isRPC "
						+ "where ma.module.moduleID=:moduleID and ma.isRPC!=:isRPC)").setBoolean(
				"isRPC", false).setString("moduleID", moduleID);
		query.executeUpdate();
		
		//为空所有设为无效
		if (moduleActionIDs == null || moduleActionIDs.length <= 0) {
			return;
		}
		// 设为服务
		query = getSession()
				.createQuery(
						"update ModuleAction as ma set ma.isRPC=:isRPC "
								+ "where ma.module.moduleID=:moduleID and ma.moduleActionID in (:moduleActionID)")
				.setBoolean("isRPC", true).setString("moduleID", moduleID).setParameterList(
						"moduleActionID", moduleActionIDs);
		query.executeUpdate();
	}
	
	@Override
	public void setHTTP(String moduleID, String[] moduleActionIDs) {
		// 该模块其余的设为非服务
		Query query = getSession().createQuery(
				"update ModuleAction as ma set ma.isHTTP=:isHTTP "
						+ "where ma.module.moduleID=:moduleID and ma.isHTTP!=:isHTTP)").setBoolean(
				"isHTTP", false).setString("moduleID", moduleID);
		query.executeUpdate();
		
		//为空所有设为无效
		if (moduleActionIDs == null || moduleActionIDs.length <= 0) {
			return;
		}
		// 设为服务
		query = getSession()
				.createQuery(
						"update ModuleAction as ma set ma.isHTTP=:isHTTP "
								+ "where ma.module.moduleID=:moduleID and ma.moduleActionID in (:moduleActionID)")
				.setBoolean("isHTTP", true).setString("moduleID", moduleID).setParameterList(
						"moduleActionID", moduleActionIDs);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionKey> getModuleActionByRoleIDs(List<String> roleIDs, String moduleID) {
		if (roleIDs == null || roleIDs.isEmpty() || StringUtils.isBlank(moduleID)) {
			return null;
		}

		Query query = getSession()
				.createQuery(
						"select distinct new org.eapp.dto.ActionKey"
								+ "(ms.action.actionID, ms.actionKey) from ModuleAction as ms join ms.roles as rr where "
								+ "ms.module.moduleID=:moduleID and ms.isValid=:isValid and ms.isHTTP=:isHTTP and rr.roleID in (:roleIDs)")
				.setString("moduleID", moduleID).setBoolean("isValid", true).setBoolean(
						"isHTTP", true).setParameterList("roleIDs", roleIDs);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionKey> getModuleActionByServiceIDs(List<String> serviceIDs, String moduleID) {
		if (serviceIDs == null || serviceIDs.isEmpty() || StringUtils.isBlank(moduleID)) {
			return null;
		}

		Query query = getSession()
				.createQuery(
						"select distinct new org.eapp.dto.ActionKey"
								+ "(ms.action.actionID, ms.actionKey) from ModuleAction as ms join ms.services as ss where "
								+ "ms.module.moduleID=:moduleID and ms.isValid=:isValid and ms.isRPC=:isRPC and ss.serviceID in (:serviceIDs)")
				.setString("moduleID", moduleID).setBoolean("isValid", true).setBoolean(
						"isRPC", true).setParameterList("serviceIDs", serviceIDs);
		return query.list();
	}

	@Override
	public ListPage<ModuleAction> queryModuleAction(ModuleActionQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select ma from ModuleAction as ma where 1=1");
			
			// 拼接查询条件
			if (qp.getModuleID() != null) {
				hql.append(" and ma.module.moduleID=:moduleID");
			}
			if (qp.getIsRPC() != null) {
				hql.append(" and ma.isRPC=:isRPC");
			}
			if (qp.getIsHTTP() != null) {
				hql.append(" and ma.isHTTP=:isHTTP");
			}
			if (qp.getIsValid() != null) {
				hql.append(" and ma.isValid=:isValid");
			}
			return new CommQuery<ModuleAction>().queryListPage(qp, 
					qp.appendOrders(hql, "ma" ), getSession());
			
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<ModuleAction>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ActionKey> getModuleActionByModuleID(String moduleID) {
		if (StringUtils.isBlank(moduleID)) {
			return null;
		}
		
		Query query = getSession().createQuery(
				"select new org.eapp.dto.ActionKey(ma.action.actionID, ma.actionKey) " +
				"from ModuleAction as ma where ma.isValid=:isValid and ma.module.moduleID=:moduleID ");
		query.setBoolean("isValid", true);
		query.setString("moduleID", moduleID);
		return query.list();
	}

	@Override
	public String getModuleID(String systemID, String moduleKey) {
		if (StringUtils.isBlank(moduleKey) || StringUtils.isBlank(systemID)) {
			return null;
		}
		Query query = getSession().createQuery(
				"select m.moduleID from Module as m where m.moduleKey=:moduleKey and " +
				"m.subSystem.subSystemID=:systemID ");
		query.setString("moduleKey", moduleKey);
		query.setString("systemID", systemID);
		return (String)query.uniqueResult();
	}
}