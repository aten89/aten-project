<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>

<script type="text/javascript" src="${ermpPath}oa/commui/attachment/oa.attachment.json.js"></script>
<link href="${ermpPath}oa/commui/attachment/style/attachment.css" rel="stylesheet"	type="text/css"></link>
<script type="text/javascript" src="${ermpPath}jqueryui/eapp.common.js"></script>	
<script type="text/javascript" src="page/operationsManage/refuncApplication/viewRefuncNotice.js"></script>

<title></title>
</head>
<body class="bdDia">
<input type="hidden" id="id" value="${refuncNotice.id }"/>
<form name="mainForm">
<div class="addCon">
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
    <tr>
    	<th colspan="4" class="tipBg">基本信息</th>
    </tr>
    <tr>
		<td class="tit"><span class="cRed">*</span>公司名称：</td>
		<td>${refuncNotice.trustCompany }</td>
	</tr>
	<tr>
		<td class="tit"><span class="cRed">*</span>退款须知： </td>
		<td style="height: 100px;">
			<textarea id="refundNotice" class="ipt05" style="height: 100%;width: 100%">${refuncNotice.refundNotice }</textarea>
		</td>
	</tr>
    <tr style="height: 180px;">
		<td class="tit"><div>资料模板：</div></td>
		<td>
			<div id="NTKO_AttachmentCtrl" style="height: 150px; margin: 0 auto;"></div>
		</td>
	</tr>
	<tr>
		<td class="tit">联系人：</td>
		<td>${refuncNotice.linkman }</td>
	</tr>
  </table>
  <div class="btTip"></div>
</div>

<div class="addTool2">
  <input type="button" id="close" class="allBtn" value="关闭"/>
</div>
</form>
</body>