<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<link rel="stylesheet" type="text/css" href="commui/autocoomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="commui/autocoomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="commui/autocoomplete/jquery.bgiframe.min.js"></script>
<script type="text/javascript" src="page/operationsManage/paymentRegister/addFBYYCustPayment.js"></script>

<title></title>
<style>
html {
	overflow: hidden;
	height: 100%;
	_padding: 0 0 43px;
}
</style>
</head>
<body class="tabBd">
<div class="tabScroll" style="overflow-x: hidden">  
<div class="addCon openWinW03">
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>投资顾问：</td>
		<td>
			<input id="custManageId" type="hidden" />
			<input id="custManage" type="text" maxlength="40" class="ipt05" disabled="disabled" />
		</td>
		<td class="tit"><span class="cRed">*</span>是否老客户划款： </td>
		<td>
			 <input name="isOldCustRadio" type="radio" value="false" class="cBox" onClick="initIconSelect(this)" id="icon1" checked="checked" /> 否
	         <input name="isOldCustRadio" type="radio" value="true" class="cBox" onClick="initIconSelect(this)" id="icon2"  /> 是
		</td>
	</tr>
    <tr>
      	<td class="tit"><span class="cRed">*</span>客户名称：</td>
      	
	    <td name="custName">
			<input type="text" id="custName"  class="ipt05 iptSuggest"/>
			<input type="hidden" id="custId"/>
			<input type="hidden" id="oriSelected"/>
		</td>
		
		<td name="none">
				<div style="width: 139px;">
					<div id="customerInfoSel" name="customerInfoSel"></div>
				</div>
			</td>
      	
		<td class="tit"><span class="cRed">*</span>客户性质：</td>
		<td>
			<div id="customerNatureSel" name="customerNatureSel"></div>
		</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>产品项目：</td>
		<td colspan="3">
			<div id="prodInfoSel" name="prodInfoSel"></div>
		</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>预约金额(万)：</td>
		<td><input id="appointmentAmount" type="text" maxlength="40" class="ipt05" /></td>
		<td class="tit"><span class="cRed">*</span>预约金额大写：</td>
		<td>
			<div id="appointmentAmountCapital"></div>
		</td>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>预计划款时间： </td>
		<td colspan="3">
			<input style="width:110px" readonly="readonly" name="transferDate" id="transferDate" type="text" class="invokeBoth" />
		</td>
	</tr>
  </table>
  <div class="btTip"></div>
</div>
</div>

<!--新增划款 end-->
<div class="oppBtnBg tabBottom">
  <input type="button" id="save"  class="allBtn" value="提交审批"/>&nbsp;&nbsp;
  <input type="button" id="close" class="allBtn" value="取消"/>
</div>

</body>

</html>