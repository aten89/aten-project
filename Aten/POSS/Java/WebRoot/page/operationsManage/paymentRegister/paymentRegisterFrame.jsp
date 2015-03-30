<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>

<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<link rel="stylesheet" type="text/css" href="commui/autocoomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="commui/autocoomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="commui/autocoomplete/jquery.bgiframe.min.js"></script>

<script type="text/javascript" src="page/operationsManage/paymentRegister/paymentRegisterFrame.js"></script>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body class="bdNone">
<input id="FrameHidModuleRights" type="hidden" value="<eapp:menu key='CUST_PAYMENT'/><eapp:menu key='FBYY'/>" />
<div class="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td width="131" valign="top"><div class="crmSubNav crmSubLevel" id="crmSubNav">
              <h1>划款登记</h1>
              <div class="mb">
                <ul id="menuList">
                  <!--客户划款 -->	
                  <li id="paymentReg1" url="page/operationsManage/paymentRegister/paymentToDoList.jsp?standardFlag=false" class="subFirst" id="payment"><input type="button" class="draft_icon"/>客户划款</li>
                  <li id="paymentReg2" url="page/operationsManage/paymentRegister/paymentToDoList.jsp?standardFlag=false" class="submb" id="payment_todo">
                    <div><input type="button" class="dbgd_icon"/>待办</div>
                  </li>
                  <li id="paymentReg3" url="page/operationsManage/paymentRegister/paymentTrackList.jsp?standardFlag=false" class="submb" id="payment_track">
                    <div><input type="button" class="track_icon"/>跟踪</div>
                  </li>
                  <li id="paymentReg4" url="page/operationsManage/paymentRegister/paymentArchList.jsp?standardFlag=false" class="submb" id="payment_arch">
                    <div><input type="button" class="archived_icon"/>归档</div>
                  </li>
                  <li id="paymentReg5" url="page/operationsManage/paymentRegister/paymentQueryList.jsp?standardFlag=false" class="subLast" id="payment_query">
                    <div><input type="button" class="xsdcx_icon"/>查询</div>
                  </li>
                  
                  <!--非标预约 -->	
                  <li id="nsPaymentReg1" url="page/operationsManage/paymentRegister/paymentToDoList.jsp?standardFlag=true" class="subFirst subOneMar"><input type="button" class="draft_icon"/>非标预约</li>
                  <li id="nsPaymentReg2" url="page/operationsManage/paymentRegister/paymentToDoList.jsp?standardFlag=true" class="submb">
                    <div><input type="button" class="dbgd_icon"/>待办</div>
                  </li>
                   <li id="nsPaymentReg3" url="page/operationsManage/paymentRegister/paymentTrackList.jsp?standardFlag=true" class="submb">
                    <div><input type="button" class="track_icon"/>跟踪</div>
                  </li>
                  <li id="nsPaymentReg4" url="page/operationsManage/paymentRegister/paymentArchList.jsp?standardFlag=true" class="submb">
                    <div><input type="button" class="archived_icon"/>归档</div>
                  </li>
                  <li id="nsPaymentReg5" url="page/operationsManage/paymentRegister/paymentQueryList.jsp?standardFlag=true" class="subLast">
                    <div><input type="button" class="xsdcx_icon"/>查询</div>
                  </li>
                </ul> 
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td class="crmFrameBg" width="100%" valign="top"><div class="crmFrameHead flatHide"><input type="button" class="frameNav2" /><span  id="crmFrameHead">客户划款</span></div>
            <div class="blank2 flatHide"></div>
            <div id="costsCon" class="costsCon"> </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>
