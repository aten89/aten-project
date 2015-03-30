<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/messagemanage/edit_message.js"></script>
<title></title>
</head>
<body class="bdDia">
<div class="dialogBk">
  <div class="addCon">
  <table width="100%" border="0" cellpadding="0"  cellspacing="1" align="center">
    <tr>
    	<td class="tit">投资顾问：</td>
		<td colspan="4">${saleManagerName }</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>标题：</td>
		<td colspan="4"><input id="title" type="text" maxlength="40" class="ipt01" value="${message.title}"/></td>
	</tr>
	<tr>
		<td class="tit"><span class="cRed">*</span>产品： </td>
		<td colspan="4"><div id="prodSel" name="prodSel"></div></td>	
	</tr>
	<tr>
      <td class="tit">客户：</td>
      <td colspan="3"><textarea id="receiverNo" name="receiverNo" class="area01">${custStr}</textarea></td>
      <td><a href="javaScript:void(0);" class="linkOver" style="float:left;margin-top:-6px" id="selectCusts" onclick="selectCusts();">选择客户</a></td>
    </tr>
    <tr>
      <td class="tit">短信内容：</td>
      <td colspan="5"><textarea id="content" name="content" class="area01" style="height:120px; width:150">${message.content}</textarea></td>
    </tr>
  </table>
  </div>
</div>
<!--新增客户资料 end-->
<div class="addTool2" style="margin-top: 15px;">
  <input type="button" id="save"  class="allBtn" value="保存"/>&nbsp;&nbsp;
  <input type="button" id="close" class="allBtn" value="关闭"/>
</div>
</body>