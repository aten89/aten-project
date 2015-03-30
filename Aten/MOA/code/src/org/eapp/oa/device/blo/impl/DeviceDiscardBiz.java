package org.eapp.oa.device.blo.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.eapp.oa.device.blo.IDevValidateFormBiz;
import org.eapp.oa.device.blo.IDeviceAreaConfigBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.dao.IDevDiscardFormDAO;
import org.eapp.oa.device.dao.IDevUseApplyFormDAO;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.dto.DevDiscardQueryParameters;
import org.eapp.oa.device.hbean.AreaDeviceCfg;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevDiscardForm;
import org.eapp.oa.device.hbean.DevPurchaseForm;
import org.eapp.oa.device.hbean.DevPurchaseList;
import org.eapp.oa.device.hbean.DevValidateForm;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceCurStatusInfo;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.util.hibernate.ListPage;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;

import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;

public class DeviceDiscardBiz implements IDeviceDiscardBiz {
    private IDevDiscardFormDAO devDiscardFormDAO;
    private IDeviceDAO deviceDAO;
    private IDevValidateFormBiz devValidateFormBiz;
    private IDevUseApplyFormDAO devUseApplyFormDAO;
    private IDeviceAreaConfigBiz deviceAreaConfigBiz;
    private IDeviceBiz deviceBiz;


    public void setDevValidateFormBiz(IDevValidateFormBiz devValidateFormBiz) {
        this.devValidateFormBiz = devValidateFormBiz;
    }

    public void setDevDiscardFormDAO(IDevDiscardFormDAO devDiscardFormDAO) {
        this.devDiscardFormDAO = devDiscardFormDAO;
    }

