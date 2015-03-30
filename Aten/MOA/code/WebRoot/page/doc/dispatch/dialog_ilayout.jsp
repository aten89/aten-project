<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript"	src="page/doc/dispatch/dialog_ilayout.js"></script>
<title></title>
</head>
<body class="bdDia">
	<div id="problemDiv"  style="padding:15px 15px 10px; margin:0">
		  <div class="selProblem">请选择发布的信息板块：</div>
	  	  <c:forEach var="layout" items="${infoLayout}">
	  	  	<input name="problemType" type="checkbox" value="${layout.name}" class="cBox02"/><label for="qwwt3">${layout.name}</label><br /><br />
	  	  </c:forEach>
		  <div class="addTool2">
			<input type="button" value="确定" class="allBtn" id="submit"/>
			<input type="button" value="不发布到版块" class="allBtn" id="unsubmit"/>
			<input type="button" value="取消" class="allBtn" id="cancel"/>
		  </div> 
	</div> 
</body>