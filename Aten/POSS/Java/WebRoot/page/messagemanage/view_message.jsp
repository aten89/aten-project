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
<script type="text/javascript" src="page/messagemanage/view_message.js"></script>
<title></title>
</head>
<body class="bdNone">
<div class="tabMid">
  <div class="addCon">
  <table width="70%" border="0" align="center" cellpadding="0"  cellspacing="1">
    <tr>
    	<td class="tit">投资顾问：</td>
		<td colspan="4">${saleManagerName }</td>
    </tr>
    <tr>
    	<td class="tit"><span class="cRed">*</span>标题：</td>
		<td colspan="4">${message.title}</td>
	</tr>
	<tr>
		<td class="tit"><span class="cRed">*</span>产品： </td>
		<td colspan="4">${message.prodName}</td>	
	</tr>
	<tr>
      <td class="tit">客户：</td>
      <td colspan="4">${message.receiverNo}</td>
    </tr>
    <tr>
      <td class="tit">短信内容：</td>
      <td colspan="5">${message.content}</td>
    </tr>
  </table>
  </div>
</div>
<!--新增客户资料 end-->
<div class="addTool2">
  <input type="button" id="close" class="allBtn" value="关闭"/>
</div>
</body>