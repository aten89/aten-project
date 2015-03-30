<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Language" content="en" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<script type="text/javascript" src="page/systemconf/system/frame_sys.js"></script>
	</head>
	<body class="bdNone">
		<input id="hidAction" value="${param.act }" type="hidden" />
		<input id="hidSubSystemId" value="${param.subSystemId }" type="hidden" />
		<div class="addTool tabMid2">
			<div class="t01">
				<input type="button" class="add_btn" id="addSubSystem" disabled="disabled" r-action="add"/>
				<input type="button" class="edit_btn" id="modifySubSystem"  r-action="modify"/>
			</div>
		</div>
		<form name="mainForm" id="mainForm" onsubmit="return false;"></form>
	</body>
</html>