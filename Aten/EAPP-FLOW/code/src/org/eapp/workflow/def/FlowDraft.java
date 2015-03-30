/**
 * 
 */
package org.eapp.workflow.def;

/**
 * 流程定义草图，记录修改中流程定义的XML描述文件，并生成版本号
 * 启动时再将XML描述文件转成对像保存，并保持版本号不变
 * 
 * @author 卓诗垚
 * @version 1.0
 */
public class FlowDraft implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4063541574823682065L;
	//流程未启用,
	public static final String FLOW_STATE_NO_ENABLE = "NO_ENABLE";
	String id;
	//流程标识
	protected String flowKey;
	//被修改流程版本
	protected long version;
	//新的流程版本
	protected long newVersion;
	//流程定义的XML描述
	protected String textDefine;
	
	//流程名称
	protected String name;
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
	 * @return the flowKey
	 */
	public String getFlowKey() {
		return flowKey;
	}
	/**
	 * @param flowKey the flowKey to set
	 */
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}
	/**
	 * @return the xmlDefine
	 */
	public String getTextDefine() {
		return textDefine;
	}
	/**
	 * @param xmlDefine the xmlDefine to set
	 */
	public void setTextDefine(String textDefine) {
		this.textDefine = textDefine;
	}
	/**
	 * @return the oldVersion
	 */
	public long getNewVersion() {
		return newVersion;
	}
	/**
	 * @param newVersion the oldVersion to set
	 */
	public void setNewVersion(long newVersion) {
		this.newVersion = newVersion;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FlowElement))
			return false;
		final FlowElement other = (FlowElement) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
