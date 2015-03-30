<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/customerServiceManage/customerVisit/customerVisitFrame.js"></script>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body class="bdNone">
<input id="FrameHidModuleRights" type="hidden" value="<eapp:menu key='CUST_VISIT'/>" />
<div class="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td width="131" valign="top" class="tdValign1"><div class="costsNav" id="crmSubNav">
              <h1>客户回访</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="waitVisitCustomer" url="page/customerServiceManage/customerVisit/toDoVisitCustomerList.jsp"><span class="costsDbsqd flatIcon_c1"></span>待回访客户</li>
                  <li id="potentialCustomer" url="page/customerServiceManage/customerVisit/potentialCustomerList.jsp"><span class="costsDbsqd flatIcon_c2"></span>潜在客户</li>
                  <li id="officialCustomer" url="page/customerServiceManage/customerVisit/visitCustomerList.jsp"><span class="costsDbsqd flatIcon_c3"></span>成交客户</li>
                </ul> 
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td class="crmFrameBg" width="100%" valign="top"><div class="crmFrameHead flatHide"><img src="themes/comm/images/frameNav2.gif"/><span id="crmFrameHead">客户回访</span></div>
            <div class="blank2 flatHide"></div>
            <div id="costsCon" class="costsCon"> </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
