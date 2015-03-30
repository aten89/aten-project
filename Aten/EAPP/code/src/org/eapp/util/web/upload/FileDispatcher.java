/**
 * 
 */
package org.eapp.util.web.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 附件转发
 * 对文件的定位与查找
 * 
 * @author 卓诗垚
 * @version May 8, 2007
 */
public final class FileDispatcher {
	
	/**
	 * 不需要创建实例
	 */
	private FileDispatcher() {
		
	}
	
	/**
	 * 配置信息
	 */
	private static UploadConfig config; 
	
	/**
	 * 获取配置信息
	 * @return 配置信息
	 */
	public static UploadConfig getConfig() {
		if (config == null) {
			//未配置属性
			throw new IllegalArgumentException("未配置属性");
		}
		return config;
	}

	public static void setConfig(UploadConfig config) {
		FileDispatcher.config = config;
	}
	
	/**
	 * 取得相对路径
	 * 从设定的RootFolder开始
	 * @param pathName 路径名
	 * @return 相对路径
	 */
	public static String getAbsPath(String pathName) {
		if (pathName == null) {
			throw new IllegalArgumentException();
		}
		return getConfig().getRootFolder() + pathName;
	}
	
	/**
	 * 根据相对路径查找文件,
	 * 从配置的保存路径及所有查找路径搜索
	 * 如果不存在返回空
	 * @param absPath 数据库存储的路径名称，
	 * 		如upload/news/img/aa.jpg
	 * @return 绝对路径，
	 * 		如：D:/aa/upload/news/img/aa.jpg
	 */
	public static File findFile(String absPath) {
		if (absPath == null) {
			return null;
		}
		File file = null;
		
		if (getConfig().isEnableUploadPath()) {
			//如果开启路径设置
			//从保存路径下查找
			file = new File(getConfig().getSavePath() + absPath);
			if (file.isFile()) {
				//找到返回
				return file;
			}
			//从查找目录下查找
			if (getConfig().getFindPaths() != null) {
				//逐个查找
				for (String findPath : getConfig().getFindPaths()) {
					file = new File(findPath + absPath);
					if (file.isFile()) {
						//找到返回
						return file;
					}
				}
			}
		} else { 
			//否则，从应用目录下找
			file = new File(getConfig().getBasePath() + absPath);
			if (file.isFile()) {
				return file;
			}
		}
		return null;
	}
	
	/**
	 * 给定目录名在上传目录下所有可能存在的绝对路径，
	 * 包括保存路径与查找路径
	 * 
	 * @param absDirName 相对路径
	 * 		如： upload/news/img/
	 * @return 真实路径
	 * 		如：{D:/aa/upload/news/img/,F:/files/upload/news/img/}
	 */
	public static List<File> findDirs(String absDirName) {
		if (absDirName == null) {
			return null;
		}
		ArrayList<File> fList = new ArrayList<File>();
		File file = null;
		if (getConfig().isEnableUploadPath()) {
			//如果开启路径设置
			//从保存路径下查找
			file = new File(getConfig().getSavePath() + absDirName);
			if (file.isDirectory()) {
				fList.add(file);
			}
			//从查找目录下查找
			if (getConfig().getFindPaths() != null) {
				//逐个查找
				for (String findPath : getConfig().getFindPaths()) {
					file = new File(findPath + absDirName);
					if (file.isDirectory()) {
						fList.add(file);
					}
				}
			}
		} else {
			//否则，从应用目录下找
			file = new File(getConfig().getBasePath() + absDirName);
			if (file.isDirectory()) {
				fList.add(file);
			}
		}
		return fList;
	}
	
	
	/**
	 * 取得该文件夹的保存路径
	 * @param absDirName 相对路径如： news/img/
	 * @return saveDir + absDir 的路径
	 */
	public static String getSaveDir(String absDirName) {
		if (absDirName == null) {
			return null;
		}
		if (getConfig().isEnableUploadPath()) {
			//如果开启路径设置
			return getConfig().getSavePath() + absDirName;
		} else {
			//否则，从应用目录下找
			return getConfig().getBasePath() + absDirName;
		}
	}
	
	
	/**
	 * 取得临时文件夹绝对路径
	 * @return 绝对路径
	 */
	public static String getTempDir() {
		//临时文件夹肯定要设在保存路径下
		return getSaveDir(getTempAbsDir());
	}
	
	
	/**
	 * 取得临时文件夹相对路径名称
	 * @return 相对路径
	 */
	public static String getTempAbsDir() {
		//定位到相对路径
		return getAbsPath(getConfig().getTempFolder());
	}
}
