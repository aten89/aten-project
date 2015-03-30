/*
 * @(#) MyDataCtrl.java 1.0 09/11/05
 */

package org.eapp.oa.address.ctrl;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eapp.comobj.SessionAccount;
import org.eapp.oa.address.blo.IAddressListBiz;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.util.web.HttpRequestHelper;
import org.eapp.oa.system.util.web.XMLResponse;
import org.eapp.util.spring.SpringHelper;

/**
 * 通讯录 我的资料
 *
 */
public class MyAddressCtrl extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6364557693988364789L;
	
	private AddressListCtrl addressListCtrl;
	private IAddressListBiz addressListBiz;
	public MyAddressCtrl(){
		super();
	}
	public void init(ServletConfig config) throws ServletException {
		addressListCtrl = new AddressListCtrl();
		addressListBiz = (IAddressListBiz) SpringHelper.getBean("addressListBiz");
	}
	
	public void doGet( HttpServletRequest request, HttpServletResponse response )
		throws ServletException, IOException{
		doPost( request, response );
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response )
		throws ServletException, IOException{
		String action = HttpRequestHelper.getParameter( request, "act" );
		if(SysConstants.VIEW.equals(action)){
			//初始化通讯录详情查看页面
			initPage(request, response, "/page/address/myaddr/view_addr.jsp");
			return;
		}
		else if("initmodify".equals(action)){
			//初始化通讯录编辑页面
			initPage(request, response, "/page/address/myaddr/edit_addr.jsp");
			return;
		}
		else if(SysConstants.MODIFY.equals(action) ){
			//保存通讯录
			addressListCtrl.saveOrUpdateAddrList( request, response );			
			return;
		}
	}
	
	public void destroy(){
		super.destroy();
	}	
	
	public void initPage(HttpServletRequest request, HttpServletResponse response, String pageUrl)
			throws ServletException, IOException{
		SessionAccount user = (SessionAccount)request.getSession()
				.getAttribute(SysConstants.SESSION_USER_KEY);
		if (user == null) {
			XMLResponse.outputStandardResponse(response, 0, "请先登录");
		}
		AddressList addrList = addressListBiz.getByAccountId(user.getAccountID());
		if(addrList == null){
			//还没有该登录用户的通讯录时，也会有该登录用户的帐户id、姓名、所属机构、职位信息，这些信息通过设置帐户id即可取到
			addrList = new AddressList();
			addrList.setUserAccountId(user.getAccountID());
		}
		request.setAttribute("addrList", addrList);
		if(addrList.getUserPhoto() != null){
			request.setAttribute("saveImgPath" , addrList.getUserPhotoPath());
		}
		
		if(pageUrl != null){
			request.getRequestDispatcher( pageUrl ).forward( request, response );
		}
	}	
}



