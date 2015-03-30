<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Language" content="en" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>流程数据库管理</title>
		<script type="text/javascript" src="jqueryui/datepicker/ui.datepicker.js"></script>
		<script type="text/javascript" src="page/flowmanage/histdata/del_data.js"></script>
	</head>
	<body class="bdNone">
		<div class="blank" style="height: 60px"></div>
		<div class="cxbsBg">
			流程实例数据清除主要包括内容：<span style="color: red;">（可能要花很长时间，请勿重复操作）</span><br/>
			(1) 流程实例数据，包括流程变量<br/>
			(2) 任务实例数据，包括任务授权信息<br/>
			(3) 流程日志数据<br/>
		</div>

		<div class="cxbsBk">
			截止时间：
			<input id="startDate" readonly="readonly" type="text" class="invokeBoth tw2"/>
			<input style="margin-left: 10px;" id="delData" type="button" value="删除" class="allBtn"  r-action="delete"/>
		</div>
	</body>
</html>