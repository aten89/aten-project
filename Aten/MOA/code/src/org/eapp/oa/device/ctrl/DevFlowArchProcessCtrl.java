package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDevFlowBiz;
import org.eapp.oa.device.blo.IDeviceAllocateBiz;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.dto.DeviceFlowPage;
import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 设备管理流程
 * 
 * @author shenyinjie
 * @version 2011-03-23
 */
public class DevFlowArchProcessCtrl extends HttpServlet {

	private static final long serialVersionUID = -1967167347504172287L;
	private IDevFlowBiz devFlowBiz;
	private IDeviceDiscardBiz deviceDiscardBiz;
	private ITaskBiz taskBiz;
	private IDeviceApplyBiz deviceApplyBiz;
	private IDeviceAllocateBiz deviceAllocateBiz;
	public DevFlowArchProcessCtrl() {
		super();
	}

	public void init() throws ServletException {
		devFlowBiz = (IDevFlowBiz) SpringHelper.getSpringContext().getBean("devFlowBiz");
		deviceDiscardBiz = (IDeviceDiscardBiz)SpringHelper.getSpringContext().getBean("deviceDiscardBiz");
		taskBiz = (ITaskBiz)SpringHelper.getSpringContext().getBean("taskBiz");
		deviceApplyBiz = (IDeviceApplyBiz)SpringHelper.getSpringContext().getBean("deviceApplyBiz");
		deviceAllocateBiz = (IDeviceAllocateBiz)SpringHelper.getSpringContext().getBean("deviceAllocateBiz");
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
		String act = request.getParameter("act");
		if (SysConstants.QUERY.equalsIgnoreCase(act)) {
			// 查询归档
			queryDevicePage(request, response);
			return;
		}else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
			// 查看设备
			initDeviceView(request, response);
			return;
		}
	}

	private void queryDevicePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
		String sortCol = HttpRequestHelper.getParameter(request,"sortCol");
		Boolean ascend = HttpRequestHelper.getBooleanParameter(request,"ascend",false);
		DeviceFlowQueryParameters qp = new DeviceFlowQueryParameters();
		qp.setPageNo(pageNo);
		qp.setPageSize(pageSize);
		String applicant = HttpRequestHelper.getParameter(request, "applicant");
		String formNO = HttpRequestHelper.getParameter(request, "formNO");
		String startArchTime = DataFormatUtil.formatTime(HttpRequestHelper
				.getParameter(request, "startArchTime"));
		String endArchTime = DataFormatUtil.formatTime(HttpRequestHelper
				.getParameter(request, "endArchTime"));
		String formType = HttpRequestHelper.getParameter(request, "formType");
		String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
		if(deviceType!=null && deviceType.length()>0){
			qp.setDeviceType(deviceType);
		}
		if (formType != null && formType.length() > 0) {
			qp.setFormType(Integer.valueOf(formType));
		}

		qp.setApplyCode(formNO);
		qp.setApplicantID(applicant);
		if (startArchTime != null) {
			qp.setBeginArchTime(Timestamp.valueOf(startArchTime));
		}
		if (endArchTime != null) {
			Timestamp t = Timestamp.valueOf(endArchTime);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			qp.setEndArchTime(t);
		}
		if (sortCol != null && !sortCol.trim().equals("")){
			qp.addOrder(sortCol, ascend);
		}else{
			qp.addOrder("archiveDate", false);
		}
		try {
			ListPage<DeviceFlowView> page = devFlowBiz.queryArchDeviceFlowPage(
					qp, user.getAccountID());
			XMLResponse.outputXML(response, new DeviceFlowPage(page)
					.toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	public void initDeviceView(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		try {
			String formType = HttpRequestHelper.getParameter(request, "formType");//表单类型
			String formId = HttpRequestHelper.getParameter(request, "formId");//表单ID
			if (formType == null || formId == null) {
				throw new OaException("参数不能为空");
			}
			//报废
			if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_DISCARD || Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_LEAVE){
				DevDiscardForm form =deviceDiscardBiz.getDevDiscardFormById(formId,true);
				if (form == null) {
					throw new OaException("设备报废单不存在！");
				}
				List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
				request.setAttribute("tasks", tasks);
				request.setAttribute("form", form);
				if (Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_DISCARD) {
					request.getRequestDispatcher("/page/device/scrap/view_scrap.jsp").forward(request, response);
				} else {
					request.getRequestDispatcher("/page/device/leavedeal/view_leave.jsp").forward(request, response);
				}
				
				
			} else if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_PERCHASE || Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_USE){
				DevPurchaseForm form =deviceApplyBiz.getDevUseApplyFormById(formId, true, true);
				if (form == null) {
					throw new OaException("设备"+(form.getApplyType()==0?"领用":"申购")+"单不存在！");
				}
				List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
				request.setAttribute("tasks", tasks);
				request.setAttribute("form", form);

				request.getRequestDispatcher("/page/device/recipients/view_reci.jsp").forward(request, response);
					
			} else if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_ALLOCATE){
				DevAllocateForm form =deviceAllocateBiz.getDevAllocateFormById(formId, true, true);
				if (form == null) {
					throw new OaException("设备调拨单不存在！");
				}
				List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
				request.setAttribute("tasks", tasks);
				request.setAttribute("form", form);
				request.getRequestDispatcher("/page/device/allocation/view_allo.jsp").forward(request, response);
					
			}
			
		} catch (OaException e1) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e1.getMessage());
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		}
	}
	
}
