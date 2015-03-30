package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDeviceCheckItemBiz;
import org.eapp.oa.device.dto.DeviceCheckItemHtml;
import org.eapp.oa.device.dto.DeviceCheckItemPage;
import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 设备检查项配置
 * 
 * @author shenyinjie
 * @version 2011-03-16
 */
public class DeviceCheckItemCtrl extends HttpServlet {

	private static final long serialVersionUID = 4065051079925052830L;
	private static final String ONE_LEVEL_SPLIT_STRING = "~!@";
	private IDeviceCheckItemBiz deviceCheckItemBiz;

	public void init() throws ServletException {
		deviceCheckItemBiz = (IDeviceCheckItemBiz)SpringHelper.getBean("deviceCheckItemBiz");
	}

	public DeviceCheckItemCtrl() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = HttpRequestHelper.getParameter(request, "act");

		if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			//查询
			queryDeviceCheckItems(request, response);
			return;
		} else if ("initadd".equalsIgnoreCase(act)) {
			//初始化新增
			initAdd(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(act)) {
			// 初始化修改
			initModify(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除设备检查项配置
			deleteDeviceCheckItem(request, response);
			return;
		} else if (SysConstants.ADD.equalsIgnoreCase(act)) {
			//新增设备检查项配置
			addDeviceCheckItem(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			//修改设备检查项配置
			modifyDeviceCheckItem(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
			//修改设备检查项配置
			viewDeviceCheckItem(request, response);
			return;
		} else if ("getcheckitem".equalsIgnoreCase(act)) {
			//检测项下拉框
			getDeviceCheckItemSel(request, response);
			return;
		}
	}

	private void queryDeviceCheckItems(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		try {
			QueryParameters qp = new QueryParameters();
			qp.setPageNo(pageNo);
			qp.setPageSize(pageSize);
			ListPage<DeviceClass> listPage = deviceCheckItemBiz.queryDeviceClassPage(qp);
			XMLResponse.outputXML(response, new DeviceCheckItemPage(listPage).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void initAdd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		request.getRequestDispatcher("/page/device/paramconf/check/edit_item.jsp").forward(request,response);
	}

	public void initModify(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String deviceClassID = HttpRequestHelper.getParameter(request, "id");
		DeviceClass deviceClass = deviceCheckItemBiz.queryCheckItemDeviceClass(deviceClassID);
		request.setAttribute("deviceClass", deviceClass);//设置页面数据
		request.getRequestDispatcher("/page/device/paramconf/check/edit_item.jsp").forward(request,response);
	}

	public void deleteDeviceCheckItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "id不能为空");
			return;
		}
		try {
			deviceCheckItemBiz.txDelByDeviceClassID(id);
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	public void addDeviceCheckItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String itemNames = HttpRequestHelper.getParameter(request, "itemNames");
		String deviceClassID = HttpRequestHelper.getParameter(request, "deviceClassID");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		if (itemNames == null || "".equals(itemNames)) {
			XMLResponse.outputStandardResponse(response, 0, "操作失败：设备检查项不能为空！");
			return;
		}
		if (deviceClassID == null || "".equals(deviceClassID)) {
			XMLResponse.outputStandardResponse(response, 0, "操作失败：设备类别不能为空！");
			return;
		}
		List<String> itemNameList = Arrays.asList(itemNames.split(ONE_LEVEL_SPLIT_STRING));
		try {
			deviceCheckItemBiz.txSaveDeviceCheckItem(deviceClassID, remark, itemNameList);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}

	}

	public void modifyDeviceCheckItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String itemNames = HttpRequestHelper.getParameter(request, "itemNames");
		String deviceClassID = HttpRequestHelper.getParameter(request, "deviceClassID");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		if (itemNames == null || "".equals(itemNames)) {
			XMLResponse.outputStandardResponse(response, 0, "操作失败：设备检查项不能为空！");
			return;
		}
		if (deviceClassID == null || "".equals(deviceClassID)) {
			XMLResponse.outputStandardResponse(response, 0, "操作失败：设备类别不能为空！");
			return;
		}
		List<String> itemNameList = Arrays.asList(itemNames.split(ONE_LEVEL_SPLIT_STRING));
		try {
			deviceCheckItemBiz.txModifyDeviceCheckItem(deviceClassID, remark, itemNameList);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		}
	}

	public void viewDeviceCheckItem(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String deviceClassID = HttpRequestHelper.getParameter(request, "id");
		DeviceClass deviceClass = deviceCheckItemBiz.queryCheckItemDeviceClass(deviceClassID);
		request.setAttribute("deviceClass", deviceClass);//设置页面数据
		request.getRequestDispatcher("/page/device/paramconf/check/view_item.jsp").forward(request,response);
	}
	
	/**
	 * 检测项下拉框
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getDeviceCheckItemSel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String classId = HttpRequestHelper.getParameter(request, "classId");
		List<DeviceCheckItem> list = deviceCheckItemBiz.queryCheckItemByDeviceClassId(classId);
		//1.取根目录
		if(list == null){
			return;
		}
		HTMLResponse.outputHTML(response, new DeviceCheckItemHtml(list).toString());
	}
}
