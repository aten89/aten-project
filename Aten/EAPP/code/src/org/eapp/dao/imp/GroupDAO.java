package org.eapp.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IGroupDAO;
import org.eapp.dao.param.GroupQueryParameters;
import org.eapp.hbean.Group;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;


/**
 * A data access object (DAO) providing persistence and search support for
 * Group entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see org.eapp.hbean.Group
 * @author MyEclipse Persistence Tools
 */

public class GroupDAO extends BaseHibernateDAO implements IGroupDAO {
	private static final Log log = LogFactory.getLog(GroupDAO.class);
	
	@Override
	public Group findById(java.lang.String id) {
		log.debug("getting Group instance with id: " + id);
		Group instance = (Group) getSession().get(
				"org.eapp.hbean.Group", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> findByGroupName(String groupName) {
		Query queryObject = getSession().createQuery("from Group as model where model.groupName= :groupName");
		queryObject.setString("groupName", groupName);
		return queryObject.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> findRootGroups(String type) {
		log.debug("finding root Group instances");
		String queryString = "select g,g.subGroups.size from Group as g where g.parentGroup is null";
		if (StringUtils.isNotBlank(type)) {
			queryString += " and g.type=:type";
		}
		queryString += " order by g.displayOrder";
		Query queryObject = getSession().createQuery(queryString);
		if (StringUtils.isNotBlank(type)) {
			queryObject.setString("type", type);
		}
		List<Object[]> list = queryObject.list();
		List<Group> groups = new ArrayList<Group>(list.size());
		Group g = null;
		Integer count = null;
		for(Object[] o : list) {
			g = (Group)o[0];
			count = (Integer)o[1];
			g.setHasSubGroup(count != null && count.longValue() > 0);
			groups.add(g);
		}
		return groups;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> findSubGroups(String groupID, String type) {
		log.debug("finding sub Group instances");
		String queryString = "select g,g.subGroups.size from Group as g where g.parentGroup.groupID=:groupID";
		if (StringUtils.isNotBlank(type)) {
			queryString += " and g.type=:type";
		}
		queryString += " order by g.displayOrder";
		Query queryObject = getSession().createQuery(queryString).setString("groupID", groupID);
		if (StringUtils.isNotBlank(type)) {
			queryObject.setString("type", type);
		}
		List<Object[]> list = queryObject.list();
		List<Group> groups = new ArrayList<Group>(list.size());
		Group g = null;
		Integer count = null;
		for(Object[] o : list) {
			g = (Group)o[0];
			count = (Integer)o[1];
			g.setHasSubGroup(count != null && count.longValue() > 0);
			groups.add(g);
		}
		return groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupsByManagerPost(List<String> managerPostIDs) {
		if (managerPostIDs == null) {
			return null;
		}
		String queryString = "select g from Group as g where g.managerPost.postID in (:managerPostIDs)";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setParameterList("managerPostIDs", managerPostIDs);
		
		return queryObject.list();
	}

	@Override
	public ListPage<Group> queryGroup(GroupQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select g from Group as g where 1=1");
			
			//拼接查询条件
			if (qp.getGroupName() != null) {
				hql.append(" and g.groupName like :groupName");
				qp.toArountParameter("groupName");
			}
			return new CommQuery<Group>().queryListPage(qp, 
					qp.appendOrders(hql, "g" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<Group>();
		}
	}

	@Override
	public int getNextDisplayOrder(String groupID) {
		StringBuffer hql = new StringBuffer("select max(g.displayOrder) from Group as g where");
		if (StringUtils.isNotBlank(groupID)) {
			hql.append(" g.parentGroup.groupID=:groupID");
		} else {
			hql.append(" g.parentGroup is null");
		}
		Query query = getSession().createQuery(hql.toString());
		if (StringUtils.isNotBlank(groupID)) {
			query.setString("groupID", groupID);
		}
		Integer order = (Integer)query.uniqueResult();
		if(order == null) {
			return 1;
		}
		return order.intValue() + 1;
	}

	@Override
	public int getNextTreeLevel(String groupID) {
		if (StringUtils.isBlank(groupID)) {
			return 1;
		}
		Query query = getSession().createQuery("select g.treeLevel from Group as g " +
				"where g.groupID=:groupID").setString("groupID", groupID);
		Integer level = (Integer)query.uniqueResult();
		if (level == null) {
			return 1;
		}
		return level.intValue() + 1;
	}

	@Override
	public boolean existSubGroup(Group group) {
		Long count = (Long)getSession().createFilter(group.getSubGroups(), "select count(*)").uniqueResult();
		return count.intValue() > 0;
	}
	
	@Override
	public boolean existUserAccount(Group group) {
		Long count = (Long)getSession().createFilter(group.getUserAccounts(), "select count(*) where isLogicDelete=0").uniqueResult();
		return count.intValue() > 0;
	}

	@Override
	public void deleteByID(String groupID) {
		if (StringUtils.isBlank(groupID )) {
			return;
		}
		Group group = this.findById(groupID);
		if (group == null) {
			return;
		}
		//更新群组关联的职位
		Query query = getSession().createQuery("update Post as p set p.group = null " +
				"where p.group=:group");
		query.setParameter("group", group);
		query.executeUpdate();
		
		this.delete(group);
	}

	@Override
	public void saveOrder(String[] groupIDs, String parentGroupID) {
		if (groupIDs == null || groupIDs.length < 1) {
			return;
		}
		Query query = null;
		if (StringUtils.isNotBlank(parentGroupID)) {
			query = getSession().createQuery("update Group as g set g.displayOrder=:order " +
					"where g.groupID=:groupID and g.parentGroup.groupID=:parentGroupID");
			query.setString("parentGroupID", parentGroupID);
		} else {
			query = getSession().createQuery("update Group as g set g.displayOrder=:order " +
			"where g.groupID=:groupID and g.parentGroup is null");
		}
		int order = 1;
		for (int i = 0; i < groupIDs.length; i++) {
			if (StringUtils.isBlank(groupIDs[i])) {
				continue;
			}
			query.setInteger("order", order)
					.setString("groupID", groupIDs[i]);
			if(query.executeUpdate() > 0) {
				order++;
			}
		}
	}

	@Override
	public void updateOrder(String parentGroupID, Integer theOrder) {
		StringBuffer hql = new StringBuffer("update Group as g set g.displayOrder=g.displayOrder-1 " +
				"where g.displayOrder>:displayOrder");
		if (parentGroupID != null) {
			hql.append(" and g.parentGroup.groupID=:parentGroupID");
		} else {
			hql.append(" and g.parentGroup is null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setInteger("displayOrder", theOrder == null ? 0 : theOrder.intValue());
		if (parentGroupID != null) {
			query.setString("parentGroupID", parentGroupID);
		}
		query.executeUpdate();
	}

	@Override
	public boolean checkRepetition(String parentGroupID, String name) {
		if(StringUtils.isBlank(name)) {
			return true;
		}
		Query query = null;
		if (parentGroupID == null) {
			query = getSession().createQuery("select count(*) from Group as s where " +
					"s.groupName=:groupName");
		} else {
			query = getSession().createQuery("select count(*) from Group as s where " +
			"s.groupName=:groupName and s.parentGroup.groupID=:parentGroupID");
			query.setString("parentGroupID", parentGroupID);
		}
		query.setString("groupName", name);
		Long count = (Long)query.uniqueResult();
		if (count != null && count.longValue() > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> findGroupsByType(String type) {
		String sql = "select g from Group as g where g.userAccounts.size > 0";
		if (StringUtils.isNotBlank(type)) {
			sql += " and g.type=:type";
		}
		sql += " order by g.treeLevel,g.displayOrder,g.groupID";
		Query query = getSession().createQuery(sql);
		if (StringUtils.isNotBlank(type)) {
			query.setString("type", type);
		}
		return query.list();
	}

}