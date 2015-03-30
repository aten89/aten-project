<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<%
		String path = request.getContextPath();
		String sPort = (request.getServerPort() == 80) ? ""
				: (":" + request.getServerPort());
		String base = request.getScheme() + "://" + request.getServerName()
				+ sPort;
		String basePath = base + path + "/";
	%>
	<!-- base href="" /> 设置后CHROME/FIREFOX 画线无箭头问题 -->
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title></title>
	<!--[if lt IE 9]>
	<?import namespace="v" implementation="#default#VML" ?>
	<![endif]-->
	<link rel="stylesheet" href="../../../jqueryui/gooflow/style/gooflow.css" type="text/css" />
	<link rel="stylesheet" href="../../../themes/${sessionUser.styleThemes }/style.css" type="text/css" />
	<script type="text/javascript" src="../../../jqueryui/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="../../../jqueryui/alerts/jquery.alerts.js"></script>
	<script type="text/javascript" src="../../../jqueryui/dialog/jquery.wbox.js"></script>
	<script type="text/javascript" src="../../../jqueryui/eapp.common.js"></script>
	<script type="text/javascript" src="../../../jqueryui/eapp.frames.js"></script>
	<script type="text/javascript" src="../../../page/base_path.js"></script>
	<script type="text/javascript" src="../../../jqueryui/select/jquery.select.js"></script>
	<script type="text/javascript" src="../../../jqueryui/gooflow/jquery.gooflow.js"></script>
	<script type="text/javascript" src="edit_flow.js"></script>
	<script type="text/javascript" src="attrview/edit_attr.js"></script>
	<script language="javascript">
		var BASE_PATH = "<%=basePath%>";
	</script>
</head>
<body class="bdNone">
	<input id="flowKey" type="hidden" value="${param.flowKey }" />

	<!-- 工具栏 -->
	<div style="height:26px;margin:2px 0 0 0px;">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>流程类别：</td>
				<td><div id="flowCategory"></div></td>
				<td>&nbsp;&nbsp;<input id="startEditFlow" type="button" value="开始" class="allBtn"/></td>
			</tr>
		</table>
	</div>

	<div style="float:left;width:75%;">
		<div id="flowEditDiv"></div><!-- 放在table里布局在FIREFOX下不能画线 -->
	</div>
	<div style="float:left;width:25%;display:none;">
		<div class="mkdzTip" style="height:18px;padding:5px 0 1px 5px;">属性</div>
		<div id="attrEditDiv" class="addCon" style="overflow:auto;">

		</div>
	</div>
</body>
</html>