package org.eapp.poss.dao;

import org.eapp.poss.dao.param.MessageQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Message;
import org.eapp.util.hibernate.ListPage;

public interface IMessageDAO extends IBaseHibernateDAO {
    
    public Message findById(String id) throws PossException;
    
    public ListPage<Message> queryMessageListPage(MessageQueryParameters qp);

}
