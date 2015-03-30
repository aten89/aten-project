package org.eapp.dao.imp;

import java.util.List;
import org.eapp.dao.IFlowHandlerDAO;
import org.eapp.dao.param.FlowHandlerQueryParameters;
import org.eapp.hbean.FlowHandler;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.eapp.util.hibernate.CommQuery;


/**
 * 动作数据访问层
 * @version
 */

public class FlowHandlerDAO extends BaseHibernateDAO implements IFlowHandlerDAO {

	@Override
	public FlowHandler findById(java.lang.String id) {
		FlowHandler instance = (FlowHandler) getSession().get("org.eapp.hbean.FlowHandler", id);
		return instance;
	}
	
	@Override
	public ListPage<FlowHandler> queryFlowHandlers(FlowHandlerQueryParameters aqp) {
		if (aqp == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select fh from FlowHandler as fh where 1=1");
			if (aqp.getName() != null) {
				hql.append(" and fh.name like :name");
				aqp.toArountParameter("name");
			}
			if (aqp.getFlowClass() != null) {
				hql.append(" and fh.flowClass = :flowClass");
			}
			if (aqp.getGlobalFlag() != null) {
				hql.append(" and fh.globalFlag = :globalFlag");
			}
			return new CommQuery<FlowHandler>().queryListPage(aqp, 
					aqp.appendOrders(hql, "fh" ), getSession());
		} catch (RuntimeException re) {
			return new ListPage<FlowHandler>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FlowHandler> findFlowHandlers(String flowClass, String handlerType) {
		Query query = getSession().createQuery(
				"from FlowHandler as fh where fh.type=:type and (fh.flowClass=:flowClass or fh.globalFlag=:globalFlag) order by fh.globalFlag, fh.name");
		query.setString("flowClass", flowClass);
		query.setString("type", handlerType);
		query.setBoolean("globalFlag", true);
		return query.list();
	}
	
	@Override
	public boolean testHandlerClassRepeat(String handlerClass, String flowClass, boolean globalFlag, String handId) {
		String hql = "select count(fh) from FlowHandler as fh where fh.handlerClass=:handlerClass";
		if (handId != null) {
			hql += " and fh.handId <> :handId";
		}
		//全局类型，所有不能重复
		if (!globalFlag) {
			hql += " and (fh.globalFlag=:globalFalse and fh.flowClass=:flowClass or fh.globalFlag=:globalTrue)";
		}
		Query query = getSession().createQuery(hql);
		query.setString("handlerClass", handlerClass);
		if (handId != null) {
			query.setString("handId", handId);
		}
		if (!globalFlag) {
			query.setBoolean("globalFalse", false);
			query.setBoolean("globalTrue", true);
			query.setString("flowClass", flowClass);
		}
		Number count = (Number)query.uniqueResult();
		return count != null && count.intValue() > 0;
    }
}