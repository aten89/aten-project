package org.eapp.poss.blo;

import org.eapp.poss.dao.param.MessageQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Message;
import org.eapp.util.hibernate.ListPage;

public interface IMessageBiz {
    
    public Message queryMessageById(String id) throws PossException;
    
    public ListPage<Message> queryMessageListPage(MessageQueryParameters qp);
    
    public void addMessage(Message message) throws PossException;
    public void deleteMessage(String id) throws PossException;
}
