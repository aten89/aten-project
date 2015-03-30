<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
	<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
	<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.dept.js"></script>
	<script type="text/javascript" src="${ermpPath}/jqueryui/tree/jquery.simple.tree.js"></script>
	<script type="text/javascript" src="page/device/paramconf/assign/frame_assign.js"></script>
</head>
<body class="bdNone">
<!--  <input id="hidModuleRights" type="hidden" value="<oa:right key=''/>" />-->
<input id="id" type="hidden" value="${param.id }"/>
<input id="flag" type="hidden" value="${param.type }"/>
<input id="title" type="hidden" value="${param.type=='0'?'授权管理':'查询管理'}"/>
<!--工具栏-->
	<div class="addTool tabMid2">
		<div class="t01">
		<input type="button" class="tjry_btn" id="ImpowerUser"/>
		<input type="button" class="tjzw_btn" id="ImpowerPost"/>
		<input type="button" class="bdjg_btn" id="ImpowerGroup"/>
	  </div>
	</div>
	<!--工具栏 end-->
	<div id="contetnMain"></div>
	<div class="addTool2" >
	  <input id="closeBnt" type="button" value="关闭" class="allBtn"/>
    </div>
</body>
</html>