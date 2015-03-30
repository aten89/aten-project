package org.eapp.workflow.def.parse;

import org.eapp.workflow.def.FlowDefine;

/**
 * 将流程定义文件转化成对像
 * @author zhuoshiyao
 */
public abstract class WfdlReader {
	/**
	 * 流程定义对象
	 */
	protected FlowDefine flowDefine;
	
	public WfdlReader(String text) {
		readFlowDefine(text);
	}
	
	/**
	 * 获取解析后的流程定义对象
	 * @return
	 */
	public FlowDefine getFlowDefine() {
		if (flowDefine == null) {
			throw new ParserException();
		}
		return flowDefine;
	}
	
	/**
	 * 读取流程定义
	 * @param text 定义文本
	 * @return 流程定义对象
	 * @throws ParserException
	 */
	protected abstract void readFlowDefine(String text);
}
