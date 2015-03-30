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
<script type="text/javascript" src="page/doc/common/draft_doc.js"></script>
<title></title>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='nor_start'/>"/>
<input type="hidden" value="${docForm.id}" id="docFormId"/>
<input type="hidden" value="${docForm.copyDocFormId}" id="copyDocFormId"/>
<input type="hidden" value="${docForm.bodyDraftDocUrl}" id="draftDocUrl"/>
<input type="hidden" value="${sessionUser.displayName}" id="userId"/>
<input type="hidden" value="${docClassUrl}" id="docClassUrl"/>
<input type="hidden" value="${docForm.docClassName }" name="docclass" id="docclass"/>

<!--起草文件-->
<div class="tabMid2" id="txbd" >
<div class="addCon">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
      <tr>
	    <th colspan="6">${docForm.docClassName }</th>
	  </tr>
      <tr>
        <td class="tit">起草人</td>
        <td width="100" id="draftsman">${docForm.draftsmanName }</td>
        <td class="tit" >起草部门</td>
        <td width="100" id="dept">
			<select id="groupname" name="groupname" class="sel02" style="width:110px">
				<c:forEach var="group" items="${groups}" >
			        <option value="${group.name }" ${group.name eq docForm.groupName ? 'selected':'' }>${group.name }</option>
				</c:forEach>
    		</select>
		</td>
        <td class="tit">起草时间</td>
        <td id="draftDate"><fmt:formatDate value="${docForm.draftDate }" pattern="yyyy-MM-dd"/></td>
      
      </tr>
      <tr>
        <td class="tit">文件标题</td>
        <td colspan="8"><input name="subject" type="text" class="ipt01" style="width:640px"  id="subject" maxlength="100" value="${docForm.subject }"/></td>
      </tr>
      <tr>
      	<td class="tit">正文内容</td>
		<td colspan="8">
			<a href="javascript:void(0)" id="fullScrean" style="font-weight:bold;text-decoration:underline">全屏编辑</a>
		</td>
		
	  </tr>
      <tr>
	    <td colspan="8" id="NTKO_OfficeCtrl" style="height:800px"></td>
	  </tr>
	  <tr>
	    <td colspan="8" id="NTKO_AttachmentCtrl" style="height:200px"></td>
	  </tr>
 </table>
</div>

<div class="addTool2">
<input id="saveActorAcc" class="allBtn" type="button" value="提交" name="saveActorAcc"/>
<input type="button" value="保存为草稿" class="allBtn" id="saveDraft"/>
<!-- 
<input id="btnTxbd" class="allBtn" type="reset" value="保存草稿" name="resetActorAcc"/>
 -->
</div>
</div>
<!--起草文件 end-->
</body>
</html>
