package org.eapp.oa.device.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.json.JSONUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.oa.device.blo.IDevValidateFormBiz;
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceClassBiz;
import org.eapp.oa.device.blo.IDeviceDiscardBiz;
import org.eapp.oa.device.hbean.DevAllocateForm;
import org.eapp.oa.device.hbean.DevAllocateList;
import org.eapp.oa.device.hbean.DevDiscardDealForm;
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
import org.eapp.oa.device.hbean.DiscardDealDevList;
import org.eapp.oa.device.hbean.DiscardDevList;
import org.eapp.oa.device.hbean.PurchaseDevPurpose;
import org.eapp.oa.device.hbean.PurchaseDevice;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

import org.eapp.oa.system.config.SysConstants;

/**
 * 
 * 设备json转对象
 * 
 * <pre>
 * 修改日期              修改人      修改原因
 * 2012-7-23      方文伟      修改注释
 * </pre>
 */
public class DeviceList {
    Collection<Device> list;

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(DeviceList.class);

    public DeviceList(Collection<Device> list) {
        this.list = list;
    }

    /**
     * 将Json解析成对象
     * 
     * @param json json字符串
     * @return Set<PurchaseDevice> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<PurchaseDevice> parsePurchaseDeviceJSON(String json) throws Exception {
        log.debug("parsePurchaseDeviceJSON with json: " + json);
        Set<PurchaseDevice> purchaseDevices = new HashSet<PurchaseDevice>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                String purpose = (String) map.get("purpose");
                String orderNum = (String) map.get("orderNum");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String strPlanUseDate = (String) map.get("planUseDate");
                Date planUseDate = null;
                if (strPlanUseDate != null && !"".equals(strPlanUseDate)) {
                    planUseDate = format.parse(strPlanUseDate);
                }
                String areaCode = (String) map.get("areaCode");//
                String belongtoAreaCode = (String) map.get("belongtoAreaCode");// 区域：申购时用到，用于获取流程
                String deviceClassID = (String) map.get("deviceClass");// 设备类别：申购时用到，用于获取流程
                String devCfgDesc = (String) map.get("devCfgDesc");//
                PurchaseDevice devPurchaseList = new PurchaseDevice();
                if (orderNum != null && !"".equals(orderNum)) {
                    devPurchaseList.setDisplayOrder(Integer.valueOf(orderNum));
                }
                devPurchaseList.setAreaCode(areaCode);
                devPurchaseList.setDeviceID(deviceID);
                devPurchaseList.setPurpose(purpose);
                devPurchaseList.setPlanUseDate(planUseDate);
                devPurchaseList.setBelongtoAreaCode(belongtoAreaCode);
                IDeviceClassBiz deviceClassBiz = (IDeviceClassBiz) SpringHelper.getBean("deviceClassBiz");
                if (deviceClassID != null && !"".equals(deviceClassID)) {
                    DeviceClass deviceClass = deviceClassBiz.getDeviceClassById(deviceClassID);
                    devPurchaseList.setDeviceClass(deviceClass);
                }
                devPurchaseList.setDeviceCfgDesc(devCfgDesc);
                purchaseDevices.add(devPurchaseList);
            }
        } catch (Exception e) {
            log.error("parsePurchaseDeviceJSON faild", e);
            throw e;
        }
        return purchaseDevices;
    }

    /**
     * 将Json解析成对象
     * 
     * @param json json字符串
     * @param applyForm 申请表单
     * @return Set<DevPurchaseList>集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DevPurchaseList> parseDevPurchaseListJSON(String json, DevPurchaseForm applyForm)
            throws Exception {
        Set<DevPurchaseList> devPurchaseListResult = new HashSet<DevPurchaseList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                String purpose = (String) map.get("purpose");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String strPlanUseDate = (String) map.get("planUseDate");
                Date planUseDate = null;
                if (strPlanUseDate != null && !"".equals(strPlanUseDate)) {
                    planUseDate = format.parse(strPlanUseDate);
                }
                String areaCode = (String) map.get("areaCode");//
                DevPurchaseList devPurchaseList = new DevPurchaseList();
                IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                if (deviceID != null && !"".equals(deviceID)) {
                    Device device = deviceBiz.getDeviceById(deviceID);
                    devPurchaseList.setDevice(device);
                }
                devPurchaseList.setPurpose(purpose);
                devPurchaseList.setPlanUseDate(planUseDate);
                devPurchaseList.setAreaCode(areaCode);
                devPurchaseList.setDevPurchaseForm(applyForm);
                devPurchaseListResult.add(devPurchaseList);
            }
        } catch (Exception e) {
            log.error("parseDevPurchaseListJSON faild", e);
            throw e;
        }
        return devPurchaseListResult;
    }

    /**
     * 将Json解析成对象
     * 
     * @param json json字符串
     * @return Set<DevPurchaseList>集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DevPurchaseList> parsePurchaseListJSON(String json) throws Exception {
        Set<DevPurchaseList> devPurchaseListResult = new HashSet<DevPurchaseList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                String purpose = (String) map.get("purpose");
                String orderNum = (String) map.get("orderNum");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String strPlanUseDate = (String) map.get("planUseDate");
                Date planUseDate = null;
                if (strPlanUseDate != null && !"".equals(strPlanUseDate)) {
                    planUseDate = format.parse(strPlanUseDate);
                }

                String areaCode = (String) map.get("areaCode");//
                DevPurchaseList devPurchaseList = new DevPurchaseList();
                IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                if (deviceID != null && !"".equals(deviceID)) {
                    Device device = deviceBiz.getDeviceById(deviceID);
                    devPurchaseList.setDevice(device);
                }
                if (orderNum != null && !"".equals(orderNum)) {
                    devPurchaseList.setDisplayOrder(Integer.valueOf(orderNum));
                }
                devPurchaseList.setPurpose(purpose);
                devPurchaseList.setPlanUseDate(planUseDate);
                devPurchaseList.setAreaCode(areaCode);
                devPurchaseListResult.add(devPurchaseList);
            }
        } catch (Exception e) {
            log.error("parsePurchaseListJSON faild", e);
            throw e;
        }
        return devPurchaseListResult;
    }

    /**
     * 将Json解析成对象
     * 
     * @param json json字符串
     * @param allocateForm 分配单
     * @return Set<DevAllocateList>集合
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DevAllocateList> parseJSON(String json, DevAllocateForm allocateForm) throws Exception {
        Set<DevAllocateList> devAllocateResult = new HashSet<DevAllocateList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                DevAllocateList devAllocateList = new DevAllocateList();
                IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                if (deviceID != null && !"".equals(deviceID)) {
                    Device device = deviceBiz.getDeviceById(deviceID);
                    devAllocateList.setDevice(device);
                    devAllocateList.setDeviceCfgDesc(device.getConfigList());
                    devAllocateList.setDevAllocateForm(allocateForm);
                    devAllocateResult.add(devAllocateList);
                }
            }
        } catch (Exception e) {
            log.error("parseJSON faild", e);
            throw e;
        }
        return devAllocateResult;
    }

    /**
     * 将Json解析成对象
     * 
     * @param json json字符串
     * @return Set<DevAllocateList>集合
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DevAllocateList> parseDevAllotListJSON(String json) throws Exception {
        Set<DevAllocateList> devAllocateResult = new HashSet<DevAllocateList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                String orderNum = (String) map.get("orderNum");//
                // String purposeBef = (String)map.get("purposeBef");//
                // String areaCodeBef = (String)map.get("areaCodeBef");//
                DevAllocateList devAllocateList = new DevAllocateList();
                IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                if (deviceID != null && !"".equals(deviceID)) {
                    Device device = deviceBiz.getDeviceById(deviceID);
                    devAllocateList.setDevice(device);
                    devAllocateList.setPurposeBef(device.getDeviceCurStatusInfo().getPurpose());
                    devAllocateList.setAreaCodeBef(device.getDeviceCurStatusInfo().getAreaCode());
                    devAllocateList.setDevStatusBef(device.getDeviceCurStatusInfo().getDeviceCurStatus());
                    devAllocateList.setDeviceCfgDesc(device.getConfigList());
                    if (orderNum != null && orderNum.length() > 0) {
                        devAllocateList.setDisplayOrder(Integer.valueOf(orderNum));
                    }
                    devAllocateResult.add(devAllocateList);
                }
            }
        } catch (Exception e) {
            log.error("parseDevAllotListJSON faild", e);
            throw e;
        }
        return devAllocateResult;
    }

    /**
     * 将Json解析成对象
     * 
     * @param json json字符串
     * @param form 废弃单
     * @return Set<DiscardDevList>集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DiscardDevList> parseJSON(String json, DevDiscardForm form) throws Exception {
        Set<DiscardDevList> discardDevListResult = new HashSet<DiscardDevList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                String dealType = (String) map.get("dealType");
                String reason = (String) map.get("reason");

                if (map.get("devDetailId") != null) {
                    IDeviceDiscardBiz deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getBean("deviceDiscardBiz");
                    String devDetailId = (String) map.get("devDetailId");
                    DiscardDevList discardDevList = deviceDiscardBiz.getDiscardDevListByID(devDetailId);
                    if (discardDevList == null) {
                        discardDevList = new DiscardDevList();
                    }
                    if (map.get("remaining") != null) {
                        String remainingStr = (String) map.get("remaining");
                        discardDevList.setRemaining((remainingStr == null || "".equals(remainingStr)) ? null : Double
                                .valueOf(remainingStr));
                    }
                    if (map.get("depreciation") != null) {
                        String depreciation = (String) map.get("depreciation");
                        discardDevList.setDepreciation((depreciation == null || "".equals(depreciation)) ? null
                                : Double.valueOf(depreciation));
                    }
                    if (map.get("discardType") != null) {
                        String discardType = (String) map.get("discardType");
                        discardDevList.setDiscardType(discardType);
                    }
                    if (map.get("discardDate") != null) {
                        String discardDate = (String) map.get("discardDate");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        discardDevList.setDiscardDate(format.parse(discardDate));
                    }
                    if (map.get("buyPrice") != null) {
                        String buyPriceStr = (String) map.get("buyPrice");
                        discardDevList.setBuyPrice((buyPriceStr == null || "".equals(buyPriceStr)) ? null : Double
                                .valueOf(buyPriceStr));
                    }
                    if (map.get("noBuyPrice") != null) {
                        String noBuyPriceStr = (String) map.get("noBuyPrice");
                        discardDevList.setNoBuyPrice((noBuyPriceStr == null || "".equals(noBuyPriceStr)) ? null
                                : Double.valueOf(noBuyPriceStr));
                    }
                    if (map.get("planPayDate") != null) {
                        String planPayDateStr = (String) map.get("planPayDate");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// yyyy-MM-dd HH:mm:ss.SSS
                        Date planPayDate = null;
                        try {
                            planPayDate = format.parse(planPayDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            planPayDate = null;
                        }
                        discardDevList.setPlanPayDate(planPayDate);
                    }

                    if (map.get("backBuyFlag") != null) {
                        String backBuyFlag = (String) map.get("backBuyFlag");
                        discardDevList.setBuyFlag("1".equals(backBuyFlag) || "true".equals(backBuyFlag) ? true : false);
                    }
                    if (map.get("inDate") != null) {
                        String strInDate = (String) map.get("inDate");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// yyyy-MM-dd HH:mm:ss.SSS
                        Date inDate = null;
                        try {
                            inDate = format.parse(strInDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            inDate = null;
                        }
                        discardDevList.setInDate(inDate);
                    }
                    discardDevListResult.add(discardDevList);
                } else {
                    DiscardDevList discardDevList = new DiscardDevList();
                    IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                    Device device = deviceBiz.getDeviceById(deviceID);
                    discardDevList.setDevice(device);
                    discardDevList.setDealType(dealType);
                    discardDevList.setDevDiscardForm(form);
                    discardDevList.setReason(reason);
                    discardDevListResult.add(discardDevList);
                }
            }
        } catch (Exception e) {
            log.error("parseJSON faild", e);
            throw e;
        }
        return discardDevListResult;
    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @return Set<DiscardDevList> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DiscardDevList> parseDiscardDevListJSON(String json) throws Exception {
        Set<DiscardDevList> discardDevListResult = new HashSet<DiscardDevList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                String dealType = (String) map.get("dealType");
                String reason = (String) map.get("reason");
                String orderNum = (String) map.get("orderNum");

                if (map.get("devDetailId") != null) {
                    IDeviceDiscardBiz deviceDiscardBiz = (IDeviceDiscardBiz) SpringHelper.getBean("deviceDiscardBiz");
                    String devDetailId = (String) map.get("devDetailId");
                    DiscardDevList discardDevList = deviceDiscardBiz.getDiscardDevListByID(devDetailId);
                    if (discardDevList == null) {
                        discardDevList = new DiscardDevList();
                    }
                    if (map.get("remaining") != null) {
                        String remainingStr = (String) map.get("remaining");
                        discardDevList.setRemaining(Double.valueOf(remainingStr));
                    }
                    if (map.get("depreciation") != null) {
                        String depreciation = (String) map.get("depreciation");
                        discardDevList.setDepreciation(Double.valueOf(depreciation));
                    }
                    if (map.get("discardType") != null) {
                        String discardType = (String) map.get("discardType");
                        discardDevList.setDiscardType(discardType);
                    }
                    if (map.get("discardDate") != null) {
                        String discardDate = (String) map.get("discardDate");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        discardDevList.setDiscardDate(format.parse(discardDate));
                    }
                    if (map.get("buyPrice") != null) {
                        String buyPriceStr = (String) map.get("buyPrice");
                        discardDevList.setBuyPrice(Double.valueOf(buyPriceStr));
                    }
                    if (map.get("noBuyPrice") != null) {
                        String noBuyPriceStr = (String) map.get("noBuyPrice");
                        discardDevList.setNoBuyPrice(Double.valueOf(noBuyPriceStr));
                    }
                    discardDevListResult.add(discardDevList);
                } else {
                    DiscardDevList discardDevList = new DiscardDevList();
                    if (orderNum != null && orderNum.length() > 0) {
                        discardDevList.setDisplayOrder(Integer.valueOf(orderNum));
                    }
                    IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                    Device device = deviceBiz.getDeviceById(deviceID);
                    discardDevList.setDevice(device);
                    discardDevList.setDealType(dealType);
                    discardDevList.setReason(reason);
                    discardDevListResult.add(discardDevList);
                }
                // if (orderNum != null && !"".equals(orderNum)) {
                //
                // }

            }
        } catch (Exception e) {
            log.error("parseDiscardDevListJSON faild", e);
            throw e;
        }
        return discardDevListResult;
    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @param form 废弃处理单
     * @return Set<DiscardDevList> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<DiscardDealDevList> parseJSON(String json, DevDiscardDealForm form) throws Exception {
        Set<DiscardDealDevList> discardDealDevListResult = new HashSet<DiscardDealDevList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String deviceID = (String) map.get("deviceID");//
                DiscardDealDevList discardDealDevList = new DiscardDealDevList();
                IDeviceBiz deviceBiz = (IDeviceBiz) SpringHelper.getBean("deviceBiz");
                Device device = deviceBiz.getDeviceById(deviceID);
                discardDealDevList.setDevice(device);
                discardDealDevList.setDevDiscardDealForm(form);
                discardDealDevListResult.add(discardDealDevList);
            }
        } catch (Exception e) {
            log.error("parseJSON faild", e);
            throw e;
        }
        return discardDealDevListResult;
    }

    /**
     * <?xml version="1.0" encoding="utf-8" ?> <root> <message code="1" /> <content total-count="1" page-size="0"
     * page-count="1" current-page="1" previous-page="false" next-page="false"> <device id="ID">
     * <device-type>设备类型</device-type> <device-NO>设备编号</device-NO> <device-name>设备名称</device-name>
     * <device-model>设备型号</device-model> <device-description>设备描述</device-description>
     * <device-nowState>当前状态</device-nowState> <device-buyTime>购买时间</device-buyTime>
     * <device-buyType>购买方式</device-buyType> <device-regTime>登记时间</device-regTime> <device-remark>备注</device-remark>
     * </device-config> </content> </root>
     * 
     * @return
     */
    public Document toDocument() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding(XMLResponse.DEFAULT_ENCODING);
        Element root = doc.addElement("root");
        if (list == null || list.isEmpty()) {
            root.addElement("message").addAttribute("code", "0");
            return doc;
        }
        root.addElement("message").addAttribute("code", "1");
        Element contentEle = root.addElement("content");
        Element proEle = null;
        for (Device d : list) {
            if (d != null) {
                proEle = contentEle.addElement("device");
                proEle.addAttribute("id", d.getId());
                if (d.getDeviceClass() == null) {
                    proEle.addElement("device-class-str").setText(
                            DataFormatUtil.noNullValue(d.getDeviceClassDisplayName()));
                } else {
                    proEle.addElement("device-class-str").setText(
                            DataFormatUtil.noNullValue(d.getDeviceClass().getName()));
                }
                proEle.addElement("device-class").setText(
                        DataFormatUtil.noNullValue(d.getDeviceClass() == null ? "" : d.getDeviceClass().getId()));
                DeviceCurStatusInfo statusInfo = d.getDeviceCurStatusInfo();
                proEle.addElement("device-no").setText(DataFormatUtil.noNullValue(d.getDeviceNO()));
                proEle.addElement("device-type").setText(DataFormatUtil.noNullValue(d.getDeviceType()));
                proEle.addElement("device-type-str").setText(
                        DataFormatUtil.noNullValue(DevFlowApplyProcess.getDeviceTypeDisplayName(d.getDeviceType())));
                proEle.addElement("device-name").setText(DataFormatUtil.noNullValue(d.getDeviceName()));
                proEle.addElement("device-model").setText(DataFormatUtil.noNullValue(d.getDeviceModel()));
                proEle.addElement("config-list").setText(DataFormatUtil.noNullValue(d.getConfigList()));
                proEle.addElement("status").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getDeviceCurStatus()));
                proEle.addElement("status-str").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getDeviceCurStatusStr()));

                proEle.addElement("work-areaName").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getAreaName()));
                proEle.addElement("work-areaCode").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getAreaCode()));
                proEle.addElement("work-purpose").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getPurpose()));
