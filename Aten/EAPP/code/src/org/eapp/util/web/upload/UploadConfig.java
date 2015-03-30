/**
 * 
 */
package org.eapp.util.web.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 附件相关的配置信息
 * 可以在web应用启动时，从配置文件解析初始化
 * 配置文件格式如：
 * <!-- 配置上传附件的路径--> 
 * <attachment><!-- enable启用开关--> 
 * 		<upload-config><!-- 附件上传配置（默认即可）--> 
 * 			<root-folder>upload/</root-folder>
 * 			<temp-folder>temp/</temp-folder>
 * 			<img-ext>.jpg.jpeg.gif.bmp.png</img-ext>
 * 		</upload-config>
 * 		
 * 		<upload-paths enable="false"><!-- 附件路径设置--> 
 * 			<path save="true">D:/aa/</path><!-- save="true"为保存路径 必须只设一个	-->
 * 			<path>F:/aa1/</path>
 * 			<path>F:/aa1/</path>
 * 		</upload-paths>
 * 		
 * 		<referer-check enable="false"><!-- 附件链接来源验证开关--> 
 *			<![CDATA[(127.0.0.1|localhost|richmap.cn|192.168)]]><!-- 内容是正则格式--> 
 *		</referer-check>
 * </attachment>
 * 
 * @author 卓诗垚
 * @version Jan 14, 2009
 */
public class UploadConfig {
	/**
	 * 附件的根文件夹
	 */
	private String rootFolder;
	/**
	 * 临时文件夹
	 */
	private String tempFolder;
	/**
	 * 图片格式
	 */
	private String imgExt;
	/**
	 * 应用根目录绝对路径
	 */
	private String basePath;
	/**
	 * 是否配置上传文件的目录到其他路径
	 */
	private boolean enableUploadPath;
	/**
	 * 上传文件目录的绝对路径
	 */
	private String savePath;
	/**
	 * 除savePath外图片搜索路径
	 */
	private List<String> findPaths;
	/**
	 * 是否验证附件链接来源
	 */
	private boolean enableRefererCheck;
	/**
	 * 信任主机地址
	 */
	private String accreditHosts;
	
	/**
	 * 添加查找路径
	 * @param path 查找路径
	 */
	public void addFindPath(String path) {
		if (findPaths == null) {
			findPaths = new ArrayList<String>();
		}
		findPaths.add(path);
	}
	
	/**
	 * 检查是否信任地址
	 * @param host 主机地址
	 * @return true/false
	 */
	public boolean isAccreditHost(String host) {
		if (host == null) {
			return true;
		}
		if (accreditHosts == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(accreditHosts); 
		Matcher matcher = pattern.matcher(host);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否图片格式
	 * @param ext 图片格式
	 * @return true/false
	 */
	public boolean isImgExt(String ext) {
		if (imgExt == null || ext == null) {
			return false;
		}
		return imgExt.indexOf(ext.toLowerCase()) >= 0;
	}
	
	public String getRootFolder() {
		return rootFolder;
	}
	/**
	 * 设置属性
	 * @param rootFolder 附件的根文件夹
	 */
	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}
	public String getTempFolder() {
		return tempFolder;
	}
	/**
	 * 设置属性
	 * @param tempFolder 临时文件夹
	 */
	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}
	public String getImgExt() {
		return imgExt;
	}
	/**
	 * 统一转为小写
	 * @param imgExt 图片扩展名
	 */
	public void setImgExt(String imgExt) {
		if (imgExt != null) {
			this.imgExt = imgExt.toLowerCase();
		}
	}
	public String getBasePath() {
		return basePath;
	}
	/**
	 * 设置属性
	 * @param basePath 应用根目录绝对路径
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public boolean isEnableUploadPath() {
		return enableUploadPath;
	}
	/**
	 * 设置属性
	 * @param enableUploadPath 是否配置上传文件的目录到其他路径
	 */
	public void setEnableUploadPath(boolean enableUploadPath) {
		this.enableUploadPath = enableUploadPath;
	}
	public String getSavePath() {
		return savePath;
	}
	/**
	 * 设置属性
	 * 可设置相对路径可绝对路径
	 * 如D:/XXdir、../../XXdir
	 * @param savePath 上传文件目录的绝对路径
	 */
	public void setSavePath(String savePath) {
		if (savePath != null) {
			File path = new File(savePath);
			try {
				//如果是相对路径转为绝对路径
				this.savePath = path.getCanonicalPath() + "/";
			} catch (IOException e) {
				//转化失败直接原值设置
				this.savePath = savePath;
			}
		}
		
	}
	public List<String> getFindPaths() {
		return findPaths;
	}
	/**
	 * 设置属性
	 * @param findPaths 除savePath外图片搜索路径
	 */
	public void setFindPaths(List<String> findPaths) {
		this.findPaths = findPaths;
	}
	public boolean isEnableRefererCheck() {
		return enableRefererCheck;
	}
	/**
	 * 设置属性
	 * @param enableRefererCheck 是否验证附件链接来源
	 */
	public void setEnableRefererCheck(boolean enableRefererCheck) {
		this.enableRefererCheck = enableRefererCheck;
	}
	public String getAccreditHosts() {
		return accreditHosts;
	}
	/**
	 * 设置属性
	 * @param accreditHosts 信任主机地址
	 */
	public void setAccreditHosts(String accreditHosts) {
		this.accreditHosts = accreditHosts;
	}
	
}
