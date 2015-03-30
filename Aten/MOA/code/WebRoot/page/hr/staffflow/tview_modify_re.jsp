<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/hr/staffflow/tview_modify_re.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<input id="employeeNumber" type="hidden" value="${staffFlowApply.employeeNumber}"/>
<input id="userAccountID" type="hidden" value="${staffFlowApply.userAccountID}"/>
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
        <td><input type="hidden" name="id" id="staffid" value="${staffFlowApply.id}"/>${staffFlowApply.id}</td>
        <td class="tit">申 请 人</td>
        <td id="applicantName">${staffFlowApply.applicantName}</td>
        <td class="tit"><span class="cRed" valid="userName">*</span>离职员工</td>
        <td>
          <div style="float:left">
			<input id="userName" disabled maxlength="40" type="text" style="width:120px" class="ipt01" value="${staffFlowApply.userName}"/>
		<!-- 	<input type="button" id="openUserSelect" class="selBtn"/> -->
		  </div>
        </td>
      </tr>
      <tr>
      	<td class="tit">项 目</td>
        <td>
          <input id="project" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.project}"/>
        </td>
        <td class="tit">培训开始时间</td>
        <td>
          <input id="tranStartDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${staffFlowApply.tranStartDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      	<td class="tit">培训结束时间</td>
        <td>
          <input id="tranEndDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${staffFlowApply.tranEndDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      </tr>
      <tr>
      	<td class="tit">培训费用</td>
        <td>
          <input id="tranCost" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.tranCost}"/>
        </td>
        <td class="tit">违约金</td>
        <td>
          <input id="penalty" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.penalty}"/>
    	</td>
      	<td class="tit"><span class="cRed" valid="staffClass">*</span>员工分类</td>
        <td>
          <input id="hidStaffClass" type="hidden" value="${staffFlowApply.staffClass}"/>
          <select id="staffClass" class="sel02">
             <option value="职能部门员工">职能部门员工</option>
             <option value="销售部门员工">销售部门员工</option>
           </select>
    	</td>
      </tr>
      <tr>
      	<td class="tit">人员隶属</td>
        <td>
          <select id="companyArea" name="regional" disabled class="sel02" style="width:110px">
			<c:forEach var="d" items="${areas}" >
		        <option value="${d.dictCode }" ${d.dictCode eq staffFlowApply.companyArea ? 'selected':'' }>${d.dictName }</option>
			</c:forEach>
   		  </select>
        </td>
        <td class="tit"><span class="cRed" valid="groupName">*</span>所属部门</td>
        <td colspan="3">
          <div style="float:left">
			<input id="groupName" disabled maxlength="40" type="text" style="width:70%" class="ipt01" value="${staffFlowApply.groupName}"/>
		<!-- 	<input type="button" id="openDeptSelect" class="selBtn"/> -->
		  </div>
		  <div id="groupFullName" style="color:#9E9E9E">${staffFlowApply.groupFullName}</div>
    	</td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed" valid="post">*</span>员工岗位</td>
        <td>
          <input id="post" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.post}"/>
    	</td>
      	<td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
        <td>
          <input id="entryDate" type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${staffFlowApply.entryDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
        <td class="tit"><span class="cRed" valid="achievement">*</span>入职累计业绩</td>
        <td>
          <input id="achievement" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.achievement}"/>
        </td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="resignDate">*</span>申请离职日期</td>
        <td>
          <input id="resignDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${staffFlowApply.resignDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
    	<td class="tit"><span class="cRed" valid="resignType">*</span>离职类型</td>
        <td>
          <input id="hidResignType" type="hidden" value="${staffFlowApply.resignType}"/>
          <select id="resignType" class="sel02">
             <option value="主动辞职">主动辞职</option>
             <option value="公司辞退">公司辞退</option>
           </select>
    	</td>
    	<td class="tit"><span class="cRed" valid="resignReason">*</span>离职原因</td>
        <td>
          <input id="hidResignReason" type="hidden" value="${staffFlowApply.resignReason}"/>
          <select id="resignReason" class="sel02">
             <option value="个人发展">个人发展</option>
             <option value="实习期结束">实习期结束</option>
             <option value="合同到期">合同到期</option>
             <option value="业绩不达标">业绩不达标</option>
             <option value="违规违纪">违规违纪</option>
             <option value="其他">其他</option>
           </select>
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="resignDesc">*</span>离职原因描述</td>
        <td colspan="5"><textarea id="resignDesc" class="area01">${staffFlowApply.resignDesc}</textarea></td>
      </tr>
    </table>
  </div>
  
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
			  <img src="themes/comm/images/stateIcoNone.gif"/>[未查看]
		</c:if>
		<c:if test="${task.taskState=='ps_start'}">
			  <img src="themes/comm/images/stateIco02.gif"/>[已查看,未处理]
		</c:if>
		<c:if test="${task.taskState=='ps_end'}">
		 	  <img src="themes/comm/images/stateIco01.gif"/>[已处理]
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
  <div class="addTool2">
  	<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	  <tr>
		<td>
		  <input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
		  <input type="hidden" id="transitionName"/>
	<c:if test="${not empty param.notify}"> 
		<span id="notifyUserNames" style="display:none;color:blue;"></span>
		<div class="blank" style="height:5px;"></div>
		<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify($('#applicantName').text(),'修改的离职申请', 'LZLC', '/m/staff_arch?act=toview&id=');" style="margin-left:50px"/>&nbsp;&nbsp;&nbsp;&nbsp;
    </c:if>
    
		  <span id="commitBut">
	<c:forEach var="transition" items="${transitions}" >
			<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
		  </span>
		</td>
	  </tr>
  	</table>
  </div>
</div>
</body>
