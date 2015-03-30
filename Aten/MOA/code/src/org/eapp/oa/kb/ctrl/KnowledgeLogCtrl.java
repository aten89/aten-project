package org.eapp.oa.kb.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eapp.oa.kb.blo.IKnowledgeLogBiz;
import org.eapp.oa.kb.dto.KnowledgeLogPage;
import org.eapp.oa.kb.dto.KnowledgeLogQueryParameters;
import org.eapp.oa.kb.hbean.KnowledgeLog;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;

/**
 * 知识点日志ctrl
 */
public class KnowledgeLogCtrl extends HttpServlet {

    /**
     * serialVersionUID
     */
	private static final long serialVersionUID = -41129433248468922L;
	
	/**
	 * 知识点日志业务层
	 */
	private IKnowledgeLogBiz knowledgeLogBiz;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		knowledgeLogBiz = (IKnowledgeLogBiz)SpringHelper.getSpringContext().getBean("knowledgeLogBiz");
		super.init(config);
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		String act = HttpRequestHelper.getParameter(req, "act");
		if (SysConstants.QUERY.equals(act)) {
			loadKnowledgeLogListPage(req, resp);
		}
	}
	
    /**
     * 获取知识库操作分页
     */
	private void loadKnowledgeLogListPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		try{
			KnowledgeLogQueryParameters queryParameters = new KnowledgeLogQueryParameters();
	
			int pageNo = HttpRequestHelper.getIntParameter(req, "pageNo", 1);
			queryParameters.setPageNo(pageNo);
			
			int pageSize = HttpRequestHelper.getIntParameter(req, "pageSize", 12);
			queryParameters.setPageSize(pageSize);			
			
			String userid = HttpRequestHelper.getParameter(req, "userid");
			queryParameters.setUserid(userid);
			
			String knowledgeid = HttpRequestHelper.getParameter(req, "knowledgeid");
			queryParameters.setKnowledgeid(knowledgeid);
			
			String knowledgetitle = HttpRequestHelper.getParameter(req, "knowledgetitle");
			queryParameters.setKnowledgetitle(knowledgetitle);
			
			String beginOperatetime = DataFormatUtil.formatTime(req.getParameter("beginOperatetime"));
			if (StringUtils.isNotEmpty(beginOperatetime)) {
				queryParameters.setBegingOperatetime(Timestamp.valueOf(beginOperatetime));
			}
			
			String endOperatetime = DataFormatUtil.formatTime(req.getParameter("endOperatetime"));
			if (StringUtils.isNotEmpty(endOperatetime)) {// 加一天
				Timestamp t = Timestamp.valueOf(endOperatetime);
				Calendar ca = Calendar.getInstance();
				ca.setTimeInMillis(t.getTime());
				ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
				t.setTime(ca.getTimeInMillis());
				queryParameters.setEndOperatetime(t);
			}
			queryParameters.addOrder("operatetime", false);
			ListPage<KnowledgeLog> list = knowledgeLogBiz.getKnowledgeLogListPage(queryParameters);
			XMLResponse.outputXML(resp, new KnowledgeLogPage(list).toDocument());
		} catch (Exception e) {
			e.printStackTrace();
			XMLResponse.outputStandardResponse(resp, 0, "系统出错");
		}		
	}
}
