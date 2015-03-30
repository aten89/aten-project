<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/systemconf/action/query_action.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<form id="mainForm" name="mainForm" onsubmit="return false;">

<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="add_btn" id="addAction" r-action="add"/>
	  <input type="button" class="flash_btn" id="refreshAction" />
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--搜索-->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
			动作名称：<input name="actionName" id="actionName" type="text" class="ipt05"  style="width:90px" maxlength="50"/>
			动作代码：<input name="actionKey" id="actionKey" type="text" class="ipt05"  style="width:70px" maxlength="20"/>
			<input type="button" id="searchAction" value="搜索" class="allBtn" r-action="query"/>
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
		<th width="20%">动作名称</th>
		<th width="20%">动作代码</th>
		<th width="10%">图标</th>
		<th width="30%">TIPS</th>
		<th width="15%">操作</th>
	  </tr>
  </thead>
  <tbody id="data_list"></tbody>
</table>
</div>
<!--列表 end-->
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
</form>
</body>
</html>