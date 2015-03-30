package org.eapp.action;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IActorAccountBiz;
import org.eapp.dao.param.ActorAccountQueryParameters;
import org.eapp.dao.param.RoleQueryParameters;
import org.eapp.dto.AccountSaveBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.ActorAccount;
import org.eapp.hbean.Service;
import org.eapp.hbean.UserAccount;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.hibernate.ListPage;

/**
 * 处理接口帐号管理的请求
 * @author zsy
 * @version
 */
public class ActorAccountAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;
	private static final Log log = LogFactory.getLog(ActorAccountAction.class);
	
	private IActorAccountBiz actorAccountBiz;
	
	private int pageNo;
	private int pageSize;
	private String accountID;
	private String displayName;
	private String credence;
	private String isLock;
	private String changePasswordFlag;
	private String invalidDate;
	private String description;
	private String keyword;
	private String isValid;
	private String order;
	private String type;
	private String[] accountIDs;
	private String[] serviceIDs;
	
	private ListPage<ActorAccount> actorAccountPage;
	private List<ActorAccount> actorAccounts;
	private ActorAccount actorAccount;
	private Set<Service> services;

	public ListPage<ActorAccount> getActorAccountPage() {
		return actorAccountPage;
	}

	public List<ActorAccount> getActorAccounts() {
		return actorAccounts;
	}

	public ActorAccount getActorAccount() {
		return actorAccount;
	}

	public Set<Service> getServices() {
		return services;
	}

	public void setActorAccountBiz(IActorAccountBiz actorAccountBiz) {
		this.actorAccountBiz = actorAccountBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setCredence(String credence) {
		this.credence = credence;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public void setChangePasswordFlag(String changePasswordFlag) {
		this.changePasswordFlag = changePasswordFlag;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAccountIDs(String[] accountIDs) {
		this.accountIDs = accountIDs;
	}

	public void setServiceIDs(String[] serviceIDs) {
		this.serviceIDs = serviceIDs;
	}

	//初始化编辑页面
	public String initFrame() {
		return success();
	}
	
	public String initQuery() {
		return success();
	}
	
	public String queryActor() {
		ActorAccountQueryParameters userQP = new ActorAccountQueryParameters();
		userQP.setPageNo(pageNo);
		userQP.setPageSize(pageSize);
		if (StringUtils.isNotBlank(keyword)) {
			userQP.setKeyword(keyword);
		}
		if (StringUtils.isNotBlank(accountID)) {
			userQP.setAccountID(accountID);
		}
		if (StringUtils.isNotBlank(displayName)) {
			userQP.setDisplayName(displayName);
		}
		if (StringUtils.isNotBlank(isLock)) {
			userQP.setIsLock("Y".equals(isLock));
		}
		if (StringUtils.isNotBlank(isValid)) {
			userQP.setIsValid("Y".equals(isValid));
		}

		if (StringUtils.isNotBlank(order)) {
			userQP.addOrder(order, "A".equals(type));
		} else {
			userQP.addOrder("accountID", true);
		}
		
		try {
			actorAccountPage = actorAccountBiz.queryActorAccount(userQP);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initAdd() {
		return success();
	}
	
	public String addActor() {
		if (StringUtils.isBlank(accountID) || StringUtils.isBlank(displayName) || StringUtils.isBlank(credence)) {
			return error("参数不能为空");
		}
		
		AccountSaveBean account = new AccountSaveBean();
		account.setAccountID(accountID);
		account.setDisplayName(displayName);
		account.setIsLock("Y".equals(isLock));
		if (StringUtils.isNotBlank(invalidDate)) {
			account.setInvalidDate(Timestamp.valueOf(DataFormatUtil.formatTime(invalidDate)));
		}
		account.setChangePasswordFlag(ActorAccount.CHANGEPASSWORD_TRUE.equals(changePasswordFlag) ? 
				ActorAccount.CHANGEPASSWORD_TRUE : ActorAccount.CHANGEPASSWORD_FALSE);
		account.setDescription(description);
		
		try {
			ActorAccount actor = actorAccountBiz.addActor(account, credence);
			//写入日志
			if (actor != null) {
				ActionLogger.log(getRequest(), actor.getActorID(), account.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String initModify() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		actorAccount = actorAccountBiz.getActorByAccountID(accountID);
		return success();
	}
	
	public String modifyActor() {
		if (StringUtils.isBlank(accountID) || StringUtils.isBlank(displayName) || StringUtils.isBlank(credence)) {
			return error("参数不能为空");
		}

		AccountSaveBean account = new AccountSaveBean();
		account.setAccountID(accountID);
		account.setDisplayName(displayName);
		account.setIsLock("Y".equals(isLock));
		if (StringUtils.isNotBlank(invalidDate)) {
			account.setInvalidDate(Timestamp.valueOf(DataFormatUtil.formatTime(invalidDate)));
		}
		account.setChangePasswordFlag(UserAccount.CHANGEPASSWORD_TRUE.equals(changePasswordFlag) ? 
				UserAccount.CHANGEPASSWORD_TRUE : UserAccount.CHANGEPASSWORD_FALSE);
		account.setDescription(description);
		try {
			ActorAccount actor = actorAccountBiz.modifyActor(account, credence);
			//写入日志
			if (actor != null) {
				ActionLogger.log(getRequest(), actor.getActorID(), actor.toString());
			}
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String deleteActors() {
		if (accountIDs == null || accountIDs.length < 1) {
			return error("参数不能为空");
		}
		try {
			List<ActorAccount> actors = actorAccountBiz.deleteActors(accountIDs);
			//写入日志
			if (actors != null) {
				for (ActorAccount actor : actors) {
					ActionLogger.log(getRequest(), actor.getActorID(), actor.toString());
				}
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String viewActor() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		actorAccount = actorAccountBiz.getActorByAccountID(accountID);
		return success();
	}
	
	public String initBindService() {
		return success();
	}
	
	public String loadBindedServices() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}
		try {
			services = actorAccountBiz.getBindingServices(accountID);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String saveBindService() {
		if (StringUtils.isBlank(accountID)) {
			return error("参数不能为空");
		}

		try {
			ActorAccount actor = actorAccountBiz.txBindService(accountID, serviceIDs);
			//写入日志
			if (actor != null) {
				StringBuffer sbf = new StringBuffer(actor.toString());
				if (actor.getServices() != null) {
					sbf.append("\n绑定对象：");
					for (Service s : actor.getServices()) {
						sbf.append("\n").append(s.toString());
					}
					
				}
				ActionLogger.log(getRequest(), actor.getActorID(), sbf.toString());
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadActors() {
		ActorAccountQueryParameters userQP = new ActorAccountQueryParameters();
		userQP.setPageSize(RoleQueryParameters.ALL_PAGE_SIZE);
		if (StringUtils.isNotBlank(keyword)) {
			userQP.setKeyword(keyword);
		}
		
		userQP.setIsLock(false);
		userQP.setIsValid(true);
		userQP.addOrder("accountID", true);
		try {
			ListPage<ActorAccount> page = actorAccountBiz.queryActorAccount(userQP);
			if (page != null) {
				actorAccounts = page.getDataList();
			}
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
}
