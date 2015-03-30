<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/transfer/tview_dispose.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <thead>
		<tr>
			<th colspan="6">基本信息</th>
		</tr>
      </thead>
	  <tr> 	
	 	<td width="100" class="tit">申请单号</td>
        <td width="200" id="tranid">${transferApply.id}</td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160" id="applicantName">${transferApply.applicantName}</td>
        <td width="100" class="tit">申请时间</td>
     	<td width="230"><fmt:formatDate value="${transferApply.applyDate }" pattern="yyyy-MM-dd HH:mm"/></td>
	  </tr>
	  <tr>
      	<td class="tit"><span class="cRed" valid="transferUserName">*</span>异动员工</td>
        <td>${transferApply.transferUserName}</td>
        <td class="tit">员工工号</td>
        <td id="transferUser">${transferApply.transferUser}</td>
        <td class="tit">合同主体</td>
        <td>${transferApply.contractBody}</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="contractStartDate">*</span>合同开始时间</td>
        <td><fmt:formatDate value="${transferApply.contractStartDate}" pattern="yyyy-MM-dd"/></td>
      	<td class="tit"><span class="cRed" valid="contractEndDate">*</span>合同结束时间</td>
        <td><fmt:formatDate value="${transferApply.contractEndDate}" pattern="yyyy-MM-dd"/></td>
    	<td class="tit"><span class="cRed" valid="transferDate">*</span>调动日期</td>
        <td><fmt:formatDate value="${transferApply.transferDate}" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferOutDept">*</span>调出部门</td>
        <td>${transferApply.transferOutDept}</td>
    	<td class="tit"><span class="cRed" valid="transferOutPost">*</span>调出前岗位</td>
        <td>${transferApply.transferOutPost}</td>
        <td class="tit"><span class="cRed" valid="entryDate">*</span>入司日期</td>
        <td><fmt:formatDate value="${transferApply.entryDate}" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferInDept">*</span>调入部门</td>
        <td>${transferApply.transferInDept}</td>
    	<td class="tit"><span class="cRed" valid="transferInPost">*</span>调入后岗位</td>
        <td>${transferApply.transferInPost}</td>
        <td class="tit"><span class="cRed" valid="reportDate">*</span>报到时间</td>
        <td><fmt:formatDate value="${transferApply.reportDate}" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="transferReason">*</span>调动原因</td>
        <td colspan="5"><textarea readonly class="area01">${transferApply.transferReason}</textarea></td>
      </tr>
      <tr>
      	<td class="tit">工作交接内容</td>
        <td colspan="5"><textarea readonly class="area01">${transferApply.transitionContent}</textarea></td>
      </tr>
      <thead>
        <tr>
          <th colspan="6">调动支持信息</th>
        </tr>
      </thead>
      <tr>
      	<td class="tit">财务支持人员</td>
        <td>${transferApply.finaceSupportUserName}</td>
        <td class="tit">财务支持电话</td>
        <td>${transferApply.finaceSupportTel}</td>
        <td class="tit">财务支持邮箱</td>
        <td>${transferApply.finaceSupportEmail}</td>
      </tr>
      <tr>
		<td class="tit">IT支持人员</td>
        <td>${transferApply.itSupportUserName}</td>
        <td class="tit">IT支持电话</td>
        <td>${transferApply.itSupportTel}</td>
        <td class="tit">IT支持邮箱</td>
        <td>${transferApply.itSupportEmail}</td>
      </tr>
      <tr>
        <td class="tit">人力支持人员</td>
        <td>${transferApply.hrSupportUserName}</td>
        <td class="tit">人力支持电话</td>
        <td>${transferApply.hrSupportTel}</td>
        <td class="tit">人力支持邮箱</td>
        <td>${transferApply.hrSupportEmail}</td>
      </tr>
      <tr>
        <td class="tit">变更后办公地</td>
        <td>${transferApply.changeOffice}</td>
        <td class="tit">周汇报线</td>
        <td>${transferApply.weeklyReportTo}</td>
        <td class="tit">月汇报线</td>
        <td>${transferApply.monthlyReportTo}</td>
      </tr>
  </table>
  
  <div class="blank"></div>
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
     <tr>
      <th colspan="2">审批</th>
 	 </tr>
     <tr>
       <td  class="tit">审批意见</td>
       <td style="position:relative;">
       		<input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
       		<textarea class="area01" id="comment" style="width:630px" ></textarea>
       </td>
	</tr>
    <tr>
	  <td  class="tit">操&nbsp;&nbsp;&nbsp;&nbsp;作</td>
        <td>
        	<input type="hidden" id="transitionName"/>
           <span id="commitBut">
	<c:forEach var="transition" items="${transitions}" >
			<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
		</span>
	<c:if test="${not empty param.notify}">   
		<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify('${sessionUser.displayName }','审批的异动申请', 'YDLC', '/m/tran_arch?act=toview&id=');" style="margin-left:50px"/>&nbsp;&nbsp;
		<span id="notifyUserNames" style="display:none;color:blue;"></span>
	</c:if>
	 </td>
    </tr>
  </table>
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
</body>
