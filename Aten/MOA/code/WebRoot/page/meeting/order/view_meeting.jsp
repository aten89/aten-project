<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="page/meeting/order/view_meeting.js"></script>

<title></title>
</head>
<body class="bdNone">
<input type="hidden" value="${meetingInfo.id}" id="id"/>
<div class="tabMid">
<div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <tr>
            <th colspan="6">查看会议详情</th>
      </tr>
      <tr>
        <td class="tit"><div style="width:98px;">预订人</div></td>
        <td width="268">
        	<c:if test="${not empty meetingInfo.groupName}">
        		${meetingInfo.groupName} ->
        	</c:if>
			 ${meetingInfo.applyManDisplayName }</td>
		<td class="tit">预订时间</td>
        <td colspan="3"><div style="width:160px;"><fmt:formatDate value="${meetingInfo.reserveTime }" pattern="yyyy年MM月dd日"/></div></td>	 
       
      </tr>
      <tr>
      	<td class="tit">起止时间</td>
        <td id="termTime"><input type="hidden" id="beginTime" value="<fmt:formatDate value="${meetingInfo.beginTime }" pattern="yyyy-MM-dd HH:mm"/>"/>
        	<input type="hidden" id="endTime" value="<fmt:formatDate value="${meetingInfo.endTime }" pattern="yyyy-MM-dd HH:mm"/>"/></td>
      	<td class="tit">会议室</td>
        <td width="160"><div style="width:160px;">${meetingInfo.meetingRoom.name }</div></td>	 
       	<td class="tit">所属地区</td>
        <td width="160">${meetingInfo.meetingRoom.areaName }</td>
      </tr>
      <tr>
        <td class="tit">参与人员</td>
        <td colspan="5">
      		<c:forEach var="mpList" items="${meetingInfo.participants}" >
      			${mpList.personName }（${mpList.accountOrEmail}）&nbsp;
           	</c:forEach>
        </td>
      </tr>
      <tr>
        <td class="tit">会议主题</td>
        <td colspan="5"> ${meetingInfo.theme }</td>
      </tr>
      <tr>
        <td class="tit">备注</td>
        <td colspan="5" style="word-break:break-all">${meetingInfo.remark}</td>
      </tr>
      
      
  </table>
  <div class="meetingConBk clearfix" style="margin:-1px 0 0;width:896px">
  	<div class="mTit01">会议内容</div>
  	<div class="mCon01" id="mCon01" style="width:680px">
    	<div>${meetingInfo.content}</div>
        <c:if test="${not meetingInfo.meetingEnd && isParticipant}">
            <div id="NTKO_AttachmentCtrl1" style="height:200px; width:613px;"></div>
        </c:if> 
    </div>
  </div>
  <c:if test="${meetingInfo.meetingEnd && isParticipant}">
  <div class="meetingConBk clearfix" style="margin:-1px 0 0;width:896px">
  	<div class="mTit01">会议纪要</div>
  	<div class="mCon01"  style="width:680px">
        <div id="NTKO_AttachmentCtrl2" style="height:200px; width:613px;"></div>
        <div class="addTool2">
            <input id="submitAndSend" class="allBtn" type="button" value="保存并发送邮件"/>&nbsp;&nbsp;
            <input id="submitMinutes" class="allBtn" type="button" value="保存" />
        </div>
    </div>
  </div>
  </c:if> 
</div>
<c:if test="${meetingInfo.meetingBegin && meetingInfo.applyMan==sessionUser.accountID}">
	<div class="addTool2">
		<input type="button" class="allBtn" value="重发会议通知" id="meetingReInform"/>&nbsp;&nbsp;
		<input type="button" class="allBtn" value="会议变更" id="meetingModify"/>&nbsp;&nbsp;
		<input type="button" class="allBtn" value="会议取消" id="meetingCancel"/>
	</div>
<br/>
</c:if>
</div>
</body>
</html>
