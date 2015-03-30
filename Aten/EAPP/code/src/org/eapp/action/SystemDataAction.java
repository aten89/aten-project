/**
 * 
 */
package org.eapp.action;

import org.eapp.sys.config.SysCodeDictLoader;
import org.eapp.util.web.upload.FileUtil;
/**
 * 系统字典加载
 */
public class SystemDataAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6870381112268389879L;
	
	//初始化页面
	public String initPage() {
		return success();
	}
	
	/**
	 * 重载系统字典
	 */
	public String redeployCacheData() {
		SysCodeDictLoader sysCodeDictLoader = SysCodeDictLoader.getInstance();
		sysCodeDictLoader.initDataDict();
		//清理生成时间在一小时之前的文件
		FileUtil.cleanTempFile(1); 
		return success();
	}
}
