package org.eapp.oa.device.ctrl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.eapp.oa.device.blo.IDeviceBiz;
import org.eapp.oa.device.blo.IDeviceClassBiz;
import org.eapp.oa.device.dto.DevicePage;
import org.eapp.oa.device.dto.DeviceQueryParameters;
import org.eapp.oa.device.hbean.Device;
import org.eapp.oa.device.hbean.DeviceClass;
import org.eapp.oa.device.hbean.DeviceClassAssign;
import org.eapp.oa.device.hbean.DeviceClassAssignArea;
import org.eapp.oa.device.hbean.DeviceClassAssignDetail;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.oa.system.util.web.HttpRequestHelper;

import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.config.SysCodeDictLoader;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.util.spring.SpringHelper;

public class DeviceQueryStatisticsCtrl extends HttpServlet implements Serializable {

    private static final long serialVersionUID = 6506965184804384685L;

    private static IDeviceBiz deviceBiz;

    private IDeviceClassBiz deviceClassBiz;

    public DeviceQueryStatisticsCtrl() {

    }

    public void init() throws ServletException {
        deviceBiz = (IDeviceBiz) SpringHelper.getSpringContext().getBean("deviceBiz");
        deviceClassBiz = (IDeviceClassBiz) SpringHelper.getSpringContext().getBean("deviceClassBiz");
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

        if ("initquery".equalsIgnoreCase(act)) {
            // 查询
            initQqueryDevice(request, response);
            return;
        } else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
            // 查询
            queryDevice(request, response);
            return;
        } else if (SysConstants.EXPORT.equalsIgnoreCase(act)) {
            // 导出
            exportDevice(request, response);
            return;
        }
    }
    
    private void initQqueryDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    		IOException {
    	request.setAttribute("companyAreas", SysCodeDictLoader.getInstance().getAreaMap().values());
    	request.getRequestDispatcher("/page/device/statistics/query_stat.jsp").forward(request, response);
    }

    protected void queryDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 获取参数
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
        String deviceTypeIds = HttpRequestHelper.getParameter(request, "deviceTypeIds");
        String areaCodeIds = HttpRequestHelper.getParameter(request, "areaCodeIds");
        String deviceClassIds = HttpRequestHelper.getParameter(request, "deviceClassIds");
        String statuses = HttpRequestHelper.getParameter(request, "statuses");
        String yearStr = HttpRequestHelper.getParameter(request, "year");
        String stratBuyTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "stratBuyTime"));
        String endBuyTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endBuyTime"));
        String sortCol = HttpRequestHelper.getParameter(request, "sortCol");
        Boolean ascend = HttpRequestHelper.getBooleanParameter(request, "ascend", false);
        // 构造查询条件
        DeviceQueryParameters qp = new DeviceQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        List<String> deviceTypeList = new ArrayList<String>();
        List<String> areaCodeList = new ArrayList<String>();
        if (deviceTypeIds != null && deviceTypeIds.length() > 0) {
            qp.setDeviceTypeIDs(deviceTypeIds.split(","));
            String[] deviceTypeStr = deviceTypeIds.split(",");
            for (String string : deviceTypeStr) {
                deviceTypeList.add(string);
            }
        }
        if (areaCodeIds != null && areaCodeIds.length() > 0) {
            qp.setAreaCodes(areaCodeIds.split(","));
            String[] areaCodeStr = areaCodeIds.split(",");
            for (String string : areaCodeStr) {
                areaCodeList.add(string);
            }
        }
        if (deviceClassIds != null && deviceClassIds.length() > 0) {
            qp.setDeviceClassIDs(deviceClassIds.split(","));
        }
        if (statuses != null && statuses.length() > 0) {

            String[] str = statuses.split(",");
            Integer[] statusInt = new Integer[str.length];
            for (int i = 0; i < str.length; i++) {
                if (str[i] != null && str[i].length() > 0) {
                    statusInt[i] = Integer.valueOf(str[i]);
                }

            }
            qp.setStatuses(statusInt);
        }
        if (yearStr != null) {

            Timestamp t = Timestamp.valueOf(yearStr + "-01-01 00:00:00");
            qp.setBeginReTime(t);
            Calendar ca = Calendar.getInstance();
            ca.setTimeInMillis(t.getTime());
            ca.set(Calendar.YEAR, ca.get(Calendar.YEAR) + 1);
            Timestamp s = Timestamp.valueOf(yearStr + "-01-01 00:00:00");
            s.setTime(ca.getTimeInMillis());
            qp.setEndReTime(s);
        }
        if (stratBuyTime != null) {
            qp.setBeginBuyTime(Timestamp.valueOf(stratBuyTime));
        }
        if (endBuyTime != null) {
            Timestamp t = Timestamp.valueOf(endBuyTime);
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
        Integer assignType = DeviceClassAssignDetail.ASSIGN_SELECT;
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
        List<DeviceClass> listClass = deviceClassBiz.getAssignClassSelect(deviceTypeList, areaCodeList, assignType,
                user.getAccountID(), groupNames, postNames);
        List<String> listStr = new ArrayList<String>();
        if (listClass != null) {
            for (DeviceClass deviceClass2 : listClass) {
                listStr.add(deviceClass2.getId());
            }
        }
        List<DeviceClassAssign> list = deviceClassBiz.getAssignClass(deviceTypeList, areaCodeList, assignType,
                user.getAccountID(), groupNames, postNames);
        Map<String, String> areaCodeMap = new HashMap<String, String>();
        Map<String, String> deviceTypeMap = new HashMap<String, String>();
        List<String> areaCodeList_ = new ArrayList<String>();
        List<String> deviceTypeList_ = new ArrayList<String>();
        if (list != null) {
            for (DeviceClassAssign assign : list) {
                Set<DeviceClassAssignArea> deviceClassAssignAreaSet = assign.getDeviceClassAssignAreas();
                if (areaCodeMap != null && areaCodeMap.containsKey(assign.getAreaCode())) {
                    for (DeviceClassAssignArea deviceClassAssignArea : deviceClassAssignAreaSet) {
                        if (deviceClassAssignArea.getDeviceClass() != null) {
                            if (!(deviceTypeMap != null
                                    && !deviceTypeMap.containsKey(deviceClassAssignArea.getDeviceClass()
                                            .getDeviceType()))) {
                                deviceTypeMap.put(deviceClassAssignArea.getDeviceClass().getDeviceType(),
                                        deviceClassAssignArea.getDeviceClass().getDeviceType());
                                deviceTypeList_.add(deviceClassAssignArea.getDeviceClass().getDeviceType());
                            }
                        }
                    }
                } else {
                    areaCodeMap.put(assign.getAreaCode(), assign.getAreaCode());
                    areaCodeList_.add(assign.getAreaCode());
                    for (DeviceClassAssignArea deviceClassAssignArea : deviceClassAssignAreaSet) {
                        if (deviceClassAssignArea.getDeviceClass() != null) {
                            deviceTypeMap.put(deviceClassAssignArea.getDeviceClass().getDeviceType(),
                                    deviceClassAssignArea.getDeviceClass().getDeviceType());
                            deviceTypeList_.add(deviceClassAssignArea.getDeviceClass().getDeviceType());
                        }
                    }
                }

            }
        }
        ListPage<Device> listPage = deviceBiz.getStatisticsDeviceListPage(qp, listStr, areaCodeList_, deviceTypeList_);

        XMLResponse.outputXML(response, new DevicePage(listPage).toDocument());
    }

    protected void exportDevice(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        // 获取参数
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", -1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 15);
        String deviceTypeIds = HttpRequestHelper.getParameter(request, "deviceTypeIds");
        String areaCodeIds = HttpRequestHelper.getParameter(request, "areaCodeIds");
        String deviceClassIds = HttpRequestHelper.getParameter(request, "deviceClassIds");
        String statuses = HttpRequestHelper.getParameter(request, "statuses");
        String yearStr = HttpRequestHelper.getParameter(request, "year");
        String stratBuyTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "stratBuyTime"));
        String endBuyTime = DataFormatUtil.formatTime(HttpRequestHelper.getParameter(request, "endBuyTime"));
        String sortCol = HttpRequestHelper.getParameter(request, "sortCol");
        String expNameAndValue = HttpRequestHelper.getParameter(request, "expNameAndValue");
        Boolean ascend = HttpRequestHelper.getBooleanParameter(request, "ascend", false);
        // 构造查询条件
        DeviceQueryParameters qp = new DeviceQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        List<String> deviceTypeList = new ArrayList<String>();
        List<String> areaCodeList = new ArrayList<String>();
        if (deviceTypeIds != null && deviceTypeIds.length() > 0) {
            qp.setDeviceTypeIDs(deviceTypeIds.split(","));
            String[] deviceTypeStr = deviceTypeIds.split(",");
            for (String string : deviceTypeStr) {
                deviceTypeList.add(string);
            }
        }
        if (areaCodeIds != null && areaCodeIds.length() > 0) {
            qp.setAreaCodes(areaCodeIds.split(","));
            String[] areaCodeStr = areaCodeIds.split(",");
            for (String string : areaCodeStr) {
                areaCodeList.add(string);
            }
        }
        if (deviceClassIds != null && deviceClassIds.length() > 0) {
            qp.setDeviceClassIDs(deviceClassIds.split(","));
        }
        if (statuses != null && statuses.length() > 0) {

            String[] str = statuses.split(",");
            Integer[] statusInt = new Integer[str.length];
            for (int i = 0; i < str.length; i++) {
                if (str[i] != null && str[i].length() > 0) {
                    statusInt[i] = Integer.valueOf(str[i]);
                }

            }
            qp.setStatuses(statusInt);
        }
        if (yearStr != null) {

            Timestamp t = Timestamp.valueOf(yearStr + "-01-01 00:00:00");
            qp.setBeginReTime(t);
            Calendar ca = Calendar.getInstance();
            ca.setTimeInMillis(t.getTime());
            ca.set(Calendar.YEAR, ca.get(Calendar.YEAR) + 1);
            Timestamp s = Timestamp.valueOf(yearStr + "-01-01 00:00:00");
            s.setTime(ca.getTimeInMillis());
            qp.setEndReTime(s);
        }
        if (stratBuyTime != null) {
            qp.setBeginBuyTime(Timestamp.valueOf(stratBuyTime));
        }
        if (endBuyTime != null) {
            Timestamp t = Timestamp.valueOf(endBuyTime);
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
        Integer assignType = DeviceClassAssignDetail.ASSIGN_SELECT;
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
        List<DeviceClass> listClass = deviceClassBiz.getAssignClassSelect(deviceTypeList, areaCodeList, assignType,
                user.getAccountID(), groupNames, postNames);
        List<String> listStr = new ArrayList<String>();
        if (listClass != null) {
            for (DeviceClass deviceClass2 : listClass) {
                listStr.add(deviceClass2.getId());
            }
        }
        List<DeviceClassAssign> list = deviceClassBiz.getAssignClass(deviceTypeList, areaCodeList, assignType,
                user.getAccountID(), groupNames, postNames);
        Map<String, String> areaCodeMap = new HashMap<String, String>();
        Map<String, String> deviceTypeMap = new HashMap<String, String>();
        List<String> areaCodeList_ = new ArrayList<String>();
        List<String> deviceTypeList_ = new ArrayList<String>();
        if (list != null) {
            for (DeviceClassAssign assign : list) {
                Set<DeviceClassAssignArea> deviceClassAssignAreaSet = assign.getDeviceClassAssignAreas();
                if (areaCodeMap != null && areaCodeMap.containsKey(assign.getAreaCode())) {
                    for (DeviceClassAssignArea deviceClassAssignArea : deviceClassAssignAreaSet) {
                        if (deviceClassAssignArea.getDeviceClass() != null) {
                            if (!(deviceTypeMap != null && !deviceTypeMap.containsKey(deviceClassAssignArea
                                    .getDeviceClass().getDeviceType()))) {
                                deviceTypeMap.put(deviceClassAssignArea.getDeviceClass().getDeviceType(),
                                        deviceClassAssignArea.getDeviceClass().getDeviceType());
                                deviceTypeList_.add(deviceClassAssignArea.getDeviceClass().getDeviceType());
                            }
                        }
                    }
                } else {
                    areaCodeMap.put(assign.getAreaCode(), assign.getAreaCode());
                    areaCodeList_.add(assign.getAreaCode());
                    for (DeviceClassAssignArea deviceClassAssignArea : deviceClassAssignAreaSet) {
                        if (deviceClassAssignArea.getDeviceClass() != null) {
                            deviceTypeMap.put(deviceClassAssignArea.getDeviceClass().getDeviceType(),
                                    deviceClassAssignArea.getDeviceClass().getDeviceType());
                            deviceTypeList_.add(deviceClassAssignArea.getDeviceClass().getDeviceType());
                        }
                    }
                }

            }
        }
        // 创建一个文件名
        String fileName = request.getSession().getId() + System.currentTimeMillis() / 10000 + ".xls";
        try {
            deviceBiz.csExportDevice(qp, listStr, areaCodeList_, deviceTypeList_, expNameAndValue, fileName, new File(
                    FileDispatcher.getTempDir()));
            XMLResponse.outputStandardResponse(response, 1, FileDispatcher.getTempAbsDir() + fileName);
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }

    }
}