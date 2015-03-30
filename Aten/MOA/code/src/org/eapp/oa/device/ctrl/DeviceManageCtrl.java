package org.eapp.oa.device.ctrl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.device.blo.IDevDiscardDisposeBiz;
import org.eapp.oa.device.blo.IDevFlowBiz;
import org.eapp.oa.device.blo.IDevRepairFormBiz;
import org.eapp.oa.device.blo.IDevValidateFormBiz;
import org.eapp.oa.device.blo.IDeviceAllocateBiz;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceClassBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.blo.IDeviceUpdateLogBiz;
import org.eapp.oa.device.dto.DevAllocateListDTO;
import org.eapp.oa.device.dto.DevFlowProcessFormDTO;
import org.eapp.oa.device.dto.DevFlowProcessListDTO;
import org.eapp.oa.device.dto.DevPurchaseListDTO;
import org.eapp.oa.device.dto.DevRepairFormListDTO;
import org.eapp.oa.device.dto.DevValidataFormJSON;
import org.eapp.oa.device.dto.DevValidateFormList;
import org.eapp.oa.device.dto.DeviceList;
import org.eapp.oa.device.dto.DeviceOptionLists;
import org.eapp.oa.device.dto.DevicePage;
import org.eapp.oa.device.dto.DevicePropertyList;
import org.eapp.oa.device.dto.DeviceQueryParameters;
import org.eapp.oa.device.dto.DeviceUpdateLogListDTO;
import org.eapp.oa.device.dto.DeviceXml;
import org.eapp.oa.device.dto.DiscardDevListDTO;
import org.eapp.oa.device.dto.StatusMapSelect;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevDiscardDealForm;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevFlowApplyProcess;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.DevRepairForm;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.device.hbean.DeviceUpdateLog;
import org.eapp.oa.device.hbean.DeviceValiDetail;
import org.eapp.oa.device.hbean.DiscardDealDevList;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.web.HTMLResponse;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.dto.DataDictionarySelect;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.spring.SpringHelper;

/**
 * IT设备-->设备管理
 * 
 */
public class DeviceManageCtrl extends HttpServlet implements Serializable {

    private static final long serialVersionUID = -8207014788875789738L;

    private static IDeviceBiz deviceBiz;
    private static IDevValidateFormBiz devValidateFormBiz;
    private IDevRepairFormBiz devRepairFormBiz;
    private IDeviceAreaConfigBiz deviceAreaConfigBiz;
    private IDeviceClassBiz deviceClassBiz;
    private IDeviceDiscardBiz deviceDiscardBiz;
    private IDeviceAllocateBiz deviceAllocateBiz;
    private IDeviceApplyBiz deviceApplyBiz;
    private IDeviceUpdateLogBiz deviceUpdateLogBiz;
    private IDevFlowBiz devFlowBiz;
    private DeviceClassCtrl deviceClassCtrl;
    private IDevDiscardDisposeBiz devDiscardDisposeBiz;

    public DeviceManageCtrl() {

    }

