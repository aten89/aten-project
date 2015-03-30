<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="commui/jquery.corner.js"></script>
<script type="text/javascript" src="page/messagemanage/choose_customer.js"></script>
</head>
<body>
<div class="soso">
  <div class="t01">
  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>客户名称：</td>
		<td><input id="customerName" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>电话：</td>
		<td><input id="tel" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
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
		<th width="5% class="listCBoxW"><input type="checkbox" class="cBox" style="height:13px" id="AllSelectState"/></th>
		<th width="30%">客户名称</th>
		<th width="20%">电话</th>
		<th width="45%">推荐产品</th>
	</tr>
  </thead>
 <tbody id="chooseCust">
 </tbody>
</table>
</div>
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
<div class="addTool2">
  <input type="button" id="save"  class="allBtn" value="保存"/>&nbsp;&nbsp;
  <input type="button" id="close" class="allBtn" value="关闭"/>
</div>
</body>
</html>