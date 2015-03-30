<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/hr/positive/tview_modify.js"></script>
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
        <td width="200" id="posiid">${positiveApply.id}</td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160" id="applicantName">${positiveApply.applicantName}</td>
        <td width="100" class="tit">申请时间</td>
        <td width="230"><fmt:formatDate value="${positiveApply.applyDate }" pattern="yyyy-MM-dd HH:mm"/></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="positiveUserName">*</span>转正员工</td>
        <td>
          <div style="float:left">
			<input id="positiveUserName" readonly maxlength="40" type="text" style="width:120px" class="ipt01" value="${positiveApply.positiveUserName}"/>
		  </div>
        </td>
        <td class="tit">员工工号</td>
        <td id="positiveUser">${positiveApply.positiveUser}</td>
        <td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
        <td>
          <input id="entryDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${positiveApply.entryDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="dept">*</span>所属部门</td>
        <td>
          <div style="float:left">
			<input id="dept" readonly maxlength="40" type="text" style="width:70%" class="ipt01" value="${positiveApply.dept}"/>
		  </div>
    	</td>
    	<td class="tit"><span class="cRed" valid="post">*</span>职位</td>
        <td>
          <input id="post" maxlength="40" type="text" class="ipt01" value="${positiveApply.post}"/>
        </td>
        <td class="tit"><span class="cRed">*</span>性 别</td>
        <td>
          <input class="cBox" name="sexOption" type="radio" value="0" ${positiveApply.sex == '1' ? '':'checked'}/>&nbsp;男&nbsp;&nbsp;
          <input class="cBox" name="sexOption" type="radio" value="1" ${positiveApply.sex == '1' ? 'checked':''}/>&nbsp;女
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="probation">*</span>试用期</td>
        <td>
          <input id="probation" maxlength="40" type="text" style="width:120px" class="ipt01" value="${positiveApply.probation}"/>月
        </td>
    	<td class="tit"><span class="cRed">*</span>转正类别</td>
        <td>
          <input class="cBox" name="formalTypeOption" type="radio" value="END" ${positiveApply.formalType == 'EARLY' ? '':'checked'}/>&nbsp;结束试用
          <input class="cBox" name="formalTypeOption" type="radio" value="EARLY" ${positiveApply.formalType == 'EARLY' ? 'checked':''}/>&nbsp;提前转正&nbsp;&nbsp;
        </td>
        <td class="tit"><span class="cRed" valid="formalDate">*</span>转正时间</td>
        <td>
          <input id="formalDate" readonly type="text" class="invokeBoth" style="width:100px" value="<fmt:formatDate value="${positiveApply.formalDate}" pattern="yyyy-MM-dd"/>"/>
    	</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="workResults">*</span>试用期工作成果</td>
        <td colspan="5"><textarea id="workResults" class="area01">${positiveApply.workResults}</textarea></td>
      </tr>
      <thead>
        <tr>
          <th colspan="6">转正感想</th>
        </tr>
      </thead>
      <tr>
      	<td class="tit">企业文化理解</td>
        <td colspan="5"><textarea id="cultureUnderstand" class="area01">${positiveApply.cultureUnderstand}</textarea></td>
      </tr>
      <tr>
      	<td class="tit">规章制度遵守情况</td>
        <td colspan="5"><textarea id="rulesCompliance" class="area01">${positiveApply.rulesCompliance}</textarea></td>
      </tr>
      <tr>
      	<td class="tit">工作体会和收获</td>
        <td colspan="5"><textarea id="workExperience" class="area01">${positiveApply.workExperience}</textarea></td>
      </tr>
      <tr>
      	<td class="tit">总结优势和不足</td>
        <td colspan="5"><textarea id="workSummary" class="area01">${positiveApply.workSummary}</textarea></td>
      </tr>
      <tr>
      	<td class="tit">改进或提升计划</td>
        <td colspan="5"><textarea id="workImprove" class="area01">${positiveApply.workImprove}</textarea></td>
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

  <!--请假单 end-->
  <c:forEach var="holiday" items="${holidayApply.holidayDetail}" >
	<textarea style="display:none" id="tmp_${holiday.id}">${holiday.remark}</textarea>
  </c:forEach>
    <div class="addTool2">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	<tr>
		         <td>
		          	<input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
		         	<input type="hidden" id="transitionName"/>
		        <c:if test="${not empty param.notify}"> 
					<span id="notifyUserNames" style="display:none;color:blue;"></span>
					<div class="blank" style="height:5px;"></div>
					<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify($('#applicantName').text(),'修改的转正申请', 'ZZLC', '/m/posi_arch?act=toview&id=');" style="margin-left:50px"/>&nbsp;&nbsp;&nbsp;&nbsp;
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
