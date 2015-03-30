package org.eapp.oa.device.blo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Hibernate;

import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.dao.IDevAllocateFormDAO;
import org.eapp.oa.device.dao.IDevDiscardFormDAO;
import org.eapp.oa.device.dao.IDevUseApplyFormDAO;
import org.eapp.oa.device.dao.IDevValidateFormDAO;
import org.eapp.oa.device.dao.IDeviceClassDAO;
import org.eapp.oa.device.dao.IDeviceDAO;
import org.eapp.oa.device.dto.DeviceQueryParameters;
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
import org.eapp.oa.device.hbean.DeviceProperty;
import org.eapp.oa.device.hbean.DevicePropertyDetail;
import org.eapp.oa.device.hbean.DeviceUpdateLog;
import org.eapp.oa.device.hbean.DeviceValiDetail;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.ExcelExportTools;
import org.eapp.rpc.dto.DataDictInfo;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;


public class DeviceBiz implements IDeviceBiz {

    /**
     * 设备
     */
    private IDeviceDAO deviceDAO;

    /**
     * 设备验证单
     */
    private IDevValidateFormDAO devValidateFormDAO;

    /**
     * 使用设备
     */
//    private IUserDeviceDAO userDeviceDAO;

    /**
     * 设备类型
     */
    private IDeviceClassDAO deviceClassDAO;
    
    /**
     * 设备分配单
     */
    private IDevAllocateFormDAO devAllocateFormDAO;

    /**
     * 设备使用申请单
     */
    private IDevUseApplyFormDAO devUseApplyFormDAO;

    /**
     * 设备废弃单
     */
    private IDevDiscardFormDAO devDiscardFormDAO;

