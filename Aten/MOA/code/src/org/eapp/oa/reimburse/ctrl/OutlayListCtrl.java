package org.eapp.oa.reimburse.ctrl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.eapp.oa.reimburse.blo.IOutlayListBiz;
import org.eapp.oa.reimburse.dto.OutlayListPage;
import org.eapp.oa.reimburse.dto.OutlayListQueryParameters;
import org.eapp.oa.reimburse.hbean.OutlayList;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.spring.SpringHelper;
import org.eapp.util.web.DataFormatUtil;


public class OutlayListCtrl extends HttpServlet {
	
	private IOutlayListBiz outlayListBiz;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6294000416727176193L;

	/**
	 * Constructor of the object.
	 */
	public OutlayListCtrl() {
		super();
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		outlayListBiz =  (IOutlayListBiz) SpringHelper.getSpringContext().getBean("outlayListBiz");
	}
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doPost(request , response);

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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String act = HttpRequestHelper.getParameter(request, "act");	
		
		if(SysConstants.QUERY.equalsIgnoreCase(act)){
			query(request , response);
			return;
		}
	}
	
	
	private void query(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		int pageNo = HttpRequestHelper.getIntParameter(request, "pageNo", 1);
		int pageSize = HttpRequestHelper.getIntParameter(request, "pageSize", 12);
		String applicant = HttpRequestHelper.getParameter(request ,"applicant");
		String budgetItemName = HttpRequestHelper.getParameter(request, "budgetItemName");
		String beginArchiveDate = DataFormatUtil.formatTime( request.getParameter("beginArchiveDate"));
		String endArchiveDate = DataFormatUtil.formatTime( request.getParameter("endArchiveDate"));
		String outlayName = HttpRequestHelper.getParameter(request, "outlayName");
		String outlayCategory = HttpRequestHelper.getParameter(request, "outlayCategory");
//		String csProject = HttpRequestHelper.getParameter(request, "csProject");
		//构造查询参数
		OutlayListQueryParameters olp = new OutlayListQueryParameters();
		olp.setPageNo(pageNo);
		olp.setPageSize(pageSize);
		
		if(beginArchiveDate != null){
			olp.setBeginArchiveDate(Timestamp.valueOf(beginArchiveDate));
		}
		if(endArchiveDate != null){
			Timestamp t = Timestamp.valueOf(endArchiveDate);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(t.getTime());
			ca.set(Calendar.DATE, ca.get(Calendar.DATE) + 1);
			t.setTime(ca.getTimeInMillis());
			olp.setEndArchiveDate(t);
		}
		olp.setOutlayCategory(outlayCategory);
		olp.setOutlayName(outlayName);
		olp.setBudgetItem(budgetItemName);
//		olp.setCsProject(csProject);
		olp.setApplicant(applicant);
		olp.addOrder("outlayCategory", true);
		olp.addOrder("outlayName", true);
		olp.addOrder("id", true);
		//查询
		ListPage<OutlayList> outlayListPage = outlayListBiz.queryOutlayList(olp);
		//转DOC
		OutlayListPage outlayListPageDTO = new OutlayListPage(outlayListPage);
		Document xmlDoc = outlayListPageDTO.toDocument();
		XMLResponse.outputXML(response, xmlDoc);
	}
}
