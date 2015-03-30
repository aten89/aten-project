package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.dto.DeviceAreaCongigXml;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

public class DeviceAreaCfgCtrl extends HttpServlet {

	private static final long serialVersionUID = -5839760672218706774L;

	private IDeviceAreaConfigBiz deviceAreaConfigBiz;

	/**
	 * Constructor of the object.
	 */
	public DeviceAreaCfgCtrl() {
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
		deviceAreaConfigBiz = (IDeviceAreaConfigBiz) SpringHelper.getSpringContext().getBean(
				"deviceAreaConfigBiz");
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
			// 新增设备编号
			addAreaConfig(request, response);
			return;
		} else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
			// 删除设备编号
			delAreaConfig(request, response);
			return;
		} else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
			// 修改设备编号
			modifyAreaConfig(request, response);
			return;
		} else if (SysConstants.QUERY.equals(act)) {
			// 取得所有的信息配置
			queryAreaConfig(request, response);
			return;
		} else if ("initupdate".equals(act)) {
			// 准备区域信息转向修改页面
			initUpdateConfig(request, response);
			return;
		} else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
			// 查看详情
			viewAreaConfig(request, response);
			return;
		} else if ("getClass".equals(act)) {
			// 根据设备区域查询区域设备类别配置
			getAreaDeviceCfgList(request, response);
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

		String classId = HttpRequestHelper.getParameter(request, "classId");
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String orderPrefix = HttpRequestHelper.getParameter(request, "orderPrefix");
		int seqNum = HttpRequestHelper.getIntParameter(request, "seqNum", 1);
		String separator = HttpRequestHelper.getParameter(request, "separator");
		String useApplyFlowKey = HttpRequestHelper.getParameter(request, "useApplyFlow");
		String allocateFlowKey = HttpRequestHelper.getParameter(request, "allocateFlow");
		String discardFlowKey = HttpRequestHelper.getParameter(request, "discardFlow");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		String dimissionFlowKey = HttpRequestHelper.getParameter(request, "dimissionFlow");
		Boolean mainDevFlag = HttpRequestHelper.getBooleanParameter(request, "mainDevFlag", true);
//		String devPerpose = HttpRequestHelper.getParameter(request, "devPuerpose");
		if (areaCode == null) {
			XMLResponse.outputStandardResponse(response, 0, "所属地区不能为空");
			return;
		}
		if (classId == null) {
			XMLResponse.outputStandardResponse(response, 0, "设备类型不能为空");
			return;
		}
		if (orderPrefix == null) {
			XMLResponse.outputStandardResponse(response, 0, "编号前缀不能为空");
			return;
		}
		AreaDeviceCfg areaDeviceCfg = new AreaDeviceCfg();
		areaDeviceCfg.setAreaCode(areaCode);
		DeviceClass deviceClass = new DeviceClass();
		deviceClass.setId(classId);
		areaDeviceCfg.setDeviceClass(deviceClass);
		areaDeviceCfg.setOrderPrefix(orderPrefix);
		areaDeviceCfg.setRemark(remark);
		areaDeviceCfg.setSeqNum(seqNum);
		areaDeviceCfg.setSeparator(separator);
		areaDeviceCfg.setDimissionFlowKey(dimissionFlowKey);
		areaDeviceCfg.setAllocateFlowKey(allocateFlowKey);
		areaDeviceCfg.setUseApplyFlowKey(useApplyFlowKey);
		areaDeviceCfg.setDiscardFlowKey(discardFlowKey);
		areaDeviceCfg.setDeviceClass(deviceClass);
//		Set<DeviceAcptCountCfg> deviceAcptCountCfgs = new HashSet<DeviceAcptCountCfg>(0);
//		if(devPerpose==null){
//			XMLResponse.outputStandardResponse(response, 0, "设备用途不能为空");
//		}
//		String[] str = devPerpose.split(",");
//		if(str!=null && str.length>0){
//			for (String string : str) {
//				DeviceAcptCountCfg countCfg= new DeviceAcptCountCfg();
//				countCfg.setAreaDeviceCfg(areaDeviceCfg);
//				String[] str_ = string.split(";");
//				countCfg.setDevPurpose(str_[0]);
//				if(str_[1].equals("true")){
//					countCfg.setManyTimesFlag(true);
//				}else{
//					countCfg.setManyTimesFlag(false);
//				}
//				deviceAcptCountCfgs.add(countCfg);
//			}
//		}
//		areaDeviceCfg.setDeviceAcptCountCfgs(deviceAcptCountCfgs);
		areaDeviceCfg.setMainDevFlag(mainDevFlag);
		try {
			areaDeviceCfg = deviceAreaConfigBiz.txAddAreaDeviceCfg(areaDeviceCfg);
			XMLResponse.outputStandardResponse(response, 1, areaDeviceCfg.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void delAreaConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
			return;
		}
		try {
			AreaDeviceCfg deviceNo = deviceAreaConfigBiz.txDelAreaDeviceCfg(id);
			XMLResponse.outputStandardResponse(response, 1, deviceNo.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void modifyAreaConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
			XMLResponse.outputStandardResponse(response, 0, "ID不能为空");
			return;
		}
		String classId = HttpRequestHelper.getParameter(request, "classId");
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String orderPrefix = HttpRequestHelper.getParameter(request, "orderPrefix");
		int seqNum = HttpRequestHelper.getIntParameter(request, "seqNum", 1);
		String separator = HttpRequestHelper.getParameter(request, "separator");
		String useApplyFlowKey = HttpRequestHelper.getParameter(request, "useApplyFlow");
		String allocateFlowKey = HttpRequestHelper.getParameter(request, "allocateFlow");
		String discardFlowKey = HttpRequestHelper.getParameter(request, "discardFlow");
		String dimissionFlowKey = HttpRequestHelper.getParameter(request, "dimissionFlow");
		String remark = HttpRequestHelper.getParameter(request, "remark");
		Boolean mainDevFlag = HttpRequestHelper.getBooleanParameter(request, "mainDevFlag", true);
//		String devPerpose = HttpRequestHelper.getParameter(request, "devPuerpose");
		if (areaCode == null) {
			XMLResponse.outputStandardResponse(response, 0, "所属地区不能为空");
			return;
		}
		if (classId == null) {
			XMLResponse.outputStandardResponse(response, 0, "设备类型不能为空");
			return;
		}
		if (orderPrefix == null) {
			XMLResponse.outputStandardResponse(response, 0, "编号前缀不能为空");
			return;
		}
		AreaDeviceCfg areaDeviceCfg = new AreaDeviceCfg();
		areaDeviceCfg.setAreaCode(areaCode);
		DeviceClass deviceClass = new DeviceClass();
		deviceClass.setId(classId);
		areaDeviceCfg.setDeviceClass(deviceClass);
		areaDeviceCfg.setOrderPrefix(orderPrefix);
		areaDeviceCfg.setRemark(remark);
		areaDeviceCfg.setSeqNum(seqNum);
		areaDeviceCfg.setSeparator(separator);
		areaDeviceCfg.setAllocateFlowKey(allocateFlowKey);
		areaDeviceCfg.setUseApplyFlowKey(useApplyFlowKey);
		areaDeviceCfg.setDiscardFlowKey(discardFlowKey);
		areaDeviceCfg.setDimissionFlowKey(dimissionFlowKey);
		areaDeviceCfg.setId(id);
//		Set<DeviceAcptCountCfg> deviceAcptCountCfgs = new HashSet<DeviceAcptCountCfg>(0);
//		if(devPerpose==null){
//			XMLResponse.outputStandardResponse(response, 0, "设备用途不能为空");
//		}
//		String[] str = devPerpose.split(",");
//		if(str!=null && str.length>0){
//			for (String string : str) {
//				DeviceAcptCountCfg countCfg= new DeviceAcptCountCfg();
//				countCfg.setAreaDeviceCfg(areaDeviceCfg);
//				String[] str_ = string.split(";");
//				countCfg.setDevPurpose(str_[0]);
//				if(str_[1].equals("true")){
//					countCfg.setManyTimesFlag(true);
//				}else{
//					countCfg.setManyTimesFlag(false);
//				}
//				deviceAcptCountCfgs.add(countCfg);
//			}
//		}
//		areaDeviceCfg.setDeviceAcptCountCfgs(deviceAcptCountCfgs);
		areaDeviceCfg.setMainDevFlag(mainDevFlag);
		try {
			areaDeviceCfg = deviceAreaConfigBiz.txModifyAreaDeviceCfg(areaDeviceCfg);
			XMLResponse.outputStandardResponse(response, 1, areaDeviceCfg.getId());
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
		}
	}

	private void queryAreaConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			QueryParameters qp=new QueryParameters();
			int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
			int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
			qp.setPageNo(pageNo);
			qp.setPageSize(pageSize);
			qp.addOrder("areaCode", true);
			qp.addOrder("deviceClass", true);
			// 查询
			ListPage<AreaDeviceCfg> list = deviceAreaConfigBiz.queryAllAreaDeviceCfgPage(qp);
			XMLResponse.outputXML(response, new DeviceAreaCongigXml(list).toPageDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统出错");
			return;
		}
	}
	
	/**
	 * 初始化区域设备修改页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void initUpdateConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		AreaDeviceCfg areaDeviceCfg;
		try {
			areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfg(id);
			request.setAttribute("areaDeviceCfg", areaDeviceCfg);
			request.getRequestDispatcher("/page/device/paramconf/area/edit_area.jsp").forward(request, response);
		} catch (OaException e) {
			e.printStackTrace();
		}
		
	}
	

	/**
	 * 查看设备区域配置详情
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void viewAreaConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		try {
			AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfg(id);
			request.setAttribute("areaDeviceCfg", areaDeviceCfg);
			request.getRequestDispatcher("/page/device/paramconf/area/view_area.jsp").forward(request, response);
		} catch (OaException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询设备类型
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getAreaDeviceCfgList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			// 查询
		String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
		String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
		List<AreaDeviceCfg> list = deviceAreaConfigBiz.queryAreaDeviceByAreaCode(areaCode,deviceType);
		XMLResponse.outputXML(response, new DeviceAreaCongigXml(list).toDocument());
	}
}
