package org.eapp.oa.device.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.dto.DevicePage;
import org.eapp.oa.device.dto.DeviceQueryParameters;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;

/**
 * 设备管理流程
 * @author shenyinjie
 * @version 2011-03-23
 */
public class MyUseDeviceCtrl extends HttpServlet {

	private static final long serialVersionUID = -1967167347504172287L;
	private IDeviceBiz deviceBiz;
	
	public MyUseDeviceCtrl() {
		super();
	}
	public void init() throws ServletException {
		deviceBiz = (IDeviceBiz)SpringHelper.getSpringContext().getBean("deviceBiz");
	}
	public void destroy() {
		super.destroy();
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);	
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("act");
		if ((SysConstants.QUERY).equalsIgnoreCase(action)) {
			//查询我申请的设备
			queryDevicesIApply(request, response);
			return;
		 } else if (SysConstants.VIEW.equals(action)) {
            // 查看设备的基本设备但不包含验收单和设备操作日志
            viewBasicDeviceInfo(request, response);
            return;
		}
	}

	
	/**
	 * 搜索我名下的设备.如果设备已删除、设备已报废、用户已离职则之前我领用的设备不能算是我名下的设备.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void queryDevicesIApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", 1);
		try {
			DeviceQueryParameters qp = new DeviceQueryParameters();
			qp.setPageNo(pageNo);
			qp.setPageSize(pageSize);
			qp.addOrder("deviceCurStatusInfo.statusUptDate", true);
			qp.addOrder("deviceNO", true); 
			ListPage<Device> devicePage = deviceBiz.queryDevicePageByApplicantID(qp, user.getAccountID());
			XMLResponse.outputXML(response,new DevicePage(devicePage).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	 private void viewBasicDeviceInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		     IOException {
		 String id = HttpRequestHelper.getParameter(request, "id");
		 if (id == null) {
		     request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
		     request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		     return;
		 }
		 Device device = deviceBiz.getDeviceById(id);
		 request.setAttribute("device", device);
		 request.getRequestDispatcher("/page/device/apply/view_devi.jsp").forward(
		         request, response);
	}

}
