<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<script type="text/javascript" src="page/MyCustomer/CustomerInfo/view_customerinfo.js"></script>
<!--工具栏-->
<div class="addTool" id="divAddTool">
  <div class="t01">
  	<input id="revisit" type="button" class="huifang_btn"/><!-- 回访 -->
    <input id="sendmessage" type="button" class="duanxin2_btn" /><!-- 发送短信 -->
    <input id="request" type="button" class="zixun_btn" /><!-- 新建咨询 -->
    <input id="appoint" type="button" class="yuyue_btn" /><!-- 新建预约 -->
  </div>
</div>
<!--工具栏 end-->

<div class="addCon">
  <input type="hidden" id="customerId"  value="${customer.id}" />
  <input type="hidden" id="tel"  value="${customer.tel}" />
  <input type="hidden" id="custName"  value="${customer.custName}" />
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="6" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit">客户名称：</td>
		<td>${customer.custName}</td>
		<td class="tit">性别： </td>
		<td>${customer.sexName}</td>
		<td class="tit">客户性质：</td>
		<td>${customer.custPropertyName}</td>
	</tr>
    <tr>
      <td class="tit">出生日期：</td>
	  <td><fmt:formatDate value="${customer.birthday}"/></td>
      <td class="tit">年龄：</td>
      <td>${customer.age}</td>
      <td class="tit">电话：</td>
      <td>
      	<span id="telTD">${customer.tel}</span> 
      	<span id="telArea" areacode="${customer.telPartArea.areaCode}" style="color:blue">（${customer.telPartArea.cityName}）</span>
      </td>
    </tr>
    <tr>
      <td class="tit">邮箱：</td>
      <td>${customer.email}</td>
      <td class="tit">通讯地址：</td>
      <td colspan="3">${customer.address}</td>
    </tr>
    <tr> 
      <td class="tit">工号</td>
      <td>${customer.saleMan}</td>      
      <td class="tit">理财金额(万)：</td>
      <td>${customer.financingAmount}</td>
      <td class="tit">期望投资年限：</td>
      <td>${customer.expectedInvestAgelimit}</td>
    </tr>
    <tr>
      <td class="tit">投资顾问</td>
      <td>${customer.saleManName}</td>
      <td colspan="4">${customer.fullDeptName}</td>
    </tr>
    <tr>
      <td class="tit">投资经历：</td>
      <td colspan="5">${customer.investExperience}</td>
    </tr>
    <tr>
      <td class="tit">期望产品：</td>
      <td colspan="5">${customer.expectedProduct}</td>
    </tr>
    <tr>
      <td class="tit">推荐产品：</td>
      <td colspan="5">${customer.recommendProduct}</td>
    </tr>
    <tr>
      <td class="tit">沟通类型：</td>
      <td colspan="5">${customer.communicationTypeName}</td>
    </tr>
    <tr>
      <td class="tit">沟通结果：</td>
      <td colspan="5">${customer.communicationResult}</td>
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
      <td class="tit">银行名称：</td>
	  <td>${customer.bankName}</td>
      <td class="tit">开户行：</td>
      <td>${customer.bankBranch}</td>
      <td class="tit">账号：</td>
      <td>${customer.bankAccount}</td>
    </tr>
    </c:if>
  </table>
  <div class="btTip">&nbsp;</div>
</div>
<div class="addTool2">
  <c:if test="${customer.status=='0' || customer.status=='2' || customer.id==null}">
	  	 <input type="button" id="commit"  class="allBtn" value="提交回访"/>&nbsp;&nbsp;
  </c:if>
  <input id="modify" type='button' class='allBtn' value='修改'/><!-- 修改 -->
  &nbsp;&nbsp;<input type='button' class='allBtn' value='电话呼叫' onclick="top.CRM_CALLCENTER_OBJ.dialout($('#telTD').text(),$('#telArea').attr('areacode'));"/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='挂机' onclick='top.CRM_CALLCENTER_OBJ.handUp();'/>
<!--   &nbsp;&nbsp;<input type='button' class='allBtn' value='示闲' onclick='showIdle();'/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='示忙' onclick='showBusy();'/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='静音' onclick='showMute();'/>
  &nbsp;&nbsp;<input type='button' class='allBtn' value='取消静音' onclick='cancelMute();'/>
 -->
</div>