//                proEle.addElement("work-purposeName").setText(
//                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getPurposeName()));
                proEle.addElement("work-groupName").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getGroupName()));
                proEle.addElement("work-userId").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getOwner()));
                proEle.addElement("work-userName").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getOwnerName()));

                proEle.addElement("description").setText(DataFormatUtil.noNullValue(d.getDescription()));
                proEle.addElement("reg-account-id").setText(DataFormatUtil.noNullValue(d.getRegAccountID()));
                proEle.addElement("reg-time").setText(
                        DataFormatUtil.noNullValue(d.getRegTime(), SysConstants.STANDARD_DATE_PATTERN));
                proEle.addElement("buy-time").setText(
                        DataFormatUtil.noNullValue(d.getBuyTime(), SysConstants.STANDARD_DATE_PATTERN));
                proEle.addElement("buy-type").setText(DataFormatUtil.noNullValue(d.getBuyType()));
                proEle.addElement("buy-type-str").setText(
                        DataFormatUtil.noNullValue(DevPurchaseForm.getBuyTypeDisplayName(d.getBuyType())));
                proEle.addElement("deduct-flag").setText(DataFormatUtil.noNullValue(d.getDeductFlag()));
                proEle.addElement("deduct-money").setText(DataFormatUtil.noNullValue(d.getDeductMoney()));
                proEle.addElement("in-date").setText(DataFormatUtil.noNullValue(d.getInDate()));
                proEle.addElement("area-code").setText(DataFormatUtil.noNullValue(d.getAreaCode()));
                proEle.addElement("area-name").setText(DataFormatUtil.noNullValue(d.getAreaName()));
                proEle.addElement("sequence").setText(DataFormatUtil.noNullValue(d.getSequence()));
                proEle.addElement("price").setText(DataFormatUtil.noNullValue(d.getPrice()));
                proEle.addElement("device-user").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getOwnerName()));
                proEle.addElement("purpose").setText(
                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getPurpose()));
