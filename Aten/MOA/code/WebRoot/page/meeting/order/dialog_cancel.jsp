<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="../../base_path.jsp"></jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>取消会议</title>
    <script type="text/javascript" src="page/meeting/order/dialog_cancel.js"></script>
</head>
<body class="bdDia">
<input type="hidden" value="${meetingInfo.id}" id="id"/>
<div class="tabMid">
    <div class="addCon">
      <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
          <tr>
                <th colspan="2">会议取消</th>
          </tr>
          <tr>
                <td class="tit">邮件标题</td>
                <td><div  contentEditable id="editMeetingTit">《${meetingInfo.theme }》的会议取消通知</div></td>
          </tr>
          <tr>
                <td class="tit">邮件内容</td>
                <td>
                    <div id="cancelMeeting" style="padding:0; margin:0">
                    	<input type="hidden" id="beginTime" value="<fmt:formatDate value="${meetingInfo.beginTime }" pattern="yyyy-MM-dd HH:mm"/>"/>
        				<input type="hidden" id="endTime" value="<fmt:formatDate value="${meetingInfo.endTime }" pattern="yyyy-MM-dd HH:mm"/>"/>
                        <div contentEditable id="editMeeting" style="clear:both; padding:15px 10px 0; background:#fff; border:1px solid #fff">
                          <p style=" font-size:14px; line-height:1.6">
                          	&nbsp;&nbsp;&nbsp;&nbsp;<font style="font-style:italic;">【此邮件系统自动发送，请勿回复！】</font><br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;由于&nbsp;<span style="font-weight:bold; text-decoration:underline">________</span>&nbsp;原因，

                                原定于&nbsp;<span style="font-weight:bold;  text-decoration:underline" id="termTime"></span>&nbsp; 
                                在&nbsp;<span style="font-weight:bold; text-decoration:underline">${meetingInfo.meetingRoom.areaName }${meetingInfo.meetingRoom.name }</span>&nbsp;举行的&nbsp;
                       		<span style="font-weight:bold; text-decoration:underline">${meetingInfo.theme }</span> &nbsp;的会议取消。请相互转告，谢谢！
                          </p>
                          <br/>
                          <div style="padding:10px 0 5px; text-align:right; line-height:1.6">${sessionUser.groupNames } -> ${sessionUser.displayName }<br /><span id="currentDate"></span></div>
                        </div>
                    </div> 
               </td>
          </tr>
       </table>
    </div>      
</div>      
      

<div class="addTool2">
        <input id="cancelAndSendBtn" type='button' class="allBtn" value='取消并发送邮件通知'/>
        <input id="cancelBtn" type='button' class="allBtn" value='取消但不发送邮件通知'/>
        <input id="closeBtn" type='button' class="allBtn" value='关闭'/>
</div>
</body>
</html>