<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title></title>
	<script type="text/javascript" src="jqueryui/datepicker/ui.datepicker.js"></script>
	<script type="text/javascript" src="page/flowmanage/instance/query_inst.js"></script>
</head>
<body class="bdNone">
<!--搜索-->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
			任务名称：<input id="taskName" type="text" class="ipt05"  style="width:90px" maxlength="50"/>
			创建时间：<input id="startCreateTime" type="text" size="10" class="invokeBoth tw2" />
			<input id="endCreateTime" type="text" size="10" class="invokeBoth tw2" />
			<input type="button" id="searchTask" value="搜索" class="allBtn" r-action="query"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
  <table id="zxt" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
		<th width="5%">序号</th>
		<th width="18%">任务描述</th>
		<th width="18%">任务名称</th>
		<th width="25%">表单ID</th>
		<th width="8%">表单提交者</th>
		<th width="8%">任务指派人</th>
		<th width="13%">任务创建时间</th>
		<th width="5%">操作</th>
	  </tr>
  </thead>
  <tbody id="data_list"></tbody>
</table>
</div>
<!--列表 end-->
<!--翻页-->
<div class="pageNext"></div>
<!--翻页 end-->
</body>
</html>