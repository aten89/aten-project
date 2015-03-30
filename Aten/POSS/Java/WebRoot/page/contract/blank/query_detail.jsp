<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/contract/blank/query_detail.js"></script>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>

<body class="oppBd">
<input type="hidden" id="contractId"  value="${param.contractId}" />
<!--工具栏-->
<div id="divAddTool" class="addTool">
  <div class="t01">
	  <input type="button" class="addBlankCon_btn" id="add" r-action="add"/>
  </div>
</div>
<!--工具栏 end-->

<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="30%">合同数</th>
		<th width="30%">登记人</th>
		<th width="30%">登记时间</th>
		<th width="10%">操作</th>
	</tr>
  </thead>
 <tbody id="data_list">
 </tbody>
</table>
</div>
<!--列表 end-->
</body>
</html>