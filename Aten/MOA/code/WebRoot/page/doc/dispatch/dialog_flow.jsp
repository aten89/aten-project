<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
	<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.user.js"></script>
	<script type="text/javascript" src="${ermpPath}/jqueryui/datepicker/ui.datepicker.js"></script>
	<script type="text/javascript"	src="page/doc/dispatch/dialog_flow.js"></script>
<title></title>
</head>
<body class="bdDia">
<input type="hidden" id="docClassName" value="${docClassName}">
	<div id="problemDiv"  style="padding:15px 15px 10px; margin:0">
		  <div class="selProblem">请选择公文流程：</div>
	  	  <!-- <input name="problemType" type="radio" value="DEFAULT_VALUE_ATONCE" checked id="qwwt1" class="cBox02"/><label for="qwwt1">直接发布</label><br /><br />
	  	  <c:forEach var="flow" items="${requestScope.flows}">
	  	  	<input name="problemType" type="radio" value="${flow.flowKey}" class="cBox02" onclick="changeRadio(this)" /><label for="qwwt3">${flow.flowName}</label><br /><br />
	  	  </c:forEach>
	  	  <c:if test="${noFlowClass}">
	  	  	<input name="problemType" type="radio" value="DEFAULT_VALUE_ATONCE" class="cBox02"/><label for="qwwt3">直接发布</label><br /><br />
	  	  </c:if>
	  	   -->
	  	  <div id="selectFlows"></div>
	  	  <div id="assignPanel" class="selProblem" style="line-height:30px;display:none">指定接收人<span id="assignText"></span>：<input type="button" id="openAppointToSelect" class="selBtn"/>
	  	  	<input name="appointTo" id="appointTo" type="hidden" value="" />
			<textarea name="appointToName" id="appointToName" readonly style="margin:0 0 0 15px;width:260px;height:50px" class="ipt01" value=""/></textarea>&nbsp;
		  </div>
		  
		  <!-- modify zhubiyong 2010-7-26 -->
		  <div id="executionTime" style="color:red;font-weight:normal;padding:10px 0 0 0;line-height:1.6;display:none">办理时限（可选）：<input id="endTime" readonly type="text" class="invokeBoth"  style="width:65px"/> <br>注：如果选择了“办理时限”，则逾期后系统将默认以“同意”方式自动办理！</div>
		  <div class="addTool2">
			<input type="button" value="确定" class="allBtn" id="submit"/>
			<input type="button" value="取消" class="allBtn" id="cancel"/>
		  </div> 
	</div> 
</body>