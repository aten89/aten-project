package org.eapp.poss.blo.imp;


import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IMessageBiz;
import org.eapp.poss.dao.IMessageDAO;
import org.eapp.poss.dao.param.MessageQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Message;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

public class MessageBiz implements IMessageBiz {
    
    
    // dao
    private IMessageDAO messageDAO;

	/* (non-Javadoc)
	 * @see org.eapp.poss.blo.IMessageBiz#queryMessageById(java.lang.String)
	 */
	@Override
	public Message queryMessageById(String id) throws PossException {
		 // params judge
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：短信ID为空");
        }
        Message message = messageDAO.findById(id);
        if (message == null) {
            throw new PossException("该短信不存在");
        }
        Hibernate.initialize(message.getProdInfo());
        return message;
	}
	    

	/* (non-Javadoc)
	 * @see org.eapp.poss.blo.IMessageBiz#queryMessageListPage(org.eapp.poss.dao.param.MessageQueryParameters)
	 */
	@Override
	public ListPage<Message> queryMessageListPage(MessageQueryParameters qp) {
		ListPage<Message> messages = messageDAO.queryMessageListPage(qp);
    	if(!CollectionUtils.isEmpty(messages.getDataList())) {
    		for(Message message : messages.getDataList()) {
    			Hibernate.initialize(message.getProdInfo());
    		}
    	}
    	return messages; 
	}

	/**
	 * @param messageDAO the messageDAO to set
	 */
	public void setMessageDAO(IMessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}


	@Override
	public void addMessage(Message message) throws PossException {
		if (message == null) {
            throw new IllegalArgumentException("非法参数：短信对象为空");
        }
        messageDAO.save(message);
	}


	@Override
	public void deleteMessage(String id) throws PossException {
		if (StringUtils.isEmpty(id)) {
			return;
		}
		Message message = messageDAO.findById(id);
		messageDAO.delete(message);
		
	}
    
}
