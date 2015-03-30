package org.eapp.dao.imp;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IActorAccountDAO;
import org.eapp.dao.param.ActorAccountQueryParameters;
import org.eapp.hbean.ActorAccount;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;

/**
 * 
 * @author zsy
 * @version
 */
public class ActorAccountDAO extends BaseHibernateDAO implements IActorAccountDAO {
	private static final Log log = LogFactory.getLog(ActorAccountDAO.class);


	@Override
	public ActorAccount findById(java.lang.String id) {
		log.debug("getting ActorAccount instance with id: " + id);
		ActorAccount instance = (ActorAccount) getSession().get(
				"org.eapp.hbean.ActorAccount", id);
		return instance;
	}

	@Override
	public ListPage<ActorAccount> queryActorAccount(ActorAccountQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select u from ActorAccount as u where u.isLogicDelete=0");
			
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
			return new CommQuery<ActorAccount>().queryListPage(qp, 
					qp.appendOrders(hql, "u" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new  ListPage<ActorAccount>();
		}
	}
	
	@Override
	public boolean testAccountID(String accountID) {
		if (StringUtils.isBlank(accountID))  {
			return false;
		}
		Query query = getSession().createQuery(
				"select count(u) from ActorAccount as u where u.accountID=:accountID and u.isLogicDelete=0");
		query.setString("accountID", accountID);
		Long c = (Long)query.uniqueResult();
		return c.longValue() == 0;
	}

	@Override
	public ActorAccount findByAccountID(String accountID) {
		if (StringUtils.isBlank(accountID))  {
			throw new IllegalArgumentException();
		}
		Query query = getSession().createQuery("from ActorAccount as u where u.accountID=:accountID and u.isLogicDelete=0");
		query.setString("accountID", accountID);
		return (ActorAccount)query.uniqueResult();

	}
}