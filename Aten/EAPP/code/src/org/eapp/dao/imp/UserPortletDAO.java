package org.eapp.dao.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IUserPortletDAO;
import org.eapp.hbean.UserPortlet;
import org.hibernate.Query;

/**
 * <p>Title:门户展示数据访问 </p>
 * @version 1.0
 */

public class UserPortletDAO extends BaseHibernateDAO implements IUserPortletDAO {
	private static final Log log = LogFactory.getLog(UserPortletDAO.class);
	
	@Override
	public int getUserCountByPortletID(String portletID){
		log.debug("getUserCountByPortletID portletID="+portletID);
		String query = "select count(*) from UserPortlet as up where up.id.portlet.portletID =:portletID";
		Query queryObject = getSession().createQuery(query);
		queryObject.setString("portletID", portletID);
		int count = ((Long) queryObject.uniqueResult()).intValue();
		log.debug("attach successful");
		return count;
	}

	@Override
	public void deleteByUser(String userID) {
		String queryString = "delete from UserPortlet as model where model.id.userAccount.userID=:userID";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setString("userID", userID);
		queryObject.executeUpdate();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<UserPortlet> findByUserID(String userID, List<String> roleIds, String subSystemID) {
		if (StringUtils.isBlank(userID) || roleIds == null || roleIds.isEmpty()) {
			return null;
		}
			
		String queryString = "select distinct(up) from UserPortlet as up join up.id.portlet.roles as upr where up.id.userAccount.userID=:userAccountID and upr.roleID in (:roleIds)";
		if (StringUtils.isNotBlank(subSystemID)) {
			queryString += " and up.id.portlet.subSystem.subSystemID=:subSystemID";
		}
		queryString += " order by up.pageContainerID,up.positionIndex";
		Query query = getSession().createQuery(queryString);
		query.setString("userAccountID", userID);
		query.setParameterList("roleIds", roleIds);
		if (StringUtils.isNotBlank(subSystemID)) {
			query.setString("subSystemID", subSystemID);
		}
		return query.list();
	}
	
}