    public void setDeviceDAO(IDeviceDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    public void setDevValidateFormDAO(IDevValidateFormDAO devValidateFormDAO) {
        this.devValidateFormDAO = devValidateFormDAO;
    }

    @Override
    public Device getDeviceById(String id) {
        if (null == id) {
            throw new IllegalArgumentException("参数错误");
        }
        Device dev = deviceDAO.findById(id);
        if (dev != null) {
            if (dev.getDeviceCurStatusInfo() == null) {
                Hibernate.initialize(dev.getDeviceCurStatusInfo());
            }
            Hibernate.initialize(dev.getDevicePropertyDetails());
            Hibernate.initialize(dev.getDevValidateForm());
            if (dev.getDevValidateForm() != null) {
                Hibernate.initialize(dev.getDevValidateForm().getDeviceValiDetails());
            }
            initConfigList(dev);
        }

        return dev;
    }

    @Override
    public Device txAddDevice(Device device) throws OaException {
        if (device == null) {
            throw new IllegalArgumentException();
        }
        DecimalFormat format = new DecimalFormat("0.00");
        // 设置验收单编号
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int no = devValidateFormDAO.findCurrentYearValidFormNO(DevValidateForm.VALITYPE_DIRECT_IN, year);// 登记验收
        DevValidateForm form = device.getDevValidateForm();
        if (form != null) {
            form.setValidFormNO(++no);// 编号序列号
            form.setSequenceYear(year);// 编号年份
        }
        checkDeviceNo(device.getDeviceNO(), device.getAreaCode(), device.getDeviceClass().getId());

        @SuppressWarnings("unused")
        boolean flag = false;
        String content = "本次设备基本信息登记内容：";

        if (device.getDeviceClass() != null) {
            DeviceClass deviceClass = deviceClassDAO.findById(device.getDeviceClass().getId());
            content += "设备类别：“" + deviceClass.getName() + "”；";
            flag = true;
        }

        if (device.getDeviceName() != null) {
            content += "设备名称：“" + device.getDeviceName() + "”；";
            flag = true;
        }
        if (device.getDeviceModel() != null) {
            content += "设备型号：“" + device.getDeviceModel() + "”；";
            flag = true;
        }

        // if(!device_old.getDeviceCurStatusInfo().getDeviceCurStatus().equals(device.getDeviceCurStatusInfo().getDeviceCurStatus())){
        // content+="设备状态、";
        // flag=true;
        // }
        if (device.getBuyType() != null) {
            content += "设备购买方式：“" + device.getBuyTypeName() + "”；";
            flag = true;
        }
        if (device.getBuyTime() != null) {
            content += "设备购买日期：“" + DataFormatUtil.noNullValue(device.getBuyTime(), SysConstants.STANDARD_DATE_PATTERN)
                    + "”；";
            flag = true;
        }
        if (device.getPrice() != null) {
            content += "设备购买金额：“" + format.format(device.getPrice() == null ? 0.0 : device.getPrice()) + "”；";
            flag = true;
        }
        if (device.getFinanceOriginalVal() != null) {

            content += "财务原值：“"
                    + format.format(device.getFinanceOriginalVal() == null ? 0.0 : device.getFinanceOriginalVal())
                    + "”；";
            flag = true;
        }
        if (device.getBuyType() != null) {
            if (device.getBuyType().equals("BUY-TYPE-SUB")) {
                if (device.getDeductFlag() != null) {
                    String deduct = "否";
                    if (device.getDeductFlag()) {
                        deduct = "是";
                    }
                    content += "是否扣款：“" + deduct + "”；";
                    flag = true;
                }
                if (device.getDeductFlag() != null && device.getDeductFlag()) {
                    if (device.getDeductMoney() != null) {
                        content += "扣款金额：“"
                                + format.format(device.getDeductMoney() == null ? 0.0 : device.getDeductMoney()) + "”；";
                        flag = true;
                    }
                    if (device.getInDate() != null) {
                        content += "到款日期：“"
                                + DataFormatUtil.noNullValue(device.getInDate(), SysConstants.STANDARD_DATE_PATTERN)
                                + "”；";
                        flag = true;
                    }
                }
            }
        }
        String propertyStr = "设备配置信息:";
        boolean propertyFlag = false;
        if (device.getDevicePropertyDetails() != null && device.getDevicePropertyDetails().size() > 0) {
            for (DevicePropertyDetail newDetail : device.getDevicePropertyDetails()) {
                String remark = "";
                if (newDetail.getPropertyValue() != null) {
                    remark = newDetail.getPropertyValue();
                }
                propertyStr += "增加属性 “" + newDetail.getPropertyName() + "“ 信息“" + remark + "”；";
                propertyFlag = true;
            }
        }
        if (propertyFlag) {
            content += propertyStr;
        }
        DeviceUpdateLog updateLog = new DeviceUpdateLog();
        updateLog.setDevice(device);
        updateLog.setOperateType(DeviceUpdateLog.OPTTYPE_REG);
        updateLog.setOperateDate(new Date());
        updateLog.setOperator(device.getRegAccountID());

        updateLog.setUpdateContent(content);
        deviceDAO.save(updateLog);
        deviceDAO.save(device);
        return device;
    }

    /**
     * 检查设备名称编号是否重复
     * 
     * @param deviceNo
     * @throws OaException
     */
    private void checkDeviceNo(String deviceNo, String areaCode, String classId) throws OaException {
        int deviceCnt = deviceDAO.getDeviceNumByNo(deviceNo, areaCode, classId);
        if (deviceCnt > 0) {
            throw new OaException("设备编号不能重复");
        }
    }

    /**
     * 检查设备名称名称是否重复
     * 
     * @param deviceNo
     * @throws OaException
     */
    @SuppressWarnings("unused")
    private void checkDeviceName(String areaCode, String classId, String deviceName) throws OaException {
        int deviceCnt = deviceDAO.getDeviceNumByName(areaCode, classId, deviceName);
        if (deviceCnt > 0) {
            throw new OaException("同一类别下设备名称不能重复");
        }
    }

    @Override
    public Device txModifyDevice(Device device) throws OaException {
        boolean flag = false;
        String content = "本次设备基本信息修改内容：";
        if (device == null) {
            throw new IllegalArgumentException();
        }
        DecimalFormat format = new DecimalFormat("0.00");
        Device device_old = deviceDAO.findById(device.getId());
        if (device_old == null) {
            throw new OaException("设备信息不存在");
        }
        Hibernate.initialize(device_old.getDevicePropertyDetails());
        Hibernate.initialize(device_old.getDevValidateForm());
        Set<DevicePropertyDetail> devicePropertyDetails = device_old.getDevicePropertyDetails();
        if (!(device_old.getDeviceClass() == null && device.getDeviceClass() == null)) {
            if (device_old.getDeviceClass() == null
                    || !device_old.getDeviceClass().getId().equals(device.getDeviceClass().getId())) {

                DeviceClass deviceClass = deviceClassDAO.findById(device.getDeviceClass().getId());
                String name = "";
                if (device_old.getDeviceClass().getName() != null) {
                    name = device_old.getDeviceClass().getName();
                }
                content += "设备类别：由“" + name + "”修改为“" + deviceClass.getName() + "”；";
                flag = true;
            }
        }

        if (!(device_old.getDeviceName() == null && device.getDeviceName() == null)) {
            if (device_old.getDeviceName() == null || !device_old.getDeviceName().equals(device.getDeviceName())) {
                String deviceName = "";
                if (device_old.getDeviceName() != null) {
                    deviceName = device_old.getDeviceName();
                }
                content += "设备名称：由“" + deviceName + "”修改为“" + device.getDeviceName() + "”；";
                flag = true;
            }
        }
        if (!(device_old.getDeviceModel() == null && device.getDeviceModel() == null)) {
            if (device_old.getDeviceModel() == null || !device_old.getDeviceModel().equals(device.getDeviceModel())) {
                String deviceModel = "";
                if (device_old.getDeviceModel() != null) {
                    deviceModel = device_old.getDeviceModel();
                }
                content += "设备型号：由“" + deviceModel + "”修改为“" + device.getDeviceModel() + "”；";
                flag = true;
            }
        }

        // if(!device_old.getDeviceCurStatusInfo().getDeviceCurStatus().equals(device.getDeviceCurStatusInfo().getDeviceCurStatus())){
        // content+="设备状态、";
        // flag=true;
        // }
        if (!(device_old.getBuyType() == null && device.getBuyType() == null)) {
            if (device_old.getBuyType() == null || !device_old.getBuyType().equals(device.getBuyType())) {
                String buyTypeName = "";
                if (device_old.getBuyTypeName() != null) {
                    buyTypeName = device_old.getBuyTypeName();
                }
                content += "设备购买方式：由“" + buyTypeName + "”修改为“" + device.getBuyTypeName() + "”；";
                flag = true;
            }
        }
        if (!(device_old.getBuyTime() == null && device.getBuyTime() == null)) {
            if (device_old.getBuyTime() == null || !device_old.getBuyTime().equals(device.getBuyTime())) {
                content += "设备购买日期：由“"
                        + DataFormatUtil.noNullValue(device_old.getBuyTime(), SysConstants.STANDARD_DATE_PATTERN)
                        + "”修改为“" + DataFormatUtil.noNullValue(device.getBuyTime(), SysConstants.STANDARD_DATE_PATTERN)
                        + "”；";
                flag = true;
            }
        }
        if (!(device_old.getPrice() == null && device.getPrice() == null)) {
            if (device_old.getPrice() == null || !device_old.getPrice().equals(device.getPrice())) {
                content += "设备购买金额：由“" + format.format(device_old.getPrice() == null ? 0.0 : device_old.getPrice())
                        + "”修改为“" + format.format(device.getPrice() == null ? 0.0 : device.getPrice()) + "”；";
                flag = true;
            }
        }
        if (!(device_old.getFinanceOriginalVal() == null || device.getFinanceOriginalVal() == null)) {
            if (device_old.getFinanceOriginalVal() == null
                    || !device_old.getFinanceOriginalVal().equals(device.getFinanceOriginalVal())) {
                content += "财务原值：由“"
                        + format.format(device_old.getFinanceOriginalVal() == null ? 0.0 : device_old
                                .getFinanceOriginalVal()) + "”修改为“"
                        + format.format(device.getFinanceOriginalVal() == null ? 0.0 : device.getFinanceOriginalVal())
                        + "”；";
                flag = true;
            }
        }
        if (!(device_old.getBuyType() == null && device.getBuyType() == null)) {
            if (device.getBuyType().equals("BUY-TYPE-SUB")) {
                if (!(device_old.getDeductFlag() == null && device.getDeductFlag() == null)) {
                    if (device_old.getDeductFlag() == null
                            || !device_old.getDeductFlag().equals(device.getDeductFlag())) {
                        String oldDeduct = "否";
                        if (device_old.getDeductFlag() != null && device_old.getDeductFlag()) {
                            oldDeduct = "是";
                        }
                        String deduct = "否";
                        if (device.getDeductFlag()) {
                            deduct = "是";
                        }
                        content += "是否扣款：由“" + oldDeduct + "”修改为“" + deduct + "”；";
                        flag = true;
                    }
                    if (device_old.getDeductFlag() != null
                            && !device_old.getDeductFlag().equals(device.getDeductFlag()) && device.getDeductFlag()) {
                        if (!(device_old.getDeductMoney() == null && device.getDeductMoney() == null)) {
                            if (device_old.getDeductMoney() == null
                                    || !device_old.getDeductMoney().equals(device.getDeductMoney())) {

                                content += "扣款金额：由“"
                                        + format.format(device_old.getDeductMoney() == null ? 0.0 : device_old
                                                .getDeductMoney())
                                        + "”修改为“"
                                        + format.format(device.getDeductMoney() == null ? 0.0 : device.getDeductMoney())
                                        + "”；";
                                flag = true;
                            }
                        }
                        if (!(device_old.getInDate() == null && device.getInDate() == null)) {
                            if (device_old.getInDate() == null || !device_old.getInDate().equals(device.getInDate())) {
                                content += "到款日期：由“"
                                        + DataFormatUtil.noNullValue(device_old.getInDate(),
                                                SysConstants.STANDARD_DATE_PATTERN)
                                        + "”修改为“"
                                        + DataFormatUtil.noNullValue(device.getInDate(),
                                                SysConstants.STANDARD_DATE_PATTERN) + "”；";
                                flag = true;
                            }
                        }
                    } else {
                        if (!(device_old.getDeductMoney() == null && device.getDeductMoney() == null)) {
                            if (device_old.getDeductMoney() == null
                                    || !device_old.getDeductMoney().equals(device.getDeductMoney())) {
                                content += "扣款金额：由“"
                                        + format.format(device_old.getDeductMoney() == null ? 0.0 : device_old
                                                .getDeductMoney())
                                        + "”修改为“"
                                        + format.format(device.getDeductMoney() == null ? 0.0 : device.getDeductMoney())
                                        + "”；";
                                flag = true;
                            }
                        }
                        if (!(device_old.getInDate() == null && device.getInDate() == null)) {
                            if (device_old.getInDate() == null || !device_old.getInDate().equals(device.getInDate())) {
                                content += "到款日期：由“"
                                        + DataFormatUtil.noNullValue(device_old.getInDate(),
                                                SysConstants.STANDARD_DATE_PATTERN)
                                        + "”修改为“"
                                        + DataFormatUtil.noNullValue(device.getInDate(),
                                                SysConstants.STANDARD_DATE_PATTERN) + "”；";
                                flag = true;
                            }
                        }
                    }
                }

            }
        }
        String propertyStr = "设备配置信息：";
        boolean propertyFlag = false;
        if (devicePropertyDetails != null && devicePropertyDetails.size() > 0) {
            Map<String, DevicePropertyDetail> oldPropertyDetailMap = new HashMap<String, DevicePropertyDetail>();
            for (DevicePropertyDetail oldDetail : devicePropertyDetails) {
                if (!oldPropertyDetailMap.containsKey(oldDetail.getPropertyName())) {
                    oldPropertyDetailMap.put(oldDetail.getPropertyName(), oldDetail);
                }
            }

            if (device.getDevicePropertyDetails() != null && device.getDevicePropertyDetails().size() > 0) {
                Map<String, DevicePropertyDetail> newPropertyDetailMap = new HashMap<String, DevicePropertyDetail>();
                for (DevicePropertyDetail devicePropertyDetail : device.getDevicePropertyDetails()) {
                    if (!newPropertyDetailMap.containsKey(devicePropertyDetail.getPropertyName())) {
                        newPropertyDetailMap.put(devicePropertyDetail.getPropertyName(), devicePropertyDetail);
                    }

                }
                for (DevicePropertyDetail oldDetail : devicePropertyDetails) {
                    if (newPropertyDetailMap.containsKey(oldDetail.getPropertyName())) {
                        if (!(oldDetail.getPropertyValue() == null && newPropertyDetailMap.get(
                                oldDetail.getPropertyName()).getPropertyValue() == null)) {
                            if (oldDetail.getPropertyValue() == null
                                    || !oldDetail.getPropertyValue().equals(
                                            newPropertyDetailMap.get(oldDetail.getPropertyName()).getPropertyValue())) {
                                String remark = "";
                                if (oldDetail.getPropertyValue() != null) {
                                    remark = oldDetail.getPropertyValue();
                                }
                                String newRemark = "";
                                if (newPropertyDetailMap.get(oldDetail.getPropertyName()).getPropertyValue() != null) {
                                    newRemark = newPropertyDetailMap.get(oldDetail.getPropertyName())
                                            .getPropertyValue();
                                }
                                propertyStr += "属性“" + oldDetail.getPropertyName() + "”由“" + remark + "”修改为“"
                                        + newRemark + "”；";
                                propertyFlag = true;
                            }
                        }
                    } else {
                        propertyStr += "删除属性“" + oldDetail.getPropertyName() + "”；";
                        propertyFlag = true;
                    }

                }
                for (DevicePropertyDetail newDetail : device.getDevicePropertyDetails()) {
                    if (!oldPropertyDetailMap.containsKey(newDetail.getPropertyName())) {
                        String remark = "";
                        if (newDetail.getPropertyValue() != null) {
                            remark = newDetail.getPropertyValue();
                        }
                        propertyStr += "增加属性“" + newDetail.getPropertyName() + "”由“”修改为“" + remark + "”；";
                        propertyFlag = true;
                    }
                }
            } else {
                for (String string : oldPropertyDetailMap.keySet()) {
                    propertyStr += "删除属性“" + string + "”；";
                    propertyFlag = true;
                }
            }
        } else {
            if (device.getDevicePropertyDetails() != null && device.getDevicePropertyDetails().size() > 0) {
                for (DevicePropertyDetail newDetail : device.getDevicePropertyDetails()) {
                    String remark = "";
                    if (newDetail.getRemark() != null) {
                        remark = newDetail.getPropertyValue();
                    }
                    propertyStr += "增加属性“" + newDetail.getPropertyName() + "“由“”修改为“" + remark + "”";
                    propertyFlag = true;
                }
            }
        }

        // if(!(device_old.getDescription()==null && device.getDescription()==null)){
        // if(device_old.getDescription()==null || !device_old.getDescription().equals(device.getDescription())){
        // content+="设备描述、";
        // flag=true;
        // }
        // }

        if (propertyFlag) {
            content += propertyStr;
        }
        if (!flag && !propertyFlag) {
            content = "本次设备基本信息没有修改内容";
        }
        device_old.getDevicePropertyDetails().clear();
        // 自动终止已经发起的相关流程
//        if (!(device_old.getDeviceClass() == null && device.getDeviceClass() == null)) {
//            if (device_old.getDeviceClass() == null
//                    || !device_old.getDeviceClass().getId().equals(device.getDeviceClass().getId())) {
//
//            }
//        }
        if (!device_old.getDeviceNO().equals(device.getDeviceNO())
                || !device_old.getDeviceClass().getId().equals(device.getDeviceClass().getId())) {
            checkDeviceNo(device.getDeviceNO(), device.getAreaCode(), device.getDeviceClass().getId());
            device_old.setSequence(device.getSequence());
        }
        device_old.setAreaCode(device.getAreaCode());
        device_old.setBuyTime(device.getBuyTime());
        device_old.setDeductMoney(device.getDeductMoney());
        device_old.setDeviceModel(device.getDeviceModel());
        device_old.setDeviceClass(device.getDeviceClass());
        device_old.setDescription(device.getDescription());
        device_old.setPrice(device.getPrice());
        device_old.setBuyType(device.getBuyType());
        device_old.setDeviceNO(device.getDeviceNO());
        device_old.setDeviceName(device.getDeviceName());
        device_old.setDeductFlag(device.getDeductFlag());
        device_old.setDeductMoney(device.getDeductMoney());
        device_old.setInDate(device.getInDate());
        device_old.setFinanceOriginalVal(device.getFinanceOriginalVal());
        if (device.getDevicePropertyDetails() != null) {
            device_old.getDevicePropertyDetails().addAll(device.getDevicePropertyDetails());
        }
        if (device_old.getDevValidateForm() != null) {

            deviceDAO.delete(device_old.getDevValidateForm());
        }

        device_old.setDevValidateForm(device.getDevValidateForm());
        deviceDAO.update(device_old);
        DeviceUpdateLog updateLog = new DeviceUpdateLog();
        updateLog.setDevice(device);
        updateLog.setOperateType(DeviceUpdateLog.OPTTYPE_UPDATE);
        updateLog.setOperateDate(new Date());
        updateLog.setOperator(device.getRegAccountID());
        txEndTaskByApproveType(device_old.getId(), device_old.getDeviceCurStatusInfo().getApproveType());
        updateLog.setUpdateContent(content);
        deviceDAO.save(updateLog);
        return device_old;
    }

    @Override
    public Device txDeleteDevice(String id) throws OaException {
        if (id == null) {
            return null;
        }
        Device device = deviceDAO.findById(id);
        if (device == null) {
            throw new OaException("该设备不存在!");
        }
        DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
        if (statusInfo != null && statusInfo.getApproveType() != null) {
            txEndTaskByApproveType(id, statusInfo.getApproveType());
        }
        // 更新设备当前状态表

        if (statusInfo != null) {
            statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_DELETE);
            statusInfo.setApproveType(null);
            statusInfo.setFormID(null);
        } else {
            statusInfo = new DeviceCurStatusInfo();
            statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_DELETE);
            device.setDeviceCurStatusInfo(statusInfo);
        }
        deviceDAO.update(device);
        // if(device.getStatus()==Device.STATUS_NORMAL){
        //
        // }else{
        // throw new OaException("查询结果为空!");
        // }

