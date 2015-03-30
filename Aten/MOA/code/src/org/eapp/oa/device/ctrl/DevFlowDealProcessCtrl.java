package org.eapp.oa.device.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.device.blo.IDevFlowBiz;
import org.eapp.oa.device.blo.IDevValidateFormBiz;
import org.eapp.oa.device.blo.IDeviceAllocateBiz;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceCheckItemBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.dto.DeviceFlowPage;
import org.eapp.oa.device.dto.DeviceFlowQueryParameters;
import org.eapp.oa.device.dto.DeviceList;
import org.eapp.oa.device.dto.StatusMapSelect;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCheckItem;
import org.eapp.oa.device.hbean.DeviceFlowView;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.ctrl.TaskDealCtrl;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.web.HTMLResponse;
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
public class DevFlowDealProcessCtrl extends HttpServlet {

    private static final long serialVersionUID = -1967167347504172287L;
    private IDevFlowBiz devFlowBiz;
    private TaskDealCtrl taskDealCtrl;
    private IDeviceDiscardBiz deviceDiscardBiz;
    private IDeviceApplyBiz deviceApplyBiz;
    private ITaskBiz taskBiz;
    private IDeviceCheckItemBiz deviceCheckItemBiz;
    private IDeviceAllocateBiz deviceAllocateBiz;
    private IDeviceAreaConfigBiz deviceAreaConfigBiz;
    private IDeviceBiz deviceBiz;
    private IDevValidateFormBiz devValidateFormBiz;

    public DevFlowDealProcessCtrl() {
        super();
    }

    public void init() throws ServletException {
        devFlowBiz = (IDevFlowBiz) SpringHelper.getSpringContext().getBean("devFlowBiz");
        deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getSpringContext().getBean("deviceDiscardBiz");
        taskBiz = (ITaskBiz) SpringHelper.getSpringContext().getBean("taskBiz");
        deviceApplyBiz = (IDeviceApplyBiz) SpringHelper.getSpringContext().getBean("deviceApplyBiz");
        deviceCheckItemBiz = (IDeviceCheckItemBiz) SpringHelper.getSpringContext().getBean("deviceCheckItemBiz");
        deviceAllocateBiz = (IDeviceAllocateBiz) SpringHelper.getSpringContext().getBean("deviceAllocateBiz");
        deviceAreaConfigBiz = (IDeviceAreaConfigBiz) SpringHelper.getSpringContext().getBean("deviceAreaConfigBiz");
        deviceBiz = (IDeviceBiz) SpringHelper.getSpringContext().getBean("deviceBiz");
        devValidateFormBiz = (IDevValidateFormBiz) SpringHelper.getSpringContext().getBean("devValidateFormBiz");
        taskDealCtrl = new TaskDealCtrl();
        taskDealCtrl.init();
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("act");
        if (SysConstants.QUERY.equalsIgnoreCase(act)) {
            // 查询我申请的设备
            queryDealDevicePage(request, response);
            return;
        } else if ("getformtypesel".equals(act)) {
            // 状态下拉框
            getFormTypeSelect(request, response);
            return;
        } else if (SysConstants.DISPOSE.equalsIgnoreCase(act)) {
            // 转到任务绑定的URL,并认领
            turnViewPage(request, response);
        } else if ("deal_approve".equalsIgnoreCase(act)) {
            // 处理审批任务
            dealTaskApprove(request, response);
            return;
        } else if ("reject_deal_approve".equalsIgnoreCase(act)) {
            // 驳回修改重新提交
            dealRejectTaskApprove(request, response);
            return;
        } else if ("view_approve".equalsIgnoreCase(act)) {
            // 任务处理页面——审批
            initDeviceFlowApprove(request, response);
        } else if ("deal_finance".equalsIgnoreCase(act)) {
            // 财务审核
            dealFinanceTaskApprove(request, response);
        } else if ("deal_hr".equalsIgnoreCase(act)) {
            // HR复核
            dealHRTaskApprove(request, response);
        } else if ("deal_backbuy".equalsIgnoreCase(act)) {
            // 设备回购
            dealBackBuyTaskApprove(request, response);
        } else if ("deal_ceo".equalsIgnoreCase(act)) {
            // 总裁审核
            dealCEOTaskApprove(request, response);
        } else if ("deal_procure".equals(act)) {
            // 设备申购采购
            dealProcurePurchaseTaskApprove(request, response);
        } else if ("initvalidate".equals(act)) {
            // 设备申购采购
            initPurchaseValidate(request, response);
        } else if ("deal_accept".equals(act)) {
            // 设备验收处理
            dealAcceptTaskApprove(request, response);
        } else if ("toviewvaliform".equals(act)) {
            // 查看设备验收单
            toViewValiForm(request, response);
        } else if ("deal_purchaseuse".equals(act)) {
            // 申购设备领用确认
            dealPurchaseUseTaskApprove(request, response);
        } else if ("deal_incheckapprove".equals(act)) {
            // 调入者审核
            dealAllotInCheckTaskApprove(request, response);
        } else if ("deal_backbuyconfirm".equalsIgnoreCase(act)) {
            // 离职回购确认
            dealBackBuyConfirmTaskApprove(request, response);
        } else if ("dispose_purchase".equalsIgnoreCase(act)) {
            // 离职回购确认
            disposePurchase(request, response);
        } else if ("deal_comp".equalsIgnoreCase(act)) {
            // 离职处理综合管理审批
            dealCompTaskApprove(request, response);
        } else if ("deal_purchasecomp".equalsIgnoreCase(act)) {
            // 申购综合管理部填写是否扣款
            dealPurchaseCompTaskApprove(request, response);
        } else if ("deal_payable".equalsIgnoreCase(act)) {
            // 申购财务部填写回购到款日期
            dealPayableTaskApprove(request, response);
        } else if ("deal_scrap_task_by_backbuyflag".equals(act)) {
            dealScrapTaskApproveByBuyFlag(request, response);
//        } else if ("print_purchase_form".equals(act)) {
//            initPrintPurchaseForm(request, response);
//        } else if ("print_dev_valid_form".equals(act)) {
//            initPrintDevValidForm(request, response);
//        } else if ("print_request_fund_form".equals(act)) {
//            initPrintRequestFundForm(request, response);
//        } else if ("print_buy_back_form".equals(act)) {
//            initPrintBuyBackForm(request, response);
        }
    }

