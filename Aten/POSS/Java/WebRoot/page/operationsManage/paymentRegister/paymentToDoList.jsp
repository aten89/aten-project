<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">

<script type="text/javascript"
	src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript"
	src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript"
	src="page/operationsManage/paymentRegister/paymentToDoList.js"></script>

<input id="userAccountID" type="hidden" value="" />
<input id="userDisplayName" type="hidden" value="" />
<input id="standardFlag" type="hidden" value="${ param.standardFlag }" />

<!-- 工具栏 -->
<div class="addTool">
	<c:if test="${ !param.standardFlag }">
		<input id="hidModuleRights" type="hidden"
			value="<eapp:right key='CUST_PAY_TODO'/>" />
		<div class="t01 t_f01">
			<input type="button" class="add1_btn"  id="addOldCustomerPay" /> 
			<input type="button" class="add2_btn" id="addNewCustomerPay" />
		</div>
	</c:if>
	<c:if test="${ param.standardFlag }">
		<input id="hidModuleRights" type="hidden"
			value="<eapp:right key='FBYY_TODO'/>" />
		<div class="t01 t_f01">
			<input type="button" class="add3_btn" id="addFBYYCustomerPay" />
		</div>
	</c:if>
</div>

<div class="blank" style="height: 3px; background: #fff"></div>

<div class="soso soRow2" >
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>划款时间：</td>
				<td colspan="3" width="300px">从<input readonly
					id="bgnTransferDateTime" type="text" class="invokeBoth soTimeW"
					value="" /> 到<input readonly id="endTransferDateTime" type="text"
					class="invokeBoth soTimeW" value="" />
				</td>
				<td>客户名称：</td>
				<!--  	<td>
					<div style="width: 139px;">
						<div id="customerInfoSel" name="customerInfoSel"></div>
					</div>
				</td>-->


				<td><input type="text" id="custName" class="ipt05" />
					<input type="hidden" id="custId" /> <input type="hidden"
					id="oriSelected" /></td>

			</tr>
			<tr style="height:26px">

				<td>产品项目：</td>
				<td colspan="3">
					<div id="prodInfoSel" name="prodInfoSel"></div>
				</td>


				<td>划款类型：</td>
				<td>
					<div id="payTypeSel">
						<div isselected="true">**请选择...</div>
						<div>1**老客户划款</div>
						<div>0**新客户划款</div>
					</div>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>

				<td>&nbsp;<input id="searchInfo" type="button" value="搜索"
					class="allBtn" />&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>
</div>

<!--列表-->
<div class="allList">
	<table id="waitVisitCustomerTab" width="100%" border="0"
		cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<th width="10%">投资顾问</th>
				<c:if test="${ param.standardFlag }">
					<th width="12%">预计划款时间</th>
				</c:if>
				<c:if test="${ !param.standardFlag }">
					<th width="12%">划款时间</th>
				</c:if>
				<th width="10%">客户名称</th>
				<th width="10%">客户性质</th>
				<th width="20%">产品项目</th>
				<th width="10%">预约金额(万)</th>
				<th width="10%">划款类型</th>
				<th width="10%">所属步骤</th>
				<th width="8%">操作</th>
			</tr>
		</thead>
		<tbody id="paymentToDoList">
		</tbody>
	</table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1" />
<input type="hidden" id="hidPageCount" value="15" />
<div class="pageNext"></div>
<!--翻页 end-->
<!--列表 end-->