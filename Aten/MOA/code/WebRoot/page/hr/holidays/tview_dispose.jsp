<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/holidays/tview_dispose.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<input type="hidden" id="cancelFlag" value="${holidayApply.cancelFlag}" />
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <thead>
		<tr>
			<th colspan="6">请假单</th>
		</tr>
      </thead>
	  <tr> 	
	 	<td width="100" class="tit">请假单号</td>
        <td width="200" id="holiid">${holidayApply.id}</td>
        <td width="100" class="tit">申 请 人</td>
        <td width="160" id="applicantName">${holidayApply.applicantName}</td>
        <td width="100" class="tit">所在部门</td>
        <td width="230">${holidayApply.applyDept}</td>	
	  </tr>
	  <tr>
        <td class="tit">人员隶属</td>
	    <td>${holidayApply.regionalName}</td>
	    <td class="tit">指定审批人</td>
	    <td colspan="3">${holidayApply.appointToName}</td>
      </tr>
<c:if test="${holidayApply.isSpecial==true}">   
     <tr>
        <td class="tit"  colspan="6" style="text-align:left;width:auto">
          <span style="color:red;font-size:13px;font-weight:bold">该员工申请了总裁特批，以下是申请的理由：</span></td>
      </tr>
      <tr id="specialReasonPanel" style='display:${holidayApply.isSpecial==true ? "": "none"}'>
        <td class="tit">特批理由</td>
        <td colspan="5">${holidayApply.specialReason}</td>
      </tr>
</c:if>
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
       <tr>
        <td colspan="6"  class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs">
            <li class="current default">
              <div class="lastNone" style="cursor:default">请假单明细</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <!--预算信息-->
                      <div class="ywjgList">
                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <tr>
                            <th>假期类型</th>
                            <th>请假时间</th>
                            <th>天数</th>
                            <th>附加说明</th>
                    <c:if test="${holidayApply.cancelFlag}">           
                            <th>销假天数</th>
                            <th>销假说明</th>
                    </c:if>       
                          </tr>
                      	<c:forEach var="holidayApplyDetail" items="${holidayApply.holidayDetail}" >
							<tr>
                            <td>${holidayApplyDetail.holidayName }</td>
                            <td>从 <fmt:formatDate value="${holidayApplyDetail.startDate }" pattern="yyyy-MM-dd"/>${holidayApplyDetail.startTimeStr } 到 <fmt:formatDate value="${holidayApplyDetail.endDate }" pattern="yyyy-MM-dd"/>${holidayApplyDetail.endTimeStr } </td>
                            <td style="color:red;font-weight:bold">${holidayApplyDetail.days }天</td>
                            <td>${holidayApplyDetail.remark}</td>
                        <c:if test="${holidayApply.cancelFlag}"> 
                            <td style="color:red;font-weight:bold"><c:if test="${holidayApplyDetail.cancelDays > 0}">${holidayApplyDetail.cancelDays}天</c:if></td>
                            <td>${holidayApplyDetail.cancelRemark}</td>
                        </c:if>
                          </tr>
						</c:forEach>
                        </table>
                      </div>
                      <!--预算信息 end-->
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
         </td>
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
       		<textarea name="qjsy" class="area01" id="comment" style="float:left;height:105px;width:420px" ></textarea>
		    <div style="float:left;width:240px;margin-left:30px">
			    <div class="newRemark" style="width:200px">该员工今年历史请假情况：<br/>
		        	<c:forEach var="holidayInfo" items="${holidayInfos}" >
						${holidayInfo.key}：<label class="cf60">${holidayInfo.value}</label>天<br/>
					</c:forEach>
		       </div>
            </div>
            <div class="boxShadow " id="selDiv01" style="display:none;position:absolute;top:50px;left:428px">
			  <div class="shadow01">
			    <div class="shadow02" >
			      <div class="shadow03" >
			        <div class="shadow04" >
			          <div class="shadowCon" style="background:#fff;padding:5px 10px 0;width:338px" >
			          	<c:forEach var="holidayApply" items="${holidayApplys}" >
							<a href="javascript:void(0)" onclick="view('${holidayApply.id}');"><b>${holidayApply.applicantName}</b> 从 <fmt:formatDate value="${holidayApply.holidayStartDate}" pattern="yyyy-MM-dd HH:mm"/> 到 <fmt:formatDate value="${holidayApply.holidayEndDate}" pattern="yyyy-MM-dd HH:mm"/> </a><br/>
						</c:forEach>
						<div style="text-align:center;"><a href="javascript:void(0)" onclick="closeVacationDiv(this)" style="background:#eee">[关闭]</a></div>
					  </div>	
			        </div>
			      </div>
			    </div>	
			  </div>
			</div>
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
					<input type="button" value="添加知会人" class="allBtn" onclick="toAddFlowNotify('${sessionUser.displayName }');" style="margin-left:50px"/>&nbsp;&nbsp;
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
