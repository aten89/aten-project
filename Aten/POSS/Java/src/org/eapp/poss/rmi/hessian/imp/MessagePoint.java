package org.eapp.poss.rmi.hessian.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.poss.blo.IMessageBiz;
import org.eapp.poss.dao.param.MessageQueryParameters;
import org.eapp.poss.hbean.Message;
import org.eapp.poss.rmi.hessian.IMessagePoint;
import org.eapp.poss.rmi.hessian.MessageInfo;
import org.eapp.util.hibernate.ListPage;

import com.caucho.hessian.server.HessianServlet;

public class MessagePoint extends HessianServlet implements IMessagePoint {

	private static final long serialVersionUID = 1226278565808624931L;
	
	/**
	 * 短信逻辑访问接口
	 */
	private IMessageBiz messageBiz;

	/**
	 * @param messageBiz the messageBiz to set
	 */
	public void setMessageBiz(IMessageBiz messageBiz) {
		this.messageBiz = messageBiz;
	}

	/* (non-Javadoc)
	 * @see org.eapp.poss.rmi.hessian.IMessagePoint#queryMessagePage(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, boolean)
	 */
	@Override
	public ListPage<MessageInfo> queryMessagePage(String userID, String tel,
			Integer pageSize, Integer pageNo, String sortCol, boolean ascend) {
		MessageQueryParameters qp = new MessageQueryParameters();
        if (StringUtils.isNotEmpty(userID)) {
        	qp.setSalesManager(userID);
        }
        // 电话
        if (StringUtils.isNotEmpty(tel)) {
            qp.setReceiverNo(tel);
        }
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        // 增加对排序列的处理
        if (StringUtils.isNotEmpty(sortCol)) {
        }
        ListPage<Message> list = messageBiz.queryMessageListPage(qp);
        
        ListPage<MessageInfo> resultList = new ListPage<MessageInfo>();
        List<MessageInfo> messageList = new ArrayList<MessageInfo>();
        if (list != null && list.getDataList() != null) {
        	for (Message message : list.getDataList()) {
        		MessageInfo m = new MessageInfo();
        		m.setId(message.getId());
        		m.setProdName(message.getProdName());
        		m.setReceiverNo(message.getReceiverNo());
        		m.setSalesManager(message.getSalesManager());
        		m.setSendNo(message.getSendNo());
        		m.setSendTime(message.getSendTime());
        		m.setTitle(message.getTitle());
        		m.setContent(message.getContent());
        		messageList.add(m);
    		}
        	resultList.setCurrentPageNo(list.getCurrentPageNo());
        	resultList.setCurrentPageSize(list.getCurrentPageSize());
        	resultList.setTotalCount(list.getTotalCount());
        	resultList.setDataList(messageList);
        }
        return resultList;
	}
	
}
