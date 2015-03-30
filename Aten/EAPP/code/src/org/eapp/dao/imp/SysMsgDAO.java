package org.eapp.dao.imp;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.ISysMsgDAO;
import org.eapp.dao.param.SysMsgQueryParameters;
import org.eapp.hbean.SysMsg;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;


/**
 * 数据访问层
 * @version
 */

public class SysMsgDAO extends BaseHibernateDAO implements ISysMsgDAO {
	private static final Log log = LogFactory.getLog(SysMsgDAO.class);
	
	@Override
	public SysMsg findById(java.lang.String id) {
		log.debug("getting Action instance with id: " + id);
		SysMsg instance = (SysMsg) getSession().get("org.eapp.hbean.SysMsg", id);
		return instance;
	}
	
	@Override
	public SysMsg findLastNoView(String toAccountID) {
		log.debug("listExcludeActionByModule moduleId=" + toAccountID);
		
		Query queryObject = getSession().createQuery("from SysMsg as sm where " +
				"sm.toAccountID=:toAccountID and sm.viewFlag=:viewFlag and sm.sendTime > :sendTime order by sm.sendTime desc");
		queryObject.setString("toAccountID", toAccountID);
		queryObject.setBoolean("viewFlag", false);
		
		//只取最近15分钟内发的
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, -15);
		queryObject.setTimestamp("sendTime", new Timestamp(now.getTimeInMillis()));
		queryObject.setMaxResults(1);
		return (SysMsg)queryObject.uniqueResult();
	}

	@Override
	public ListPage<SysMsg> querySysMsgs(SysMsgQueryParameters aqp) {
		if (aqp == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select sm from SysMsg as sm where 1=1");
			if (aqp.getToAccountID() != null) {
				hql.append(" and sm.toAccountID = :toAccountID");
				aqp.toArountParameter("name");
			}
			if (aqp.getMsgSender() != null) {
				hql.append(" and sm.msgSender like :msgSender");
				aqp.toArountParameter("msgSender");
			}
			if (aqp.getViewFlag() != null) {
				hql.append(" and sm.viewFlag = :viewFlag");
			}
			if (aqp.getBeginTime() != null) {
				hql.append(" and sm.sendTime >= :beginTime");
			}
			
			if (aqp.getEndeTime() != null) {
				hql.append(" and sm.sendTime <= :endTime");
			}
			return new CommQuery<SysMsg>().queryListPage(aqp, 
					aqp.appendOrders(hql, "sm" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<SysMsg>();
		}
	}
	
}