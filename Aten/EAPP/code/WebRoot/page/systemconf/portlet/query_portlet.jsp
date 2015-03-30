<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/systemconf/portlet/query_portlet.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="add_btn" id="addPortlet" r-action="add"/>
	  <input type="button" class="mrmf_btn" id="defaultPortlet" r-action="setdefault"/>
	  <input type="button" class="flash_btn" id="refreshPortlet"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<form id="mainForm" onsubmit="return false;">
<!--搜索-->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
			板块名称：<input name="portletname" id="portletname" type="text" class="ipt05"  style="width:100px" maxlength="50"/>
			<input type="button" id="searchPortlet" value="搜索" class="allBtn"  r-action="query"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--搜索 end-->
</form>
<!--列表-->
<div class="allList">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
		<th width="5%">序号</th>
		<th width="20%">板块名称</th>
		<th width="20%">所属系统</th>
		<th width="40%">板块样式</th>
		<th width="15%">操作</th>
	  </tr>
  </thead>
  <tbody id="data_list"></tbody>
</table>
</div>
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>