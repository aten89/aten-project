package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.device.blo.IDeviceClassBiz;
import org.eapp.oa.device.blo.IDevicePropertyBiz;
import org.eapp.oa.device.dto.DeviceCfgItemXml;
import org.eapp.oa.device.dto.DeviceClassList;
import org.eapp.oa.device.dto.DevicePropertyXml;
import org.eapp.oa.device.hbean.DeviceCfgItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * Description:设备分类控制器
 * 
 * @author sds
 * @version Sep 1, 2009
 */
public class DeviceClassCtrl extends HttpServlet {

	private static final long serialVersionUID = 1037277321701629079L;
	private IDeviceClassBiz deviceClassBiz;
	private IDevicePropertyBiz devicePropertyBiz;
	public DeviceClassCtrl() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void init() throws ServletException {
		deviceClassBiz = (IDeviceClassBiz) SpringHelper.getSpringContext().getBean("deviceClassBiz");
		devicePropertyBiz = (IDevicePropertyBiz) SpringHelper.getSpringContext().getBean("devicePropertyBiz");
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String act = HttpRequestHelper.getParameter(request, "act");

		if (SysConstants.ADD.equalsIgnoreCase(act)) {
			// 新增设备类别信息
			addDeviceClass(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除设备类别信息
			delDeviceClass(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改设备类别信息
			modifyDeviceClass(request, response);
			return;
		} else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询设备类别信息
			queryDeviceClass(request, response);
			return;
		} else if ("findall".equals(act)) {
			// 取得所有的信息配置
			queryDeviceClass(request, response);
			return;
		} else if ("findallproperty".equals(act)){
			//取得所有设备属性信息
			findAllProperty(request, response);
			return;
		} else if ("findcheckproperty".equals(act)){
			//取得所有设备属性信息
			findCheckProperty(request, response);
			return;
		} else if ("initmodify".equals(act)){
			//取得设备属性信息
			initDeviceClass(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)){
			//查看
			viewDeviceClass(request, response);
			return;
		} else if ("classselect".equals(act)) {
			// 取得所有的信息配置
			queryDeviceClassSelect(request, response);
			return;
		} else if ("classsselassign".equals(act)) {
			// 根据授权获取设备类别
			deviceClassSelectByAssign(request, response);
			return;
		} else if ("getoptionbyclassid".equals(act)) {
			// 根据设备类别id查询设备属性项
			geOptionByClassId(request, response);
			return;
		} else if ("getoptionlistbyclassid".equals(act)) {
			// 根据设备类别id查询设备属性项
			geOptionListByClassId(request, response);
			return;
		} else if ("classsqueryassign".equals(act)) {
			// 根据设备类别id查询设备属性项
			deviceClassListByAssign(request, response);
			return;
		} 
	}

	/**
	 * 新增设备类别
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addDeviceClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String[] cfgItemIds = HttpRequestHelper.getParameters(request, "cfgItemIdList");
		String name = HttpRequestHelper.getParameter(request, "name");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		String classType = HttpRequestHelper.getParameter(request, "classType");

		try {
			DeviceClass deviceClass = deviceClassBiz.txAddDeviceClass(classType, name, remark, cfgItemIds);
			XMLResponse.outputStandardResponse(response, 1, deviceClass.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	/**
	 * 删除设备类别
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void delDeviceClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			throw new IllegalArgumentException("ID不能为空");
		}
		try {
			
			DeviceClass deviceClass = deviceClassBiz.txDelDeviceClass(id);
			XMLResponse.outputStandardResponse(response, 1, deviceClass.getId());
		}  catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		}  catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	/**
	 * 保存修改设备类别
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void modifyDeviceClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			throw new IllegalArgumentException("ID不能为空");
		}

		String[] cfgItemIds = HttpRequestHelper.getParameters(request, "cfgItemIdList");
		String name = HttpRequestHelper.getParameter(request, "name");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		String classType = HttpRequestHelper.getParameter(request, "classType");
		try {
			DeviceClass deviceClass = deviceClassBiz.txModifyDeviceClass(id, classType, name, remark, cfgItemIds);
			XMLResponse.outputStandardResponse(response, 1, deviceClass.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	/**
	 * 查询所有设备类别
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void queryDeviceClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		try {
			// 查询
			QueryParameters qp =new QueryParameters();
			int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
			int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
			qp.setPageNo(pageNo);
			qp.setPageSize(pageSize);
			qp.addOrder("deviceType", true);
			qp.addOrder("name", true);
			ListPage<DeviceClass> listPage = deviceClassBiz.queryDeviceClassPage(qp);
			XMLResponse.outputXML(response, new DeviceClassList(listPage).toPageDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}

	

	/**
	 * 查询所有设备属性选项
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void findAllProperty(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			List<DeviceProperty> list = devicePropertyBiz.queryDevicePropertyList();
			XMLResponse.outputXML(response, new DevicePropertyXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 根据id查询设备已经配置的属性
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void findCheckProperty(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			String id = HttpRequestHelper.getParameter(request, "id");
			List<DeviceCfgItem> list = deviceClassBiz.queryDeviceCfgItem(id);
			XMLResponse.outputXML(response, new DeviceCfgItemXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 初始化设备类别修改页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initDeviceClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		DeviceClass deviceClass = deviceClassBiz.getDeviceClassById(id);
		request.setAttribute("deviceClass", deviceClass);
		request.getRequestDispatcher("/page/device/paramconf/class/edit_class.jsp").forward(request,response);
	}
	
	/**
	 * 查看设备类型详情
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewDeviceClass(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		DeviceClass deviceClass = deviceClassBiz.getDeviceClassById(id);
		request.setAttribute("deviceClass", deviceClass);
		request.getRequestDispatcher("/page/device/paramconf/class/view_class.jsp").forward(request,response);
	}
	
	/**
	 * 查询设备类别下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void queryDeviceClassSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			// 查询
			String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceType");
			String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
			List<DeviceClass> list = deviceClassBiz.getAllDeviceClass(deviceTypeCode,areaCode);
			HTMLResponse.outputHTML(response, new DeviceClassList(list).toString());
	}

	/**
	 * 根据授权查询设备类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deviceClassSelectByAssign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
		Integer assignType = HttpRequestHelper.getIntParameter(request, "assignType", 0);
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		// 查询
		List<DeviceClass> list = loadAssignClass(deviceType, areaCode, assignType, user);
		HTMLResponse.outputHTML(response, new DeviceClassList(list).toString());
	}
	
	/**
	 * 加载已授权的设备类别列表
	 * @param deviceType
	 * @param areaCode
	 * @param assignType
	 * @param user
	 * @return
	 */
	public List<DeviceClass> loadAssignClass(String deviceType, String areaCode, Integer assignType, SessionAccount user) {
		if (user == null) {
			return null;
		}
		List<String> groupNames = new ArrayList<String>();
		if (user.getGroups() != null) {
			for (Name n : user.getGroups()) {
				groupNames.add(n.getName());
			}
		}
		List<String> postNames = new ArrayList<String>();
		if (user.getPosts() != null) {
			for (Name n : user.getPosts()) {
				postNames.add(n.getName());
			}
		}
		return deviceClassBiz.getAssignClass(deviceType, areaCode, assignType, user.getAccountID(), groupNames, postNames);
	}
	
	/**
	 * 根据授权查询设备类型下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deviceClassListByAssign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		String deviceTypeIds = HttpRequestHelper.getParameter(request, "deviceTypeIds");
		Integer assignType = HttpRequestHelper.getIntParameter(request, "assignType", 1);
		String areaCodeIds = HttpRequestHelper.getParameter(request, "areaCodeIds");
		List<String> deviceTypeIdList=null;
		List<String> areaCodeIdList=null;
		if(deviceTypeIds!=null && deviceTypeIds.length()>0){
			deviceTypeIdList= new ArrayList<String>();
			for (String string : deviceTypeIds.split(",")) {
				deviceTypeIdList.add(string);
			}
		}
		if(areaCodeIds!=null && areaCodeIds.length()>0){
			areaCodeIdList= new ArrayList<String>();
			for (String string : areaCodeIds.split(",")) {
				areaCodeIdList.add(string);
			}
		}
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		List<String> groupNames = new ArrayList<String>();
		if (user.getGroups() != null) {
			for (Name n : user.getGroups()) {
				groupNames.add(n.getName());
			}
		}
		List<String> postNames = new ArrayList<String>();
		if (user.getPosts() != null) {
			for (Name n : user.getPosts()) {
				postNames.add(n.getName());
			}
		}
		// 查询
		List<DeviceClass> list = deviceClassBiz.getAssignClassSelect(deviceTypeIdList,areaCodeIdList, assignType, user.getAccountID(), groupNames, postNames);
		XMLResponse.outputXML(response, new DeviceClassList(list).toDocument());
	}
	/**
	 * 根据id查询设备已经配置的属性
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void geOptionByClassId(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			String id = HttpRequestHelper.getParameter(request, "classId");
			List<DeviceCfgItem> list = deviceClassBiz.queryDeviceCfgItem(id);
			HTMLResponse.outputHTML(response, new DeviceCfgItemXml(list).toString());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	/**
	 * 根据id查询设备已经配置的属性
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void geOptionListByClassId(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			String id = HttpRequestHelper.getParameter(request, "classId");
			List<DeviceCfgItem> list = deviceClassBiz.queryDeviceCfgItem(id);
			XMLResponse.outputXML(response, new DeviceCfgItemXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
}
