package org.eapp.oa.device.blo.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eapp.oa.device.blo.IDevValidateFormBiz;
import org.eapp.oa.device.blo.IDeviceAllocateBiz;
import org.eapp.oa.device.blo.IDeviceApplyBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.dao.IDevAllocateFormDAO;
import org.eapp.oa.device.dao.IDevUseApplyFormDAO;
import org.eapp.oa.device.dao.IDeviceClassDAO;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.dto.AreaClassFlowDTO;
import org.eapp.oa.device.dto.DevAllocateQueryParameters;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevFlowApplyProcess;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.device.hbean.DevicePropertyDetail;
import org.eapp.oa.device.hbean.PurchaseDevPurpose;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysConstants;

public class DeviceAllocateBiz implements IDeviceAllocateBiz {

    private IDevAllocateFormDAO devAllocateFormDAO;
    private IDeviceDAO deviceDAO;
    private IDeviceClassDAO deviceClassDAO;
    private ITaskDAO taskDAO;
    private IDevValidateFormBiz devValidateFormBiz;
    private IDeviceBiz deviceBiz;
    private IDevUseApplyFormDAO devUseApplyFormDAO;
    private IDeviceAreaConfigBiz deviceAreaConfigBiz;
    private IDeviceApplyBiz deviceApplyBiz;

    public void setDevAllocateFormDAO(IDevAllocateFormDAO devAllocateFormDAO) {
        this.devAllocateFormDAO = devAllocateFormDAO;
    }

    public IDeviceDAO getDeviceDAO() {
        return deviceDAO;
    }

