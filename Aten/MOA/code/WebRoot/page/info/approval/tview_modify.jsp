<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="commui/attachment/style/attachment.css" rel="stylesheet" type="text/css"></link>
<link rel="STYLESHEET" type="text/css"  href="commui/jwysiwyg/jquery.wysiwyg.css"/>
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="commui/office/oa.office.js"></script>
<script type="text/javascript" src="commui/jwysiwyg/jquery.wysiwyg.js"></script>
<script type="text/javascript"	src="page/info/approval/tview_modify.js"></script>
<title></title>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='info_deal'/>" />
<div class="tabMid">
<input type="hidden" value="${infoForm.id}" id="infoFormId"/>
<input type="hidden" value="${infoForm.contentDocUrl}" id="contentDocUrl"/>
<input type="hidden" value="${sessionUser.displayName}" id="userId"/>
<input type="hidden" value="${infoForm.information.displayMode}" id="displayMode"/>
<!--公告新增-->
<div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  <thead>
	<tr>
		<th colspan="6">信息详情</th>
	</tr>
  </thead>
  <tr >
  	<td class="tit">起&nbsp;草&nbsp;人</td>
    <td width="170">${infoForm.information.draftsManName }</td>
    <td class="tit">起草时间</td>
    <td style="width:150px"><fmt:formatDate value="${infoForm.information.draftDate }"/></td>
    <td class="tit">所属机构</td>
    <td width="170"><select id="groupname" name="groupname" class="sel02" style="width:110px">
	<c:forEach var="group" items="${groups}" >
        <option value="${group.name }" ${group.name eq infoForm.information.groupName ? 'selected':'' }>${group.name }</option>
	</c:forEach>
    	</select>
    </td>
    
  </tr>
  <tr >
    <!-- td  class="tit">失效时间</td>
    <td><input style="width:115px" readonly name="invaliddate" type="text" class="invokeBoth" /></td> -->
    <td class="tit">版&nbsp;&nbsp;&nbsp;&nbsp;块</td>
    <td id="infoLayout" colspan="5">${infoForm.information.infoLayout }</td>
  </tr>
  <tr>
    <td class="tit">标&nbsp;&nbsp;&nbsp;&nbsp;题</td>
    <td colspan="5"><input id="subject" name="subject" type="text" class="ipt01" style="width:600px" maxlength="100" value="${infoForm.information.subject }"/> 
     <select id="titleColor" name="titleColor" class="sel02" style="width:75px">
		<option value="black" ${'black' eq infoForm.information.subjectColor ? 'selected':'' }>黑色</option>
        <option value="red" ${'red' eq infoForm.information.subjectColor ? 'selected':'' }>红色</option>
        <option value="blue" ${'blue' eq infoForm.information.subjectColor ? 'selected':'' }>蓝色</option>
    </select></td>
    </tr>
     <tr>
     <td class="tit">内&nbsp;&nbsp;&nbsp;&nbsp;容</td>
       <td colspan="5" id="contentSet"><a href="javascript:void(0)" id="fullScrean" style="font-weight:bold;text-decoration:underline">全屏编辑</a></td>
   </tr>
  <tr>
    <td colspan="6" id="NTKO_OfficeCtrl" style="height:800px;padding:0 0;"></td>
    </tr>
  <tr>
    <td colspan="6" id="NTKO_AttachmentCtrl" style="height:200px;padding:0 0;"></td>
  </tr>
</table>
</div>
<div class="blank"></div>
<div class="addCon">
		<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
		      <tr>
			    <th colspan="2">审批</th>
			  </tr>
		      <tr>
		        <td  class="tit">审批意见</td>
		        <td><textarea id="comment" class="area01" style="width: 632px;" name="comment"></textarea>
		        	<input type="hidden" id="taskInstanceID" name="tiid" value="${param.tiid }"/>
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
				 </td>
		     </tr>
		</table>
</div>	  
<div class="blank"></div>
	<!-- 各级审批意见 -->
	<div class="costsLog" id="costsLog">
		<h1 class="logArrow"><a  href="javascript:void(0)">各级审批意见显示</a></h1>
		<div class="mb">
			<c:forEach var="task" items="${tasks}" >
					[${task.transactorDisplayName }]:${task.comment } (<fmt:formatDate value="${task.endTime }" pattern="yyyy-MM-dd HH:mm"/>)<br />
			</c:forEach>
	</div>
</div>
</div>
<!--公告新增 end-->
<div id="contentVal" style="display:none;">${infoForm.information.content}</div>
</body>
</html>