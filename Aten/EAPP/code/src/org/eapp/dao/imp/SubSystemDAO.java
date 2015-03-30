package org.eapp.dao.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.ISubSystemDAO;
import org.eapp.hbean.SubSystem;
import org.hibernate.Query;


/**
 * 子系统数据操作层
 * @version 1.0
 */

public class SubSystemDAO extends BaseHibernateDAO implements ISubSystemDAO {
	private static final Log log = LogFactory.getLog(SubSystemDAO.class);
	
	@Override
	public SubSystem findById(String id) {
		log.debug("getting SubSystem instance with id: " + id);
		SubSystem instance = (SubSystem) getSession().get(
				"org.eapp.hbean.SubSystem", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubSystem> findByName(String name) {
		Query queryObject = getSession().createQuery("from SubSystem as model where model.name=:name");
		queryObject.setString("name", name);
		return queryObject.list();
	}

	/* (non-Javadoc)
	 * @see org.eapp.hbean.dao.ISubSystemDAO#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SubSystem> findAll() {
		log.debug("finding all SubSystem instances");
		String queryString = "from SubSystem order by displayOrder";
		Query queryObject = getSession().createQuery(queryString);
		return queryObject.list();
	}

	/**
	 * @author zsy
	 * @param roleIDs
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SubSystem> getSubSystemByRoleIDs(List<String> roleIDs) {
		if (roleIDs == null || roleIDs.size() <= 0) {
			return null;
		}
		Query query = getSession()
				.createQuery(
						"select distinct ms.module.subSystem from ModuleAction as ms join ms.roles as rr where "
								+ "ms.isValid=:isValid and rr.isValid=:isValid and rr.roleID in (:roleIDs) order by ms.module.subSystem.displayOrder")
				.setBoolean("isValid", true).setParameterList("roleIDs", roleIDs);
		return query.list();
	}

	@Override
	public void sortSubSystems(Map<String, Integer> idsort) {
		log.debug("sortSubSystems");
		String hql = "update SubSystem as sys set sys.displayOrder = :displayOrder where sys.subSystemID = :subSystemID";
		Query query = getSession().createQuery(hql);
		Set<String> ids = idsort.keySet();
		for(String id:ids){
			Integer sort = idsort.get(id);
			query.setInteger("displayOrder", sort);
			query.setString("subSystemID", id);
			query.executeUpdate();
		}
		log.debug("attach successful");
	}

	@Override
	public int getMaxOrder() {
		Query query = getSession().createQuery("select max(sys.displayOrder) from SubSystem as sys");
		Integer m = (Integer)query.uniqueResult();
		return m == null ? 0 : m.intValue();
	}
}