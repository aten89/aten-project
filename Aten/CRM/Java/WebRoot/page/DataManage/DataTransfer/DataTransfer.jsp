<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/DataManage/DataTransfer/DataTransfer.js"></script>
</head>

<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='DATA_SHIFT'/>" />
<div class="addTool sortLine">
  <div class="t01">
	<input id="dataTransfer" type="button" class="treeFront1_btn" />
	<input id="refresh"  type="button" class="flash_btn"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>

<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
  
	  <tr>
		<td class="tit">投资顾问：</td>
		<td>
			<div style="width: 139px;">
				<div id="customerManageSel" name="customerManageSel"></div>
			</div>
		</td>
		<td>客户状态：</td>
		<td><div id="statusSel" name="statusSel">
               <div isselected="true">**请选择...</div>
               <div>0**未提交</div>
               <div>1**待回访</div>
               <div>2**驳回</div>
               <div>3**潜在</div>
               <div>4**待完善</div>
               <div>5**正式</div>
             </div>
		</td>
		<td>
			&nbsp;&nbsp;<input id="searchInfo" type="button" value="搜索" class="allBtn"/>
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
		<th width="5%" class="listCBoxW"><input type="checkbox" class="cBox" style="height:13px" id="AllSelectState"/></th>
		<th width="20%">客户名称</th>
		<th width="10%">性别</th>
		<th width="20%">电话</th>
		<th width="30%">邮箱</th>
		<th width="15%">客户状态</th>
	</tr>
  </thead>
 <tbody id="custList">
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