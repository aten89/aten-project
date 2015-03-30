package org.eapp.dao.imp;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.dao.param.UserAccountQueryParameters;
import org.eapp.hbean.UserAccount;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;

/**
 * 
 * @author zsy
 * @version
 */
public class UserAccountDAO extends BaseHibernateDAO implements IUserAccountDAO {
	private static final Log log = LogFactory.getLog(UserAccountDAO.class);
	
	@Override
	public UserAccount findById(java.lang.String id) {
		log.debug("getting UserAccount instance with id: " + id);
		UserAccount instance = (UserAccount) getSession().get(
				"org.eapp.hbean.UserAccount", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccount> findAll() {
		log.debug("finding all UserAccount instances");
		String queryString = "from UserAccount as u where u.isLogicDelete=0";
		Query queryObject = getSession().createQuery(queryString);
		return queryObject.list();
	}

	@Override
	public ListPage<UserAccount> queryUserAccount(UserAccountQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer();
			if (qp.getGroupID() != null) {
				hql.append("select distinct(u) from UserAccount as u join u.groups as g " +
						"where u.isLogicDelete=0 and g.groupID=:groupID");
			} else {
				hql.append("select u from UserAccount as u where u.isLogicDelete=0");
			}
			
			//拼接查询条件
			if (qp.getAccountID() != null) {
				hql.append(" and u.accountID=:accountID");
			}
			if (qp.getDisplayName() != null) {
				hql.append(" and u.displayName like :displayName");
				qp.toArountParameter("displayName");
			}
			if (qp.getKeyword() != null) {
				hql.append(" and (u.displayName like :keyword or u.accountID like :keyword)");
				qp.toArountParameter("keyword");
			}
			if (qp.getIsLock() != null) {
				hql.append(" and u.isLock=:isLock");
			}
			if (qp.getIsValid() != null) {
				if (qp.getIsValid().booleanValue()) {
					hql.append(" and (u.invalidDate>:ctime or u.invalidDate is null)");
				} else {
					hql.append(" and u.invalidDate<:ctime");
				}
				//移除不用的参数
				qp.removeParameter("isValid");
				//添加时间参数
				qp.addParameter("ctime", new Timestamp(System.currentTimeMillis()));
			}
			return new CommQuery<UserAccount>().queryListPage(qp, 
					qp.appendOrders(hql, "u" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<UserAccount>();
		}
	}

	@Override
	public boolean testAccountID(String accountID) {
		if (StringUtils.isBlank(accountID))  {
			return false;
		}
		Query query = getSession().createQuery(
				"select count(u) from UserAccount as u where u.accountID=:accountID and u.isLogicDelete=0");
		query.setString("accountID", accountID);
		Long c = (Long)query.uniqueResult();
		return c.longValue() == 0;
	}



	@Override
	public UserAccount findByAccountID(String accountID) {
		if (StringUtils.isBlank(accountID))  {
			return null;
		}
		Query query = getSession().createQuery(
				"from UserAccount as u where u.accountID=:accountID and u.isLogicDelete=0");
		query.setString("accountID", accountID);
		query.setCacheable(true);//开启查询缓存
		return (UserAccount)query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserAccount> findByGroupIDs(List<String> groupIDs) {
		if (groupIDs == null || groupIDs.isEmpty())  {
			return null;
		}
		Query query = getSession().createQuery(
				"select distinct u from UserAccount as u join u.groups as g where u.isLogicDelete=0 and g.id in (:groupIDs)");
		query.setParameterList("groupIDs", groupIDs);
		return query.list();
	}
}