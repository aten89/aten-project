<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<script type="text/javascript" src="jqueryui/datepicker/ui.datepicker.js"></script>
		<!-- 本页面 -->
		<script type="text/javascript" src="page/logquery/online/query_online.js"></script>
		<!-- 本页面 end -->
	</head>
	<body class="bdNone">
		<!--搜索-->
		<form id="mainForm" name="mainForm" onsubmit="return false;">
			<div class="soso">
				<div class="t01">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								用户帐号：
								<input id="accountid" name="accountid" type="text" class="ipt05"
									value="" maxlength="40" style="width:80px" />
								用户名称：
								<input id="accountname" name="accountname" type="text" class="ipt05"
									value="" maxlength="40" style="width:80px" />
								&nbsp; 
								<input type="button" value="搜索" id="searchLog" class="allBtn" r-action="query"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 在线用户总数：<b id="onlineCount"></b>
							</td>
						</tr>
				  </table>
				</div>
			</div>
		</form>
		<!--搜索 end-->
		<!--列表-->
		<div class="allList">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="14%">用户名称</th>
						<th width="16%">用户帐号</th>
						<th width="16%">IP 地址</th>
						<th width="49%">登录时间</th>
					</tr>
				</thead>
				<tbody id="data_list"></tbody>
			</table>
		</div>
		<!--翻页-->
		<div class="pageNext">
		</div>
		<!--翻页 end-->
		<!--列表 end-->
	</body>
</html>
