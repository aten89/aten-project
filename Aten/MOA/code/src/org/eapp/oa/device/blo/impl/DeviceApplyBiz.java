package org.eapp.oa.device.blo.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.dao.IDevAllocateFormDAO;
import org.eapp.oa.device.dao.IDevUseApplyFormDAO;
import org.eapp.oa.device.dao.IDevValidateFormDAO;
import org.eapp.oa.device.dao.IDeviceClassDAO;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.dto.AreaClassFlowDTO;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevFlowApplyProcess;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.device.hbean.DevicePropertyDetail;
import org.eapp.oa.device.hbean.DeviceValiDetail;
import org.eapp.oa.device.hbean.PurchaseDevPurpose;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.hibernate.Hibernate;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.oa.system.util.Tools;
import org.eapp.oa.system.config.SysConstants;

public class DeviceApplyBiz implements IDeviceApplyBiz {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DeviceApplyBiz.class);

    /**
     * 设备使用单
     */
    private IDevUseApplyFormDAO devUseApplyFormDAO;

    /**
     * 设备
     */
    private IDeviceDAO deviceDAO;

    /**
     * 任务
     */
    private ITaskDAO taskDAO;

    /**
     * 设备类型
     */
    private IDeviceClassDAO deviceClassDAO;

    /**
     * 设备地区配置
     */
    private IDeviceAreaConfigBiz deviceAreaConfigBiz;

    /**
     * 设备
     */
    private IDeviceBiz deviceBiz;

    /**
     * 设备验证单
     */
    private IDevValidateFormDAO devValidateFormDAO;

    /**
     * 设备分配单
     */
    private IDevAllocateFormDAO devAllocateFormDAO;

    public IDeviceClassDAO getDeviceClassDAO() {
        return deviceClassDAO;
    }

    public void setDeviceClassDAO(IDeviceClassDAO deviceClassDAO) {
        this.deviceClassDAO = deviceClassDAO;
    }

    public IDevUseApplyFormDAO getDevUseApplyFormDAO() {
        return devUseApplyFormDAO;
    }

    public void setDevUseApplyFormDAO(IDevUseApplyFormDAO devUseApplyFormDAO) {
        this.devUseApplyFormDAO = devUseApplyFormDAO;
    }

    public IDeviceDAO getDeviceDAO() {
        return deviceDAO;
    }

    public void setDeviceDAO(IDeviceDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    public ITaskDAO getTaskDAO() {
        return taskDAO;
    }

    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setDevAllocateFormDAO(IDevAllocateFormDAO devAllocateFormDAO) {
        this.devAllocateFormDAO = devAllocateFormDAO;
    }

    public void setDeviceAreaConfigBiz(IDeviceAreaConfigBiz deviceAreaConfigBiz) {
        this.deviceAreaConfigBiz = deviceAreaConfigBiz;
    }

    public void txDelDevApplyForm(String id) throws OaException {
        if (id == null) {
            return;
        }
        DevPurchaseForm devApplyForm = devUseApplyFormDAO.findById(id);
        if (devApplyForm == null) {
            return;
        }
        if (DevPurchaseForm.FORMSTATUS_UNPUBLISH != devApplyForm.getFormStatus()) {
            throw new OaException("只能删除未发布的消息");
        }
        devUseApplyFormDAO.delete(devApplyForm);
    }
    
    public void txStartFlow(DevPurchaseForm devAppForm, String flowKey, int formType) throws OaException {
        if (devAppForm == null) {
            return;
        }
        if (flowKey == null) {// 不启动流程，直接发布
            devAppForm.setPassed(true);
            devAppForm.setArchiveDate(new Date());
            devAppForm.setFormStatus(DevAllocateForm.FORMSTATUS_PUBLISH);
        } else {
            devAppForm.setFormStatus(DevAllocateForm.FORMSTATUS_APPROVAL);
            WfmContext context = WfmConfiguration.getInstance().getWfmContext();
            try {
                // 设置流程上下文变量中，并启动流程
                Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
                // 把表单ID
                ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID,
                        ContextVariable.DATATYPE_STRING, devAppForm.getId());
                contextVariables.put(cv.getName(), cv);
                // 发起人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                        devAppForm.getApplicant());
                contextVariables.put(cv.getName(), cv);
                // 发起人部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME, ContextVariable.DATATYPE_STRING,
                        devAppForm.getApplyGroupName());
                contextVariables.put(cv.getName(), cv);
                // 表单类别
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMTYPE, ContextVariable.DATATYPE_STRING,
                        String.valueOf(formType));
                contextVariables.put(cv.getName(), cv);
                
                // 2013-05-24 修改：增加所属区域名称、设备用途流程变量
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_AREA, ContextVariable.DATATYPE_STRING,
                        devAppForm.getAreaCode());
                contextVariables.put(cv.getName(), cv);
                
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_OUTACCOUNTID, ContextVariable.DATATYPE_STRING, null);
                contextVariables.put(cv.getName(), cv);
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_OUTGROUPNAME, ContextVariable.DATATYPE_STRING, null);
                contextVariables.put(cv.getName(), cv);
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_INACCOUNTID, ContextVariable.DATATYPE_STRING, null);
                contextVariables.put(cv.getName(), cv);
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_INGROUPNAME, ContextVariable.DATATYPE_STRING, null);
                contextVariables.put(cv.getName(), cv);
                
                // 任务描述
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                        devAppForm.getApplicantDisplayName() + "的设备" + (devAppForm.getApplyType() == 0 ? "领用" : "申购")
                                + "单(" + devAppForm.getFullFormNO() + ")");
                contextVariables.put(cv.getName(), cv);
                FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
                flowInstance.signal();
                context.save(flowInstance);
                // 设置表单视图的ID
                devAppForm.setFlowInstanceID(flowInstance.getId());
            } catch (Exception e) {
                log.error("txStartFlow faild", e);
                context.rollback();
                throw new OaException(e.getMessage());
            } finally {
                context.close();
            }
        }
    }
    
    
    public DevPurchaseForm getDevUseApplyFormById(String id, boolean initList, boolean purpose) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        DevPurchaseForm form = devUseApplyFormDAO.findById(id);
        if (form != null && initList) {
            // 领用
            if (form.getApplyType() == DevPurchaseForm.APPLY_TYPE_USE) {
                Hibernate.initialize(form.getDevPurchaseLists());
                if (form.getDevPurchaseLists() != null && form.getDevPurchaseLists().size() > 0) {
                    boolean initPlanUseDate = false;
                    for (DevPurchaseList devPurchaseList : form.getDevPurchaseLists()) {
                        if (devPurchaseList != null) {
                            if (!initPlanUseDate) {
                                form.setPlanUseDate(devPurchaseList.getPlanUseDate());
                                form.setDevPurchase(devPurchaseList);
                                initPlanUseDate = true;
                            }
                            if (devPurchaseList.getDevice() != null) {
                                Hibernate.initialize(devPurchaseList.getDevice());
                                deviceBiz.initConfigList(devPurchaseList.getDevice());
                                Hibernate.initialize(devPurchaseList.getDevice().getDevicePropertyDetails());
                            }
                        }
                    }

                    // 对领用的设备按设备编号排序
                    Set<DevPurchaseList> devPurchaseLists = new TreeSet<DevPurchaseList>(
                            new Comparator<DevPurchaseList>() {
                                @Override
                                public int compare(DevPurchaseList o1, DevPurchaseList o2) {
                                    if (o1 != null && o1.getDevice() != null && o1.getDevice().getDeviceNO() != null
                                            && o2 != null && o2.getDevice() != null) {
                                        return o1.getDevice().getDeviceNO().compareTo(o2.getDevice().getDeviceNO());
                                    }
                                    return -1;
                                }
                            });
                    devPurchaseLists.addAll(form.getDevPurchaseLists());
                    form.getDevPurchaseLists().clear();
                    form.getDevPurchaseLists().addAll(devPurchaseLists);
                }
            }

            // 申购
            if (form.getApplyType() == DevPurchaseForm.APPLY_TYPE_PURCHASE) {
                Hibernate.initialize(form.getPurchaseDevices());
                Hibernate.initialize(form.getPurchaseDeviceClass());
                if (form.getPurchaseDevices() != null && form.getPurchaseDevices().size() > 0) {
                    boolean initPlanUseDate = false;
                    boolean initDevCfgDesc = false;
                    boolean initAreaCode = false;
                    double tempBuyMoney = 0.0;// 总的购买金额
                    Date tempBuyDate = null;// 购买时间：取申购设备列表中最早的
                    for (PurchaseDevice devPurchaseList : form.getPurchaseDevices()) {
                        if (devPurchaseList != null) {
                            tempBuyMoney = Tools.add(
                                    (devPurchaseList.getPrice() == null ? 0.0 : devPurchaseList.getPrice()),
                                    tempBuyMoney);// 计算总的购买金额
                            if (tempBuyDate == null) {
                                tempBuyDate = devPurchaseList.getBuyTime();
                            } else {
                                if (tempBuyDate.compareTo(devPurchaseList.getBuyTime()) > 0) {
                                    tempBuyDate = devPurchaseList.getBuyTime();//
                                }
                            }
                            form.setTempBuyDate(tempBuyDate);// 设置主表单的设备购买时间
                            form.setTempBuyMoney(tempBuyMoney);
                            if (devPurchaseList.getDeviceID() != null) {
                                // Device device= deviceBiz.getDeviceById(devPurchaseList.getDeviceID());
                                Device device = deviceDAO.findById(devPurchaseList.getDeviceID());
                                devPurchaseList.setDeviceNo(device.getDeviceNO());
                            }
                            if (!initPlanUseDate) {
                                form.setPlanUseDate(devPurchaseList.getPlanUseDate());
                                Hibernate.initialize(devPurchaseList.getDeviceClass());
                                if (devPurchaseList.getDeviceClass() != null) {
                                    form.setDeviceClass(devPurchaseList.getDeviceClass().getId());
                                    form.setDeviceClassDisplayName(devPurchaseList.getDeviceClass().getName());
                                }

                                initPlanUseDate = true;
                            }
                            if (!initDevCfgDesc) {
                                form.setDevCfgDesc(devPurchaseList.getDeviceCfgDesc());
                                initDevCfgDesc = true;
                            }
                            if (!initAreaCode) {
                                form.setAreaCode(devPurchaseList.getBelongtoAreaCode());
                                form.setAreaCodePurpose(devPurchaseList.getAreaCode());
                                if (devPurchaseList.getDeviceClass() != null) {
                                    form.setDeviceClass(devPurchaseList.getDeviceClass().getId());
                                }

                                initAreaCode = true;
                            }
                            if (form.getPurchaseDevices() != null) {
                                for (PurchaseDevice purchaseDevice : form.getPurchaseDevices()) {
                                    Hibernate.initialize(purchaseDevice.getDeviceClass());
                                    Hibernate.initialize(purchaseDevice.getDevicePropertyDetails());
                                    this.initConfigList(purchaseDevice);
                                }
                            }
                        }
                    }
                }
                if (purpose) {
                    Hibernate.initialize(form.getPurchaseDevPurposes());
                    if (form.getPurchaseDevPurposes() != null) {
                        boolean purposeFlag = true;
                        String purchaseDevPurposeStr = "";
                        for (PurchaseDevPurpose purchaseDevPurpose : form.getPurchaseDevPurposes()) {
                            int count = 0;
                            form.setPurpose(purchaseDevPurpose.getPurpose());
                            purchaseDevPurposeStr += purchaseDevPurpose.getPurpose() + ";";
                            Map<String, Integer> countMap = deviceBiz.getDeviceUseCount(form.getDeviceType(), null,
                                    form.getDeviceClass(), purchaseDevPurpose.getPurpose(), form.getApplicant(), true);
                            for (String key : countMap.keySet()) {
                                if (key != null) {
                                    count += countMap.get(key);
                                    if (key.equals("0")) {
                                        purchaseDevPurpose.setUseFuzhouCount(countMap.get(key));
                                    } else if (key.equals("1")) {
                                        purchaseDevPurpose.setUseShangHaiCount(countMap.get(key));
                                    } else if (key.equals("2")) {
                                        purchaseDevPurpose.setUseXiamenCount(countMap.get(key));
                                    }
                                }

                            }
                            purchaseDevPurpose.setUseCount(count);
                            if (purchaseDevPurpose.getManyTimeFlag() && purposeFlag) {
                                form.setUseCount(-1);
                                purposeFlag = false;
                            } else {
                                if (purposeFlag) {
                                    if (form.getUseCount() == null) {
                                        form.setUseCount(1);
                                    } else {
                                        form.setUseCount(form.getUseCount() + 1);
                                    }

                                }
                            }
                        }
                        form.setPurposes(purchaseDevPurposeStr);
                    }
                }
            }
        }
        return form;
    }

    public void txPublishApplyForm(String id) {
        if (id == null) {
            throw new IllegalArgumentException("非法参数:id");
        }
        DevPurchaseForm form = devUseApplyFormDAO.findById(id);
        if (form == null) {
            throw new IllegalArgumentException("文档不存在");
        }
        form.setFormStatus(DevFlowApplyProcess.FORMSTATUS_PUBLISH);
        form.setArchiveDate(new Date());
        if (form.getApplyType() == DevPurchaseForm.APPLY_TYPE_USE) {
            if (form.getDevPurchaseLists() != null && form.getDevPurchaseLists().size() > 0) {
                for (DevPurchaseList devPurchaseList : form.getDevPurchaseLists()) {
                    if (devPurchaseList != null) {
                        Hibernate.initialize(devPurchaseList.getDevice());
                        Hibernate.initialize(devPurchaseList.getDevice().getDeviceCurStatusInfo());
                        DeviceCurStatusInfo deviceCurStatusInfo = devPurchaseList.getDevice().getDeviceCurStatusInfo();
                        deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                        deviceCurStatusInfo.setStatusUptDate(new Date());
                        deviceCurStatusInfo.setOwner(form.getApplicant());
                        deviceCurStatusInfo.setGroupName(form.getApplyGroupName());
                        deviceCurStatusInfo.setAreaCode(devPurchaseList.getAreaCode());
                        deviceCurStatusInfo.setPurpose(devPurchaseList.getPurpose());
                        deviceCurStatusInfo.setApproveType(null);
                        deviceCurStatusInfo.setFormID(null);
                        devUseApplyFormDAO.saveOrUpdate(deviceCurStatusInfo);
                    }
                }
            }
            devUseApplyFormDAO.saveOrUpdate(form);
        }
    }

    public void txPublishDeviceByApplyForm(String applyFormID) throws OaException {
        if (applyFormID == null) {
            throw new IllegalArgumentException("非法参数:id");
        }
        DevPurchaseForm form = devUseApplyFormDAO.findById(applyFormID);
        if (form != null && form.getApplyType().intValue() == DevPurchaseForm.APPLY_TYPE_PURCHASE) {
            Hibernate.initialize(form.getPurchaseDevices());
            if (form != null && form.getPurchaseDevices() != null && form.getPurchaseDevices().size() > 0) {
                for (PurchaseDevice purchaseDevice : form.getPurchaseDevices()) {
                    Device device = new Device();
                    if (purchaseDevice != null) {
                        AreaDeviceCfg deviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(
                                purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass().getId());
                        String deviceNo = "";
                        if (deviceCfg != null) {
                            if (deviceCfg.getOrderPrefix() != null) {
                                deviceNo += deviceCfg.getOrderPrefix();
                            }
                            if (deviceCfg.getSeparator() != null) {
                                deviceNo += deviceCfg.getSeparator();
                            }
                            int sepNum = deviceBiz.getDeviceSepNum(purchaseDevice.getDeviceClass().getId(),
                                    purchaseDevice.getBelongtoAreaCode()) + 1;
                            deviceNo += SerialNoCreater.getIsomuxByNum(String.valueOf(sepNum), deviceCfg.getSeqNum());
                            device.setDeviceNO(deviceNo);
                            device.setSequence(sepNum);
                        }

                        device.setAreaCode(purchaseDevice.getBelongtoAreaCode());
                        device.setBuyTime(purchaseDevice.getBuyTime());
                        // device.setDeductMoney(deductMoney);
                        device.setDeviceType(form.getDeviceType());
                        device.setDeviceModel(purchaseDevice.getDeviceModel());
                        device.setDeviceClass(purchaseDevice.getDeviceClass());
                        device.setDescription(purchaseDevice.getDescription());
                        device.setPrice(purchaseDevice.getPrice());
                        device.setBuyType(form.getBuyType());
                        device.setDeductFlag(purchaseDevice.getDeductFlag());
                        device.setDeductMoney(purchaseDevice.getDeductMoney());
                        // if(Device.BUY_TYPE_SUB.equals(form.getBuyType())){
                        // device.setDeductFlag(false);
                        // }
                        device.setDeviceName(purchaseDevice.getDeviceName());

                        device.setRegAccountID("super@admins");
                        device.setRegTime(new Date());
                        Hibernate.initialize(purchaseDevice.getDevicePropertyDetails());
                        if (purchaseDevice.getDevicePropertyDetails() != null
                                && purchaseDevice.getDevicePropertyDetails().size() > 0) {
                            Set<DevicePropertyDetail> propertyDetailSet = new HashSet<DevicePropertyDetail>();
                            for (DevicePropertyDetail propertyDetail : purchaseDevice.getDevicePropertyDetails()) {
                                DevicePropertyDetail newPropertyDetail = new DevicePropertyDetail();
                                newPropertyDetail.setPropertyName(propertyDetail.getPropertyName());
                                newPropertyDetail.setPropertyValue(propertyDetail.getPropertyValue());
                                newPropertyDetail.setRemark(propertyDetail.getRemark());
                                newPropertyDetail.setDevice(device);
                                propertyDetailSet.add(newPropertyDetail);
                            }
                            device.setDevicePropertyDetails(propertyDetailSet);
                        }
                        Hibernate.initialize(purchaseDevice.getDevValidateForm());
                        if (purchaseDevice.getDevValidateForm() != null) {
                            DevValidateForm newDevValidateForm = new DevValidateForm();
                            DevValidateForm devValidateForm = purchaseDevice.getDevValidateForm();
                            newDevValidateForm.setDevice(device);
                            newDevValidateForm.setAccountID(devValidateForm.getAccountID());
                            newDevValidateForm.setValiDate(devValidateForm.getValiDate());
                            newDevValidateForm.setValidFormNO(devValidateForm.getValidFormNO());
                            newDevValidateForm.setSequenceYear(devValidateForm.getSequenceYear());
                            newDevValidateForm.setRemark(devValidateForm.getRemark());
                            Hibernate.initialize(devValidateForm.getDeviceValiDetails());
                            if (devValidateForm.getDeviceValiDetails() != null) {
                                Set<DeviceValiDetail> deviceValiDetailSet = new HashSet<DeviceValiDetail>();
                                for (DeviceValiDetail deviceValiDetail : devValidateForm.getDeviceValiDetails()) {
                                    DeviceValiDetail newDeviceValiDetail = new DeviceValiDetail();
                                    newDeviceValiDetail.setIsEligibility(deviceValiDetail.getIsEligibility());
                                    newDeviceValiDetail.setDevValidateForm(newDevValidateForm);
                                    newDeviceValiDetail.setItem(deviceValiDetail.getItem());
                                    newDeviceValiDetail.setRemark(deviceValiDetail.getRemark());
                                    deviceValiDetailSet.add(newDeviceValiDetail);
                                }
                                newDevValidateForm.setDeviceValiDetails(deviceValiDetailSet);
                            }
                            device.setDevValidateForm(newDevValidateForm);
                        }
                        // 保存设备状态表
                        DeviceCurStatusInfo statusInfo = new DeviceCurStatusInfo();
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                        statusInfo.setStatusUptDate(new Date());
                        statusInfo.setDevice(device);
                        statusInfo.setGroupName(form.getApplyGroupName());
                        statusInfo.setOwner(null);
                        statusInfo.setPurpose(purchaseDevice.getPurpose());
                        statusInfo.setAreaCode(purchaseDevice.getAreaCode());
                        statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_PURCH);// 将设备的状态变为申购审批中
                        statusInfo.setStatusUptDate(form.getApplyDate());// 设备申购申请日期
                        statusInfo.setFormID(form.getId());
                        device.setDeviceCurStatusInfo(statusInfo);
                        device = deviceBiz.txAddDevice(device);
                        purchaseDevice.setDeviceID(device.getId());
                        devUseApplyFormDAO.saveOrUpdate(purchaseDevice);
                    }
                }
            }
            devUseApplyFormDAO.saveOrUpdate(form);
        }
    }

    public void txCancellDevPurchaseForm(String formId) {

        if (formId == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        DevPurchaseForm form = devUseApplyFormDAO.findById(formId);
        if (form == null) {
            throw new IllegalArgumentException("设备领用或申购单不存在");
        }
        form.setArchiveDate(new Date());
        form.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
        if (form.getApplyType() == DevPurchaseForm.APPLY_TYPE_USE) {
            if (form != null && form.getDevPurchaseLists() != null && form.getDevPurchaseLists().size() > 0) {
                for (DevPurchaseList devPurchaseList : form.getDevPurchaseLists()) {
                    if (devPurchaseList != null) {
                        Hibernate.initialize(devPurchaseList.getDevice());
                        if (devPurchaseList.getDevice() != null) {
                            Hibernate.initialize(devPurchaseList.getDevice().getDeviceCurStatusInfo());
                            DeviceCurStatusInfo deviceCurStatusInfo = devPurchaseList.getDevice()
                                    .getDeviceCurStatusInfo();
                            deviceCurStatusInfo.setApproveType(null);
                            deviceCurStatusInfo.setFormID(null);
                            devUseApplyFormDAO.saveOrUpdate(deviceCurStatusInfo);
                        }
                    }
                }
            }
        }
        devUseApplyFormDAO.saveOrUpdate(form);

    }

    @Override
    public DevPurchaseForm txAddDeviceApplyForm(DevPurchaseForm applyForm, boolean validMainDevFlag) throws OaException {
        if (applyForm == null) {
            throw new IllegalArgumentException("参数错误");
        }
        Map<String, Boolean> manyMap = new HashMap<String, Boolean>();
        Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
        if (applyForm.getDevPurchaseLists() != null && applyForm.getDevPurchaseLists().size() > 0) {
            Map<String, PurchaseDevPurpose> purposeTimes = new HashMap<String, PurchaseDevPurpose>();
            boolean flag = true;
            String deviceClassId = null;
            String deviceClassName = null;
            String deviceType = null;
            if (validMainDevFlag) {
                getUseMainDevCount(applyForm.getDevPurchaseLists(), applyForm.getApplicant(), 0, null);// 追加权限
            }
            for (DevPurchaseList devPurchaseList : applyForm.getDevPurchaseLists()) {
                if (flag && devPurchaseList.getDevice() != null) {
                    deviceClassId = devPurchaseList.getDevice().getDeviceClass().getId();
                    deviceClassName = devPurchaseList.getDevice().getDeviceClass().getName();
                    deviceType = devPurchaseList.getDevice().getDeviceType();
                    flag = false;
                }
//                if (devPurchaseList != null && devPurchaseList.getDevice() != null) {

//                    boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(applyForm.getAreaCode(),
//                            devPurchaseList.getDevice().getDeviceClass().getId(), devPurchaseList.getPurpose());
//                    String manykey = applyForm.getAreaCode() + ","
//                            + devPurchaseList.getDevice().getDeviceClass().getId() + "," + devPurchaseList.getPurpose();
//                    manyMap.put(manykey, manyTimeFlag);
//                    if (!manyTimeFlag) {
//                        PurchaseDevPurpose devPurposePurchase = new PurchaseDevPurpose();
//                        devPurposePurchase.setPurpose(devPurchaseList.getPurpose());
//                        devPurposePurchase.setManyTimeFlag(manyTimeFlag);
//                        purchaseDevPurposes.add(devPurposePurchase);
//                        String key = applyForm.getAreaCode() + ","
//                                + devPurchaseList.getDevice().getDeviceClass().getId();
//                        if (purposeTimes != null && purposeTimes.get(key) != null) {
//                            PurchaseDevPurpose devPurpose = purposeTimes.get(key);
//                            devPurpose.setUseCount(devPurpose.getUseCount() + 1);
//                            purposeTimes.put(key, devPurpose);
//                        } else {
//                            PurchaseDevPurpose devPurpose = new PurchaseDevPurpose();
//                            devPurpose.setPurpose(devPurchaseList.getPurpose());
//                            devPurpose.setDeviceClassName(devPurchaseList.getDevice().getDeviceClass().getName());
//                            devPurpose.setUseCount(1);
//                            purposeTimes.put(key, devPurpose);
//                        }
//                    }
//                }
                if (purposeTimes != null) {
                    String msg = "";
                    for (String str : purposeTimes.keySet()) {
                        PurchaseDevPurpose devPurpose = purposeTimes.get(str);
                        if (devPurpose != null) {
                            if (devPurpose.getUseCount() > 1) {
                                msg += "设备类别名称为【" + devPurpose.getDeviceClassName() + "】的设备只能领用一台\r";
                            }
                        }
                    }
                    if (msg != "") {
                        throw new OaException(msg);// 未配置领用、申购流程
                    }
                }
                checkDeviceMsg(deviceType, applyForm.getAreaCode(), deviceClassId, deviceClassName,
                        applyForm.getApplicant(), applyForm.getApplyType(), purchaseDevPurposes, true, true);
                if (devPurchaseList != null && devPurchaseList.getDevice() != null) {
                    DeviceCurStatusInfo statusInfo = devPurchaseList.getDevice().getDeviceCurStatusInfo();
                    statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                    statusInfo.setOwner(applyForm.getApplicant());
                    statusInfo.setGroupName(applyForm.getApplyGroupName());
                    statusInfo.setStatusUptDate(new Date());
                    statusInfo.setAreaCode(devPurchaseList.getAreaCode());
                    statusInfo.setPurpose(devPurchaseList.getPurpose());
                    deviceBiz.txEndTaskByApproveType(devPurchaseList.getDevice().getId(), statusInfo.getApproveType());
                    String manykey = applyForm.getAreaCode() + ","
                            + devPurchaseList.getDevice().getDeviceClass().getId() + "," + devPurchaseList.getPurpose();
                    if (manyMap.get(manykey) != null && !manyMap.get(manykey)) {
                        deviceBiz.txEndTaskByPurpose(deviceType, applyForm.getAreaCode(), deviceClassId,
                                devPurchaseList.getPurpose(), applyForm.getApplicant());
                    }
                    statusInfo.setStatusUptDate(new Date());
                    statusInfo.setApproveType(null);
                    statusInfo.setFormID(null);
                    devUseApplyFormDAO.merge(statusInfo);
                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int no = devUseApplyFormDAO.findCurrentYearFormNO(year);
        applyForm.setApplyFormNO(++no);// 编号序列号
        applyForm.setSequenceYear(year);// 编号年份
        devUseApplyFormDAO.save(applyForm);
        return applyForm;
    }

    @Override
    public DevPurchaseForm txSaveAsDraf(String deviceTypeCode, String deviceClass, String userAccountID,
            String applyGroupName, String remark, String buyType, Double budgetMoney, int formType, String areaCode,
            String workAreaCode, Set<DevPurchaseList> devPurchaseList, Set<PurchaseDevice> purchaseDevices,
            Set<PurchaseDevPurpose> purchaseDevPurposes, boolean isStartFlow, boolean validMainDevFlag)
            throws OaException {
        Date now = new Date();
        DevPurchaseForm applyForm = new DevPurchaseForm();
        applyForm.setDeviceType(deviceTypeCode);
        applyForm.setRegAccountID(userAccountID);// 登记人
        applyForm.setRegTime(now);// 登记时间
        applyForm.setApplicant(userAccountID);// 申请人
        applyForm.setApplyGroupName(applyGroupName);// 申请人所属部门
        applyForm.setApplyDate(now);// 申请时间
        applyForm.setRemark(remark);
        applyForm.setBuyType(buyType);
        applyForm.setBudgetMoney(budgetMoney);
        applyForm.setApplyType(formType);
        applyForm.setAreaCode(areaCode);
        applyForm.setWorkAreaCode(workAreaCode);
        if (deviceClass != null) {
            DeviceClass deviceClass_ = new DeviceClass();
            deviceClass_.setId(deviceClass);
            applyForm.setPurchaseDeviceClass(deviceClass_);
        }
        String deviceClassId = null;
        String deviceClassName = null;
        boolean flag = true;
        // 2013-05-24修改：增加设备用途str
        StringBuffer purposeSb = new StringBuffer();
        if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
            // 领用
            if (devPurchaseList != null && devPurchaseList.size() > 0) {
                for (DevPurchaseList list : devPurchaseList) {
                    if (list != null) {
                        // 2013-05-24修改：增加设备用途str
                        purposeSb.append(list.getPurpose());
                        
                        list.setDevPurchaseForm(applyForm);// 设备主表单
                        if (flag) {
                            if (list.getDevice() != null) {
                                deviceClassId = list.getDevice().getDeviceClass().getId();
                                if (list.getDevice().getDeviceClass().getName() != null) {
                                    DeviceClass d = deviceClassDAO.findById(deviceClassId);
                                    if (d != null) {
                                        deviceClassName = d.getName();
                                    }

                                } else {
                                    deviceClassName = list.getDevice().getDeviceClass().getName();
                                }
                                flag = false;
                            }

                        }
                    }
                }
                applyForm.getDevPurchaseLists().addAll(devPurchaseList);
            }
        } else {
            // 申购
            flag = true;
            if (purchaseDevices != null && purchaseDevices.size() > 0) {
                for (PurchaseDevice list : purchaseDevices) {
                    if (list != null) {
                        list.setDevPurchaseForm(applyForm);// 设备主表单
                        if (flag) {
                            if (list.getDeviceClass() != null) {
                                deviceClassId = list.getDeviceClass().getId();
                                if (list.getDeviceClass().getName() != null) {
                                    DeviceClass d = deviceClassDAO.findById(deviceClassId);
                                    if (d != null) {
                                        deviceClassName = d.getName();
                                    }

                                } else {
                                    deviceClassName = list.getDeviceClass().getName();
                                }
                                flag = false;
                            }

                        }
                    }
                }
                applyForm.getPurchaseDevices().addAll(purchaseDevices);
            }
            if (purchaseDevPurposes != null && purchaseDevPurposes.size() > 0) {
                for (PurchaseDevPurpose list : purchaseDevPurposes) {
                    if (list != null) {
                        // 2013-05-24修改：增加设备用途str
                        purposeSb.append(list.getPurpose());
                        list.setDevPurchaseForm(applyForm);// 设备主表单
                    }
                }
                applyForm.getPurchaseDevPurposes().addAll(purchaseDevPurposes);
            }

        }
        
        // 2013-05-24修改：增加设备用途str
        applyForm.setPurposes(purposeSb.toString());
        
        applyForm.setFormStatus(DevPurchaseForm.FORMSTATUS_UNPUBLISH);// 未发布
        if (isStartFlow) {
            if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                for (DevPurchaseList list : devPurchaseList) {
                    if (list != null && list.getDevice() != null
                            && list.getDevice().getDeviceCurStatusInfo().getApproveType() != null) {
                        // 审批中
                        throw new OaException("编号为“"
                                + list.getDevice().getDeviceNO()
                                + "”的设备正处于"
                                + DeviceCurStatusInfo.approveTypeMap.get(list.getDevice().getDeviceCurStatusInfo()
                                        .getApproveType()) + "，现不能领用！");
                    }
                }
                if (validMainDevFlag) {
                    getUseMainDevCount(devPurchaseList, userAccountID, 0, null);// 追加权限
                }
            } else {
                if (validMainDevFlag) {
                    getPurchaseMainDevCount(purchaseDevices, userAccountID, formType, null, true);// 检测主设备领用次数
                }

            }

            applyForm.setFormStatus(DevPurchaseForm.FORMSTATUS_APPROVAL);// 审批中
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int no = devUseApplyFormDAO.findCurrentYearFormNO(year);
            applyForm.setApplyFormNO(++no);// 编号序列号
            applyForm.setSequenceYear(year);// 编号年份

            Set<AreaClassFlowDTO> areaClassFlowDTOs = null;
            // 保存设备的审批状态
            if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                Map<String, PurchaseDevPurpose> purposeTimes = new HashMap<String, PurchaseDevPurpose>();
                purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
                for (DevPurchaseList list : applyForm.getDevPurchaseLists()) {
                    if (list != null && list.getDevice() != null) {
                        PurchaseDevPurpose devPurposePurchase = new PurchaseDevPurpose();
                        devPurposePurchase.setPurpose(list.getPurpose());
                        DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                        statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_USE);
                        statusInfo.setStatusUptDate(applyForm.getApplyDate());// 设备领用申请日期
                        statusInfo.setFormID(applyForm.getId());
                        devUseApplyFormDAO.merge(statusInfo);
//                        boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(areaCode, list.getDevice()
//                                .getDeviceClass().getId(), list.getPurpose());
                        devPurposePurchase.setManyTimeFlag(false);
                        purchaseDevPurposes.add(devPurposePurchase);
//                        if (!manyTimeFlag) {
                            String key = areaCode + "," + list.getDevice().getDeviceClass().getId();
                            if (purposeTimes != null && purposeTimes.get(key) != null) {
                                PurchaseDevPurpose devPurpose = purposeTimes.get(key);
                                devPurpose.setUseCount(devPurpose.getUseCount() + 1);
                                purposeTimes.put(key, devPurpose);
                            } else {
                                PurchaseDevPurpose devPurpose = new PurchaseDevPurpose();
                                devPurpose.setPurpose(list.getPurpose());
                                devPurpose.setDeviceClassName(list.getDevice().getDeviceClass().getName());
                                devPurpose.setUseCount(1);
                                purposeTimes.put(key, devPurpose);

                            }
//                        }
                    }
                }
                if (purposeTimes != null) {
                    String msg = "";
                    for (String str : purposeTimes.keySet()) {
                        PurchaseDevPurpose devPurpose = purposeTimes.get(str);
                        if (devPurpose != null) {
                            if (devPurpose.getUseCount() > 1) {
                                msg += "设备类别名称为【" + devPurpose.getDeviceClassName() + "】的设备只能领用一台\r";
                            }

                        }
                    }
                    if (msg != "") {
                        throw new OaException(msg);// 未配置领用、申购流程
                    }
                }
                areaClassFlowDTOs = buildUseAreaClassFlows(devPurchaseList, AreaDeviceCfg.FLOW_TYPE_DEVUSE);
            } else {
                areaClassFlowDTOs = buildPurchaseAreaClassFlows(purchaseDevices, areaCode,
                        AreaDeviceCfg.FLOW_TYPE_DEVPURCHASE);
            }
            checkDeviceMsg(deviceTypeCode, areaCode, deviceClassId, deviceClassName, applyForm.getApplicant(),
                    formType, purchaseDevPurposes, false, true);// 判断工作用途是否可以领用多台，包括已领用，申购、领用在审批中的

            String flowKey = getFlowKeyByType(areaClassFlowDTOs, AreaDeviceCfg.FLOW_TYPE_DEVUSE);// 领用和申购用的是同一个流程
            if (flowKey == null) {
                throw new OaException("未配置" + DevPurchaseForm.applyTypeMap.get(formType) + "流程");// 未配置领用、申购流程
            }
            devUseApplyFormDAO.save(applyForm);
            txStartFlow(applyForm, flowKey, formType);
        } else {
            devUseApplyFormDAO.save(applyForm);
        }
        return applyForm;
    }

    /**
     * 组合领用区域设备类别流程列表
     * 
     * @param devPurchaseList
     * @param flowType
     * @return
     */
    private Set<AreaClassFlowDTO> buildUseAreaClassFlows(Set<DevPurchaseList> devPurchaseList, int flowType) {
        Set<AreaClassFlowDTO> areaClassFlowDTOs = new HashSet<AreaClassFlowDTO>();
        if (devPurchaseList != null) {
            for (DevPurchaseList list : devPurchaseList) {
                if (list != null) {
                    AreaClassFlowDTO areaClassFlowDTO = new AreaClassFlowDTO();
                    areaClassFlowDTO.setAreaCode(list.getDevice().getAreaCode());
                    areaClassFlowDTO.setDeviceClassID(list.getDevice().getDeviceClass().getId());
                    areaClassFlowDTO.setFlowType(flowType);
                    areaClassFlowDTOs.add(areaClassFlowDTO);
                }
            }
        }
        return areaClassFlowDTOs;
    }

    /**
     * 组合申购区域设备类别流程列表
     * 
     * @param purchaseDevices
     * @param flowType
     * @return
     */
    private Set<AreaClassFlowDTO> buildPurchaseAreaClassFlows(Set<PurchaseDevice> purchaseDevices, String areaCode,
            int flowType) {
        Set<AreaClassFlowDTO> areaClassFlowDTOs = new HashSet<AreaClassFlowDTO>();
        if (purchaseDevices != null) {
            for (PurchaseDevice list : purchaseDevices) {
                if (list != null) {
                    AreaClassFlowDTO areaClassFlowDTO = new AreaClassFlowDTO();
                    areaClassFlowDTO.setAreaCode(areaCode);
                    areaClassFlowDTO.setDeviceClassID(list.getDeviceClass() == null ? null : list.getDeviceClass()
                            .getId());
                    areaClassFlowDTO.setFlowType(flowType);
                    areaClassFlowDTOs.add(areaClassFlowDTO);
                }
            }
        }
        return areaClassFlowDTOs;
    }

    /**
     * 根据流程类别获取对应的流程key
     * 
     * @param areaClassFlowDTOs
     * @param flowType
     * @return
     * @throws OaException
     */
    private String getFlowKeyByType(Set<AreaClassFlowDTO> areaClassFlowDTOs, int flowType) throws OaException {
        String flowKey = null;
        Map<String, String> flowMap = new HashMap<String, String>();
        List<AreaDeviceCfg> cfgs = deviceAreaConfigBiz.queryAllAreaDevices();
        if (cfgs != null) {
            for (AreaDeviceCfg cfg_ : cfgs) {
                if (cfg_ != null) {
                    if (flowType == AreaDeviceCfg.FLOW_TYPE_DEVPURCHASE) {
                        // 申购
                        flowMap.put(cfg_.getAreaCode() + cfg_.getDeviceClass().getId() + flowType,
                                cfg_.getPurchaseFlowKey());
                    } else if (flowType == AreaDeviceCfg.FLOW_TYPE_DEVUSE) {
                        // 领用
                        flowMap.put(cfg_.getAreaCode() + cfg_.getDeviceClass().getId() + flowType,
                                cfg_.getUseApplyFlowKey());
                    } else if (flowType == AreaDeviceCfg.FLOW_TYPE_DEVALLOT) {
                        // 调拨
                        flowMap.put(cfg_.getAreaCode() + cfg_.getDeviceClass().getId() + flowType,
                                cfg_.getAllocateFlowKey());
                    } else if (flowType == AreaDeviceCfg.FLOW_TYPE_DEVSCRAP) {
                        // 报废
                        flowMap.put(cfg_.getAreaCode() + cfg_.getDeviceClass().getId() + flowType,
                                cfg_.getDiscardFlowKey());
                    } else if (flowType == AreaDeviceCfg.FLOW_TYPE_DEVDIMISSION) {
                        // 离职
                        flowMap.put(cfg_.getAreaCode() + cfg_.getDeviceClass().getId() + flowType,
                                cfg_.getDimissionFlowKey());
                    }
                }
            }
        }

        Set<String> flowKeys = new HashSet<String>();
        if (areaClassFlowDTOs != null) {
            for (AreaClassFlowDTO list : areaClassFlowDTOs) {
                if (list != null) {
                    String key = list.getAreaCode() + list.getDeviceClassID() + flowType;
                    flowKey = flowMap.get(key);
                    if (areaClassFlowDTOs.size() == 1) {
                        break;
                    }
                    flowKeys.add(flowMap.get(key));
                    if (flowKeys.size() > 1) {
                        // 所选择的设备的流程不一致
                        throw new OaException("所选择的设备的" + AreaDeviceCfg.flowMap.get(flowType) + "不一致");
                    }
                }
            }
        }
        return flowKey;
    }

    /**
     * 判断用户是否已领用了不同种类的主设备
     * 
     * @return
     * @throws OaException
     */
    public void getUseMainDevCount(Set<DevPurchaseList> devPurchaseLists, String accountID, int formType,
            Set<String> deleteDeviceIDs) throws OaException {
        String strOptType = null;
        if (formType == 0) {
            strOptType = "领用";
        } else if (formType == 1) {
            strOptType = "申购";
        } else if (formType == 2) {
            strOptType = "调拨";
        }
        String strSplit = ",";
        if (devPurchaseLists == null || devPurchaseLists.size() == 0) {
            throw new IllegalArgumentException("参数错误");
        }
        Map<String, AreaDeviceCfg> mapCfg = new HashMap<String, AreaDeviceCfg>();
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.queryAllAreaDevices();// 获取所有区域设备配置
        if (areaDeviceCfgs == null || areaDeviceCfgs.size() == 0) {
            throw new OaException("区域设备配置数据不存在！");
        }
        String mainDevClassTitle = getMainDevClassTitle(devPurchaseLists);
        for (AreaDeviceCfg cfg : areaDeviceCfgs) {
            String key = cfg.getAreaCode() + strSplit + cfg.getDeviceClass().getId();// 以区域代码和设备类别作为key
            mapCfg.put(key, cfg);
        }

        // 追加本次新增的
        Set<String> mainDevKeys = new HashSet<String>();
        for (DevPurchaseList devPurchaseList : devPurchaseLists) {
            if (devPurchaseList != null && devPurchaseList.getDevice() != null) {
                Device d = devPurchaseList.getDevice();
                String deviceClassID = d.getDeviceClass() == null ? null : d.getDeviceClass().getId();
                String key = d.getAreaCode() + strSplit + deviceClassID;
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(d.getAreaCode(), d.getDeviceClass().getId(),
                        areaDeviceCfgs);
                if (mainDevFlag) {
                    if (!mainDevKeys.contains(key)) {
                        mainDevKeys.add(key);
                    }
                }
            }
        }
        Set<String> mainDevKeys_ = mainDevKeys;
        // 追加已在流程中的
        getMainDevCountInFlow(accountID, formType, mainDevKeys, deleteDeviceIDs, mainDevClassTitle, true);

        // 将已领用的设备中是主设备的放到mainDevKeys中
        List<Device> devices = deviceDAO.findUseDevicesByApplicantID(accountID);// 已领用的设备
        if (devices != null) {
            for (Device d : devices) {
                String key = d.getAreaCode() + strSplit + d.getDeviceClass().getId();
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(d.getAreaCode(), d.getDeviceClass().getId(),
                        areaDeviceCfgs);
                if (mainDevFlag) {
                    if (!mainDevKeys.contains(key)) {
                        mainDevKeys.add(key);
                    }
                    mainDevKeys.removeAll(mainDevKeys_);
                    if (mainDevKeys.size() > 1) {
                        String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，你已领用的编号为“" + d.getDeviceNO()
                                + "”的设备是属于“" + d.getAreaName() + d.getDeviceClass().getName() + "”类别的主设备，" + "不能再"
                                + strOptType + "其它种类的主设备！";
                        throw new OaException(strPrompt);
                    }
                }
            }
        }
    }

    /**
     * 获取领用清单中哪些类别是主设备。
     * 
     * @param devPurchaseLists 领用清单列表
     * @return 返回是主设备的类别，格式如下：福州笔记本、厦门台式机。 如果devPurchaseLists=null或者领用清单的设备其类别都不是主设备则返回""。
     */
    private String getMainDevClassTitle(Set<DevPurchaseList> devPurchaseLists) {
        if (devPurchaseLists == null) {
            return "";
        }
        String strPrompt = "";
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.queryAllAreaDevices();// 获取所有区域设备配置
        for (DevPurchaseList devPurchaseList : devPurchaseLists) {
            if (devPurchaseList != null) {
                Device d = devPurchaseList.getDevice();
                String deviceClassID = d.getDeviceClass() == null ? null : d.getDeviceClass().getId();
                String areaCode = d.getAreaCode();
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(areaCode, deviceClassID, areaDeviceCfgs);
                if (mainDevFlag) {
                    if (!"".equals(strPrompt)) {
                        strPrompt += "、";
                    }
                    strPrompt += "“" + d.getAreaName() + d.getDeviceClass().getName() + "”";
                }
            }
        }
        return strPrompt;
    }

    /**
     * 获取领用清单中哪些类别是主设备。
     * 
     * @param devPurchaseLists 领用清单列表
     * @return 返回是主设备的类别，格式如下：福州笔记本、厦门台式机。 如果devPurchaseLists=null或者领用清单的设备其类别都不是主设备则返回""。
     */
    private String getMainDevClassTitleInPurchase(Set<PurchaseDevice> purchaseDevices) {
        if (purchaseDevices == null) {
            return "";
        }
        String strPrompt = "";
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.queryAllAreaDevices();// 获取所有区域设备配置
        for (PurchaseDevice purchaseDevice : purchaseDevices) {
            if (purchaseDevice != null) {
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(purchaseDevice.getBelongtoAreaCode(),
                        purchaseDevice.getDeviceClass().getId(), areaDeviceCfgs);
                if (mainDevFlag) {
                    if (!"".equals(strPrompt)) {
                        strPrompt += "、";
                    }
                    strPrompt += "“" + purchaseDevice.getBelongtoAreaName() + purchaseDevice.getDeviceClass().getName()
                            + "”";
                }
            }
        }
        return strPrompt;
    }

    /**
     * 根据指定的区域和设备类别从区域设备配置中查找该指定区域设备类别是否是主设备
     * 
     * @return
     */
    public boolean getMainDevFlagInAreaDeviceCfg(String areaCode, String deviceClassID,
            List<AreaDeviceCfg> areaDeviceCfgs) {
        if (areaCode == null || deviceClassID == null || areaDeviceCfgs == null) {
            throw new IllegalArgumentException("参数错误");

        }
        for (AreaDeviceCfg areaDeviceCfg : areaDeviceCfgs) {
            if (areaDeviceCfg != null && deviceClassID.equals(areaDeviceCfg.getDeviceClass().getId())
                    && areaCode.equals(areaDeviceCfg.getAreaCode())) {
                return areaDeviceCfg.getMainDevFlag();
            }
        }
        return false;
    }

    /**
     * 申购：判断用户是否已领用了不同种类的主设备
     * 
     * @return
     * @throws OaException
     */
    public void getPurchaseMainDevCount(Set<PurchaseDevice> devPurchaseLists, String accountID, int formType,
            Set<String> deleteDeviceIDs, boolean userFlag) throws OaException {
        String mainDevClassTitle = getMainDevClassTitleInPurchase(devPurchaseLists);
        if (mainDevClassTitle == "" || mainDevClassTitle == null) {
            return;
        }
        String strSplit = ",";
        if (devPurchaseLists == null || devPurchaseLists.size() == 0) {
            throw new IllegalArgumentException("参数错误");
        }

        Map<String, AreaDeviceCfg> mapCfg = new HashMap<String, AreaDeviceCfg>();
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.queryAllAreaDevices();// 获取所有区域设备配置
        if (areaDeviceCfgs == null || areaDeviceCfgs.size() == 0) {
            throw new OaException("区域设备配置数据不存在！");
        }
        for (AreaDeviceCfg cfg : areaDeviceCfgs) {
            String key = cfg.getAreaCode() + strSplit + cfg.getDeviceClass().getId();// 以区域代码和设备类别作为key
            mapCfg.put(key, cfg);
        }

        Set<String> mainDevKeys = new HashSet<String>();
        String mapKey = null;
        // 追加本次新增的
        for (PurchaseDevice devPurchaseList : devPurchaseLists) {
            if (devPurchaseList != null) {
                String deviceClassID = devPurchaseList.getDeviceClass() == null ? null : devPurchaseList
                        .getDeviceClass().getId();
                String areaCode = devPurchaseList.getBelongtoAreaCode();
                String key = areaCode + strSplit + deviceClassID;
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(areaCode, deviceClassID, areaDeviceCfgs);
                if (mainDevFlag) {
                    // 主设备
                    mapKey = key;
                    if (!mainDevKeys.contains(key)) {
                        mainDevKeys.add(key);
                    }
                }
            }
        }
        Set<String> mainDevKeys_ = new HashSet<String>();
        mainDevKeys = new HashSet<String>();
        // 追加已在流程中的
        getMainDevCountInFlow(accountID, formType, mainDevKeys, deleteDeviceIDs, mainDevClassTitle, userFlag);

        // 将已领用的设备中是主设备的放到mainDevKeys中
        List<Device> devices = deviceDAO.findUseDevicesByApplicantID(accountID);// 已领用的设备
        if (devices != null) {
            for (Device d : devices) {
                String key = d.getAreaCode() + strSplit + d.getDeviceClass().getId();
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(d.getAreaCode(), d.getDeviceClass().getId(),
                        areaDeviceCfgs);
                if (mainDevFlag) {
                    if (!key.equals(mapKey)) {
                        if (mainDevKeys != null && !mainDevKeys.contains(key)) {
                            mainDevKeys.add(key);
                        } else {
                            mainDevKeys.add(key);
                        }
                    }

                    if (!key.equals(mapKey) && !mainDevKeys_.contains(key)) {
                        mainDevKeys_.add(key);
                    }
                    if (!userFlag) {
                        if (mainDevKeys_.size() > 0) {
                            String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，" + (userFlag ? "您" : "该员工")
                                    + "已领用的编号为“" + d.getDeviceNO() + "”的设备是属于“" + d.getAreaName()
                                    + d.getDeviceClass().getName() + "”类别的主设备，" + "不能再申购其它种类的主设备！";
                            throw new OaException(strPrompt);
                        }
                    } else {
                        if (mainDevKeys.size() > 0) {
                            String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，" + (userFlag ? "您" : "该员工")
                                    + "已领用的编号为“" + d.getDeviceNO() + "”的设备是属于“" + d.getAreaName()
                                    + d.getDeviceClass().getName() + "”类别的主设备，" + "不能再申购其它种类的主设备！";
                            throw new OaException(strPrompt);
                        }
                    }

                }
            }
        }
    }

    /**
     * 获取申购中的设备相关用途的次数Map。key是区域编码,设备类别ID,用途的组合字符串；value是次数
     * 
     * @throws OaException
     * 
     */
    private Set<String> getMainDevCountInFlow(String accountID, int formType, Set<String> mainDevKeys,
            Set<String> deleteDeviceIDs, String mainDevClassTitle, boolean userFlag) throws OaException {
        String strOptType = null;
        mainDevClassTitle = (mainDevClassTitle == null || "".equals(mainDevClassTitle)) ? "" : "系统已将"
                + mainDevClassTitle + "设为主设备，";
        if (formType == 0) {
            strOptType = "领用";
        } else if (formType == 1) {
            strOptType = "申购";
        } else if (formType == 2) {
            strOptType = "调拨";
        }
        if (mainDevKeys == null) {
            mainDevKeys = new HashSet<String>();
        }
        Set<String> mainDevKeys_ = mainDevKeys;
        String strSplit = ",";
        Map<String, AreaDeviceCfg> mapCfg = new HashMap<String, AreaDeviceCfg>();
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.queryAllAreaDevices();// 获取所有区域设备配置
        if (areaDeviceCfgs == null || areaDeviceCfgs.size() == 0) {
            throw new OaException("区域设备配置数据不存在！");
        }
        for (AreaDeviceCfg cfg : areaDeviceCfgs) {
            String key = cfg.getAreaCode() + strSplit + cfg.getDeviceClass().getId();// 以区域代码和设备类别作为key
            mapCfg.put(key, cfg);
        }

        if (mainDevKeys != null && mainDevKeys.size() > 0) {
            // 获取申购中的
            List<DevPurchaseForm> devPurchaseForms = devUseApplyFormDAO.findDealingFormByApplicantID(accountID,
                    DevPurchaseForm.APPLY_TYPE_PURCHASE);
            if (devPurchaseForms != null && devPurchaseForms.size() > 0) {
                for (DevPurchaseForm form : devPurchaseForms) {
                    if (form.getPurchaseDevices() != null && form.getPurchaseDevices().size() > 0) {
                        for (PurchaseDevice d : form.getPurchaseDevices()) {
                            if (d != null
                                    && (deleteDeviceIDs == null || !deleteDeviceIDs.contains(d.getBelongtoAreaCode()
                                            + "," + d.getDeviceClass().getId()))) {
                                String key = d.getBelongtoAreaCode() + strSplit + d.getDeviceClass().getId();
                                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(d.getBelongtoAreaCode(), d
                                        .getDeviceClass().getId(), areaDeviceCfgs);
                                if (mainDevFlag) {
                                    // 主设备
                                    // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                                    if (!mainDevKeys.contains(key)) {
                                        mainDevKeys.add(key);
                                    }
                                    // if(!userFlag){
                                    mainDevKeys.removeAll(mainDevKeys_);
                                    // }
                                    if (mainDevKeys.size() > 0) {
                                        String strPrompt = mainDevClassTitle + (userFlag ? "您" : "该员工") + "申购的“"
                                                + d.getBelongtoAreaName() + d.getDeviceClass().getName()
                                                + "”类别的主设备已在审批中，不能再" + strOptType + "其它种类的主设备！";
                                        throw new OaException(strPrompt);
                                    }
                                    // }
                                }
                            }
                        }
                    }
                }
            }

            // 获取领用中的
            devPurchaseForms = devUseApplyFormDAO.findDealingFormByApplicantID(accountID,
                    DevPurchaseForm.APPLY_TYPE_USE);
            if (devPurchaseForms != null && devPurchaseForms.size() > 0) {
                for (DevPurchaseForm form : devPurchaseForms) {
                    if (form != null && form.getDevPurchaseLists() != null && form.getDevPurchaseLists().size() > 0) {
                        for (DevPurchaseList list : form.getDevPurchaseLists()) {
                            if (list != null && list.getDevice() != null
                                    && (deleteDeviceIDs == null || !deleteDeviceIDs.contains(list.getDevice().getId()))) {// 去除掉已删除的
                                String key = list.getDevice().getAreaCode() + strSplit
                                        + list.getDevice().getDeviceClass().getId();
                                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(list.getDevice().getAreaCode(),
                                        list.getDevice().getDeviceClass().getId(), areaDeviceCfgs);
                                if (mainDevFlag) {
                                    // 主设备
                                    // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                                    if (!mainDevKeys.contains(key)) {
                                        mainDevKeys.add(key);
                                    }
                                    // if(!userFlag){
                                    mainDevKeys.removeAll(mainDevKeys_);
                                    // }
                                    if (mainDevKeys.size() > 0) {
                                        String strPrompt = mainDevClassTitle + "你申请领用的编号为“"
                                                + list.getDevice().getDeviceNO() + "”的设备是属于“"
                                                + list.getDevice().getAreaName()
                                                + list.getDevice().getDeviceClass().getName() + "”类别的主设备，其已在审批中，"
                                                + "不能再" + strOptType + "其它种类的主设备！";
                                        throw new OaException(strPrompt);
                                    }
                                    // }
                                }
                            }
                        }
                    }
                }
            }

            // 获取调拨中的
            List<DevAllocateForm> devAllocateForms = devAllocateFormDAO.queryDealingAllocateForm(accountID);
            if (devAllocateForms != null && devAllocateForms.size() > 0) {
                for (DevAllocateForm form : devAllocateForms) {
                    if (form.getDevAllocateLists() != null && form.getDevAllocateLists().size() > 0) {
                        for (DevAllocateList list : form.getDevAllocateLists()) {
                            if (list != null && list.getDevice() != null
                                    && (deleteDeviceIDs == null || !deleteDeviceIDs.contains(list.getDevice().getId()))) {
                                String key = list.getDevice().getAreaCode() + strSplit
                                        + list.getDevice().getDeviceClass().getId();
                                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(list.getDevice().getAreaCode(),
                                        list.getDevice().getDeviceClass().getId(), areaDeviceCfgs);
                                if (mainDevFlag) {
                                    // 主设备
                                    // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                                    if (!mainDevKeys.contains(key)) {
                                        mainDevKeys.add(key);
                                    }
                                    // if(!userFlag){
                                    mainDevKeys.removeAll(mainDevKeys_);
                                    // }
                                    if (mainDevKeys.size() > 1) {
                                        String strPrompt = mainDevClassTitle + "调拨到你名下的编号为“"
                                                + list.getDevice().getDeviceNO() + "”的设备是属于“"
                                                + list.getDevice().getAreaName()
                                                + list.getDevice().getDeviceClass().getName() + "”类别的主设备，其已在审批中，"
                                                + "不能再" + strOptType + "其它种类的主设备！";
                                        throw new OaException(strPrompt);
                                    }
                                    // }
                                }
                            }
                        }
                    }
                }
            }
        }
        return mainDevKeys;
    }

    public List<DevPurchaseList> getArchDevUseListByDeviceID(String deviceID, Boolean archiveDateOrder) {
        return devUseApplyFormDAO.queryArchDevUseListByDeviceID(deviceID, archiveDateOrder);
    }

    public PurchaseDevice getArchPurchaseDevByDeviceID(String deviceID, Boolean archiveDateOrder) {
        return devUseApplyFormDAO.queryArchPurchaseDevByDeviceID(deviceID, archiveDateOrder);
    }

    public DevPurchaseForm txModifyDraftForm(String id, String applyGroupName, String deviceTypeCode,
            String deviceClass, String remark, String buyType, Double budgetMoney, int formType, String areaCode,
            String workAreaCode, Set<DevPurchaseList> devPurchaseLists, Set<PurchaseDevice> purchaseDevices,
            Set<PurchaseDevPurpose> purchaseDevPurposes, boolean isStartFlow, boolean validMainDevFlag)
            throws OaException {
        DevPurchaseForm form = getDevUseApplyFormById(id, false, false);
        String deviceClassId = null;
        String deviceClassName = null;
        if (deviceClass != null) {
            DeviceClass deviceClass_ = new DeviceClass();
            deviceClass_.setId(deviceClass);
            form.setPurchaseDeviceClass(deviceClass_);
        }
        form.setWorkAreaCode(workAreaCode);
        boolean flag = true;
        // 2013-05-24修改：增加设备用途str
        StringBuffer purposeSb = new StringBuffer();
        if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
            purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
            // 领用
            if (devPurchaseLists != null && devPurchaseLists.size() > 0) {
                for (DevPurchaseList list : devPurchaseLists) {
                    if (list != null) {
//                        boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(form.getAreaCode(), list
//                                .getDevice().getDeviceClass().getId(), list.getPurpose());
                        // 2013-05-24修改：增加设备用途str
                        purposeSb.append(list.getPurpose());
                        
                        PurchaseDevPurpose purchaseDevPurpose = new PurchaseDevPurpose();
                        purchaseDevPurpose.setManyTimeFlag(false);
                        purchaseDevPurpose.setPurpose(list.getPurpose());
                        purchaseDevPurposes.add(purchaseDevPurpose);
                        list.setDevPurchaseForm(form);// 设备主表单
                        if (flag && isStartFlow && list.getDevice().getDeviceClass() != null) {
                            deviceClassId = list.getDevice().getDeviceClass().getId();
                            if (list.getDevice().getDeviceClass().getName() != null) {
                                DeviceClass d = deviceClassDAO.findById(deviceClassId);
                                if (d != null) {
                                    deviceClassName = d.getName();
                                }

                            } else {
                                deviceClassName = list.getDevice().getDeviceClass().getName();
                            }
                            flag = false;
                        }

                    }
                }
            }
        } else {
            flag = true;
            // 申购
            if (purchaseDevices != null && purchaseDevices.size() > 0) {
                for (PurchaseDevice list : purchaseDevices) {
                    if (list != null) {
                        list.setDevPurchaseForm(form);// 设备主表单
                        if (flag && isStartFlow && list.getDeviceClass() != null) {
                            deviceClassId = list.getDeviceClass().getId();
                            if (list.getDeviceClass().getName() != null) {
                                DeviceClass d = deviceClassDAO.findById(deviceClassId);
                                if (d != null) {
                                    deviceClassName = d.getName();
                                }

                            } else {
                                deviceClassName = list.getDeviceClass().getName();
                            }
                            flag = false;
                        }
                    }
                }
            }
            form.getPurchaseDevPurposes().clear();
            if (purchaseDevPurposes != null && purchaseDevPurposes.size() > 0) {
                for (PurchaseDevPurpose list : purchaseDevPurposes) {
                    if (list != null) {
                        list.setDevPurchaseForm(form);// 设备主表单
                        purposeSb.append(list.getPurpose());
                    }
                }
                form.getPurchaseDevPurposes().addAll(purchaseDevPurposes);
            }
        }
        
        // 2013-05-24修改：增加设备用途str
        form.setPurposes(purposeSb.toString());
        
        if (form != null) {
            if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                // 先删除原先的
                if (form.getDevPurchaseLists() != null) {
                    Iterator<DevPurchaseList> devPurchaseListItr = form.getDevPurchaseLists().iterator();
                    while (devPurchaseListItr.hasNext()) {
                        DevPurchaseList list = devPurchaseListItr.next();
                        if (list != null) {
                            devPurchaseListItr.remove();
                            list.setDevPurchaseForm(null);
                            devUseApplyFormDAO.delete(list);
                        }
                    }
                }

                if (devPurchaseLists != null) {
                    // 追加新设备列表
                    form.getDevPurchaseLists().addAll(devPurchaseLists);
                }
            } else {
                // 申购
                // 先删除原先的
                if (form.getPurchaseDevices() != null) {
                    Iterator<PurchaseDevice> purchaseDevicesItr = form.getPurchaseDevices().iterator();
                    while (purchaseDevicesItr.hasNext()) {
                        PurchaseDevice list = purchaseDevicesItr.next();
                        if (list != null) {
                            purchaseDevicesItr.remove();
                            list.setDevPurchaseForm(null);
                            devUseApplyFormDAO.delete(list);
                        }
                    }
                }
                if (purchaseDevices != null) {
                    // 追加新设备列表
                    form.getPurchaseDevices().addAll(purchaseDevices);
                }
            }

            form.setDeviceType(deviceTypeCode);
            form.setApplyGroupName(applyGroupName);
            form.setApplyDate(new Date());// 申请时间
            form.setRemark(remark);
            form.setBuyType(buyType);
            form.setBudgetMoney(budgetMoney);
            form.setApplyType(formType);
            form.setFormStatus(DevDiscardForm.FORMSTATUS_UNPUBLISH);
            form.setAreaCode(areaCode);
            if (isStartFlow) {
                if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                    for (DevPurchaseList list : devPurchaseLists) {
                        if (list != null && list.getDevice() != null
                                && list.getDevice().getDeviceCurStatusInfo().getApproveType() != null) {
                            // 审批中
                            throw new OaException("编号为“"
                                    + list.getDevice().getDeviceNO()
                                    + "”的设备正处于"
                                    + DeviceCurStatusInfo.approveTypeMap.get(list.getDevice().getDeviceCurStatusInfo()
                                            .getApproveType()) + "，现不能领用！");
                        }
                    }
                    if (validMainDevFlag) {
                        getUseMainDevCount(devPurchaseLists, form.getApplicant(), 0, null);// 追加权限
                    }
                } else {
                    if (validMainDevFlag) {
                        getPurchaseMainDevCount(purchaseDevices, form.getApplicant(), formType, null, true);// 检测主设备领用次数
                    }
                }
                checkDeviceMsg(deviceTypeCode, areaCode, deviceClassId, deviceClassName, form.getApplicant(), formType,
                        purchaseDevPurposes, false, true);// 判断工作用途是否可以领用多台，包括已领用，申购、领用在审批中的
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int no = devUseApplyFormDAO.findCurrentYearFormNO(year);
                form.setApplyFormNO(++no);// 编号序列号
                form.setSequenceYear(year);// 编号年份

                // 保存设备的审批状态
                Set<AreaClassFlowDTO> areaClassFlowDTOs = new HashSet<AreaClassFlowDTO>();

                if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                    purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
                    Map<String, PurchaseDevPurpose> purposeTimes = new HashMap<String, PurchaseDevPurpose>();
                    for (DevPurchaseList list : form.getDevPurchaseLists()) {
                        if (list != null && list.getDevice() != null) {
                            PurchaseDevPurpose devPurposePurchase = new PurchaseDevPurpose();
                            devPurposePurchase.setPurpose(list.getPurpose());
                            DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                            statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_USE);
                            statusInfo.setStatusUptDate(form.getApplyDate());// 设备领用申请日期
                            statusInfo.setFormID(form.getId());
                            devUseApplyFormDAO.merge(statusInfo);
//                            boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(areaCode, list
//                                    .getDevice().getDeviceClass().getId(), list.getPurpose());
                            devPurposePurchase.setManyTimeFlag(false);
//                            if (!manyTimeFlag) {
                                String key = areaCode + "," + list.getDevice().getDeviceClass().getId();
                                if (purposeTimes != null && purposeTimes.get(key) != null) {
                                    PurchaseDevPurpose devPurpose = purposeTimes.get(key);
                                    devPurpose.setUseCount(devPurpose.getUseCount() + 1);
                                    purposeTimes.put(key, devPurpose);
                                } else {
                                    PurchaseDevPurpose devPurpose = new PurchaseDevPurpose();
                                    devPurpose.setPurpose(list.getPurpose());
                                    devPurpose.setDeviceClassName(list.getDevice().getDeviceClass().getName());
                                    devPurpose.setUseCount(1);
                                    purposeTimes.put(key, devPurpose);

                                }

//                            }
                            purchaseDevPurposes.add(devPurposePurchase);
                        }
                    }
                    if (purposeTimes != null) {
                        String msg = "";
                        for (String str : purposeTimes.keySet()) {
                            PurchaseDevPurpose devPurpose = purposeTimes.get(str);
                            if (devPurpose != null) {
                                if (devPurpose.getUseCount() > 1) {
                                    msg += "设备类别名称为【" + devPurpose.getDeviceClassName() + "】的设备只能领用一台\r";
                                }

                            }
                        }
                        if (msg != "") {
                            throw new OaException(msg);// 未配置领用、申购流程
                        }
                    }
                    areaClassFlowDTOs = buildUseAreaClassFlows(devPurchaseLists, AreaDeviceCfg.FLOW_TYPE_DEVUSE);
                } else {
                    areaClassFlowDTOs = buildPurchaseAreaClassFlows(purchaseDevices, areaCode,
                            AreaDeviceCfg.FLOW_TYPE_DEVPURCHASE);
                }
                String flowKey = getFlowKeyByType(areaClassFlowDTOs, AreaDeviceCfg.FLOW_TYPE_DEVUSE);// 领用和申购用的是同一个流程
                if (flowKey == null) {
                    throw new OaException("未配置" + DevPurchaseForm.applyTypeMap.get(formType) + "流程");// 未配置领用、申购流程
                }
                txStartFlow(form, flowKey, formType);
            } else {
                devUseApplyFormDAO.update(form);
            }
        }
        return form;
    }

    @Override
    public DevPurchaseForm txDraftmanAmend(String id, String applyGroupName, String remark, String buyType,
            Double budgetMoney, String workAreaCode, Set<DevPurchaseList> devPurchaseLists,
            Set<PurchaseDevice> purchaseDevices, Set<PurchaseDevPurpose> purchaseDevPurposes, boolean validMainDevFlag)
            throws OaException {
//        String oldFlowKey = null;
//        // 判断流程是否一致
//        Task task = taskDAO.findDealingTasksByFormID(id);
//        if (task != null) {
//            oldFlowKey = task.getFlowKey();
//        }

        String deviceClassId = null;
        String deviceClassName = null;
        boolean flag = true;
        Set<PurchaseDevPurpose> purchaseDevPurposeSet = new HashSet<PurchaseDevPurpose>();
        DevPurchaseForm form = getDevUseApplyFormById(id, false, false);
        Set<AreaClassFlowDTO> areaClassFlowDTOs = new HashSet<AreaClassFlowDTO>();
//        int flowType;
        if (form.getApplyType().intValue() == DevPurchaseForm.APPLY_TYPE_USE) {
            // 领用
            if (validMainDevFlag) {
                Set<String> deleteDeviceIDs = getUseDeleteDeviceIDs(devPurchaseLists, form.getDevPurchaseLists());
                getUseMainDevCount(devPurchaseLists, form.getApplicant(), 0, deleteDeviceIDs);// 追加权限
            }

//            flowType = AreaDeviceCfg.FLOW_TYPE_DEVUSE;
            if (purchaseDevices != null && purchaseDevices.size() > 0) {
                areaClassFlowDTOs = buildUseAreaClassFlows(devPurchaseLists, AreaDeviceCfg.FLOW_TYPE_DEVUSE);
            }
        } else {
            // 申购
            if (validMainDevFlag) {
                Set<String> deleteDeviceIDs = getPurchaseDeleteDeviceIDs(purchaseDevices, form.getPurchaseDevices());
                getPurchaseMainDevCount(purchaseDevices, form.getApplicant(), form.getApplyType().intValue(),
                        deleteDeviceIDs, true);// 检测主设备领用次数
            }

//            flowType = AreaDeviceCfg.FLOW_TYPE_DEVPURCHASE;
            areaClassFlowDTOs = buildPurchaseAreaClassFlows(purchaseDevices, form.getAreaCode(),
                    AreaDeviceCfg.FLOW_TYPE_DEVPURCHASE);
        }
        String newFlowKey = getFlowKeyByType(areaClassFlowDTOs, AreaDeviceCfg.FLOW_TYPE_DEVUSE);// 领用和申购用的是同一个流程
        if (purchaseDevices != null && purchaseDevices.size() > 0) {
            if (newFlowKey == null) {
                throw new OaException("未配置" + DevPurchaseForm.applyTypeMap.get(form.getApplyType()) + "流程");// 未配置领用、申购流程
            }
//            if (!newFlowKey.equals(oldFlowKey)) {
//                // 所选择的设备的流程与现在的流程不一致
//                throw new OaException("所选择的设备的" + AreaDeviceCfg.flowMap.get(flowType) + "不一致");
//            }
        }

        if (form.getApplyType().intValue() == DevPurchaseForm.APPLY_TYPE_USE) {
            // 领用
            if (devPurchaseLists != null && devPurchaseLists.size() > 0) {
                Set<DevPurchaseList> set = form.getDevPurchaseLists();
                Map<String, String> map = new HashMap<String, String>();
                if (set != null) {
                    for (DevPurchaseList devPurchaseList : set) {
                        map.put(devPurchaseList.getPurpose(), devPurchaseList.getPurpose());
                    }
                }
                form.getPurchaseDevPurposes().clear();
                for (DevPurchaseList list : devPurchaseLists) {
                    if (list != null) {
                        if (map != null) {
                            String str = map.get(list.getPurpose());
                            if (str == null) {
//                                boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(form.getAreaCode(),
//                                        list.getDevice().getDeviceClass().getId(), list.getPurpose());
                                PurchaseDevPurpose purchaseDevPurpose = new PurchaseDevPurpose();
                                purchaseDevPurpose.setManyTimeFlag(false);
                                purchaseDevPurpose.setPurpose(list.getPurpose());
                                purchaseDevPurposeSet.add(purchaseDevPurpose);
                            }

                        }
                        list.setDevPurchaseForm(form);// 设备主表单
                        if (flag) {
                            deviceClassId = list.getDevice().getDeviceClass().getId();
                            if (list.getDevice().getDeviceClass().getName() != null) {
                                DeviceClass d = deviceClassDAO.findById(deviceClassId);
                                if (d != null) {
                                    deviceClassName = d.getName();
                                }

                            } else {
                                deviceClassName = list.getDevice().getDeviceClass().getName();
                            }
                            flag = false;
                        }
                    }
                }
            }
        } else {
            // 申购
            flag = true;
            if (purchaseDevices != null && purchaseDevices.size() > 0) {
                for (PurchaseDevice list : purchaseDevices) {
                    if (list != null) {
                        list.setDevPurchaseForm(form);// 设备主表单
                        if (flag) {
                            deviceClassId = list.getDeviceClass().getId();
                            if (list.getDeviceClass().getName() != null) {
                                DeviceClass d = deviceClassDAO.findById(deviceClassId);
                                if (d != null) {
                                    deviceClassName = d.getName();
                                }

                            } else {
                                deviceClassName = list.getDeviceClass().getName();
                            }
                            flag = false;
                        }
                    }
                }
            }
            Set<PurchaseDevPurpose> set = form.getPurchaseDevPurposes();
            Map<String, String> map = new HashMap<String, String>();
            if (set != null) {
                for (PurchaseDevPurpose purchaseDevPurpose : set) {
                    map.put(purchaseDevPurpose.getPurpose(), purchaseDevPurpose.getPurpose());
                }
            }

            form.getPurchaseDevPurposes().clear();
            if (purchaseDevPurposes != null && purchaseDevPurposes.size() > 0) {

                for (PurchaseDevPurpose list : purchaseDevPurposes) {
                    if (list != null) {
                        if (map != null) {
                            String str = map.get(list.getPurpose());
                            if (str == null) {
                                purchaseDevPurposeSet.add(list);
                            }

                        }
                        list.setDevPurchaseForm(form);// 设备主表单
                    }
                }
                form.getPurchaseDevPurposes().addAll(purchaseDevPurposes);

            }

        }
        if (form != null) {
            if (form.getApplyType().intValue() == DevPurchaseForm.APPLY_TYPE_USE) {
                // 设备ID与DevPurchaseList组合的Map，用于对设备状态进行操作
                Map<String, DevPurchaseList> newDeviceListMap = new HashMap<String, DevPurchaseList>();
                if (devPurchaseLists != null) {
                    for (DevPurchaseList list : devPurchaseLists) {
                        if (list != null && list.getDevice() != null) {
                            newDeviceListMap.put(list.getDevice().getId(), list);
                        }
                    }
                }

                // 先删除原先的
                if (form.getDevPurchaseLists() != null) {
                    Iterator<DevPurchaseList> devPurchaseListItr = form.getDevPurchaseLists().iterator();
                    while (devPurchaseListItr.hasNext()) {
                        DevPurchaseList list = devPurchaseListItr.next();
                        if (list != null && list.getDevice() != null) {
                            if (list.getDevice().getId() != null
                                    && !newDeviceListMap.containsKey(list.getDevice().getId())) {
                                DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                                // 在新设备列表中不存在旧的设备ID，说明设备已被删除，要恢复旧单的设备的审批状态为不在审批中
                                statusInfo.setApproveType(null);
                                statusInfo.setFormID(null);
                                devUseApplyFormDAO.update(statusInfo);
                            } else {
                                newDeviceListMap.remove(list.getDevice().getId());
                            }
                        }
                        devPurchaseListItr.remove();
                        list.setDevPurchaseForm(null);
                        devUseApplyFormDAO.delete(list);
                    }
                }
                form.getDevPurchaseLists().clear();
                if (devPurchaseLists != null) {
                    // 追加新设备列表
                    // form.getDevPurchaseLists().addAll(devPurchaseLists);
                    // 保存新单的设备的审批状态。
                    if (form.getApplyType().intValue() == DevPurchaseForm.APPLY_TYPE_USE) {
                        Map<String, PurchaseDevPurpose> purposeTimes = new HashMap<String, PurchaseDevPurpose>();
                        purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
                        for (Entry<String, DevPurchaseList> list : newDeviceListMap.entrySet()) {
                            if (list.getValue() != null && list.getValue().getDevice() != null) {
                                DeviceCurStatusInfo statusInfo = list.getValue().getDevice().getDeviceCurStatusInfo();
                                statusInfo.setApproveType(DevPurchaseForm.APPLY_TYPE_USE);
                                statusInfo.setStatusUptDate(form.getApplyDate());// 设备领用申请日期
                                statusInfo.setFormID(form.getId());
                                devUseApplyFormDAO.merge(statusInfo);
                            }
                        }
                        // 判断设备用途是否可领用多台
                        for (DevPurchaseList devPurchaseList : devPurchaseLists) {
//                            boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(form.getAreaCode(),
//                                    devPurchaseList.getDevice().getDeviceClass().getId(), devPurchaseList.getPurpose());
                            PurchaseDevPurpose devPurposePurchase = new PurchaseDevPurpose();
                            devPurposePurchase.setPurpose(devPurchaseList.getPurpose());
                            devPurposePurchase.setManyTimeFlag(false);
                            purchaseDevPurposes.add(devPurposePurchase);
//                            if (!manyTimeFlag) {
                                String key = form.getAreaCode() + ","
                                        + devPurchaseList.getDevice().getDeviceClass().getId();
                                if (purposeTimes != null && purposeTimes.get(key) != null) {
                                    PurchaseDevPurpose devPurpose = purposeTimes.get(key);
                                    devPurpose.setUseCount(devPurpose.getUseCount() + 1);
                                    purposeTimes.put(key, devPurpose);
                                } else {
                                    PurchaseDevPurpose devPurpose = new PurchaseDevPurpose();
                                    devPurpose.setPurpose(devPurchaseList.getPurpose());
                                    devPurpose.setDeviceClassName(devPurchaseList.getDevice().getDeviceClass()
                                            .getName());
                                    devPurpose.setUseCount(1);
                                    purposeTimes.put(key, devPurpose);

                                }

//                            }
                        }
                        if (purposeTimes != null) {
                            String msg = "";
                            for (String str : purposeTimes.keySet()) {
                                PurchaseDevPurpose devPurpose = purposeTimes.get(str);
                                if (devPurpose != null) {
                                    if (devPurpose.getUseCount() > 1) {
                                        msg += "设备类别名称为【" + devPurpose.getDeviceClassName() + "】的设备只能领用一台\r";
                                    }

                                }
                            }
                            if (msg != "") {
                                throw new OaException(msg);// 未配置领用、申购流程
                            }
                        }
                    }
                }
            } else {
                // 申购
                // 先删除原先的
                if (form.getPurchaseDevices() != null) {
                    Iterator<PurchaseDevice> purchaseDevicesItr = form.getPurchaseDevices().iterator();
                    while (purchaseDevicesItr.hasNext()) {
                        PurchaseDevice list = purchaseDevicesItr.next();
                        if (list != null) {
                            purchaseDevicesItr.remove();
                            list.setDevPurchaseForm(null);
                            devUseApplyFormDAO.delete(list);
                        }
                    }
                }

            }
            form.setWorkAreaCode(workAreaCode);
            checkDeviceMsg(form.getDeviceType(), form.getAreaCode(), deviceClassId, deviceClassName,
                    form.getApplicant(), form.getApplyType(), purchaseDevPurposeSet, false, true);// 判断工作用途是否可以领用多台，包括已领用，申购、领用在审批中的
            form.setApplyGroupName(applyGroupName);
            form.setRemark(remark);
            form.setBuyType(buyType);
            form.setBudgetMoney(budgetMoney);
            if (devPurchaseLists != null && devPurchaseLists.size() > 0) {
                form.getDevPurchaseLists().addAll(devPurchaseLists);
            }
            if (purchaseDevices != null) {
                // 追加新设备列表
                form.getPurchaseDevices().addAll(purchaseDevices);
            }
            devUseApplyFormDAO.update(form);
        }
        return form;
    }

    private Set<String> getUseDeleteDeviceIDs(Set<DevPurchaseList> newDevPurchaseLists,
            Set<DevPurchaseList> oldDevPurchaseLists) {
        Set<String> deleteDeviceIDs = new HashSet<String>();
        Set<String> newDeviceIDs = new HashSet<String>();
        Set<String> oldDeviceIDs = new HashSet<String>();
        for (DevPurchaseList list : newDevPurchaseLists) {
            if (list != null && list.getDevice() != null) {
                newDeviceIDs.add(list.getDevice().getId());
            }
        }
        for (DevPurchaseList list : oldDevPurchaseLists) {
            if (list != null && list.getDevice() != null) {
                oldDeviceIDs.add(list.getDevice().getId());
            }
        }
        for (String oldDeviceID : oldDeviceIDs) {
            if (oldDeviceID != null && !newDeviceIDs.contains(oldDeviceID)) {
                deleteDeviceIDs.add(oldDeviceID);
            }
        }
        return deleteDeviceIDs;
    }

    private Set<String> getPurchaseDeleteDeviceIDs(Set<PurchaseDevice> newPurchaseDevices,
            Set<PurchaseDevice> oldPurchaseDevices) {
        Set<String> deleteDeviceIDs = new HashSet<String>();
        Set<String> newDeviceIDs = new HashSet<String>();
        Set<String> oldDeviceIDs = new HashSet<String>();
        for (PurchaseDevice list : newPurchaseDevices) {
            if (list != null) {
                newDeviceIDs.add(list.getBelongtoAreaCode() + "," + list.getDeviceClass().getId());
            }
        }
        for (PurchaseDevice list : oldPurchaseDevices) {
            if (list != null) {
                oldDeviceIDs.add(list.getBelongtoAreaCode() + "," + list.getDeviceClass().getId());
            }
        }
        for (String oldDeviceID : oldDeviceIDs) {
            if (oldDeviceID != null && !newDeviceIDs.contains(oldDeviceID)) {
                deleteDeviceIDs.add(oldDeviceID);
            }
        }
        return deleteDeviceIDs;
    }

    public void setDeviceBiz(IDeviceBiz deviceBiz) {
        this.deviceBiz = deviceBiz;
    }

    @Override
    public void txUpdatePurchaseForm(String id, Set<PurchaseDevice> purchaseDevices) throws OaException {
        if (id == null || id == "") {
            throw new IllegalArgumentException("参数异常：id为空");
        }
        DevPurchaseForm form = devUseApplyFormDAO.findById(id);
        Hibernate.initialize(form.getPurchaseDevices());
        Set<PurchaseDevice> oldPurchaseDevices = form.getPurchaseDevices();
        PurchaseDevice oldPurchaseDevice = null;
        if (oldPurchaseDevices != null) {
            for (PurchaseDevice purchaseDevice : oldPurchaseDevices) {
                oldPurchaseDevice = purchaseDevice;
                break;
            }
        }
        if (oldPurchaseDevice == null) {
            return;
        }
        form.getPurchaseDevices().clear();
        getPurchaseMainDevCount(purchaseDevices, form.getApplicant(), 1, null, false);
        for (PurchaseDevice purchaseDevice : purchaseDevices) {
            // purchaseDevice.setAreaCode(oldPurchaseDevice.getAreaCode());
            purchaseDevice.setBelongtoAreaCode(form.getAreaCode());
            purchaseDevice.setDeviceCfgDesc(oldPurchaseDevice.getDeviceCfgDesc());
            purchaseDevice.setPlanUseDate(oldPurchaseDevice.getPlanUseDate());
            purchaseDevice.setBuyType(oldPurchaseDevice.getBuyType());
            purchaseDevice.setDevPurchaseForm(form);
            // purchaseDevice.setPurpose(oldPurchaseDevice.getPurpose());
            Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
//            PurchaseDevPurpose purchaseDevPurpose = new PurchaseDevPurpose();
//            AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(
//                    purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass().getId());
//            for (DeviceAcptCountCfg deviceAcptCountCfg : areaDeviceCfg.getDeviceAcptCountCfgs()) {
//                if (deviceAcptCountCfg.getDevPurpose() != null
//                        && deviceAcptCountCfg.getDevPurpose().equals(purchaseDevice.getPurpose())) {
//                    purchaseDevPurpose.setPurpose(purchaseDevice.getPurpose());
//                    purchaseDevPurpose.setManyTimeFlag(deviceAcptCountCfg.getManyTimesFlag());
//                }
//            }
            checkDeviceMsg(form.getDeviceType(), purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass()
                    .getId(), purchaseDevice.getDeviceClass().getName(), form.getApplicant(), 1, purchaseDevPurposes,
                    false, false);
        }
        form.getPurchaseDevices().addAll(purchaseDevices);

        devUseApplyFormDAO.saveOrUpdate(form);

    }

    @Override
    public void txSaveOrUpdateDevValidateForms(String id, Set<PurchaseDevice> purchaseDevices, Boolean bntFlag)
            throws OaException {
        if (id == null || id == "" || purchaseDevices == null || purchaseDevices.size() == 0) {
            return;
        }
        DevPurchaseForm form = null;
        int num = 0;
        if (bntFlag) {
            form = devUseApplyFormDAO.findById(id);
            getPurchaseMainDevCount(purchaseDevices, form.getApplicant(), 1, null, false);
        }
        for (PurchaseDevice purchaseDevice : purchaseDevices) {
            DevValidateForm devValidateForm = null;
            PurchaseDevice purchaseDevice2 = devUseApplyFormDAO.findByPurchaseDeviceId(purchaseDevice.getId());
            Hibernate.initialize(purchaseDevice2.getDevValidateForm());
            if (bntFlag) {
                // 判断主设备
                Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
//                PurchaseDevPurpose purchaseDevPurpose = new PurchaseDevPurpose();
//                AreaDeviceCfg areaDeviceCfg = deviceAreaConfigBiz.getAreaDeviceCfgByClassId(
//                        purchaseDevice.getBelongtoAreaCode(), purchaseDevice.getDeviceClass().getId());
//                for (DeviceAcptCountCfg deviceAcptCountCfg : areaDeviceCfg.getDeviceAcptCountCfgs()) {
//                    if (deviceAcptCountCfg.getDevPurpose() != null
//                            && deviceAcptCountCfg.getDevPurpose().equals(purchaseDevice.getPurpose())) {
//                        purchaseDevPurpose.setPurpose(purchaseDevice.getPurpose());
//                        purchaseDevPurpose.setManyTimeFlag(deviceAcptCountCfg.getManyTimesFlag());
//                    }
//                }
                checkDeviceMsg(form.getDeviceType(), purchaseDevice.getBelongtoAreaCode(), purchaseDevice
                        .getDeviceClass().getId(), purchaseDevice.getDeviceClass().getName(), form.getApplicant(), 1,
                        purchaseDevPurposes, false, false);
            }
            Calendar calendar = Calendar.getInstance();
            if (purchaseDevice2.getDevValidateForm() != null) {
                devValidateForm = purchaseDevice2.getDevValidateForm();
                Hibernate.initialize(devValidateForm.getDeviceValiDetails());
                devValidateForm.getDeviceValiDetails().clear();
                devValidateForm.setPurchaseDevice(purchaseDevice2);
                for (DeviceValiDetail deviceValiDetail : purchaseDevice.getDevValidateForm().getDeviceValiDetails()) {
                    deviceValiDetail.setDevValidateForm(devValidateForm);
                }
                devValidateForm.getDeviceValiDetails().addAll(
                        purchaseDevice.getDevValidateForm().getDeviceValiDetails());

                devValidateForm.setValiDate(purchaseDevice.getDevValidateForm().getValiDate());
                devValidateForm.setAccountID(purchaseDevice.getDevValidateForm().getAccountID());
                devValidateForm.setValiType(DevValidateForm.VALITYPE_DIRECT_IN);
                devValidateForm.setRemark(purchaseDevice.getDevValidateForm().getRemark());
                // int no = devValidateFormDAO.findCurrentYearValidFormNO(DevValidateForm.VALITYPE_DIRECT_IN,
                // calendar.get(Calendar.YEAR));
                devValidateForm.setValidFormNO(purchaseDevice.getDevValidateForm().getValidFormNO());
                devValidateForm.setSequenceYear(purchaseDevice.getDevValidateForm().getSequenceYear());
            } else {
                devValidateForm = new DevValidateForm();
                devValidateForm.setValiDate(purchaseDevice.getDevValidateForm().getValiDate());
                devValidateForm.setAccountID(purchaseDevice.getDevValidateForm().getAccountID());
                for (DeviceValiDetail deviceValiDetail : purchaseDevice.getDevValidateForm().getDeviceValiDetails()) {
                    deviceValiDetail.setDevValidateForm(devValidateForm);
                }
                devValidateForm.setDeviceValiDetails(purchaseDevice.getDevValidateForm().getDeviceValiDetails());
                devValidateForm.setPurchaseDevice(purchaseDevice2);
                devValidateForm.setValiType(DevValidateForm.VALITYPE_DIRECT_IN);
                devValidateForm.setRemark(purchaseDevice.getDevValidateForm().getRemark());
                if (num == 0) {
                    num = devValidateFormDAO.findCurrentYearValidFormNO(DevValidateForm.VALITYPE_DIRECT_IN,
                            calendar.get(Calendar.YEAR)) + 1;
                } else {
                    ++num;
                }
                devValidateForm.setValidFormNO(num);
                devValidateForm.setSequenceYear(calendar.get(Calendar.YEAR));
            }
            devValidateFormDAO.saveOrUpdate(devValidateForm);
        }
    }

    private void initConfigList(PurchaseDevice d) {
        if (d != null) {
            StringBuffer strCfgs = new StringBuffer();
            StringBuffer strCfgsStr = new StringBuffer();
            if (!Hibernate.isInitialized(d.getDevicePropertyDetails())) {
                Hibernate.initialize(d.getDevicePropertyDetails());
            }
            Set<DevicePropertyDetail> devicePropertyDetails = d.getDevicePropertyDetails();
            if (devicePropertyDetails != null) {
                for (DevicePropertyDetail prop : devicePropertyDetails) {
                    if (prop != null) {
                        String pn = DataFormatUtil.noNullValue(prop.getPropertyName());
                        String pv = DataFormatUtil.noNullValue(prop.getPropertyValue());
                        strCfgs.append(pn + ":" + pv + "\r\n");
                        strCfgsStr.append(pn + ":" + pv + ";");
                    }
                }
            }
            d.setConfigListStr(strCfgsStr.toString());
            d.setConfigList(strCfgs.toString());
        }
    }

    public void setDevValidateFormDAO(IDevValidateFormDAO devValidateFormDAO) {
        this.devValidateFormDAO = devValidateFormDAO;
    }

    @Override
    public void txSaveOrUpdateDeviceForms(String id, Map<String, PurchaseDevice> map) throws OaException, Exception {
        if (id == null || map == null || map.size() == 0) {
            return;
        }
        try {
            DevPurchaseForm devPurchaseForm = devUseApplyFormDAO.findById(id);
            if (devPurchaseForm == null) {
                return;
            }
            Hibernate.initialize(devPurchaseForm.getPurchaseDevices());
            if (!devPurchaseForm.getPurchaseDevices().isEmpty()) {
                Map<String, PurchaseDevPurpose> purposeTimes = new HashMap<String, PurchaseDevPurpose>();
                for (PurchaseDevice purchaseDevice : devPurchaseForm.getPurchaseDevices()) {
                    PurchaseDevice newPurchaseDevice = map.get(purchaseDevice.getId());
                    if (newPurchaseDevice != null) {
                        // purchaseDevice.setPurpose(newPurchaseDevice.getPurpose());
                        purchaseDevice.setAreaCode(newPurchaseDevice.getAreaCode());
//                        boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(
//                                devPurchaseForm.getAreaCode(), purchaseDevice.getDeviceClass().getId(),
//                                newPurchaseDevice.getPurpose());
//                        if (!manyTimeFlag) {
                            String key = devPurchaseForm.getAreaCode() + "," + purchaseDevice.getDeviceClass().getId();
                            if (purposeTimes != null && purposeTimes.get(key) != null) {
                                PurchaseDevPurpose devPurpose = purposeTimes.get(key);
                                devPurpose.setUseCount(devPurpose.getUseCount() + 1);
                                purposeTimes.put(key, devPurpose);
                            } else {
                                PurchaseDevPurpose devPurpose = new PurchaseDevPurpose();
                                devPurpose.setPurpose(newPurchaseDevice.getPurpose());
                                devPurpose.setDeviceClassName(purchaseDevice.getDeviceClass().getName());
                                devPurpose.setUseCount(1);
                                purposeTimes.put(key, devPurpose);

                            }

//                        }
                    }

                }
                if (purposeTimes != null) {
                    String msg = "";
                    for (String str : purposeTimes.keySet()) {
                        PurchaseDevPurpose devPurpose = purposeTimes.get(str);
                        if (devPurpose != null) {
                            if (devPurpose.getUseCount() > 1) {
                                msg += "设备类别名称为【" + devPurpose.getDeviceClassName() + "】的设备只能领用一台\r";
                            }

                        }
                    }
                    if (msg != "") {
                        throw new OaException(msg);// 未配置领用、申购流程
                    }
                }
                // getDevPurchaseCount(devPurchaseForm.getPurchaseDevices(),
                // devPurchaseForm.getApplicant());//检测设备相关用途的领用次数
                devUseApplyFormDAO.saveOrUpdate(devPurchaseForm);
            }
        } catch (Exception e) {
            log.error("setDevValidateFormDAO faild", e);
            throw e;
        }
    }

    public void checkDeviceMsg(String deviceTypeCode, String areaCode, String deviceClassId, String deviceClassName,
            String userId, Integer formType, Set<PurchaseDevPurpose> purchaseDevPurposes, Boolean managerFlag,
            boolean userFlag) throws OaException {
        if (purchaseDevPurposes != null && purchaseDevPurposes.size() > 0) {
            String msg = "";
            String userStr = userFlag ? "您" : "该员工";
            String devMsg = userStr + "名下已有：";
            String useDealMsg = userStr + "已发起：";
            String alloctDealMsg = userStr + "已发起：";
            String purchaseDealMsg = userStr + "已发起：";
            boolean alloct = false;
            boolean devFlag = false;
            boolean useDealFlag = false;
            boolean purchaseDealFlag = false;
            for (PurchaseDevPurpose list : purchaseDevPurposes) {
                if (list != null) {
                    if (list.getManyTimeFlag() == null || !list.getManyTimeFlag()) {
                        Map<String, String> map = deviceBiz.getCheckDevicePurchase(deviceTypeCode, areaCode,
                                deviceClassId, deviceClassName, list.getPurpose(), userId, list.getManyTimeFlag(),
                                formType, managerFlag);
                        if (map != null) {

                            String devMsg_ = map.get(list.getPurpose() + "_-1");
                            if (devMsg_ != null && devMsg_ != "") {
                                devMsg += devMsg_;
                                devFlag = true;
                            }
                            String alloMsg_ = map.get(list.getPurpose() + "_2");
                            if (alloMsg_ != null && alloMsg_ != "") {
                                alloctDealMsg += alloMsg_;
                                alloct = true;
                            }
                            String useDealMsg_ = map.get(list.getPurpose() + "_0");
                            if (useDealMsg_ != null && useDealMsg_ != "") {
                                useDealMsg += useDealMsg_;
                                useDealFlag = true;
                            }
                            String purchaseDealMsg_ = map.get(list.getPurpose() + "_1");
                            if (purchaseDealMsg_ != null && purchaseDealMsg_ != "") {
                                purchaseDealMsg += purchaseDealMsg_;
                                purchaseDealFlag = true;
                            }
                        }
                    }

                }
            }
            String formStr = "";
            if (!managerFlag) {
                if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                    formStr = "领用流程";
                } else if (formType == DevPurchaseForm.APPLY_TYPE_PURCHASE) {
                    formStr = "申购流程";
                } else if (formType == DevPurchaseForm.APPLY_TYPE_ALLOT) {
                    formStr = "调拨流程";
                }
                if (devFlag) {
                    msg += devMsg + "无法继续领用;\r";
                }
                if (useDealFlag) {
                    msg += useDealMsg + "无法发起" + formStr + ";\r";
                }
                if (alloct) {
                    msg += alloctDealMsg + "不能进行调拨;\r";
                }
                if (purchaseDealFlag) {
                    msg += purchaseDealMsg + "无法发起" + formStr + ";\r";
                }
            } else {
                if (formType == DevPurchaseForm.APPLY_TYPE_USE) {
                    formStr = "领用";
                } else if (formType == DevPurchaseForm.APPLY_TYPE_ALLOT) {
                    formStr = "调拨";
                }
                if (devFlag) {
                    msg += devMsg + "无法继续领用;\r";
                }
                if (useDealFlag) {
                    msg += useDealMsg + "不能进行" + formStr + "操作;\r";
                }
                if (purchaseDealFlag) {
                    msg += purchaseDealMsg + "不能进行" + formStr + "操作;\r";
                }
            }

            if (msg != "" && msg.length() > 0) {
                throw new OaException(msg);
            }

        }
    }

    @Override
    public List<PurchaseDevice> getDeviceDealPurchaseByPurpose(String deviceType, String areaCode, String deviceClass,
            String userId) {
        return devUseApplyFormDAO.getDeviceDealPurchaseByPurpose(deviceType, areaCode, deviceClass, userId);
    }

    @Override
    public List<DevPurchaseList> getDeviceDealUseByPurpose(String deviceType, String areaCode, String deviceClass,
            String userId) {
        return devUseApplyFormDAO.getDeviceDealUseByPurpose(deviceType, areaCode, deviceClass, userId);
    }

    @Override
    public void txUpdatePurchaseDevices(Set<PurchaseDevice> purchaseDevices) {
        if (purchaseDevices == null || purchaseDevices.size() == 0) {
            return;
        }
        for (PurchaseDevice purchaseDevice : purchaseDevices) {
            PurchaseDevice oldPurchaseDevice = devUseApplyFormDAO.findByPurchaseDeviceId(purchaseDevice.getId());
            if (oldPurchaseDevice != null) {
                oldPurchaseDevice.setDeductFlag(purchaseDevice.getDeductFlag());
                oldPurchaseDevice.setDeductMoney(purchaseDevice.getDeductMoney());
                Hibernate.initialize(oldPurchaseDevice.getDevPurchaseForm());
                Device device = deviceBiz.getDeviceById(oldPurchaseDevice.getDeviceID());
                DeviceCurStatusInfo deviceCurStatusInfo = device.getDeviceCurStatusInfo();
                // 设备回写是否扣款和扣款金额
                if (oldPurchaseDevice.getDeviceID() != null && purchaseDevice.getDeductFlag()) {
                    device.setDeductFlag(purchaseDevice.getDeductFlag());
                    device.setDeductMoney(purchaseDevice.getDeductMoney());
                    devUseApplyFormDAO.saveOrUpdate(device);
                }
                deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                deviceCurStatusInfo.setStatusUptDate(new Date());
                deviceCurStatusInfo.setOwner(oldPurchaseDevice.getDevPurchaseForm().getApplicant());// 设置领用人
                deviceCurStatusInfo.setGroupName(oldPurchaseDevice.getDevPurchaseForm().getApplyGroupName());
                deviceCurStatusInfo.setApproveType(null);
                deviceCurStatusInfo.setFormID(null);
                devUseApplyFormDAO.saveOrUpdate(deviceCurStatusInfo);
                devValidateFormDAO.saveOrUpdate(oldPurchaseDevice);
            }

        }
    }

    public PurchaseDevice getByPurchaseDeviceId(String purchaseDeviceID) {
        return devUseApplyFormDAO.findByPurchaseDeviceId(purchaseDeviceID);
    }
}
