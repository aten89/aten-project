package org.eapp.oa.system.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.blo.ITaskBiz;
import org.eapp.oa.flow.ctrl.TaskDealCtrl;
import org.eapp.oa.flow.dto.TaskPage;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.info.blo.IInformationBiz;
import org.eapp.oa.info.ctrl.InformationCtrl;
import org.eapp.oa.info.dto.InformationPage;
import org.eapp.oa.info.dto.InformationQueryParameters;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;
import org.eapp.util.spring.SpringHelper;


/**
 * 处理门户请求的数据
 * @author zsy
 * @version
 */
public class PortletCtrl extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5572557105969757507L;
    /**
     * 任务处理业务逻辑接口
     */
    private ITaskBiz taskBiz;
    /**
     * 问题业务逻辑接口
     */
//    private IProblemBiz problemBiz;
    /**
     * informationBiz
     */
    private IInformationBiz informationBiz;

    /**
     * taskDealCtrl
     */
    private TaskDealCtrl taskDealCtrl;

    /**
     * informationCtrl
     */
    private InformationCtrl informationCtrl;
    /**
     * 通讯录模块中的我的资料子模块的业务逻辑处理接口
     */
//    private IAddressListBiz addressListBiz;

    /**
     * Constructor of the object.
     */
    public PortletCtrl() {
        super();
    }

    /**
     * 加载
     */
    public void init() throws ServletException {
        taskBiz = (ITaskBiz) SpringHelper.getBean("taskBiz");
//        problemBiz = (IProblemBiz) SpringHelper.getSpringContext().getBean("problemBiz");
        informationBiz = (IInformationBiz) SpringHelper.getBean("informationBiz");
        /**
         * 通讯录模块中的我的资料子模块的业务逻辑处理接口
         */
//        addressListBiz = (IAddressListBiz) SpringHelper.getSpringContext().getBean("addressListBiz");

        taskDealCtrl = new TaskDealCtrl();
        taskDealCtrl.init();

        informationCtrl = new InformationCtrl();
        informationCtrl.init();
    }

    /**
     * get方法
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-23		卢凯宁		修改注释
     * </pre>
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * post 方法
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("act");
        if ("dealingtasks".equalsIgnoreCase(action)) {
            // 所有待办任务门户
            getDealingTasks(request, response);
            return;
        } else if ("moredealingtasks".equalsIgnoreCase(action)) {
            // 所有待办任务门户-更多页面
            getMoreDealingTasks(request, response);
            return;
        } else if ("dispose".equalsIgnoreCase(action)) {
            // 处理任务
            turnToViewPage(request, response);
            return;
//        } else if ("problems".equalsIgnoreCase(action)) {
//            // 用户登记的问题
//            getProblems(request, response);
//            return;
//        } else if ("moreproblems".equalsIgnoreCase(action)) {
//            // 用户登记的问题-更多页面
//            initMoreProblems(request, response);
//            return;
//        } else if ("initaddproblem".equalsIgnoreCase(action)) {
//            // 转到保存用户登记的问题
//            initAddProblem(request, response);
//        } else if ("addproblem".equalsIgnoreCase(action)) {
//            // 保存用户登记的问题
//            addProblem(request, response);
//        } else if ("viewproblem".equalsIgnoreCase(action)) {
//            // 查看用户登记的问题
//            viewProblem(request, response);
//        } else if ("delproblem".equalsIgnoreCase(action)) {
//            // 删除用户登记的问题
//            deleteProblem(request, response);

        } else if ("infoportlet".equalsIgnoreCase(action)) {
            // 信息-门户页面
            initInfoPortlet(request, response);
            return;
        } else if ("moreinfos".equalsIgnoreCase(action)) {
            // 信息-更多页面
            initMoreInfos(request, response);
            return;
        } else if ("getinfos".equalsIgnoreCase(action)) {
            // 信息-查询
            getInfos(request, response);
            return;
        } else if ("viewinfo".equalsIgnoreCase(action)) {
            // 查看信息
            viewInfo(request, response);
//        } else if ("initsearchkb".equalsIgnoreCase(action)) {
//            // 转到搜索页面
//            initSearchKnowledge(request, response);
        }
    }

    /**
     * 所有待办任务门户
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-23		卢凯宁		修改注释
     * </pre>
     */
    private void getDealingTasks(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录");
            return;
        }
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", QueryParameters.ALL_PAGE_SIZE);
        QueryParameters qp = new QueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        ListPage<Task> reis = taskBiz.getDealingTask(qp, user.getAccountID());
        XMLResponse.outputXML(response, new TaskPage(reis).toDocument());
    }

    /**
     * 所有待办任务门户-更多页面
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期               修改人       修改原因
     * 2012-8-23        卢凯宁     修改注释
     * </pre>
     */
    private void getMoreDealingTasks(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.getRequestDispatcher("/page/portlet/task/more_task.jsp").forward(request, response);
    }

    /**
     * 处理任务
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期               修改人       修改原因
     * 2012-8-23        卢凯宁   修改注释
     * </pre>
     */
    private void turnToViewPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        taskDealCtrl.turnToViewPage(request, response);
    }

