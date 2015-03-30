/**
 * 
 */
package org.eapp.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IActionBiz;
import org.eapp.dao.param.ActionQueryParameters;
import org.eapp.dto.ActionSelect;
import org.eapp.exception.EappException;
import org.eapp.hbean.Action;
import org.eapp.util.hibernate.ListPage;

/**
 * @version
 */
public class ActionAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7294486562729366421L;
	private static final Log log = LogFactory.getLog(ActionAction.class);
	
	private IActionBiz actionBiz;

	//IN PARAMS
	private int pageNo;
	private int pageSize;
	private String actionID;
	private String actionKey;
	private String name;
	private String logoURL;
	private String tips;
	private String description;
	private String moduleID;
	
	//OUT PARAMS
	private ListPage<Action> actInfoPage;
	private List<Action> actInfos;
	private Action actInfo;
	
	private String htmlValue;//输出HTML内容
	
	public void setActionBiz(IActionBiz actionBiz) {
		this.actionBiz = actionBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setActionID(String actionID) {
		this.actionID = actionID;
	}

	public void setActionKey(String actionKey) {
		this.actionKey = actionKey;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public ListPage<Action> getActInfoPage() {
		return actInfoPage;
	}

	public List<Action> getActInfos() {
		return actInfos;
	}

	public Action getActInfo() {
		return actInfo;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	/**
	 * 页面初始化
	 */
	public String initQueryPage() {
		return success();
	}
	
	//查询
	public String queryAction() {
		ActionQueryParameters aqp = new ActionQueryParameters();
		aqp.setPageSize(pageSize);
		aqp.setPageNo(pageNo);
		if (StringUtils.isNotBlank(name)) {
			aqp.setActionName(name);
		}
		if (StringUtils.isNotBlank(actionKey)) {
			aqp.setActionKey(actionKey);
		}
		aqp.addOrder("name", true);
		try {
			actInfoPage = actionBiz.queryActions(aqp);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	//初始化编辑页面
	public String initFrame() {
		return success();
	}
	
	/**
	 * 初始化新增页面
	 */
	public String initAdd() {
		return success();
	}
	
	/**
	 * 新增操作
	 */
	public String addAction() {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(actionKey)) {
			return error("参数不能为空");
		}

		if (actionKey.indexOf("_") > 0) {//一个权限点控制多个URL时用“_”分隔（如m/user?act=modify、m/user?act=modify_xx、m/user?act=modify_yy都等于modify的权限）
			return error("actionkey不能包含“_”");
		}
		try {
			Action action = actionBiz.addAction(actionKey, name, logoURL, tips, description);
			ActionLogger.log(getRequest(), action.getActionID(), action.toString());
			return success(action.getActionID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 初始化修改页面
	 */
	public String initModify() {
		if (StringUtils.isBlank(actionID)) {
			return error("参数不能为空");
		}
		actInfo = actionBiz.getAction(actionID);
		return success();
	}
	
	/**
	 * 修改动作信息
	 */
	public String modifyAction() {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(actionID)) {
			return error("参数不能为空");
		}
		
		try {
			Action action = actionBiz.modifyAction(actionID, name, logoURL, tips, description);
			ActionLogger.log(getRequest(), actionID, action.toString());
			return success();
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	/**
	 * 删除动作
	 */
	public String deleteAction() {
		if (StringUtils.isBlank(actionID)) {
			return error("参数不能为空");
		}
		
		try {
			Action action = actionBiz.deleteAction(actionID);
			ActionLogger.log(getRequest(), action.getActionID(), action.toString());
			return success(action.getActionID());
		} catch (EappException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 查看页面
	 */
	public String viewAction() {
		if (StringUtils.isBlank(actionID)) {
			return error("参数不能为空");
		}
		actInfo = actionBiz.getAction(actionID);
		return success();
	}

	public String loadExcludeActions() {
		if (StringUtils.isBlank(moduleID)) {
			return error("参数不能为空");
		}
		
		try {
			actInfos = actionBiz.getExcludeActions(moduleID, name);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	public String loadActionSelect() {
		List<Action> acts = actionBiz.getActionsByModuleID(moduleID);
		htmlValue = new ActionSelect(acts).toString();
		return success();
	}
}
