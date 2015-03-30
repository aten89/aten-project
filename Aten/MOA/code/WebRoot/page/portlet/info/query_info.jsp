<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Language" content="en" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link href="commui/smallslider/style/smallslider.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="commui/smallslider/jquery.smallslider.js"></script>
	<script type="text/javascript" src="page/portlet/info/query_info.js"></script>
  </head>
  <body class="bdNone" style="padding:0;min-width:250px">
  	<input type="hidden" value="${layoutName}" id="layoutName"/>
<c:if test="${param.leftImg == '1'}">
  	<div id="flashbox" class="smallslider" style="float:left;padding:2px 15px 2px 2px; width:350px; height:175px">
		<ul>
			<li style="cursor:pointer" onclick="window.open(BASE_PATH + 'page/portlet/info/images/001.jpg');"><img src="page/portlet/info/images/001.jpg" width="350" height="175" alt="标题1" /></li>
			<li style="cursor:pointer" onclick="window.open(BASE_PATH + 'page/portlet/info/images/002.jpg');"><img src="page/portlet/info/images/002.jpg" width="350" height="175" alt="标题2" /></li>
			<li style="cursor:pointer" onclick="window.open(BASE_PATH + 'page/portlet/info/images/003.jpg');"><img src="page/portlet/info/images/003.jpg" width="350" height="175" alt="标题3" /></li>
			<li style="cursor:pointer" onclick="window.open(BASE_PATH + 'page/portlet/info/images/004.jpg');"><img src="page/portlet/info/images/004.jpg" width="350" height="175" alt="标题4" /></li>
		</ul>
  	</div>
</c:if>
  	<div class="divPanelSub" style="clear:none">
		<ul id="infokList"></ul>
	</div>
  </body>
</html>