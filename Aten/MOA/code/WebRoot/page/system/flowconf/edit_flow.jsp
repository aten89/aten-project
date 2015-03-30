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
var action = "${param.act}";
var flowKey = "${param.flowKey}";
var gFlowCategory = "${flowCategory}"; //修改时的流程类别
</script>
<script type="text/javascript" src="page/system/flowconf/edit_flow.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='flow_man'/><oa:right key='flow_draft'/>" />
<!-- 工具栏 -->
<div style="height:26px;padding:5px 0">
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
		    <!--下拉框-->
			<div id="flowCategory" type="single" overflow="true" name="flowClass">
			</div>
			<!--下拉框 end-->
		</td>
		<td>&nbsp;<input id="btnCreateFlow" type="button" value="创建流程" class="allBtn"/></td>
		<td>&nbsp;<input id="btnSave" type="button" value="保存" class="allBtn"/></td>
		<td>&nbsp;<input id="btnUpdateMeta" type="button" value="更新环境变量" class="allBtn"/></td>
	<!--	<td>&nbsp;<input id="btnEncode" type="button" value="生成汉字编码" class="allBtn"/></td>-->
	  </tr>
	</table>
</div>
<!-- 工具栏 end -->
<!-- 工作区 -->
<div id="workspace"></div>
</body>
</html>