        return device;
    }

    @Override
    public List<DataDictInfo> getDeviceTypeAll(String deviceClass) {
        Collection<DataDictInfo> dicts = SysCodeDictLoader.getInstance().getDeviceType().values();
        if (dicts == null || deviceClass == null) {
            return null;
        }
        for (DataDictInfo dto : dicts) {
            if (deviceClass.equals(dto.getDictCode())) {
                return dto.getChildDataDicts();
            }
        }
        return null;
    }

    /**
     * 获得设备类型(区分配件与非配件)
     */
    public List<DataDictInfo> getDeviceType(String deviceClass, String flag) {
        if (deviceClass == null) {
            return null;
        }

        List<DataDictInfo> dicts = getDeviceTypeAll(deviceClass);
        if (dicts == null) {
            return null;
        }
        if (flag == null) {
            return dicts;
        }
        // String typeValue="0";
        // if (isAccessory){
        // typeValue = "1";
        // }
        List<DataDictInfo> listDto = new ArrayList<DataDictInfo>();
        for (DataDictInfo dto : dicts) {
            if (dto.getCeilValue().equalsIgnoreCase(flag)) {
                listDto.add(dto);
            }
        }
        return listDto;
    }

    /**
     * 获得设备属性
     */
    @Override
    public Collection<DeviceProperty> getDeviceProperty(String id, String deviceClass, String deviceType, boolean merge) {
        HashMap<String, DeviceProperty> dpMap = new HashMap<String, DeviceProperty>();
//        if (id != null) {
//            // 已有属性
//            // Device d = deviceDAO.findById(id);
//            // for (DeviceProperty dp : d.getDeviceProperty()) {
//            // dpMap.put(dp.getPropertyName(), dp);
//            // }
//        }

        // 开始查找字典中配置的属性
        if (deviceType == null || deviceClass == null || !merge) {
            return dpMap.values();
        }

        List<DataDictInfo> dicts = getDeviceType(deviceClass, "0");
        if (dicts == null) {
            return dpMap.values();
        }
        List<DataDictInfo> finded = null;
        for (DataDictInfo dto : dicts) {
            if (deviceType.equals(dto.getDictCode())) {
                finded = dto.getChildDataDicts();
                break;
            }
        }
        if (finded == null) {
            return dpMap.values();
        }

        // 合并字典中新的属性
        // for (DataDictInfo dto : finded) {
        // if (!dpMap.containsKey(dto.getDictKey())) {
        // int propertyType = DeviceProperty.PROPERTYTYPE_V;
        // try {
        // propertyType = Integer.parseInt(dto.getFloorValue());
        // } catch(Exception e) {

        // }
        // dpMap.put(dto.getDictKey(), new DeviceProperty(propertyType, dto.getDictKey()));
        // }
        // }
        return dpMap.values();
    }

    @Override
    public Collection<DeviceValiDetail> getDeviceValiDetail(String id, int valiType, String dictKey) {
        HashMap<String, DeviceValiDetail> dpMap = new HashMap<String, DeviceValiDetail>();
        if (id != null) {
            // 已有属性
//            List<DevValidateForm> list = devValidateFormDAO.findByDeviceIdAndValiType(valiType, id);
            // if (list != null && list.size() > 0) {
            // for (DeviceValiDetail dp : list.get(0).getDeviceValiDetail()) {
            // dpMap.put(dp.getItem(), dp);
            // }
            // }
        }

        // 开始查找字典中配置的属性
        if (dictKey == null) {
            return dpMap.values();
        }

        List<DataDictInfo> dicts = SysCodeDictLoader.getInstance().getSubDeviceValiType(dictKey);
        if (dicts == null) {
            return dpMap.values();
        }

        // 合并字典中新的属性
//        for (DataDictInfo dto : dicts) {
//             if (!dpMap.containsKey(dto.getDescription())) {
//             // dpMap.put(dto.getDescription(), new DeviceValiDetail(dto.getDescription()));
//             }
//        }
        return dpMap.values();
    }

    @Override
    public ListPage<Device> getDeviceListPage(DeviceQueryParameters qp, List<String> deviceClass) {
        ListPage<Device> page = new ListPage<Device>();
        if (qp == null) {
            throw new IllegalArgumentException();
        }
        if (deviceClass == null || deviceClass.isEmpty()) {
            return page;
        }
        page = deviceDAO.queryDeviceListPage(qp, deviceClass);
        if (page.getDataList() != null) {
            for (Device device : page.getDataList()) {
                Hibernate.initialize(device.getDeviceClass());
            }
        }
        return page;
    }

    @Override
    public void txDiscardITDevice(String id) {
        Device device = deviceDAO.findById(id);
        if (device == null || device.getDeviceCurStatusInfo() == null) {
            return;
        }
        DeviceCurStatusInfo statusInfo = device.getDeviceCurStatusInfo();
        statusInfo.setDeviceCurStatus(DeviceCurStatusInfo.STATUS_SCRAP_UNDISPOSE);
        statusInfo.setGroupName(null);
        statusInfo.setOwner(null);
        deviceDAO.update(device);
        // UserDevice userDevice = device.getUserDevice();
        // if(userDevice !=null){ //解绑定
        // UserDeviceDetailId detailId = new UserDeviceDetailId(userDevice,device);
        // UserDeviceDetail detail = userDeviceDAO.findUserDeviceDetailById(detailId);
        // if(null != detail) {
        // deviceDAO.delete(detail);
        // }
        // }
    }

    @Override
    public void txDiscardMeetDevice(String id) {
        Device device = getDeviceById(id);
        if (device == null) {
            return;
        }
        // device.setStatus(Device.STATUS_DISCARD);
        deviceDAO.update(device);
    }

    @Override
    public Collection<DataDictInfo> getDeviceClass() {
        Collection<DataDictInfo> deviceClassList = SysCodeDictLoader.getInstance().getDeviceType().values();
        if (deviceClassList == null)
            return null;
        return deviceClassList;
    }

    @Override
    public void txUpdateDeviceUseNumber(String id, Integer useNumber) {
        Device device = this.getDeviceById(id);
        if (device == null) {
            return;
        }
        // device.setUseNumber(device.getUseNumber() + useNumber);
        deviceDAO.saveOrUpdate(device);
    }

    public List<Device> queryDeviceBorrowInfo(String id, String deviceClass, Timestamp beginTime, Timestamp endTime) {
        return deviceDAO.queryDeviceBorrowInfo(id, deviceClass, beginTime, endTime);
    }

    public List<Device> getUnbindITDevice(String deviceType, String deviceClass, String status, boolean isAccessory) {
        return deviceDAO.findUnbindITDevice(deviceType, deviceClass, status, isAccessory);
    }

    public void queryDeviceToExcel(ListPage<Device> listPage, String fileName, File file) throws IOException,
            OaException {
        if (listPage == null || fileName == null || file == null) {
            throw new IllegalArgumentException();
        }

        if (listPage.getTotalCount() <= 0) {
            throw new OaException("查询结果为空!");
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        ExcelExportTools tools = new ExcelExportTools(wb);
        // 标题样式
        HSSFCellStyle cellStyle = tools.getCellStyle(null, true, true);
        // 普通内容样式
        // HSSFCellStyle contentCellStyle = tools.getCellStyle(null, false, false);

        // List<Device> data=listPage.getDataList();
        // 创建一个工作簿

        HSSFSheet sheet = wb.createSheet("设备记录");

        // 创建标题列
        String[] titles = { "设备编号", "使用人", "部门", "设备型号", "设备类型", "硬件信息", "购买日期" };

        HSSFRow row = sheet.createRow((short) 0);
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titles[i]));
            cell.setCellStyle(cellStyle);
            ;
        }
        // 把数据写入Sheet
        // writeToSheet(sheet,data,contentCellStyle);
        File path = new File(file, fileName);
        file.mkdirs();
        path.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();
    }

    private void writeToSheet(HSSFSheet sheet, List<Device> data, String[] values, HSSFCellStyle contentStyle) {
        if (data != null) {
//            DecimalFormat format = new DecimalFormat("0.00");
            for (Device ol : data) {
                HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
                for (int i = 0; i < values.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellStyle(contentStyle);
                    if ("area-name".equals(values[i])) {
                        // 财务隶属
                        cell.setCellValue(new HSSFRichTextString(ol.getAreaName()));
                    } else if ("device-no".equals(values[i])) {
                        // 设备编号
                        cell.setCellValue(new HSSFRichTextString(ol.getDeviceNO()));
                    } else if ("device-type-str".equals(values[i])) {
                        // 资产类别
                        cell.setCellValue(new HSSFRichTextString(ol.getDeviceTypeName()));
                    } else if ("device-name".equals(values[i])) {
                        // 设备名称
                        cell.setCellValue(new HSSFRichTextString(ol.getDeviceName()));
                    } else if ("device-model".equals(values[i])) {
                        // 设备型号
                        cell.setCellValue(new HSSFRichTextString(ol.getDeviceModel()));
                    } else if ("device-class-str".equals(values[i])) {
                        // 设备类别
                        cell.setCellValue(new HSSFRichTextString(ol.getDeviceClass().getName()));
                    } else if ("status-str".equals(values[i])) {
                        // 设备状态
                        cell.setCellValue(new HSSFRichTextString(ol.getDeviceCurStatusInfo().getDeviceCurStatusStr()));
                    } else if ("buy-time".equals(values[i])) {
                        // 购买时间
                        cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(ol.getBuyTime())));
                    } else if ("price".equals(values[i])) {
                        // 价格
                        if (ol.getPrice() != null) {
                            cell.setCellValue(ol.getPrice());
                        } else {
                            cell.setCellValue(0.0);
                        }
                    } else if ("remaining".equals(values[i])) {
                        // 余值
                        if (ol.getRemaining() != null) {
                            cell.setCellValue(ol.getRemaining());
                        } else {
                            cell.setCellValue(0.0);
                        }
                    } else if ("buyPrice".equals(values[i])) {
                        // 回收价格
                        if (ol.getBuyPrice() != null) {
                            cell.setCellValue(ol.getBuyPrice());
                        } else {
                            cell.setCellValue(0.0);
                        }
                    } else if ("config-list".equals(values[i])) {
                        // 配置信息
                        if (ol.getConfigList() != null) {
                            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(ol.getConfigList())));
                        } else {
                            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue("")));
                        }
                    } else if ("work-userName".equals(values[i])) {
                        // 使用人
                        if (ol.getDeviceCurStatusInfo() != null) {
                            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(ol
                                    .getDeviceCurStatusInfo().getOwnerName())));
                        }
                    } else if ("work-groupName".equals(values[i])) {
                        // 使用部门
                        if (ol.getDeviceCurStatusInfo() != null) {
                            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(ol
                                    .getDeviceCurStatusInfo().getGroupName())));
                        }
                    } else if ("buy-type-str".equals(values[i])) {
                        // 购买方式
                        cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(DevPurchaseForm
                                .getBuyTypeDisplayName(ol.getBuyType()))));
                    } else if ("deduct-flag".equals(values[i])) {
                        // 是否扣款
                        if ("BUY-TYPE-SUB".equals(ol.getBuyType())) {
                            if (ol.getDeductFlag() != null && ol.getDeductFlag()) {
                                cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue("是")));
                            } else {
                                cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue("否")));
                            }

                        } else {
                            cell.setCellValue(new HSSFRichTextString(""));
                        }
                    } else if ("deduct-money".equals(values[i])) {
                        // 扣款金额
                        if ("BUY-TYPE-SUB".equals(ol.getBuyType()) && ol.getDeductFlag() != null && ol.getDeductFlag()) {
                            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(ol.getDeductMoney())));
                        } else {
                            cell.setCellValue(new HSSFRichTextString(""));
                        }
                    } else if ("in-date".equals(values[i])) {
                        // 扣款金额
                        if ("BUY-TYPE-SUB".equals(ol.getBuyType()) && ol.getDeductFlag() != null && ol.getDeductFlag()) {
                            cell.setCellValue(new HSSFRichTextString(DataFormatUtil.noNullValue(ol.getInDate())));
                        } else {
                            cell.setCellValue(new HSSFRichTextString(""));
                        }
                    } else if ("finance-original-val".equals(values[i])) {
                        // 财务原值
                        if (ol.getFinanceOriginalVal() != null) {
                            cell.setCellValue(ol.getFinanceOriginalVal());
                        } else {
                            cell.setCellValue(0.0);
                        }
                    }

                }

            }
        }
    }

    public List<Device> queryDevicesByApplicantID(String applicantID) {
        return deviceDAO.findDevicesByApplicantID(applicantID);
    }

    public ListPage<Device> queryDevicePageByApplicantID(DeviceQueryParameters qp, String applicantID) {
        ListPage<Device> devicePage = deviceDAO.findDevicePageByApplicantID(qp, applicantID);
        if (devicePage != null && devicePage.getDataList() != null) {
            for (Device d : devicePage.getDataList()) {
                DeviceClass deviceClass = d.getDeviceClass();
                if (deviceClass != null) {
                    d.setDeviceClassDisplayName(deviceClass.getName());
                }
            }
        }
        return devicePage;
    }

    @Override
    public List<Device> queryDeviceInfo(String deviceClass) {
        return deviceDAO.findDeviceInfo(deviceClass);
    }

    @Override
    public int getDeviceSepNum(String classID, String areaCode) {
        return deviceDAO.getDeviceSepNum(classID, areaCode);
    }

    public ListPage<Device> queryDevices(DeviceQueryParameters qp, String ownerAccountID, boolean assignDevClassFlag,
            List<String> assignDeviceClassIDs, boolean mergeFlag, Boolean approvingFlag, Boolean excludeScrapFlag,
            List<String> deletedIDsAtReject, boolean initDevCfgList) {
        ListPage<Device> devicePage = deviceDAO.findDevices(qp, ownerAccountID, assignDevClassFlag,
                assignDeviceClassIDs, mergeFlag, approvingFlag, excludeScrapFlag, deletedIDsAtReject);
        if (devicePage != null && devicePage.getDataList() != null) {
            for (Device d : devicePage.getDataList()) {
                Hibernate.initialize(d.getDeviceClass());
                if (initDevCfgList) {
                    initConfigList(d);
                }
            }
        }
        return devicePage;
    }

    public List<Device> queryDevicesByIDs(List<String> deviceIDList, boolean initDevCfgList) {
        List<Device> devices = deviceDAO.findDevicesByIDs(deviceIDList);
        if (devices != null) {
            if (initDevCfgList) {
                for (Device device : devices) {
                    initConfigList(device);
                }
            }
        }
        return devices;
    }

    @Override
    public ListPage<Device> getStatisticsDeviceListPage(DeviceQueryParameters qp, List<String> deviceClass,
            List<String> deviceAreaCodes, List<String> deviceTypes) {
        ListPage<Device> page = new ListPage<Device>();
        if (qp == null) {
            throw new IllegalArgumentException();
        }
        if (deviceClass == null || deviceClass.isEmpty()) {
            return page;
        }
        page = deviceDAO.getStatisticsDeviceListPage(qp, deviceClass, deviceAreaCodes, deviceTypes);
        if (page.getDataList() != null) {
            for (Device device : page.getDataList()) {
                Hibernate.initialize(device.getDeviceClass());
                if (device.getDiscardDevLists() != null) {
                    Hibernate.initialize(device.getDiscardDevLists());
                    for (DiscardDevList devList : device.getDiscardDevLists()) {
                        Hibernate.initialize(devList.getDevDiscardForm());
                        if (devList.getDevDiscardForm() != null
                                && devList.getDevDiscardForm().getFormStatus() == DevFlowApplyProcess.FORMSTATUS_PUBLISH) {
                            device.setRemaining(devList.getRemaining());
                            device.setBuyPrice(devList.getBuyPrice());
                            break;
                        }
                    }
                }
                this.initConfigList(device);
            }
        } else {
            page.setCurrentPageNo(1);
        }
        return page;
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

    public IDeviceClassDAO getDeviceClassDAO() {
        return deviceClassDAO;
    }

    public void setDeviceClassDAO(IDeviceClassDAO deviceClassDAO) {
        this.deviceClassDAO = deviceClassDAO;
    }

    @Override
    public void csExportDevice(DeviceQueryParameters qp, List<String> deviceClass, List<String> deviceTypes,
            List<String> deviceAreaCodes, String expNameAndValue, String fileName, File dir) throws IOException,
            OaException {
        if (qp == null || dir == null || fileName == null || expNameAndValue == null) {
            throw new IllegalArgumentException();
        }
        // 设置查询一次为500条数据
        qp.setPageSize(0);
        // 取得数据
        ListPage<Device> listPage = this.getStatisticsDeviceListPage(qp, deviceClass, deviceTypes, deviceAreaCodes);
        if (listPage.getTotalCount() <= 0) {
            throw new OaException("查询结果为空!");
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        ExcelExportTools tools = new ExcelExportTools(wb);
        // 标题样式
        HSSFCellStyle cellStyle = tools.getCellStyle(null, true, true);
        // 普通内容样式
        HSSFCellStyle contentCellStyle = tools.getCellStyle(null, false, false);

        List<Device> data = listPage.getDataList();
        // 创建一个工作簿

        HSSFSheet sheet = wb.createSheet("设备");
        // 创建标题列

        String[] nameAndValue = expNameAndValue.split(";");
        String[] titles = new String[nameAndValue.length];
        String[] values = new String[nameAndValue.length];
        for (int i = 0; i < nameAndValue.length; i++) {
            String[] str = nameAndValue[i].split(",");
            titles[i] = str[0];
            values[i] = str[1];
        }
        HSSFRow row = sheet.createRow((short) 0);
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titles[i]));
            cell.setCellStyle(cellStyle);
        }
        // 把数据写入Sheet
        writeToSheet(sheet, data, values, contentCellStyle);
        File path = new File(dir, fileName);
        dir.mkdirs();
        path.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();
    }

    @Override
    public boolean validDeviceSameType(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            throw new IllegalArgumentException("参数错误");
        }
        List<Device> devices = deviceDAO.findDevicesByIDs(ids);
        Set<String> deviceTypeIDs = new HashSet<String>();
        if (devices != null) {
            for (Device d : devices) {
                if (d != null) {
                    deviceTypeIDs.add(d.getDeviceType());
                }
            }
        }
        return deviceTypeIDs.size() == 0 || deviceTypeIDs.size() == 1;
    }

    @Override
    public ListPage<Device> getDeviceListPage(DeviceQueryParameters qp) {
        ListPage<Device> devicePage = deviceDAO.getDeviceListPage(qp);
        if (devicePage != null && devicePage.getDataList() != null) {
            for (Device d : devicePage.getDataList()) {
                Hibernate.initialize(d.getDeviceClass());
                initConfigList(d);
            }
        }
        return devicePage;
    }

    public void endTask(String formID, WfmContext context) {
        // 结束流程
//        Task task = taskBiz.getDealingTaskByFormID(formID);
//        if (task != null) {
//            context.getFlowInstance(task.getFlowInstanceID()).end();
//            taskBiz.txEndTask(task.getTaskInstanceID(), "ps_end", new Date(), "由系统自动强行作废----此日志由系统自动发出");
//        }
    }

    public void txEndTaskByApproveType(String deviceId, Integer approveType) {
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try{
            if (approveType != null) {
                if (approveType == DeviceCurStatusInfo.APPROVE_TYPE_PURCH) {
                    // 申购审批中;
                    List<DevPurchaseForm> list = devUseApplyFormDAO.queryDealDevPurchaseFormByDeviceID(deviceId,
                            DevPurchaseForm.APPLY_TYPE_PURCHASE);
                    if (list != null && list.size() > 0) {
                        for (DevPurchaseForm devPurchaseForm : list) {
                            endTask(devPurchaseForm.getId(), context);
                            devPurchaseForm.setArchiveDate(new Date());
                            devPurchaseForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                            devUseApplyFormDAO.saveOrUpdate(devPurchaseForm);
                        }
                    }
                    
                } else if (approveType == DeviceCurStatusInfo.APPROVE_TYPE_USE) {
                    // 领用审批中;
                    List<DevPurchaseForm> list = devUseApplyFormDAO.queryDealDevPurchaseFormByDeviceID(deviceId,
                            DevPurchaseForm.APPLY_TYPE_USE);
                    if (list != null && list.size() > 0) {
                        for (DevPurchaseForm devPurchaseForm : list) {
                            Hibernate.initialize(devPurchaseForm.getDevPurchaseLists());
                            if (devPurchaseForm.getDevPurchaseLists() != null) {
                                for (DevPurchaseList devPurchaseList : devPurchaseForm.getDevPurchaseLists()) {
                                    Hibernate.initialize(devPurchaseList.getDevice());
                                    if (devPurchaseList.getDevice() != null
                                            && devPurchaseList.getDevice().getDeviceCurStatusInfo() != null) {
                                        DeviceCurStatusInfo deviceCurStatusInfo = devPurchaseList.getDevice()
                                                .getDeviceCurStatusInfo();
                                        deviceCurStatusInfo.setApproveType(null);
                                        deviceCurStatusInfo.setFormID(null);
                                        devUseApplyFormDAO.update(deviceCurStatusInfo);
                                    }
                                }
                            }
                            endTask(devPurchaseForm.getId(), context);
                            devPurchaseForm.setArchiveDate(new Date());
                            devPurchaseForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                            devUseApplyFormDAO.saveOrUpdate(devPurchaseForm);
                        }
                    }
                } else if (approveType == DeviceCurStatusInfo.APPROVE_TYPE_ALLOT) {
                    // 调拨审批中;
                    List<DevAllocateForm> list = devAllocateFormDAO.queryDealDevAllocateFormByDeviceID(deviceId);
                    if (list != null && list.size() > 0) {
                        for (DevAllocateForm allocateForm : list) {
                            Hibernate.initialize(allocateForm.getDevAllocateLists());
                            if (allocateForm.getDevAllocateLists() != null) {
                                for (DevAllocateList devAllocateList : allocateForm.getDevAllocateLists()) {
                                    Hibernate.initialize(devAllocateList.getDevice());
                                    if (devAllocateList.getDevice() != null
                                            && devAllocateList.getDevice().getDeviceCurStatusInfo() != null) {
                                        DeviceCurStatusInfo deviceCurStatusInfo = devAllocateList.getDevice()
                                                .getDeviceCurStatusInfo();
                                        deviceCurStatusInfo.setApproveType(null);
                                        deviceCurStatusInfo.setFormID(null);
                                        devUseApplyFormDAO.update(deviceCurStatusInfo);
                                    }
                                }
                            }
                            endTask(allocateForm.getId(), context);
                            allocateForm.setArchiveDate(new Date());
                            allocateForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                            devAllocateFormDAO.saveOrUpdate(allocateForm);
                        }
                    }
                } else if (approveType == DeviceCurStatusInfo.APPROVE_TYPE_SCRAP) {
                    // 报废审批中;
                    List<DevDiscardForm> list = devDiscardFormDAO.queryDealDevDiscardFormByDeviceID(deviceId,
                            DevDiscardForm.FORM_TYPE_DISCARD_NORMAL);
                    if (list != null && list.size() > 0) {
                        for (DevDiscardForm discardForm : list) {
                            if (discardForm.getDiscardDevLists() != null) {
                                for (DiscardDevList discardDevList : discardForm.getDiscardDevLists()) {
                                    Hibernate.initialize(discardDevList.getDevice());
                                    if (discardDevList.getDevice() != null
                                            && discardDevList.getDevice().getDeviceCurStatusInfo() != null) {
                                        DeviceCurStatusInfo deviceCurStatusInfo = discardDevList.getDevice()
                                                .getDeviceCurStatusInfo();
                                        deviceCurStatusInfo.setApproveType(null);
                                        deviceCurStatusInfo.setFormID(null);
                                        devUseApplyFormDAO.update(deviceCurStatusInfo);
                                    }
                                }
                            }
                            endTask(discardForm.getId(), context);
                            discardForm.setArchiveDate(new Date());
                            discardForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                            devDiscardFormDAO.saveOrUpdate(discardForm);
                        }
                    }
                } else if (approveType == DeviceCurStatusInfo.APPROVE_TYPE_LEAVE) {
                    // 离职审批中;
                    List<DevDiscardForm> list = devDiscardFormDAO.queryDealDevDiscardFormByDeviceID(deviceId,
                            DevDiscardForm.FORM_TYPE_DISCARD_LEAVE);
                    if (list != null && list.size() > 0) {
                        for (DevDiscardForm discardForm : list) {
                            if (discardForm.getDiscardDevLists() != null) {
                                for (DiscardDevList discardDevList : discardForm.getDiscardDevLists()) {
                                    Hibernate.initialize(discardDevList.getDevice());
                                    if (discardDevList.getDevice() != null
                                            && discardDevList.getDevice().getDeviceCurStatusInfo() != null) {
                                        DeviceCurStatusInfo deviceCurStatusInfo = discardDevList.getDevice()
                                                .getDeviceCurStatusInfo();
                                        deviceCurStatusInfo.setApproveType(null);
                                        deviceCurStatusInfo.setFormID(null);
                                        devUseApplyFormDAO.update(deviceCurStatusInfo);
                                    }
                                }
                            }
                            endTask(discardForm.getId(), context);
                            discardForm.setArchiveDate(new Date());
                            discardForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                            devDiscardFormDAO.saveOrUpdate(discardForm);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
    }

    public void txEndTaskByPurpose(String deviceType, String areaCode, String deviceClass, String purpose, String userId) {
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try{
            // 申购审批中;
            List<DevPurchaseForm> devPurchaseForms = devUseApplyFormDAO.getDevPurchaseFormDealPurchaseByPurpose(deviceType,
                    areaCode, deviceClass, userId);
            if (devPurchaseForms != null && devPurchaseForms.size() > 0) {
                for (DevPurchaseForm devPurchaseForm : devPurchaseForms) {
                    endTask(devPurchaseForm.getId(), context);
                    devPurchaseForm.setArchiveDate(new Date());
                    devPurchaseForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                    devUseApplyFormDAO.saveOrUpdate(devPurchaseForm);
                }
            }
            // 领用审批中;
            List<DevPurchaseForm> devPurchaseFormList = devUseApplyFormDAO.getDevPurchaseFormDealUseByPurpose(deviceType,
                    areaCode, deviceClass, userId);
            if (devPurchaseFormList != null && devPurchaseFormList.size() > 0) {
                for (DevPurchaseForm devPurchaseForm : devPurchaseFormList) {
                    Hibernate.initialize(devPurchaseForm.getDevPurchaseLists());
                    if (devPurchaseForm.getDevPurchaseLists() != null) {
                        for (DevPurchaseList devPurchaseList : devPurchaseForm.getDevPurchaseLists()) {
                            Hibernate.initialize(devPurchaseList.getDevice());
                            if (devPurchaseList.getDevice() != null
                                    && devPurchaseList.getDevice().getDeviceCurStatusInfo() != null) {
                                DeviceCurStatusInfo deviceCurStatusInfo = devPurchaseList.getDevice()
                                        .getDeviceCurStatusInfo();
                                deviceCurStatusInfo.setApproveType(null);
                                deviceCurStatusInfo.setFormID(null);
                                devUseApplyFormDAO.update(deviceCurStatusInfo);
                            }
                        }
                    }
                    endTask(devPurchaseForm.getId(), context);
                    devPurchaseForm.setArchiveDate(new Date());
                    devPurchaseForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                    devUseApplyFormDAO.saveOrUpdate(devPurchaseForm);
                }
            }
            // 调拨审批中;
            List<DevAllocateForm> list = devAllocateFormDAO.getDevAllocateFormDealDevAllocateByPurpose(deviceType,
                    areaCode, deviceClass, userId);
            if (list != null && list.size() > 0) {
                for (DevAllocateForm allocateForm : list) {
                    Hibernate.initialize(allocateForm.getDevAllocateLists());
                    if (allocateForm.getDevAllocateLists() != null) {
                        for (DevAllocateList devAllocateList : allocateForm.getDevAllocateLists()) {
                            Hibernate.initialize(devAllocateList.getDevice());
                            if (devAllocateList.getDevice() != null
                                    && devAllocateList.getDevice().getDeviceCurStatusInfo() != null) {
                                DeviceCurStatusInfo deviceCurStatusInfo = devAllocateList.getDevice()
                                        .getDeviceCurStatusInfo();
                                deviceCurStatusInfo.setApproveType(null);
                                deviceCurStatusInfo.setFormID(null);
                                devUseApplyFormDAO.update(deviceCurStatusInfo);
                            }
                        }
                    }
                    endTask(allocateForm.getId(), context);
                    allocateForm.setArchiveDate(new Date());
                    allocateForm.setFormStatus(DevFlowApplyProcess.FORMSTATUS_CANCELLATION);
                    devAllocateFormDAO.saveOrUpdate(allocateForm);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Integer> getDeviceUseCount(String deviceType, String areaCode, String deviceClass,
            String purpose, String userId, boolean manyTimesFlag) {
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        if (manyTimesFlag) {
            // 查询设备库存表
            List<Device> list = deviceDAO.getDeviceListByPuerpose(deviceType, areaCode, deviceClass, purpose, userId);
            if (list != null && list.size() > 0) {
                for (Device device : list) {
                    String areaCodeStr = device.getAreaCode();
                    if (countMap.get(areaCodeStr) != null) {
                        countMap.put(areaCodeStr, countMap.get(areaCodeStr) + 1);
                    } else {
                        countMap.put(areaCodeStr, 1);
                    }
                }
            }
        }
        return countMap;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getCheckDevicePurchase(String deviceType, String areaCode, String deviceClass,
            String deviceClassName, String purpose, String userId, Boolean manyTimesFlag, Integer formType,
            Boolean managerFlag) {
        //
        Map<String, String> map = new HashMap<String, String>();
//        String purposeName = "";
//        Map<String, DataDictInfo> devUseTypeMap = SysCodeDictLoader.getInstance().getDevUseTypeMap();
//        if (devUseTypeMap != null) {
//            DataDictInfo dict = devUseTypeMap.get(purpose);
//            if (dict != null) {
//                String dictKey = dict.getDictName();
//                if (dictKey != null) {
//                    purposeName = dictKey;
//                }
//            }
//        }
        String msg = "";
        boolean flag = true;
        // 查询设备库存表
        List<Device> list = deviceDAO.getDeviceListByPuerpose(deviceType, areaCode, deviceClass, purpose, userId);
        if (list != null && list.size() > 0) {

            msg = "设备类别名称为【" + deviceClassName + "】已领用" + list.size() + "台，";
            map.put(purpose + "_-1", msg);
        }
        if (!managerFlag) {
            if (flag) {
                // 领用审批中
                List<DevPurchaseList> devPurchaseLists = devUseApplyFormDAO.getDeviceDealUseByPurpose(deviceType,
                        areaCode, deviceClass, userId);
                if (devPurchaseLists != null && devPurchaseLists.size() > 0) {
                    msg = "设备类别名称为【" + deviceClassName + "】领用流程，";
                    map.put(purpose + "_0", msg);
                }
            }
            if (flag) {
                // 调拨审批中
                List<DevAllocateList> devPurchaseLists = devAllocateFormDAO.getDeviceDealDevAllocateByPurpose(
                        deviceType, areaCode, deviceClass, userId);
                if (devPurchaseLists != null && devPurchaseLists.size() > 0) {
                    msg = "设备类别名称为【" + deviceClassName + "】调拨流程，";
                    map.put(purpose + "_2", msg);
                }
            }
            if (flag) {
                // 申购审批中
                List<PurchaseDevice> devPurchaseLists = devUseApplyFormDAO.getDeviceDealPurchaseByPurpose(deviceType,
                        areaCode, deviceClass, userId);
                if (devPurchaseLists != null && devPurchaseLists.size() > 0) {
                    msg = "设备类别名称为【" + deviceClassName + "】申购流程，";
                    map.put(purpose + "_1", msg);
                }
            }
        }

        return map;
    }


    public void setDevAllocateFormDAO(IDevAllocateFormDAO devAllocateFormDAO) {
        this.devAllocateFormDAO = devAllocateFormDAO;
    }

    public void setDevDiscardFormDAO(IDevDiscardFormDAO devDiscardFormDAO) {
        this.devDiscardFormDAO = devDiscardFormDAO;
    }

    public void setDevUseApplyFormDAO(IDevUseApplyFormDAO devUseApplyFormDAO) {
        this.devUseApplyFormDAO = devUseApplyFormDAO;
    }

}
