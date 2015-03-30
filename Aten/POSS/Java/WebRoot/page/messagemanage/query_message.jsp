<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/messagemanage/query_message.js"></script>
</head>

<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='MY_MESSAGE'/>" />
<!--工具栏-->
<div id="divAddTool" class="addTool">
  <div class="t01">
	  <input type="button" class="add_btn" id="add" />
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  	<td class="soPaddLeft">发行日期从：</td>
		<td class="soPaddRight"><input readonly id="sendTimeBegin" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
		<td class="soPaddLeft">到：</td>
		<td class="soPaddRight"><input readonly id="sendTimeEnd" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
		<td>接收者：</td>
		<td><input id="receiverNo" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
		<td>产品：</td>
		<td><div id="prodSel" name="prodSel"></div></td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<input id="searchInfo" type="button" value="搜索" class="allBtn"/>&nbsp;&nbsp;
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="30%">接收人</th>
		<th width="15%">发送时间</th>
		<th width="13%">产品项目</th>
		<th width="30%">短信内容</th>
		<th width="12%">操作</th>
	</tr>
  </thead>
 <tbody id="messageList">
 </tbody>
</table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="10"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>