    public void setDeviceDAO(IDeviceDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    public IDeviceClassDAO getDeviceClassDAO() {
        return deviceClassDAO;
    }

    public void setDeviceClassDAO(IDeviceClassDAO deviceClassDAO) {
        this.deviceClassDAO = deviceClassDAO;
    }

    public ITaskDAO getTaskDAO() {
        return taskDAO;
    }

    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public IDevValidateFormBiz getDevValidateFormBiz() {
        return devValidateFormBiz;
    }

    public void setDevValidateFormBiz(IDevValidateFormBiz devValidateFormBiz) {
        this.devValidateFormBiz = devValidateFormBiz;
    }

    public void setDevUseApplyFormDAO(IDevUseApplyFormDAO devUseApplyFormDAO) {
        this.devUseApplyFormDAO = devUseApplyFormDAO;
    }

    public List<DevAllocateForm> queryAllocateForm(String userAccountId, int formStatus) {
        if (userAccountId == null) {
            return null;
        }
        return devAllocateFormDAO.findAllocateForm(userAccountId, formStatus);
    }

    public DevAllocateForm getDevAllocateFormById(String id, boolean initList, boolean initDevCfgList) {
        if (id == null) {
            return null;
        }
        DevAllocateForm form = devAllocateFormDAO.findById(id);
        if (initList) {
            if (form != null && form.getDevAllocateLists() != null && form.getDevAllocateLists().size() > 0) {
                for (DevAllocateList list : form.getDevAllocateLists()) {
                    if (list != null) {
                        Hibernate.initialize(list.getDevice());
                        if (initDevCfgList) {
                            initConfigList(list.getDevice());
                        }
                    }
                }
            }
        }
        return form;
    }

    public void initConfigList(Device d) {
        if (d != null) {
            StringBuffer strCfgs = new StringBuffer();
            Set<DevicePropertyDetail> devicePropertyDetails = d.getDevicePropertyDetails();
            if (!Hibernate.isInitialized(devicePropertyDetails)) {
                Hibernate.initialize(devicePropertyDetails);
            }
            if (devicePropertyDetails != null) {
                for (DevicePropertyDetail prop : devicePropertyDetails) {
                    if (prop != null) {
                        String pn = DataFormatUtil.noNullValue(prop.getPropertyName());
                        String pv = DataFormatUtil.noNullValue(prop.getPropertyValue());
                        strCfgs.append(pn + ":" + pv + "\r\n");
                    }
                }
            }
            d.setConfigList(strCfgs.toString());
        }
    }

    public void setDeviceAreaConfigBiz(IDeviceAreaConfigBiz deviceAreaConfigBiz) {
        this.deviceAreaConfigBiz = deviceAreaConfigBiz;
    }

    public void txDelDevAllocateForm(String id) throws OaException {
        if (id == null) {
            return;
        }
        DevAllocateForm devAlcForm = devAllocateFormDAO.findById(id);
        if (devAlcForm == null) {
            return;
        }
        devAllocateFormDAO.delete(devAlcForm);
    }

    public void txStartFlow(DevAllocateForm devAlcForm, String flowKey) throws OaException {
        if (devAlcForm == null) {
            return;
        }
//        String toStorageFlag = DevAllocateForm.MOVE_TYPE_STORAGE.equals(devAlcForm.getMoveType()) ? "1" : "0";// 调拨入库标识
        if (flowKey == null) {// 不启动流程，直接发布
            devAlcForm.setPassed(true);
            devAlcForm.setArchiveDate(new Date());
            devAlcForm.setFormStatus(DevAllocateForm.FORMSTATUS_PUBLISH);
        } else {
            devAlcForm.setFormStatus(DevAllocateForm.FORMSTATUS_APPROVAL);
            WfmContext context = WfmConfiguration.getInstance().getWfmContext();
            try {
                // 设置流程上下文变量中，并启动流程
                Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
                // 把表单ID
                ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID,
                        ContextVariable.DATATYPE_STRING, devAlcForm.getId());
                contextVariables.put(cv.getName(), cv);
                // 发起人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getApplicant());
                contextVariables.put(cv.getName(), cv);

                // 部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getApplyGroupName());
                contextVariables.put(cv.getName(), cv);
                
                // 调出人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_OUTACCOUNTID, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getApplicant());
                contextVariables.put(cv.getName(), cv);

                // 调出部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_OUTGROUPNAME, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getApplyGroupName());
                contextVariables.put(cv.getName(), cv);

                // 调入人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_INACCOUNTID, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getInAccountID());
                contextVariables.put(cv.getName(), cv);

                // 调入部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_INGROUPNAME, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getInGroupName());
                contextVariables.put(cv.getName(), cv);

                // 表类类型
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMTYPE, ContextVariable.DATATYPE_STRING, null);
                contextVariables.put(cv.getName(), cv);
                // 区域
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_AREA, ContextVariable.DATATYPE_STRING, null);
                contextVariables.put(cv.getName(), cv);
                
                // 是否调拨入库
//                cv = new ContextVariable(SysConstants.FLOW_VARNAME_TOSTORAGEFLAG, ContextVariable.DATATYPE_STRING,
//                        toStorageFlag);
//                contextVariables.put(cv.getName(), cv);

                // 任务描述
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getApplicantDisplayName() + "的设备调拨单(" + devAlcForm.getFullFormNO() + ")");
                contextVariables.put(cv.getName(), cv);
                FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
                flowInstance.signal();
                context.save(flowInstance);
                // 设置表单视图的ID
                devAlcForm.setFlowInstanceID(flowInstance.getId());
            } catch (Exception e) {
                e.printStackTrace();
                context.rollback();
                throw new OaException(e.getMessage());
            } finally {
                context.close();
            }
        }
        devAllocateFormDAO.saveOrUpdate(devAlcForm);
    }

    public List<DevAllocateForm> queryDealingAllocateFrom(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        return devAllocateFormDAO.queryDealingAllocateForm(userId);
    }

    public List<Task> getEndedTasks(String formId) {
        DevAllocateForm allocateForm = devAllocateFormDAO.findById(formId);
        if (allocateForm == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        // return taskDAO.findEndedTasks(allocateForm.getFlowInstanceId());
        return null;
    }

    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String formId) {
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try {
            DevAllocateForm devAlcForm = devAllocateFormDAO.findById(formId);
            if (devAlcForm == null) {
                throw new IllegalArgumentException();
            }

            TaskInstance ti = context.getTaskInstance(taskInstanceId);
            if (ti == null) {
                throw new IllegalArgumentException();
            }
            ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID,
                    ContextVariable.DATATYPE_STRING, devAlcForm.getId());
            ti.getFlowInstance().addContextVariable(var);
            // 发起人
            var = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                    devAlcForm.getApplicant());
            ti.getFlowInstance().addContextVariable(var);

            // 调出人
            var = new ContextVariable(SysConstants.FLOW_VARNAME_OUTACCOUNTID, ContextVariable.DATATYPE_STRING,
                    devAlcForm.getApplicant());
            ti.getFlowInstance().addContextVariable(var);

            // 部门
            var = new ContextVariable(SysConstants.FLOW_VARNAME_OUTGROUPNAME, ContextVariable.DATATYPE_STRING,
                    devAlcForm.getApplyGroupName());
            ti.getFlowInstance().addContextVariable(var);

            if (devAlcForm.getMoveType() != null
                    && (devAlcForm.getMoveType().equals("ALLOT_DEPT") || devAlcForm.getMoveType()
                            .equals("ALLOT_INSIDE"))) {
                // 调入人
                var = new ContextVariable(SysConstants.FLOW_VARNAME_INACCOUNTID, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getInAccountID());
                ti.getFlowInstance().addContextVariable(var);

                // 部门
                var = new ContextVariable(SysConstants.FLOW_VARNAME_INGROUPNAME, ContextVariable.DATATYPE_STRING,
                        devAlcForm.getInGroupName());
                ti.getFlowInstance().addContextVariable(var);
            }
            // 任务描述
            var = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING, "调拨单");
            ti.getFlowInstance().addContextVariable(var);
            ti.setComment(comment);
            if (transitionName != null) {
                ti.end(transitionName);
            } else {
                ti.end();
            }
            context.save(ti);
        } catch (RuntimeException e) {
            e.printStackTrace();
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
    }

    public ListPage<DevAllocateForm> getArchAllocateForm(DevAllocateQueryParameters qp, String userAccountId) {
        if (userAccountId == null) {
            throw new IllegalArgumentException();
        }
        return devAllocateFormDAO.queryArchAllocateForm(qp, userAccountId);
    }

    public ListPage<DevAllocateForm> getQueryAllocateForm(DevAllocateQueryParameters qp) {
        return devAllocateFormDAO.queryAllocateForm(qp);
    }

    public void txSaveValidateForm(String allocateId, String valiId, String deviceID, int valiType, Date inDate,
            String accountID, Date valiDate, boolean isMoney, double deductMoney, String remark, String valiDetailStr) {
        DevValidateForm validateForm = devValidateFormBiz.txSaveDevValidateForm(valiId, deviceID, valiType, inDate,
                accountID, valiDate, isMoney, deductMoney, remark, valiDetailStr);
        DevAllocateForm allocateForm = devAllocateFormDAO.findById(allocateId);
        allocateForm.setDevValidateForm(validateForm);
        devAllocateFormDAO.saveOrUpdate(allocateForm);
    }

    public DevAllocateForm txPublishAllocateForm(String id) throws OaException {
        DevAllocateForm allocateForm = devAllocateFormDAO.findById(id);
        if (allocateForm != null && allocateForm.getDevAllocateLists() != null
                && allocateForm.getDevAllocateLists().size() > 0) {
            for (DevAllocateList list : allocateForm.getDevAllocateLists()) {
                if (list != null) {
                    Hibernate.initialize(list.getDevice());
                    Device device = list.getDevice();
                    Hibernate.initialize(list.getDevice().getDeviceCurStatusInfo());
                    if (device != null && device.getDeviceCurStatusInfo() != null) {
                        DeviceCurStatusInfo deviceCurStatusInfo = device.getDeviceCurStatusInfo();
                        if (DevAllocateForm.MOVE_TYPE_STORAGE.equals(allocateForm.getMoveType())) {
                            deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                            deviceCurStatusInfo.setStatusUptDate(new Date());
                            deviceCurStatusInfo.setGroupName("");
                            deviceCurStatusInfo.setPurpose(null);
                            deviceCurStatusInfo.setAreaCode(null);
                            deviceCurStatusInfo.setOwner("");
                        } else if (DevAllocateForm.MOVE_TYPE_BORROW.equals(allocateForm.getMoveType())) {
                            deviceCurStatusInfo.setGroupName(allocateForm.getInGroupName());
                            deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_BORROW);
                            deviceCurStatusInfo.setGroupName("");
                            deviceCurStatusInfo.setPurpose(null);
                            deviceCurStatusInfo.setAreaCode(null);
                            deviceCurStatusInfo.setOwner("");
                        } else {
                            deviceCurStatusInfo.setGroupName(allocateForm.getInGroupName());
                            deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                            deviceCurStatusInfo.setStatusUptDate(new Date());
                            deviceCurStatusInfo.setOwner(allocateForm.getInAccountID());
                            deviceCurStatusInfo.setPurpose(list.getPurpose());
                            deviceCurStatusInfo.setAreaCode(list.getAreaCode());
                        }
                        deviceCurStatusInfo.setApproveType(null);
                        deviceCurStatusInfo.setFormID(null);
                        devAllocateFormDAO.saveOrUpdate(deviceCurStatusInfo);
                    }
                }
            }
        }
        allocateForm.setFormStatus(DevAllocateForm.FORMSTATUS_PUBLISH);
        allocateForm.setPassed(true);
        allocateForm.setArchiveDate(new Date());
        // 生成领用单
        if (!(DevAllocateForm.MOVE_TYPE_BORROW.equals(allocateForm.getMoveType()) || DevAllocateForm.MOVE_TYPE_STORAGE
                .equals(allocateForm.getMoveType()))) {
            DevPurchaseForm devUseForm = txCreateDeviceUseFormFromAllot(allocateForm);
            allocateForm.setRefDevUseFormID(devUseForm.getId());// 设备关联的领用单ID
        }
        devAllocateFormDAO.saveOrUpdate(allocateForm);
        return allocateForm;
    }

    public void txCancellAllocateForm(String formId) throws OaException {
        if (formId == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        DevAllocateForm allocateForm = devAllocateFormDAO.findById(formId);
        if (allocateForm != null && allocateForm.getDevAllocateLists() != null
                && allocateForm.getDevAllocateLists().size() > 0) {
            for (DevAllocateList list : allocateForm.getDevAllocateLists()) {
                if (list != null) {
                    Hibernate.initialize(list.getDevice());
                    Device device = list.getDevice();
                    Hibernate.initialize(list.getDevice().getDeviceCurStatusInfo());
                    if (device != null && device.getDeviceCurStatusInfo() != null) {
                        DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
                        statusInfo.setApproveType(null);
                        statusInfo.setFormID(null);
                        devAllocateFormDAO.saveOrUpdate(statusInfo);
                    }
                }
            }
        }
        allocateForm.setPassed(false);
        allocateForm.setFormStatus(DevAllocateForm.FORMSTATUS_CANCELLATION);
        allocateForm.setArchiveDate(new Date());
        devAllocateFormDAO.saveOrUpdate(allocateForm);
    }

    @Override
    public DevAllocateForm txSaveDevAllocateForm(DevAllocateForm allocateForm, String deviceId,
            boolean validMainDevFlag) throws OaException {
        if (deviceId == null) {
            throw new IllegalArgumentException("非法参数：设备ID不能为空");
        }
        if (allocateForm == null) {
            throw new IllegalArgumentException("非法参数：设备调拨对象不能为空");
        }

        if (!(DevAllocateForm.MOVE_TYPE_STORAGE.equals(allocateForm.getMoveType()) || DevAllocateForm.MOVE_TYPE_BORROW
                .equals(allocateForm.getMoveType()))) {
            Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
            String deviceClassId = null;
            String deviceClassName = null;
            String areaCode = null;
            String deviceType = null;
            for (DevAllocateList list : allocateForm.getDevAllocateLists()) {
                // 保存设备的审批状态
                String deviceID = list.getDevice().getId();
                Device device = deviceDAO.findById(deviceID);
                if (device != null) {
                    Hibernate.initialize(device.getDeviceClass());
                    if (device.getDeviceClass() != null && deviceClassId == null) {
                        deviceClassId = device.getDeviceClass().getId();
                        deviceClassName = device.getDeviceClass().getName();
                        areaCode = device.getAreaCode();
                        deviceType = device.getDeviceType();
                    }
                }
                if (device.getDeviceCurStatusInfo() != null
                        && device.getDeviceCurStatusInfo().getDeviceCurStatus() == 1
                        && device.getDeviceCurStatusInfo().getOwner() != null) {
                    list.setDevStatusBef(DevAllocateList.STATUS_TYPE_USE);
                } else {
                    list.setDevStatusBef(DevAllocateList.STATUS_TYPE_NO_USE);
                }
                list.setDevice(device);
                // if(device.getDeviceCurStatusInfo().getApproveType()!=null){
                // // deviceBiz.txEndTaskByApproveType(device.getId(),
                // device.getDeviceCurStatusInfo().getApproveType());
                // }
                if (validMainDevFlag) {
                    getUseMainDevCount(allocateForm.getDevAllocateLists(), allocateForm.getInAccountID(), null);// 追加权限
                }
//                boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(areaCode, list.getDevice()
//                        .getDeviceClass().getId(), list.getPurpose());
//                if (!manyTimeFlag) {
                    PurchaseDevPurpose devPurposePurchase = new PurchaseDevPurpose();
                    devPurposePurchase.setPurpose(list.getPurpose());
//                    devPurposePurchase.setManyTimeFlag(manyTimeFlag);
                    purchaseDevPurposes.add(devPurposePurchase);
                    deviceBiz.txEndTaskByPurpose(deviceType, areaCode, deviceClassId, list.getPurpose(),
                            allocateForm.getApplicant());// 终止流程
//                }

            }
            deviceApplyBiz.checkDeviceMsg(deviceType, areaCode, deviceClassId, deviceClassName,
                    allocateForm.getApplicant(), DevPurchaseForm.APPLY_TYPE_ALLOT, purchaseDevPurposes, true, true);

        } else {
            for (DevAllocateList list : allocateForm.getDevAllocateLists()) {
                String deviceID = list.getDevice().getId();
                Device device = deviceDAO.findById(deviceID);
                // 保存设备的审批状态
                if (device.getDeviceCurStatusInfo() != null
                        && device.getDeviceCurStatusInfo().getDeviceCurStatus() == 1
                        && device.getDeviceCurStatusInfo().getOwner() != null) {
                    list.setDevStatusBef(DevAllocateList.STATUS_TYPE_USE);
                } else {
                    list.setDevStatusBef(DevAllocateList.STATUS_TYPE_NO_USE);
                }
            }
        }

        Device device = deviceDAO.findById(deviceId);
        if (device == null) {
            throw new OaException("未找到该设备");
        }
        if (device.getDeviceCurStatusInfo().getDeviceCurStatus() == DeviceCurStatusInfo.STATUS_SCRAP_DISPOSEED
                || device.getDeviceCurStatusInfo().getDeviceCurStatus() == DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE) {
            throw new OaException("该设备已经报废或报废未处理");
        }
        Calendar calendar = Calendar.getInstance();
        Integer no = devAllocateFormDAO.getMaxSequenceNo(calendar.get(Calendar.YEAR));
        allocateForm.setSequenceYear(calendar.get(Calendar.YEAR));
        allocateForm.setApplyFormNO(no + 1);
        Hibernate.initialize(device.getDeviceCurStatusInfo());
        DeviceCurStatusInfo deviceCurStatusInfo = device.getDeviceCurStatusInfo();
        deviceBiz.txEndTaskByApproveType(deviceId, deviceCurStatusInfo.getApproveType());
        if (DevAllocateForm.MOVE_TYPE_STORAGE.equals(allocateForm.getMoveType())) {
            if (deviceCurStatusInfo != null) {
                deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                deviceCurStatusInfo.setStatusUptDate(new Date());
                deviceCurStatusInfo.setGroupName("");
                deviceCurStatusInfo.setOwner("");
            }

        } else if (DevAllocateForm.MOVE_TYPE_BORROW.equals(allocateForm.getMoveType())) {

            deviceCurStatusInfo.setGroupName(allocateForm.getInGroupName());
            deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_BORROW);
            deviceCurStatusInfo.setGroupName("");
            deviceCurStatusInfo.setOwner("");
        } else {
            deviceCurStatusInfo.setGroupName(allocateForm.getInGroupName());
            deviceCurStatusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
            deviceCurStatusInfo.setStatusUptDate(new Date());
            deviceCurStatusInfo.setOwner(allocateForm.getInAccountID());
//            deviceCurStatusInfo.setPurpose(purpose);
            // 生成领用单
            DevPurchaseForm devUseForm = txCreateDeviceUseFormFromAllot(allocateForm);
            allocateForm.setRefDevUseFormID(devUseForm.getId());// 设备关联的领用单ID

        }
        deviceCurStatusInfo.setApproveType(null);
        deviceCurStatusInfo.setFormID(null);
        txUpdateDevUseReturnBackDate(allocateForm);
        devAllocateFormDAO.saveOrUpdate(deviceCurStatusInfo);
        devAllocateFormDAO.save(allocateForm);
        return allocateForm;
    }

    /**
     * 更新调拨单调拨的设备的归还时间
     * 
     * @param allocateForm
     */
    private void txUpdateDevUseReturnBackDate(DevAllocateForm allocateForm) {
        if (allocateForm != null) {
            for (DevAllocateList list : allocateForm.getDevAllocateLists()) {
                if (list != null) {
                    PurchaseDevice purchaseDevice = devUseApplyFormDAO.queryArchPurchaseDevByDeviceID(list.getDevice()
                            .getId(), false);
                    if (purchaseDevice != null && purchaseDevice.getReturnBackDate() == null) {
                        // 更新申购设备的归还日期
                        purchaseDevice.setReturnBackDate(new Date());
                        devUseApplyFormDAO.update(purchaseDevice);
                    } else {
                        // 更新领用里的设备归还日期
                        int iUpdateIndex = 0;
                        if (!DevAllocateForm.MOVE_TYPE_STORAGE.equals(allocateForm.getMoveType())
                                && !DevAllocateForm.MOVE_TYPE_BORROW.equals(allocateForm.getMoveType())) {
                            // 除了调拨入库和外借外其余要生成领用记录，所以更新索引要加1
                            iUpdateIndex = 1;
                        }
                        List<DevPurchaseList> devPurchaseLists_ = devUseApplyFormDAO.queryArchDevUseListByDeviceID(list
                                .getDevice().getId(), false);// 降序排序，第iUpdateIndex条就是待更新的记录
                        if (devPurchaseLists_ != null && devPurchaseLists_.size() > iUpdateIndex) {
                            DevPurchaseList latestDevPurList = devPurchaseLists_.get(iUpdateIndex);// 第iUpdateIndex条就是待更新的记录
                            if (latestDevPurList != null) {
                                latestDevPurList.setReturnBackDate(allocateForm.getArchiveDate());// 更新归还日期
                                devUseApplyFormDAO.update(latestDevPurList);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据调拨单生成领用单
     * 
     * @param allocateForm
     */
    private DevPurchaseForm txCreateDeviceUseFormFromAllot(DevAllocateForm allocateForm) {
        DevPurchaseForm applyForm = null;
        if (allocateForm != null && !DevAllocateForm.MOVE_TYPE_STORAGE.equals(allocateForm.getMoveType())) {
            Calendar calendar = Calendar.getInstance();
            // 生成设备领用单
            applyForm = new DevPurchaseForm();
            applyForm.setDeviceType(allocateForm.getDeviceType());
            applyForm.setApplyGroupName(allocateForm.getInGroupName());
            applyForm.setRemark("通过调拨单生成");
            applyForm.setApplicant(allocateForm.getInAccountID());
            applyForm.setApplyDate(allocateForm.getApplyDate());
            applyForm.setRegAccountID(allocateForm.getRegAccountID());
            applyForm.setRegTime(allocateForm.getRegTime());
            applyForm.setApplyType(DevPurchaseForm.APPLY_TYPE_ALLOT);
            applyForm.setFormStatus(DevPurchaseForm.FORMSTATUS_PUBLISH);
            applyForm.setPassed(true);
            applyForm.setArchiveDate(allocateForm.getArchiveDate());
            Set<DevPurchaseList> devPurchaseLists = new HashSet<DevPurchaseList>();
            DevPurchaseList devPurchaseList = null;
            for (DevAllocateList list : allocateForm.getDevAllocateLists()) {
                if (list != null) {
                    devPurchaseList = new DevPurchaseList();
                    devPurchaseList.setDevice(list.getDevice());
                    devPurchaseList.setPurpose(list.getPurpose());
                    devPurchaseList.setPlanUseDate(list.getDevAllocateForm().getMoveDate());
                    devPurchaseList.setAreaCode(list.getAreaCode());
                    devPurchaseList.setDevPurchaseForm(applyForm);
                    devPurchaseLists.add(devPurchaseList);
                }
            }
            applyForm.setDevPurchaseLists(devPurchaseLists);
            if (applyForm.getFormStatus() != DevPurchaseForm.FORMSTATUS_UNPUBLISH
                    && (applyForm.getApplyFormNO() == null || applyForm.getApplyFormNO() == 0
                            || applyForm.getSequenceYear() == null || applyForm.getSequenceYear() == 0)) {
                // 草稿状态或者已编过号的文档无须编号
                int year = calendar.get(Calendar.YEAR);
                int no = devUseApplyFormDAO.findCurrentYearFormNO(year);
                applyForm.setApplyFormNO(++no);// 编号序列号
                applyForm.setSequenceYear(year);// 编号年份
            }

            devUseApplyFormDAO.save(applyForm);
        }
        return applyForm;
    }

    /**
     * 判断用户是否已领用了不同种类的主设备
     * 
     * @return
     * @throws OaException
     */
    public void getUseMainDevCount(Set<DevAllocateList> devAllocateLists, String accountID, Set<String> deleteDeviceIDs)
            throws OaException {
        String mainDevClassTitle = getMainDevClassTitle(devAllocateLists);
        String strOptType = "调拨";
        String strSplit = ",";
        if (devAllocateLists == null || devAllocateLists.size() == 0) {
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

        // 追加本次新增的
        for (DevAllocateList devAllocateList : devAllocateLists) {
            if (devAllocateList != null && devAllocateList.getDevice() != null) {
                Device d = devAllocateList.getDevice();
                String deviceClassID = d.getDeviceClass() == null ? null : d.getDeviceClass().getId();
                String areaCode = d.getAreaCode();
                String key = d.getAreaCode() + strSplit + deviceClassID;
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(areaCode, deviceClassID, areaDeviceCfgs);
                if (mainDevFlag) {
                    // 主设备
                    if (!mainDevKeys.contains(key)) {
                        mainDevKeys.add(key);
                    }
                }
            }
        }

        // 追加已在流程中
        getMainDevCountInFlow(devAllocateLists, accountID, 2, mainDevKeys, deleteDeviceIDs);

        // 将已领用的设备中是主设备的放到mainDevKeys中
        List<Device> devices = deviceDAO.findUseDevicesByApplicantID(accountID);// 已领用的设备
        if (devices != null) {
            for (Device d : devices) {
                int mainDevCount = 0;
                String key = d.getAreaCode() + strSplit + d.getDeviceClass().getId();
                boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(d.getAreaCode(), d.getDeviceClass().getId(),
                        areaDeviceCfgs);
                if (mainDevFlag) {
                    // 主设备
                    // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                    if (!mainDevKeys.contains(key)) {
                        mainDevCount = 1 + mainDevKeys.size();
                    }
                    if (mainDevCount > 1) {
                        String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，你已领用的编号为“" + d.getDeviceNO()
                                + "”的设备是属于“" + d.getAreaName() + d.getDeviceClass().getName() + "”类别的主设备，" + "不能再"
                                + strOptType + "其它种类的主设备！";
                        throw new OaException(strPrompt);
                    }
                    // 否则，应放到已领用的mainDevKeys中
                    mainDevKeys.add(key);// 继续下一个领用设备的判断
                }
            }
        }
    }

    /**
     * 获取调拨清单中哪些类别是主设备。
     * 
     * @param devAllocateLists 调拨清单列表
     * @return 返回是主设备的类别，格式如下：福州笔记本、厦门台式机。 如果devAllocateLists=null或者调拨清单的设备其类别都不是主设备则返回""。
     */
    private String getMainDevClassTitle(Set<DevAllocateList> devAllocateLists) {
        if (devAllocateLists == null) {
            return "";
        }
        String strPrompt = "";
        List<AreaDeviceCfg> areaDeviceCfgs = deviceAreaConfigBiz.queryAllAreaDevices();// 获取所有区域设备配置
        for (DevAllocateList devAllocateList : devAllocateLists) {
            if (devAllocateList != null) {
                Device d = devAllocateList.getDevice();
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
     * 获取申购中的设备相关用途的次数Map。key是区域编码,设备类别ID,用途的组合字符串；value是次数
     * 
     * @throws OaException
     * 
     */
    private Set<String> getMainDevCountInFlow(Set<DevAllocateList> devAllocateLists, String accountID, int formType,
            Set<String> mainDevKeys, Set<String> deleteDeviceIDs) throws OaException {
        String mainDevClassTitle = null;
        if (devAllocateLists != null && devAllocateLists.size() > 0) {
            mainDevClassTitle = getMainDevClassTitle(devAllocateLists);
        }

        String strOptType = null;
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
        // 获取申购中的
        List<DevPurchaseForm> devPurchaseForms = devUseApplyFormDAO.findDealingFormByApplicantID(accountID,
                DevPurchaseForm.APPLY_TYPE_PURCHASE);
        if (devPurchaseForms != null && devPurchaseForms.size() > 0) {
            for (DevPurchaseForm form : devPurchaseForms) {
                if (form != null && form.getPurchaseDevices() != null && form.getPurchaseDevices().size() > 0) {
                    for (PurchaseDevice d : form.getPurchaseDevices()) {
                        if (d != null
                                && (deleteDeviceIDs == null || !deleteDeviceIDs.contains(d.getBelongtoAreaCode() + ","
                                        + d.getDeviceClass().getId()))) {
                            int mainDevCount = 0;
                            String key = d.getBelongtoAreaCode() + strSplit + d.getDeviceClass().getId();
                            boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(d.getBelongtoAreaCode(), d
                                    .getDeviceClass().getId(), areaDeviceCfgs);
                            if (mainDevFlag) {
                                // 主设备
                                // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                                if (!mainDevKeys.contains(key)) {
                                    mainDevCount = 1 + mainDevKeys.size();
                                }
                                if (mainDevCount > 1) {
                                    String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，你申购的“"
                                            + d.getBelongtoAreaName() + d.getDeviceClass().getName()
                                            + "”类别的主设备已在审批中，不能再" + strOptType + "其它种类的主设备！";
                                    throw new OaException(strPrompt);
                                }
                                // 否则，应放到已领用的mainDevKeys中
                                mainDevKeys.add(key);// 继续下一个领用设备的判断
                            }
                        }
                    }
                }
            }
        }

        // 获取领用中的
        devPurchaseForms = devUseApplyFormDAO.findDealingFormByApplicantID(accountID, DevPurchaseForm.APPLY_TYPE_USE);
        if (devPurchaseForms != null && devPurchaseForms.size() > 0) {
            for (DevPurchaseForm form : devPurchaseForms) {
                if (form != null && form.getDevPurchaseLists() != null && form.getDevPurchaseLists().size() > 0) {
                    for (DevPurchaseList list : form.getDevPurchaseLists()) {
                        if (list != null && list.getDevice() != null
                                && (deleteDeviceIDs == null || !deleteDeviceIDs.contains(list.getDevice().getId()))) {// 去除掉已删除的
                            int mainDevCount = 0;
                            String key = list.getDevice().getAreaCode() + strSplit
                                    + list.getDevice().getDeviceClass().getId();
                            boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(list.getDevice().getAreaCode(), list
                                    .getDevice().getDeviceClass().getId(), areaDeviceCfgs);
                            if (mainDevFlag) {
                                // 主设备
                                // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                                if (!mainDevKeys.contains(key)) {
                                    mainDevCount = 1 + mainDevKeys.size();
                                }
                                if (mainDevCount > 1) {
                                    String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，你申请领用的编号为“"
                                            + list.getDevice().getDeviceNO() + "”的设备是属于“"
                                            + list.getDevice().getAreaName()
                                            + list.getDevice().getDeviceClass().getName() + "”类别的主设备，其已在审批中，" + "不能再"
                                            + strOptType + "其它种类的主设备！";
                                    throw new OaException(strPrompt);
                                }
                                // 否则，应放到已领用的mainDevKeys中
                                mainDevKeys.add(key);// 继续下一个领用设备的判断
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
                                && (deleteDeviceIDs == null || !deleteDeviceIDs.contains(list.getDevice().getId()))) {// 去除掉已删除的
                            int mainDevCount = 0;
                            String key = list.getDevice().getAreaCode() + strSplit
                                    + list.getDevice().getDeviceClass().getId();
                            boolean mainDevFlag = getMainDevFlagInAreaDeviceCfg(list.getDevice().getAreaCode(), list
                                    .getDevice().getDeviceClass().getId(), areaDeviceCfgs);
                            if (mainDevFlag) {
                                // 主设备
                                // 是否在已领用的范围内，如果是，则忽略，如果不在已领用的范围内，则应与已领用的范围进行相加
                                if (!mainDevKeys.contains(key)) {
                                    mainDevCount = 1 + mainDevKeys.size();
                                }
                                if (mainDevCount > 1) {
                                    String strPrompt = "系统已将" + mainDevClassTitle + "设为主设备，调拨到你名下的编号为“"
                                            + list.getDevice().getDeviceNO() + "”的设备是属于“"
                                            + list.getDevice().getAreaName()
                                            + list.getDevice().getDeviceClass().getName() + "”类别的主设备，其已在审批中，" + "不能再"
                                            + strOptType + "其它种类的主设备！";
                                    throw new OaException(strPrompt);
                                }
                                // 否则，应放到已领用的mainDevKeys中
                                mainDevKeys.add(key);// 继续下一个领用设备的判断
                            }
                        }
                    }
                }
            }
        }
        return mainDevKeys;
    }

    @SuppressWarnings("unused")
    private String getMainDevUseInfo(Map<String, AreaDeviceCfg> mapCfg, Set<String> mainDevKeys, String strSplit) {
        String strPrompt = "";
        int i = 1;
        for (String maiDevKey : mainDevKeys) {
            if (maiDevKey != null) {
                String areaCode = maiDevKey.split(strSplit)[0];
                String areaName = Device.getAreaName(areaCode);
                AreaDeviceCfg areaDeviceCfgTemp = mapCfg.get(maiDevKey);
                String deviceClassName = areaDeviceCfgTemp.getDeviceClass().getName();
                strPrompt += (mainDevKeys.size() == 1 ? "" : (i++) + ":") + areaName + deviceClassName + "\r\n";
            }
        }
        return strPrompt;
    }

    @Override
    public DevAllocateForm txSaveAsDraf(String deviceTypeCode, String allotType, Date moveDate, String applicantID,
            String userGroupName, String inAccountID, String inGroupName, String reason,
            Set<DevAllocateList> devAllocateList, boolean isStartFlow, boolean validMainDevFlag) throws OaException {
        Date now = new Date();
        DevAllocateForm allocateForm = new DevAllocateForm();
        allocateForm.setDeviceType(deviceTypeCode);
        allocateForm.setMoveType(allotType);
        allocateForm.setMoveDate(moveDate);
        allocateForm.setRegAccountID(applicantID);
        allocateForm.setRegTime(now);
        allocateForm.setInAccountID(inAccountID);
        allocateForm.setInGroupName(inGroupName);
        allocateForm.setApplicant(applicantID);
        allocateForm.setApplyGroupName(userGroupName);
        allocateForm.setApplyDate(now);
        allocateForm.setReason(reason);

        if (devAllocateList != null && devAllocateList.size() > 0) {
            for (DevAllocateList list : devAllocateList) {
                if (list != null) {
                    list.setDevAllocateForm(allocateForm);// 设备主表单
                }
            }
            allocateForm.getDevAllocateLists().addAll(devAllocateList);
        }

        devAllocateFormDAO.save(allocateForm);
        allocateForm.setFormStatus(DevPurchaseForm.FORMSTATUS_UNPUBLISH);// 未发布
        if (isStartFlow) {
            for (DevAllocateList list : devAllocateList) {
                if (list != null && list.getDevice() != null && list.getDevice().getDeviceCurStatusInfo() != null) {
                    DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                    if (statusInfo.getApproveType() != null) {
                        // 审批中
                        throw new OaException("编号为“"
                                + list.getDevice().getDeviceNO()
                                + "”的设备正处于"
                                + DeviceCurStatusInfo.approveTypeMap.get(list.getDevice().getDeviceCurStatusInfo()
                                        .getApproveType()) + "，现不能调拨！");
                    } else {
                        // 保存设备的审批状态
                        if (statusInfo != null && statusInfo.getDeviceCurStatus() == 1 && statusInfo.getOwner() != null) {
                            list.setDevStatusBef(DevAllocateList.STATUS_TYPE_USE);
                        } else {
                            list.setDevStatusBef(DevAllocateList.STATUS_TYPE_NO_USE);
                        }

                        statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_ALLOT);
                        statusInfo.setStatusUptDate(allocateForm.getApplyDate());// 设备调拨申请日期
                        statusInfo.setFormID(allocateForm.getId());
                        // devAllocateFormDAO.update(statusInfo);
                        devAllocateFormDAO.merge(statusInfo);// a different object with the same identifier value was
                                                             // already associated with the session:
                    }
                }
                if (DevAllocateForm.MOVE_TYPE_INSIDE.equals(allocateForm.getMoveType())
                        || DevAllocateForm.MOVE_TYPE_DEPT.equals(allocateForm.getMoveType())) {
                    // 内部、部门之间调拨的要判断权限
                    if (validMainDevFlag) {
                        getUseMainDevCount(devAllocateList, inAccountID, null);// 检测主设备领用次数
                    }
                }
            }
            allocateForm.setFormStatus(DevPurchaseForm.FORMSTATUS_APPROVAL);// 审批中
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int no = devAllocateFormDAO.getMaxSequenceNo(year);
            allocateForm.setApplyFormNO(++no);// 编号序列号
            allocateForm.setSequenceYear(year);// 编号年份

            Set<AreaClassFlowDTO> areaClassFlowDTOs = null;
            areaClassFlowDTOs = buildAllotAreaClassFlows(devAllocateList, AreaDeviceCfg.FLOW_TYPE_DEVALLOT);
            String flowKey = getFlowKeyByType(areaClassFlowDTOs, AreaDeviceCfg.FLOW_TYPE_DEVALLOT);
            if (flowKey == null) {
                throw new OaException("未配置调拨流程");
            }
            txStartFlow(allocateForm, flowKey);
        }
        return allocateForm;
    }

    public DevAllocateForm txModifyDraftForm(String id, String deviceTypeCode, String allotType, Date moveDate,
            String inAccountID, String inGroupName, String reason, Set<DevAllocateList> devAllocateList,
            boolean isStartFlow, boolean validMainDevFlag) throws OaException {
        DevAllocateForm form = getDevAllocateFormById(id, false, false);
        if (devAllocateList != null && devAllocateList.size() > 0) {
            for (DevAllocateList list : devAllocateList) {

                if (list != null) {
                    DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                    if (statusInfo != null && statusInfo.getDeviceCurStatus() == 1 && statusInfo.getOwner() != null) {
                        list.setDevStatusBef(DevAllocateList.STATUS_TYPE_USE);
                    } else {
                        list.setDevStatusBef(DevAllocateList.STATUS_TYPE_NO_USE);
                    }
                    list.setDevAllocateForm(form);// 设备主表单
                }
            }
        }
        if (form != null) {
            // 先删除原先的
            if (form.getDevAllocateLists() != null) {
                Iterator<DevAllocateList> devAllotListItr = form.getDevAllocateLists().iterator();
                while (devAllotListItr.hasNext()) {
                    DevAllocateList list = devAllotListItr.next();
                    if (list != null && list.getDevice() != null) {
                        devAllotListItr.remove();
                        list.setDevAllocateForm(form);
                        devUseApplyFormDAO.delete(list);
                    }

                }
            }
            if (devAllocateList != null) {
                // 追加新设备列表
                form.getDevAllocateLists().addAll(devAllocateList);
            }
            Date now = new Date();
            form.setDeviceType(deviceTypeCode);
            form.setMoveType(allotType);
            form.setMoveDate(moveDate);
            form.setInAccountID(inAccountID);
            form.setInGroupName(inGroupName);
            form.setApplyDate(now);
            form.setReason(reason);
            // form.setDevAllocateLists(devAllocateList);//A collection with cascade="all-delete-orphan" was no longer
            form.setFormStatus(DevDiscardForm.FORMSTATUS_UNPUBLISH);
            if (isStartFlow) {
                for (DevAllocateList list : devAllocateList) {
                    if (list != null && list.getDevice() != null
                            && list.getDevice().getDeviceCurStatusInfo().getApproveType() != null) {
                        // 审批中
                        throw new OaException("编号为“"
                                + list.getDevice().getDeviceNO()
                                + "”的设备正处于"
                                + DeviceCurStatusInfo.approveTypeMap.get(list.getDevice().getDeviceCurStatusInfo()
                                        .getApproveType()) + "，现不能调拨！");
                    }
                }
                if (DevAllocateForm.MOVE_TYPE_INSIDE.equals(form.getMoveType())
                        || DevAllocateForm.MOVE_TYPE_DEPT.equals(form.getMoveType())) {
                    // 内部、部门之间调拨的要判断权限
                    if (validMainDevFlag) {
                        getUseMainDevCount(devAllocateList, inAccountID, null);// 检测主设备领用次数
                    }
                }

                form.setFormStatus(DevFlowApplyProcess.FORMSTATUS_APPROVAL);// 审批中

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int no = devAllocateFormDAO.getMaxSequenceNo(year);
                form.setApplyFormNO(++no);// 编号序列号
                form.setSequenceYear(year);// 编号年份

                Set<AreaClassFlowDTO> areaClassFlowDTOs = null;
                // 保存设备的审批状态
                for (DevAllocateList list : form.getDevAllocateLists()) {
                    if (list != null && list.getDevice() != null) {
                        DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                        statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_ALLOT);
                        statusInfo.setStatusUptDate(form.getApplyDate());// 设备调拨申请日期
                        statusInfo.setFormID(form.getId());
                        devAllocateFormDAO.merge(statusInfo);
                    }
                }
                areaClassFlowDTOs = buildAllotAreaClassFlows(devAllocateList, AreaDeviceCfg.FLOW_TYPE_DEVALLOT);

                String flowKey = getFlowKeyByType(areaClassFlowDTOs, AreaDeviceCfg.FLOW_TYPE_DEVALLOT);
                if (flowKey == null) {
                    throw new OaException("未配置调拨流程");
                }
                txStartFlow(form, flowKey);
            } else {
                devAllocateFormDAO.update(form);
            }
        }
        return form;
    }

    @Override
    public DevAllocateForm txDraftmanAmend(String id, String allotType, Date moveDate, String inAccountID,
            String inGroupName, String reason, Set<DevAllocateList> devAllocateList, boolean validMainDevFlag)
            throws OaException {
        if (id == null || "".equals(id)) {
            throw new OaException("参数错误");
        }

        DevAllocateForm form = getDevAllocateFormById(id, false, false);
        if (DevAllocateForm.MOVE_TYPE_INSIDE.equals(allotType) || DevAllocateForm.MOVE_TYPE_DEPT.equals(allotType)) {
            // 内部、部门之间调拨的要判断权限
            if (validMainDevFlag) {
                Set<String> deleteDeviceIDs = getAllotDeleteDeviceIDs(devAllocateList, form.getDevAllocateLists());
                getUseMainDevCount(devAllocateList, inAccountID, deleteDeviceIDs);// 检测主设备领用次数
            }
        }

//        String oldFlowKey = null;
//        // 判断流程是否一致
//        Task task = taskDAO.findDealingTasksByFormID(id);
//        if (task == null) {
//            throw new OaException("任务不存在！");
//        }
//        oldFlowKey = task.getFlowKey();
        Set<AreaClassFlowDTO> areaClassFlowDTOs = buildAllotAreaClassFlows(devAllocateList,
                AreaDeviceCfg.FLOW_TYPE_DEVALLOT);
        String newFlowKey = getFlowKeyByType(areaClassFlowDTOs, AreaDeviceCfg.FLOW_TYPE_DEVALLOT);
        if (newFlowKey == null) {
            throw new OaException("未配置" + DevPurchaseForm.applyTypeMap.get(AreaDeviceCfg.FLOW_TYPE_DEVALLOT) + "流程");
        }
//        if (!newFlowKey.equals(oldFlowKey)) {
//            // 所选择的设备的流程与现在的流程不一致
//            throw new OaException("所选择的设备的" + AreaDeviceCfg.flowMap.get(AreaDeviceCfg.FLOW_TYPE_DEVALLOT) + "不一致");
//        }

//        WfmContext context = null;
//        try {
//            // 追加流程环境变量：入库标识
//            context = WfmConfiguration.getInstance().getWfmContext();
//            TaskInstance ti = context.getTaskInstance(task.getTaskInstanceID());
//            String toStorageFlag = DevAllocateForm.MOVE_TYPE_STORAGE.equals(allotType) ? "1" : "0";// 调拨入库标识
//            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_TOSTORAGEFLAG,
//                    ContextVariable.DATATYPE_STRING, toStorageFlag);
//            ti.getFlowInstance().addContextVariable(cv);
//        } catch (Exception e) {
//            e.printStackTrace();
//            context.rollback();
//            throw new OaException(e.getMessage());
//        } finally {
//            context.close();
//        }

        if (devAllocateList != null && devAllocateList.size() > 0) {
            for (DevAllocateList list : devAllocateList) {
                if (list != null) {
                    DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                    if (statusInfo != null && statusInfo.getDeviceCurStatus() == 1 && statusInfo.getOwner() != null) {
                        list.setDevStatusBef(DevAllocateList.STATUS_TYPE_USE);
                    } else {
                        list.setDevStatusBef(DevAllocateList.STATUS_TYPE_NO_USE);
                    }
                    list.setDevAllocateForm(form);// 设备主表单
                }
            }
        }
        Map<String, DevAllocateList> newDeviceListMap = new HashMap<String, DevAllocateList>();
        if (form != null) {
            if (devAllocateList != null) {
                for (DevAllocateList list : devAllocateList) {
                    if (list != null && list.getDevice() != null) {
                        newDeviceListMap.put(list.getDevice().getId(), list);
                    }
                }
            }

            // 先删除原先的
            if (form.getDevAllocateLists() != null) {
                Iterator<DevAllocateList> devAllotListItr = form.getDevAllocateLists().iterator();
                while (devAllotListItr.hasNext()) {
                    DevAllocateList list = devAllotListItr.next();
                    if (list != null && list.getDevice() != null) {
                        if (list.getDevice().getId() != null && !newDeviceListMap.containsKey(list.getDevice().getId())) {
                            DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                            // 在新设备列表中不存在旧的设备ID，说明设备已被删除，要恢复旧单的设备的审批状态为不在审批中
                            statusInfo.setApproveType(null);
                            statusInfo.setFormID(null);
                            devUseApplyFormDAO.update(statusInfo);
                        } else {
                            newDeviceListMap.remove(list.getDevice().getId());
                        }
                    }
                    devAllotListItr.remove();
                    list.setDevAllocateForm(null);
                    devAllocateFormDAO.delete(list);
                }
            }
            if (devAllocateList != null) {
                // 追加新设备列表
                form.getDevAllocateLists().addAll(devAllocateList);
                // 保存新单的设备的审批状态。
                for (Entry<String, DevAllocateList> list : newDeviceListMap.entrySet()) {
                    if (list.getValue() != null && list.getValue().getDevice() != null) {
                        DeviceCurStatusInfo statusInfo = list.getValue().getDevice().getDeviceCurStatusInfo();
                        statusInfo.setApproveType(DevPurchaseForm.APPLY_TYPE_ALLOT);
                        statusInfo.setStatusUptDate(form.getApplyDate());// 设备调拨申请日期
                        statusInfo.setFormID(form.getId());
                        devAllocateFormDAO.update(statusInfo);
                    }
                }
            }
            form.setMoveType(allotType);
            form.setMoveDate(moveDate);
            form.setInAccountID(inAccountID);
            form.setInGroupName(inGroupName);
            form.setReason(reason);
            devAllocateFormDAO.update(form);
        }
        return form;
    }

    /**
     * 组合调拨区域设备类别流程列表
     * 
     * @param devAllocateList
     * @param flowType
     * @return
     */
    private Set<AreaClassFlowDTO> buildAllotAreaClassFlows(Set<DevAllocateList> devAllocateList, int flowType) {
        Set<AreaClassFlowDTO> areaClassFlowDTOs = new HashSet<AreaClassFlowDTO>();
        if (devAllocateList != null) {
            for (DevAllocateList list : devAllocateList) {
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

    public List<DevAllocateList> getArchDevAllotListByDeviceID(String deviceID, Boolean archiveDateOrder) {
        return devAllocateFormDAO.queryArchDevAllotListByDeviceID(deviceID, archiveDateOrder);
    }

    @Override
    public void txSaveOrUpdateDevAllocateForm(String id, Map<String, DevAllocateList> map, Boolean valiDevFlag)
            throws OaException {
        if (id == null || map == null || map.size() == 0) {
            return;
        }
        DevAllocateForm allocateForm = devAllocateFormDAO.findById(id);
        if (allocateForm == null) {
            return;
        }

        Set<PurchaseDevPurpose> purchaseDevPurposes = new HashSet<PurchaseDevPurpose>();
        Hibernate.initialize(allocateForm.getDevAllocateLists());
        Map<String, Set<PurchaseDevPurpose>> deviceClassMap = new HashMap<String, Set<PurchaseDevPurpose>>();
        Map<String, String> areaMap = new HashMap<String, String>();
        Map<String, String> deviceClassNameMap = new HashMap<String, String>();
        if (!allocateForm.getDevAllocateLists().isEmpty()) {

            Map<String, PurchaseDevPurpose> purposeTimes = new HashMap<String, PurchaseDevPurpose>();
            for (DevAllocateList purchaseDevice : allocateForm.getDevAllocateLists()) {
                Hibernate.initialize(purchaseDevice.getDevice());
                Hibernate.initialize(purchaseDevice.getDevice().getDeviceClass());
                DevAllocateList newDevAllocateList = map.get(purchaseDevice.getId());
                if (newDevAllocateList != null) {
                    // purchaseDevice.setPurpose(newDevAllocateList.getPurpose());
                    // purchaseDevice.setAreaCode(newDevAllocateList.getAreaCode());
//                    boolean manyTimeFlag = deviceAreaConfigBiz.getManyTimeFlagByPurpose(purchaseDevice.getDevice()
//                            .getAreaCode(), purchaseDevice.getDevice().getDeviceClass().getId(), newDevAllocateList
//                            .getPurpose());
                    PurchaseDevPurpose devPurposePurchase = new PurchaseDevPurpose();
                    devPurposePurchase.setPurpose(newDevAllocateList.getPurpose());
                    devPurposePurchase.setManyTimeFlag(false);
                    purchaseDevPurposes.add(devPurposePurchase);
                    if (deviceClassMap != null) {
                        Set<PurchaseDevPurpose> set = deviceClassMap.get(purchaseDevice.getDevice().getDeviceClass()
                                .getId());
                        if (set == null) {
                            set = new HashSet<PurchaseDevPurpose>();
                            set.add(devPurposePurchase);
                            deviceClassMap.put(purchaseDevice.getDevice().getDeviceClass().getId(), set);
                        } else {
                            set.add(devPurposePurchase);
                        }
                    } else {
                        Set<PurchaseDevPurpose> set = new HashSet<PurchaseDevPurpose>();
                        set.add(devPurposePurchase);
                        deviceClassMap.put(purchaseDevice.getDevice().getDeviceClass().getId(), set);
                    }
                    if (areaMap != null) {
                        String str = areaMap.get(purchaseDevice.getDevice().getDeviceClass().getId());
                        if (str == null) {
                            areaMap.put(purchaseDevice.getDevice().getDeviceClass().getId(), purchaseDevice.getDevice()
                                    .getAreaCode());
                        }
                    } else {
                        areaMap.put(purchaseDevice.getDevice().getDeviceClass().getId(), purchaseDevice.getDevice()
                                .getAreaCode());
                    }
                    if (deviceClassNameMap != null) {
                        String str = deviceClassNameMap.get(purchaseDevice.getDevice().getDeviceClass().getId());
                        if (str == null) {
                            deviceClassNameMap.put(purchaseDevice.getDevice().getDeviceClass().getId(), purchaseDevice
                                    .getDevice().getDeviceClass().getName());
                        }
                    } else {
                        deviceClassNameMap.put(purchaseDevice.getDevice().getDeviceClass().getId(), purchaseDevice
                                .getDevice().getDeviceClass().getName());
                    }
//                    if (!manyTimeFlag) {
                        String key = newDevAllocateList.getAreaCode() + ","
                                + purchaseDevice.getDevice().getDeviceClass().getId();
                        if (purposeTimes != null && purposeTimes.get(key) != null) {
                            PurchaseDevPurpose devPurpose = purposeTimes.get(key);
                            devPurpose.setUseCount(devPurpose.getUseCount() + 1);
                            purposeTimes.put(key, devPurpose);
                        } else {
                            PurchaseDevPurpose devPurpose = new PurchaseDevPurpose();
                            devPurpose.setPurpose(newDevAllocateList.getPurpose());
                            devPurpose.setDeviceClassName(purchaseDevice.getDevice().getDeviceClass().getName());
                            devPurpose.setUseCount(1);
                            purposeTimes.put(key, devPurpose);

                        }

//                    }

                }

            }
            if (purposeTimes != null && valiDevFlag) {
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
            if (valiDevFlag) {
                for (String str : deviceClassMap.keySet()) {
                    if (null != deviceClassMap.get(str)) {
                        deviceApplyBiz.checkDeviceMsg(allocateForm.getDeviceType(), areaMap.get(str), str,
                                deviceClassNameMap.get(str), allocateForm.getInAccountID(),
                                DevPurchaseForm.APPLY_TYPE_ALLOT, purchaseDevPurposes, false, true);// 判断工作用途是否可以领用多台，包括已领用，申购、领用在审批中的
                    }
                }
            }
            // getDevUseCount(allocateForm.getDevAllocateLists(), allocateForm.getInAccountID());//检测设备相关用途的领用次数

        }
        if (!allocateForm.getDevAllocateLists().isEmpty()) {
            for (DevAllocateList purchaseDevice : allocateForm.getDevAllocateLists()) {
                Hibernate.initialize(purchaseDevice.getDevice());
                Hibernate.initialize(purchaseDevice.getDevice().getDeviceClass());
                DevAllocateList newDevAllocateList = map.get(purchaseDevice.getId());
                if (newDevAllocateList != null) {
                    purchaseDevice.setPurpose(newDevAllocateList.getPurpose());
                    purchaseDevice.setAreaCode(newDevAllocateList.getAreaCode());
                }
            }
        }
        devUseApplyFormDAO.saveOrUpdate(allocateForm);
    }

    private Set<String> getAllotDeleteDeviceIDs(Set<DevAllocateList> newDevPurchaseLists,
            Set<DevAllocateList> oldDevPurchaseLists) {
        Set<String> deleteDeviceIDs = new HashSet<String>();
        Set<String> newDeviceIDs = new HashSet<String>();
        Set<String> oldDeviceIDs = new HashSet<String>();
        for (DevAllocateList list : newDevPurchaseLists) {
            if (list != null && list.getDevice() != null) {
                newDeviceIDs.add(list.getDevice().getId());
            }
        }
        for (DevAllocateList list : oldDevPurchaseLists) {
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

    public void setDeviceBiz(IDeviceBiz deviceBiz) {
        this.deviceBiz = deviceBiz;
    }

    public void setDeviceApplyBiz(IDeviceApplyBiz deviceApplyBiz) {
        this.deviceApplyBiz = deviceApplyBiz;
    }
}
