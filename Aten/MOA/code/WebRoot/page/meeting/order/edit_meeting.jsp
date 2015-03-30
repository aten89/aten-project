<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link href="commui/attachment/style/attachment.css" rel="stylesheet" type="text/css"></link>
<link rel="STYLESHEET" type="text/css"  href="commui/jwysiwyg/jquery.wysiwyg.css"/>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>

<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="commui/jwysiwyg/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="page/meeting/order/edit_meeting.js"></script>

<style>
.mkdzTip td{ background:none}
.bindUser td{ padding-left:9px}
</style>
<title></title>
</head>
<body class="bdNone">
<input type="hidden" id="id" value="${meetingInfo.id}"/>
<input type="hidden" id="roomId" value="${roomId}"/>
<input type="hidden" id="areaCode" value="${areaCode}"/>
<input type="hidden" id="isResend" value="${param.resend }"/>
<input type="hidden" id="orderDate" value="<fmt:formatDate value="${defaultOrderDate}" pattern="yyyy-MM-dd"/>"/>

<input type="hidden" id="bTime" value="<fmt:formatDate value="${meetingInfo.beginTime }" pattern="yyyy-MM-dd HH:mm"/>"/>
<input type="hidden" id="eTime" value="<fmt:formatDate value="${meetingInfo.endTime }" pattern="yyyy-MM-dd HH:mm"/>"/>