    public void setDeviceDAO(IDeviceDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    public void setDeviceAreaConfigBiz(IDeviceAreaConfigBiz deviceAreaConfigBiz) {
        this.deviceAreaConfigBiz = deviceAreaConfigBiz;
    }

    @Override
    public DevDiscardForm getDevDiscardFormById(String id, boolean initList) {
        if (null == id) {
            return null;
        }
        DevDiscardForm form = devDiscardFormDAO.findById(id);
        if (form != null) {
            if (initList) {
                Hibernate.initialize(form.getDiscardDevLists());
                if (form.getDiscardDevLists() != null) {
                    for (DiscardDevList list : form.getDiscardDevLists()) {
                        if (list != null) {
                            Hibernate.initialize(list.getDevice());
                        }
                    }
                    // 对报废的设备按设备编号排序
                    Set<DiscardDevList> discardDevLists = new TreeSet<DiscardDevList>(new Comparator<DiscardDevList>() {
                        @Override
                        public int compare(DiscardDevList o1, DiscardDevList o2) {
                            if (o1 != null && o1.getDevice() != null && o1.getDevice().getDeviceNO() != null
                                    && o2 != null && o2.getDevice() != null) {
                                return o1.getDevice().getDeviceNO().compareTo(o2.getDevice().getDeviceNO());
                            }
                            return -1;
                        }
                    });
                    discardDevLists.addAll(form.getDiscardDevLists());
                    form.getDiscardDevLists().clear();
                    form.getDiscardDevLists().addAll(discardDevLists);
                }
            }
        }
        return form;
    }

    @Override
    public void txDelDevDiscardForm(String id) throws OaException {
        if (null == id) {
            return;
        }
        DevDiscardForm devDiscardForm = devDiscardFormDAO.findById(id);
        if (null == devDiscardForm) {
            return;
        }
        String formType = "设备报废单";
        if (DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue() == devDiscardForm.getFormType().intValue()) {
            formType = "离职处理单";
        }
        if (DevDiscardForm.FORMSTATUS_UNPUBLISH != devDiscardForm.getFormStatus()) {
            throw new OaException("只能删除未发布的" + formType);
        }
        devDiscardFormDAO.delete(devDiscardForm);
    }

    @Override
    public DevDiscardForm txSaveAsDraf(DevDiscardForm form, Integer formType, boolean isStartFlow) throws OaException {
        if (null == form) {
            throw new IllegalArgumentException("非法参数:参数错误");
        }
        form.setFormType(formType);
        form.setFormStatus(DevDiscardForm.FORMSTATUS_UNPUBLISH);
        devDiscardFormDAO.saveOrUpdate(form);

        if (isStartFlow) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int no = devDiscardFormDAO.getMaxSequenceNo(year, formType);
            form.setApplyFormNO(++no);// 编号序列号
            form.setSequenceYear(year);// 编号年份
            // 保存设备的审批状态
            for (DiscardDevList list : form.getDiscardDevLists()) {
                if (list != null && list.getDevice() != null) {
                    DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                    if (statusInfo != null && statusInfo.getApproveType() != null) {
                        // 审批中
                        throw new OaException("编号为“"
                                + list.getDevice().getDeviceNO()
                                + "”的设备正处于"
                                + DeviceCurStatusInfo.approveTypeMap.get(list.getDevice().getDeviceCurStatusInfo()
                                        .getApproveType()) + "，现不能操作！");
                    }
                    if (formType == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL) {
                        statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_SCRAP);// 正常报废
                    } else {
                        statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_LEAVE);// 离职报废
                    }
                    statusInfo.setFormID(form.getId());
                    devDiscardFormDAO.update(statusInfo);
                }
            }
            Integer flowType = null;
            if (formType == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL) {
                flowType = AreaDeviceCfg.FLOW_TYPE_DEVSCRAP;// 报废
            } else {
                flowType = AreaDeviceCfg.FLOW_TYPE_DEVDIMISSION;// 离职
            }
            String flowKey = getFlowKeyByType(form.getDiscardDevLists(), flowType);
            if (flowKey == null) {
                throw new OaException("未配置" + AreaDeviceCfg.flowMap.get(flowType));// 未配置报废、离职流程
            }
            txStartFlow(form, flowKey);
        }
        return form;
    }

    public DevDiscardForm txModifyDraftForm(String id, String applyGroupName, String deviceTypeCode,
            Set<DiscardDevList> discardDevLists, boolean isStartFlow) throws OaException {
        DevDiscardForm form = getDevDiscardFormById(id, false);
        if (discardDevLists != null) {
            for (DiscardDevList list : discardDevLists) {
                if (list != null && list.getDevDiscardForm() == null) {
                    list.setDevDiscardForm(form);// 设备主表单
                }
            }
        }
        if (form != null) {
            // 先删除原先的
            if (form.getDiscardDevLists() != null) {
                Iterator<DiscardDevList> discardDevListItr = form.getDiscardDevLists().iterator();
                while (discardDevListItr.hasNext()) {
                    DiscardDevList list = discardDevListItr.next();
                    discardDevListItr.remove();
                    list.setDevDiscardForm(null);
                    devDiscardFormDAO.delete(list);
                }
                if (discardDevLists != null) {
                    form.getDiscardDevLists().addAll(discardDevLists);
                }
            }

            form.setApplyDate(new Date());// 申请时间取当前时间
            form.setApplyGroupName(applyGroupName);
            form.setDeviceType(deviceTypeCode);
            form.setFormStatus(DevDiscardForm.FORMSTATUS_UNPUBLISH);
            if (isStartFlow) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int no = devDiscardFormDAO.getMaxSequenceNo(year, form.getFormType());
                form.setApplyFormNO(++no);// 编号序列号
                form.setSequenceYear(year);// 编号年份
                // 保存设备的审批状态
                for (DiscardDevList list : form.getDiscardDevLists()) {
                    if (list != null && list.getDevice() != null) {
                        DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                        if (statusInfo != null && statusInfo.getApproveType() != null) {
                            // 审批中
                            throw new OaException("编号为“"
                                    + list.getDevice().getDeviceNO()
                                    + "”的设备正处于"
                                    + DeviceCurStatusInfo.approveTypeMap.get(list.getDevice().getDeviceCurStatusInfo()
                                            .getApproveType()) + "，现不能操作！");
                        }
                        if (form.getFormType() == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL) {
                            statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_SCRAP);// 正常报废
                        } else {
                            statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_LEAVE);// 离职报废
                        }
                        statusInfo.setFormID(form.getId());
                        devDiscardFormDAO.merge(statusInfo);
                        // devDiscardFormDAO.update(statusInfo);
                    }
                }

                Integer flowType = null;
                if (form.getFormType().equals(DevDiscardForm.FORM_TYPE_DISCARD_NORMAL)) {
                    flowType = AreaDeviceCfg.FLOW_TYPE_DEVSCRAP;// 报废
                } else {
                    flowType = AreaDeviceCfg.FLOW_TYPE_DEVDIMISSION;// 离职
                }
                String flowKey = getFlowKeyByType(form.getDiscardDevLists(), flowType);
                if (flowKey == null) {
                    throw new OaException("未配置" + AreaDeviceCfg.flowMap.get(flowType));// 未配置报废、离职流程
                }
                txStartFlow(form, flowKey);
            } else {
                devDiscardFormDAO.update(form);
            }
        }
        return form;
    }

    /**
     * 根据流程类别获取对应的流程key
     * 
     * @param devPurchaseList
     * @param flowType
     * @return
     * @throws OaException
     */
    private String getFlowKeyByType(Set<DiscardDevList> discardDevList, int flowType) throws OaException {
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
        if (discardDevList != null) {
            for (DiscardDevList list : discardDevList) {
                if (list != null) {
                    String key = list.getDevice().getAreaCode() + list.getDevice().getDeviceClass().getId() + flowType;
                    flowKey = flowMap.get(key);
                    if (discardDevList.size() == 1) {
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

    private void txStartFlow(DevDiscardForm devDiscardForm, String flowKey) throws OaException {
        if (devDiscardForm == null) {
            return;
        }
//        String toStorageFlag = "0";
//        for (DiscardDevList list : devDiscardForm.getDiscardDevLists()) {
//            if (DevDiscardForm.LEAVE_DEAL_TYPE_TOSTORAGE.equals(list.getDealType())) {
//                toStorageFlag = "1";
//                break;
//            }
//        }
        devDiscardForm.setFormStatus(DevDiscardForm.FORMSTATUS_APPROVAL);
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try {
            // 设置流程上下文变量中，并启动流程
            Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
            // 把表单ID
            ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID, ContextVariable.DATATYPE_STRING,
                    devDiscardForm.getId());
            contextVariables.put(cv.getName(), cv);
            // 发起人
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                    devDiscardForm.getApplicant());
            contextVariables.put(cv.getName(), cv);

            // 发起部门
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME, ContextVariable.DATATYPE_STRING,
                    devDiscardForm.getApplyGroupName());
            contextVariables.put(cv.getName(), cv);

            // 任务描述
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                    devDiscardForm.getApplicantDisplayName()
                            + (devDiscardForm.getFormType().equals(DevDiscardForm.FORM_TYPE_DISCARD_NORMAL) ? "的设备报废单"
                                    : "的设备离职处理单") + "(" + devDiscardForm.getFullFormNO() + ")");
            contextVariables.put(cv.getName(), cv);

//            String dealTypeBackBuyFlag = "0";
//            String backBuyFlag = "0";
//            if (devDiscardForm.getFormType() == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL) {
//                // 设备报废表单:设置流程环境变量:backBuyFlag
//                for (DiscardDevList list : devDiscardForm.getDiscardDevLists()) {
//                    if (list != null && DevDiscardForm.DEAL_TYPE_MOVEBACK.equals(list.getDealType())) {
//                        dealTypeBackBuyFlag = "1";
//                        break;
//                    }
//                }

//                cv = new ContextVariable(SysConstants.FLOW_VARNAME_BACKBUYFLAG, ContextVariable.DATATYPE_STRING,
//                        dealTypeBackBuyFlag);
//                contextVariables.put(cv.getName(), cv);
//            } else {
                // 离职处理表单：设置流程环境变量：dealTypeBackBuyFlag和backBuyFlag。
                // dealTypeBackBuyFlag和backBuyFlag区别如下:
                // dealTypeBackBuyFlag表示填单时选择的设备处理方式，true表示处理方式为离职回购，否则为拿走和退库。
                // 而backBuyFlag：表示审批中填写的是否确认回购，true:表示确认回购；false：表示不回购;
//                for (DiscardDevList list : devDiscardForm.getDiscardDevLists()) {
//                    if (list != null && DevDiscardForm.LEAVE_DEAL_TYPE_BACKBUY.equals(list.getDealType())) {
//                        dealTypeBackBuyFlag = "1";// 当设备处理方式为离职回购时，则dealTypeBackBuyFlag和backBuyFlag默认为1。backBuyFlag在审批过程中根据实际填写的“是否确认回购”进行更新。
//                        backBuyFlag = "1";
//                        break;
//                    }
//                }

//                cv = new ContextVariable(SysConstants.FLOW_VARNAME_DEAL_TYPE_BACKBUYFLAG, // 设置流程环境变量dealTypeBackBuyFlag
//                        ContextVariable.DATATYPE_STRING, dealTypeBackBuyFlag);
//                contextVariables.put(cv.getName(), cv);
//                cv = new ContextVariable(SysConstants.FLOW_VARNAME_BACKBUYFLAG, // 设置流程环境变量backBuyFlag
//                        ContextVariable.DATATYPE_STRING, backBuyFlag);
//                contextVariables.put(cv.getName(), cv);
//            }

//            // 是否退库
//            cv = new ContextVariable(SysConstants.FLOW_VARNAME_TOSTORAGEFLAG, ContextVariable.DATATYPE_STRING,
//                    toStorageFlag);
//            contextVariables.put(cv.getName(), cv);

            
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_OUTACCOUNTID, ContextVariable.DATATYPE_STRING, null);
            contextVariables.put(cv.getName(), cv);
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_OUTGROUPNAME, ContextVariable.DATATYPE_STRING, null);
            contextVariables.put(cv.getName(), cv);
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_INACCOUNTID, ContextVariable.DATATYPE_STRING, null);
            contextVariables.put(cv.getName(), cv);
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_INGROUPNAME, ContextVariable.DATATYPE_STRING, null);
            contextVariables.put(cv.getName(), cv);
            
            // 表类类型
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMTYPE, ContextVariable.DATATYPE_STRING, null);
            contextVariables.put(cv.getName(), cv);
            // 区域
            cv = new ContextVariable(SysConstants.FLOW_VARNAME_AREA, ContextVariable.DATATYPE_STRING, null);
            contextVariables.put(cv.getName(), cv);
            
            FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
            flowInstance.signal();
            context.save(flowInstance);
            // 设置表单视图的ID
            devDiscardForm.setFlowInstanceID(flowInstance.getId());
        } catch (Exception e) {
            e.printStackTrace();
            context.rollback();
            throw new OaException(e.getMessage());
        } finally {
            context.close();
        }
        devDiscardFormDAO.saveOrUpdate(devDiscardForm);
    }

    @Override
    public List<DevDiscardForm> queryDealDiscardForm(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        return devDiscardFormDAO.queryDealDiscardForm(userId);
    }

    public List<Task> getEndedTasks(String formId) {
        DevDiscardForm discardForm = devDiscardFormDAO.findById(formId);
        if (discardForm == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        // return taskDAO.findEndedTasks(discardForm.getFlowInstanceId());
        return null;
    }

    @Override
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String formId) {
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try {
            DevDiscardForm discardForm = devDiscardFormDAO.findById(formId);
            if (discardForm == null) {
                throw new IllegalArgumentException("非法参数：找不到相应的表单");
            }
            String formType = "设备报废单";
            if (DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue() == discardForm.getFormType().intValue()) {
                formType = "离职处理单";
            }
            TaskInstance ti = context.getTaskInstance(taskInstanceId);
            if (ti == null) {
                throw new IllegalArgumentException();
            }

            // 表单ID
            ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID,
                    ContextVariable.DATATYPE_STRING, discardForm.getId());
            ti.getFlowInstance().addContextVariable(var);
            // //发起人
            // var = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID,
            // ContextVariable.DATATYPE_STRING, discardForm.getDraftsman());
            // ti.getFlowInstance().addContextVariable(var);

            // 任务描述
            var = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                    formType);
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


    @Override
    public ListPage<DevDiscardForm> queryArchDiscardForm(DevDiscardQueryParameters qp, String userAccountId) {
        if (userAccountId == null) {
            throw new IllegalArgumentException("非法参数:userAccountId为空");
        }
        return devDiscardFormDAO.queryArchDiscardForm(qp, userAccountId);
    }

    @Override
    public DevDiscardForm txCancellDiscardForm(String formId) {
        if (formId == null) {
            throw new IllegalArgumentException("非法参数：formId不能为空");
        }
        DevDiscardForm devDiscardForm = getDevDiscardFormById(formId, true);

        Set<DiscardDevList> discardDevLists = devDiscardForm.getDiscardDevLists();
        for (DiscardDevList discardDevList : discardDevLists) {
            Device device = discardDevList.getDevice();
            if (device != null && device.getDeviceCurStatusInfo() != null) {
                DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
                statusInfo.setApproveType(null);
                statusInfo.setFormID(null);
                devDiscardFormDAO.saveOrUpdate(statusInfo);
            }
        }
        devDiscardForm.setPassed(false);
        devDiscardForm.setFormStatus(DevAllocateForm.FORMSTATUS_CANCELLATION);
        devDiscardForm.setArchiveDate(new Date());
        devDiscardFormDAO.saveOrUpdate(devDiscardForm);
        return devDiscardForm;
    }

    @Override
    public void txPublishDiscardForm(String formId) {
        if (null == formId) {
            throw new IllegalArgumentException("非法参数：formId不能为空");
        }
        DevDiscardForm devDiscardForm = getDevDiscardFormById(formId, true);// 报废设备
        if (null == devDiscardForm) {
            throw new IllegalArgumentException("非法参数：找不到相应的表单");
        }
        Set<DiscardDevList> discardDevLists = devDiscardForm.getDiscardDevLists();
        if (DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue() == devDiscardForm.getFormType().intValue()) {
            // 报废
            for (DiscardDevList discardDevList : discardDevLists) {
                Device device = discardDevList.getDevice();
                if (device != null && device.getDeviceCurStatusInfo() != null) {
                    DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
                    if (DevDiscardForm.DEAL_TYPE_MOVEBACK.equals(discardDevList.getDealType())) {
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE);
                        statusInfo.setGroupName(null);
                        statusInfo.setOwner(null);
                    } else if (DevDiscardForm.DEAL_TYPE_TAKE.equals(discardDevList.getDealType())) {
                        // 填单人确认回购，不回购该设备就退库
                        if (discardDevList.getBuyFlag()) {
                            statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_TAKE);
                        } else {
                            statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE);
                            statusInfo.setGroupName(null);
                            statusInfo.setOwner(null);
                        }

                    } else if (DevDiscardForm.DEAL_TYPE_TAKEAWAY.equals(discardDevList.getDealType())) {
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LEAVE_TAKE);
                    } else {
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LOST);
                    }
                    statusInfo.setApproveType(null);
                    statusInfo.setFormID(null);
                    devDiscardFormDAO.saveOrUpdate(statusInfo);
                }
            }
        } else if (DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue() == devDiscardForm.getFormType().intValue()) {
            // 离职处理
            for (DiscardDevList discardDevList : discardDevLists) {
                Device device = discardDevList.getDevice();
                if (device != null && device.getDeviceCurStatusInfo() != null) {
                    DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
                    if (DevDiscardForm.LEAVE_DEAL_TYPE_TOSTORAGE.equals(discardDevList.getDealType())) {
                        // 退库
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                        statusInfo.setGroupName(null);
                        statusInfo.setOwner(null);
                    } else if (DevDiscardForm.LEAVE_DEAL_TYPE_TAKE.equals(discardDevList.getDealType())) {
                        // 拿走
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LEAVE_TAKE);
                    } else if (discardDevList.getBuyFlag().booleanValue()) {
                        // 回购
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LEAVEBUY);// 离职回购
                    } else if (!discardDevList.getBuyFlag().booleanValue()) {
                        // 不回购
                        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);// 退库
                        statusInfo.setGroupName(null);
                        statusInfo.setOwner(null);
                    }
                    statusInfo.setApproveType(null);
                    statusInfo.setFormID(null);
                    devDiscardFormDAO.saveOrUpdate(statusInfo);
                }
            }
        }

        // 更新报废单属性
        devDiscardForm.setFormStatus(DevDiscardForm.FORMSTATUS_PUBLISH);
        devDiscardForm.setPassed(true);
        devDiscardForm.setArchiveDate(new Date());
        devDiscardFormDAO.saveOrUpdate(devDiscardForm);

    }

    @Override
    public ListPage<DevDiscardForm> queryAllArchedDiscardForm(DevDiscardQueryParameters qp) {
        return devDiscardFormDAO.queryAllArchedDiscardForm(qp);
    }

    @Override
    public void txSaveDiscardFormValidation(String discardFormId, String devValidateFormId, String deviceID,
            int valiType, Date inDate, String accountID, Date valiDate, boolean isMoney, double deductMoney,
            String remark, String valiDetailStr) {
        if (null == discardFormId) {
            throw new IllegalArgumentException("非法参数：ID不能为空");
        }
        DevDiscardForm devDiscardForm = this.getDevDiscardFormById(discardFormId, false);
        if (null == devDiscardForm) {
            throw new IllegalArgumentException("非法参数：找不到相应的表单");
        }

        DevValidateForm devValidateForm = devValidateFormBiz.txSaveDevValidateForm(devValidateFormId, deviceID,
                valiType, null, accountID, valiDate, false, 0, remark, valiDetailStr);

        devDiscardForm.setDevValidateForm(devValidateForm);
        devDiscardFormDAO.saveOrUpdate(devDiscardForm);
    }

    @Override
    public DevDiscardForm txSaveDevDiscardForm(DevDiscardForm devDiscardForm, Integer formType, String deviceId)
            throws OaException {
        if (deviceId == null) {
            throw new IllegalArgumentException("非法参数：设备ID不能为空");
        }
        if (devDiscardForm == null) {
            throw new IllegalArgumentException("非法参数：找不到相应的表单");
        }
        devDiscardForm.setFormType(formType);// 设置表单类别
        Device device = deviceDAO.findById(deviceId);
        if (device == null) {
            throw new OaException("未找到该设备");
        }
        DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
        if (statusInfo.getDeviceCurStatus() == DeviceCurStatusInfo.STATUS_SCRAP_DISPOSEED
                || statusInfo.getDeviceCurStatus() == DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE) {
            throw new OaException("该设备已经报废或报废未处理");
        }
        if (statusInfo.getApproveType() != null) {
            deviceBiz.txEndTaskByApproveType(device.getId(), statusInfo.getApproveType());
        }

        Calendar calendar = Calendar.getInstance();
        Integer no = devDiscardFormDAO.getMaxSequenceNo(calendar.get(Calendar.YEAR), formType);
        devDiscardForm.setSequenceYear(calendar.get(Calendar.YEAR));
        devDiscardForm.setApplyFormNO(no + 1);
        if (statusInfo.getDeviceCurStatus() == DeviceCurStatusInfo.STATUS_NORMAL
                && (statusInfo.getOwner() != null && statusInfo.getOwner() != "")) {
            txUpdateDevUseReturnBackDate(devDiscardForm);
        }

        if (DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue() == devDiscardForm.getFormType().intValue()) {
            // 报废
            if (DevDiscardForm.DEAL_TYPE_MOVEBACK.equals(devDiscardForm.getDealType())) {
                // 退库
                statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE);
                statusInfo.setGroupName(null);
                statusInfo.setOwner(null);
            } else if (DevDiscardForm.DEAL_TYPE_TAKE.equals(devDiscardForm.getDealType())) {
                statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_TAKE);
            } else {
                statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LOST);
            }
        } else if (DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue() == devDiscardForm.getFormType().intValue()) {
            // 离职处理
            if (DevDiscardForm.LEAVE_DEAL_TYPE_TOSTORAGE.equals(devDiscardForm.getDealType())) {
                // 退库
                statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_NORMAL);
                statusInfo.setGroupName(null);
                statusInfo.setOwner(null);
            } else if (DevDiscardForm.LEAVE_DEAL_TYPE_TAKE.equals(devDiscardForm.getDealType())) {
                // 拿走
                statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LEAVE_TAKE);
            } else {
                // 回购
                statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_LEAVEBUY);// 离职回购
            }
        }

        statusInfo.setApproveType(null);
        statusInfo.setFormID(null);
        statusInfo.setStatusUptDate(new Date());
        devDiscardFormDAO.saveOrUpdate(device);
        devDiscardFormDAO.save(devDiscardForm);

        return devDiscardForm;
    }

    /**
     * 报废时设备领用归还时间
     * 
     * @param allocateForm
     */
    private void txUpdateDevUseReturnBackDate(DevDiscardForm discardForm) {
        if (discardForm != null) {
            for (DiscardDevList list : discardForm.getDiscardDevLists()) {
                if (list != null) {
                    PurchaseDevice purchaseDevice = devUseApplyFormDAO.queryArchPurchaseDevByDeviceID(list.getDevice()
                            .getId(), false);
                    if (purchaseDevice != null && purchaseDevice.getReturnBackDate() == null) {
                        // 更新申购设备的归还日期
                        purchaseDevice.setReturnBackDate(new Date());
                        devUseApplyFormDAO.update(purchaseDevice);
                    } else {
                        // 更新领用里的设备归还日期
                        List<DevPurchaseList> devPurchaseLists_ = devUseApplyFormDAO.queryArchDevUseListByDeviceID(list
                                .getDevice().getId(), false);// 降序排序，第一条就是待更新的记录
                        if (devPurchaseLists_ != null && devPurchaseLists_.size() > 0) {
                            DevPurchaseList latestDevPurList = devPurchaseLists_.get(0);// 第一条就是待更新的记录
                            if (latestDevPurList != null) {
                                latestDevPurList.setReturnBackDate(discardForm.getArchiveDate());// 更新归还日期
                                devUseApplyFormDAO.update(latestDevPurList);
                            }
                        }
                    }

                }
            }
        }
    }

    public List<DiscardDevList> getArchDevScrapListByDeviceID(String deviceID, Boolean archiveDateOrder) {
        return devDiscardFormDAO.queryArchDevScrapListByDeviceID(deviceID, archiveDateOrder);
    }

    @Override
    public List<DevDiscardForm> getArchDevDiscardFormDeviceID(String deviceID) {
        return devDiscardFormDAO.queryArchDevDiscardFormDeviceID(deviceID);
    }

    public IDevUseApplyFormDAO getDevUseApplyFormDAO() {
        return devUseApplyFormDAO;
    }

    public void setDevUseApplyFormDAO(IDevUseApplyFormDAO devUseApplyFormDAO) {
        this.devUseApplyFormDAO = devUseApplyFormDAO;
    }

    @Override
    public DiscardDevList getDiscardDevListByID(String ID) {
        return devDiscardFormDAO.findDiscardDevListByID(ID);
    }

    @Override
    public DevDiscardForm txUpdateDiscardDev(DevDiscardForm devDiscardForm, boolean addFlowVar) throws OaException {
        if (devDiscardForm == null || devDiscardForm.getId() == null) {
            throw new IllegalArgumentException("非法参数：表单不存在");
        }

        if (addFlowVar) {
            // 增加流程环境变量
//            WfmContext context = null;
//            try {
//                Task task = taskDAO.findDealingTasksByFormID(devDiscardForm.getId());
//                if (task != null) {
//                    context = WfmConfiguration.getInstance().getWfmContext();
//                    TaskInstance ti = context.getTaskInstance(task.getTaskInstanceID());
//                    String backBuyFlag = "0";// 是否回购
//                    if (DevDiscardForm.FORM_TYPE_DISCARD_LEAVE.intValue() == devDiscardForm.getFormType().intValue()) {
//                        for (DiscardDevList list : devDiscardForm.getDiscardDevLists()) {
//                            if (DevDiscardForm.LEAVE_DEAL_TYPE_BACKBUY.equals(list.getDealType())) {// 离职回购
//                                backBuyFlag = "1";
//                                break;
//                            }
//                        }
//                        ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_BACKBUYFLAG,
//                                ContextVariable.DATATYPE_STRING, backBuyFlag);
//                        ti.getFlowInstance().addContextVariable(cv);
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                context.rollback();
//                throw new OaException(e.getMessage());
//            } finally {
//                context.close();
//            }
        }
        devDiscardFormDAO.saveOrUpdate(devDiscardForm);
        return devDiscardForm;
    }

    @Override
    public DevDiscardForm txUpdateDiscardByID(String id, Double workYear, Timestamp enterCompanyDate) {
        if (id == null || id == "") {
            throw new IllegalArgumentException("非法参数：表单ID不能为空");
        }
        DevDiscardForm form = devDiscardFormDAO.findById(id);
        form.setWorkYear(workYear);
        form.setEnterCompanyDate(enterCompanyDate);
        devDiscardFormDAO.saveOrUpdate(form);
        return form;
    }

    @Override
    public DevDiscardForm txDraftmanAmend(String id, String applyGroupName, Set<DiscardDevList> discardDevLists)
            throws OaException {
        if (null == id) {
            throw new IllegalArgumentException("非法参数:参数错误");
        }
//        String oldFlowKey = null;
//        // 判断流程是否一致
//        Task task = taskDAO.findDealingTasksByFormID(id);
//        if (task != null) {
//            oldFlowKey = task.getFlowKey();
//        }
        DevDiscardForm form = devDiscardFormDAO.findById(id);
        if (form == null) {
            throw new OaException("表单不存在");
        }
        int flowType;
        if (DevDiscardForm.FORM_TYPE_DISCARD_NORMAL.intValue() == form.getFormType().intValue()) {
            flowType = AreaDeviceCfg.FLOW_TYPE_DEVSCRAP;
        } else {
            flowType = AreaDeviceCfg.FLOW_TYPE_DEVDIMISSION;
        }
        if (discardDevLists != null && discardDevLists.size() > 0) {
            String newFlowKey = getFlowKeyByType(discardDevLists, flowType);
            if (newFlowKey == null) {
                throw new OaException("未配置" + DevPurchaseForm.applyTypeMap.get(flowType) + "流程");
            }
//            if (!newFlowKey.equals(oldFlowKey)) {
//                // 所选择的设备的流程与现在的流程不一致
//                throw new OaException("所选择的设备的" + AreaDeviceCfg.flowMap.get(flowType) + "不一致");
//            }
//            WfmContext context = null;
//            try {
//                // 追加流程环境变量：入库标识
//                context = WfmConfiguration.getInstance().getWfmContext();
//                TaskInstance ti = context.getTaskInstance(task.getTaskInstanceID());
//                String toStorageFlag = "0";
//                for (DiscardDevList list : discardDevLists) {
//                    if (DevDiscardForm.LEAVE_DEAL_TYPE_TOSTORAGE.equals(list.getDealType())) {
//                        toStorageFlag = "1";
//                        break;
//                    }
//                }
//                ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_TOSTORAGEFLAG,
//                        ContextVariable.DATATYPE_STRING, toStorageFlag);
//                ti.getFlowInstance().addContextVariable(cv);
//            } catch (Exception e) {
//                e.printStackTrace();
//                context.rollback();
//                throw new OaException(e.getMessage());
//            } finally {
//                context.close();
//            }
        }

        if (discardDevLists != null) {
            for (DiscardDevList list : discardDevLists) {
                if (list != null && list.getDevDiscardForm() == null) {
                    list.setDevDiscardForm(form);// 设备主表单
                }
            }
        }
        if (form != null) {
            // 设备ID与DiscardDevList组合的Map，用于对设备状态进行操作
            Map<String, DiscardDevList> newDeviceListMap = new HashMap<String, DiscardDevList>();
            if (discardDevLists != null) {
                for (DiscardDevList list : discardDevLists) {
                    if (list != null && list.getDevice() != null) {
                        newDeviceListMap.put(list.getDevice().getId(), list);
                    }
                }
            }

            // 先删除原先的
            if (form.getDiscardDevLists() != null) {
                Iterator<DiscardDevList> discardDevListItr = form.getDiscardDevLists().iterator();
                while (discardDevListItr.hasNext()) {
                    DiscardDevList list = discardDevListItr.next();
                    if (list != null && list.getDevice() != null) {
                        if (list.getDevice().getId() != null && !newDeviceListMap.containsKey(list.getDevice().getId())) {
                            DeviceCurStatusInfo statusInfo = list.getDevice().getDeviceCurStatusInfo();
                            // 在新设备列表中不存在旧的设备ID，说明设备已被删除，要恢复旧单的设备的审批状态为不在审批中
                            statusInfo.setApproveType(null);
                            statusInfo.setFormID(null);
                            devDiscardFormDAO.update(statusInfo);
                        } else {
                            newDeviceListMap.remove(list.getDevice().getId());
                        }
                    }
                    discardDevListItr.remove();
                    list.setDevDiscardForm(null);
                    devDiscardFormDAO.delete(list);
                }
            }

            if (discardDevLists != null) {
                // 追加新设备列表
                form.getDiscardDevLists().addAll(discardDevLists);
                // 保存新单的设备的审批状态。
                for (Entry<String, DiscardDevList> list : newDeviceListMap.entrySet()) {
                    if (list.getValue() != null && list.getValue().getDevice() != null) {
                        DeviceCurStatusInfo statusInfo = list.getValue().getDevice().getDeviceCurStatusInfo();
                        if (form.getFormType() == DevDiscardForm.FORM_TYPE_DISCARD_NORMAL) {
                            statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_SCRAP);// 正常报废
                        } else {
                            statusInfo.setApproveType(DeviceCurStatusInfo.APPROVE_TYPE_LEAVE);// 离职报废
                        }
                        statusInfo.setFormID(form.getId());
                        devDiscardFormDAO.update(statusInfo);
                    }
                }
            }

            form.setApplyGroupName(applyGroupName);
        }
        devDiscardFormDAO.update(form);
        return form;
    }

    public void setDeviceBiz(IDeviceBiz deviceBiz) {
        this.deviceBiz = deviceBiz;
    }

