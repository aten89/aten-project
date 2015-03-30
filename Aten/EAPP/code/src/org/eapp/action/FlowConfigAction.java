/**
 * 
 */
package org.eapp.action;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowConfigBiz;
import org.eapp.dao.param.FlowConfigQueryParameters;
import org.eapp.dto.FlowTracePointBean;
import org.eapp.exception.EappException;
import org.eapp.hbean.FlowConfig;
import org.eapp.util.hibernate.ListPage;

/**
 * @version
 */
public class FlowConfigAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7294486562729366421L;
	private static final Log log = LogFactory.getLog(FlowConfigAction.class);
	
	private IFlowConfigBiz flowConfigBiz;

	//IN PARAMS
	private int pageNo;
	private int pageSize;
	private String flowClass;	//流程类别
    private String flowKey;	//流程key
    private String flowName;	//流程名称
    private int draftFlag;	//草稿标识
    private String flowJson;//
    private String flowInstanceId;
	
	//OUT PARAMS
	private ListPage<FlowConfig> flowConfigPage;
	private Set<FlowTracePointBean> flowTracePoints;
	
	public void setFlowConfigBiz(IFlowConfigBiz flowConfigBiz) {
		this.flowConfigBiz = flowConfigBiz;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public void setDraftFlag(int draftFlag) {
		this.draftFlag = draftFlag;
	}
	
	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}

	public void setFlowJson(String flowJson) {
		this.flowJson = flowJson;
	}

	public String getFlowJson() {
		return flowJson;
	}

	public ListPage<FlowConfig> getFlowConfigPage() {
		return flowConfigPage;
	}

	public Set<FlowTracePointBean> getFlowTracePoints() {
		return flowTracePoints;
	}

	/**
	 * 页面初始化
	 */
	public String initQueryPage() {
		return success();
	}
	
	//查询
	public String queryFlowConfig() {
		FlowConfigQueryParameters qp = new FlowConfigQueryParameters();
		qp.setPageSize(pageSize);
		qp.setPageNo(pageNo);
		if (StringUtils.isNotBlank(flowClass)) {
			qp.setFlowClass(flowClass);
		}
		if (StringUtils.isNotBlank(flowName)) {
			qp.setFlowName(flowName);
		}
		if (StringUtils.isNotBlank(flowKey)) {
			qp.setFlowKey(flowKey);
		}
		qp.setDraftFlag(draftFlag);
		qp.addOrder("flowClass", true);
		qp.addOrder("flowName", true);
		try {
			flowConfigPage = flowConfigBiz.queryFlowConfig(qp);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	//新增
	public String addFlowConfig() {
		if (StringUtils.isBlank(flowJson)) {
			return error("流程JSON不能为空");
		}
		try {
			FlowConfig fc = flowConfigBiz.addFlowDraft(flowJson);
			ActionLogger.log(getRequest(), fc.getConfId(), fc.toString());
			return success(fc.getFlowKey());
		} catch(EappException e) {
			return error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//修改
	public String modifyFlowConfig() {
		if (StringUtils.isBlank(flowKey) || StringUtils.isBlank(flowJson)) {
			return error("参数不能为空");
		}
		try {
			FlowConfig fc = flowConfigBiz.modifyFlowDraft(flowKey, flowJson);
			ActionLogger.log(getRequest(), fc.getConfId(), fc.toString());
			return success();
		} catch(EappException e) {
			return error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//删除
	public String deleteFlowConfig() {
		if (StringUtils.isBlank(flowKey)) {
			return error("流程标识不能为空");
		}
		try {
			FlowConfig fc = flowConfigBiz.deleteFlowDraft(flowKey);
			if (fc != null) {
				ActionLogger.log(getRequest(), fc.getConfId(), fc.toString());
			}
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//发布启用
	public String enableFlowConfig() {
		if (StringUtils.isBlank(flowKey)) {
			return error("流程标识不能为空");
		}
		try {
			FlowConfig fc = flowConfigBiz.txPublishFlowDraft(flowKey);
			ActionLogger.log(getRequest(), fc.getConfId(), fc.toString());
			return success();
		} catch(EappException e) {
			return error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//禁用
	public String disableFlowConfig() {
		if (StringUtils.isBlank(flowKey)) {
			return error("流程标识不能为空");
		}
		try {
			FlowConfig fc = flowConfigBiz.txDisableFlow(flowKey);
			ActionLogger.log(getRequest(), fc.getConfId(), fc.toString());
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//加载流程的文本格式定义
	public String loadFlowAsString() {
		if (StringUtils.isBlank(flowKey)) {
			return error("流程标识不能为空");
		}
		try {
			flowJson = flowConfigBiz.getFlowAsString(flowKey, FlowConfig.FLOW_FLAG_DRAFT == draftFlag);
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//加载流程的文本格式定义
	public String loadFlowAsStringByInst() {
		if (StringUtils.isBlank(flowInstanceId)) {
			return error("流程实例ID不能为空");
		}
		try {
			flowJson = flowConfigBiz.getFlowAsString(flowInstanceId);
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	public String loadFlowTracePoints() {
		if (StringUtils.isBlank(flowInstanceId)) {
			return error("流程实例ID不能为空");
		}
		try {
			flowTracePoints = flowConfigBiz.getFlowTracePoints(flowInstanceId);
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
	
	//修改正式流程时，如果不存在草稿则创建
	public String copyFlowToDraft() {
		if (StringUtils.isBlank(flowKey)) {
			return error("流程标识不能为空");
		}
		try {
			flowConfigBiz.txCopyFlowToDraft(flowKey);
			return success();
		} catch(EappException e) {
			return error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}
}
