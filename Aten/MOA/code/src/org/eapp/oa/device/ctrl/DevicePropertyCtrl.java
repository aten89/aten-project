package org.eapp.oa.device.ctrl;
/**
 * Description:设备属性
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.device.blo.IDevicePropertyBiz;
import org.eapp.oa.device.dto.DeviceCfgOptionPage;
import org.eapp.oa.device.dto.DeviceOptionQueryParameters;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

public class DevicePropertyCtrl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IDevicePropertyBiz devicePropertyBiz;
	/**
	 * Constructor of the object.
	 */
	public DevicePropertyCtrl() {
		super();
	}
	
	public void init() throws ServletException {
		devicePropertyBiz = (IDevicePropertyBiz)SpringHelper.getSpringContext().getBean("devicePropertyBiz");
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String act = HttpRequestHelper.getParameter(request, "act");

		if (SysConstants.ADD.equalsIgnoreCase(act)) {
			// 新增设备属性信息
			addDeviceOption(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除设备属性信息
			delDeviceOption(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改设备属性信息
			modifyDeviceOption(request, response);
			return;
		} else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询设备属性信息
			queryDeviceOption(request, response);
			return;
		}
	}

	private void queryDeviceOption(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			DeviceOptionQueryParameters qp =new DeviceOptionQueryParameters();
			int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
			int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
			qp.setPageNo(pageNo);
			qp.setPageSize(pageSize);
			qp.addOrder("propertyName", true);
			ListPage<DeviceProperty> pageList = devicePropertyBiz.queryDevicePropertyPage(qp);
			XMLResponse.outputXML(response, new DeviceCfgOptionPage(pageList).toDocument());
		}  catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	private void addDeviceOption(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String name = HttpRequestHelper.getParameter(request, "name");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		try {
			devicePropertyBiz.txAddDeviceProperty(name, remark);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	private void modifyDeviceOption(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		String name = HttpRequestHelper.getParameter(request, "name");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		if (id == null) {
			throw new IllegalArgumentException("ID不能为空");
		}
		try {
			devicePropertyBiz.txUpdateDeviceProperty(id, name, remark);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	private void delDeviceOption(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			throw new IllegalArgumentException("ID不能为空");
		}
		try {
			devicePropertyBiz.txDeleteDeviceProperty(id);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		}  catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		}  catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

}
