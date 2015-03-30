/**
 * 
 */
package org.eapp.blo.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IFlowHandlerBiz;
import org.eapp.dao.IFlowHandlerDAO;
import org.eapp.dao.param.FlowHandlerQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowHandler;
import org.eapp.util.hibernate.ListPage;


/**
 * 动作业务逻辑层
 * @version 
 */
public class FlowHandlerBiz implements IFlowHandlerBiz {
	
	private IFlowHandlerDAO flowHandlerDAO;

	public void setFlowHandlerDAO(IFlowHandlerDAO flowHandlerDAO) {
		this.flowHandlerDAO = flowHandlerDAO;
	}

	@Override
	public FlowHandler getFlowHandler(String handId) {
		return flowHandlerDAO.findById(handId);
	}

	@Override
	public FlowHandler deleteFlowHandler(String handId) {
		FlowHandler flowHandler = flowHandlerDAO.findById(handId);
		if(flowHandler == null){
			throw new IllegalArgumentException("ID在数据库中不存在");
		}
		flowHandlerDAO.delete(flowHandler);
		return flowHandler;
	}
	
	@Override
	public FlowHandler addFlowHandler(String flowClass, String name, String handlerClass, 
			String type, boolean globalFlag) throws EappException {
		if(StringUtils.isBlank(handlerClass)){
			throw new IllegalArgumentException("类名不能为空");
		}
		if(StringUtils.isBlank(flowClass) && !globalFlag){
			throw new IllegalArgumentException("非全局动作的流程类别不能为空");
		}
		
		if(flowHandlerDAO.testHandlerClassRepeat(handlerClass, flowClass, globalFlag, null)){
			throw new EappException("类名不能重复");
		}
		FlowHandler fh = new FlowHandler();
		fh.setGlobalFlag(globalFlag);
		if (!globalFlag) {
			fh.setFlowClass(flowClass);
		}
		fh.setName(name);
		fh.setHandlerClass(handlerClass);
		fh.setType(type);
		flowHandlerDAO.save(fh);
		return fh;
	}
	
	@Override
	public FlowHandler modifyFlowHandler(String handId, String flowClass, String name, 
			String handlerClass, String type, boolean globalFlag) throws EappException {
		if(StringUtils.isBlank(handId) || StringUtils.isBlank(handlerClass)){
			throw new IllegalArgumentException();
		}
		if(StringUtils.isBlank(flowClass) && !globalFlag){
			throw new IllegalArgumentException("非全局动作的流程类别不能为空");
		}
		
		FlowHandler fh = flowHandlerDAO.findById(handId);
		//类别有变或名称有变时才要再次检测
		if(flowHandlerDAO.testHandlerClassRepeat(handlerClass, flowClass, globalFlag, fh.getHandId())){
			throw new EappException("类名不能重复");
		}
		
		fh.setGlobalFlag(globalFlag);
		if (!globalFlag) {
			fh.setFlowClass(flowClass);
		} else {
			fh.setFlowClass(null);
		}
		fh.setName(name);
		fh.setHandlerClass(handlerClass);
		fh.setType(type);
		flowHandlerDAO.update(fh);
		return fh;
	}
	
	@Override
	public ListPage<FlowHandler> queryFlowHandlers(FlowHandlerQueryParameters aqp) {
		return flowHandlerDAO.queryFlowHandlers(aqp);
	}
	

	@Override
	public List<FlowHandler> getFlowHandlers(String flowClass, String handlerType) {
		return flowHandlerDAO.findFlowHandlers(flowClass, handlerType);
	}
}
