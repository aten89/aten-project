package org.eapp.oa.flow.ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.flow.blo.IFlowDataBiz;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 知识点审核
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2012-7-11	方文伟	新建
 * </pre>
 */
public class FlowDataCtrl extends HttpServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 894932421382316916L;

    /**
     * 日志
     */
	private static Logger log = LoggerFactory.getLogger(FlowDataCtrl.class);
    /**
     * 流程数据库操作业务接口
     */
    private IFlowDataBiz flowDataBiz;

    /**
     * Constructor of the object.
     */
    public FlowDataCtrl() {
        super();
    }

    /**
     * 初始化
     * 
     * @param config servlet配置
     * @throws ServletException servlet异常
     */
    public void init(ServletConfig config) throws ServletException {
        flowDataBiz = (IFlowDataBiz) SpringHelper.getBean("flowDataBiz");
    }

    /**
     * 销毁方法
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * doGet方法
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * doPost方法
     * 
     * @param request request
     * @param response reponse
     * @throws ServletException servlet异常
     * @throws IOException IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = HttpRequestHelper.getParameter(request, "act");
        if (SysConstants.DELETE.equals(act)) {
            // 删除流程数据库
            deleteFlowData(request, response);
            return;
        } else if ("checkflowdata".equals(act)) {
            // 检查删除流程数据库是否完成操作.
            checkFlowData(request, response);
            return;
        }
    }

    /**
     * 删除流程数据库
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常卢凯宁     新建
     * </pre>
     */
    private void deleteFlowData(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        try {
            // 从前台获取日期,转换成 Timestamp
            String dateStr = HttpRequestHelper.getParameter(request, "date");
            Date date = new SimpleDateFormat(SysConstants.STANDARD_DATE_PATTERN).parse(dateStr);
            // 删除流程该日期之前的相关数据
            flowDataBiz.deleteFlowData(date);
            XMLResponse.outputStandardResponse(response, 1, "操作成功.");
        } catch (OaException e) {
            XMLResponse.outputStandardResponse(response, 0, e.getMessage());
        } catch (Exception e) {
        	log.error("deleteFlowData failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }
    }

    /**
     * 检查删除流程数据库操作是否完成
     * 
     * @param request request
     * @param response response
     * @throws ServletException servlet异常
     * @throws IOException io异常
     */
    private void checkFlowData(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
    	SessionAccount user = (SessionAccount) request.getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            XMLResponse.outputStandardResponse(response, 0, "请先登录!");
            return;
        }
        try {
            // 检查删除流程数据库操作是否完成
            Integer flag = flowDataBiz.getRunningFlag();
            XMLResponse.outputStandardResponse(response, 1, String.valueOf(flag));
        } catch (Exception e) {
        	log.error("checkFlowData failed:", e);
            XMLResponse.outputStandardResponse(response, 0, "系统错误");
        }
    }

}
