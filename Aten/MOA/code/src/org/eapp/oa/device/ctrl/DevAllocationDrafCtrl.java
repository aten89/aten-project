package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDeviceAllocateBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.dto.DevAllocateFormList;
import org.eapp.oa.device.dto.DeviceList;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;
/**
 * @author tim
 *	设备调拨起草
 */
public class DevAllocationDrafCtrl extends HttpServlet {

	private static final long serialVersionUID = 4181696264100667028L;
	
	private IDeviceAllocateBiz deviceAllocateBiz;
	private IDeviceBiz deviceBiz;
	private IDeviceAreaConfigBiz deviceAreaConfigBiz;
	
	public void init() throws ServletException {
		deviceAllocateBiz = (IDeviceAllocateBiz)SpringHelper.getSpringContext().getBean("deviceAllocateBiz");
		deviceBiz = (IDeviceBiz)SpringHelper.getSpringContext().getBean("deviceBiz");
		deviceAreaConfigBiz = (IDeviceAreaConfigBiz) SpringHelper.getSpringContext().getBean("deviceAreaConfigBiz");
	}
	public DevAllocationDrafCtrl() {
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
		if(SysConstants.QUERY.equals(act)){
			//查询草稿列表
			queryDeviceAllocation(request, response);
			return;
		} else if("initadd".equals(act)){
			//初始化新增
			initAdd(request, response);
			return;
		} else if("initmodify".equalsIgnoreCase(act)){
			//初始化修改页面
			initModifyForm(request, response);
			return;
		} else if(SysConstants.DELETE.equalsIgnoreCase(act)){
			//删除公文
			deleteForm(request, response);
			return;
		} else if("addallot".equals(act)){ 
			//保存
			addDevAllocateForm(request, response);
			return;
		} else if("modifyallot".equals(act)){
			//修改
			modifyDevAllocateForm(request, response);
			return;
		} else if ("draftmanamend".equals(act)) {
			draftmanAmend(request, response);
			return;
		} 
		
	}
	
	
	private void queryDeviceAllocation(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		try {
			List<DevAllocateForm> list = deviceAllocateBiz.queryAllocateForm(user.getAccountID(), DevAllocateForm.FORMSTATUS_UNPUBLISH);
			XMLResponse.outputXML(response,new DevAllocateFormList(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	
	private void initAdd(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String deviceIDs = HttpRequestHelper.getParameter(request, "deviceIDs");//要调拨的设备ID
		DevAllocateForm applyForm = new DevAllocateForm();
		applyForm.setRegAccountID(user.getAccountID());
		applyForm.setRegTime(new Date());
		applyForm.setApplicant(user.getAccountID());
		applyForm.setApplyGroupName(user.getGroupNames());
		applyForm.setApplyDate(new Date());
		
		if (deviceIDs != null) {
			for (String deviceID : deviceIDs.split(",")) {
				if (deviceID != null && !"".equals(deviceID)) {
					Device device = deviceBiz.getDeviceById(deviceID);
					if (device != null && device.getDeviceCurStatusInfo().getApproveType() != null) {
						//报废审批中
						request.setAttribute(SysConstants.REQUEST_ERROR_MSG, 
								"编号为“" + device.getDeviceNO() + "”的设备正处于" + 
								DeviceCurStatusInfo.approveTypeMap.get(device.getDeviceCurStatusInfo().getApproveType()) + "，现不能调拨！");
						
						//调拨审批中
						request.getRequestDispatcher("/page/error.jsp").forward(request, response);
						return;
					}
					applyForm.setDeviceType(device.getDeviceType());
					DevAllocateList list = new DevAllocateList();
					list.setDevice(device);
					applyForm.getDevAllocateLists().add(list);
				}
			}
		}
		
		request.setAttribute("applyForm", applyForm); 
		List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
		request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
		request.getRequestDispatcher("/page/device/allocation/edit_allo.jsp").forward(request, response);
	}
	
	
	public void initModifyForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		DevAllocateForm devAlcForm = deviceAllocateBiz.getDevAllocateFormById(id, true, true);
		if (devAlcForm == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "记录不存在");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}
		request.setAttribute("applyForm", devAlcForm);
		List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
		request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
		request.getRequestDispatcher("/page/device/allocation/edit_allo.jsp").forward(request, response);
	}

	public void deleteForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
        	XMLResponse.outputStandardResponse(response, 0, "id不能为空");
        	return;
        }
		try {
			deviceAllocateBiz.txDelDevAllocateForm(id);
			XMLResponse.outputStandardResponse(response, 1, "删除成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "删除失败");
		}
	}
	
	public void addDevAllocateForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String allotType = HttpRequestHelper.getParameter(request, "allotType");
		String strMoveDate = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "moveDate"));
		Date moveDate = null;
		if (strMoveDate != null) {
			moveDate = new Date(Timestamp.valueOf(strMoveDate).getTime());
		}
		String inAccountID = null;
		String inGroupName = null;
		if (!DevAllocateForm.MOVE_TYPE_STORAGE.equals(allotType) && !DevAllocateForm.MOVE_TYPE_BORROW.equals(allotType)) {
			inAccountID = HttpRequestHelper.getParameter(request, "inAccountID");
			inGroupName = HttpRequestHelper.getParameter(request, "inGroupName");
		}
		String reason = HttpRequestHelper.getParameter(request, "reason");
		String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "isStartFlow", false);
		boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);//是否验证主设备
		try {
			Set<DevAllocateList> devAllocateList = DeviceList.parseDevAllotListJSON(aDevList);
			deviceAllocateBiz.txSaveAsDraf(deviceTypeCode, allotType, moveDate, user.getAccountID(), user.getGroupNames(), inAccountID, inGroupName, reason, 
					devAllocateList, isStartFlow, validMainDevFlag);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}
	}
	
	public void modifyDevAllocateForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id =  HttpRequestHelper.getParameter(request, "id");
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String allotType = HttpRequestHelper.getParameter(request, "allotType");
		String strMoveDate = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "moveDate"));
		Date moveDate = null;
		if(strMoveDate!=null){
			moveDate = new Date(Timestamp.valueOf(strMoveDate).getTime());
		}
		String inAccountID = null;
		String inGroupName = null;
		if (!DevAllocateForm.MOVE_TYPE_STORAGE.equals(allotType) && !DevAllocateForm.MOVE_TYPE_BORROW.equals(allotType)) {
			inAccountID = HttpRequestHelper.getParameter(request, "inAccountID");
			inGroupName = HttpRequestHelper.getParameter(request, "inGroupName");
		}
		String reason = HttpRequestHelper.getParameter(request, "reason");
		String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "isStartFlow", false);
		boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);//是否验证主设备
		try {
			Set<DevAllocateList> devAllocateList = DeviceList.parseDevAllotListJSON(aDevList);
			deviceAllocateBiz.txModifyDraftForm(id, deviceTypeCode, allotType, moveDate, inAccountID, inGroupName, reason, 
					devAllocateList, isStartFlow, validMainDevFlag);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
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
		String id = HttpRequestHelper.getParameter(request, "id");
		String allotType = HttpRequestHelper.getParameter(request, "allotType");
		String strMoveDate = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "moveDate"));
		Date moveDate = null;
		if (strMoveDate != null) {
			moveDate = new Date(Timestamp.valueOf(strMoveDate).getTime());
		}
		String inAccountID = null;
		String inGroupName = null;
		if (!DevAllocateForm.MOVE_TYPE_STORAGE.equals(allotType) && !DevAllocateForm.MOVE_TYPE_BORROW.equals(allotType)) {
			inAccountID = HttpRequestHelper.getParameter(request, "inAccountID");
			inGroupName = HttpRequestHelper.getParameter(request, "inGroupName");
		}
		String reason = HttpRequestHelper.getParameter(request, "reason");
		String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
		boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);//是否验证主设备
		try {
			Set<DevAllocateList> devAllocateList = DeviceList.parseDevAllotListJSON(aDevList);
			deviceAllocateBiz.txDraftmanAmend(id, allotType, moveDate, inAccountID, inGroupName, reason, 
					devAllocateList, validMainDevFlag);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	public void setDeviceBiz(IDeviceBiz deviceBiz) {
		this.deviceBiz = deviceBiz;
	}
	

}
