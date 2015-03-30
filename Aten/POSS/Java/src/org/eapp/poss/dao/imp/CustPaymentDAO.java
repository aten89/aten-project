/**
 * 
 */
package org.eapp.poss.dao.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.ICustPaymentDAO;
import org.eapp.poss.dao.param.CustPaymentQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.hbean.Task;
import org.eapp.poss.hbean.TaskAssign;
import org.eapp.poss.rmi.hessian.PaymentRecord;
import org.eapp.poss.rmi.hessian.TransactionRecord;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Query;


/**
 * 客户划款DAO实现
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-20	黄云耿	新建
 * </pre>
 */
public class CustPaymentDAO extends BaseHibernateDAO implements ICustPaymentDAO {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustPaymentDAO.class);
    
    @Override
    public CustPayment findById(String id) throws PossException {
        logger.debug("execute findById  with id: " + id);
        CustPayment instance = (CustPayment) getSession().get(CustPayment.class, id);
        return instance;
    }

    @SuppressWarnings("unchecked")
	@Override
    public ListPage<CustPayment> queryCustPaymentList(CustPaymentQueryParameters qp, List<String> userRoles) throws PossException {
        logger.debug("execute queryCustPaymentList");    
        
        StringBuffer hql = new StringBuffer("from CustPayment as cp, Task as t left join t.taskAssigns as p ");
        hql.append(" where t.flowClass = :flowClass and (t.taskState = :createState or t.taskState = :startState) ");
        hql.append(" and t.flowInstanceId = cp.flowInstanceId ");
        hql.append(" and t.formId = cp.id ");
        hql.append(" and cp.formStatus is not null and cp.formStatus = :dealStatus ");
        hql.append(" and ((t.transactor = :currentUserId) or ");
        hql.append(" (p.assignKey = :currentUserId and t.transactor is null and p.type = :type) or ");
        hql.append(" (p.assignKey in(:userRoles) and t.transactor is null and p.type = :roleType))");
        
        if(qp.getStandardFlag()){
            qp.addParameter("flowClass", CustPayment.FBPAYMENT_FLOW_KEY);
        } else {
            qp.addParameter("flowClass", CustPayment.PAYMENT_FLOW_KEY);
        }
        qp.addParameter("createState", TaskInstance.PROCESS_STATE_CREATE);
        qp.addParameter("startState", TaskInstance.PROCESS_STATE_START);
        qp.addParameter("dealStatus", CustPayment.PROCESS_STATUS_DEAL);
        qp.addParameter("type", TaskAssign.TYPE_USER);
        qp.addParameter("userRoles", userRoles);
        qp.addParameter("roleType", TaskAssign.TYPE_ROLE);
        
        //过滤条件
        creatHql(hql, qp);
        
//        return new CommQuery<CustPayment>().queryListPage(qp, "select distinct cp, t " + qp.appendOrders(hql, "cp"),
//                "select count(distinct cp) " + hql.toString(), getSession());
        
        ListPage listPage = new CommQuery().queryListPage(qp, "select distinct cp, t " + qp.appendOrders(hql, "cp"),
                "select count (distinct cp)" + hql.toString(), getSession());
        
        List<CustPayment> cpList = new ArrayList<CustPayment>();
        if (listPage.getDataList() != null) {
            List<Object[]> list = listPage.getDataList();
            for (Object[] o : list) {
                CustPayment cp = (CustPayment) o[0];
                Task task = (Task) o[1];
                cp.setTask(task);
                cpList.add(cp);
            }
        }
        listPage.setDataList(cpList);
        return listPage;
    }
    
    @Override
    public ListPage<CustPayment> queryCustPaymentListWithoutTask(
    		CustPaymentQueryParameters iqp) throws PossException {
    	try {                                     
            StringBuffer hql = new StringBuffer(" from CustPayment as cp ");
            hql.append(" where 1=1 ");
            hql.append(" and (cp.formStatus = :archiveStatus or cp.formStatus = :scarpStatus) ");
            hql.append(" and cp.archiveDate is not null ");
            
            iqp.addParameter("archiveStatus", CustPayment.PROCESS_STATUS_ARCH);
            iqp.addParameter("scarpStatus", CustPayment.PROCESS_STATUS_INVALID);
            iqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            
            //过滤条件
            creatHql(hql, iqp);

            ListPage<CustPayment> listPage = new CommQuery<CustPayment>().queryListPage(iqp, "select distinct cp "  + iqp.appendOrders(hql, "cp"),
                    "select count (distinct cp)" + hql.toString(), getSession());
            
            return listPage;
        } catch (RuntimeException re) {
            logger.error("queryCustPaymentListWithoutTask failed", re);
            throw re;
        }
    }
    
	@Override
    public ListPage<CustPayment> queryArchiveCustPaymentList(CustPaymentQueryParameters iqp) throws PossException {
        try {                                     
            StringBuffer hql = new StringBuffer(" from CustPayment as cp, Task as t where ((t.taskState = :endState and t.transactor = :currentUserId) or cp.proposer = :currentUserId)");
            if(iqp.getStandardFlag() != null) {
            	hql.append(" and t.flowClass = :flowClass");
            }
            hql.append(" and t.flowInstanceId = cp.flowInstanceId ");
            hql.append(" and (cp.formStatus = :archiveStatus or cp.formStatus = :scarpStatus) ");
            hql.append(" and t.formId = cp.id and cp.archiveDate is not null ");
            
            iqp.addParameter("archiveStatus", CustPayment.PROCESS_STATUS_ARCH);
            iqp.addParameter("scarpStatus", CustPayment.PROCESS_STATUS_INVALID);
            iqp.addParameter("endState", TaskInstance.PROCESS_STATE_END);
            
            if(iqp.getStandardFlag() != null) {
            	if(iqp.getStandardFlag()){
                    iqp.addParameter("flowClass", CustPayment.FBPAYMENT_FLOW_KEY);
                } else {
                    iqp.addParameter("flowClass", CustPayment.PAYMENT_FLOW_KEY);
                }
            }
            //过滤条件
            creatHql(hql, iqp);

            return new CommQuery<CustPayment>().queryListPage(iqp, "select distinct cp "  + iqp.appendOrders(hql, "cp"),
                    "select count (distinct cp)" + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.error("queryArchiveIssueList failed", re);
            throw re;
        }
    }

	@Override
    public ListPage<CustPayment> queryTrackCustPaymentList(CustPaymentQueryParameters iqp) {
        try {                                     
            StringBuffer hql = new StringBuffer(" from CustPayment as cp, Task as t ");
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
            if(iqp.getStandardFlag()){
                iqp.addParameter("flowClass", CustPayment.FBPAYMENT_FLOW_KEY);
            } else {
                iqp.addParameter("flowClass", CustPayment.PAYMENT_FLOW_KEY);
            }
            iqp.addParameter("type", TaskAssign.TYPE_USER);
            
            //过滤条件
            creatHql(hql, iqp);

           return new CommQuery<CustPayment>().queryListPage(iqp, "select distinct cp"  + iqp.appendOrders(hql, "cp"),
                    "select count (distinct cp)" + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.error("queryTrackCustPaymentList failed", re);
            throw re;
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public ListPage<TransactionRecord> queryTransactionRecordPage(CustPaymentQueryParameters qp) {
    	StringBuffer hql = 
    			new StringBuffer("select pi.prodName as prodName, " +
    					"sum(cp.transferAmount) as pamentMoneySum, " +
    					"max(cp.transferDate) as transactionDate, " +
    					"max(pi.prodSetUpDate) as prodSetUpDate, " +
    					"max(pi.actualCashDate) as actualCashDate, " +
    					"max(pi.sellTimeLimit) as sellTimeLimit ");
//    	StringBuffer countHql = new StringBuffer("select count(*) from ( select pi.prodName ");
    	StringBuffer commonHql = new StringBuffer();
    	commonHql.append(" from CustPayment as cp, ProdInfo as pi where cp.prodId = pi.id ");
    	commonHql.append(" and cp.formStatus = 2 ");
        
        if(StringUtils.isNotEmpty(qp.getCustId())) {
        	commonHql.append(" and cp.custId =:custId ");
        }
        
        commonHql.append(" group by pi.prodName ");
        
        ListPage listPage = new CommQuery().queryListPage(qp, hql.append(commonHql).toString(),
        		"select count(distinct cp.prodId) from CustPayment as cp where cp.formStatus = 2 and cp.custId =:custId", getSession());
        
        List<TransactionRecord> trList = new ArrayList<TransactionRecord>();
        if (listPage.getDataList() != null) {
            List<Object> list = listPage.getDataList();
            for (Object o : list) {
            	Object[] cp = (Object[]) o;
            	TransactionRecord transactionRecord = new TransactionRecord();
            	transactionRecord.setProdName((String)cp[0]);
            	transactionRecord.setPamentMoneySum((Double)cp[1]);
            	transactionRecord.setTransactionDate((Date)cp[2]);
            	transactionRecord.setProdSetUpDate((Date)cp[3]);
            	transactionRecord.setActualCashDate((Date)cp[4]);
            	transactionRecord.setSellTimeLimit((Integer)cp[5]);
            	
//            	transactionRecord.setActualCashDateStr(null != transactionRecord.getActualCashDate() ? df.format(transactionRecord.getActualCashDate()) : "");
//    			transactionRecord.setProdSetUpDateStr(null != transactionRecord.getProdSetUpDate() ? df.format(transactionRecord.getProdSetUpDate()) : "");
//    			transactionRecord.setTransactionDateStr(null != transactionRecord.getTransactionDate() ? df.format(transactionRecord.getTransactionDate()) : "");
            	
            	trList.add(transactionRecord);
            }
        }
        listPage.setDataList(trList);
        
    	return listPage;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ListPage<PaymentRecord> queryPaymentRecordPage(CustPaymentQueryParameters qp) {
    	StringBuffer hql = new StringBuffer("select cp.id, pi.prodName, cp.transferAmount, cp.totalRefundAmount, cp.transferDate, cp.remark");
		StringBuffer commonHql = new StringBuffer();
		commonHql.append(" from CustPayment as cp, ProdInfo as pi where cp.prodId = pi.id and cp.formStatus = 2 ");
	    
	    if(StringUtils.isNotEmpty(qp.getCustId())) {
	    	commonHql.append(" and cp.custId =:custId ");
	    }
	    
	    commonHql.append(" order by cp.transferDate desc");
	    
	    ListPage listPage = new CommQuery().queryListPage(qp, hql.append(commonHql).toString(),
	    		"select count(cp)" + commonHql.toString(), getSession());
	    
	    List<PaymentRecord> trList = new ArrayList<PaymentRecord>();
	    if (listPage.getDataList() != null) {
	        List<Object> list = listPage.getDataList();
	        for (Object o : list) {
	        	Object[] cp = (Object[]) o;
	        	PaymentRecord transactionRecord = new PaymentRecord();
	        	transactionRecord.setPaymentId((String)cp[0]);
	        	transactionRecord.setProdName((String)cp[1]);
	        	transactionRecord.setTransferAmount((Double)cp[2]);
	        	transactionRecord.setTotalRefundAmount((Double)cp[3]);
	        	transactionRecord.setTransferDate((Date)cp[4]);
	        	transactionRecord.setRemark((String)cp[5]);
	        	trList.add(transactionRecord);
	        }
	    }
	    listPage.setDataList(trList);
	    
		return listPage;
    }

    private void creatHql(StringBuffer hql, CustPaymentQueryParameters qp) {
        if(qp.getStandardFlag() != null){
            hql.append(" and cp.standardFlag =:standardFlag ");
        }
        if (qp.getBgnTransferDate() != null) {
            hql.append(" and cp.transferDate >=:bgnTransferDate ");
        }
        if (qp.getEndTransferDate() != null) {
            hql.append(" and cp.transferDate <=:endTransferDate ");
        } 
        if (StringUtils.isNotEmpty(qp.getCustId())) {
            hql.append(" and cp.custId =:custId ");
        } 
        if (StringUtils.isEmpty(qp.getCustId()) && StringUtils.isNotEmpty(qp.getCustName())) {
            hql.append(" and cp.custName =:custName ");
        } 
        if (StringUtils.isNotEmpty(qp.getProdId())) {
            hql.append(" and cp.prodId =:prodId ");
        } 
        if (StringUtils.isNotEmpty(qp.getPayType())) {
            hql.append(" and cp.payType =:payType ");
        } 
        if (StringUtils.isNotEmpty(qp.getSaleManId())) {
            hql.append(" and cp.saleManId =:saleManId ");
        }
        if (StringUtils.isNotEmpty(qp.getFormStatus())) {
            hql.append(" and cp.formStatus =:formStatus ");
        }
    }
    
    @Override
    public ListPage<CustPayment> queryAllCustPaymentList(CustPaymentQueryParameters iqp) {
        try {                                     
            StringBuffer hql = new StringBuffer(" from CustPayment as cp where 1=1 ");
            
            //过滤条件
            creatHql(hql, iqp);

            return new CommQuery<CustPayment>().queryListPage(iqp, "select cp"  + iqp.appendOrders(hql, "cp"),
                    "select count (cp)" + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.error("queryTrackCustPaymentList failed", re);
            throw re;
        }
    }
    
    @Override
    public boolean checkCustPayment(String custId, String excludeCustPaymentId) {
    	Query query = getSession().createQuery("select count(cp) from CustPayment as cp where cp.transferAmount > cp.totalRefundAmount and cp.custId=:custId and cp.id <> :excludeCustPaymentId");
    	Number totalNumber = (Number) query.uniqueResult();
    	int count = totalNumber != null ? totalNumber.intValue() : 0;
    	return count > 0;
    }
}
