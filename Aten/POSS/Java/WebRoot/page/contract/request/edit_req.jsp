<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/contract/request/edit_req.js"></script>
<title></title>
</head>
<body class="bdDia">
<input type="hidden" id="prodId_p"  value="${param.prodId}" />
<input type="hidden" id="prodId"  value="${contractRequest.prodId}" />
<input type="hidden" id="reqId"  value="${contractRequest.id}" />
<div class="dialogBk">
  <div class="addCon">
  <table width="100%" border="0" cellpadding="0"  cellspacing="1" align="center">
    <tr>
		<td class="tit"><span class="cRed">*</span>产品项目： </td>
		<td><div id="prodSel" name="prodSel"></div></td>	
	</tr>
    <tr>
    	<td class="tit">所属机构：</td>
		<td>${contractRequest.orgName}</td>
	</tr>
	<tr>
    	<td class="tit">剩余合同数：</td>
		<td id="remainNums"></td>
	</tr>
	<tr>
    	<td class="tit"><span class="cRed">*</span>所需合同数：</td>
		<td><input id="reqNums" type="text" maxlength="10" class="ipt01" value="${contractRequest.reqNums}"/></td>
	</tr>
	<tr>
    	<td class="tit">需求备注：</td>
		<td><textarea id="reqRemark" class="area01">${contractRequest.reqRemark}</textarea></td>
	</tr>
  </table>
  </div>
</div>
<!--新增客户资料 end-->
<div class="addTool2" style="margin-top: 5px;">
  <input type="button" id="save"  class="allBtn" value="保存"/>&nbsp;&nbsp;
  <input type="button" id="close" class="allBtn" value="关闭"/>
</div>
</body>