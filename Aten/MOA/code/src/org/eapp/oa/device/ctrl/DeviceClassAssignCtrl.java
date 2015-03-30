package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eapp.oa.device.blo.IDeviceClassAssignBiz;
import org.eapp.oa.device.dto.DeviceClassAssignDetailXml;
import org.eapp.oa.device.dto.DeviceClassAssignXml;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignArea;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
/**
 * 设备授权管理
 * @author jiangxiongsheng
 *
 */
public class DeviceClassAssignCtrl extends HttpServlet {

	private static final long serialVersionUID = -5839760672218706774L;

	private IDeviceClassAssignBiz deviceClassAssignBiz;

	/**
	 * Constructor of the object.
	 */
	public DeviceClassAssignCtrl() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init(ServletConfig config) throws ServletException {
		deviceClassAssignBiz = (IDeviceClassAssignBiz) SpringHelper.getSpringContext().getBean(
				"deviceClassAssignBiz");
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

		if (SysConstants.ADD.equalsIgnoreCase(act)) {
			// 新增设备类别区域授权
			addAreaConfig(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除设备类别区域授权
			delAreaAssgin(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改设备类别区域授权
			modifyAreaAssgin(request, response);
			return;
		} else if (SysConstants.QUERY.equals(act)) {
			// 取得所有的授权配置
			queryAreaAssgin(request, response);
			return;
		} else if("get_binding_users".equalsIgnoreCase(act)){
			//已绑定的用户
			getBindUser(request,response);
			return;
		}else if("bind_user".equalsIgnoreCase(act)){
			//绑定用户
			saveBindUser(request,response);
			return;
		}else if("get_binding_groups".equalsIgnoreCase(act)){
			//已绑定的机构
			getBindGroup(request,response);
			return;
		}else if("bind_group".equalsIgnoreCase(act)){
			//绑定机构
			saveBindGroup(request,response);
			return;
		}else if("get_binding_post".equalsIgnoreCase(act)){
			//已绑定的职位
			getBindPost(request,response);
			return;
		}else if("bind_post".equalsIgnoreCase(act)){
			//绑定职位
			saveBindPost(request,response);
			return;
		}else if("init_assign".equalsIgnoreCase(act)){
			//初始化授权页面
			initAssign(request,response);
			return;
		}else if("init_assign_detail".equalsIgnoreCase(act)){
			//初始化详细授权页面
			initAssignDetail(request,response);
			return;
		}else if("batchdelete".equalsIgnoreCase(act)){
			//批量删除
			batchDelete(request,response);
			return;
		}
		
	}

	/**
	 * 保存设备类型编号已经配置的流程类别
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addAreaConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String classId = HttpRequestHelper.getParameter(request, "classIds");
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
		
		if (areaCode == null) {
			XMLResponse.outputStandardResponse(response, 0, "所属地区不能为空");
			return;
		}
		if (classId == null) {
			XMLResponse.outputStandardResponse(response, 0, "设备类型不能为空");
			return;
		}
		
		DeviceClassAssign classAssign = new DeviceClassAssign();
		Set<DeviceClassAssignArea> deviceClassSet = new HashSet<DeviceClassAssignArea>();
		String[] classIds =	classId.split(";");
		for (String string : classIds) {
			if (StringUtils.isBlank(string)) {
				continue;
			}
			DeviceClassAssignArea area = new DeviceClassAssignArea();
			DeviceClass deviceClass =new DeviceClass();
			deviceClass.setId(string);
			area.setDeviceClass(deviceClass);
			deviceClassSet.add(area);
		}
		classAssign.setDeviceType(deviceType);
		classAssign.setAreaCode(areaCode);
		classAssign.setDeviceClassAssignAreas(deviceClassSet);
		classAssign.setConfigTime(new Date());
		classAssign.setRemark(remark);
		try {
			deviceClassAssignBiz.txSaveDeviceClassAssign(classAssign);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void delAreaAssgin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
			return;
		}
		try {
			deviceClassAssignBiz.txDeleteDeviceClassAssign(id);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void modifyAreaAssgin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String classId = HttpRequestHelper.getParameter(request, "classIds");
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
		if (areaCode == null) {
			XMLResponse.outputStandardResponse(response, 0, "所属地区不能为空");
			return;
		}
		if (classId == null) {
			XMLResponse.outputStandardResponse(response, 0, "设备类型不能为空");
			return;
		}
		
		DeviceClassAssign classAssign = new DeviceClassAssign();
		Set<DeviceClassAssignArea> deviceClassSet = new HashSet<DeviceClassAssignArea>();
		String[] classIds =	classId.split(";");
		for (String string : classIds) {
			if (StringUtils.isBlank(string)) {
				continue;
			}
			DeviceClassAssignArea area = new DeviceClassAssignArea();
			DeviceClass deviceClass =new DeviceClass();
			deviceClass.setId(string);
			area.setDeviceClass(deviceClass);
			deviceClassSet.add(area);
		}
		classAssign.setDeviceType(deviceType);
		classAssign.setId(id);
		classAssign.setAreaCode(areaCode);
		classAssign.setDeviceClassAssignAreas(deviceClassSet);
		classAssign.setConfigTime(new Date());
		classAssign.setRemark(remark);
		try {
			deviceClassAssignBiz.txUpdateDeviceClassAssign(classAssign);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException oa) {
			XMLResponse.outputStandardResponse(response, 0, oa.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void queryAreaAssgin(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 查询
			List<DeviceClassAssign> list = deviceClassAssignBiz.queryDeviceClassAssignList();
			XMLResponse.outputXML(response, new DeviceClassAssignXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 获得绑定的用户
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getBindUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String id = HttpRequestHelper.getParameter(request, "id");
		String flag = HttpRequestHelper.getParameter(request, "flag");
		if (id == null || flag == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		try {
			List<DeviceClassAssignDetail> list = deviceClassAssignBiz.getBindingUsers(id, Integer.valueOf(flag));
			XMLResponse.outputXML(response, new DeviceClassAssignDetailXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	/**
	 * 绑定用户
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveBindUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		
		String flag = HttpRequestHelper.getParameter(request, "flag");
		if (id == null || flag == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		String[] userIDs = HttpRequestHelper.getParameters(request, "user_ids");
		try {		
			deviceClassAssignBiz.txBindingUsers(id, userIDs, Integer.valueOf(flag));
			XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
		}
	}
	/**
	 * 获得绑定的机构
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getBindGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String id = HttpRequestHelper.getParameter(request, "id");
		String flag = HttpRequestHelper.getParameter(request, "flag");
		if (id == null || flag == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		try {
			List<DeviceClassAssignDetail> list = deviceClassAssignBiz.getBindingGroups(id, Integer.valueOf(flag));
			XMLResponse.outputXML(response, new DeviceClassAssignDetailXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	/**
	 * 绑定机构
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveBindGroup(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String flag = HttpRequestHelper.getParameter(request, "flag");
		if (id == null || flag == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		String[] groupIDs = HttpRequestHelper.getParameters(request, "group_ids");
		try {		
			deviceClassAssignBiz.txBindingGroups(id, groupIDs, Integer.valueOf(flag));
			XMLResponse.outputStandardResponse(response, 1, "绑定机构成功！");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "绑定机构失败！");
		}
	}
	/**
	 * 获得绑定的职位
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getBindPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String id = HttpRequestHelper.getParameter(request, "id");
		String flag = HttpRequestHelper.getParameter(request, "flag");
		if (id == null || flag == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		try {
			List<DeviceClassAssignDetail> list = deviceClassAssignBiz.getBindingPosts(id, Integer.valueOf(flag));
			XMLResponse.outputXML(response, new DeviceClassAssignDetailXml(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	/**
	 * 绑定职位
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveBindPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		String flag = HttpRequestHelper.getParameter(request, "flag");
		if (id == null || flag == null) {
			XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
			return;
		}
		String[] postIDs = HttpRequestHelper.getParameters(request, "post_ids");
		try {		
			deviceClassAssignBiz.txBindingPosts(id, postIDs, Integer.valueOf(flag));
			XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
		}
	}
	/**
	 * 初始化授权页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initAssign(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//获取页面显示标题 
		String title = HttpRequestHelper.getParameter(request, "title");
		request.setAttribute("title", title);
		request.getRequestDispatcher("/page/device/paramconf/assign/frame_assign.jsp").forward(request, response);
	}
	
	/**
	 * 初始化详细授权页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initAssignDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String page = HttpRequestHelper.getParameter(request, "page");
		String title = HttpRequestHelper.getParameter(request, "title");
		request.setAttribute("title", title);
		request.getRequestDispatcher("/page/device/paramconf/assign/"+page).forward(request, response);
	
	}
	/**
	 * 批量删除
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void batchDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String ids = HttpRequestHelper.getParameter(request,"assignIds");
		if(ids==null){
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
		}
		String[] id=ids.split(",");
		try{
			deviceClassAssignBiz.txBatchDeltete(id);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		}catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
}
