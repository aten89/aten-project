package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.dto.DeviceList;
import org.eapp.oa.device.dto.PurchaseDeviceListDTO;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.PurchaseDevPurpose;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.dto.DataDictionarySelect;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.spring.SpringHelper;
/**
 * @author tim
 *	设备领用起草
 */
public class DevApplyDrafCtrl extends HttpServlet {

	private static final long serialVersionUID = 4181696264100667028L;
	
	private IDeviceApplyBiz deviceApplyBiz;
	private IDeviceAreaConfigBiz deviceAreaConfigBiz;
	
	public void init() throws ServletException {
		deviceApplyBiz = (IDeviceApplyBiz)SpringHelper.getSpringContext().getBean("deviceApplyBiz");
		deviceAreaConfigBiz = (IDeviceAreaConfigBiz) SpringHelper.getSpringContext().getBean("deviceAreaConfigBiz");
	}
	public DevApplyDrafCtrl() {
		super();
		deviceApplyBiz = (IDeviceApplyBiz)SpringHelper.getSpringContext().getBean("deviceApplyBiz");
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
		if("initadd".equals(act)){
			//初始化新增
			initAdd(request, response);
			return;
		} else if("initmodify".equalsIgnoreCase(act)){
			//初始化修改页面
			initModifyForm(request, response);
			return;
		} else if("initbuytype".equals(act)){
			//购买方式选择
			makeBuyTypeSelect(request, response);
			return;
		} else if("modifydraftform".equals(act)){
			//修改
			modifyDraftForm(request, response);
			return;
		} else if("adddevuse".equals(act)){
			//保存
			addDevApplyForm(request, response);
			return;
		} else if("checkmainandmanytime".equals(act)){
			//申购设备采购判断主设备和是否在流程中
			checkMainAndManyTime(request, response);
			return;
		} else if ("draftmanamend".equals(act)) {
			draftmanAmend(request, response);
			return;
		} else if ("getinfo".equals(act)) {
			getInfo(request, response);
			return;
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
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		DevPurchaseForm applyForm = new DevPurchaseForm();
		applyForm.setRegAccountID(user.getAccountID());
		applyForm.setRegTime(new Date());
		applyForm.setApplicant(user.getAccountID());
		applyForm.setApplyGroupName(user.getGroupNames());
		applyForm.setApplyDate(new Date());
		applyForm.setDeviceType(deviceTypeCode);
		request.setAttribute("applyForm", applyForm);
		
		List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
		request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
		request.getRequestDispatcher("/page/device/recipients/edit_reci.jsp").forward(request, response);
	}
	
	private void makeBuyTypeSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Collection<DataDictInfo> rootList = SysCodeDictLoader.getInstance()
				.getBuyType().values();
		// 1.取根目录
		if (rootList == null) {
			return;
		}
		HTMLResponse.outputHTML(response, new DataDictionarySelect(rootList).toString());
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
		DevPurchaseForm applyForm = deviceApplyBiz.getDevUseApplyFormById(id, true, true);
		request.setAttribute("applyForm", applyForm);
		List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
		request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
		request.getRequestDispatcher("/page/device/recipients/edit_reci.jsp").forward(request, response);
	}

