package org.eapp.dao.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IModuleDAO;
import org.eapp.hbean.Module;
import org.hibernate.Query;

/**
 * 模块数据访问层
 * @version
 */

public class ModuleDAO extends BaseHibernateDAO implements IModuleDAO {
	private static final Log log = LogFactory.getLog(ModuleDAO.class);

	@Override
	public void updateModuleSort(Map<String,Integer> idSorts) {
		log.debug("updateModuleSort");
		Set<String> ids = idSorts.keySet(); 
		for(String moduleId:ids){
			if(StringUtils.isBlank(moduleId)){
				throw new IllegalArgumentException("参数中含有空的模块ID");
			}
			Module module = this.findById(moduleId);
			if(module == null){
				throw new IllegalArgumentException("参数中含无效的模块ID");
			}
			Integer sort = idSorts.get(moduleId);
			module.setDisplayOrder(sort);
			this.saveOrUpdate(module);
		}
	}

	@Override
	public int getChildModulesCountByModuleID(String moduleId) {
		log.debug("getModuleNumberBySubSystem");
		String queryString = "select count(*) from Module where parentModule.moduleID = :moduleID";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("moduleID", moduleId);
		Long count = (Long) queryObject.uniqueResult();
		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Module> getChildModulesByASC(String fatherId) {
		log.debug("finding Module instance with property: parentModuleID" + ", value: " + fatherId);
		List<Module> childModules = null;
		String queryString = "from Module as model where model.parentModule.moduleID = :moduleID order by displayOrder";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("moduleID", fatherId);
		childModules = queryObject.list();
		return childModules;
	}

	@Override
	public int getModuleCountBySubSystemID(String subSystemId) {
		log.debug("getModuleNumberBySubSystem");
		String queryString = "select count(*) from Module where subSystem.subSystemID = :subSystemID and parentModule.moduleID is null";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("subSystemID", subSystemId);
		Long count = (Long) queryObject.uniqueResult();
		return count.intValue();
	}
	
	@Override
	public int getNextDisplayOrder(String subSystemID, String moduleID) {
		StringBuffer hql = new StringBuffer("select max(g.displayOrder) from Module as g where g.subSystem.subSystemID=:subSystemID");
		if (StringUtils.isNotBlank(moduleID)) {
			hql.append(" and g.parentModule.moduleID=:moduleID");
		} else {
			hql.append(" and g.parentModule is null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setString("subSystemID", subSystemID);
		if (StringUtils.isNotBlank(moduleID)) {
			query.setString("moduleID", moduleID);
		}
		Integer order = (Integer)query.uniqueResult();
		if(order == null) {
			return 1;
		}
		return order.intValue() + 1;
	}
	
	@Override
	public int getNextTreeLevel(String subSystemID, String moduleID) {
		if (StringUtils.isBlank(moduleID)) {
			return 1;
		}
		Query query = getSession().createQuery("select g.treeLevel from Module as g " +
				"where g.subSystem.subSystemID=:subSystemID and g.moduleID=:moduleID");
		query.setString("subSystemID", subSystemID);
		query.setString("moduleID", moduleID);
		Integer level = (Integer)query.uniqueResult();
		if (level == null) {
			return 1;
		}
		return level.intValue() + 1;
	}

	@Override
	public void updateOrder(String parentModuleID, Integer theOrder) {
		StringBuffer hql = new StringBuffer("update Module as g set g.displayOrder=g.displayOrder-1 " +
				"where g.displayOrder>:displayOrder");
		if (StringUtils.isNotBlank(parentModuleID)) {
			hql.append(" and g.parentModule.moduleID=:parentModuleID");
		} else {
			hql.append(" and g.parentModule is null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setInteger("displayOrder", theOrder == null ? 0 : theOrder.intValue());
		if (StringUtils.isNotBlank(parentModuleID)) {
			query.setString("parentModuleID", parentModuleID);
		}
		query.executeUpdate();
	}

	
	@Override
	public Module findById(java.lang.String id) {
		log.debug("getting Module instance with id: " + id);
		Module instance = (Module) getSession().get("org.eapp.hbean.Module", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Module> findByParentModuleID(String parentModuleID) {
		Query queryObject = getSession().createQuery("from Module as model where model.parentModule.moduleID= :moduleID");
		queryObject.setString("moduleID", parentModuleID);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Module> findBySubSystemID(String subSystemID) {
		Query queryObject = getSession().createQuery("from Module as model where model.subSystem.subSystemID= :subSystemID");
		queryObject.setString("subSystemID", subSystemID);
		return queryObject.list();
	}
	
	@Override
	public boolean isKeyRepeat(String subSystemId, String key){
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(key)) {
			return true;
		}
		try {
			Query query = getSession().createQuery(
					"select count(m) from Module as m where m.subSystem.subSystemID=:systemID and m.moduleKey=:key");
			query.setString("systemID", subSystemId);
			query.setString("key", key);
			Long count = (Long)query.uniqueResult();
			if (count != null && count.longValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			log.error("select module fail", e);
		}
		return false;
	}

	/**
	 * @author zsy
	 * @param systemID 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Module> findRootModulesBySysID(String systemID) {
		if (StringUtils.isBlank(systemID )) {
			return null;
		}
		Query query = getSession().createQuery(
				"from Module as m where m.subSystem.subSystemID=:systemID and m.parentModule.moduleID is null order by displayOrder");
		query.setString("systemID", systemID);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Module> findModulesBySysID(String systemID) {
		if (StringUtils.isBlank(systemID)) {
			return null;
		}
		Query query = getSession().createQuery(
				"from Module as m where m.subSystem.subSystemID=:systemID and m.quoteModule is null");
		query.setString("systemID", systemID);
		return query.list();
	}
	
	/**
	 * @author zsy
	 * 
	 * @param roleIDs
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Module> findModuleByRoleIDs(List<String> roleIDs, String systemID) {
		if (roleIDs == null || roleIDs.isEmpty()) {
			return null;
		}
		Query query = getSession()
				.createQuery(
						"select distinct ms.module from ModuleAction as ms join ms.roles as rr " +
						"where ms.isValid=:isValid and ms.module.subSystem.subSystemID=:systemID and " +
						"rr.isValid=:isValid and rr.roleID in (:roleIDs) and ms.module.quoteModule is null")
				.setBoolean("isValid", true).setString("systemID", systemID)
				.setParameterList("roleIDs", roleIDs);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Module> findQuoteModules(String systemID) {
		String queryString =  "select m from Module as m where m.quoteModule is not null";
		if (StringUtils.isNotBlank(systemID)) {
			queryString += " and m.subSystem.subSystemID=:systemID";
		}
		Query query = getSession().createQuery(queryString);
		if (StringUtils.isNotBlank(systemID)) {
			query.setString("systemID", systemID);
		}
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Module> queryModuleByName(String subSystemID, String name) {
		Query query = getSession().createQuery(
				"from Module as m where m.subSystem.subSystemID =:subSystemID and m.name like :name");
		query.setString("subSystemID", subSystemID);
		query.setString("name", "%"+name+"%");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Module> findModulesBySystemIDModuleKey(String systemID, String moduleKey) {
		Query query = getSession().createQuery(
				"from Module as m where m.subSystem.subSystemID =:subSystemID and m.parentModule.moduleKey = :moduleKey");
		query.setString("subSystemID", systemID);
		query.setString("moduleKey", moduleKey);
		return query.list();
	}
	
	@Override
	public boolean isNameRepeat(String subSystemId, String parentModuleId, String name) {
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(name)) {
			return true;
		}
		Query query = getSession().createQuery(
				"select count(m) from Module as m where m.subSystem.subSystemID =:subSystemID " +
				"and m.parentModule.moduleID=:parentModuleID and m.name=:name");
		query.setString("subSystemID", subSystemId);
		query.setString("parentModuleID", parentModuleId);
		query.setString("name", name.trim());
		Long count = (Long)query.uniqueResult();
		if (count != null && count.longValue() > 0) {
			return true;
		}

		return false;
	}
	
	@Override
	public Module findModuleByModuleKey(String subSystemId,String key){
		if (StringUtils.isBlank(subSystemId) || StringUtils.isBlank(key)) {
			return null;
		}
		Query query = getSession().createQuery(
				"from Module as m where m.subSystem.subSystemID=:systemID and m.moduleKey=:key");
		query.setString("systemID", subSystemId);
		query.setString("key", key);
		query.setMaxResults(1);
		return (Module)query.uniqueResult();
//		List<Module> l = query.list();
//		if (l != null && l.size() > 0) {
//			return l.get(0);
//		}

	}
}