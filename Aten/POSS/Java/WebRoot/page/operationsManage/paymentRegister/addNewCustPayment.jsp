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
<script type="text/javascript" src="page/operationsManage/paymentRegister/addNewCustPayment.js"></script>

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
		<td class="tit"><span class="cRed">*</span>划款时间： </td>
		<td>
			<input style="width:110px" readonly="readonly" name="transferDate" id="transferDate"
				value="<fmt:formatDate value="${custPayment.transferDate }" pattern="yyyy-MM-dd"/>" type="text" class="invokeBoth" />
		</td>
	</tr>
    <tr>
      	<td class="tit"><span class="cRed">*</span>客户名称：</td>
		<td >
							<input type="text" id="custName"  class="ipt05 iptSuggest"/>
							<input type="hidden" id="custId"/>
							<input type="hidden" id="oriSelected"/>
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
    	<td class="tit"><span class="cRed">*</span>有无定金：</td>
		<td colspan="3">
			<div id="earnestSel">
						        <div isselected="true">**请选择...</div>
						        <div>1**有</div>
						        <div>0**无</div>
				            </div>
		</td>
    </tr>
    <tr>
    	<td class="tit">剩余额度(万)：</td>
		<td><input id="remainingAmount" type="text" maxlength="40" class="ipt05" disabled="disabled" /></td>
		<td class="tit">已划款总额度(万)：</td>
		<td><input id="transferAmount" type="text" maxlength="40" class="ipt05" disabled="disabled" /></td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>预约金额(万)：</td>
		<td><input id="appointmentAmount" type="text" maxlength="40" class="ipt05" /></td>
		<td class="tit"><span class="cRed">*</span>预约金额大写：</td>
		<td>
			<div id="appointmentAmountCapital"></div>
		</td>
    </tr>
  <!--   <tr>
    	<td class="tit"><span class="cRed">*</span>划款金额(万)：</td>
		<td><input id="payAmount" type="text" maxlength="40" class="ipt05" /></td>
		<td class="tit"><span class="cRed">*</span>划款金额大写：</td>
		<td><input id="payAmountCapital" type="text" maxlength="40" class="ipt05" /></td>
    </tr> 
    -->
    <tr>
      <td class="tit">备注：</td>
      <td colspan="3"><textarea id="remark" name="remark" class="area01"></textarea></td>
    </tr>
   <!--    <tr>
    <td class="tit"><span class="cRed">*</span>审批人：</td>
      <td colspan="3">
      	<input id="dispacherDis" type="text" maxlength="20" class="iptSo iptSoSleW" readonly="readonly" />
      	<input id="dispacher" type="hidden" />
      	<input id="openDispacher" class="selBtn" type="button"/>
      </td>
    </tr> -->
  </table>
  <div class="btTip"></div>
</div>
</div>
<!--新增划款 end-->
<div class="oppBtnBg tabBottom">
  <input type="button" id="save"  class="allBtn" value="提交"/>&nbsp;&nbsp;
  <input type="button" id="close" class="allBtn" value="取消"/>
</div>

</body>
</html>