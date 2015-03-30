<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>请假单明细</title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/hr/holidays/edit_holi.js"></script>
</head>
<body class="bdDia">
<div class="addCon">
  <input type="hidden" id="holidayDays" value="0"/>
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <thead>
		<tr>
			<th colspan="2">请假信息</th>
		</tr>
      </thead>
	  <tr>
	    <td class="tit"><div  style="width:66px;float:right">假期类型</div></td>
	    <td width="85%">
          <div class="vacationVarious" id="holidayOptions">
  	<c:forEach var="ht" items="${holiTypes}">
		 	<li><input class="cBox" name="holidayOption" type="radio" value="${ht.holidayName}" /><label>${ht.holidayName}</label></li>
	</c:forEach>
          </div>
        </td>
      </tr>
	  <tr>
	    <td class="tit">请假期限</td>
	    <td>
	        <div style="float:left">从<input id="beginDate" readonly type="text" class="invokeBoth" style="width:70px"/></div>
	        <div id="beginTime" name="beginTime" style="float:left">
	          <div isselected="true">A**上午</div>
	          <div>P**下午</div>
	        </div>
	        <div style="float:left;">&nbsp;到&nbsp;<input id="endDate" readonly type="text" class="invokeBoth" style="width:70px"/></div>
	        <div id="endTime" name="endTime" style="float:left">
	          <div isselected="true">A**上午</div>
	          <div>P**下午</div>
	        </div>
	        <div id="totalDays" class="dataCount"></div>
        </td>
      </tr>	
	  <tr>
	    <td class="tit">附加说明</td>
	    <td><textarea class="area01" id="remark" style="width:480px"></textarea></td>
      </tr>
      
  </table>
</div>
<div class="addTool2">
	<input id="btnSave" type="button" value="确定" class="allBtn"/>
	<input id="btnCancel" type="button" value="取消" class="allBtn"/>
</div>
</body>
