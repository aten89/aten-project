package org.eapp.dao.imp;

import java.util.List;

import org.eapp.dao.IFlowConfigDAO;
import org.eapp.dao.param.FlowConfigQueryParameters;
import org.eapp.hbean.FlowConfig;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Administrator
 * @version Nov 28, 2014
 */
public class FlowConfigDAO extends BaseHibernateDAO implements IFlowConfigDAO  {
	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(FlowConfigDAO.class);

	@Override
	public FlowConfig findByFlowKey(String flowKey, int flowFlag) {
		if (flowKey == null)  {
			throw new IllegalArgumentException("非法参数：流程标识不能为空！");
		}
		Query query = getSession().createQuery(
				"from FlowConfig as model where model.flowKey=:flowKey and model.draftFlag=:flowFlag");
		query.setString("flowKey", flowKey);
		query.setInteger("flowFlag", flowFlag);
		query.setMaxResults(1);
		return (FlowConfig)query.uniqueResult();
    }
	
    @SuppressWarnings("unchecked")
	@Override
    public List<FlowConfig> findByflowClass(String flowClass, int flowFlag) {
    	Query query = getSession().createQuery(
				"from FlowConfig as model where model.flowClass=:flowClass and model.draftFlag=:flowFlag");
		query.setString("flowClass", flowClass);
		query.setInteger("flowFlag", flowFlag);
		return query.list();
    }
    
    @Override
    public ListPage<FlowConfig> queryFlowConfig(FlowConfigQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select m from FlowConfig as m where 1=1 ");
			//拼接查询条件
			if(qp.getFlowClass() != null){
				hql.append(" and m.flowClass = :flowClass");
			}
			if(qp.getFlowKey()!= null){
				hql.append(" and m.flowKey=:flowKey");
			}
			if(qp.getFlowName() != null){//流程名称模糊匹配
				hql.append(" and m.flowName like :flowName");
				qp.toArountParameter("flowName");
			}
			if(qp.getDraftFlag()!= null){
				hql.append(" and m.draftFlag=:draftFlag");
			}

			return new CommQuery<FlowConfig>().queryListPage(qp, qp.appendOrders(hql, "m"), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error", re);
			return new ListPage<FlowConfig>();
		}
	}

    /**
     * 通过流程名称、标识及id判断名称是否重复
     * @param flowKey
     * @param flowFlag
     * @return
     */
	@Override
	public boolean testFlowNameRepeat(String flowName, String flowKey) {
		Query query = getSession().createQuery(
				"select count(model) from FlowConfig as model where model.flowName=:flowName and model.flowKey <> :flowKey and model.draftFlag <>:draftFlag");
		query.setString("flowName", flowName);
		query.setString("flowKey", flowKey);
		query.setInteger("draftFlag", FlowConfig.FLOW_FLAG_DISABLE);
		Number count = (Number)query.uniqueResult();
		return count != null && count.intValue() > 0;

    }
	
	@Override
	public boolean testFlowKeyRepeat(String flowKey) {
		Query query = getSession().createQuery(
				"select count(model) from FlowConfig as model where model.flowKey=:flowKey and model.draftFlag = :draftFlag");
		query.setString("flowKey", flowKey);
		query.setInteger("draftFlag", FlowConfig.FLOW_FLAG_DRAFT);
		Number count = (Number)query.uniqueResult();
		return count != null && count.intValue() > 0;
		
    }
}