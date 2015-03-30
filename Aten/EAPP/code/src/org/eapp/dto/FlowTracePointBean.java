package org.eapp.dto;

import java.io.Serializable;

import org.eapp.workflow.trace.TracePoint;

/**
 * 
 * @author zsy
 *
 */
public class FlowTracePointBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3749267893156858575L;
	private String refGraphKey;
	private String elementType;//任务名称
	
	public String getRefGraphKey() {
		return refGraphKey;
	}
	public void setRefGraphKey(String refGraphKey) {
		this.refGraphKey = refGraphKey;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	
	public FlowTracePointBean() {
		
	}
	
	public FlowTracePointBean(TracePoint tracePoin) {
		this.refGraphKey = tracePoin.getRefGraphKey();
		this.elementType = TracePoint.ELEMENTTYPE_NODE.equals(tracePoin.getElementType()) ? "node" : "line";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elementType == null) ? 0 : elementType.hashCode());
		result = prime * result
				+ ((refGraphKey == null) ? 0 : refGraphKey.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FlowTracePointBean other = (FlowTracePointBean) obj;
		if (elementType == null) {
			if (other.elementType != null)
				return false;
		} else if (!elementType.equals(other.elementType))
			return false;
		if (refGraphKey == null) {
			if (other.refGraphKey != null)
				return false;
		} else if (!refGraphKey.equals(other.refGraphKey))
			return false;
		return true;
	}

	
}