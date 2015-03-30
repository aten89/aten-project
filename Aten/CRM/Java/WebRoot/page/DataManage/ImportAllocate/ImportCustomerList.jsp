<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/DataManage/ImportAllocate/ImportCustomerList.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='IMPORT_ALLOCATE'/>" />
<div class="addTool sortLine">
  <div class="t01">
	<input id="import_customer" type="button" class="import_btn"/>
	<input id="manualAllot" type="button" class="manuAllot_btn"/>
	<input id="autoAllot" type="button" class="autoAllot_btn"/>
	<input id="refresh" type="button" class="flash_btn">
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>

<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
  
	  <tr>
		<td>批次号：</td>
		<td><input id="batchNumber" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>客户名称：</td>
		<td><input id="customerName" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>电话：</td>
		<td><input id="tel" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>邮箱：</td>
		<td><input id="email" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th class="listCBoxW"><input type="checkbox" class="cBox" style="height:13px" id="AllSelectState"/></th>
		<th width="15%">批次号</th>
		<th width="20%">客户名称</th>
		<th width="10%">性别</th>
		<th width="20%">电话</th>
		<th width="25%">邮箱</th>
		<th width="5%">操作</th>
	</tr>
  </thead>
 <tbody id="importCust">
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
</body>
</html>