//    /**
//     * 处理任务
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     */
//    private void getProblems(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        SessionAccountInfo user = (SessionAccountInfo) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
//        if (user == null) {
//            XMLResponse.outputStandardResponse(response, 0, "请先登录");
//            return;
//        }
//        int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
//        int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", QueryParameters.ALL_PAGE_SIZE);
//        String type = HttpRequestHelper.getParameter(request, "type");
//        ProblemQueryParameters rqp = new ProblemQueryParameters();
//        rqp.setPageNo(pageNo);
//        rqp.setPageSize(pageSize);
//        rqp.setSubmitter(user.getAccountID());
//        rqp.setType(type);
//        rqp.addOrder("submitDate", false);
//        ListPage<Problem> page = problemBiz.queryProblem(rqp);
//        XMLResponse.outputXML(response, new ProblemPage(page).toDocument());
//    }
//
//    /**
//     * 用户登记的问题-更多页面
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     * 
//     *             <pre>
//     * 修改日期               修改人       修改原因
//     * 2012-8-23        卢凯宁     修改注释
//     * </pre>
//     */
//    private void initMoreProblems(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        request.getRequestDispatcher("/page/Portlet/Problem/moreProblems.jsp").forward(request, response);
//    }
//
//    /**
//     * 转到保存用户登记的问题
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     * 
//     *             <pre>
//     * 修改日期               修改人       修改原因
//     * 2012-8-23        卢凯宁     修改注释
//     * </pre>
//     */
//    private void initAddProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        Problem problem = new Problem();
//        SessionAccountInfo user = (SessionAccountInfo) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
//        if (user == null) {
//            request.setAttribute("errorMsg", "请先登录");
//            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
//            return;
//        }
//        problem.setSubmitter(user.getAccountID());
//        problem.setSubmitterDept(user.getGroupNames());
//        problem.setSubmitDate(new Date());
//        problem.setStatus(Problem.STATUS_NEW);
//        request.setAttribute("problem", problem);
//        request.getRequestDispatcher("/page/Portlet/Problem/ProblemManage.jsp").forward(request, response);
//    }
//
//    /**
//     * 保存用户登记的问题
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     * 
//     *             <pre>
//     * 修改日期               修改人       修改原因
//     * 2012-8-23        卢凯宁     修改注释
//     * </pre>
//     */
//    private void addProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        SessionAccountInfo user = (SessionAccountInfo) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
//        if (user == null) {
//            request.setAttribute("errorMsg", "请先登录");
//            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
//            return;
//        }
//        String subject = HttpRequestHelper.getParameter(request, "subject");
//        String content = HttpRequestHelper.getParameter(request, "content");
//        String type = HttpRequestHelper.getParameter(request, "type");
//        if (subject == null || type == null) {
//            XMLResponse.outputStandardResponse(response, 0, "参数为空");
//            return;
//        }
//        try {
//            Problem p = problemBiz.txAddProblem(user.getAccountID(), user.getGroupNames(), subject, content, type);
//            // 发送邮件
//            SysRuntimeParams srtp = SysRuntimeParams.loadSysRuntimeParams();
//            //2012-04-24修改：取邮箱地址改成从通讯录里面去，没有在用账号拼接邮箱地址
//            String emaliAddr = "";
//            AddressList addressList = addressListBiz.getByAccountId(srtp.getProblemProcessor());
//            if (addressList != null) {
//                emaliAddr = addressList.getUserEmail();
//            }
//            if (StringUtils.isEmpty(emaliAddr)) {
//                emaliAddr = TransformTool.getEmail(srtp.getProblemProcessor());
//            }
//            JMailProxy.daemonSend(new MailMessage(emaliAddr, srtp
//                    .getProblemTitle(), srtp.getProblemContent().replaceAll("@subject", subject)
//                    .replaceAll("@submitter", p.getSubmitterName())
//                    .replaceAll("@submitDate", DataFormatUtil.noNullValue(p.getSubmitDate()))));
//            XMLResponse.outputStandardResponse(response, 1, p.getId());
//        } catch (Exception e) {
//            e.printStackTrace();
//            XMLResponse.outputStandardResponse(response, 0, "操作失败");
//        }
//
//    }
//
//    /**
//     * 查看用户登记的问题
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     * 
//     *             <pre>
//     * 修改日期               修改人       修改原因
//     * 2012-8-23        卢凯宁     修改注释
//     * </pre>
//     */
//    private void viewProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        String problemID = HttpRequestHelper.getParameter(request, "problemid");
//        if (problemID == null) {
//            request.setAttribute(SysConstants.REQUEST_ERROR_MSG, "问题ID不能为空");
//            request.getRequestDispatcher("/page/error.jsp").forward(request, response);
//            return;
//        }
//        Problem problem = problemBiz.getProblemById(problemID);
//
//        request.setAttribute("problem", problem);
//        request.getRequestDispatcher("/page/Portlet/Problem/ProblemManage.jsp").forward(request, response);
//    }
//
//    /**
//     * 删除用户登记的问题
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     * 
//     *             <pre>
//     * 修改日期               修改人       修改原因
//     * 2012-8-23        卢凯宁     修改注释
//     * </pre>
//     */
//    private void deleteProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        String problemId = HttpRequestHelper.getParameter(request, "problemid");
//        if (problemId == null) {
//            XMLResponse.outputStandardResponse(response, 0, "参数不能为空");
//            return;
//        }
//        try {
//            problemBiz.txDeleteProblem(problemId);
//            XMLResponse.outputStandardResponse(response, 1, "删除成功");
//        } catch (Exception e) {
//            XMLResponse.outputStandardResponse(response, 0, "删除失败");
//        }
//    }

    /**
     * 信息-门户页面
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期               修改人       修改原因
     * 2012-8-23        卢凯宁     修改注释
     * </pre>
     */
    private void initInfoPortlet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String layoutName = HttpRequestHelper.getParameter(request, "layoutname");
        request.setAttribute("layoutName", layoutName);
        request.getRequestDispatcher("/page/portlet/info/query_info.jsp").forward(request, response);
    }

    /**
     * 信息-更多页面
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期               修改人       修改原因
     * 2012-8-23        卢凯宁     修改注释
     * </pre>
     */
    private void initMoreInfos(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String layoutName = HttpRequestHelper.getParameter(request, "layoutname");

//        List<DataDictionaryDTO> infoClass = SysCodeDictLoader.getInstance().getSubInfoClass(layoutName);
        request.setAttribute("layoutName", layoutName);
//        request.setAttribute("infoClass", infoClass);
        request.getRequestDispatcher("/page/portlet/info/more_info.jsp").forward(request, response);
    }

    /**
     * 信息-查询
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期               修改人       修改原因
     * 2012-8-23        卢凯宁     修改注释
     * </pre>
     */
    private void getInfos(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String layoutName = HttpRequestHelper.getParameter(request, "layoutname");
        String infoClass = HttpRequestHelper.getParameter(request, "infoclass");
        String subject = HttpRequestHelper.getParameter(request, "subject");
        int pageNo = HttpRequestHelper.getIntParameter(request, "pageno", 1);
        int pageSize = HttpRequestHelper.getIntParameter(request, "pagecount", QueryParameters.ALL_PAGE_SIZE);
        InformationQueryParameters qp = new InformationQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        qp.setInfoClass(infoClass);
        qp.setSubject(subject);
        qp.setInfoLayout(layoutName);
        qp.setInfoStatus(Information.STATUS_PUBLISH);
        qp.setInfoPropertys(new Integer[] { Information.PROPERTY_COMMON, Information.PROPERTY_TOTOP });
        qp.addOrder("infoProperty", true);
        qp.addOrder("publicDate", false);

        ListPage<Information> list = informationBiz.queryInformation(qp);
        InformationPage listPage = new InformationPage(list);
        XMLResponse.outputXML(response, listPage.toDocument());
    }

    /**
     * 查看信息
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     * 
     *             <pre>
     * 修改日期               修改人       修改原因
     * 2012-8-23        卢凯宁     修改注释
     * </pre>
     */
    private void viewInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        informationCtrl.view(request, response);
    }

//    /**
//     * 转到搜索页面
//     * 
//     * @param request request
//     * @param response response
//     * @throws ServletException servlet异常
//     * @throws IOException io异常
//     * 
//     *             <pre>
//     * 修改日期               修改人       修改原因
//     * 2012-8-23        卢凯宁     修改注释
//     * </pre>
//     */
//    private void initSearchKnowledge(HttpServletRequest request, HttpServletResponse response) throws ServletException,
//            IOException {
//        String keyword = HttpRequestHelper.getParameter(request, "keyword");
////        keyword = new String(keyword.getBytes("iso-8859-1"), "UTF-8"); // 转码
//        String prodName = HttpRequestHelper.getParameter(request, "prodName");
////        prodName = new String(prodName.getBytes("iso-8859-1"), "UTF-8"); // 转码
//        request.setAttribute("keyword", keyword);
//        request.setAttribute("prodName", prodName);
//        request.getRequestDispatcher("/page/Portlet/Knowledge/search.jsp").forward(request, response);
//    }
}
