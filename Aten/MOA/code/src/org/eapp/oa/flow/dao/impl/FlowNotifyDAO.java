package org.eapp.oa.flow.dao.impl;

import java.util.List;

import org.eapp.oa.flow.dao.IFlowNotifyDAO;
import org.eapp.oa.flow.dto.FlowNotifyQueryParameters;
import org.eapp.oa.flow.hbean.FlowNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

/**
 * 
 */

public class FlowNotifyDAO extends BaseHibernateDAO implements IFlowNotifyDAO {
	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(FlowNotifyDAO.class);

	@Override
	public FlowNotify findById(java.lang.String id) {
		log.debug("getting FlowNotify instance with id: " + id);
		FlowNotify instance = (FlowNotify) getSession().get(FlowNotify.class, id);
		return instance;
	}

	@Override
	public ListPage<FlowNotify> queryUserNotify(FlowNotifyQueryParameters parms, String userAccountId) {
		if (parms == null)  {
			throw new IllegalArgumentException("非法参数:查询参数对象为null");
		}
		StringBuffer hql = new StringBuffer("from FlowNotify as fn where fn.notifyUser=:notifyUser and fn.status > 0");
		try {
			parms.addParameter("notifyUser", userAccountId);
			if (parms.getSubject() != null) {
				hql.append(" and fn.subject like :subject");
				parms.toArountParameter("subject");
			}
			if (parms.getCreator() != null) {
				hql.append(" and fn.creator = :creator");
			}
			if (parms.getFlowClass() != null) {
				hql.append(" and fn.flowClass = :flowClass");
			}
			if (parms.getFlowStatus() != null) {
				hql.append(" and fn.flowStatus = :flowStatus");
			}
			if (parms.getBeginNotifyTime() != null) {
				hql.append(" and fn.notifyTime >= :beginNotifyTime");
			}
			if (parms.getEndNotifyTime() != null) {
				hql.append(" and fn.notifyTime <= :endNotifyTime");
			}
			
			return new CommQuery<FlowNotify>().queryListPage(parms, parms.appendOrders(hql, "fn" ), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error",re);
			return new ListPage<FlowNotify>();
		}
	}

	@Override
	public void updateNotifyStatus(String refFormID, int flowStatus) {
//		//流程结束后知会状态修改
//		Query query = getSession().createQuery("update FlowNotify as fn set fn.status=:status, " +
//				"fn.notifyTime=:notifyTime where fn.refFormID=:refFormID and fn.notifyType=:notifyType");
//		query.setInteger("status", FlowNotify.STATUS_NOTIFY);
//		query.setTimestamp("notifyTime", new Date());
//		query.setString("refFormID", refFormID);
//		query.setInteger("notifyType", FlowNotify.NOTIFYTYPE_END);
//		query.executeUpdate();
		
		//该表单所有流程状态更新
		Query query = getSession().createQuery("update FlowNotify as fn set fn.flowStatus=:flowStatus " +
				"where fn.refFormID=:refFormID");
		query.setInteger("flowStatus", flowStatus);
		query.setString("refFormID", refFormID);
		query.executeUpdate();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FlowNotify> findEndNotify(String refFormID) {
		Query query = getSession().createQuery("from FlowNotify as fn where fn.refFormID=:refFormID and fn.notifyType=:notifyType");
		query.setString("refFormID", refFormID);
		query.setInteger("notifyType", FlowNotify.NOTIFYTYPE_END);
		return query.list();
	}
}