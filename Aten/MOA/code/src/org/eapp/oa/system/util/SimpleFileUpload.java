
package org.eapp.oa.system.util;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eapp.oa.system.util.web.HttpRequestHelper;



/**
 * 基于HTML的文件上传组件,是对commons－fileupload的简单封装
 * 构造对象后调用doParse()方法进行解悉上传的文件，然后就可以通过getFileDataList()方法取得文件列表，
 * getParameter(String key)方法取得普通表单域的值
 * modify by zsy 2008.5.23
 * @author 林良益 卓诗垚
 * @version 2.0
 */
public class SimpleFileUpload {
	
	//上传文件时用于临时存放文件的内存大小,这里是默认10K.多于的部分将临时存在硬盘
	private int sizeThreshold = DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD;
	//临时文件的目录
	private String repositoryPath;
	//文件最大上传大小，等于－1表示没有最大值限制
	private int sizeMax = -1;
	//字符编码，当读取上传表单的各部分时会用到该encoding
	private String encoding;
	
	private HttpServletRequest request;
	//表单域存储映射表
	private Map<String, List<String>> params = new HashMap<String, List<String>>();
	//文件存储列表
	private List<FileItem> fileDataList = new ArrayList<FileItem>();
	
	/**
	 * 构造SimpleFileUpload对象
	 * @param request HttpServletRequest对象
	 * @param maxUploadSize 缓冲区大小
	 */
	public SimpleFileUpload(HttpServletRequest request, int maxUploadSize, String encoding) {
		this.request = request;
		this.sizeMax = maxUploadSize;
		this.encoding = encoding;
	}
	
	public SimpleFileUpload(HttpServletRequest request, int maxUploadSize) {
		this(request, maxUploadSize, HttpRequestHelper.TARGET_ENCODING);
	}
	
	public SimpleFileUpload(HttpServletRequest request, String encoding) {
		this(request, -1, encoding);
	}
	
	public SimpleFileUpload(HttpServletRequest request) {
		this(request, -1, HttpRequestHelper.TARGET_ENCODING);
	}
	
	/**
	 * 解悉上传的文件对象
	 * @throws SizeLimitExceededException
	 */
	@SuppressWarnings("unchecked")
	public void doParse() throws SizeLimitExceededException {	
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//设置上传文件时用于临时存放文件的内存大小
		factory.setSizeThreshold(sizeThreshold);
		//设置存放临时文件的目录
		if (repositoryPath != null) {
			factory.setRepository(new File(repositoryPath));
		}
		
		//  Create a new file upload handler
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		//  Set overall request size constraint
		fileUpload.setSizeMax(sizeMax);
		if (encoding != null) {
			fileUpload.setHeaderEncoding(encoding);
		}
		
		List<FileItem> fileItems = null;
		try { 	
			fileItems = fileUpload.parseRequest(request); 	
			for(FileItem fileItem : fileItems) {
				if (fileItem == null) {
					continue;
				}
				if(fileItem.isFormField()) {
				 //   String value = fileItem.getString("utf-8");
					String value = fileItem.getString();
					String name = fileItem.getFieldName();
					List<String> values = (List<String>)params.get(name);
					if (values == null) {
						values = new ArrayList<String>();
						params.put(name , values);
					}
					values.add(value);
					
				} else {
					fileDataList.add(fileItem);	
				}
			}
		} catch(SizeLimitExceededException slee) {
		    throw slee;
		} catch(FileUploadException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @return the sizeThreshold
	 */
	public int getSizeThreshold() {
		return sizeThreshold;
	}

	/**
	 * 设置临时存放文件的内存大小
	 * @param sizeThreshold the sizeThreshold to set
	 */
	public void setSizeThreshold(int sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}

	/**
	 * @return the repositoryPath
	 */
	public String getRepositoryPath() {
		return repositoryPath;
	}

	/**
	 * 设置临时文件的目录
	 * @param repositoryPath the repositoryPath to set
	 */
	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	/**
	 * @return the sizeMax
	 */
	public int getSizeMax() {
		return sizeMax;
	}

	/**
	 * 设置上传文件的最大值
	 * @param sizeMax the sizeMax to set
	 */
	public void setSizeMax(int sizeMax) {
		this.sizeMax = sizeMax;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * 设置编码类型
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 根据名称取得参数值
	 * @param name
	 * @param encoding
	 * @return
	 */
	public String getParameter(String name){
		String[] values = getParameterValues(name);
		if (values == null || values.length < 1) {
			return null;
		} else {
			return values[0];
		}
	}
	
	/**
	 * 取得参数
	 * @param name
	 * @param encoding
	 * @return
	 */
	public String[] getParameterValues(String name) {
		List<String> values = params.get(name);
		if (values == null) {
			return null;
		}
		return values.toArray(new String[0]);
	}
	
	/**
	 * 取得参数Map
	 * @return
	 */
	public Map<String, List<String>> getParameters() {
		return params;
	}
	
	/**
	 * 取得文件列表
	 * @return
	 */
	public List<FileItem> getFileDataList(){
		return this.fileDataList;
	}
	
	/**
	 * 得到上传文件的文件名
	 * @param item
	 * @return
	 */
	public String getFileName(FileItem item){ 
		if (item == null) {
			return null;
		}
        String fileName = item.getName();   
        fileName = fileName.replaceAll("\\\\", "/");  
        fileName = fileName.substring(fileName.lastIndexOf("/")+1);   
        return fileName;   
    }  
}
