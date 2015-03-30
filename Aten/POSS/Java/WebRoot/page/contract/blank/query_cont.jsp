<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/contract/blank/query_cont.js"></script>
<style type="text/css">
	.listData a:link{color:blue}
	.listData a:visited {color:#6b6b6b}
	.listData a:hover {color:red}
</style>
</head>

<body class="bdNone">
<!--工具栏-->
<div id="divAddTool" class="addTool">
  <div class="t01">
	  <input type="button" class="addBlankCon_btn"id="add" r-action="add"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>产品项目：</td>
		<td><input id="prodName" type="text" maxlength="40" class="ipt01" style="width:200px" />&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn" r-action="query"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="46%">产品项目</th>
		<th width="8%">登记次数</th>
		<th width="8%">合同总数</th>
		<th width="8%">剩余合同数</th>
		<th width="8%">最迟上交天数</th>
		<th width="8%">是否盖章返还</th>
		<th width="14%">操作</th>
	</tr>
  </thead>
 <tbody id="data_list">
 </tbody>
</table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="10"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>