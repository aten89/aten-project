package org.eapp.oa.device.ctrl;

import java.io.IOException;
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
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

/**
 * 设备管理流程
 * @author shenyinjie
 * @version 2011-03-23
 */
public class DevFlowStartProcessCtrl extends HttpServlet {

	private static final long serialVersionUID = -1967167347504172287L;
	private IDevFlowBiz devFlowBiz;
	private IDeviceDiscardBiz deviceDiscardBiz;
	private IDeviceApplyBiz deviceApplyBiz;
	private IDeviceAllocateBiz deviceAllocateBiz;
	public DevFlowStartProcessCtrl() {
		super();
	}
	public void init() throws ServletException {
		devFlowBiz = (IDevFlowBiz)SpringHelper.getSpringContext().getBean("devFlowBiz");
		deviceDiscardBiz = (IDeviceDiscardBiz)SpringHelper.getSpringContext().getBean("deviceDiscardBiz");
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
			//查询起草
			queryDevicePage(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			//删除起草
			deleteDeviceApply(request, response);
			return;
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
		String formType = HttpRequestHelper.getParameter(request, "formType");
		if(formType!=null && formType.length()>0){
			qp.setFormType(Integer.valueOf(formType));
		}
		if (sortCol != null && !sortCol.trim().equals("")){
			qp.addOrder(sortCol, ascend);
		}else{
			qp.addOrder("applyDate", false);
		}
		try{
			ListPage<DeviceFlowView> page = devFlowBiz.queryDraftDeviceFlowPage(qp, user.getAccountID());
			XMLResponse.outputXML(response,new DeviceFlowPage(page).toDocument());
		}catch(Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteDeviceApply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		String formType = HttpRequestHelper.getParameter(request, "formType");//表单类型
		if (id == null) {
			throw new IllegalArgumentException("ID不能为空");
		}
		try {
			if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_DISCARD){
				//报废
				deviceDiscardBiz.txDelDevDiscardForm(id);
			} else if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_USE || Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_PERCHASE){
				//领用、申购
				deviceApplyBiz.txDelDevApplyForm(id);
			} else if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_ALLOCATE){
				//调拨
				deviceAllocateBiz.txDelDevAllocateForm(id);
			} else if(Integer.valueOf(formType)==DeviceFlowView.FORM_TYPE_LEAVE){
				//离职处理
				deviceDiscardBiz.txDelDevDiscardForm(id);
			} 
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		}  catch (OaException ex) {
			XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
		}  catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
}
