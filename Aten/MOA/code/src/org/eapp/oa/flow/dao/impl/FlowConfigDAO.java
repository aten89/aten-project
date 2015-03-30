package org.eapp.oa.flow.dao.impl;
// default package

import java.util.List;

import org.eapp.oa.flow.dao.IFlowConfigDAO;
import org.eapp.oa.flow.dto.FlowConfigQueryParameters;
import org.eapp.oa.flow.hbean.FlowConfig;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;

/**
 	* A data access object (DAO) providing persistence and search support for FlowConfig entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see .FlowConfig
  * @author MyEclipse Persistence Tools 
 */

public class FlowConfigDAO extends BaseHibernateDAO implements IFlowConfigDAO  {
	/**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(FlowConfigDAO.class);


    @Override
    public FlowConfig findById( java.lang.String id) {
        log.debug("getting FlowConfig instance with id: " + id);
        try {
            FlowConfig instance = (FlowConfig) getSession()
                    .get(FlowConfig.class, id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<FlowConfig> findByflowClass(String flowClass) {
    	Query query = getSession().createQuery(
				"from FlowConfig as model where model.flowClass=:flowClass and model.draftFlag=:flowFlag");
		
		query.setString("flowClass", flowClass);
		query.setInteger("flowFlag", FlowConfig.FLOW_FLAG_PUBLISHED);
		return query.list();
    }
    
    @Override
    public ListPage<FlowConfig> queryFlowConfig(FlowConfigQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("from FlowConfig as m where 1=1 ");
			
			//拼接查询条件
			if(qp.getId() != null) {
				hql.append(" and m.id=:id");
			}
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
			if(qp.getFlowVersion()!= null){
				hql.append(" and m.flowVersion=:flowVersion");
			}
			
			if(qp.getDraftFlag()!= null){
				hql.append(" and m.draftFlag=:draftFlag");
			}

			return new CommQuery<FlowConfig>().queryListPage(qp, 
					"select distinct m " + qp.appendOrders(hql, "m"), 
					"select count(distinct m) "  + hql.toString(), getSession());
		} catch (RuntimeException re) {
			log.error("query listPage error", re);
			return new ListPage<FlowConfig>();
		}
	}

   @Override
    public FlowConfig findFlowByFlowKey(String flowKey, int flowFlag) {
        log.debug("getting FlowConfig instance with flowKey: " + flowKey);
        
		if (flowKey == null)  {
			throw new IllegalArgumentException("非法参数：流程标识不能为空！");
		}

		try {
			Query query = getSession().createQuery(
					"from FlowConfig as model where model.flowKey=:flowKey and model.draftFlag=:flowFlag");
			
			query.setString("flowKey", flowKey);
			query.setInteger("flowFlag", flowFlag);
			query.setMaxResults(1);
			return (FlowConfig)query.uniqueResult();
		} catch (RuntimeException re) {
			re.printStackTrace();
            log.error("get failed", re);
            throw re;
		}
    }
    

    /**
     * 通过流程名称、标识及id判断名称是否重复
     * @param flowKey
     * @param flowFlag
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	public boolean isFlowNameRepeat(String flowName, String flowKey) {
        log.debug("getting FlowConfig instance with flowName: " + flowName + " and  flowKey: " + flowKey);
        
		try {
			Query query = getSession().createQuery(
					"select count(model) from FlowConfig as model where lower(model.flowName)=lower(:flowName) and model.flowKey <> :flowKey and model.draftFlag <> :flowFlag");
			
			query.setString("flowName", flowName);
			query.setString("flowKey", flowKey);
			query.setInteger("flowFlag", FlowConfig.FLOW_FLAG_DISABLE);
 
			Number count = (Number)query.uniqueResult();
			return count != null && count.intValue() > 0;
		} catch (RuntimeException re) {
			re.printStackTrace();
            log.error("get failed", re);
            throw re;
		}
    }
   
}