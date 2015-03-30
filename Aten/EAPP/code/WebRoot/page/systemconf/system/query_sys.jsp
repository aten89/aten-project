<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/systemconf/system/query_sys.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool">
  <div class="t01">
	  <input type="button" class="add_btn" id="addSubSystem" r-action="add" />
	  <input type="button" class="sort_btn" id="opSort" r-action="order" />
	  <input type="button" class="flash_btn" id="refreshSubSystem" />
  </div>
</div>
<!--工具栏 end-->
<!--列表-->
<div class="allList">
  <table id="zxt" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
		<th width="5%">序号</th>
		<th width="17%">系统名称</th>
		<th width="28%">系统编号</th>
		<th width="5%">图标</th>
		<th width="14%">域名</th>
		<th width="5%">端口</th>
		<th width="6%">服务名</th>
		<th width="5%">开放</th>
		<th width="15%">操作</th>
	  </tr>
  </thead>
<tbody id="data_list"></tbody>
</table>
</div>
<!--列表 end-->
</body>
</html>