	public void addDevApplyForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		Double budgetMoney = HttpRequestHelper.getDoubleParameter(request, "budgetMoney", 0d);//预算金额
		String buyType = HttpRequestHelper.getParameter(request, "buyType");//购买方式
		int applyType = HttpRequestHelper.getIntParameter(request, "applyType", 0);//0:领用;1:申购
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "startFlow", false);
		String strDevPurchaseList = HttpRequestHelper.getParameter(request, "devPurchaseList");//领用设备列表
		String strPurchaseDevices = HttpRequestHelper.getParameter(request, "purchaseDevices");//申购设备列表
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");//所属地区
		String purposeValues = HttpRequestHelper.getParameter(request, "purposeValues");//所属地区
		boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);//是否验证主设备
		String workAreaCode = HttpRequestHelper.getParameter(request, "workAreaCode");
		String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
		try {
			Set<PurchaseDevPurpose> purchaseDevPurposes = DeviceList.parsePurchaseDevPurposeJSON(purposeValues);
			Set<DevPurchaseList> devUseListResult = DeviceList.parsePurchaseListJSON(strDevPurchaseList);
			Set<PurchaseDevice> purchaseDevices = DeviceList.parsePurchaseDeviceJSON(strPurchaseDevices);
			deviceApplyBiz.txSaveAsDraf(deviceTypeCode,deviceClass, user.getAccountID(), user.getGroupNames(), 
					remark, buyType, budgetMoney, applyType, areaCode, workAreaCode,
					devUseListResult, purchaseDevices, purchaseDevPurposes,isStartFlow, validMainDevFlag);//领用
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}
	}
	
	public void modifyDraftForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		//String regAccountID = user.getAccountID();//登记人
		//String applicant =  HttpRequestHelper.getParameter(request, "applicant");//申请人
		String applyGroupName = user.getGroupNames();//申请人所属部门
		String remark = HttpRequestHelper.getParameter(request, "remark");
		Double budgetMoney = HttpRequestHelper.getDoubleParameter(request, "budgetMoney", 0d);//预算金额
		String buyType = HttpRequestHelper.getParameter(request, "buyType");//购买方式
		int formType = HttpRequestHelper.getIntParameter(request, "applyType", 0);//0:领用;1:申购
		//int iFormStatus = HttpRequestHelper.getIntParameter(request, "formStatus", 0);//表单状态
		//boolean bPassed = HttpRequestHelper.getBooleanParameter(request, "passed", false);//是否审批通过
		boolean isStartFlow = HttpRequestHelper.getBooleanParameter(request, "startFlow", false);
		String strDevPurchaseList = HttpRequestHelper.getParameter(request, "devPurchaseList");//设备列表
		String strPurchaseDevices = HttpRequestHelper.getParameter(request, "purchaseDevices");//申购设备列表
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");//所属地区
		String purposeValues = HttpRequestHelper.getParameter(request, "purposeValues");//所属地区
		boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);//是否验证主设备
		String workAreaCode = HttpRequestHelper.getParameter(request, "workAreaCode");
		String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
		try {
			Set<PurchaseDevPurpose> purchaseDevPurposes = DeviceList.parsePurchaseDevPurposeJSON(purposeValues);
			Set<DevPurchaseList> devPurchaseListResult = DeviceList.parsePurchaseListJSON(strDevPurchaseList);
			Set<PurchaseDevice> purchaseDevices = DeviceList.parsePurchaseDeviceJSON(strPurchaseDevices);
			deviceApplyBiz.txModifyDraftForm(id, applyGroupName, deviceTypeCode,deviceClass, 
					remark, buyType, budgetMoney, formType,areaCode, workAreaCode,
					devPurchaseListResult, purchaseDevices, purchaseDevPurposes,isStartFlow, validMainDevFlag);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}
	}
	public void checkMainAndManyTime(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
//		String purpose = HttpRequestHelper.getParameter(request, "purpose");
		String userId = HttpRequestHelper.getParameter(request, "userId");
		try {
			AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(areaCode, deviceClass);
			if(areaDeviceCfg==null){
				XMLResponse.outputStandardResponse(response, 1, "该设备类别没有区域配置！");
			}
			String deviceName = "";
				
			if(areaDeviceCfg.getMainDevFlag()){
				Set<PurchaseDevice> set = new HashSet<PurchaseDevice>();
				PurchaseDevice purchaseDevice = new PurchaseDevice();
				purchaseDevice.setBelongtoAreaCode(areaCode);
				if(areaDeviceCfg.getDeviceClass()!=null){
					purchaseDevice.setDeviceClass(areaDeviceCfg.getDeviceClass());
					deviceName = areaDeviceCfg.getDeviceClass().getName();
				}
				set.add(purchaseDevice);
				deviceApplyBiz.getPurchaseMainDevCount(set, userId, 1, null,false);
			}
			Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
//			PurchaseDevPurpose purchaseDevPurpose = new PurchaseDevPurpose();
//			for (DeviceAcptCountCfg deviceAcptCountCfg : areaDeviceCfg.getDeviceAcptCountCfgs()) {
//				if(deviceAcptCountCfg.getDevPurpose()!=null && deviceAcptCountCfg.getDevPurpose().equals(purpose)){
//					purchaseDevPurpose.setPurpose(purpose);
//					purchaseDevPurpose.setManyTimeFlag(deviceAcptCountCfg.getManyTimesFlag());
//				}
//			}
			deviceApplyBiz.checkDeviceMsg(deviceTypeCode, areaCode, deviceClass, deviceName, userId, 1, purchaseDevPurposes, false,false);
			XMLResponse.outputStandardResponse(response, 1, "操作成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "删除失败");
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
		String applyGroupName = user.getGroupNames();//申请人所属部门
		String remark = HttpRequestHelper.getParameter(request, "remark");
		Double budgetMoney = HttpRequestHelper.getDoubleParameter(request, "budgetMoney", 0d);//预算金额
		String buyType = HttpRequestHelper.getParameter(request, "buyType");//购买方式
		String strDevPurchaseList = HttpRequestHelper.getParameter(request, "devPurchaseList");//设备列表
		String strPurchaseDevices = HttpRequestHelper.getParameter(request, "purchaseDevices");//申购设备列表
		String purposeValues = HttpRequestHelper.getParameter(request, "purposeValues");//所属地区
		boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);//是否验证主设备
		String workAreaCode = HttpRequestHelper.getParameter(request, "workAreaCode");
		try {
			Set<PurchaseDevPurpose> purchaseDevPurposes = DeviceList.parsePurchaseDevPurposeJSON(purposeValues);
			Set<DevPurchaseList> devPurchaseListResult = DeviceList.parsePurchaseListJSON(strDevPurchaseList);
			Set<PurchaseDevice> purchaseDevices = DeviceList.parsePurchaseDeviceJSON(strPurchaseDevices);
			deviceApplyBiz.txDraftmanAmend(id, applyGroupName, 
					remark, buyType, budgetMoney,workAreaCode,
					devPurchaseListResult, purchaseDevices, purchaseDevPurposes, validMainDevFlag);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
	
	public void getInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request,response);
			return;
		}
		String id = HttpRequestHelper.getParameter(request, "id");
		try {
			DevPurchaseForm form = deviceApplyBiz.getDevUseApplyFormById(id, true, false);
			if (form != null) {
				XMLResponse.outputXML(response, new PurchaseDeviceListDTO(form.getPurchaseDevices()).toDocument());
			}
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}
}