//    @Override
//    public void sendScrapEMail(DevDiscardForm discardForm) throws OaException {
//        if (discardForm != null) {
//            // 读取系统配置
//            SysRuntimeParams srtp = SysRuntimeParams.loadSysRuntimeParams();
//            if (discardForm.getDiscardDevLists() != null && !discardForm.getDiscardDevLists().isEmpty()) {
//                String emailTitle = srtp.getScrapBackTitle();
//                String emailContent = "";
//                String content = srtp.getScrapBackContent();
//                for (DiscardDevList discardDevList : discardForm.getDiscardDevLists()) {
//                    if (DevDiscardForm.DEAL_TYPE_TAKE.equals(discardDevList.getDealType())
//                            && discardDevList.getBuyFlag() != null && discardDevList.getBuyFlag()) {
//                        emailContent += "设备（名称：" + discardDevList.getDevice().getDeviceName() + "，编号："
//                                + discardDevList.getDevice().getDeviceNO() + "）" + "的回购审批已审批通过，您需要在"
//                                + DataFormatUtil.noNullValue(discardDevList.getPlanPayDate(), "yyyy-MM-dd")
//                                + "前向财务部缴款￥" + discardDevList.getBuyPrice() + "；<br>";
//                    } else if (DevDiscardForm.DEAL_TYPE_LOSE.equals(discardDevList.getDealType())) {
//                        emailContent += "设备（名称：" + discardDevList.getDevice().getDeviceName() + "，编号："
//                                + discardDevList.getDevice().getDeviceNO() + "）" + "的丢失赔偿已审批通过，您需要在"
//                                + DataFormatUtil.noNullValue(discardDevList.getPlanPayDate(), "yyyy-MM-dd")
//                                + "前向财务部缴款￥" + discardDevList.getRemaining() + "；<br>";
//                    }
//                }
//                if (emailContent != "" && emailContent.length() > 0) {
//                    content = content.replaceAll("@scrapID", discardForm.getFullFormNO()).replaceAll("@deviceInfo",
//                            emailContent);
//                    // 获取收件人
//                    AddressList addressList = addressListBiz.getByAccountId(discardForm.getApplicant());
//                    if (addressList == null || addressList.getUserEmail() == null) {
//                        log.error("通知对象“" + discardForm.getApplicant() + "”的通讯录不存在");
//                        return;
//                    }
//                    JMailProxy.daemonSend(new MailMessage(addressList.getUserEmail(), emailTitle, content));
//                    log.info("发送邮件地址: " + addressList.getUserEmail() + "。 邮件内容: " + content);
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void sendLeaveEMail(DevDiscardForm discardForm, Integer taskState) throws OaException {
//        if (discardForm != null) {
//            // 读取系统配置
//            SysRuntimeParams srtp = SysRuntimeParams.loadSysRuntimeParams();
//            if (discardForm.getDiscardDevLists() != null && !discardForm.getDiscardDevLists().isEmpty()) {
//                String emailTitle = srtp.getLeaveBackTitle();
//                String emailContent = "";
//                String content = null;
//                if (taskState == 0) {
//                    content = srtp.getLeaveCOMPContent();
//                } else {
//                    content = srtp.getLeaveCEOContent();
//                }
//
//                for (DiscardDevList discardDevList : discardForm.getDiscardDevLists()) {
//                    if (taskState == 0) {
//                        if (discardDevList.getBuyFlag() != null && !discardDevList.getBuyFlag()
//                                && DevDiscardForm.LEAVE_DEAL_TYPE_BACKBUY.equals(discardDevList.getDealType())) {
//                            emailContent += "设备（名称：" + discardDevList.getDevice().getDeviceName() + "，编号："
//                                    + discardDevList.getDevice().getDeviceNO() + "）"
//                                    + "的离职不回购审批已审批通过，人力资源部将会把该设备的不回购款￥" + discardDevList.getBuyPrice()
//                                    + "打入您的工资卡，届时请注意查收；<br>";
//                        }
//                    } else if (taskState == 1) {
//                        if (discardDevList.getBuyFlag() != null && discardDevList.getBuyFlag()) {
//                            emailContent += "设备（名称：" + discardDevList.getDevice().getDeviceName() + "，编号："
//                                    + discardDevList.getDevice().getDeviceNO() + "）" + "的离职回购审批已审批通过，您需要在"
//                                    + DataFormatUtil.noNullValue(discardDevList.getPlanPayDate(), "yyyy-MM-dd")
//                                    + "前向财务部缴款￥" + discardDevList.getBuyPrice() + "；<br>";
//                        }
//                    }
//                }
//                if (emailContent != "" && emailContent.length() > 0) {
//                    content = content.replaceAll("@leaveID", discardForm.getFullFormNO()).replaceAll("@deviceInfo",
//                            emailContent);
//
//                    // 获取收件人
//                    AddressList addressList = addressListBiz.getByAccountId(discardForm.getApplicant());
//                    if (addressList == null || addressList.getUserEmail() == null) {
//                        log.error("通知对象“" + discardForm.getApplicant() + "”的通讯录不存在");
//                        return;
//                    }
//                    JMailProxy.daemonSend(new MailMessage(addressList.getUserEmail(), emailTitle, content));
//                    log.info("发送邮件地址: " + addressList.getUserEmail() + "。 邮件内容: " + content);
//                }
//            }
//        }
//
//    }
}
