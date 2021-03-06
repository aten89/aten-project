/**
 * 
 */
package org.eapp.oa.system.dto;

import org.eapp.util.web.DataFormatUtil;

/**
 * 生成下拉列表组件的HTML片段
 * @author zsy
 * @version 
 */
public abstract class HTMLOptionsDTO {
	/**
	 * 生成下拉列表选项
	 * @param key
	 * @param value
	 * @return
	 */
	public String createOption(String key, String value) {
		StringBuffer out = new StringBuffer();
		out.append("<div>").append(DataFormatUtil.noNullValue(key)).append("**")
				.append(DataFormatUtil.noNullValue(value)).append("</div>\n");
		return out.toString();
	}
	/**
	 * 生成下拉列表选项
	 * @param key
	 * @param value
	 * @return
	 */
	public String createOption(Integer key, Integer value) {
		StringBuffer out = new StringBuffer();
		out.append("<div>").append(DataFormatUtil.noNullValue(key)).append("**")
				.append(DataFormatUtil.noNullValue(value)).append("</div>\n");
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
			out.append("<div isselected=\"true\">").append(DataFormatUtil.noNullValue(key)).append("**")
				.append(DataFormatUtil.noNullValue(value)).append("</div>\n");
		}else{
			out.append("<div>").append(DataFormatUtil.noNullValue(key)).append("**")
			.append(DataFormatUtil.noNullValue(value)).append("</div>\n");
		}
		return out.toString();
	}
}
