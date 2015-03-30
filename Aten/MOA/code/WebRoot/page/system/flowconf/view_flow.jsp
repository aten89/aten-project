<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>工作流设计器</title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<link rel="stylesheet" href="commui/workflow/desinger.css" type="text/css" exclude="true"/>
<script type="text/javascript" src="commui/workflow/desinger.js"></script>
<script type="text/javascript">
var flowType = "${param.flowType}";
var flowKey = "${param.flowKey}";
</script>
<script type="text/javascript" src="page/system/flowconf/view_flow.js"></script>
<style type="text/css">
.workflowArea{margin-right:0px;height:520px}
</style>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='flow_man'/><oa:right key='flow_draft'/>" />

<!-- 工作区 -->
<div id="workspace" style="width:100%;height:100%;"></div>
</body>
</html>
