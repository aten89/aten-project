/**
 * 
 */
package org.eapp.workflow.svr;

import java.util.Date;

import org.eapp.workflow.WfmContext;
import org.eapp.workflow.db.TraceSession;
import org.eapp.workflow.def.Node;
import org.eapp.workflow.def.Transition;
import org.eapp.workflow.exe.Execution;
import org.eapp.workflow.trace.TracePoint;


/**
 * 日志服务接口
 * @version 2.0
 */
public class LogService {

	private TraceSession traceSession;
	
	public LogService(WfmContext context){
		this.traceSession = context.getTraceSession();
	}
	
	/**
	 * 
	 * @param flowDefineId
	 * @param flowInstanceId
	 * @param refGraphKey
	 * @param elementType
	 * @param elementId
	 * @param actionType
	 */
	private void addTracePoint(String flowDefineId, String flowInstanceId, 
			String refGraphKey, String elementType, String elementId, String actionType) {
		TracePoint tp = new TracePoint();
		tp.setFlowDefineId(flowDefineId);
		tp.setFlowInstanceId(flowInstanceId);
		tp.setRefGraphKey(refGraphKey);
		tp.setElementType(elementType);
		tp.setElementId(elementId);
		tp.setActionType(actionType);
		tp.setSnapTime(new Date());
		traceSession.save(tp);
	}
	
	/**
	 * 记录转向通过轨迹日志
	 * @param node
	 * @param flowInstance
	 */
	public void logThroughTransition(Transition transition, Execution execution) {
		addTracePoint(transition.getFlowDefine().getId(),
				execution.getFlowInstance().getId(),
				transition.getRefGraphKey(),
				TracePoint.ELEMENTTYPE_TRANSITION,
				transition.getId(),
				null);
	}
	
	/**
	 * 记录节点进入轨迹日志
	 * @param node
	 * @param flowInstance
	 */
	public void logEnterNode(Node node, Execution execution) {
		addTracePoint(node.getFlowDefine().getId(),
				execution.getFlowInstance().getId(),
				node.getRefGraphKey(),
				TracePoint.ELEMENTTYPE_NODE,
				node.getId(),
				TracePoint.ACTIONTYPE_ENTER);
	}
	
	/**
	 * 记录节点离开轨迹日志
	 * @param node
	 * @param flowInstance
	 */
	public void logLeaveNode(Node node, Execution execution) {
		addTracePoint(node.getFlowDefine().getId(),
				execution.getFlowInstance().getId(),
				node.getRefGraphKey(),
				TracePoint.ELEMENTTYPE_NODE,
				node.getId(),
				TracePoint.ACTIONTYPE_LEAVE);
	}
}
