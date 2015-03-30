<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/systemconf/portlet/frame_portlet.js"></script>
</head>
<body class="bdNone">
<div id="jkzhMain">
<div class="addTool tabMid2" id="jkzhTool">
	<div class="t01">
		<input type="button" class="add_btn" id="addPortlet" r-action="add" />
		<input type="button" class="edit_btn" id="modifyPortlet" r-action="modify"/>
		<input type="button" class="tjjs_btn"  id="bindRole" r-action="bindrole"/>
	</div>
</div>
</div>
<div id="contetnMain">
</div>
<input type="hidden" id="hidAction" value="${param.action }" />
<input type="hidden" id="hidPortletId" value="${param.portletid }" />
<input type="hidden" id="hidPortletName" value="" />
</body>
</html>