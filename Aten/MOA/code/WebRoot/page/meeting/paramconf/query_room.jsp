<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/meeting/paramconf/query_room.js"></script>
<title></title>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='meet_param'/>"/>
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01">
		<input type="button" class="add_btn" id="addMeetingRoom"/>
		<input id="btnSort" type="button" class="sort_btn"/> 
		<input type="button" class="flash_btn" id="reflash"/>
	</div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--列表-->
<div class="allList">
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="roomSet">
  <thead>
	<tr>
		<th width="10%">所属地区</th>
		<th width="13%">会议室</th>
		<th width="5%">座位</th>
		<th width="5%">插座</th>
		<th width="5%">网线</th>
		<th width="5%">电话线</th>
		<th width="10%">电话号码</th>
		<th width="7%">可被预订</th>
		<th width="15%">环境</th>
		<th width="15%">备注</th>
		<th width="10%">操作</th>
	</tr> 
		
  </thead>
  <tbody>
  </tbody>
</table>
</div>
<!--列表 end-->
</body>
</html>
 