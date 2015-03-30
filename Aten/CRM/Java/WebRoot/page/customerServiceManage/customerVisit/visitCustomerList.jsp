<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<script type="text/javascript" src="page/customerServiceManage/customerVisit/visitCustomerList.js"></script>

<input id="hidModuleRights" type="hidden" value="<eapp:right key='V_FORMAL_CUST'/>" />
<div class="soso">
	<div class="t01">
		<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>客户名称：</td>
		<td><input id="customerName" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>电话：</td>
		<td><input id="tel" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>邮箱：</td>
		<td><input id="email" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>推荐产品：</td>
		<td><input id="recommendPro" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
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
		<th width="15%">客户名称</th>
		<th width="15%">性别</th>
		<th width="20%">电话</th>
		<th width="20%">邮箱</th>
		<th width="20%">推荐产品</th>
		<th width="10%">操作</th>
	</tr>
  </thead>
 <tbody id="waitVisitCustomerList">
 </tbody>
</table>
</div>
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->