//                proEle.addElement("purpose-name").setText(
//                        DataFormatUtil.noNullValue(statusInfo == null ? "" : statusInfo.getPurposeName()));
                proEle.addElement("approve-type-str").setText(
                        DataFormatUtil.noNullValue(statusInfo == null || statusInfo.getApproveType() == null ? ""
                                : DeviceCurStatusInfo.approveTypeMap.get(statusInfo.getApproveType())));
            }
        }
        return doc;
    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @param id id
     * @return Set<PurchaseDevice> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<PurchaseDevice> parsePurchaseDeviceJSON(String json, String id) throws Exception {
        Set<PurchaseDevice> devPurchaseDeviceResult = new HashSet<PurchaseDevice>();
        if (json == null || json.trim().equals("")) {
            return null;
        }

        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            IDevValidateFormBiz devValidateFormBiz = (IDevValidateFormBiz) SpringHelper.getBean("devValidateFormBiz");
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                DevPurchaseForm devPurchaseForm = new DevPurchaseForm();
                devPurchaseForm.setId(id);
                PurchaseDevice purchaseDevice = new PurchaseDevice();
                String deviceName = (String) map.get("deviceName");//
                String valiFormId = (String) map.get("valiFormId");//
                String orderNum = (String) map.get("orderNum");//
                String purchaseId = (String) map.get("id");//
                if (purchaseId != null && purchaseId.length() > 0) {
                    purchaseDevice.setId(purchaseId);
                }

                if (valiFormId != null) {
                    DevValidateForm devValidateForm = devValidateFormBiz.getDevValidateFormById(valiFormId);
                    if (devValidateForm != null) {
                        DevValidateForm devValidateFormNew = new DevValidateForm();
                        devValidateFormNew.setAccountID(devValidateForm.getAccountID());
                        devValidateFormNew.setRemark(devValidateForm.getRemark());
                        devValidateFormNew.setSequenceYear(devValidateForm.getSequenceYear());
                        devValidateFormNew.setValidFormNO(devValidateForm.getValidFormNO());
                        devValidateFormNew.setValiType(devValidateForm.getValiType());
                        devValidateFormNew.setPurchaseDevice(purchaseDevice);
                        devValidateFormNew.setValiDate(devValidateForm.getValiDate());
                        if (devValidateForm.getDeviceValiDetails() != null
                                && !devValidateForm.getDeviceValiDetails().isEmpty()) {
                            Set<DeviceValiDetail> deviceValiDetails = new HashSet<DeviceValiDetail>();
                            for (DeviceValiDetail deviceValiDetail : devValidateForm.getDeviceValiDetails()) {
                                if (deviceValiDetail != null) {
                                    DeviceValiDetail deviceValiDetailNew = new DeviceValiDetail();
                                    deviceValiDetailNew.setIsEligibility(deviceValiDetail.getIsEligibility());
                                    deviceValiDetailNew.setDevValidateForm(devValidateFormNew);
                                    deviceValiDetailNew.setItem(deviceValiDetail.getItem());
                                    deviceValiDetailNew.setRemark(deviceValiDetail.getRemark());
                                    deviceValiDetails.add(deviceValiDetailNew);
                                }
                            }
                            devValidateFormNew.setDeviceValiDetails(deviceValiDetails);
                        }
                        purchaseDevice.setDevValidateForm(devValidateFormNew);
                    } else {
                        DevValidateForm devValidateFormNew = new DevValidateForm();
                        purchaseDevice.setDevValidateForm(devValidateFormNew);
                    }

                } else {
                    DevValidateForm devValidateFormNew = new DevValidateForm();
                    purchaseDevice.setDevValidateForm(devValidateFormNew);
                }
                String deductFlagStr = (String) map.get("deductFlag");//
                if (deductFlagStr != null) {
                    if ("1".equals(deductFlagStr)) {
                        purchaseDevice.setDeductFlag(true);
                    } else {
                        purchaseDevice.setDeductFlag(false);
                    }

                }
                if (orderNum != null && !"".equals(orderNum)) {
                    purchaseDevice.setDisplayOrder(Integer.valueOf(orderNum));
                }
                String deductMoneyStr = (String) map.get("deductMoney");//
                if (deductMoneyStr != null && deductMoneyStr.length() > 0) {
                    Double deductMoney = Double.valueOf(deductMoneyStr);
                    purchaseDevice.setDeductMoney(deductMoney);
                }
                purchaseDevice.setDeviceName(deviceName);
                String belongtoAreaCode = (String) map.get("belongtoAreaCode");//
                purchaseDevice.setBelongtoAreaCode(belongtoAreaCode);
                String deviceModel = (String) map.get("deviceModel");
                purchaseDevice.setDeviceModel(deviceModel);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String buyTimeStr = (String) map.get("buyTime");
                Date buyTime = null;
                if (buyTimeStr != null && !"".equals(buyTimeStr)) {
                    buyTime = format.parse(buyTimeStr);
                }
                purchaseDevice.setBuyTime(buyTime);
                String deviceClassId = (String) map.get("deviceClass");//
                DeviceClass deviceClass = new DeviceClass();
                deviceClass.setId(deviceClassId);
                String description = (String) map.get("description");//
                String optionLists = (String) map.get("optionLists");// 设备属性
                String areaCodePurpose = (String) map.get("areaCodePurpose");// 工作所在地
                String purpose = (String) map.get("purpose");// 工作所在地
                purchaseDevice.setAreaCode(areaCodePurpose);
                purchaseDevice.setPurpose(purpose);
                if (optionLists != null && optionLists.length() > 0) {
                    Set<DevicePropertyDetail> devicePropertyDetails = new HashSet<DevicePropertyDetail>();
                    String[] options = optionLists.split(";");
                    for (String string : options) {
                        DevicePropertyDetail devicePropertyDetail = new DevicePropertyDetail();
                        String[] str = string.split(":");
                        devicePropertyDetail.setPropertyName(str[0]);
                        devicePropertyDetail.setPropertyValue(str[1]);
                        devicePropertyDetail.setPurchaseDevice(purchaseDevice);
                        devicePropertyDetails.add(devicePropertyDetail);
                    }
                    purchaseDevice.setDevicePropertyDetails(devicePropertyDetails);
                }
                if (map.get("price") != null) {
                    String priceStr = (String) map.get("price");
                    purchaseDevice.setPrice(Double.valueOf(priceStr));
                }
                purchaseDevice.setDescription(description);
                purchaseDevice.setDeviceClass(deviceClass);
                purchaseDevice.setDevPurchaseForm(devPurchaseForm);

                devPurchaseDeviceResult.add(purchaseDevice);
            }
        } catch (Exception e) {
            log.error("parsePurchaseDeviceJSON faild", e);
            throw e;
        }
        return devPurchaseDeviceResult;
    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @return Set<PurchaseDevice> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<PurchaseDevice> parsePurchaseValiJSON(String json) throws Exception {
        Set<PurchaseDevice> devPurchaseDeviceResult = new HashSet<PurchaseDevice>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            int num = 0;
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            IDevValidateFormBiz devValidateFormBiz = (IDevValidateFormBiz) SpringHelper.getBean("devValidateFormBiz");
            Calendar calendar = Calendar.getInstance();
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                PurchaseDevice purchaseDevice = new PurchaseDevice();
                String purchaseId = (String) map.get("id");//
                purchaseDevice.setId(purchaseId);
                String deviceNo = (String) map.get("deviceNo");// 设置设备编号
                purchaseDevice.setDeviceNo(deviceNo);
                String valiFormId = (String) map.get("valiFormId");//
                String deviceClassStr = (String) map.get("deviceClass");//
                if (deviceClassStr != null && deviceClassStr != "") {
                    DeviceClass deviceClass = new DeviceClass();
                    deviceClass.setId(deviceClassStr);
                    purchaseDevice.setDeviceClass(deviceClass);
                }
                String areaCode = (String) map.get("areaCode");//
                if (areaCode != null && areaCode != "") {
                    purchaseDevice.setBelongtoAreaCode(areaCode);
                }
                DevValidateForm devValidateForm = new DevValidateForm();
                if (valiFormId != null && valiFormId.length() > 0) {
                    DevValidateForm devValidateFormOld = devValidateFormBiz.getDevValidateFormById(valiFormId);
                    devValidateForm.setValidFormNO(devValidateFormOld.getValidFormNO());
                    devValidateForm.setSequenceYear(devValidateFormOld.getSequenceYear());
                } else {
                    if (num == 0) {
                        num = devValidateFormBiz.getCurrentYearValidFormNO(DevValidateForm.VALITYPE_DIRECT_IN,
                                calendar.get(Calendar.YEAR)) + 1;
                    } else {
                        ++num;
                    }

                    devValidateForm.setSequenceYear(calendar.get(Calendar.YEAR));
                    devValidateForm.setValidFormNO(num);
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String valiDateStr = (String) map.get("valiDate");//
                Date valiDate = null;
                if (valiDateStr != null && !"".equals(valiDateStr)) {
                    valiDate = format.parse(valiDateStr);
                }
                devValidateForm.setValiDate(valiDate);
                String userId = (String) map.get("userId");//
                devValidateForm.setAccountID(userId);
                String remark = (String) map.get("remark");//
                devValidateForm.setRemark(remark);
                Set<DeviceValiDetail> deviceValiDetails = new HashSet<DeviceValiDetail>();
                List<Map<String, Object>> valiObj = (List<Map<String, Object>>) (map.get("valiTiems"));
                if (valiObj != null && !valiObj.isEmpty()) {
                    for (Map<String, Object> valiMap : valiObj) {
                        if (valiMap.size() == 0) {
                            continue;
                        }
                        DeviceValiDetail deviceValiDetail = new DeviceValiDetail();
                        deviceValiDetail.setDevValidateForm(devValidateForm);
                        String itemName = (String) valiMap.get("itemName");//
                        deviceValiDetail.setItem(itemName);
                        String itemRemark = (String) valiMap.get("itemRemark");//
                        deviceValiDetail.setRemark(itemRemark);
                        String isEligibility = (String) valiMap.get("isEligibility");//
                        if ("1".equals(isEligibility)) {
                            deviceValiDetail.setIsEligibility(true);
                        } else {
                            deviceValiDetail.setIsEligibility(false);
                        }
                        deviceValiDetails.add(deviceValiDetail);
                    }
                    devValidateForm.setDeviceValiDetails(deviceValiDetails);
                }
                purchaseDevice.setDevValidateForm(devValidateForm);
                devPurchaseDeviceResult.add(purchaseDevice);
            }
            return devPurchaseDeviceResult;
        } catch (Exception e) {
            log.error("parsePurchaseValiJSON faild", e);
            throw e;
        }

    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @return Map<String, PurchaseDevice> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, PurchaseDevice> parsePurchaseDeviceByIDJSON(String json) throws Exception {
        Map<String, PurchaseDevice> devPurchaseDeviceResult = new HashMap<String, PurchaseDevice>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                PurchaseDevice purchaseDevice = new PurchaseDevice();
                String purchaseId = (String) map.get("id");//
                purchaseDevice.setId(purchaseId);
                // String purpose = (String)map.get("purpose");//
                String areaCode = (String) map.get("areaCode");//
                purchaseDevice.setAreaCode(areaCode);
                // purchaseDevice.setPurpose(purpose);
                devPurchaseDeviceResult.put(purchaseId, purchaseDevice);
            }
        } catch (Exception e) {
            log.error("parsePurchaseDeviceByIDJSON faild", e);
            throw e;
        }
        return devPurchaseDeviceResult;
    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @return Map<String, DevAllocateList> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, DevAllocateList> parseDevAllocateListByIDJSON(String json) throws Exception {
        Map<String, DevAllocateList> devAllocateListResult = new HashMap<String, DevAllocateList>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                DevAllocateList devAllocateList = new DevAllocateList();
                String purchaseId = (String) map.get("id");//
                devAllocateList.setId(purchaseId);
                String purpose = (String) map.get("purpose");//
                String areaCode = (String) map.get("areaCode");//
                devAllocateList.setAreaCode(areaCode);
                devAllocateList.setPurpose(purpose);
                devAllocateListResult.put(purchaseId, devAllocateList);
            }
        } catch (Exception e) {
            log.error("parseDevAllocateListByIDJSON faild", e);
            throw e;
        }
        return devAllocateListResult;
    }

    /**
     * 将json解析为对象
     * 
     * @param json json字符串
     * @return Set<PurchaseDevPurpose> 集合
     * @throws OaException oa异常
     * @throws Exception 异常
     * 
     *             <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-23      方文伟      修改注释
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static Set<PurchaseDevPurpose> parsePurchaseDevPurposeJSON(String json) throws Exception {
        Set<PurchaseDevPurpose> devPurchaseListResult = new HashSet<PurchaseDevPurpose>();
        if (json == null || json.trim().equals("")) {
            return null;
        }
        try {
            List<Map<String, Object>> obj = (List<Map<String, Object>>) JSONUtil.deserialize(json);
            if (obj == null || obj.isEmpty()) {
                return null;
            }
            for (Map<String, Object> map : obj) {
                if (map.size() == 0) {
                    continue;
                }
                String purpose = (String) map.get("purpose");
                String manyTimeStr = (String) map.get("manyTimeFlag");
                Boolean selectedFlag = (Boolean) map.get("selectedFlag");
                PurchaseDevPurpose purchaseDevPurpose = new PurchaseDevPurpose();
                purchaseDevPurpose.setPurpose(purpose);
                if (manyTimeStr != null && manyTimeStr.equals("true")) {
                    purchaseDevPurpose.setManyTimeFlag(true);
                } else {
                    purchaseDevPurpose.setManyTimeFlag(false);
                }
                purchaseDevPurpose.setSelectedFlag(selectedFlag);

                devPurchaseListResult.add(purchaseDevPurpose);
            }
        } catch (Exception e) {
            log.error("parsePurchaseDevPurposeJSON faild", e);
            throw e;
        }
        return devPurchaseListResult;
    }
}
