<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">

<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/operationsManage/paymentRegister/paymentQueryList.js"></script>

<input id="standardFlag" type="hidden" value="${ param.standardFlag }" />

<c:if test="${ !param.standardFlag }">
	<input id="hidModuleRights" type="hidden" value="<eapp:right key='CUST_PAY_ARCH'/>" />
</c:if>
<c:if test="${ param.standardFlag }">
		<input id="hidModuleRights" type="hidden" value="<eapp:right key='FBYY_ARCH'/>" />
</c:if>

<div class="soso soRow2">
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>划款时间：</td>
							<td colspan="3"  width="300px">
							从<input readonly id="bgnTransferDateTime" type="text" class="invokeBoth soTimeW" value=""/>
							 到<input readonly  id="endTransferDateTime" type="text" class="invokeBoth soTimeW" value=""/>
							</td>
				<td>客户名称：</td>
				<td >
							<input type="text" id="custName"  class="ipt05"/>
							<input type="hidden" id="custId"/>
							<input type="hidden" id="oriSelected"/>
						</td>
				<td>投资顾问：</td>
				<td >
					<input id="saleManId" type="hidden"/>
		    		<input id="saleManName" readonly type="text" class="ipt01" maxlength="30" style="width:65px"/>
		    		<input type="button" id="openUserSelect" class="selBtn"/>&nbsp;&nbsp;
				</td>
				<td>状态：</td>
	    		<td><div id="formStatusDiv" name="formStatus" style="display:none"><div isselected="true">1**审批中</div><div>2**审批通过</div><div>3**作废</div></div>&nbsp;</td>
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
				
				<td>
					&nbsp;<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
				</td>
			  </tr>
		</table>
	</div>
</div>

<!--列表-->
<div class="allList">
<table id="waitVisitCustomerTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="10%">投资顾问</th>
		<c:if test="${ param.standardFlag }">
		<th width="12%">预计划款时间</th>
		</c:if>
		<c:if test="${ !param.standardFlag  }">
		<th width="12%">划款时间</th>
		</c:if>
		<th width="9%">客户名称</th>
		<th width="8%">客户性质</th>
		<th width="18%">产品项目</th>
		<th width="10%">预约金额(万)</th>
		<th width="10%">划款类型</th>
		<th width="8%">状态</th>
		<th width="5%">操作</th>
	</tr>
  </thead>
 <tbody id="paymentArchList">
 </tbody>
</table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->