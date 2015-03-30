<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/MyCustomer/CustomerInfo/edit_customerinfo_div.js"></script>
<input type="hidden" id="customerId"  value="${customer.id}" />
<input type="hidden" id="sex"  value="${customer.sex}" />
<input type="hidden" id="custProperty"  value="${customer.custProperty}" />
<input type="hidden" id="communicationType"  value="${customer.communicationType}" />
<input type="hidden" id="status" value="${customer.status}" />
<input type="hidden" id="recommendProduct" value="${customer.recommendProduct}"/>
<div class="addCon">
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
 	<tr>
    	<th colspan="6" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>客户名称：</td>
		<td><input id="custName" type="text" maxlength="40" class="ipt05" value="${customer.custName}"/></td>
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
      <td class="tit">邮箱：</td>
      <td><input name="email" id="email" maxlength="30" class="ipt01" type="text"  value="${customer.email}" /></td>
    </tr>
    <tr>
      <td class="tit"><span class="cRed">*</span>电话：</td>
      <td><input name="tel" id="tel" maxlength="16" class="ipt01" type="text"  value="${customer.tel}" /></td>
 	  <td colspan="5"><span class="cRed">请填写11位手机号码</span></td>
    </tr>
    <tr>
      <td class="tit">通讯地址：</td>
      <td colspan="5"><input class="ipt01" name="address" id="address" type="text" maxlength="100"  value="${customer.address}" /></td>
    </tr>
    <tr>
      <td class="tit">投资顾问</td>
      <td>${customer.saleManName}</td>
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
      <td colspan="5"><div id="prodInfoSel" name="prodInfoSel"></div></td>
    </tr>
    <tr>
      <td class="tit">沟通类型：</td>
      <td colspan="5"><div id="commTypeSel" name="commTypeSel"></div></td>
    </tr>
    <tr>
      <td class="tit">沟通结果：</td>
      <td colspan="5"><textarea id="communicationResult" name="communicationResult" class="area01">${customer.communicationResult}</textarea></td>
    </tr>
    <c:if test="${customer.status=='4' || customer.status=='5'}">
    <tr>
    	<th colspan="6" class="tipBg">证件信息</th>
    </tr>
    <tr>
		<td class="tit">证件类型：</td>
		<td>身份证</td>
		<td class="tit">证件号码： </td>
		<td colspan="3">${customer.identityNum}</td>
	</tr>
    <tr>
      <td class="tit"><span class="cRed">*</span>银行名称：</td>
	  <td><input name="bankName" id="bankName" maxlength="50" class="ipt01" type="text"  value="${customer.bankName}" /></td>
      <td class="tit"><span class="cRed">*</span>开户行：</td>
      <td><input name="bankBranch" id="bankBranch" maxlength="50" class="ipt01" type="text"  value="${customer.bankBranch}" /></td>
      <td class="tit"><span class="cRed">*</span>账号：</td>
      <td><input name="bankAccount" id="bankAccount" maxlength="50" class="ipt01" type="text"  value="${customer.bankAccount}" /></td>
    </tr>
    </c:if>
  </table>
</div>
<div class="btTip">&nbsp;</div>
<div class="addTool2" > 
	  <input type="button" id="save"  class="allBtn" value="保存"/>&nbsp;&nbsp;
	  <c:if test="${customer.status=='0' || customer.status=='2' || customer.id==null}">
	  	 <input type="button" id="commit"  class="allBtn" value="提交回访"/>&nbsp;&nbsp;
	  </c:if>
	  <c:if test="${customer.status=='4'}">
	  	 <input type="button" id="toFomal"  class="allBtn" value="转正式"/>&nbsp;&nbsp;
	  </c:if>
	  <input type="button" id="close" class="allBtn" value="取消"/>
</div>
