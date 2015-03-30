/**
 * 
 */
package org.eapp.action;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowVarBiz;
import org.eapp.dao.param.FlowVarQueryParameters;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowVar;
import org.eapp.util.hibernate.ListPage;

/**
 * @version
 */
public class FlowVarAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7294486562729366421L;
	private static final Log log = LogFactory.getLog(FlowVarAction.class);
	
	private IFlowVarBiz flowVarBiz;

	//IN PARAMS
	private int pageNo;
	private int pageSize;
	private String varId;			//元数据标识
	private String flowClass;	//流程类别
	private String name;		//元数据名称
	private String displayName;	//显示名称
	private String type;			//类型
	private Boolean notNull;		//能否为空
	private String globalFlag;	//是否全局公用
	private String expression;//表达式
    private String flowJson;//
    private String returnType;
	
	//OUT PARAMS
	private ListPage<FlowVar> flowVarPage;
	private List<FlowVar> flowVars;
	private FlowVar flowVar;

	public ListPage<FlowVar> getFlowVarPage() {
		return flowVarPage;
	}

	public List<FlowVar> getFlowVars() {
		return flowVars;
	}

	public FlowVar getFlowVar() {
		return flowVar;
	}

	public void setFlowVarBiz(IFlowVarBiz flowVarBiz) {
		this.flowVarBiz = flowVarBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setVarId(String varId) {
		this.varId = varId;
	}

	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}

	public void setGlobalFlag(String globalFlag) {
		this.globalFlag = globalFlag;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setFlowJson(String flowJson) {
		this.flowJson = flowJson;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * 页面初始化
	 */
	public String initQueryPage() {
		return success();
	}
	
	//查询
	public String queryFlowVar() {
		FlowVarQueryParameters aqp = new FlowVarQueryParameters();
		aqp.setPageSize(pageSize);
		aqp.setPageNo(pageNo);
		if (StringUtils.isNotBlank(displayName)) {
			aqp.setDisplayName(displayName);
		}
		if (StringUtils.isNotBlank(flowClass)) {
			aqp.setFlowClass(flowClass);
		}
		if (StringUtils.isNotBlank(globalFlag)) {
			aqp.setGlobalFlag("true".equals(globalFlag));
		}
		aqp.addOrder("globalFlag", false);
		aqp.addOrder("displayName", true);
		try {
			flowVarPage = flowVarBiz.queryFlowVars(aqp);
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
	public String addFlowVar() {
		if (StringUtils.isBlank(name)) {
			return error("参数不能为空");
		}

		try {
			FlowVar flowVar = flowVarBiz.addFlowVar(flowClass, name, displayName, type, notNull, "true".equals(globalFlag));
			ActionLogger.log(getRequest(), flowVar.getVarId(), flowVar.toString());
			return success(flowVar.getVarId());
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
		if (StringUtils.isBlank(varId)) {
			return error("参数不能为空");
		}
		flowVar = flowVarBiz.getFlowVar(varId);
		return success();
	}
	
	/**
	 * 修改动作信息
	 */
	public String modifyFlowVar() {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(varId)) {
			return error("参数不能为空");
		}
		
		try {
			FlowVar flowVar = flowVarBiz.modifyFlowVar(varId, flowClass, name, displayName, type, notNull, "true".equals(globalFlag));
			ActionLogger.log(getRequest(), varId, flowVar.toString());
			return success(flowVar.getVarId());
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
	public String deleteFlowVar() {
		if (StringUtils.isBlank(varId)) {
			return error("参数不能为空");
		}
		
		try {
			FlowVar flowVar = flowVarBiz.deleteFlowVar(varId);
			ActionLogger.log(getRequest(), flowVar.getVarId(), flowVar.toString());
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}

	/**
	 * 查看页面
	 */
	public String viewFlowVar() {
		if (StringUtils.isBlank(varId)) {
			return error("参数不能为空");
		}
		flowVar = flowVarBiz.getFlowVar(varId);
		return success();
	}
	
	//加载流程变量
	public String loadFlowVars() {
		flowVars = flowVarBiz.getFlowVars(flowClass);
		return success();
	}
	
	//验证表达式
	public String validExpression() {
		if (StringUtils.isBlank(expression)) {
			return error("参数不能为空");
		}
		try {
			flowVarBiz.testExpression(expression, returnType, flowJson);
			return success();
		} catch(EappException e) {
			return error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}


}
