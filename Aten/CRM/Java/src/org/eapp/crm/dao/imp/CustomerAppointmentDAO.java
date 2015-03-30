package org.eapp.crm.dao.imp;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.dao.ICustomerAppointmentDAO;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerAppointment;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

public class CustomerAppointmentDAO extends BaseHibernateDAO implements ICustomerAppointmentDAO {

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(CustomerAppointmentDAO.class);

    @Override
    public List<CustomerAppointment> queryAllCustomerAppointment(Date appointmentTimeBegin, Date appointmentTimeEnd) throws CrmException {
    	logger.debug("queryAllCustomerAppointment start...");
    	QueryParameters qp = new QueryParameters();
    	qp.setPageSize(0);
    	StringBuffer hql = new StringBuffer(" from CustomerAppointment as ca");
        hql.append(" where 1=1");
        if(null != appointmentTimeBegin) {
        	hql.append(" and TRUNC(ca.appointmentTime - NUMTODSINTERVAL(ca.warnOpportunity, 'minute'), 'mi') >= TRUNC(:appointmentTimeBegin, 'mi')");
        	qp.addParameter("appointmentTimeBegin", appointmentTimeBegin);
        }
        
        if(null != appointmentTimeEnd) {
        	hql.append(" and TRUNC(ca.appointmentTime - NUMTODSINTERVAL(ca.warnOpportunity, 'minute'), 'mi') < TRUNC(:appointmentTimeEnd, 'mi')");
        	qp.addParameter("appointmentTimeEnd", appointmentTimeEnd);
        }
        
        ListPage<CustomerAppointment> resultListPage = new CommQuery<CustomerAppointment>().queryListPage(qp, qp.appendOrders(hql, "ca"),
                "select count(distinct ca) " + hql.toString(), getSession());
        if(null != resultListPage && null != resultListPage.getDataList()) {
        	return resultListPage.getDataList();
        } else {
        	return null;
        }
    }
    
    @Override
    public CustomerAppointment findById(String customerAppointmentId)
    		throws CrmException {
    	logger.debug("execute findById with customerAppointmentId: " + customerAppointmentId);
    	CustomerAppointment instance = (CustomerAppointment) getSession().get(CustomerAppointment.class, customerAppointmentId);
        return instance;
    }
}