    public void init() throws ServletException {
        deviceBiz = (IDeviceBiz) SpringHelper.getSpringContext().getBean("deviceBiz");
        devValidateFormBiz = (IDevValidateFormBiz) SpringHelper.getSpringContext().getBean("devValidateFormBiz");
        deviceApplyBiz = (IDeviceApplyBiz) SpringHelper.getSpringContext().getBean("deviceApplyBiz");
        devRepairFormBiz = (IDevRepairFormBiz) SpringHelper.getSpringContext().getBean("devRepairFormBiz");
        deviceAreaConfigBiz = (IDeviceAreaConfigBiz) SpringHelper.getSpringContext().getBean("deviceAreaConfigBiz");
        deviceClassBiz = (IDeviceClassBiz) SpringHelper.getSpringContext().getBean("deviceClassBiz");
        deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getSpringContext().getBean("deviceDiscardBiz");
        deviceAllocateBiz = (IDeviceAllocateBiz) SpringHelper.getSpringContext().getBean("deviceAllocateBiz");
        deviceUpdateLogBiz = (IDeviceUpdateLogBiz) SpringHelper.getSpringContext().getBean("deviceUpdateLogBiz");
        devFlowBiz = (IDevFlowBiz) SpringHelper.getSpringContext().getBean("devFlowBiz");
        deviceClassCtrl = new DeviceClassCtrl();
        deviceClassCtrl.init();
        devDiscardDisposeBiz = (IDevDiscardDisposeBiz)SpringHelper.getSpringContext().getBean("devDiscardDisposeBiz");
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * The doPost method of the servlet.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String act = HttpRequestHelper.getParameter(request, "act");
        if ("initmenu".equalsIgnoreCase(act)) {
        	initMenu(request, response);
        } else if ("initadd".equalsIgnoreCase(act)) {
            // 初始化设备添加页面
            initAdd(request, response, "/page/device/manage/edit_dev.jsp");
            return;
        } else if ("initmodify".equalsIgnoreCase(act)) {
            initModify(request, response,
                    "/page/device/manage/edit_dev.jsp?flag=modify");
            return;
        } else if (SysConstants.ADD.equalsIgnoreCase(act)) {
            // 添加设备
            saveDevice(request, response);
            return;
        } else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
            // 修改设备
            saveDevice(request, response);
            return;
        } else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
            // 删除设备(修改状态为 status = 2)
            discardDevice(request, response);
            return;
        } else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
            // 查询
            queryDeviceList(request, response);
            return;
        } else if ("deviceTypeSelect".equalsIgnoreCase(act)) {
            // 初始化设备类型
            initDeviceTypeSelect(request, response);
            return;
        } else if ("initBuyType".equalsIgnoreCase(act)) {
            // 加载设备购买类别
            initBuyType(request, response);
            return;
        } else if ("initDeviceProperty".equalsIgnoreCase(act)) {
            // 加载对应设备类型属性
            initDeviceProperty(request, response);
            return;
        } else if ("viewValidetail".equalsIgnoreCase(act)) {
            // 查看验证明细
            viewValiDetail(request, response);
            return;
        } else if (SysConstants.VIEW.equalsIgnoreCase(act)) {
            // 查看设备
            viewDevice(request, response);
            return;
//        } else if ("xmlDevice".equalsIgnoreCase(act)) {
//            // 设备信息DIV展现 XML 数据构造 填写维修单时用
//            xmlDevice(request, response);
//            return;
        } else if ("initdevice".equals(act)) {
            initITDeviceById(request, response);
            return;
        } else if ("export".equals(act)) {
            export(request, response);
            return;
        } else if ("import".equals(act)) {
            // importDate(request, response);
            return;
        } else if ("getnuber".equals(act)) {
            // 根据设备类型获取设备编号
            getDeviceNOByType(request, response);
            return;
        } else if ("initdevselpage".equals(act)) {
            // 初始化选择设备页面
            initDevSelPage(request, response);
            return;
        } else if ("loaddevicepage".equals(act)) {
            // 加载设备分页数据：可以选择多个设备
            loadDevicePage(request, response);
            return;
        } else if ("loaddevicelist".equals(act)) {
            // 加载设备列表数据
            loadDeviceList(request, response);
            return;
        } else if ("initpurchase".equals(act)) {
            // 初始化设备领用页面
            initDevPurchaseForm(request, response);
            return;
        } else if ("initallot".equals(act)) {
            // 初始化设备调拨信息
            initDevAllocateForm(request, response);
            return;
        } else if ("initdiscard".equals(act) || "initleave".equals(act)) {
            // 初始化设备报废信息|离职处理信息
            initDevDiscardForm(request, response);
            return;
        } else if ("initsingledevselpage".equals(act)) {
            // 加载设备列表数据:只能选择一个设备
            initSingeDevSelPage(request, response);
            return;
        } else if (SysConstants.RECIPIENTS.equals(act)) {
            // 设备领用
            addDevApplyForm(request, response);
            return;
        } else if ("initmaintain".equals(act)) {
            // 维修登记信息
        	initAddMaintain(request, response);
            return;
        } else if (SysConstants.MAINTAIN.equals(act)) {
            // 保存维修登记信息
            saveDeviceMaintain(request, response);
            return;
        } else if (SysConstants.ALLOCATION.equals(act)) {
            // 保存调拨登记信息
            saveDeviceAllocation(request, response);
            return;
        } else if (SysConstants.SCRAP.equals(act)) {
            // 保存报废登记信息
            saveDeviceScrap(request, response);
            return;
        } else if ("initscrapdeal".equals(act)) {
            // 保存报废处理登记信息
        	initScrapDeal(request, response);
            return; 
            
        } else if (SysConstants.SCRAPDEAL.equals(act)) {
            // 保存报废处理登记信息
        	addDiscardDispose(request, response);
            return;
        } else if ("loaduselist".equals(act)) {
            // 加载设备领用记录列表
            getDevicePurchaseList(request, response);
            return;
        } else if ("viewuse".equals(act)) {
            // 查看领用记录
            getDevicePurchaseForm(request, response);
            return;
        } else if ("loadallotlist".equals(act)) {
            // 加载设备调拨记录列表
            getDeviceAllotList(request, response);
            return;
        } else if ("viewallot".equals(act)) {
            // 查看调拨记录
            getDeviceAllotForm(request, response);
            return;
        } else if ("loadrepairlist".equals(act)) {
            // 加载设备维修记录列表
            getDeviceRepairList(request, response);
            return;
        } else if ("viewrepair".equals(act)) {
            // 查看维修记录
            getDeviceRepairForm(request, response);
            return;
        } else if ("loadscraplist".equals(act)) {
            // 设备报废记录列表
            getDeviceScrapList(request, response);
            return;
        } else if ("viewscrap".equals(act)) {
            // 查看报废记录
            getDeviceScrapForm(request, response);
            return;
        } else if ("viewscrapdispose".equals(act)) {
            // 查看报废处理记录
        	getDeviceScrapDisposeForm(request, response);
        } else if ("loaduptloglist".equals(act)) {
            // 加载设备更新日志列表
            getDeviceUptLogList(request, response);
            return;
//        } else if ("getareadevusetype".equals(act)) {
//            // 取得与区域设备类别关联的设备用途
//            getAreaDevUseTypeSel(request, response);
//            return;
        } else if ("getstatusselect".equals(act)) {
            // 状态下拉框
            getStatusSelect(request, response);
            return;
//        } else if ("getareadevusedivtype".equals(act)) {
//            // 设备用途div下拉框
//            getAreaDevUseTypeDivSel(request, response);
//            return;
        } else if ("loaddevflowloglist".equals(act)) {
            // 取得设备操作流程日志
            getDeviceFlowLogList(request, response);
            return;
        } else if ("getstatuslist".equals(act)) {
            // 状态lists
            getStatusList(request, response);
            return;
//        } else if ("toviewbasic".equals(act)) {
//            // 查看设备的基本设备但不包含验收单和设备操作日志
//            viewBasicDeviceInfo(request, response);
//            return;
        } else if ("loadleaddevicepage".equals(act)) {
            // 申购设备查看库存设备信息
            loadLeadDevicePage(request, response);
            return;
        } else if ("getstatus".equals(act)) {
            // 判断单个设备是否在流程中
            getStatusMsg(request, response);
            return;
        } else if ("getmanystatus".equals(act)) {
            // 判断多个设备是否在流程中
            getManyStatusMsg(request, response);
            return;
//        } else if ("getdevpurpose".equals(act)) {
//            // 设备用途
//            getAreaDevPuerposes(request, response);
//            return;
        }
    }
    
    protected void initMenu(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	request.setAttribute("companyAreas", SysCodeDictLoader.getInstance().getAreaMap().values());
    	request.getRequestDispatcher("/page/device/manage/frame_dev.jsp").forward(request, response);
	}
    

    protected void export(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // 获取参数

        String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        String deviceNO = HttpRequestHelper.getParameter(request, "deviceNO");
        String deviceName = HttpRequestHelper.getParameter(request, "deviceName");
        String buyTimeStart = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "buyTimeStart"));
        String buyTimeEnd = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "buyTimeEnd"));

        // 构造查询条件
        DeviceQueryParameters qp = new DeviceQueryParameters();
        qp.setDeviceClass(deviceClass);
        qp.setDeviceType(deviceType);
        qp.setDeviceNO(deviceNO);

        qp.setDeviceName(deviceName);

        if (buyTimeStart != null) {
            qp.setBeginBuyTime(Timestamp.valueOf(buyTimeStart));
        }
        if (buyTimeEnd != null) {
            Timestamp t = Timestamp.valueOf(buyTimeEnd);
            Calendar ca = Calendar.getInstance();
            ca.setTimeInMillis(t.getTime());
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
            t.setTime(ca.getTimeInMillis());
            qp.setEndBuyTime(t);
        }
        qp.addOrder("deviceNO", true);
        ListPage<Device> list = deviceBiz.getDeviceListPage(qp, null);

        if (list == null || list.getDataList() == null || list.getDataList().size() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "未找到设备数据！");
            return;
        }
        // 创建一个文件名
        String fileName = request.getSession().getId() + System.currentTimeMillis() / 10000 + ".xls";
        try {
            deviceBiz.queryDeviceToExcel(list, fileName, new File(FileDispatcher.getTempDir()));
            XMLResponse.outputStandardResponse(response, 1, FileDispatcher.getTempAbsDir() + fileName);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            e.printStackTrace();
        }
    }

    protected void initAdd(HttpServletRequest request, HttpServletResponse response, String toPage)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        Device device = new Device();
        device.setAreaCode(areaCode);
        device.setRegTime(new Date());
        device.setRegAccountID(user.getAccountID());
        device.setDeviceType(deviceTypeCode);
        request.setAttribute("device", device);
        Collection<DeviceValiDetail> valiDetails = deviceBiz.getDeviceValiDetail(null,
                DevValidateForm.VALITYPE_DIRECT_IN, "DEVICE-VALIDATE-DIRECT");
        request.setAttribute("valiDetails", valiDetails);
        request.getRequestDispatcher(toPage).forward(request, response);
    }

    protected void initModify(HttpServletRequest request, HttpServletResponse response, String toPage)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }

        Device device = deviceBiz.getDeviceById(id);
        request.setAttribute("device", device);
        if (device.getAreaCode() == null || device.getDeviceClass() == null || device.getDeviceClass().getId() == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        // List<DevValidateForm> valiedates = devValidateFormBiz.getDevValidateForms(id,
        // DevValidateForm.VALITYPE_DIRECT_IN);
        if (device != null && device.getDevValidateForm() != null) {
            request.setAttribute("valiFormFlag", true);
        } else {
            request.setAttribute("valiFormFlag", false);
        }
        try {
            String deviceNo = "";
            AreaDeviceCfg deviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(device.getAreaCode(), device
                    .getDeviceClass().getId());
            if (deviceCfg != null) {
                if (deviceCfg.getOrderPrefix() != null) {
                    deviceNo += deviceCfg.getOrderPrefix();
                }
                if (deviceCfg.getSeparator() != null) {
                    deviceNo += deviceCfg.getSeparator();
                }
            }
            // 流水号数值
            request.setAttribute("sepNum",
                    SerialNoCreater.getIsomuxByNum(String.valueOf(device.getSequence()), deviceCfg.getSeqNum()));
            // 流水号位数
            request.setAttribute("orderLength", deviceCfg.getSeqNum());
            // 流水号前缀
            request.setAttribute("deviceNO", deviceNo);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, "获取设备编号配置出错");
            e.printStackTrace();
        }
        request.getRequestDispatcher(toPage).forward(request, response);
    }

    protected void saveDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 设备参数
        String deviceId = HttpRequestHelper.getParameter(request, "deviceId");
        String deviceNO = HttpRequestHelper.getParameter(request, "deviceNO"); // 设备编号
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode"); // 资产类别
        String deviceClassId = HttpRequestHelper.getParameter(request, "deviceClass"); // 设备类别
        String deviceModel = HttpRequestHelper.getParameter(request, "deviceModel"); // 设备型号
        String deviceName = HttpRequestHelper.getParameter(request, "deviceName"); // 设备名称
        int status = HttpRequestHelper.getIntParameter(request, "status", DeviceCurStatusInfo.STATUS_NORMAL);
        String buyTimeStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "buyTime")); // 购买日期
        String buyType = HttpRequestHelper.getParameter(request, "buyType"); // 购买方式
        String description = HttpRequestHelper.getParameter(request, "description"); // 设备描述
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode"); // 设备所属区域
        double price = HttpRequestHelper.getDoubleParameter(request, "price", 0.0); // 金额
        double financeOriginalVal = HttpRequestHelper.getDoubleParameter(request, "financeOriginalVal", 0.0); // 财务原值
        Boolean deductFlag = HttpRequestHelper.getBooleanParameter(request, "deductFlag", true);// 是否扣款
        double deductMoney = HttpRequestHelper.getDoubleParameter(request, "deductMoney", 0.0); // 扣款金额
        String inDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "inDate")); // 到款日期
        // 设备属性
        String optionJSON = HttpRequestHelper.getParameter(request, "optionLists"); // 设备属性
        // 设备验收单
        boolean checkFormFlag = HttpRequestHelper.getBooleanParameter(request, "checkFormFlag", true); // 是否添加验收单
        String checkFormJSON = HttpRequestHelper.getParameter(request, "checkForm"); // 设备验收单
        int sequence = HttpRequestHelper.getIntParameter(request, "sepNum", 0);
        if (deviceClassId == null || deviceName == null || buyTimeStr == null) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }

        try {

            Device device = new Device();
            device.setId(deviceId);
            device.setAreaCode(areaCode);
            device.setBuyTime(Timestamp.valueOf(buyTimeStr));
            // device.setDeductMoney(deductMoney);
            device.setDeviceType(deviceTypeCode);
            device.setDeviceModel(deviceModel);
            DeviceClass deviceClass_ = new DeviceClass();
            deviceClass_.setId(deviceClassId);
            device.setDeviceClass(deviceClass_);
            device.setDescription(description);
            device.setPrice(price);
            device.setBuyType(buyType);
            device.setDeviceNO(deviceNO);
            device.setDeviceName(deviceName);
            device.setSequence(sequence);
            device.setDeductFlag(deductFlag);
            device.setDeductMoney(deductMoney);
            device.setRegAccountID(user.getAccountID());
            device.setRegTime(new Date());
            device.setFinanceOriginalVal(financeOriginalVal);
            if (inDateStr != null) {
                device.setInDate(Timestamp.valueOf(inDateStr));
            }
            if (optionJSON != null) {
                device.setDevicePropertyDetails(DeviceOptionLists.parseJSON(optionJSON, device));
            }
            if (checkFormFlag && checkFormJSON != null) {
                device.setDevValidateForm(DevValidataFormJSON.parseJSON(checkFormJSON, device));
            }

            // 保存设备状态表
            DeviceCurStatusInfo statusInfo = null;
            if (device.getDeviceCurStatusInfo() == null) {
                statusInfo = new DeviceCurStatusInfo();
            }
            statusInfo.setAreaCode(areaCode);
            statusInfo.setDeviceCurStatus(status);
            statusInfo.setStatusUptDate(new Date());
            statusInfo.setDevice(device);
            device.setDeviceCurStatusInfo(statusInfo);
            if (deviceId == null) {// 新增
                device = deviceBiz.txAddDevice(device);
            } else {// 修改
                device = deviceBiz.txModifyDevice(device);
            }

            XMLResponse.outputStandardResponse(response, 1, "操作成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            XMLResponse.outputStandardResponse(response, 0, "操作失败");
            e.printStackTrace();
        }
    }

    protected void discardDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        try {
            deviceBiz.txDeleteDevice(id);
            XMLResponse.outputStandardResponse(response, 1, "操作成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 查询设备列表分页数据
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void queryDeviceList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 获取参数
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 12);
        String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        String statusStr = HttpRequestHelper.getParameter(request, "status");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        String deviceNO = HttpRequestHelper.getParameter(request, "deviceNO");
        String deviceName = HttpRequestHelper.getParameter(request, "deviceName");
        String buyTimeStart = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "buyTimeStart"));
        String buyTimeEnd = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "buyTimeEnd"));
        String isUsered = HttpRequestHelper.getParameter(request, "isUsered");
        String userAccountID = HttpRequestHelper.getParameter(request, "userId");
        String groupName = HttpRequestHelper.getParameter(request, "groupName");
        String sortCol = HttpRequestHelper.getParameter(request, "sortCol");
        Boolean ascend = HttpRequestHelper.getBooleanParameter(request, "ascend", false);
        String deviceModel = HttpRequestHelper.getParameter(request, "deviceModel");
        // 构造查询条件
        DeviceQueryParameters qp = new DeviceQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        qp.setDeviceClass(deviceClass);
        qp.setDeviceType(deviceType);
        qp.setDeviceNO(deviceNO);
        qp.setDeviceName(deviceName);
        qp.setApplicantID(userAccountID);
        qp.setGroupName(groupName);
        qp.setAreaCode(areaCode);
        if (statusStr != null) {
            qp.setStatus(Integer.valueOf(statusStr));
        }
        if (isUsered != null) {
            qp.setIsUsered(Integer.valueOf(isUsered));
        }
        if (buyTimeStart != null) {
            qp.setBeginBuyTime(Timestamp.valueOf(buyTimeStart));
        }
        if (buyTimeEnd != null) {
            Timestamp t = Timestamp.valueOf(buyTimeEnd);
            Calendar ca = Calendar.getInstance();
            ca.setTimeInMillis(t.getTime());
            ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
            t.setTime(ca.getTimeInMillis());
            qp.setEndBuyTime(t);
        }
        if (sortCol != null && !sortCol.trim().equals("")) {
            qp.addOrder(sortCol, ascend);
        } else {
            qp.addOrder("buyTime", false);
        }
        Integer assignType = DeviceClassAssignDetail.ASSIGN_MANAGER;
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        List<String> groupNames = new ArrayList<String>();
        if (user.getGroups() != null) {
            for (Name n : user.getGroups()) {
                groupNames.add(n.getName());
            }
        }
        List<String> postNames = new ArrayList<String>();
        if (user.getPosts() != null) {
            for (Name n : user.getPosts()) {
                postNames.add(n.getName());
            }
        }
        if (deviceModel != null && deviceModel.length() > 0) {
            qp.setDeviceModel(deviceModel);
        }
        // 查询
        List<DeviceClass> list = deviceClassBiz.getAssignClass(deviceType, areaCode, assignType, user.getAccountID(),
                groupNames, postNames);
        List<String> listStr = new ArrayList<String>();
        if (list != null) {
            for (DeviceClass deviceClass2 : list) {
                listStr.add(deviceClass2.getId());
            }
        }
        ListPage<Device> listPage = deviceBiz.getDeviceListPage(qp, listStr);
        XMLResponse.outputXML(response, new DevicePage(listPage).toDocument());
    }

    protected void initDeviceTypeSelect(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
        String isAccessory = HttpRequestHelper.getParameter(request, "isAccessory");
        if (isAccessory != null) {
            isAccessory = isAccessory.equals("true") ? "1" : "0";
        }

        try {
            HTMLResponse.outputHTML(response,
                    new DataDictionarySelect(deviceBiz.getDeviceType(deviceClass, isAccessory)).toString());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    private void initBuyType(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
            Map<String, List<DataDictInfo>> map = SysCodeDictLoader.getInstance().getBuyTypeMapByKey();
            if (deviceType == null) {
                return;
            }
            List<DataDictInfo> list = map.get(deviceType);
            HTMLResponse.outputHTML(response, new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    protected void initDeviceProperty(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass"); // 大类别
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType"); // 类型
        String id = HttpRequestHelper.getParameter(request, "id"); // 类型
        StringBuffer sb = null;

        // 根据类型查找编号

        // List<DeviceNo> list = deviceNoBiz.findByDeviceType(deviceType);
        // if(list!=null && list.size()>0){
        // DeviceNo no = list.get(0);
        //
        // String curryOrder = String.valueOf(Integer.parseInt(no.getOrderPostfix()) + 1);
        // int maxLength = no.getOrderPostfix().length();
        //
        // int curryLength = curryOrder.length();
        //
        // if(curryLength < maxLength){
        // int temp = maxLength - curryLength;
        // for(int i = 0 ; i < temp ; i++){
        // curryOrder = "0"+ curryOrder;
        // }
        // }
        //
        // sb = new StringBuffer();
        // sb.append(no.getOrderPrefix());
        // sb.append(curryOrder);
        //
        // }

        try {
            Collection<DeviceProperty> dps = deviceBiz.getDeviceProperty(id, deviceClass, deviceType, true);
            XMLResponse.outputXML(response, new DevicePropertyList(dps, sb).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    protected void viewValiDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String deviceId = HttpRequestHelper.getParameter(request, "deviceId");
        if (deviceId == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        try {
            Collection<DevValidateForm> list = devValidateFormBiz.getDevValidateForms(deviceId, null);
            XMLResponse.outputXML(response, new DevValidateFormList(list).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    private void viewDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        Device device = deviceBiz.getDeviceById(id);
        request.setAttribute("device", device);
        request.getRequestDispatcher("/page/device/manage/view_dev.jsp").forward(
                request, response);
    }

//    private void viewBasicDeviceInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        String id = HttpRequestHelper.getParameter(request, "id");
//        if (id == null) {
//            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
//            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
//            return;
//        }
//        Device device = deviceBiz.getDeviceById(id);
//        request.setAttribute("device", device);
//        request.getRequestDispatcher("/page/EquipmentManagement/EquipmentFlowApproval/EquipmentInfoView.jsp").forward(
//                request, response);
//    }

//    private void xmlDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
//        if (user == null) {
//            XMLResponse.outputStandardResponse(response, 0, "请先登录");
//            return;
//        }
//        Integer assignType = DeviceClassAssignDetail.ASSIGN_MANAGER;
//        if (user == null) {
//            XMLResponse.outputStandardResponse(response, 0, "请先登录");
//            return;
//        }
//        List<String> groupNames = new ArrayList<String>();
//        if (user.getGroups() != null) {
//            for (Name n : user.getGroups()) {
//                groupNames.add(n.getName());
//            }
//        }
//        List<String> postNames = new ArrayList<String>();
//        if (user.getPosts() != null) {
//            for (Name n : user.getPosts()) {
//                postNames.add(n.getName());
//            }
//        }
//
//        String deviceName = HttpRequestHelper.getParameter(request, "deviceName");
//        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
//        String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
//        String statusStr = HttpRequestHelper.getParameter(request, "status");
//        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
//        // 查询
//        List<DeviceClass> list = deviceClassBiz.getAssignClass(deviceType, areaCode, assignType, user.getAccountID(),
//                groupNames, postNames);
//        List<String> listStr = new ArrayList<String>();
//        if (list != null) {
//            for (DeviceClass deviceClass2 : list) {
//                listStr.add(deviceClass2.getId());
//            }
//        }
//        try {
//            DeviceQueryParameters qp = new DeviceQueryParameters();
//            qp.setPageSize(QueryParameters.ALL_PAGE_SIZE);
//            qp.setDeviceName(deviceName);
//            qp.setDeviceType(deviceType);
//            qp.setDeviceClass(deviceClass);
//            if (statusStr != null) {
//                // 将String数组转为Integer数组
//                String[] statusStrArr = statusStr.split(",");
//                Integer[] statusArr = new Integer[statusStrArr.length];
//                for (int i = 0; i < statusStrArr.length; i++) {
//                    statusArr[i] = Integer.valueOf(statusStrArr[i]);
//                }
//                // qp.setStatusArr(statusArr);
//            }
//            
//            ListPage<Device> listPage = deviceBiz.getDeviceListPage(qp, listStr);
//            XMLResponse.outputXML(response, new DevicePage(listPage).toDocument());
//        } catch (Exception e) {
//            XMLResponse.outputStandardResponse(response, 0, "系统出错");
//            return;
//        }
//    }

    private void initITDeviceById(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        Device device = deviceBiz.getDeviceById(id);
        try {
            XMLResponse.outputXML(response, new DeviceXml(device).toDocument());
        } catch (Exception e) {
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    public String dateToString(Date date, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        String str = format.format(date);
        return str;
    }

    protected void getDeviceNOByType(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String classID = HttpRequestHelper.getParameter(request, "classID");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        if (classID == null || areaCode == null) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        try {
            AreaDeviceCfg deviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(areaCode, classID);
            String deviceNo = "";
            if (deviceCfg != null) {

                if (deviceCfg.getOrderPrefix() != null) {
                    deviceNo += deviceCfg.getOrderPrefix();
                }
                if (deviceCfg.getSeparator() != null) {
                    deviceNo += deviceCfg.getSeparator();
                }
                int sepNum = deviceBiz.getDeviceSepNum(classID, areaCode) + 1;
                deviceNo = deviceNo + ";"
                        + SerialNoCreater.getIsomuxByNum(String.valueOf(sepNum), deviceCfg.getSeqNum()) + ";"
                        + deviceCfg.getSeqNum();
                XMLResponse.outputStandardResponse(response, 1, deviceNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    private void loadDevicePage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        String deviceClassCode = HttpRequestHelper.getParameter(request, "deviceClassCode");
        String deviceName = HttpRequestHelper.getParameter(request, "deviceName");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        String isUsing = HttpRequestHelper.getParameter(request, "isUsing");
        String buyType = HttpRequestHelper.getParameter(request, "buyType");
        String status = HttpRequestHelper.getParameter(request, "status");
        String excludeDeviceIDs = HttpRequestHelper.getParameter(request, "excludeDeviceIDs");
        boolean initDevCfgList = HttpRequestHelper.getBooleanParameter(request, "initDevCfgList", false);// 是否初始化设备配置项
        boolean myDeviceFlag = HttpRequestHelper.getBooleanParameter(request, "myDeviceFlag", false);// 我名下的设备标识
        boolean assignDevClassFlag = HttpRequestHelper.getBooleanParameter(request, "assignDevClassFlag", false);// 我名下的设备标识
        boolean mergeFlag = HttpRequestHelper.getBooleanParameter(request, "mergeFlag", false);// 是否同时追加我名下的设备和闲置设备的结果集
        String strApprovingFlag = HttpRequestHelper.getParameter(request, "approvingFlag");
        Boolean bApprovingFlag = strApprovingFlag == null ? null : Boolean.valueOf(strApprovingFlag);
        int assignType = HttpRequestHelper.getIntParameter(request, "assignType", 0);
        String deleteDeviceIDs = HttpRequestHelper.getParameter(request, "deleteDeviceIDs");
        String excludeSelfBuyFlag = HttpRequestHelper.getParameter(request, "excludeSelfBuyFlag");// 过滤掉个人全额
        String excludeScrapFlag = HttpRequestHelper.getParameter(request, "excludeScrapFlag");
        String excludeSubBuyFlag = HttpRequestHelper.getParameter(request, "excludeSubBuyFlag");// 过滤掉比例购买
        String deviceNo = HttpRequestHelper.getParameter(request, "deviceNo");// 过滤掉比例购买
        List<String> deleteDeviceIDsAtReject = new ArrayList<String>();
        if (deleteDeviceIDs != null) {
            deleteDeviceIDsAtReject.addAll(Arrays.asList(deleteDeviceIDs.split(",")));
        }

        try {
            DeviceQueryParameters qp = new DeviceQueryParameters();
            if (deviceTypeCode != null) {
                qp.setDeviceType(deviceTypeCode);
            }
            if (deviceClassCode != null) {
                qp.setDeviceClass(deviceClassCode);
            }
            if (deviceNo != null) {
                qp.setDeviceNO(deviceNo);
            }
            if (deviceName != null) {
                qp.setDeviceName(deviceName);
            }
            if (excludeDeviceIDs != null) {
                qp.setIDs(excludeDeviceIDs.split(","));
            }
            if (isUsing != null) {
                qp.setIsUsing(Integer.valueOf(isUsing));
            }
            if (areaCode != null) {
                qp.setAreaCode(areaCode);
            }
            if (status != null) {
                qp.setStatus(Integer.valueOf(status));
            }
            if (buyType != null) {
                String[] buyTypeStr = buyType.split(",");
                qp.setBuyTypes(buyTypeStr);
            }
            String ownerAccountID = null;// 设备归属账户ID
            if (myDeviceFlag) {
                ownerAccountID = user.getAccountID();
            }
            if (excludeSelfBuyFlag != null) {
                qp.setExcludeSelfBuyFlag(Boolean.valueOf(excludeSelfBuyFlag));
            }
            if (excludeSubBuyFlag != null) {
                qp.setExcludeSubBuyFlag(Boolean.valueOf(excludeSubBuyFlag));
            }
            Boolean bExcludeScrapFlag = excludeScrapFlag == null ? null : Boolean.valueOf(excludeScrapFlag);
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            qp.addOrder("deviceNO", true);
            //资产类别，设备类别，设备名称排序
            List<String> assignDeviceClassIDs = new ArrayList<String>();
            if (assignDevClassFlag) {
                List<String> groupNames = new ArrayList<String>();
                if (user.getGroups() != null) {
                    for (Name n : user.getGroups()) {
                        groupNames.add(n.getName());
                    }
                }
                List<String> postNames = new ArrayList<String>();
                if (user.getPosts() != null) {
                    for (Name n : user.getPosts()) {
                        postNames.add(n.getName());
                    }
                }
                // 查询
                List<DeviceClass> deviceClasses = deviceClassBiz.getAssignClass(deviceTypeCode, areaCode, assignType,
                        user.getAccountID(), groupNames, postNames);
                if (deviceClasses != null) {
                    for (DeviceClass deviceClass : deviceClasses) {
                        assignDeviceClassIDs.add(deviceClass.getId());
                    }
                }
            }
            ListPage<Device> devices = deviceBiz.queryDevices(qp, ownerAccountID, assignDevClassFlag,
                    assignDeviceClassIDs, mergeFlag, bApprovingFlag, bExcludeScrapFlag, deleteDeviceIDsAtReject,
                    initDevCfgList);
            XMLResponse.outputXML(response, new DevicePage(devices).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    private void loadDeviceList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String deviceIDs = HttpRequestHelper.getParameter(request, "deviceIDs");
        boolean initDevCfgList = HttpRequestHelper.getBooleanParameter(request, "initDevCfgList", false);
        try {
            List<String> deviceIDList = null;
            if (deviceIDs != null) {
                deviceIDList = new ArrayList<String>(Arrays.asList(deviceIDs.split(",")));
            }
            List<Device> devices = deviceBiz.queryDevicesByIDs(deviceIDList, initDevCfgList);
            XMLResponse.outputXML(response, new DeviceList(devices).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    private void initDevSelPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        Integer assignType = HttpRequestHelper.getIntParameter(request, "assignType", 0);
        boolean disUsingFlag = HttpRequestHelper.getBooleanParameter(request, "disUsingFlag", true);
        List<DeviceClass> assignDeviceClasses = deviceClassCtrl.loadAssignClass(deviceTypeCode, areaCode, assignType,
                user);
        if (disUsingFlag) {
            disUsingFlag = assignDeviceClasses == null || assignDeviceClasses.size() == 0 ? false : true;
        }
        request.setAttribute("isAdmin", disUsingFlag);
        request.getRequestDispatcher("/page/device/manage/select_dev.jsp").forward(
                request, response);
    }

    private void initSingeDevSelPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.getRequestDispatcher("/page/device/manage/select_sindev.jsp")
                .forward(request, response);
    }

    protected void initDevPurchaseForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");// 所属地区
        DevPurchaseForm applyForm = new DevPurchaseForm();
        applyForm.setRegAccountID(user.getAccountID());
        applyForm.setRegTime(new Date());
        applyForm.setApplicant(user.getAccountID());
        applyForm.setApplyGroupName(user.getGroupNames());
        applyForm.setApplyDate(new Date());
        applyForm.setDeviceType(deviceTypeCode);
        applyForm.setAreaCode(areaCode);
        if (deviceID != null) {
            DevPurchaseList devPurchaseList = new DevPurchaseList();
            Device device = deviceBiz.getDeviceById(deviceID);
            devPurchaseList.setDevice(device);
            applyForm.getDevPurchaseLists().add(devPurchaseList);
            applyForm.setDeviceType(device.getDeviceType());
        }
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
        request.setAttribute("applyForm", applyForm);
        request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
        request.getRequestDispatcher("/page/device/manage/reci_dev.jsp").forward(
                request, response);
    }

    protected void initDevAllocateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");
        Date now = new Date();
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        DevAllocateForm applyForm = new DevAllocateForm();
        applyForm.setRegAccountID(user.getAccountID());
        applyForm.setRegTime(new Date());
        applyForm.setApplyDate(now);
        applyForm.setInAccountID(user.getAccountID());
        applyForm.setInGroupName(user.getGroupNames());
        applyForm.setDeviceType(deviceTypeCode);
        applyForm.setMoveDate(now);
        if (deviceID != null) {
            DevAllocateList list = new DevAllocateList();
            Device device = deviceBiz.getDeviceById(deviceID);
            list.setDevice(device);
            list.setAreaCodeBef(device.getAreaCode());
            list.setPurposeBef(device.getDeviceCurStatusInfo().getPurpose());
            applyForm.setDevAllocateDevice(list);
            applyForm.setDeviceType(device.getDeviceType());
            applyForm.setDeviceClass(device.getDeviceClass().getId());
            applyForm.setApplicant(device.getDeviceCurStatusInfo().getOwner());
            applyForm.setApplyGroupName(device.getDeviceCurStatusInfo().getGroupName());
            applyForm.setAreaName(device.getDeviceCurStatusInfo().getAreaName());
        }
        request.setAttribute("applyForm", applyForm);
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
        request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
        request.getRequestDispatcher("/page/device/manage/allo_dev.jsp").forward(
                request, response);
    }

    protected void addDevApplyForm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        Date now = new Date();
        DevPurchaseForm applyForm = new DevPurchaseForm();
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");// 所属地区
        String regAccountID = user.getAccountID();// 登记人
        String applicant = HttpRequestHelper.getParameter(request, "applicant");// 申请人
        String applyGroupName = HttpRequestHelper.getParameter(request, "applyGroupName");// 申请人所属部门
        String remark = HttpRequestHelper.getParameter(request, "remark");
        Double budgetMoney = HttpRequestHelper.getDoubleParameter(request, "budgetMoney", 0d);// 预算金额
        String buyType = HttpRequestHelper.getParameter(request, "buyType");// 购买方式

        int iApplyType = HttpRequestHelper.getIntParameter(request, "applyType", 0);// 0:领用;1:申购
        int iFormStatus = HttpRequestHelper.getIntParameter(request, "formStatus", 0);// 表单状态
        boolean bPassed = HttpRequestHelper.getBooleanParameter(request, "passed", false);// 是否审批通过
        String devPurchaseList = HttpRequestHelper.getParameter(request, "devPurchaseList");// 设备列表
        boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);
        try {
            applyForm.setDeviceType(deviceTypeCode);
            applyForm.setAreaCode(areaCode);
            applyForm.setRegAccountID(regAccountID);
            applyForm.setRegTime(now);// 登记时间
            applyForm.setApplicant(applicant);
            applyForm.setApplyGroupName(applyGroupName);
            applyForm.setApplyDate(now);// 申请时间
            applyForm.setRemark(remark);
            applyForm.setBuyType(buyType);
            applyForm.setBudgetMoney(budgetMoney);
            applyForm.setApplyType(iApplyType);
            applyForm.setFormStatus(iFormStatus);
            applyForm.setPassed(bPassed);
            applyForm.setArchiveDate(now);// 归档时间
            Set<DevPurchaseList> devPurchaseListResult = DeviceList
                    .parseDevPurchaseListJSON(devPurchaseList, applyForm);
            applyForm.setDevPurchaseLists(devPurchaseListResult);
            applyForm = deviceApplyBiz.txAddDeviceApplyForm(applyForm, validMainDevFlag);// 领用
            XMLResponse.outputStandardResponse(response, 1, "保存成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }
    }

    private void initAddMaintain(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String deviceID = HttpRequestHelper.getParameter(request, "deviceID");
		DevRepairForm devRepairForm = new DevRepairForm();
		devRepairForm.setCreateTime(new Date());
		devRepairForm.setRegAccountID(user.getAccountID());//登记人
		devRepairForm.setAccountID(user.getAccountID());//申请人
		devRepairForm.setGroupName(user.getGroupNames());
		devRepairForm.setApplyTime(new Date());
		devRepairForm.setDeviceTypeCode(deviceTypeCode);//通过设置设备类型代码可以获取到设备类型的显示名称
		
		if (deviceID != null) {
			Device device = deviceBiz.getDeviceById(deviceID);
			
			devRepairForm.setDevice(device);
			if(device.getDeviceCurStatusInfo().getOwnerName()!=null && device.getDeviceCurStatusInfo().getOwnerName()!=""){
				devRepairForm.setAccountID(device.getDeviceCurStatusInfo().getOwnerName());
			}else{
				devRepairForm.setAccountID(user.getAccountID());
			}
		}
		//设备名称
		//设备编号
		request.setAttribute("devRepairForm", devRepairForm);
		request.getRequestDispatcher("/page/device/manage/repa_dev.jsp").forward(request, response);
	}
    
    protected void saveDeviceMaintain(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 设备参数
        String deviceId = HttpRequestHelper.getParameter(request, "deviceId");// 设备id
        String userId = HttpRequestHelper.getParameter(request, "userId");
        double budgetMoney = HttpRequestHelper.getDoubleParameter(request, "budgetMoney", 0.0); // 扣款金额
        String applyTimeStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "applyTime"));
        String remark = HttpRequestHelper.getParameter(request, "remark");// 设备id
        String groupName = HttpRequestHelper.getParameter(request, "groupName");
        if (deviceId == null || deviceId.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        Device device = new Device();
        device.setId(deviceId);
        DevRepairForm devRepairForm = new DevRepairForm();
        devRepairForm.setAccountID(userId);
        devRepairForm.setCreateTime(new Date());
        devRepairForm.setApplyTime(Timestamp.valueOf(applyTimeStr));
        devRepairForm.setBudgetMoney(budgetMoney);
        devRepairForm.setRegAccountID(user.getAccountID());
        devRepairForm.setDevice(device);
        devRepairForm.setRemark(remark);
        devRepairForm.setStatus(1);
        devRepairForm.setGroupName(groupName);
        try {
            devRepairFormBiz.txAddDevRepairForm(devRepairForm);
            XMLResponse.outputStandardResponse(response, 1, "操作成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            XMLResponse.outputStandardResponse(response, 0, "操作失败");
            e.printStackTrace();
        }
    }

    protected void initDevDiscardForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        int formType = HttpRequestHelper.getIntParameter(request, "formType", 0);// 0:设备报废单；1：离职处理单
        DevDiscardForm applyForm = new DevDiscardForm();
        applyForm.setRegAccountID(user.getAccountID());
        applyForm.setRegTime(new Date());
        applyForm.setApplicant(user.getAccountID());
        applyForm.setApplyGroupName(user.getGroupNames());
        applyForm.setApplyDate(new Date());
        applyForm.setDeviceType(deviceTypeCode);
        applyForm.setFormType(formType);
        if (deviceID != null) {
            DiscardDevList list = new DiscardDevList();
            Device device = deviceBiz.getDeviceById(deviceID);
            list.setDevice(device);
            applyForm.setDiscardDevice(list);
            applyForm.setDeviceType(device.getDeviceType());
            applyForm.setDeviceClass(device.getDeviceClass().getId());
            if (device.getDeviceCurStatusInfo().getOwnerName() != null
                    && device.getDeviceCurStatusInfo().getOwnerName() != "") {
                applyForm.setApplicant(device.getDeviceCurStatusInfo().getOwnerName());
            } else {
                applyForm.setApplicant(user.getAccountID());
            }

        }
        request.setAttribute("applyForm", applyForm);
        if (formType == 0) {
            request.getRequestDispatcher("/page/device/manage/scrap_dev.jsp")
                    .forward(request, response);
        } else if (formType == 1) {
            request.getRequestDispatcher("/page/device/manage/scarp_leave_dev.jsp")
                    .forward(request, response);
        }
    }

    protected void saveDeviceScrap(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 设备参数
        String deviceId = HttpRequestHelper.getParameter(request, "deviceId");// 设备id
        String userId = HttpRequestHelper.getParameter(request, "userId");
        String applyTimeStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "applyTime")); // 申请日期
        String remark = HttpRequestHelper.getParameter(request, "remark");// 设备备注
        String scrapDisposeType = HttpRequestHelper.getParameter(request, "scrapDisposeType");// 设备处理方式
        String scrapType = HttpRequestHelper.getParameter(request, "scrapType");// 设备报废类型
        String discardDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "discardDate"));// 报废日期
        String groupName = HttpRequestHelper.getParameter(request, "groupName");
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        double depreciation = HttpRequestHelper.getDoubleParameter(request, "depreciation", 0.0d);
        double remaining = HttpRequestHelper.getDoubleParameter(request, "remaining", 0.0d);
        String enterCompanyDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request,
                "enterCompanyDate"));// 入司时间
        double workYear = HttpRequestHelper.getDoubleParameter(request, "workYear", 0.0d); // 工龄
        double buyPrice = HttpRequestHelper.getDoubleParameter(request, "buyPrice", 0.0d);
        if (deviceId == null || deviceId.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        int formType = HttpRequestHelper.getIntParameter(request, "formType", 0);// 报废单类别
        Device device = new Device();
        device.setId(deviceId);
        DevDiscardForm discardForm = new DevDiscardForm();
        discardForm.setApplicant(userId);
        discardForm.setDeviceType(deviceType);
        discardForm.setApplyGroupName(groupName);
        discardForm.setFormStatus(DevDiscardForm.FORMSTATUS_PUBLISH);
        discardForm.setRegTime(new Date());
        discardForm.setArchiveDate(new Date());
        discardForm.setRegAccountID(userId);
        discardForm.setApplyDate(Timestamp.valueOf(applyTimeStr));
        discardForm.setEnterCompanyDate(enterCompanyDateStr == null ? null : Timestamp.valueOf(enterCompanyDateStr));// 入司时间
        discardForm.setWorkYear(workYear);// 员工工龄
        Set<DiscardDevList> discardDevLists = new HashSet<DiscardDevList>(0);
        DiscardDevList devList = new DiscardDevList();
        if (scrapDisposeType != null && scrapDisposeType.equals(DevDiscardForm.LEAVE_DEAL_TYPE_BACKBUY)) {
            devList.setBuyFlag(true);
        }
        devList.setDevDiscardForm(discardForm);
        devList.setDevice(device);
        devList.setDiscardType(scrapType);
        devList.setDiscardDate(discardDateStr == null ? null : Timestamp.valueOf(discardDateStr));
        devList.setDealType(scrapDisposeType);
        devList.setDepreciation(depreciation);
        devList.setRemaining(remaining);
        devList.setBuyPrice(buyPrice);
        devList.setReason(remark);
        discardDevLists.add(devList);
        discardForm.setDiscardDevLists(discardDevLists);
        discardForm.setDealType(scrapDisposeType);
        try {
            deviceDiscardBiz.txSaveDevDiscardForm(discardForm, formType, deviceId);
            //结束在审批过程中的设备流程：报废、领用、调拨、申购、离职处理

            XMLResponse.outputStandardResponse(response, 1, "操作成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            XMLResponse.outputStandardResponse(response, 0, "操作失败");
            e.printStackTrace();
        }
    }

    private void initScrapDeal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
			request.getRequestDispatcher("/page/error.jsp").forward(request, response);
			return;
		}		
		Date now = new Date();
		String deviceID = HttpRequestHelper.getParameter(request, "deviceID");
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		DevDiscardDealForm form = new DevDiscardDealForm();
		form.setRegAccountID(user.getAccountID());
		form.setRegTime(now);
		form.setDeviceType(deviceTypeCode);
		form.setSaleDate(now);
		if (deviceID != null) {
			DiscardDealDevList discardDealDevList = new DiscardDealDevList();
			Device device = deviceBiz.getDeviceById(deviceID);
			discardDealDevList.setDevice(device);
			form.getDiscardDealDevLists().add(discardDealDevList);
		}
		request.setAttribute("form", form);
		request.getRequestDispatcher("/page/device/manage/scrap_desp_dev.jsp").forward(request, response);
	}
    
    public void addDiscardDispose(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount)request.getSession()
		.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
		String regAccountID = user.getAccountID();
		String salePrice = HttpRequestHelper.getParameter(request, "salePrice");
		String saleDate = HttpRequestHelper.getParameter(request, "saleDate");
		String devDiscardDealList = HttpRequestHelper.getParameter(request, "devDiscardDealList");
		try {
			Date now = new Date();
			DevDiscardDealForm form = new DevDiscardDealForm();
			form.setDeviceType(deviceTypeCode);
			form.setRegTime(now);
			form.setRegAccountID(regAccountID);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			form.setSaleDate(saleDate == null ? null : sdf.parse(saleDate));
			form.setSalePrice(salePrice == null ? null : Double.parseDouble(salePrice));
			Set<DiscardDealDevList> discardDealDevListResult = DeviceList.parseJSON(devDiscardDealList, form);
			form.setDiscardDealDevLists(discardDealDevListResult);
			form = devDiscardDisposeBiz.txSaveForm(form);
			XMLResponse.outputStandardResponse(response, 1, "保存成功");
		} catch (OaException e) {
			XMLResponse.outputStandardResponse(response, 0, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(response, 0, "系统错误");
		}
	}
    
    protected void saveDeviceAllocation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 设备参数
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");// 设备类型
        String deviceId = HttpRequestHelper.getParameter(request, "deviceId");// 设备id
        String moveType = HttpRequestHelper.getParameter(request, "allotType"); // 调拨类型
        String inAccountID = HttpRequestHelper.getParameter(request, "inAccountID");//
        String inGroupName = HttpRequestHelper.getParameter(request, "inGroupName");
        String workAreaCode = HttpRequestHelper.getParameter(request, "workAreaCode");
        String workAreaCodeBef = HttpRequestHelper.getParameter(request, "workAreaCodeBef");
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        String moveDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "moveDate")); // 调拨日期
        String remark = HttpRequestHelper.getParameter(request, "remark");// 设备id
        String applicantID = HttpRequestHelper.getParameter(request, "applicantID");//
        String groupName = HttpRequestHelper.getParameter(request, "groupName");
//        String purpose = HttpRequestHelper.getParameter(request, "purpose");
        String purposeBef = HttpRequestHelper.getParameter(request, "purposeBef");
        boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag", true);
        if (deviceId == null || deviceId.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        Device device = new Device();
        device.setId(deviceId);
        DevAllocateForm allocateForm = new DevAllocateForm();
        allocateForm.setDeviceType(deviceTypeCode);
        allocateForm.setApplicant(applicantID);
        allocateForm.setApplyDate(new Date());
        allocateForm.setApplyGroupName(groupName);
        allocateForm.setMoveDate(Timestamp.valueOf(moveDateStr));
        allocateForm.setArchiveDate(new Date());
        allocateForm.setMoveType(moveType);
        allocateForm.setDeviceType(deviceType);
        allocateForm.setInAccountID(inAccountID);
        allocateForm.setInGroupName(inGroupName);
        allocateForm.setReason(remark);
        allocateForm.setRegAccountID(user.getAccountID());
        allocateForm.setRegTime(new Date());
        allocateForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_PUBLISH);
        Set<DevAllocateList> devAllocateLists = new HashSet<DevAllocateList>(0);
        DevAllocateList devList = new DevAllocateList();
        // devList.setBuyFlag(buyFlag);
        devList.setDevAllocateForm(allocateForm);
        devList.setAreaCode(workAreaCode);
        devList.setAreaCodeBef(workAreaCodeBef);
        devList.setDevice(device);
//        devList.setPurpose(purpose);
        devList.setPurposeBef(purposeBef);
        devAllocateLists.add(devList);
        allocateForm.setDevAllocateLists(devAllocateLists);
        try {
            deviceAllocateBiz.txSaveDevAllocateForm(allocateForm, deviceId, validMainDevFlag);
            XMLResponse.outputStandardResponse(response, 1, "操作成功");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            XMLResponse.outputStandardResponse(response, 0, "操作失败");
            e.printStackTrace();
        }
    }

    /**
     * 获取设备领用记录列表
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDevicePurchaseList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        if (deviceID == null || deviceID.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        List<DevPurchaseList> devPurchaseList = deviceApplyBiz.getArchDevUseListByDeviceID(deviceID, true);
        XMLResponse.outputXML(response, new DevPurchaseListDTO(devPurchaseList).toDocument());
    }

    /**
     * 获取设备领用记录
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDevicePurchaseForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String id = HttpRequestHelper.getParameter(request, "id");// 设备id
        if (id == null || id.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        DevPurchaseForm form = deviceApplyBiz.getDevUseApplyFormById(id, true, false);
        request.setAttribute("form", form);
        request.getRequestDispatcher("/page/device/manage/view_dev_reci.jsp")
                .forward(request, response);
    }

    /**
     * 获取设备调拨记录列表
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceAllotList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        if (deviceID == null || deviceID.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        List<DevAllocateList> devAllocateList = deviceAllocateBiz.getArchDevAllotListByDeviceID(deviceID, true);
        XMLResponse.outputXML(response, new DevAllocateListDTO(devAllocateList).toDocument());
    }

    /**
     * 获取设备调拨记录
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceAllotForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String id = HttpRequestHelper.getParameter(request, "id");// 设备id
        if (id == null || id.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        DevAllocateForm form = deviceAllocateBiz.getDevAllocateFormById(id, true, false);
        request.setAttribute("form", form);
        request.getRequestDispatcher("/page/device/manage/view_dev_allo.js.jsp")
                .forward(request, response);
    }

    /**
     * 获取设备维修记录列表
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceRepairList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        if (deviceID == null || deviceID.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        List<DevRepairForm> devRepairList = devRepairFormBiz.findByDeviceID(deviceID, true);
        XMLResponse.outputXML(response, new DevRepairFormListDTO(devRepairList).toDocument());
    }

    /**
     * 获取设备维修记录
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceRepairForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String id = HttpRequestHelper.getParameter(request, "id");// 设备id
        if (id == null || id.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        DevRepairForm form = devRepairFormBiz.getDevRepairFormById(id);
        request.setAttribute("form", form);
        request.getRequestDispatcher("/page/device/manage/view_dev_repa.jsp")
                .forward(request, response);
    }

    /**
     * 获取设备报废记录列表
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceScrapList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        if (deviceID == null || deviceID.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        List<DiscardDevList> discardDevList = deviceDiscardBiz.getArchDevScrapListByDeviceID(deviceID, true);
        XMLResponse.outputXML(response, new DiscardDevListDTO(discardDevList).toDocument());
    }

    /**
     * 获取设备报废记录
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceScrapForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String id = HttpRequestHelper.getParameter(request, "id");// 设备id
        if (id == null || id.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        DevDiscardForm form = deviceDiscardBiz.getDevDiscardFormById(id, true);
        request.setAttribute("form", form);
        request.getRequestDispatcher("/page/device/manage/view_dev_scrap.jsp")
                .forward(request, response);
    }
    
    /**
	 * 获取设备报废处理记录
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getDeviceScrapDisposeForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
			return;
		}
		
		String id = HttpRequestHelper.getParameter(request,"id");//设备id
		if (id == null || id.length()<=0) {
			XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
			return;
		}
		DevDiscardDealForm form = devDiscardDisposeBiz.getById(id);
		request.setAttribute("form", form);
		request.getRequestDispatcher("/page/device/manage/view_dev_scap_desp.jsp").forward(request, response);
	}

    /**
     * 获取设备操作流程记录列表
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceFlowLogList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        List<DevFlowProcessFormDTO> devFlowProcessFormDTOs = devFlowBiz.queryAllDevFlowList(deviceID);
        XMLResponse.outputXML(response, new DevFlowProcessListDTO(devFlowProcessFormDTOs).toDocument());
    }

    /**
     * 获取设备更新记录列表
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void getDeviceUptLogList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }

        String deviceID = HttpRequestHelper.getParameter(request, "deviceID");// 设备id
        if (deviceID == null || deviceID.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        List<DeviceUpdateLog> deviceUpdateLogs = deviceUpdateLogBiz.queryByDeviceID(deviceID, true);
        XMLResponse.outputXML(response, new DeviceUpdateLogListDTO(deviceUpdateLogs).toDocument());
    }

//    /**
//     * 获取设备用途
//     * 
//     * @param request
//     * @param response
//     * @throws ServletException
//     * @throws IOException
//     */
//    private void getAreaDevUseTypeSel(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
//            String deviceClassID = HttpRequestHelper.getParameter(request, "deviceClassID");
//            AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(areaCode, deviceClassID);
//            XMLResponse.outputXML(response,
//                    new DevicePurposeSelect(areaDeviceCfg == null ? null : areaDeviceCfg.getDeviceAcptCountCfgs())
//                            .toDocument());
//        } catch (Exception e) {
//            e.printStackTrace();
//            XMLResponse.outputStandardResponse(response, 0, "系统出错");
//            return;
//        }
//    }

    /**
     * 获取设备用途
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
//    private void getAreaDevUseTypeDivSel(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
//            String deviceClassID = HttpRequestHelper.getParameter(request, "deviceClassID");
//            AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(areaCode, deviceClassID);
//            HTMLResponse.outputHTML(response,
//                    new DevicePurposeSelect(areaDeviceCfg == null ? null : areaDeviceCfg.getDeviceAcptCountCfgs())
//                            .toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//            XMLResponse.outputStandardResponse(response, 0, "系统出错");
//            return;
//        }
//    }

    protected void getStatusSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            HTMLResponse.outputHTML(response, new StatusMapSelect(DeviceCurStatusInfo.statusSelMap).toString());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    protected void getStatusList(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            XMLResponse.outputXML(response, new StatusMapSelect(DeviceCurStatusInfo.statusSelMap).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    private void loadLeadDevicePage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
        String deviceTypeCode = HttpRequestHelper.getParameter(request, "deviceTypeCode");
        String deviceClassCode = HttpRequestHelper.getParameter(request, "deviceClassCode");
        String deviceName = HttpRequestHelper.getParameter(request, "deviceName");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        try {
            DeviceQueryParameters qp = new DeviceQueryParameters();
            if (deviceTypeCode != null) {
                qp.setDeviceType(deviceTypeCode);
            }
            if (deviceClassCode != null) {
                qp.setDeviceClass(deviceClassCode);
            }
            if (deviceName != null) {
                qp.setDeviceName(deviceName);
            }
            if (areaCode != null) {
                qp.setAreaCode(areaCode);
            }
            qp.setPageNo(pageNo);
            qp.setPageSize(pageSize);
            qp.addOrder("deviceNO", true);

            ListPage<Device> devices = deviceBiz.getDeviceListPage(qp);
            XMLResponse.outputXML(response, new DevicePage(devices).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    protected void getStatusMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        boolean purposeFlag = HttpRequestHelper.getBooleanParameter(request, "purposeFlag", false);
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        String deviceClass = HttpRequestHelper.getParameter(request, "deviceClass");
//        String puerpose = HttpRequestHelper.getParameter(request, "puerpose");
//        String puerposeName = HttpRequestHelper.getParameter(request, "puerposeName");
        String userId = HttpRequestHelper.getParameter(request, "userId");
        // Boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag",false);
        if (id == null || id.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        Device device = deviceBiz.getDeviceById(id);
        Set<DevAllocateList> devAllocateLists = new HashSet<DevAllocateList>(0);
        DevAllocateList devList = new DevAllocateList();
        devList.setDevice(device);
        devAllocateLists.add(devList);
        try {

            String msg = "";
            if (device != null && device.getDeviceCurStatusInfo() != null
                    && device.getDeviceCurStatusInfo().getApproveType() != null) {
                if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_PURCH) {
                    msg = "申购审批流程中";
                } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_USE) {
                    msg = "领用审批流程中";
                } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_ALLOT) {
                    msg = "调拨审批流程中";
                } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_SCRAP) {
                    msg = "报废审批流程中";
                } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_LEAVE) {
                    msg = "离职审批流程中";
                }
            }
            if (purposeFlag) {
                // 设备用途是否在流程领用、申购中
//                boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(areaCode, deviceClass, puerpose);
//                if (manyTimeFlag) {
                    List<PurchaseDevice> list = deviceApplyBiz.getDeviceDealPurchaseByPurpose(deviceType, areaCode,
                            deviceClass, userId);
                    if (list != null && list.size() > 0) {
                        msg = "设备类别名称为【" + device.getDeviceTypeName() + "】在申购流程审批中";
                    }

                    List<DevPurchaseList> useList = deviceApplyBiz.getDeviceDealUseByPurpose(deviceType, areaCode,
                            deviceClass, userId);
                    if (useList != null && useList.size() > 0) {
                        msg = "设备类别名称为【" + device.getDeviceTypeName() + "】在领用流程审批中";
                    }
                }

//            }

            XMLResponse.outputStandardResponse(response, 1, msg);
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    private void getManyStatusMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String deviceIDs = HttpRequestHelper.getParameter(request, "deviceIDs");
        boolean purposeFlag = HttpRequestHelper.getBooleanParameter(request, "purposeFlag", false);
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
        String userId = HttpRequestHelper.getParameter(request, "userId");
        String strDevPurchaseList = HttpRequestHelper.getParameter(request, "devPurchaseList");// 领用设备列表
        // Boolean validMainDevFlag = HttpRequestHelper.getBooleanParameter(request, "validMainDevFlag",false);
        if (deviceIDs == null || deviceIDs.length() <= 0) {
            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
            return;
        }
        List<String> deviceIDList = null;
        if (deviceIDs != null) {
            deviceIDList = new ArrayList<String>(Arrays.asList(deviceIDs.split(",")));
        }
        Set<DevAllocateList> devAllocateLists = new HashSet<DevAllocateList>(0);
        if (deviceIDList != null) {
            for (String string : deviceIDList) {
                Device device = deviceBiz.getDeviceById(string);
                DevAllocateList devList = new DevAllocateList();
                devList.setDevice(device);
                devAllocateLists.add(devList);
            }
        }
        try {

            List<Device> devices = deviceBiz.queryDevicesByIDs(deviceIDList, false);
            String msg = "";
            // 设备处于流程审批中
            if (devices != null) {
                for (Device device : devices) {
                    if (device != null && device.getDeviceCurStatusInfo() != null
                            && device.getDeviceCurStatusInfo().getApproveType() != null) {
                        if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_PURCH) {
                            msg += "设备名称为【" + device.getDeviceName() + "】目前正在申购审批流程中；\r";
                        } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_USE) {
                            msg += "设备名称为【" + device.getDeviceName() + "】目前正在领用审批流程中；\r";
                        } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_ALLOT) {
                            msg += "设备名称为【" + device.getDeviceName() + "】目前正在调拨审批流程中；\r";
                        } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_SCRAP) {
                            msg += "设备名称为【" + device.getDeviceName() + "】目前正在报废审批流程中；\r";
                        } else if (device.getDeviceCurStatusInfo().getApproveType() == DeviceCurStatusInfo.APPROVE_TYPE_LEAVE) {
                            msg += "设备名称为【" + device.getDeviceName() + "】目前正在离职审批流程中；\r";
                        }
                    }
                }
            }
            if (purposeFlag) {
                // 设备用途是否在流程领用、申购中
                Set<DevPurchaseList> devUseListResult = DeviceList.parsePurchaseListJSON(strDevPurchaseList);
                for (DevPurchaseList devPurchaseList : devUseListResult) {
//                    boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(areaCode, devPurchaseList
//                            .getDevice().getDeviceClass().getId(), devPurchaseList.getPurpose());
//                    if (!manyTimeFlag) {
                    if (devPurchaseList.getDevice() != null && devPurchaseList.getDevice().getDeviceClass() != null) {
                        List<PurchaseDevice> list = deviceApplyBiz.getDeviceDealPurchaseByPurpose(deviceType,
                                areaCode, devPurchaseList.getDevice().getDeviceClass().getId(), userId);
                        if (list != null && list.size() > 0) {
                            msg = "设备类别名称为【" + devPurchaseList.getDevice().getDeviceTypeName() + "】在申购流程审批中；\r";
                        }

                        List<DevPurchaseList> useList = deviceApplyBiz.getDeviceDealUseByPurpose(deviceType,
                                areaCode, devPurchaseList.getDevice().getDeviceClass().getId(), userId);
                        if (useList != null && useList.size() > 0) {
                            msg += "设备类别名称为【" + devPurchaseList.getDevice().getDeviceTypeName() + "】在领用流程审批中；\r";
                        }
                    }

//                    }
                }

            }
            if (msg != "") {
                msg = msg.substring(0, msg.length() - 1);
            }
            XMLResponse.outputStandardResponse(response, 1, msg);
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 获取设备用途
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
//    private void getAreaDevPuerposes(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
//        if (user == null) {
//            XMLResponse.outputStandardResponse(response, 0, "请先登录");
//            return;
//        }
//        try {
//            String areaCode = HttpRequestHelper.getParameter(request, "areaCode");
//            String flag = HttpRequestHelper.getParameter(request, "flag");
//            String deviceClassID = HttpRequestHelper.getParameter(request, "deviceClassID");
//            AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(areaCode, deviceClassID);
//            if (areaDeviceCfg != null && areaDeviceCfg.getDeviceAcptCountCfgs() != null) {
//                if (areaDeviceCfg != null && areaDeviceCfg.getDeviceAcptCountCfgs() != null) {
//                    for (DeviceAcptCountCfg deviceAcptCountCfg : areaDeviceCfg.getDeviceAcptCountCfgs()) {
//                        int count = 0;
//                        if (flag != null && flag != "") {
//                            areaCode = null;
//                        }
//                        Map<String, Integer> countMap = deviceBiz.getDeviceUseCount(areaDeviceCfg.getDeviceClass()
//                                .getDeviceType(), areaCode, deviceClassID, deviceAcptCountCfg.getDevPurpose(), user
//                                .getAccountID(), true);
//                        for (String key : countMap.keySet()) {
//                            if (key != null) {
//                                count += countMap.get(key);
//                            }
//                        }
//                        deviceAcptCountCfg.setUseCount(count);
//                    }
//                }
//            }
//            XMLResponse.outputXML(response,
//                    new DevicePurposeSelect(areaDeviceCfg == null ? null : areaDeviceCfg.getDeviceAcptCountCfgs())
//                            .toDocument());
//        } catch (Exception e) {
//            e.printStackTrace();
//            XMLResponse.outputStandardResponse(response, 0, "系统出错");
//            return;
//        }
//    }
}
