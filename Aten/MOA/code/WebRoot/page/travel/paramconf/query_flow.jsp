<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="page/travel/paramconf/query_flow.js"></script>
<title></title>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='trip_param'/>"/>
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01">
		<input type="button" class="add_btn" id="addInfoFlow"/>
		<input type="button" class="flash_btn" id="reflash"/>
	</div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--列表-->
<div class="allList">
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="flowSet">
  <thead>
	<tr>
		<th width="24%">部门名称</th>
		<th width="26%">流程名称</th>
		<th width="40%">说明</th>
		<th width="10%">操作</th>
	</tr>
  </thead>
  <tbody>
  </tbody>
</table>
</div>
<!--列表 end-->
</body>
</html>