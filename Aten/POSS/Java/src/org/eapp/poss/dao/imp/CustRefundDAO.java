/**
 * 
 */
package org.eapp.poss.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.ICustRefundDAO;
import org.eapp.poss.dao.param.CustRefundQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.CustRefund;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;


/**
 * 客户划款DAO实现
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	黄云耿	新建
 * </pre>
 */
public class CustRefundDAO extends BaseHibernateDAO implements ICustRefundDAO {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustRefundDAO.class);
    
    @Override
    public CustRefund findById(String id) throws PossException {
        logger.debug("execute findById  with id: " + id);
        CustRefund instance = (CustRefund) getSession().get(CustRefund.class, id);
        return instance;
    }

    @Override
    public ListPage<CustRefund> queryCustRefundDraftList(
    		CustRefundQueryParameters qp) throws PossException {
    	logger.debug("execute queryCustRefundList");    
        
        StringBuffer hql = new StringBuffer("from CustRefund as cp where 1=1");
        hql.append(" and cp.formStatus is not null and cp.formStatus = :formStatus ");
        qp.addParameter("formStatus", CustRefund.PROCESS_STATUS_DRAFT);
        
        creatHql(hql, qp);
        
        ListPage<CustRefund> listPage = new CommQuery<CustRefund>().queryListPage(qp, "select distinct cp " + qp.appendOrders(hql, "cp"),
                "select count (distinct cp)" + hql.toString(), getSession());
        return listPage;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public ListPage<CustRefund> queryCustRefundList(CustRefundQueryParameters qp, List<String> userRoles) throws PossException {
        logger.debug("execute queryCustRefundList");    
        
        StringBuffer hql = new StringBuffer("from CustRefund as cp, Task as t left join t.taskAssigns as p ");
        hql.append(" where t.flowClass = :flowClass and (t.taskState = :createState or t.taskState = :startState) ");
        hql.append(" and t.flowInstanceId = cp.flowInstanceId ");
        hql.append(" and t.formId = cp.id ");
        hql.append(" and cp.formStatus is not null and cp.formStatus = :dealStatus ");
        hql.append(" and ((t.transactor = :currentUserId) or ");
        hql.append(" (p.assignKey = :currentUserId and t.transactor is null and p.type = :type) or ");
        hql.append(" (p.assignKey in(:userRoles) and t.transactor is null and p.type = :roleType))");
        
        qp.addParameter("flowClass", CustRefund.REFUND_FLOW_KEY);
        qp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        qp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
        qp.addParameter("dealStatus", CustRefund.PROCESS_STATUS_DEAL);
        qp.addParameter("type", TaskAssign.TYPE_USER);
//        qp.addParameter("userRoles", listToString(userRoles));
        qp.addParameter("userRoles", userRoles);
        qp.addParameter("roleType", TaskAssign.TYPE_ROLE);
        
        creatHql(hql, qp);
        
        ListPage listPage = new CommQuery().queryListPage(qp, "select distinct cp, t " + qp.appendOrders(hql, "cp"),
                "select count (distinct cp)" + hql.toString(), getSession());
        
        List<CustRefund> cpList = new ArrayList<CustRefund>();
        if (listPage.getDataList() != null) {
            List<Object[]> list = listPage.getDataList();
            for (Object[] o : list) {
                CustRefund cp = (CustRefund) o[0];
                Task task = (Task) o[1];
                cp.setTask(task);
                cpList.add(cp);
            }
        }
        listPage.setDataList(cpList);
        return listPage;
    }
    
//    private String listToString(List<String> list) {
//    	String result = "";
//    	if(!CollectionUtils.isEmpty(list)) {
//    		for(String str : list) {
//    			result += "'" + str + "',";
//    		}
//    	}
//    	if(-1 != result.indexOf(",")) {
//    		result = result.substring(0, result.length() - 1);
//    	}
//    	return result;
//    }
    
    @Override
    public ListPage<CustRefund> queryArchiveCustRefundList(CustRefundQueryParameters iqp) {
        try {                                     
            StringBuffer hql = new StringBuffer(" from CustRefund as cp, Task as t ");
            hql.append(" where t.flowClass = :flowClass and ((t.taskState = :endState and t.transactor = :currentUserId) or cp.proposer = :currentUserId) ");
            hql.append(" and t.flowInstanceId = cp.flowInstanceId ");
            hql.append(" and (cp.formStatus = :archiveStatus or cp.formStatus = :scarpStatus) ");
            hql.append(" and t.formId = cp.id and cp.archiveDate is not null ");
            
            iqp.addParameter("archiveStatus", CustRefund.PROCESS_STATUS_ARCH);
            iqp.addParameter("scarpStatus", CustRefund.PROCESS_STATUS_INVALID);
            iqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            iqp.addParameter("flowClass", CustRefund.REFUND_FLOW_KEY);

            creatHql(hql, iqp);
            
            ListPage<CustRefund> listPage = new CommQuery<CustRefund>().queryListPage(iqp, "select distinct cp"  + iqp.appendOrders(hql, "cp"),
                    "select count (distinct cp)" + hql.toString(), getSession());
            
            return listPage;
        } catch (RuntimeException re) {
            logger.error("queryArchiveIssueList failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public ListPage<CustRefund> queryTrackCustRefundList(CustRefundQueryParameters iqp) {
        try {                                     
            StringBuffer hql = new StringBuffer(" from CustRefund as cp, Task as t ");
            hql.append(" where t.flowClass = :flowClass and ((t.taskState = :endState and t.transactor = :currentUserId) or cp.proposer = :currentUserId) ");
            hql.append(" and cp.archiveDate is null ");
            hql.append(" and t.flowInstanceId = cp.flowInstanceId ");
            hql.append(" and t.formId = cp.id ");
            hql.append(" and cp.id not in ");
            hql.append(" (select ct.formId from Task as ct left join ct.taskAssigns as ta where ct.formId = cp.id and");
            hql.append(" ((ct.taskState = :createState or ct.taskState = :startState) and ((ct.transactor = :currentUserId)");
            hql.append(" or (ta.assignKey = :currentUserId and ct.transactor is null and ta.type = :type)))) ");
            iqp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
            iqp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
            iqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            iqp.addParameter("flowClass", CustRefund.REFUND_FLOW_KEY);
            iqp.addParameter("type", TaskAssign.TYPE_USER);
            
            creatHql(hql, iqp);

            ListPage listPage = new CommQuery().queryListPage(iqp, "select distinct cp, t"  + iqp.appendOrders(hql, "cp"),
                    "select count (distinct cp)" + hql.toString(), getSession());
            
            List<CustRefund> cpList = new ArrayList<CustRefund>();
            if (listPage.getDataList() != null) {
                List<Object[]> list = listPage.getDataList();
                for (Object[] o : list) {
                    CustRefund cp = (CustRefund) o[0];
                    Task task = (Task) o[1];
                    cp.setTask(task);
                    cpList.add(cp);
                }
            }
            listPage.setDataList(cpList);
            return listPage;
        } catch (RuntimeException re) {
            logger.error("queryTrackCustRefundList failed", re);
            throw re;
        }
    }
    
    private void creatHql(StringBuffer hql, CustRefundQueryParameters qp) {
//    	if(null != qp.getCurrentUserId()) {
//        	hql.append(" and cp.proposer is not null and cp.proposer = :currentUserId ");
//        }
        if(null != qp.getApplyTimeBegin()) {
        	hql.append(" and cp.applyTime >= :applyTimeBegin ");
        }
        if(null != qp.getApplyTimeEnd()) {
        	hql.append(" and cp.applyTime <= :applyTimeEnd ");
        }
        if(null != qp.getCustId()) {
        	hql.append(" and cp.custId = :custId ");
        }
        if(null != qp.getSaleManId()) {
        	hql.append(" and cp.proposer = :saleManId");
        }
    }
}
