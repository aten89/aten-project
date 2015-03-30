package org.eapp.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IRoleDAO;
import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.hbean.Role;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;

/**
 * @author zsy
 * @version
 */
public class RoleDAO extends BaseHibernateDAO implements IRoleDAO {
	private static final Log log = LogFactory.getLog(RoleDAO.class);

	@Override
	public Role findById(java.lang.String id) {
		log.debug("getting Role instance with id: " + id);
		Role instance = (Role) getSession().get(
				"org.eapp.hbean.Role", id);
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findByRoleName(String roleName) {
		Query queryObject = getSession().createQuery("from Role as r where r.roleName= :roleName");
		queryObject.setString("roleName", roleName);
		return queryObject.list();
	}
	
	@Override
	public ListPage<Role> queryRole(RoleQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer();
			if (qp.getGroupIDs() != null && qp.getGroupIDs().size()>0) {
				hql.append("select distinct r from Role as r join r.groups as g where g.groupID in (:groupIDs)");
			} else {
				hql.append("select r from Role as r where 1=1");
				qp.setGroupIDs(null);
			}
			//拼接查询条件
			if (qp.getRoleID() != null) {
				hql.append(" and r.roleID=:roleID");
			}
			if (qp.getRoleName() != null) {
				hql.append(" and r.roleName like :roleName");
				qp.toArountParameter("roleName");
			}
			if (qp.getIsValid() != null) {
				hql.append(" and r.isValid=:isValid");
			}
			return new CommQuery<Role>().queryListPage(qp, 
					qp.appendOrders(hql, "r" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<Role>();
		}
	}
	
	@Override
	public boolean existGroup(Role role) {
		Long count = (Long)getSession().createFilter(role.getGroups(), "select count(*)").uniqueResult();
		return count.intValue() > 0;
	}
	
	@Override
	public boolean existUserAccount(Role role) {
		Long count = (Long)getSession().createFilter(role.getUserAccounts(), "select count(*)  where isLogicDelete=0").uniqueResult();
		return count.intValue() > 0;
	}

	@Override
	public boolean checkRepetition(String name) {
		if(StringUtils.isBlank(name)) {
			return true;
		}
		Query query = getSession().createQuery("select count(*) from Role as s where s.roleName=:roleName");
		query.setString("roleName", name);
		Long count = (Long)query.uniqueResult();
		if (count != null && count.longValue() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isBindToGroups(String roleID, List<String> groupIDs) {
		if (groupIDs == null || groupIDs.isEmpty()) {
			return false;
		}
		String queryString = "select count(*) from Role as r join r.groups as g where r.roleID=:roleID and g.groupID in (:groupIDs)";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("roleID", roleID);
		queryObject.setParameterList("groupIDs", groupIDs);
		Long lr = (Long)queryObject.uniqueResult();
		return lr != null && lr.longValue() > 0;
	}
}