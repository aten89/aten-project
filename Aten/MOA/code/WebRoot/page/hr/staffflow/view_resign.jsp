<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/staffflow/view_resign.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <thead>
		<tr>
			<th colspan="6">离职审批单</th>
		</tr>
      </thead>
	  <tr> 	
	 	<td class="tit">申请单号</td>
        <td>${staffFlowApply.id}</td>
        <td class="tit">申 请 人</td>
        <td>${staffFlowApply.applicantName}</td>
        <td class="tit"><span class="cRed" valid="userName">*</span>离职员工</td>
        <td>${staffFlowApply.userName}</td>	
	  </tr>
	  <tr>
      	<td class="tit">项 目</td>
        <td>${staffFlowApply.project}</td>
        <td class="tit">培训开始时间</td>
        <td><fmt:formatDate value="${staffFlowApply.tranStartDate}" pattern="yyyy-MM-dd"/></td>
      	<td class="tit">培训结束时间</td>
        <td><fmt:formatDate value="${staffFlowApply.tranEndDate}" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
      	<td class="tit">培训费用</td>
        <td>${staffFlowApply.tranCost}</td>
        <td class="tit">违约金</td>
        <td>${staffFlowApply.penalty}</td>
      	<td class="tit"><span class="cRed" valid="staffClass">*</span>员工分类</td>
        <td>${staffFlowApply.staffClass}</td>
      </tr>
      <tr>
      	<td class="tit">人员隶属</td>
        <td>${staffFlowApply.companyAreaName}</td>
        <td class="tit"><span class="cRed" valid="groupName">*</span>所属部门</td>
       	<td colspan="3">${staffFlowApply.groupName} &nbsp;<span style="color:#9E9E9E">${staffFlowApply.groupFullName}</span></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed" valid="post">*</span>员工岗位</td>
        <td>${staffFlowApply.post}</td>
      	<td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
        <td><fmt:formatDate value="${staffFlowApply.entryDate}" pattern="yyyy-MM-dd"/></td>
        <td class="tit"><span class="cRed" valid="achievement">*</span>入职累计业绩</td>
        <td>${staffFlowApply.achievement}</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="resignDate">*</span>申请离职日期</td>
        <td><fmt:formatDate value="${staffFlowApply.resignDate}" pattern="yyyy-MM-dd"/></td>
    	<td class="tit"><span class="cRed" valid="resignType">*</span>离职类型</td>
        <td>${staffFlowApply.resignType}</td>
    	<td class="tit"><span class="cRed" valid="resignReason">*</span>离职原因</td>
        <td>${staffFlowApply.resignReason}</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="resignDesc">*</span>离职原因描述</td>
        <td colspan="5"><textarea readonly id="resignDesc" class="area01">${staffFlowApply.resignDesc}</textarea></td>
      </tr>
  </table>
  <div class="blank"></div>
</div>
<!--请假单 end-->
<div class="blank"></div>
<div class="costsLog" id="costsLog">
  <h1 class="logArrow"><a  href="javascript:void(0)">各级审批意见显示</a></h1>
  <div class="mb">
<c:forEach var="task" items="${tasks}" >
	<div class="processBk">
	  <div class="clgcTit">
		<ul class="clearfix">
		  <li><b>处理人：</b>${task.transactorDisplayName }</li>
		  <li><b>收到时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
		  <li><b>完成时间：</b><fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm"/></li>
		  <li><b>处理状态：</b>
		<c:if test="${task.taskState=='ps_create'}">
			<img src="themes/comm/images/stateIcoNone.gif">[未查看]
		</c:if>
		<c:if test="${task.taskState=='ps_start'}">
			<img src="themes/comm/images/stateIco02.gif">[已查看,未处理]
		</c:if>
		<c:if test="${task.taskState=='ps_end'}">
			<img src="themes/comm/images/stateIco01.gif">[已处理]
		</c:if>
		  </li>
		</ul>
	  </div>
	  <div class="taskCon nonebb">
		<div class="handleCon"> ${task.comment } </div>
	  </div>
	</div>
</c:forEach>
  </div>
</div>
<div class="blank"></div>
</body>
