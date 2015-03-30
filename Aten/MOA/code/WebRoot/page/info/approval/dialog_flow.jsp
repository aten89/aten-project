<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<script type="text/javascript"	src="page/info/approval/dialog_flow.js"></script>
<title></title>
</head>
<body class="bdDia">
	<div id="problemDiv"  style="padding:15px 15px 10px; margin:0">
		  <div class="selProblem">请选择信息流程：</div>
	  	  <!-- <input name="problemType" type="radio" value="DEFAULT_VALUE_ATONCE" checked id="qwwt1" class="cBox02"/><label for="qwwt1">直接发布</label><br /><br /> -->
	  	  <c:forEach var="flow" items="${requestScope.flows}">
	  	  	<input name="problemType" type="radio" value="${flow.flowKey}" class="cBox02"/><label for="qwwt3">${flow.flowName}</label><br /><br />
	  	  </c:forEach>
	  	  <c:if test="${noFlowClass}">
	  	  	<input name="problemType" type="radio" value="DEFAULT_VALUE_ATONCE" class="cBox02"/><label for="qwwt3">直接发布</label><br /><br />
	  	  </c:if>
		  <div class="addTool2">
			<input type="button" value="确定" class="allBtn" id="submit"/>
			<input type="button" value="取消" class="allBtn" id="cancel"/>
		  </div> 
	</div> 
</body>