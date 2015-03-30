<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/hr/salarybill/my_salary.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='my_salary'/>"/>
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>年：</td>
		<td><div id="yearDIV" name="year" style="display:none"></div>&nbsp;</td>
		<td>月：</td>
		<td><div id="montyDIV" name="month" style="display:none">
			<div>01**01月</div>
			<div>02**02月</div>
			<div>03**03月</div>
			<div>04**04月</div>
			<div>05**05月</div>
			<div>06**06月</div>
			<div>07**07月</div>
			<div>08**08月</div>
			<div>09**09月</div>
			<div>10**10月</div>
			<div>11**11月</div>
			<div>12**12月</div>
		</div>&nbsp;</td>
		<td><input id="querySalary" type="button" value="搜索" class="allBtn"/>&nbsp;
		<input id="refresh" type="button" class="flash_btn"/></td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="4%">序号</th>
		<th width="7%">月份</th>
		<th width="8%">帐号名</th>
		<th width="8%">姓名</th>
		<th width="8%">工号</th>
		<th width="10%">职级名称</th>
		<th width="10%">工资总额</th>
		<th width="10%">基本工资</th>
		<th width="10%">职级工资</th>
		<th width="10%">考勤工资</th>
		<th width="10%">实发工资</th>
		<th width="5%">操作</th>
	</tr>
  </thead>
  <tbody id="dataList">
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