package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDevFlowBiz;
import org.eapp.oa.device.dto.DeviceFlowPage;
import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 设备管理流程
 * @author shenyinjie
 * @version 2011-03-23
 */
public class DevFlowTrackProcessCtrl extends HttpServlet {

	private static final long serialVersionUID = -1967167347504172287L;
	private IDevFlowBiz devFlowBiz;
	private DevFlowArchProcessCtrl devFlowArchProcessCtrl;
	public DevFlowTrackProcessCtrl() {
		super();
	}
	public void init() throws ServletException {
		devFlowBiz = (IDevFlowBiz)SpringHelper.getSpringContext().getBean("devFlowBiz");
		devFlowArchProcessCtrl = new DevFlowArchProcessCtrl();
		devFlowArchProcessCtrl.init();
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
			//查询跟踪
			queryDevicePage(request, response);
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
			// 查看设备
			devFlowArchProcessCtrl.initDeviceView(request, response);
		}
	}
	
	private void queryDevicePage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
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
		String startApplyTime = DataFormatUtil.formatTime(HttpRequestHelper
				.getParameter(request, "startApplyTime"));
		String endApplyTime = DataFormatUtil.formatTime(HttpRequestHelper
				.getParameter(request, "endApplyTime"));
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
		if (startApplyTime != null) {
			qp.setBeginApplyTime(Timestamp.valueOf(startApplyTime));
		}
		if (endApplyTime != null) {
			Timestamp t = Timestamp.valueOf(endApplyTime);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			qp.setEndApplyTime(t);
		}
		if (sortCol != null && !sortCol.trim().equals("")){
			qp.addOrder(sortCol, ascend);
		}else{
			qp.addOrder("applyDate", false);
		}
		try {
			ListPage<DeviceFlowView> page = devFlowBiz.queryTrackDeviceFlowPage(
					qp, user.getAccountID());
			XMLResponse.outputXML(response, new DeviceFlowPage(page)
					.toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
}
