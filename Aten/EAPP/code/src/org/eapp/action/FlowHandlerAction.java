/**
 * 
 */
package org.eapp.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowHandlerBiz;
import org.eapp.dao.param.FlowHandlerQueryParameters;
import org.eapp.dto.FlowHandlerSelect;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowHandler;
import org.eapp.util.hibernate.ListPage;

/**
 * @version
 */
public class FlowHandlerAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7294486562729366421L;
	private static final Log log = LogFactory.getLog(FlowHandlerAction.class);
	
	private IFlowHandlerBiz flowHandlerBiz;

	//IN PARAMS
	private int pageNo;
	private int pageSize;
	private String handId;
	private String flowClass;	//流程类别
	private String name;		//名称
	private String handlerClass;	//类名
	private String type;			//类型
	private String globalFlag;	//是否全局公用
	
	//OUT PARAMS
	private ListPage<FlowHandler> flowHandlerPage;
	private FlowHandler flowHandler;
	private String htmlValue;//输出HTML内容

	public ListPage<FlowHandler> getFlowHandlerPage() {
		return flowHandlerPage;
	}

	public FlowHandler getFlowHandler() {
		return flowHandler;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	public void setFlowHandlerBiz(IFlowHandlerBiz flowHandlerBiz) {
		this.flowHandlerBiz = flowHandlerBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setHandId(String handId) {
		this.handId = handId;
	}

	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setGlobalFlag(String globalFlag) {
		this.globalFlag = globalFlag;
	}

	/**
	 * 页面初始化
	 */
	public String initQueryPage() {
		return success();
	}
	
	//查询
	public String queryFlowHandler() {
		FlowHandlerQueryParameters aqp = new FlowHandlerQueryParameters();
		aqp.setPageSize(pageSize);
		aqp.setPageNo(pageNo);
		if (StringUtils.isNotBlank(name)) {
			aqp.setName(name);
		}
		if (StringUtils.isNotBlank(flowClass)) {
			aqp.setFlowClass(flowClass);
		}
		if (StringUtils.isNotBlank(globalFlag)) {
			aqp.setGlobalFlag("true".equals(globalFlag));
		}
		aqp.addOrder("globalFlag", false);
		aqp.addOrder("name", true);
		try {
			flowHandlerPage = flowHandlerBiz.queryFlowHandlers(aqp);
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
	public String addFlowHandler() {
		if (StringUtils.isBlank(name)) {
			return error("参数不能为空");
		}

		try {
			FlowHandler flowHandler = flowHandlerBiz.addFlowHandler(flowClass, name, handlerClass, type, "true".equals(globalFlag));
			ActionLogger.log(getRequest(), flowHandler.getHandId(), flowHandler.toString());
			return success(flowHandler.getHandId());
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
		if (StringUtils.isBlank(handId)) {
			return error("参数不能为空");
		}
		flowHandler = flowHandlerBiz.getFlowHandler(handId);
		return success();
	}
	
	/**
	 * 修改动作信息
	 */
	public String modifyFlowHandler() {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(handId)) {
			return error("参数不能为空");
		}
		
		try {
			FlowHandler flowHandler = flowHandlerBiz.modifyFlowHandler(handId, flowClass, name, handlerClass, type, "true".equals(globalFlag));
			ActionLogger.log(getRequest(), handId, flowHandler.toString());
			return success(flowHandler.getHandId());
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
	public String deleteFlowHandler() {
		if (StringUtils.isBlank(handId)) {
			return error("参数不能为空");
		}
		
		try {
			FlowHandler flowHandler = flowHandlerBiz.deleteFlowHandler(handId);
			ActionLogger.log(getRequest(), flowHandler.getHandId(), flowHandler.toString());
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 查看页面
	 */
	public String viewFlowHandler() {
		if (StringUtils.isBlank(handId)) {
			return error("参数不能为空");
		}
		flowHandler = flowHandlerBiz.getFlowHandler(handId);
		return success();
	}
	
	//加载流程HANDLER列表
	public String loadFlowHandlerSelect() {
		List<FlowHandler> handlers = flowHandlerBiz.getFlowHandlers(flowClass, type);
		htmlValue = new FlowHandlerSelect(handlers).toString();
		return success();
	}
}
