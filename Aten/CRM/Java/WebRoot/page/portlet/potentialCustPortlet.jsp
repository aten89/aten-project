<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/portlet/potentialCustPortlet.js"></script>
</head>
<body class="bdNone">
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="20%">客户名称</th>
		<th width="10%">性别</th>
		<th width="20%">电话</th>
		<th width="20%">邮箱</th>
		<th width="20%">推荐产品</th>
		<th width="10%">操作</th>
	</tr>
  </thead>
 <tbody id="customerList">
 </tbody>
</table>
</div>
</body>
</html>