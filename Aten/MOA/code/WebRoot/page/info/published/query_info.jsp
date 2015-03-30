<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/info/published/query_info.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='info_man'/>" />
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>标题：<input id="title" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>版块：</td><td><div id="infoLayoutSel" type="single" overflow="true" name="infoLayout"></div>&nbsp;&nbsp;</td>
		<!--  td>类别：</td><td><div id="typeSel" type="single" overflow="true" name="type"></div>&nbsp;&nbsp;</td>-->
		<td>&nbsp;状态：</td><td><div id="stateSel" type="single" overflow="true" name="state"><div>**请选择...</div><div>0**置顶</div><div>1**普通</div><div>2**屏蔽</div></div>&nbsp;&nbsp;</td>
		<td>&nbsp;<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;
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
		<th width="40%">标题</th>
		<th width="14%">发布时间</th>
		<th width="12%">发布机构</th>
		<th width="8%">发布人</th>
		<th width="5%">状态</th>
		<th width="17%">操作</th>
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
