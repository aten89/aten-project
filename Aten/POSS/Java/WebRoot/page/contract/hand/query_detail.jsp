<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/contract/hand/query_detail.js"></script>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>

<body class="oppBd">
<input id="r_userActionKeys1" type="hidden" value="<eapp:right key='hand_reg'/>" />
<input type="hidden" id="prodId"  value="${param.prodId}" />
<input type="hidden" id="orgName"  value="${orgName}" />
<!--工具栏-->
<div id="divAddTool" class="addTool">
  <div class="t01 t_f01">
	  <input type="button" class="addReqCon_btn" id="add" r-action="add"/>
  </div>
</div>
<!--工具栏 end-->

<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="6%">审核状态</th>
		<th width="6%">登记人</th>
		<th width="8%">登记时间</th>
		<th width="4%">上交数</th>
		<th width="4%">签署数</th>
		<th width="4%">空白数</th>
		<th width="4%">签废数</th>
		<th width="5%">不通过数</th>
		<th width="9%">快递公司名称</th>
		<th width="9%">上交快递单号</th>
		<th width="8%">上交时间</th>
		<th width="12%">上交备注</th>
		<th width="12%">审核备注</th>
		<th width="9%">操作</th>
	</tr>
  </thead>
 <tbody id="data_list">
 </tbody>
</table>
</div>
<!--列表 end-->
</body>
</html>