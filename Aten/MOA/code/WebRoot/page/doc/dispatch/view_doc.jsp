<%@ page language="java" pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="page/doc/dispatch/view_doc.js"></script>
<title></title>
</head>
<body class="bdNone">
<input type="hidden" value="${docForm.id}" id="docFormId"/>
<input type="hidden" value="${docForm.copyDocFormId}" id="copyDocFormId"/>
<input type="hidden" value="${docForm.bodyDraftDocUrl}" id="draftDocUrl"/>
<input type="hidden" value="${docForm.bodyDocUrl}" id="docUrl"/>
<input type="hidden" value="${sessionUser.displayName}" id="userId"/>
<!--起草文件-->
<div class="tabMid2" id="txbd" >
<div class="addCon">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
      <tr>
	    <th colspan="6">公司发文</th>
	  </tr>
      <tr>
        <td class="tit">起草人</td>
        <td width="150">${docForm.draftsmanName }</td>
        <td class="tit" >起草部门</td>
        <td width="150" id="dept">${docForm.groupName}</td>
        <td class="tit">起草时间</td>
        <td><fmt:formatDate value="${docForm.draftDate }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit">发文字号</td>
        <td>${docForm.docNumber}</td>
        <td class="tit">主送单位</td>
        <td>${docForm.submitTo}</td>
        <td class="tit">抄送单位</td>
        <td>${docForm.copyTo}</td>
      </tr>
      <tr>
        <td class="tit">缓急程度</td>
        <td>${ docForm.urgency}</td>
        <td class="tit">密级</td>
        <td>${docForm.securityClass }</td>
         <td class="tit">公文类别</td>
        <td>${ docForm.docClassName}</td>
      </tr>
      <tr>
        <td class="tit">文件标题</td>
        <td colspan="5">${docForm.subject }</td>
      </tr>
      <tr>
      	<td class="tit">内&nbsp;&nbsp;&nbsp;&nbsp;容</td>
		<td colspan="5">
		<a href="javascript:void(0)" id="fullScrean" style="font-weight:bold;text-decoration:underline">全屏查看</a>
		</td>
	  </tr>
      <tr>
	    <td colspan="6" id="NTKO_OfficeCtrl" style="height:800px"></td>
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
</body>
</html>
