<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/flowmanage/draft/query_flow.js"></script>
</head>
<body class="bdNone">
	<!--工具栏-->
	<div class="addTool sortLine">
	  <div class="t01">
		  <input type="button" class="add_btn" id="addFlow" r-action="add"/>
		  <input type="button" class="flash_btn" id="refreshFlow" />
	  </div>
	</div>
	<!--工具栏 end-->
	<div class="blank3"></div>
	<!--搜索-->
	<div class="soso">
	  <div class="t01">
	  <table border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td>流程类别：</td>
			<td><div id="flowClassDiv"></div></td>
			<td>&nbsp; 流程名称：<input id="flowName" type="text" class="ipt05"  style="width:120px"/>
				<input type="button" id="searchFlow" value="搜索" class="allBtn" r-action="query"/>
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
			<th width="17%">流程类别</th>
			<th width="17%">流程名称</th>
			<th width="31%">流程标识</th>
			<th width="12%">版本号</th>
			<th width="18%">操作</th>
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
</body>
</html>