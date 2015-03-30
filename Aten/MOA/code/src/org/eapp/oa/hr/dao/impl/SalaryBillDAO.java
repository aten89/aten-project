
package org.eapp.oa.hr.dao.impl;

// default package
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.hr.dao.ISalaryBillDAO;
import org.eapp.oa.hr.dto.SalaryBillQueryParameters;
import org.eapp.oa.hr.hbean.SalaryBill;
import org.eapp.oa.system.dao.imp.BaseHibernateDAO;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

public class SalaryBillDAO extends BaseHibernateDAO implements ISalaryBillDAO {

	private static final Log log = LogFactory.getLog(SalaryBillDAO.class);
	
	@Override
	public SalaryBill findById(java.lang.String id){
		log.debug("getting SalaryBill instance with id: " + id);
		try {
			SalaryBill instance = (SalaryBill) getSession().get(SalaryBill.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public SalaryBill findSalaryBill(String userAccountID, int month) {
		Query query = getSession().createQuery("from SalaryBill as sb where sb.month=:month and sb.userAccountID=:userAccountID order by sb.importDate desc");
		query.setInteger("month", month);
		query.setString("userAccountID", userAccountID);
		query.setMaxResults(1);
		return (SalaryBill)query.uniqueResult();
	}

	@Override
	public ListPage<SalaryBill> querySalaryBillPage(SalaryBillQueryParameters qp) {
		if (qp == null) {
            throw new IllegalArgumentException("非法参数:查询参数对象为null");
        }
        try {
            StringBuffer hql = new StringBuffer(
                    "from SalaryBill as re where 1=1");
            if (qp.getEmployeeNumber() != null) {
            	hql.append(" and re.employeeNumber like :employeeNumber ");
            	qp.toArountParameter("employeeNumber");
            }
            if (qp.getUserAccountID() != null) {
            	hql.append(" and re.userAccountID = :userAccountID ");
            }
            if (qp.getMonth() != null) {
            	hql.append(" and re.month = :month ");
            }
            if (qp.getUserKeyword() != null) {
				hql.append(" and (re.userName like :userKeyword or re.userAccountID like :userKeyword)");
				qp.toArountParameter("userKeyword");
			}

            return new CommQuery<SalaryBill>().queryListPage(qp,
                    "select re " + qp.appendOrders(hql, "re"), "select count(re) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            log.error("getTrackOrArchStaffFlowApply faild", re);
            return new ListPage<SalaryBill>();
        }
	}
	
	@Override
	public int countByMonth(int month) {
		Query query = getSession().createQuery("select count(sb) from SalaryBill as sb where sb.month=:month");
		query.setInteger("month", month);
		Long count = (Long)query.uniqueResult();
		return count == null ? 0 : count.intValue();
	}

	@Override
	public void deletByMonth(int month) {
		Query query = getSession().createQuery("delete from SalaryBill as sb where sb.month=:month");
		query.setInteger("month", month);
		query.executeUpdate();
	}
}

