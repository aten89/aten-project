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
<script type="text/javascript" src="commui/attachment/oa.attachment.js"></script>
<script type="text/javascript" src="commui/office/oa.office.js"></script>
<script type="text/javascript"	src="page/info/approval/view_info.js"></script>
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
    <td width="170">${infoForm.information.groupName }</td>
    
  </tr>
  <tr >
    <!-- td  class="tit">失效时间</td>
    <td><input style="width:115px" readonly name="invaliddate" type="text" class="invokeBoth" /></td> -->
    <td class="tit">版&nbsp;&nbsp;&nbsp;&nbsp;块</td>
    <td id="infoLayout" colspan="5">${infoForm.information.infoLayout }</td>
  </tr>
  <tr>
    <td class="tit">标&nbsp;&nbsp;&nbsp;&nbsp;题</td>
    <td colspan="5">${infoForm.information.subject }</td>
    </tr>
     <tr>
      <td class="tit">内&nbsp;&nbsp;&nbsp;&nbsp;容</td>
        <td colspan="5" id="contentSet">
        	<a href="javascript:void(0)" id="fullScrean" style="font-weight:bold;text-decoration:underline">全屏查看</a>
			&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="showRevision" value="1" onclick="showRevisions(this);" />显示修订痕迹
        </td>
    </tr>
  <tr>
    <td colspan="6" id="NTKO_OfficeCtrl" style="height:800px">
    	<div id="contentVal" style="display:none;">${infoForm.information.content}</div>
    </td>
  </tr>
  <tr>
    <td colspan="6" id="NTKO_AttachmentCtrl" style="height:200px"></td>
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
</body>
</html>