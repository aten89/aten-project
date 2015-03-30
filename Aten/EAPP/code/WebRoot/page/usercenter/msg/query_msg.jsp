<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/usercenter/msg/query_msg.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<form id="mainForm" name="mainForm" onsubmit="return false;">
<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="allDel_btn" id="delAllMsg"/>
	  <input type="button" class="flash_btn" id="refresh"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--搜索-->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
			发送者：<input name="msgSender" id="msgSender" type="text" class="ipt05"  style="width:90px" maxlength="50"/>&nbsp;
			时间：<input id="begintime" name="begintime" type="text" size="10" value="" class="invokeBoth tw2" />
			<input id="endtime" name="endtime" value="" type="text" size="10" class="invokeBoth tw2" />
			<input type="button" id="searchMsg" value="搜索" class="allBtn"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--列表-->
<div class="allList">
  <table id="zxt" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
		<th width="4%">序号</th>
		<th width="4%" class="qx"><input id="checkbox" type="checkbox" name="checkbox" value="checkbox" onclick="checkAll();"/></th>
		<th width="10%">发送者</th>
		<th width="14%">发送时间</th>
		<th width="68%">消息内容</th>
	  </tr>
  </thead>
  <tbody id="data_list"></tbody>
</table>
</div>
<!--列表 end-->
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
</form>
</body>
</html>