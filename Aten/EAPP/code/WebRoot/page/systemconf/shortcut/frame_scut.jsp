<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/systemconf/shortcut/frame_scut.js"></script>
</head>
<body class="bdNone">
<input id="hidAction" value="${param.action}" type="hidden" />
<input id="hidshortCutID" value="${param.shortCutMenuID}" type="hidden" />
<div class="addTool tabMid2">
	<div class="t01">
		<input type="button" class="add_btn" id="opAdd" r-action="add"/>
		<input type="button" class="edit_btn" id="opModify" r-action="modify"/>
	</div>
</div>
<form name="mainForm" id="mainForm" onsubmit="return false;"></form>
</body>
</html>