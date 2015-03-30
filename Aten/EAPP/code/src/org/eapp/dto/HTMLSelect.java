/**
 * 
 */
package org.eapp.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * 生成下拉列表组件的HTML片段
 * @author zsy
 * @version 
 */
public abstract class HTMLSelect {
	/**
	 * 生成下拉列表选项
	 * @param key
	 * @param value
	 * @return
	 */
	public String createOption(String key, String value) {
		StringBuffer out = new StringBuffer();
		out.append("<div>").append(StringUtils.trimToEmpty(key)).append("**")
				.append(StringUtils.trimToEmpty(value)).append("</div>\n");
		return out.toString();
	}
	
	/**
	 * 有默认值的情况，在DIV中加上isselected="true"
	 * @param key
	 * @param value
	 * @param defaultOption
	 * @return
	 */
	public String createOption(String key, String value, String defaultOption) {
		StringBuffer out = new StringBuffer();
		if (key.equals(defaultOption)){
			out.append("<div isselected=\"true\">").append(StringUtils.trimToEmpty(key)).append("**")
				.append(StringUtils.trimToEmpty(value)).append("</div>\n");
		}else{
			out.append("<div>").append(StringUtils.trimToEmpty(key)).append("**")
			.append(StringUtils.trimToEmpty(value)).append("</div>\n");
		}
		return out.toString();
	}
}
