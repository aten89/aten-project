package org.eapp.dao.imp;

import java.util.List;

import org.eapp.dao.IFlowVarDAO;
import org.eapp.dao.param.FlowVarQueryParameters;
import org.eapp.hbean.FlowVar;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

import org.eapp.util.hibernate.CommQuery;

/**
 * 数据访问层
 * @version
 */
public class FlowVarDAO extends BaseHibernateDAO implements IFlowVarDAO {

	@Override
	public FlowVar findById(String id) {
		FlowVar instance = (FlowVar) getSession().get("org.eapp.hbean.FlowVar", id);
		return instance;
	}

	@Override
	public ListPage<FlowVar> queryFlowVars(FlowVarQueryParameters aqp) {
		if (aqp == null) {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select fv from FlowVar as fv where 1=1");
			if (aqp.getDisplayName() != null) {
				hql.append(" and fv.displayName like :displayName");
				aqp.toArountParameter("displayName");
			}
			if (aqp.getFlowClass() != null) {
				hql.append(" and fv.flowClass = :flowClass");
			}
			if (aqp.getGlobalFlag() != null) {
				hql.append(" and fv.globalFlag = :globalFlag");
			}
			return new CommQuery<FlowVar>().queryListPage(aqp, 
					aqp.appendOrders(hql, "fv" ), getSession());
		} catch (RuntimeException re) {
			return new ListPage<FlowVar>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FlowVar> findFlowVars(String flowClass) {
		Query query = getSession().createQuery(
				"from FlowVar as fv where fv.flowClass=:flowClass or fv.globalFlag=:globalFlag order by fv.globalFlag, fv.displayName");
		query.setString("flowClass", flowClass);
		query.setBoolean("globalFlag", true);
		return query.list();
	}
	
	@Override
	public boolean testNameRepeat(String name, String flowClass, boolean globalFlag, String varId) {
		String hql = "select count(fh) from FlowVar as fh where fh.name=:name";
		if (varId != null) {
			hql += " and fh.varId <> :varId";
		}
		//全局类型，所有不能重复
		if (!globalFlag) {
			hql += " and (fh.globalFlag=:globalFalse and fh.flowClass=:flowClass or fh.globalFlag=:globalTrue)";
		}
		Query query = getSession().createQuery(hql);
		query.setString("name", name);
		if (varId != null) {
			query.setString("varId", varId);
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