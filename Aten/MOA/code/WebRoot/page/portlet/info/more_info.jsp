<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/portlet/info/more_info.js"></script>
</head>
<body class="bdNone">
<input type="hidden" value="${layoutName}" id="layoutName"/>
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>标题：<input id="subject" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td><input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;
		<input id="refresh" type="button" class="flash_btn"/></td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="4%">序号</th>
		<th width="60%">标题</th>
		<th width="15%">发布时间</th>
		<th width="15%">发布机构</th>
		<th width="6%">操作</th>
	</tr>
  </thead>
  <tbody>
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