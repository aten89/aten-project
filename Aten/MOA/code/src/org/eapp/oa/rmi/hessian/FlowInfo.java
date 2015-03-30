package org.eapp.oa.rmi.hessian;

import java.io.Serializable;


/**
 * FlowConfig entity. @author MyEclipse Persistence Tools
 */

public class FlowInfo implements Serializable {

	private static final long serialVersionUID = 3720716896866557124L;
  	private String flowClass;	//流程类别
   	private String flowKey;	//流程key
   	private String flowName;	//流程名称
    private Long flowVersion;	//流程版本
	public String getFlowClass() {
		return flowClass;
	}
	public void setFlowClass(String flowClass) {
		this.flowClass = flowClass;
	}
	public String getFlowKey() {
		return flowKey;
	}
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public Long getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(Long flowVersion) {
		this.flowVersion = flowVersion;
	}
  
   
}