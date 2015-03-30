<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/MyCustomer/CustomerInfo/customerInfoFrame.js"></script>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='${param.moduleKey }'/>" />
<input id="customerId" type="hidden" value="${param.customerId }" />
<div class="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td width="131" valign="top"><div class="crmSubNav crmSubLevel" id="crmSubNav">
              <h1>客户资料</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="custInfo" url="m/customer/view?customerId=${param.customerId}">基本信息</li>
                  <li id="consultRecord" url="page/MyCustomer/CustomerInfo/consultRecordList.jsp">咨询记录</li>
                  <li id="consultRecord" url="page/MyCustomer/CustomerInfo/transferRecordList.jsp">划款记录</li>
                  <li id="custDealRecord" url="page/MyCustomer/CustomerInfo/transferRecordDealList.jsp">客户成交记录</li>
                  <li id="visitRecord" url="page/MyCustomer/CustomerInfo/visitRecordList.jsp">回访记录</li>
                  <li id="reservationRecord" url="page/MyCustomer/CustomerInfo/appointmentRecordList.jsp">预约记录</li>
                  <li id="messageRecord" url="page/MyCustomer/CustomerInfo/messageList.jsp">短信记录</li>
                </ul> 
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td class="crmFrameBg" width="100%" valign="top"><div class="crmFrameHead flatHide"><img src="themes/comm/images/frameNav2.gif"/><span id="crmFrameHead">客户资料</span></div>
            <div class="blank2 flatHide"></div>
            <div id="costsCon" class="costsCon"> </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