    private void queryDealDevicePage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);

        DeviceFlowQueryParameters qp = new DeviceFlowQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        String applicant = HttpRequestHelper.getParameter(request, "applicant");
        String formNO = HttpRequestHelper.getParameter(request, "formNO");
        String startApplyTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "startApplyTime"));
        String endApplyTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endApplyTime"));
        String formType = HttpRequestHelper.getParameter(request, "formType");
        String deviceType = HttpRequestHelper.getParameter(request, "deviceType");
        if (deviceType != null && deviceType.length() > 0) {
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
        try {
            ListPage<DeviceFlowView> page = devFlowBiz.queryDealDeviceFlowPage(qp, user.getAccountID());
            XMLResponse.outputXML(response, new DeviceFlowPage(page).toDocument());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    protected void getFormTypeSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            HTMLResponse.outputHTML(response, new StatusMapSelect(DeviceFlowView.formTypeMap).toString());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    public void turnViewPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        taskDealCtrl.turnToViewPage(request, response);
    }

    public void dealTaskApprove(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        taskDealCtrl.dealApprove(request, response);
    }

    public void dealRejectTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        DevPurchaseForm form = deviceApplyBiz.getDevUseApplyFormById(id, false, true);
        if (form == null) {
            throw new IllegalArgumentException("设备申购单不存在");
        }
        try {
            if (form.getApplyType() == DevPurchaseForm.APPLY_TYPE_PURCHASE) {
                devFlowBiz.txDealRejectTaskByFlowInstanceId(form.getFlowInstanceID(), form.getBuyType(),
                        taskInstanceId, comment, transitionName);
                XMLResponse.outputStandardResponse(response, 1, "提交成功");
            } else {
                taskDealCtrl.dealApprove(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    public void dealFinanceTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        DevDiscardForm form = deviceDiscardBiz.getDevDiscardFormById(id, true);
        try {
            Set<DiscardDevList> discardDevLists = DeviceList.parseJSON(aDevList, form);
            if (discardDevLists != null) {
                form.setDiscardDevLists(discardDevLists);
                deviceDiscardBiz.txUpdateDiscardDev(form, false);
                if (form == null) {
                    throw new IllegalArgumentException("设备报废单不存在");
                }
                /*
                 * String backBuyFlag="2"; for (DiscardDevList devList : form.getDiscardDevLists()) {
                 * if(DevDiscardForm.DEAL_TYPE_TAKE.equals(devList.getDealType())){ backBuyFlag="1"; break; }
                 * if(DevDiscardForm.DEAL_TYPE_LOSE.equals(devList.getDealType())){ backBuyFlag="0"; }
                 * 
                 * }
                 */
                String backBuyFlag = "1";
                for (DiscardDevList devList : form.getDiscardDevLists()) {
                    if (DevDiscardForm.DEAL_TYPE_MOVEBACK.equals(devList.getDealType())) {
                        backBuyFlag = "0"; // 流程调整，退库为0，其它为1
                        break;
                    }
                }
                devFlowBiz.txDealApproveTaskByFlowInstanceId(form.getFlowInstanceID(), backBuyFlag, 1, taskInstanceId,
                        comment, transitionName);
                XMLResponse.outputStandardResponse(response, 1, "提交成功");
            } else {
                taskDealCtrl.dealApprove(request, response);
            }

        } catch (OaException e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    public void dealBackBuyTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        DevDiscardForm form = deviceDiscardBiz.getDevDiscardFormById(id, true);
        try {
            Set<DiscardDevList> discardDevLists = DeviceList.parseJSON(aDevList, form);
            if (discardDevLists != null) {
                form.setDiscardDevLists(discardDevLists);
                deviceDiscardBiz.txUpdateDiscardDev(form, false);
                if (form == null) {
                    throw new IllegalArgumentException("设备报废单不存在");
                }
                String backBuyFlag = "0";
                for (DiscardDevList devList : form.getDiscardDevLists()) {
                    if (DevDiscardForm.DEAL_TYPE_TAKE.equals(devList.getDealType())) {
                        backBuyFlag = "1"; // 流程调整，回购为1，其它为0
                        break;
                    }
                }
                devFlowBiz.txDealApproveTaskByFlowInstanceId(form.getFlowInstanceID(), backBuyFlag, 1, taskInstanceId,
                        comment, transitionName);
                XMLResponse.outputStandardResponse(response, 1, "提交成功");
            } else {
                taskDealCtrl.dealApprove(request, response);
            }
        } catch (OaException e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    private void initDeviceFlowApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        try {
            String id = HttpRequestHelper.getParameter(request, "id");// 报废单ID
            String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID,从上一个地址转过来的参数
            String formType = HttpRequestHelper.getParameter(request, "formType");// 任务ID,从上一个地址转过来的参数
            String flag = HttpRequestHelper.getParameter(request, "flag");//
            if (id == null || taskInstanceId == null || formType == null) {
                throw new OaException("参数不能为空");
            }
            // 报废
            if (Integer.valueOf(formType) == DeviceFlowView.FORM_TYPE_DISCARD
                    || Integer.valueOf(formType) == DeviceFlowView.FORM_TYPE_LEAVE) {
                DevDiscardForm form = deviceDiscardBiz.getDevDiscardFormById(id, true);
                if (form == null) {
                    if (Integer.valueOf(formType) == DeviceFlowView.FORM_TYPE_DISCARD) {
                        throw new OaException("设备报废单不存在！");
                    } else {
                        throw new OaException("离职处理单不存在！");
                    }
                }
                List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
                request.setAttribute("tasks", tasks);
                List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
                request.setAttribute("transitions", transitions);
                request.setAttribute("form", form);
                if (form.getFormType().intValue() == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue()) {
                    // 正常报废
                    if ("finance".equals(flag)) {
                        // 财务部审核
                        request.getRequestDispatcher("/page/device/scrap/tview_finance.jsp").forward(request, response);
                    } else if ("ceo".equals(flag)) {
                        // 总裁审批
                        request.getRequestDispatcher("/page/device/scrap/tview_ceo.jsp").forward(request, response);
                    } else if ("hr".equals(flag)) {
                        // 人力资源部审核
                        request.getRequestDispatcher("/page/device/scrap/tview_hr.jsp").forward(request, response);
                    } else if ("backbuy".equals(flag)) {
                        // 设备回购
                        request.getRequestDispatcher("/page/device/scrap/tview_backbuy.jsp").forward(request, response);
                    } else if ("useconfirm".equals(flag)) {
                        // 申请人回购确认
                        request.getRequestDispatcher("/page/device/scrap/tview_usercfm.jsp").forward(request, response);
                    } else if ("payable".equals(flag)) {
                        // 财务部到款确认
                        request.getRequestDispatcher("/page/device/scrap/tview_payable.jsp").forward(request, response);
                    } else if ("compconfirm".equals(flag)) {
                        // 综合管理部确认
                        request.getRequestDispatcher("/page/device/scrap/tview_compcfm.jsp").forward(request, response);
                    } else if ("m".equals(flag)) {
                        // 驳回起草人
                        request.getRequestDispatcher("/page/device/scrap/tview_modify.jsp").forward(request, response);
                    } else {
                    	request.getRequestDispatcher("/page/device/scrap/tview_dispose.jsp").forward(request, response);
                    }
                } else {
                    // 离职处理：报废
                    if ("finance".equals(flag)) {
                        // 财务部审核
                        request.getRequestDispatcher("/page/device/leavedeal/tview_finance.jsp").forward(request, response);
                    } else if ("hr".equals(flag)) {
                        // 人力资源部审核
                        request.getRequestDispatcher("/page/device/leavedeal/tview_hr.jsp").forward(request, response);
                    } else if ("ceo".equals(flag)) {
                        // 总裁审批
                        request.getRequestDispatcher("/page/device/leavedeal/tview_ceo.jsp").forward(request, response);
                    } else if ("comp".equals(flag)) {
                        // 综合部
                        request.getRequestDispatcher("/page/device/leavedeal/tview_compcfm.jsp").forward(request, response);
                    } else if ("backbuymoneyconfirm".equals(flag)) {
                        // 设备回购款确认
                        request.getRequestDispatcher("/page/device/leavedeal/tview_payable.jsp").forward(request, response);
                    } else if ("backbuyconfirm".equals(flag)) {
                        // 设备回购确认
                        request.getRequestDispatcher("/page/device/leavedeal/tview_backbuy.jsp").forward(request, response);
                    } else if ("applymanconfirm".equals(flag)) {
                        // 申请人确认
                        request.getRequestDispatcher("/page/device/leavedeal/tview_usercfg.jsp").forward(request, response);
                    } else if ("m".equals(flag)) {
                        // 驳回起草人
                        request.getRequestDispatcher("/page/device/leavedeal/tview_modify.jsp").forward(request, response);
                    } else {
                    	 // 部门经理审核
                        request.getRequestDispatcher("/page/device/leavedeal/tview_dispose.jsp").forward(request, response);
                    }
                }

            } else if (Integer.valueOf(formType) == DeviceFlowView.FORM_TYPE_PERCHASE) {// 申购
                DevPurchaseForm form = deviceApplyBiz.getDevUseApplyFormById(id, true, true);
                if (form == null) {
                    throw new OaException("设备申购单不存在！");
                }
                List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
                request.setAttribute("tasks", tasks);
                List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
                request.setAttribute("transitions", transitions);
                request.setAttribute("form", form);
                if ("procure".equals(flag)) {
                    // 采购
                    for (PurchaseDevice purchaseDevice : form.getPurchaseDevices()) {
                        AreaDeviceCfg deviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(
                                purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass().getId());
                        if (deviceCfg != null) {
                            purchaseDevice.setDeviceClassMainFlag(deviceCfg.getMainDevFlag());
                        }
                    }
                    request.getRequestDispatcher("/page/device/recipients/buy/tview_purchase.jsp").forward(request, response);
                } else if ("accept".equals(flag)) {
                    // 验收
                	initBuyAccept(form);
                    request.getRequestDispatcher("/page/device/recipients/buy/tview_acept.jsp").forward(request,response);
                } else if ("use".equals(flag)) {
                    // 领用确认
                    request.getRequestDispatcher("/page/device/recipients/buy/tview_confirm.jsp").forward(request, response);
                } else if ("comp".equals(flag)) {
                    // 综合管理部审核填写比例购买
                    for (PurchaseDevice purchaseDevice : form.getPurchaseDevices()) {
                        if (purchaseDevice != null) {
                            Device device = deviceBiz.getDeviceById(purchaseDevice.getDeviceID());
                            if (device != null) {
                                purchaseDevice.setDeviceNo(device.getDeviceNO());
                            }
                        }
                    }
                    request.getRequestDispatcher("/page/device/recipients/buy/tview_deduct.jsp").forward(request,response);
                } else if ("m".equals(flag)) {
                    // 驳回起草人修改
                    List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
                    request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
                    request.getRequestDispatcher("/page/device/recipients/tview_modify.jsp").forward(request,response);
                } else {
                    // 只读审批
                    request.getRequestDispatcher("/page/device/recipients/tview_dispose.jsp").forward(request,response);
                }
            } else if (Integer.valueOf(formType) == DeviceFlowView.FORM_TYPE_USE) {// 领用
                DevPurchaseForm form = deviceApplyBiz.getDevUseApplyFormById(id, true, false);
                if (form == null) {
                    throw new OaException("设备领用单不存在！");
                }
                List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
                request.setAttribute("tasks", tasks);
                List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
                request.setAttribute("transitions", transitions);
                request.setAttribute("form", form);
                if ("m".equals(flag)) {
                    // 驳回起草人
                    List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
                    request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
                    request.getRequestDispatcher("/page/device/recipients/tview_modify.jsp").forward(request,response);
                } else {
                	request.getRequestDispatcher("/page/device/recipients/tview_dispose.jsp").forward(request,response);
                }
            } else if (Integer.valueOf(formType) == DeviceFlowView.FORM_TYPE_ALLOCATE) {// 调拨
                DevAllocateForm form = deviceAllocateBiz.getDevAllocateFormById(id, true, true);
                if (form == null) {
                    throw new OaException("设备调拨单不存在！");
                }
                List<Task> tasks = taskBiz.getEndedTasks(form.getFlowInstanceID());
                request.setAttribute("tasks", tasks);
                List<String> transitions = taskBiz.getTaskTransitions(taskInstanceId);
                request.setAttribute("transitions", transitions);
                request.setAttribute("form", form);
                if ("incheck".equals(flag)) {
                    // 调入者审核
                    request.getRequestDispatcher("/page/device/allocation/tview_inuser.jsp").forward(request, response);
                } else if ("inDeptCheck".equals(flag)) {
                    // 调入部门审核，综合部审核
                    request.getRequestDispatcher("/page/device/allocation/tview_indept.jsp").forward(request, response);
                } else if ("m".equals(flag)) {
                    // 驳回起草人修改
                    List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.getMainDevAreaDeviceCfgs();
                    request.setAttribute("areaDeviceCfgs", areaDeviceCfgs);
                    request.getRequestDispatcher("/page/device/allocation/tview_modify.jsp").forward(request, response);
                } else {
                	// 部门经理审核、信息中心审核
                    request.getRequestDispatcher("/page/device/allocation/tview_dispose.jsp").forward(request, response);
                }
            }
        } catch (OaException e1) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e1.getMessage());
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "系统出错");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
        }
    }
    
    /**
     * 初始化验收审批页面
     * @param form
     * @throws OaException
     */
    private void initBuyAccept(DevPurchaseForm form) throws OaException {
    	Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
        Map<String, Integer> intMap = new HashMap<String, Integer>();
        for (PurchaseDevice purchaseDevice : form.getPurchaseDevices()) {
            String deviceNo = "";
            int sepNum = 0;
            int formatNum = 0;// 格式化后的位数
            if (purchaseDevice != null) {
                Map<String, Integer> numberMap = new HashMap<String, Integer>();
                if (map == null) {

                    AreaDeviceCfg deviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(
                            purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass().getId());
                    if (deviceCfg != null) {
                        if (deviceCfg.getOrderPrefix() != null) {
                            deviceNo += deviceCfg.getOrderPrefix();
                        }
                        if (deviceCfg.getSeparator() != null) {
                            deviceNo += deviceCfg.getSeparator();
                        }
                    }
                    sepNum = deviceBiz.getDeviceSepNum(purchaseDevice.getDeviceClass().getId(),
                            purchaseDevice.getBelongtoAreaCode()) + 1;
                    formatNum = deviceCfg.getSeqNum();
                    numberMap.put(deviceNo, sepNum);
                    intMap.put(purchaseDevice.getDeviceClass().getId(), formatNum);
                    map.put(purchaseDevice.getDeviceClass().getId(), numberMap);
                } else {
                    if (!map.containsKey(purchaseDevice.getDeviceClass().getId())) {
                        AreaDeviceCfg deviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(
                                purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass()
                                        .getId());
                        if (deviceCfg != null) {
                            if (deviceCfg.getOrderPrefix() != null) {
                                deviceNo += deviceCfg.getOrderPrefix();
                            }
                            if (deviceCfg.getSeparator() != null) {
                                deviceNo += deviceCfg.getSeparator();
                            }
                        }
                        sepNum = deviceBiz.getDeviceSepNum(purchaseDevice.getDeviceClass().getId(),
                                purchaseDevice.getBelongtoAreaCode()) + 1;
                        formatNum = deviceCfg.getSeqNum();
                        numberMap.put(deviceNo, sepNum);
                        intMap.put(purchaseDevice.getDeviceClass().getId(), formatNum);
                        map.put(purchaseDevice.getDeviceClass().getId(), numberMap);
                    } else {
                        Map<String, Integer> numberMap_ = map.get(purchaseDevice.getDeviceClass().getId());
                        for (String string : numberMap_.keySet()) {
                            Integer num = numberMap_.get(string);
                            numberMap_.remove(string);
                            numberMap_.put(string, num + 1);
                        }
                        map.put(purchaseDevice.getDeviceClass().getId(), numberMap_);
                    }
                }
                Map<String, Integer> numberMap_ = map.get(purchaseDevice.getDeviceClass().getId());
                for (String string : numberMap_.keySet()) {
                    sepNum = numberMap_.get(string);
                    deviceNo = string;
                }
                purchaseDevice.setDeviceNo(deviceNo
                        + SerialNoCreater.getIsomuxByNum(String.valueOf(sepNum),
                                intMap.get(purchaseDevice.getDeviceClass().getId())));
            }
        }
    }

    public void dealHRTaskApprove(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        Double workYear = HttpRequestHelper.getDoubleParameter(request, "workYear", 1.0);
        String enterCompanyDateStr = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request,
                "enterCompanyDate"));
        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            deviceDiscardBiz.txUpdateDiscardByID(id, workYear, Timestamp.valueOf(enterCompanyDateStr));
            taskDealCtrl.dealApprove(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    public void dealCEOTaskApprove(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            DevDiscardForm devDiscardForm = deviceDiscardBiz.getDevDiscardFormById(id, true);
            if (devDiscardForm == null) {
                throw new IllegalArgumentException("设备报废单不存在");
            }
            String backBuyFlag = "0";
            // 如果报废方式为回购或丢失要经过财务到款确认然后到综合部复核，否则直接到综合部复核
            for (DiscardDevList devList : devDiscardForm.getDiscardDevLists()) {
                // 拿走
                if (DevDiscardForm.DEAL_TYPE_TAKE.equals(devList.getDealType())) {
                    backBuyFlag = "1";
                    break;
                }
                // 丢失
                if (DevDiscardForm.DEAL_TYPE_LOSE.equals(devList.getDealType())) {
                    backBuyFlag = "1";
                    break;
                }
            }
            // 发送邮件给申请人
//            deviceDiscardBiz.sendScrapEMail(devDiscardForm);
            devFlowBiz.txDealApproveTaskByFlowInstanceId(devDiscardForm.getFlowInstanceID(), backBuyFlag, 1,
                    taskInstanceId, comment, transitionName);
            XMLResponse.outputStandardResponse(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    /**
     * 处理报废/离职处理任务：如果有确认要回购的设备，则将回购标识写入流程环境变量
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void dealScrapTaskApproveByBuyFlag(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向

        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            DevDiscardForm devDiscardForm = deviceDiscardBiz.getDevDiscardFormById(id, true);
            if (devDiscardForm == null) {
                throw new IllegalArgumentException("文档不存在");
            }
            String backBuyFlag = "0";
            for (DiscardDevList devList : devDiscardForm.getDiscardDevLists()) {
                if (devList.getBuyFlag() != null && devList.getBuyFlag().booleanValue()) {
                    // 确认回购
                    backBuyFlag = "1";
                    break;
                }
            }

            devFlowBiz.txDealApproveTaskByFlowInstanceId(devDiscardForm.getFlowInstanceID(), backBuyFlag, 1,
                    taskInstanceId, comment, transitionName);
            XMLResponse.outputStandardResponse(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }
    }

    public void dealProcurePurchaseTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            Set<PurchaseDevice> purchaseDeviceSet = DeviceList.parsePurchaseDeviceJSON(aDevList, id);
            deviceApplyBiz.txUpdatePurchaseForm(id, purchaseDeviceSet);
            taskDealCtrl.dealApprove(request, response);
        } catch (OaException e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    private void initPurchaseValidate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请先登录");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        String deviceClassId = HttpRequestHelper.getParameter(request, "deviceClassId");//
        List<DeviceCheckItem> deviceCheckItems = deviceCheckItemBiz.queryCheckItemByDeviceClassId(deviceClassId);
        request.setAttribute("userName", user.getDisplayName());
        request.setAttribute("userId", user.getAccountID());
        request.setAttribute("validate", new Date());
        request.setAttribute("deviceCheckItems", deviceCheckItems);
        request.getRequestDispatcher("/page/device/recipients/buy/vali_dev.jsp")
                .forward(request, response);
    }

    /**
     * 设备验收处理
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void dealAcceptTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        if (aDevList == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            String id = HttpRequestHelper.getParameter(request, "id");
            boolean bntFlag = HttpRequestHelper.getBooleanParameter(request, "bntFlag", true);// false驳回修改
            Set<PurchaseDevice> purchaseDeviceSet = DeviceList.parsePurchaseValiJSON(aDevList);
            deviceApplyBiz.txSaveOrUpdateDevValidateForms(id, purchaseDeviceSet, bntFlag);
            // 采购设备入库
            if (bntFlag) {
                deviceApplyBiz.txPublishDeviceByApplyForm(id);
            }
            taskDealCtrl.dealApprove(request, response);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    private void toViewValiForm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		    IOException {
		String id = HttpRequestHelper.getParameter(request, "id");
		if (id == null) {
		    request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "请传入正确参数");
		    request.getRequestDispatcher("/page/error.jsp").forward(request, response);
		    return;
		}
		DevValidateForm form = devValidateFormBiz.getDevValidateFormById(id);
		request.setAttribute("form", form);
		request.getRequestDispatcher("/page/device/recipients/buy/view_vali.jsp")
		        .forward(request, response);
	}
    
    /**
     * 设备申购领用确认
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void dealPurchaseUseTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null || id == "") {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            Map<String, PurchaseDevice> purchaseDeviceMap = DeviceList.parsePurchaseDeviceByIDJSON(aDevList);
            deviceApplyBiz.txSaveOrUpdateDeviceForms(id, purchaseDeviceMap);
            taskDealCtrl.dealApprove(request, response);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    /**
     * 设备调拨确认
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void dealAllotInCheckTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null || id == "") {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            boolean valiDevFlag = HttpRequestHelper.getBooleanParameter(request, "valiDevFlag", true);
            Map<String, DevAllocateList> purchaseDeviceMap = DeviceList.parseDevAllocateListByIDJSON(aDevList);
            deviceAllocateBiz.txSaveOrUpdateDevAllocateForm(id, purchaseDeviceMap, valiDevFlag);
            taskDealCtrl.dealApprove(request, response);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    public void disposePurchase(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        String taskId = HttpRequestHelper.getParameter(request, "taskid");// 任务ID
        String tiid = HttpRequestHelper.getParameter(request, "tiid");// 任务实例ID
        String formId = HttpRequestHelper.getParameter(request, "formId");// 任务ID
        if (formId == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "表单不存在");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        if (taskId == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "任务不存在");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        String formType = "";
        DevPurchaseForm devPurchaseForm = deviceApplyBiz.getDevUseApplyFormById(formId, false, false);
        if (formId == null) {
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "表单不存在");
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
            return;
        }
        if (devPurchaseForm.getApplyType() == DevPurchaseForm.APPLY_TYPE_USE) {
            formType = String.valueOf(DeviceFlowView.FORM_TYPE_USE);
        } else {
            formType = String.valueOf(DeviceFlowView.FORM_TYPE_PERCHASE);
        }
        String viewUrl;

        try {
            // Task task = taskBiz.findById(taskId);
            // if(task !=null){
            // if(!task.getViewFlag()){
            // PeriodTask pt = periodTaskBiz.findById(task.getId());
            // if(pt!=null){
            // String message =
            // "此文件被要求在"+sdf.format(pt.getExecutionTime())+"之前完成审批，如逾期未审批，则自动按照<同意>处理。请在"+sdf.format(pt.getExecutionTime())+"之前及时完成审批工作，谢谢！";
            // request.setAttribute("message", message);
            // }
            // }
            // }
        	
        	List<String> userRoles = new ArrayList<String>();
    		List<Name> roles = user.getRoles();
    		if (roles != null && !roles.isEmpty()) {
    			for (Name role : roles) {
    				userRoles.add(role.getName());
    			}
    		}
    		
            viewUrl = taskBiz.txStartTask(taskId, user.getAccountID(), userRoles) + "&tiid=" + tiid + "&formType=" + formType;
            request.getRequestDispatcher(viewUrl).forward(request, response);
        } catch (OaException e) {
            e.printStackTrace();
            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, e.getMessage());
            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
        }
    }

    public void dealCompTaskApprove(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            DevDiscardForm devDiscardForm = deviceDiscardBiz.getDevDiscardFormById(id, true);
            if (devDiscardForm == null) {
                throw new IllegalArgumentException("设备报废单不存在");
            }
            String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
            Set<DiscardDevList> discardDevLists = DeviceList.parseJSON(aDevList, devDiscardForm);
            devDiscardForm.setDiscardDevLists(discardDevLists);
            deviceDiscardBiz.txUpdateDiscardDev(devDiscardForm, false);

            String backBuyFlag = "0";
            // 有一个设备是要离职回购的，就要走离职回购流程。
            for (DiscardDevList devList : devDiscardForm.getDiscardDevLists()) {
                if (DevDiscardForm.LEAVE_DEAL_TYPE_BACKBUY.equals(devList.getDealType())) {
                    backBuyFlag = "1";
                    break;
                }
            }
            devFlowBiz.txDealApproveTaskByFlowInstanceId(devDiscardForm.getFlowInstanceID(), backBuyFlag, 1,
                    taskInstanceId, comment, transitionName);
            XMLResponse.outputStandardResponse(response, 1, "提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    public void dealBackBuyConfirmTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        String id = HttpRequestHelper.getParameter(request, "id");
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        DevDiscardForm form = deviceDiscardBiz.getDevDiscardFormById(id, true);
        try {
            if (form == null) {
                throw new IllegalArgumentException("设备离职处理单不存在");
            }

            Set<DiscardDevList> discardDevLists = DeviceList.parseJSON(aDevList, form);
            String backBuyFlag = "0";
            if (discardDevLists != null) {
	            for (DiscardDevList devList : discardDevLists) {
	                if (devList.getBuyFlag()) {
	                    backBuyFlag = "1";
	                    break;
	                }
	            }
            }

            form.setDiscardDevLists(discardDevLists);
            deviceDiscardBiz.txUpdateDiscardDev(form, true);
            devFlowBiz.txDealApproveTaskByFlowInstanceId(form.getFlowInstanceID(), backBuyFlag, 1, taskInstanceId,
                    comment, transitionName);
            XMLResponse.outputStandardResponse(response, 1, "提交成功");
        } catch (OaException e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

    public void dealPurchaseCompTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        try {
            Set<PurchaseDevice> purchaseDeviceSet = DeviceList.parsePurchaseDeviceJSON(aDevList, id);
            deviceApplyBiz.txUpdatePurchaseDevices(purchaseDeviceSet);
            taskDealCtrl.dealApprove(request, response);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }
    }

    public void dealPayableTaskApprove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        String aDevList = HttpRequestHelper.getParameter(request, "aDevList");
        if (id == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        String taskInstanceId = HttpRequestHelper.getParameter(request, "tiid");// 任务ID
        String comment = HttpRequestHelper.getParameter(request, "comment");// 意见
        String transitionName = HttpRequestHelper.getParameter(request, "transition");// 转向
        DevDiscardForm form = deviceDiscardBiz.getDevDiscardFormById(id, true);
        try {
            Set<DiscardDevList> discardDevLists = DeviceList.parseJSON(aDevList, form);
            if (discardDevLists != null) {
                form.setDiscardDevLists(discardDevLists);
                deviceDiscardBiz.txUpdateDiscardDev(form, false);
                if (form == null) {
                    throw new IllegalArgumentException("设备报废单不存在");
                }
                String backBuyFlag = "2";
                for (DiscardDevList devList : form.getDiscardDevLists()) {
                    if (DevDiscardForm.DEAL_TYPE_TAKE.equals(devList.getDealType())) {
                        backBuyFlag = "1";
                        break;
                    }
                    if (DevDiscardForm.DEAL_TYPE_LOSE.equals(devList.getDealType())) {
                        backBuyFlag = "0";
                    }
                    if (DevDiscardForm.DEAL_TYPE_TAKEAWAY.equals(devList.getDealType())) {
                        backBuyFlag = "3";
                    }
                }
                devFlowBiz.txDealApproveTaskByFlowInstanceId(form.getFlowInstanceID(), backBuyFlag, 1, taskInstanceId,
                        comment, transitionName);
                XMLResponse.outputStandardResponse(response, 1, "提交成功");
            } else {
                taskDealCtrl.dealApprove(request, response);
            }
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }

   
}
