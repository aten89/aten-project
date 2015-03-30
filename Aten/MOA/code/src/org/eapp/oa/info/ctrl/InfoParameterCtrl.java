package org.eapp.oa.info.ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.oa.info.blo.IInfoLayoutAssignBiz;
import org.eapp.oa.info.blo.IInfoLayoutBiz;
import org.eapp.oa.info.dto.InfoLayoutAssignXml;
import org.eapp.oa.info.dto.InfoLayoutList;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

/**
 * 信息流程配置CTRL
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2013-3-4	           李海根	注释修改
 * </pre>
 */
public class InfoParameterCtrl extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1330245250999006112L;
    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(InfoParameterCtrl.class);

    /**
     * infoLayoutBiz接口
     */
    private IInfoLayoutBiz infoLayoutBiz;
    /**
     * infoLayoutAssignBiz接口
     */
    private IInfoLayoutAssignBiz infoLayoutAssignBiz;

    /**
     * Constructor of the object.
     */
    public InfoParameterCtrl() {
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
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        infoLayoutBiz = (IInfoLayoutBiz) SpringHelper.getBean("infoLayoutBiz");
        infoLayoutAssignBiz = (IInfoLayoutAssignBiz) SpringHelper.getBean("infoLayoutAssignBiz");
    }

    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String act = HttpRequestHelper.getParameter(request, "act");

        if (SysConstants.ADD.equalsIgnoreCase(act)) {
            // 新增类别配置信息
            addInfoParameter(request, response);
            return;
        } else if (SysConstants.DELETE.equalsIgnoreCase(act)) {
            // 删除类别配置信息
            delInfoParameter(request, response);
            return;
        } else if (SysConstants.MODIFY.equalsIgnoreCase(act)) {
            // 修改类别配置信息
            modifyInfoParameter(request, response);
            return;
        } else if (SysConstants.QUERY.equalsIgnoreCase(act)) {
            // 查询类别配置信息
            queryInfoParameter(request, response);
            return;
        } else if ("initsort".equalsIgnoreCase(act)) {
            // 初始化排序
            initsort(request, response);
            return;
        } else if (SysConstants.ORDER.equalsIgnoreCase(act)) {
            // 保存排序
            saveSort(request, response);
            return;
//        } else if ("infoSelect".equalsIgnoreCase(act)) {
//            // 数据字典读取信息类别
//            makeInfoSelect(request, response);
//            return;

        } else if ("getassignlayout".equalsIgnoreCase(act)) {
            // 取得用户有权限的信息版块
            getAssignLayout(request, response);
            return;
        } else if ("init_assign".equalsIgnoreCase(act)) {
            // 初始化授权页面
            initAssign(request, response);
            return;
        } else if ("init_assign_detail".equalsIgnoreCase(act)) {
            // 初始化详细授权页面
            initAssignDetail(request, response);
            return;
        } else if ("get_binding_users".equalsIgnoreCase(act)) {
            // 已绑定的用户
            getBindUser(request, response);
            return;
        } else if (SysConstants.BIND_USER.equals(act)) {
            // 绑定用户
            saveBindUser(request, response);
            return;
        } else if ("get_binding_groups".equalsIgnoreCase(act)) {
            // 已绑定的机构
            getBindGroup(request, response);
            return;
        } else if (SysConstants.BIND_GROUP.equals(act)) {
            // 绑定机构
            saveBindGroup(request, response);
            return;
        } else if ("get_binding_post".equalsIgnoreCase(act)) {
            // 已绑定的职位
            getBindPost(request, response);
            return;
        } else if (SysConstants.BIND_POST.equals(act)) {
            // 绑定职位
            saveBindPost(request, response);
            return;
        } else if ("findall".equals(act)) {
            // 取得所有的信息配置
            queryInfoParameter(request, response);
            return;
//        } else if ("classselect".equals(act)) {
//            // 取得版块下类型
//            infoClassSelect(request, response);
//            return;
//        } else if ("init_emailAddr".equals(act)) {
//            // 初始化授权页面
//            initAssign(request, response);
//            return;
//        } else if ("get_email_users".equals(act)) {
//            // 获取已绑定的收件人
//            getEamilUsers(request, response);
//        } else if ("bindEmailUser".equals(act)) {
//            // 保存绑定的收件人
//            saveBindEmailUser(request, response);
        }
    }

    /**
     * 初始化详细授权页面
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	           李海根	注释修改
     * </pre>
     */
    private void initAssignDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // 信息类别
        String page = HttpRequestHelper.getParameter(request, "page");
        String title = HttpRequestHelper.getParameter(request, "title");
        request.setAttribute("title", title);
        request.getRequestDispatcher("/page/info/paramconf/" + page).forward(request, response);

    }

    /**
     * 初始化授权页面
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	           李海根	注释修改
     * </pre>
     */
    private void initAssign(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // 信息类别
        String title = HttpRequestHelper.getParameter(request, "title");
        request.setAttribute("title", title);
        request.getRequestDispatcher("/page/info/paramconf/frame_assign.jsp")
                .forward(request, response);
    }

    /**
     * 新增类别信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     * </pre>
     */
    private void addInfoParameter(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // 信息类别
        String infoName = HttpRequestHelper.getParameter(request, "infoname");
        if (infoName == null) {
            XMLResponse.outputStandardResponse(response, 0, "请选择分类名称");
            return;
        }
        // 流程类别
        String flowClass = HttpRequestHelper.getParameter(request, "flowclass");
//        // 是否发邮件通知
//        Boolean isEmail = HttpRequestHelper.getBooleanParameter(request, "isEmail", false);
        // 说明
        String desc = HttpRequestHelper.getParameter(request, "desc");

        try {
            InfoLayout infoLayout = infoLayoutBiz.addInfoLayout(infoName, flowClass, desc);
            XMLResponse.outputStandardResponse(response, 1, infoLayout.getId());
        } catch (OaException e) {
            LOG.error("addInfoParameter failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            LOG.error("addInfoParameter failed: ", ex);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 删除类别信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void delInfoParameter(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
            return;
        }
        try {
            InfoLayout infoLayout = infoLayoutBiz.deleteInfoLayout(id);
            XMLResponse.outputStandardResponse(response, 1, infoLayout.getId());
        } catch (OaException e) {
            LOG.error("delInfoParameter failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            LOG.error("delInfoParameter failed: ", ex);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 修改类别信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void modifyInfoParameter(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "ID不能为控");
            return;
        }

        // 信息类别
        String infoName = HttpRequestHelper.getParameter(request, "infoname");
        if (infoName == null) {
            XMLResponse.outputStandardResponse(response, 0, "请选择分类名称");
            return;
        }
        // 流程类别
        String flowClass = HttpRequestHelper.getParameter(request, "flowclass");
        // 是否发邮件通知
//        Boolean isEmail = HttpRequestHelper.getBooleanParameter(request, "isEmail", false);
        // 说明
        String desc = HttpRequestHelper.getParameter(request, "desc");

        try {
            InfoLayout infoLayout = infoLayoutBiz.modifyInfoLayour(id, infoName, flowClass, desc);
            XMLResponse.outputStandardResponse(response, 1, infoLayout.getId());
        } catch (OaException e) {
            LOG.error("modifyInfoParameter failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception ex) {
            LOG.error("modifyInfoParameter failed: ", ex);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 查询类别信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void queryInfoParameter(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {
            // 查询
            List<InfoLayout> list = infoLayoutBiz.getAllInfoLayout();
            XMLResponse.outputXML(response, new InfoLayoutList(list).toDocument());
        } catch (Exception e) {
            LOG.error("queryInfoParameter failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }
    }

    /**
     * 初始化排序
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void initsort(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.getRequestDispatcher("/page/info/paramconf/sort_layout.jsp").forward(request,
                response);
    }

    /**
     * 保存排序
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	           李海根	注释修改
     * </pre>
     */
    private void saveSort(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String orderIDs = HttpRequestHelper.getParameter(request, "orderids");
        if (orderIDs == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数");
            return;
        }

        try {

            String[] infoLayoutIDs = orderIDs.split(",");
            if (infoLayoutIDs != null && infoLayoutIDs.length > 0) {
                infoLayoutBiz.txSaveOrder(infoLayoutIDs);
            }

            XMLResponse.outputStandardResponse(response, 1, "操作成功");
        } catch (Exception e) {
            LOG.error("saveSort failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
            return;
        }

    }

//    /**
//     * 读取信息类别
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException ServletException
//     * @throws IOException IOException
//     * 
//     *             <pre>
//     * 修改日期		修改人	修改原因
//     * 2013-3-4	           李海根	注释修改
//     * </pre>
//     */
//    private void makeInfoSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//
//        List<DataDictionaryDTO> rootList = SysCodeDictLoader.getInstance().getInfoClass();
//        // 1.取根目录
//        if (rootList == null) {
//            return;
//        }
//        HTMLResponse.outputHTML(response, new DataDictionarySelect(rootList).toString());
//    }

    /**
     * 获得授权信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void getAssignLayout(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);

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
        List<InfoLayout> layouts = infoLayoutBiz.getAssignLayout(user.getAccountID(), groupNames, postNames, flag);
        XMLResponse.outputXML(response, new InfoLayoutList(layouts).toDocument());
    }

    /**
     * 获得绑定的用户
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void getBindUser(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            List<InfoLayoutAssign> list = infoLayoutAssignBiz.getBindingUsers(id, flag);
            XMLResponse.outputXML(response, new InfoLayoutAssignXml(list).toDocument());
        } catch (Exception e) {
            LOG.error("getBindUser failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 绑定用户
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void saveBindUser(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        String[] userIDs = HttpRequestHelper.getParameters(request, "user_ids");
        try {
            infoLayoutAssignBiz.txBindingUsers(id, userIDs, flag);
            XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
        } catch (OaException ex) {
            LOG.error("saveBindUser failed: ", ex);
            XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
        } catch (Exception e) {
            LOG.error("saveBindUser failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
        }
    }

    /**
     * 获得绑定的机构
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void getBindGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            List<InfoLayoutAssign> list = infoLayoutAssignBiz.getBindingGroups(id, flag);
            XMLResponse.outputXML(response, new InfoLayoutAssignXml(list).toDocument());
        } catch (Exception e) {
            LOG.error("getBindGroup failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 绑定机构
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void saveBindGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        String[] groupIDs = HttpRequestHelper.getParameters(request, "group_ids");
        try {
            infoLayoutAssignBiz.txBindingGroups(id, groupIDs, flag);
            XMLResponse.outputStandardResponse(response, 1, "绑定机构成功！");
        } catch (OaException ex) {
            XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
        } catch (Exception e) {
            LOG.error("saveBindGroup failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "绑定机构失败！");
        }
    }

    /**
     * 获得绑定的职位
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void getBindPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String id = HttpRequestHelper.getParameter(request, "id");
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        try {
            List<InfoLayoutAssign> list = infoLayoutAssignBiz.getBindingPosts(id, flag);
            XMLResponse.outputXML(response, new InfoLayoutAssignXml(list).toDocument());
        } catch (Exception e) {
            LOG.error("getBindPost failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "系统出错");
        }
    }

    /**
     * 绑定职位
     * 
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     * 
     */
    private void saveBindPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String id = HttpRequestHelper.getParameter(request, "id");
        int flag = HttpRequestHelper.getIntParameter(request, "flag", 0);
        if (id == null) {
            XMLResponse.outputStandardResponse(response, 0, "请传入正确的参数!");
            return;
        }
        String[] postIDs = HttpRequestHelper.getParameters(request, "post_ids");
        try {
            infoLayoutAssignBiz.txBindingPosts(id, postIDs, flag);
            XMLResponse.outputStandardResponse(response, 1, "绑定用户成功！");
        } catch (OaException ex) {
            LOG.error("saveBindPost failed: ", ex);
            XMLResponse.outputStandardResponse(response, 0, ex.getMessage());
        } catch (Exception e) {
            LOG.error("saveBindPost failed: ", e);
            XMLResponse.outputStandardResponse(response, 0, "绑定用户失败！");
        }
    }

}