<div class="tabMid" style="width:830px">
	<div id="tab00" style="display:none">
		<div class="addCon">
			<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
        		<tr><th colspan="4">会议预订</th></tr>
              	<tr>
                    <td class="tit">预订人</td>
                    <td width="250">
                    	<c:if test="${not empty meetingInfo.groupName}">
			        		${meetingInfo.groupName} ->
			        	</c:if>
				 		${meetingInfo.applyManDisplayName }
                    </td>
                    <td class="tit">预订时间</td>
                    <td  width="250"><fmt:formatDate value="${meetingInfo.reserveTime }" pattern="yyyy-MM-dd HH:mm"/></td>
               	</tr>      
              	<tr>
                    <td class="tit"><span class="cRed">*</span>所属地区</td>
                    <td >
                    	<div id="areaCodeDiv" name="areaCodeDiv"></div>
                    </td>
                    <td class="tit"><span class="cRed">*</span>会议室</td>
                    <td><div id="meetingRoomDiv" name="meetingRoom">                    	
                    </td>
             	</tr>
             	<tr>
                    <td class="tit"><span class="cRed">*</span>起止时间</td>
                    <td colspan="3">
                    	<div style="float:left"><input id="beginDate" readonly type="text" class="invokeBoth"  style="width:62px"/></div>
                    	<div id="beginTimeSel" name="beginTime" style="float:left;margin-left:3px"></div>
                    	<div style="float:left;margin-left:3px">--</div>
                        <div style="float:left"><input id="endDate" readonly type="text" class="invokeBoth"  style="float:left;margin-left:3px;width:62px"/></div>                        
						<div id="endTimeSel" name="endTime" style="float:left;margin-left:3px"></div>
						<div id="roomFlag" style="color:red;margin-left:5px;font-size:12px"></div>
                    </td>
             	</tr>
             	<!--  
              	<tr>
                    <td class="tit">设备名称</td>
                    <td colspan="3">
                    	<div id="meetRoomEnv" style="margin-left:5px;margin-bottom: 5px;"></div>
                    	<div class="meetingSb" id="deviceList">
                      	</div>
                    </td>
             	</tr>
             	-->
              	<tr>
                    <td class="tit"><span class="cRed">*</span>会议主题</td>
                    <td colspan="3"><input type="text" class="ipt01" style="width:450px" id="theme" maxlength="60" value="${meetingInfo.theme }"/></td>
              	</tr>
              	<tr>
                    <td class="tit">备注</td>
                    <td colspan="3"><textarea class="area01" style="width:450px" id="remark">${meetingInfo.remark }</textarea></td>
              	</tr>
				<tr>
                    <td colspan="4" valign="top">
                    	<div  class="dzbd">
							<table border="0" cellspacing="0" cellpadding="0" >
								<tr>
									<td valign="top" width="352px">
									  <!-- 对话框[选择] -->
								      <div class="mkdzTip" >
									  	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
									  	  <tr>
											<td style="min-width:10px">机构：</td>
											<td style="min-width:10px"><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
											<td style="min-width:10px">帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:70px" /> 
												<input id="searchGroupUsers" type="button" class="allBtn" value="检索" style="width:38px"/>
											</td>
									 	  </tr>
									 	</table>
									  </div>
									  <div class="mkdz">
									    <div id="unBindUser" class="bindUser" style="height:267px;width:350px" >
									      <table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
								            <thead>
								              <tr>
												 <th nowrap width="30">操作</th>
												 <th width="28%" nowrap>显示名称</th>
												 <th width="20%" nowrap>用户帐号</th>
												 <th width="37%" nowrap>所属机构</th>
											  </tr>
											 </thead>
											 <tbody></tbody>
								          </table>
									    </div>
									  </div>    	        
									<!-- 对话框[选择] end-->
									</td>
									<td align="center" width="15"><img src="page/meeting/order/meetingImg/arrowMeeting.gif" style="margin:0; vertical-align:middle; cursor:default" /></td>
									<td valign="top" width="322">
									  <div style="padding: 6px 0pt 0pt 10px; position:relative" class="mkdzTip">
						                  <img src="themes/comm/images/addMeeting.gif" id="addMeeting"/>参与人员
						                  <div class="meetingBg"  style="display:none" id="addMan">
						                      <div class="addMeeting">
						                          <p>姓名：<input type="text" class="ipt01" style="width:133px" id="name"/></p>
						                          <p>邮箱：<input type="text" class="ipt01" style="width:133px"  id="email"/></p>
						                          <p style="text-align:right;padding-right:6px">
						                          <input type="button" value="确定" class="allBtn" style="width:50px" id="addUserBtn"/>
						                          <input type="button" value="关闭" class="allBtn" style="width:50px" id="tipClose"/>
						                          </p>
						                      </div> 
						                  </div> 
						              </div>
									  <div class="mkdz">
									    <div id="bindUsers" class="bindUser" style="width:320px">
										   <table  id="bindUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
											   <thead>
												   <tr>
							                          <th width="30px">操作</th>
							                          <th>显示名称</th>
							                          <th>帐号/邮箱</th>
												  	</tr>
											   </thead>
											   <tbody>
									
											   </tbody>
										   </table>
									    </div>					
									  </div>
									</td>
								</tr>
							</table>
						</div>
              		</td>
            	</tr>
         	</table>
		</div>
	</div>

    <div id="tab01" class="tabShow" style="display:none;width:797px;">
		<div class="addCon" style=" margin:0 auto">
    		<div class="blank" style="height:15px"></div>
   			<table width="100%" border="0" align="center"  cellpadding="0"  cellspacing="1">
    			  <tr>
				    <th>会议内容</th>
				  </tr>
			</table>
    	</div>
		<div id="content" style="margin:0 auto; text-align:center">
			<div id="contentVal" style="display:none; "></div>
			<input type="hidden" id="title"/>
			<textarea rows="20" id="wysiwyg" style="width:802px;"></textarea>
		</div>
		<div id="NTKO_AttachmentCtrl" style="height:200px; width:797px; margin:0 auto;"></div>
	</div>
</div>

<div class="addTool2" id="but00" style="display:none">
    <input type="button" id="saveOrderBtn" class="allBtn" value="确定"/>
</div>

<div class="addTool2" id="but01" style="display:none">
    <input type="button" id="saveEmailBtn" class="allBtn" value="保存"/>
    <input type="button" id="sendEmailBtn" class="allBtn" value="保存并发送邮件"/>
	<input type="button" id="closeBtn" class="allBtn" value="关闭"/>
</div>
</body>
</html>
