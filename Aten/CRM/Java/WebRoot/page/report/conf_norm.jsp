<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>	
		<script type="text/javascript" src="page/report/conf_norm.js"></script>	
	</head>
	<body class="bdNone">
	  	<input id="rptId" type="hidden" value="${param.rptID }"/>
		
		<div class="tabMid">
			<div class="addCon">
		  		<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
					<tr><th colspan="6">报表指标配置</th></tr>
		<c:forEach items="${normConfs}" var="normConf">
					<tr>
						<td class="tit" style="width:35%">${normConf.normName}</td>
						<td><input id="${normConf.normCode}" type="text" value="${normConf.normValue}" class="ipt01" style="width:70%" /></td>
					</tr>
		</c:forEach>
				</table>
			</div>
		</div>
		
		<div class="addTool2">
			<input type="button" value="保存" id="saveConf" class="allBtn" />
		</div>
	</body>
</html>