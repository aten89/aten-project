package org.eapp.dao.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IShortCutMenuDAO;
import org.eapp.hbean.ShortCutMenu;
import org.hibernate.Query;

/**
 * <p>Title:快捷方式数据访问实现 </p>
 * @version 1.0
 */

public class ShortCutMenuDAO extends BaseHibernateDAO implements IShortCutMenuDAO {
	private static final Log log = LogFactory.getLog(ShortCutMenuDAO.class);

	@Override
	public ShortCutMenu findById(String id) {
		log.debug("getting ShortCutMenu instance with id: " + id);
		ShortCutMenu instance = (ShortCutMenu) getSession().get(
				"org.eapp.hbean.ShortCutMenu", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShortCutMenu> findByUserID(String userID) {
		Query queryObject = getSession().createQuery("from ShortCutMenu as model where model.userAccount.userID= :userID order by model.displayOrder");
		queryObject.setString("userID", userID);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShortCutMenu> findByType(String type) {
		Query queryObject = getSession().createQuery("from ShortCutMenu as model where model.type= :type order by model.displayOrder");
		queryObject.setString("type", type);
		return queryObject.list();
	}
	
//	@Override
//	public ListPage<ShortCutMenu> queryShortCutMenus(QueryParameters qp) {
//		if (qp == null) {
//			throw new IllegalArgumentException();
//		}
//		try {
//			StringBuffer hql = new StringBuffer("select sm from ShortCutMenu as sm where 1=1");
//			if (qp.getParameter("type") != null) {
//				hql.append(" and sm.type=:type");
//
//			}
//			return new CommQuery<ShortCutMenu>().queryListPage(qp, 
//					qp.appendOrders(hql, "sm" ), getSession());
//		} catch (RuntimeException re) {
//			log.error("query listPage error",re);
//			return new ListPage<ShortCutMenu>();
//		}
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShortCutMenu> findEnableByType(String type) {
		String queryString = "from ShortCutMenu as scm where scm.type=:type and scm.isValid=:isValid order by scm.displayOrder";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("type", type);
		queryObject.setBoolean("isValid", Boolean.TRUE);
		return queryObject.list();
	}

	@Override
	public void sortShortCutMenus(Map<String, Integer> idsort) {
		log.debug("sortShortCutMenus");
		String hql = "update ShortCutMenu as scm set scm.displayOrder = :displayOrder where scm.shortCutMenuID = :shortCutMenuID";
		Query query = getSession().createQuery(hql);
		Set<String> ids = idsort.keySet();
		for(String id:ids){
			Integer sort = idsort.get(id);
			query.setInteger("displayOrder", sort);
			query.setString("shortCutMenuID", id);
			query.executeUpdate();
		}
		log.debug("attach successful");
	}
	
	@Override
	public void sortShortCutMenus(Map<String, Integer> idsort,Boolean isValid) {
		log.debug("sortShortCutMenus");
		String hql = "update ShortCutMenu as scm set scm.displayOrder = :displayOrder,scm.isValid =:isValid where scm.shortCutMenuID = :shortCutMenuID";
		Query query = getSession().createQuery(hql);
		Set<String> ids = idsort.keySet();
		for(String id:ids){
			Integer sort = idsort.get(id);
			if(sort == null){
				throw new IllegalArgumentException("排列顺序不能为空");
			}
			query.setInteger("displayOrder", sort);
			query.setBoolean("isValid", isValid);
			query.setString("shortCutMenuID", id);
			query.executeUpdate();
		}
		log.debug("attach successful");
	}

	@Override
	public int getMaxDisplayOrderByUserID(String userID) {
		log.debug("deleting ShortCutMenu shortCutMenuID");
		String queryString = "select max(displayOrder) from ShortCutMenu where userAccount.userID = :userID";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("userID", userID);
		Integer o = (Integer) queryObject.uniqueResult();
		if (o == null) {
			return 0;
		} else {
			return o.intValue();
		}
	}


	@Override
	public boolean isShortCutMenuRepeat(String userid, String type, String menuTitle) {
		log.debug("finding all ShortCutMenu instances");
		String queryString = "select count(scm) from ShortCutMenu as scm where scm.userAccount.userID =:userid and scm.type=:type and scm.menuTitle =:menuTitle";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setParameter("userid", userid);
		queryObject.setParameter("type", type);
		queryObject.setParameter("menuTitle", menuTitle);
		Long shortCutNums = (Long)queryObject.uniqueResult();
		return shortCutNums != null && shortCutNums > 0;
	}
}