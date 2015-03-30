package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.dto.DeviceList;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 设备报废-->起草 
 * 
 * @author sds
 * @version 2009-09-07
 */
public class DevDiscardDrafCtrl extends HttpServlet {

	private static final long serialVersionUID = 4065051079925052830L;

	private IDeviceDiscardBiz deviceDiscardBiz;
	private IDeviceBiz deviceBiz;
	
	public void init() throws ServletException {
		deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getSpringContext().getBean("deviceDiscardBiz");
		deviceBiz = (IDeviceBiz) SpringHelper.getSpringContext().getBean("deviceBiz");
	}

	public DevDiscardDrafCtrl() {
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
		if ("initadd".equals(act)) {
			//初始化新增
			initAdd(request, response);
			return;
		} else if ("initmodify".equalsIgnoreCase(act)) {
			// 初始化设备报废单修改
			initModifyDiscardForm(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除设备报废单
			deleteDiscardForm(request, response);
			return;
		} else if ("adddevscrap".equals(act)) {
			//新增设备报废单
			addDevDiscardForm(request, response);
			return;
		} else if ("modifydevscrap".equals(act)) {
			//修改报废单
			modifyDevDiscardForm(request, response);
			return;
		} else if("amend".equals(act)){
			//驳回修改
			draftmanAmend(request, response);
			return;
		}
	}

	private void initAdd(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String deviceIDs = HttpRequestHelper.getParameter(request, "deviceIDs");
		int formType = HttpRequestHelper.getIntParameter(request, "formType", 0);
		DevDiscardForm applyForm = new DevDiscardForm();
		applyForm.setRegAccountID(user.getAccountID());
		applyForm.setRegTime(new Date());
		applyForm.setApplicant(user.getAccountID());
		applyForm.setApplyGroupName(user.getGroupNames());
		applyForm.setApplyDate(new Date());
		applyForm.setFormType(formType);
		if (deviceIDs != null) { 
			for (String deviceID : deviceIDs.split(",")) {
				if (deviceID != null && !"".equals(deviceID)) {
					Device device = deviceBiz.getDeviceById(deviceID);
					if (device != null && device.getDeviceCurStatusInfo().getApproveType() != null) {
						//报废审批中
						request.setAttribute(SysConstants.REQUEST_ERROR_MSG, 
								"编号为“" + device.getDeviceNO() + "”的设备正处于" + 
								DeviceCurStatusInfo.approveTypeMap.get(device.getDeviceCurStatusInfo().getApproveType()) + "，现不能报废！");
						request.getRequestDispatcher("/page/error.jsp").forward(request, response);
						return;
					}
					applyForm.setDeviceType(device.getDeviceType());
					DiscardDevList list = new DiscardDevList();
					list.setDevice(device);
					applyForm.getDiscardDevLists().add(list);
				}
			}
		}
		
		request.setAttribute("applyForm", applyForm); 
		if (formType == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue()) {
			request.getRequestDispatcher("/page/device/scrap/edit_scrap.jsp").forward(request,response);
		} else if (formType == DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue()){
			request.getRequestDispatcher("/page/device/leavedeal/edit_leave.jsp").forward(request,response);
		}
		
	}

	public void initModifyDiscardForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		DevDiscardForm applyForm = deviceDiscardBiz.getDevDiscardFormById(id, true);
		if (applyForm == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		request.setAttribute("applyForm", applyForm); 
		if (applyForm.getFormType().intValue() == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue()) {
			request.getRequestDispatcher("/page/device/scrap/edit_scrap.jsp").forward(request,response);
		} else if (applyForm.getFormType().intValue() == DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue()){
			request.getRequestDispatcher("/page/device/leavedeal/edit_leave.jsp").forward(request,response);
		}
	}

	public void deleteDiscardForm(HttpServletRequest request,
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
			deviceDiscardBiz.txDelDevDiscardForm(id);
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	public void addDevDiscardForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "isStartFlow", false);
		int formType = HttpRequestHelper.getIntParameter(request, "formType", 0);
		
		try {
			DevDiscardForm form = new DevDiscardForm();
			Date now = new Date();
			form.setRegAccountID(user.getAccountID());
			form.setRegTime(now);
			form.setApplicant(user.getAccountID());
			form.setApplyDate(now);
			form.setApplyGroupName(user.getGroupNames());
			form.setDeviceType(deviceTypeCode);
			Set<DiscardDevList> discardDevLists = DeviceList.parseJSON(aDevList, form);
			form.setDiscardDevLists(discardDevLists);
			form = deviceDiscardBiz.txSaveAsDraf(form, formType, isStartFlow);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}

	}

	public void modifyDevDiscardForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "isStartFlow", false);
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			throw new IllegalArgumentException("参数错误");
		}
		try {
			Set<DiscardDevList> discardDevLists = DeviceList.parseDiscardDevListJSON(aDevList);
			deviceDiscardBiz.txModifyDraftForm(id, user.getGroupNames(), deviceTypeCode, discardDevLists, isStartFlow);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	/**
	 * 驳回到起草人修改
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void draftmanAmend(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			throw new IllegalArgumentException("参数错误");
		}
		try {
			Set<DiscardDevList> discardDevLists = DeviceList.parseDiscardDevListJSON(aDevList);
			deviceDiscardBiz.txDraftmanAmend(id, user.getGroupNames(), 
					discardDevLists);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
}
