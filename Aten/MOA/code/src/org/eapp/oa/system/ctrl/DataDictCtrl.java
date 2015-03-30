package org.eapp.oa.system.ctrl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.dto.DataDictionarySelect;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.rpc.dto.DataDictInfo;



/**
 * 数据字典 控制层
 * @version 2011-03-24
 */
public class DataDictCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 894932421380316916L;

	/**
	 * Constructor of the object.
	 */
	public DataDictCtrl() {
		super();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = HttpRequestHelper.getParameter(request, "act");
		 if ("selectdevtype".equals(act)) {
			// 取得资产类别
			getDeviceTypeSel(request, response);
			return;
		 } else if ("selectallottype".equals(act)) {
			// 取得调拨类型
			getAllotTypeSel(request, response);
			return;
		 } else if ("selectscraptype".equals(act)) {
			// 取得报废类型
			getScrapTypeSel(request, response);
			return;
		 } else if ("selectscrapdisposetype".equals(act)) {
			// 取得html格式的报废类型处理方式
			getScrapDisposeTypeSel(request, response);
			return;
		 } else if ("selectscrapdisposetypexml".equals(act)) {
			// 取得xml格式的报废类型处理方式
			getScrapDisposeTypeXML(request, response);
			return;
		 } else if ("selectscraptypexml".equals(act)) {
			// 取得xml格式的报废类型
			getScrapTypeXML(request, response);
			return;
//		 } 
//		 else if ("getdevusetype".equals(act)) {
//			// 取得设备用途
//			getDevUseTypeSel(request, response);
//			return;
		 } else if ("divdevicetype".equals(act)) {
			// 取得调拨类型
			getDeviceType(request, response);
			return;
//		 } else if ("quickappropn".equals(act)) {
//			 //取得快速审批意见
//			 getQuickApprOpnSel(request, response);
//			 return;
		 } else if ("leavedealtypesel".equals(act)) {
			 getLeaveDealTypeSel(request, response);
			 return;
		 } else if ("selectdevtypexml".equals(act)) {
			// 取得资产类别
			getDeviceTypeSelXml(request, response);
			return;
		 } else if ("ereasel".equals(act)) {
			// 区域
			 getCompanyAreaSel(request, response);
			 return;
		 } 
	}

	/**
	 * 读取设备类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getDeviceTypeSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<DataDictInfo> list = SysCodeDictLoader.getInstance().getDeviceTypeList();
			HTMLResponse.outputHTML(response, new DataDictionarySelect(list).toString());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 读取设备类型列表
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getDeviceType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<DataDictInfo> list = SysCodeDictLoader.getInstance().getDeviceTypeList();
			XMLResponse.outputXML(response, new DataDictionarySelect(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 读取调拨类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getAllotTypeSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String deviceType =HttpRequestHelper.getParameter(request, "deviceType");
			if(deviceType==null){
				return;
			}
			Map<String,List<DataDictInfo>> map = SysCodeDictLoader.getInstance().getAllotTypeMapByKey();
			HTMLResponse.outputHTML(response, new DataDictionarySelect(map.get(deviceType)).toString());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 读取报废类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getScrapTypeSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<DataDictInfo> list = SysCodeDictLoader.getInstance().getScrapTypeList();
			HTMLResponse.outputHTML(response, new DataDictionarySelect(list).toString());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 读取html格式的报废类型处理方式下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getScrapDisposeTypeSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String deviceType =HttpRequestHelper.getParameter(request, "deviceType");
			if(deviceType==null){
				return;
			}
			Map<String,List<DataDictInfo>> map = SysCodeDictLoader.getInstance().getScrapDisposeTypeMapByKey();
			HTMLResponse.outputHTML(response, new DataDictionarySelect(map.get(deviceType)).toString());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 读取xml格式的报废类型处理方式下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getScrapDisposeTypeXML(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String deviceType =HttpRequestHelper.getParameter(request, "deviceType");
			if(deviceType==null){
				return;
			}
			Map<String,List<DataDictInfo>> map = SysCodeDictLoader.getInstance().getScrapDisposeTypeMapByKey();
			XMLResponse.outputXML(response, new DataDictionarySelect(map.get(deviceType)).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 读取xml格式的报废类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getScrapTypeXML(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<DataDictInfo> list = SysCodeDictLoader.getInstance().getScrapTypeList();
			XMLResponse.outputXML(response, new DataDictionarySelect(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 获取设备用途
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
//	private void getDevUseTypeSel(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		try {
//			String dataValue = HttpRequestHelper.getParameter(request, "dataValue");
//			Map<String, List<DataDictInfo>> map = SysCodeDictLoader.getInstance().getDevUseTypesMapbyKey();
//			List<DataDictInfo> list = null;
//			if(map!=null && map.containsKey(dataValue)){
//				list = map.get(dataValue);
//				
//			}
//			XMLResponse.outputXML(response, new DataDictionarySelect(list).toDocument());
//		} catch (Exception e) {
//			e.printStackTrace();
//			XMLResponse.outputStandardResponse(response, 0, "系统出错");
//			return;
//		}
//	}
	
//	private void getQuickApprOpnSel(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		try {
//			List<DataDictInfo> list = SysCodeDictLoader.getInstance().getQuickApprOpn();
//			HTMLResponse.outputHTML(response, new DataDictionarySelect(list).toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			XMLResponse.outputStandardResponse(response, 0, "系统出错");
//			return;
//		}
//	}
//	public String initNonProductSel(){
//		return null;
//	}		
	private void getLeaveDealTypeSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			
			String deviceType =HttpRequestHelper.getParameter(request, "deviceType");
			if(deviceType==null){
				return;
			}
			Map<String,List<DataDictInfo>> map = SysCodeDictLoader.getInstance().getLeaveDealTypeMapByKey();
			HTMLResponse.outputHTML(response, new DataDictionarySelect(map.get(deviceType)).toString());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	/**
	 * 读取设备类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getDeviceTypeSelXml(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			List<DataDictInfo> list = SysCodeDictLoader.getInstance().getDeviceTypeList();
			XMLResponse.outputXML(response, new DataDictionarySelect(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	private void getCompanyAreaSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map<String, DataDictInfo> areaMap = SysCodeDictLoader.getInstance().getAreaMap();
		HTMLResponse.outputHTML(response, new DataDictionarySelect(areaMap.values()).toString());
	}
	
}
