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
<link rel="stylesheet" href="themes/${sessionUser.styleThemes }/style.css" type="text/css" />

<script type="text/javascript" src="jqueryui/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="jqueryui/jquery.cookie.js"></script>
<script type="text/javascript" src="jqueryui/alerts/jquery.alerts.js"></script>
<script type="text/javascript" src="jqueryui/dialog/jquery.wbox.js"></script>
<script type="text/javascript" src="jqueryui/eapp.common.js"></script>
<script type="text/javascript" src="jqueryui/eapp.frames.js"></script>
<script type="text/javascript" src="page/base_path.js"></script>

<script language="javascript">
	var BASE_PATH = "<%=basePath%>";
</script>

<input id="r_userActionKeys" type="hidden" value="<eapp:right/>" />