package org.eapp.poss.action;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.rmi.hessian.CustInfo;
import org.eapp.crm.rmi.hessian.ICustomerInfoPoint;
import org.eapp.poss.blo.IMessageBiz;
import org.eapp.poss.blo.IProdInfoBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.param.MessageQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.ListPage;

public class MessageAction extends BaseAction {

	private static final long serialVersionUID = -2015474581029550421L;

	/**
     * logger
     */
    private static Log logger = LogFactory.getLog(MessageAction.class);
    
    // service
    private IMessageBiz messageBiz;
    private IProdInfoBiz prodInfoBiz;
    private ICustomerInfoPoint custInfoPoint;
    
    // 输入参数
    private String title;
    private String prodId;
    private String prodName;
    private String receiverNo;
    private String content;
    
    private String customerName;
    private String tel;
    private String custStr;
    
    /**
     * 页码
     */
    private int pageNo;
    /**
     * 每页数
     */
    private int pageSize;
    /**
     * 当前排序列
     */
    private String sortCol;
    /**
     * true:升序;false:降序
     */
    private boolean ascend;
    
    private String id;
    /**
     * 发行日期开始
     */
    private Date sendTimeBegin;
    
    private Date sendTimeEnd;

    
    // 输出参数
    private ListPage<org.eapp.poss.hbean.Message> messageListPage;
    private ListPage<ProdInfo> prodInfoListPage;
    private String saleManagerName;
    private org.eapp.poss.hbean.Message message;
    private ListPage<CustInfo> custListPage;
    
    // method
    public String initAddMessage() {
        SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
        saleManagerName = user.getDisplayName();
		return success();
	}
    
    public String queryMessageListPage() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
            MessageQueryParameters qp = new MessageQueryParameters();
            if (sendTimeBegin != null) {
            	qp.setSendTimeBegin(sendTimeBegin);
            }
            if (sendTimeEnd != null) {
            	qp.setSellDateEnd(sendTimeEnd);
            }
            if (StringUtils.isNotEmpty(prodId)) {
            	qp.setProdId(prodId);
            }
            if (StringUtils.isNotEmpty(receiverNo)) {
            	qp.setReceiverNo(receiverNo);
            }
            qp.setSalesManager(user.getAccountID());
            
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            // 增加对排序列的处理
            if (StringUtils.isNotEmpty(sortCol)) {
                qp.addOrder(sortCol, ascend);
            }
            messageListPage = messageBiz.queryMessageListPage(qp);
            return success();
        } catch (Exception e) {
            logger.error("queryMessageListPage failed: ", e);
            return error("系统错误");
        }
    }
 
    public String addMessage() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
        	return error("请先登录");
        }
        org.eapp.poss.hbean.Message message = new org.eapp.poss.hbean.Message();
        if (StringUtils.isEmpty(prodId)){
        	return error("产品不能为空");
        }
    	try {
    		ProdInfo prodInfo = prodInfoBiz.queryProdInfoById(prodId);
            if (prodInfo == null){
            	return error("该产品不存在");
            }
//            ProdInfo prodInfo = new ProdInfo();
//        	prodInfoBiz.txAddProdInfo(prodInfo);
            message.setTitle(title);
            message.setProdInfo(prodInfo);
            message.setProdName(prodName);
            message.setReceiverNo(receiverNo);
            message.setSalesManager(user.getAccountID());
            message.setSendTime(new Date());
            message.setContent(content);
            
			messageBiz.addMessage(message);
		}  catch (PossException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			logger.error("addMessage failed: ", e);
			return error("系统错误");
		}
    	return success();
    }
    
    public String viewMessage() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
    	if (StringUtils.isEmpty(id)) {
    		return error("该短信不存在");
        }
        try {
        	saleManagerName = user.getDisplayName();
            message = messageBiz.queryMessageById(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("viewMessage failed: ", e);
            return error();
        }
    }
    
    public String initCustChoose() {
    	return success();
    }
    
    public String queryCustomerList() {
    	SessionAccount user = (SessionAccount)getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
		try {
            
            custListPage = custInfoPoint.queryCustPage(user.getAccountID(), customerName, tel, pageSize, pageNo, sortCol, ascend);
            return success();
        } catch (Exception e) {
            logger.error("queryCustomerList failed: ", e);
            return error("系统错误");
        }
    }
    
    public String deleteMessage() {
    	if (StringUtils.isEmpty(id)) {
    		return error("该短信不存在");
        }
		try {
            
             messageBiz.deleteMessage(id);
            return success();
        } catch (Exception e) {
            logger.error("queryCustomerList failed: ", e);
            return error("系统错误");
        }
    }

	/**
	 * @return the pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the sortCol
	 */
	public String getSortCol() {
		return sortCol;
	}

	/**
	 * @param sortCol the sortCol to set
	 */
	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}

	/**
	 * @return the ascend
	 */
	public boolean isAscend() {
		return ascend;
	}

	/**
	 * @param ascend the ascend to set
	 */
	public void setAscend(boolean ascend) {
		this.ascend = ascend;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the messageListPage
	 */
	public ListPage<org.eapp.poss.hbean.Message> getMessageListPage() {
		return messageListPage;
	}

	/**
	 * @return the prodInfoListPage
	 */
	public ListPage<ProdInfo> getProdInfoListPage() {
		return prodInfoListPage;
	}

	/**
	 * @param messageBiz the messageBiz to set
	 */
	public void setMessageBiz(IMessageBiz messageBiz) {
		this.messageBiz = messageBiz;
	}

	/**
	 * @param prodInfoBiz the prodInfoBiz to set
	 */
	public void setProdInfoBiz(IProdInfoBiz prodInfoBiz) {
		this.prodInfoBiz = prodInfoBiz;
	}

	/**
	 * @param sendTimeBegin the sendTimeBegin to set
	 */
	public void setSendTimeBegin(Date sendTimeBegin) {
		this.sendTimeBegin = sendTimeBegin;
	}

	/**
	 * @param sendTimeEnd the sendTimeEnd to set
	 */
	public void setSendTimeEnd(Date sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}

	/**
	 * @param prodId the prodId to set
	 */
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	/**
	 * @param prodName the prodName to set
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * @param receiverNo the receiverNo to set
	 */
	public void setReceiverNo(String receiverNo) {
		this.receiverNo = receiverNo;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the saleManagerName
	 */
	public String getSaleManagerName() {
		return saleManagerName;
	}

	/**
	 * @return the message
	 */
	public org.eapp.poss.hbean.Message getMessage() {
		return message;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the custListPage
	 */
	public ListPage<CustInfo> getCustListPage() {
		return custListPage;
	}

	/**
	 * @param custInfoPoint the custInfoPoint to set
	 */
	public void setCustInfoPoint(ICustomerInfoPoint custInfoPoint) {
		this.custInfoPoint = custInfoPoint;
	}

	/**
	 * @return the custStr
	 */
	public String getCustStr() {
		return custStr;
	}

	/**
	 * @param custStr the custStr to set
	 */
	public void setCustStr(String custStr) {
		this.custStr = custStr;
	}
	
}
