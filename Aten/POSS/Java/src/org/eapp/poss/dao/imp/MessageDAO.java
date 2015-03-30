/**
 * 
 */
package org.eapp.poss.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.dao.IMessageDAO;
import org.eapp.poss.dao.param.MessageQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Message;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;

public class MessageDAO extends BaseHibernateDAO implements IMessageDAO {

    private static Log logger = LogFactory.getLog(MessageDAO.class);
    
    /* (non-Javadoc)
     * @see org.eapp.poss.dao.IProdInfoDAO#findById(java.lang.String)
     */
    @Override
    public Message findById(String id) throws PossException {
    	try {
            return (Message) getSession().get(Message.class, id);
        } catch (RuntimeException re) {
            logger.error("MessageDAO.findById failed: ", re);
            throw re;
        }
    }

	/* (non-Javadoc)
	 * @see org.eapp.poss.dao.IMessageDAO#queryMessageListPage(org.eapp.poss.dao.param.MessageQueryParameters)
	 */
	@Override
	public ListPage<Message> queryMessageListPage(MessageQueryParameters qp) {
		StringBuffer hql = new StringBuffer();
        hql.append(" from Message as m where 1=1 ");
        if (qp.getSendTimeBegin() != null) {
            hql.append(" and m.sendTime >= :sendTimeBegin ");
        }
        if (qp.getSendTimeEnd() != null) {
            hql.append(" and m.sendTime <= :sendTimeEnd ");
        }
        if (StringUtils.isNotEmpty(qp.getProdId())) {
        	hql.append(" and m.prodInfo.id = :prodId ");
        }
        if (StringUtils.isNotEmpty(qp.getSalesManager())) {
       	 	hql.append(" and m.salesManager = :salesManager ");
        }
        if (StringUtils.isNotEmpty(qp.getReceiverNo())) {
        	hql.append(" and m.receiverNo like :receiverNo ");
        	qp.toArountParameter("receiverNo");
        }
        try {
            return new CommQuery<Message>().queryListPage(qp, qp.appendOrders(hql, "m"),
                    "select count(distinct m) " + hql.toString(), getSession());
        } catch (RuntimeException re) {
            logger.debug("queryMessageListPage by QueryParameters failed:", re);
            throw re;
        }
	}

}
