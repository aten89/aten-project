<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%
	String path = request.getContextPath();
	String sPort = (request.getServerPort() == 80) ? ""
			: (":" + request.getServerPort());
	String base = request.getScheme() + "://" + request.getServerName()
			+ sPort;
	String basePath = base + path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet" href="themes/comm/Calendar.css" type="text/css"/>
<link rel="stylesheet" href="themes/comm/oaComm.css" type="text/css"/>
<link rel="stylesheet" href="${ermpPath}themes/${sessionUser.styleThemes }/style.css" type="text/css"/>
<link rel="stylesheet" href="themes/${sessionUser.styleThemes }/oa.css" type="text/css"/>

<script type="text/javascript" src="commui/jquery-1.2.6.min.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/jquery.cookie.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/alerts/jquery.alerts.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/dialog/jquery.wbox.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/eapp.common.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/eapp.frames.js"></script>
<script type="text/javascript" src="page/base_path.js"></script>
<script language="javascript">
	var BASE_PATH = "<%=basePath%>";
	var ERMP_PATH = "${ermpPath}";
	var POSS_PATH = "${ermpPath}poss/";
</script>
<iframe src="${ermpPath}poss/page/" width=0 height=0 style="display:none">让POSS系统单点登录</iframe>
<input id="r_userActionKeys" type="hidden" value="<eapp:right/>" />
<input id="r_userModuleKeys" type="hidden" value="<eapp:menu/>" />