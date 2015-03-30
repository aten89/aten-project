<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/MyCustomer/CustomerInfo/editReturnVist.js"></script>
<title></title>
</head>
<body class="bdNone">
<input type="hidden" id="customerId"  value="${customer.id}" />
<input type="hidden" id="sex"  value="${customer.sex}" />
<input type="hidden" id="custProperty"  value="${customer.custProperty}" />
<input type="hidden" id="communicationType"  value="${customer.communicationType}" />
<input type="hidden" id="status" value="${customer.status}" />
<input type="hidden" id="returnVistId" value="${returnVist.id}" />
<input type="hidden" id="recommendProductHid" value="${customer.recommendProduct}" />

<input type="hidden" id="bankName" value="${customer.bankName}" />
<input type="hidden" id="bankBranch" value="${customer.bankBranch}" />
<input type="hidden" id="bankAccount" value="${customer.bankAccount}" />

<div class="addCon">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="6" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>客户名称：</td>
		<td><input id="custName" type="text" maxlength="40" class="ipt01" value="${customer.custName}"/></td>
		<td class="tit"><span class="cRed">*</span>性别： </td>
		<td><div id="sexSel" name="sexSel"></div></td>	
		<td class="tit"><span class="cRed">*</span>客户性质：</td>
		<td ><div id="customerNatureSel" name="customerNatureSel"></div></td>
	</tr>
    <tr>
      <td class="tit">出生日期：</td>
	  <td><input style="width:110px" readonly"readonly" name="birthday" id="birthday" type="text" class="invokeBoth" value="<fmt:formatDate value="${customer.birthday}" pattern="yyyy-MM-dd"/>"/></td>
      <td class="tit">年龄：</td>
      <td><input class="ipt01" name="age" id="age" type="text" maxlength="3"  value="${customer.age}" /></td>
      <td class="tit"><span class="cRed">*</span>电话：</td>
      <td>
	      <c:choose>
	      	<c:when test="${isTelEditable == true}">
	      		<input name="tel" id="tel" maxlength="16" class="ipt01" type="text" style="width:50%" value="${customer.tel}" />
	      	</c:when>
	      	<c:otherwise>
	      		<input name="tel" id="tel" maxlength="16" class="ipt01" type="hidden"  value="${customer.tel}" />
	      		<input name="tel" id="telStr" maxlength="16" disabled="disabled" class="ipt01" style="width:50%" type="text"  value="${hidTelStr}" />
	      	</c:otherwise>
	      </c:choose>
	      <span id="telArea" areacode="${customer.telPartArea.areaCode}" style="color:blue">（${customer.telPartArea.cityName}）</span>
      </td>
    </tr>
    <tr>
      <td class="tit">邮箱：</td>
      <td><input name="email" id="email" maxlength="30" class="ipt01" type="text"  value="${customer.email}" /></td>
      <td class="tit">通讯地址：</td>
      <td colspan="3"><input class="ipt01" name="address" id="address" type="text" maxlength="100"  value="${customer.address}" /></td>
    </tr>
    <tr>
      <td class="tit">投资顾问</td>
      <td><input maxlength="50" class="ipt01" type="text" id="saleMan" name="saleMan"  value="${customer.saleManName}"/></td>
      <td class="tit">理财金额(万)：</td>
      <td><input class="ipt01" name="financingAmount" id="financingAmount" type="text" maxlength="8"  value="${customer.financingAmount}" /></td>
      <td class="tit">期望投资年限：</td>
      <td><input class="ipt01" name="expectedInvestAgelimit" id="expectedInvestAgelimit" type="text" maxlength="5"  value="${customer.expectedInvestAgelimit}" /></td>
    </tr>
    <tr>
      <td class="tit">投资经历：</td>
      <td colspan="5"><textarea id="investExperience" name="investExperience" class="area01">${customer.investExperience}</textarea></td>
    </tr>
    <tr>
      <td class="tit">期望产品：</td>
      <td colspan="5"><input class="ipt01" name="expectedProduct" id="expectedProduct" type="text" maxlength="100"  value="${customer.expectedProduct}" /></td>
    </tr>
    <tr>
      <td class="tit">推荐产品：</td>
      <td colspan="4"><div id="prodInfoSel" name="prodInfoSel"></div></td>
      <td><a href="javascript:void(0);" onclick="viewProductInfo();">查看产品详细信息</a></td>
    </tr>
    <tr>
      <td class="tit">沟通类型：</td>
      <td colspan="5"><div id="commTypeSel" name="commTypeSel"></div></td>
    </tr>
    <tr>
      <td class="tit">沟通结果：</td>
      <td colspan="5"><textarea id="communicationResult" name="communicationResult" class="area01">${customer.communicationResult}</textarea></td>
    </tr>
  </table>
  <div class="btTip"></div>
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
  	<tr>
    	<th colspan="6" class="tipBg">回访信息</th>
    </tr>
    <tr>
 		<td class="tit">客户咨询内容：</td>
 		<td><textarea id="customerConsultContent" name="customerConsultContent" class="area01">${customerConsult.content}</textarea></td>
  	</tr>
  	<tr>
 		<td class="tit"><span class="cRed">*</span>回访内容：</td>
 		<td><textarea id="returnVistContent" name="returnVistContent" class="area01">${returnVist.content}</textarea></td>
  	</tr>
  </table>
</div>
<!--新增客户资料 end-->
<div class="addTool2">
	<input type="button" id="addAppointmentRecord"  class="allBtn" value="添加预约"/>
<c:choose>
	<c:when test="${customer.status == '1'}">
	&nbsp;&nbsp;<input type="button" id="pass"  class="allBtn" value="通过"/>
  	&nbsp;&nbsp;<input type="button" id="reject" class="allBtn" value="驳回"/>
  	&nbsp;&nbsp;<input type="button" id="unpass" class="allBtn" value="不通过"/>
	</c:when>
	<c:otherwise>
    &nbsp;&nbsp;<input type="button" id="save"  class="allBtn" value="保存"/>
  	&nbsp;&nbsp;<input type="button" id="close" class="allBtn" value="关闭"/>
	</c:otherwise>
</c:choose>
  	&nbsp;&nbsp;<input type='button' class='allBtn' value='电话呼叫' onclick="top.CRM_CALLCENTER_OBJ.dialout($('#tel').val(),$('#telArea').attr('areacode'));"/>
  	&nbsp;&nbsp;<input type='button' class='allBtn' value='挂机' onclick='top.CRM_CALLCENTER_OBJ.handUp();'/>
<!--   &nbsp;&nbsp;<input type='button' class='allBtn' value='示闲' onclick='showIdle();'/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='示忙' onclick='showBusy();'/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='静音' onclick='showMute();'/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='取消静音' onclick='cancelMute();'/>
 -->
</div>
</body>