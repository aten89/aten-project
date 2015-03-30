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
		<script type="text/javascript" src="page/logquery/interface/query_ilog.js"></script>
		<!-- 本页面 end -->
		<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="page/dialog/dialog.module.js"></script>
	</head>
	<body class="bdNone" style="height:100%">
		<!--搜索-->
		<form id="mainForm" name="mainForm" onsubmit="return false;">
			<div class="soso">
				<div class="t01">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>系统：</td>
							<td>
							    <div style="width:145px">
								<!--下拉框-->
								<div id="subSystemList">
								</div>
								<!--下拉框 end-->
								</div>
							</td>
							<td>模块：</td>
							<td>
								<!--下拉框-->
								<input id="moduleName" readonly type="text" style="width:86px" class="ipt05" value=""/><button id="openModuleBtn" class="selBtn"></button>
								<!--下拉框 end-->
							</td>
							<td>&nbsp;动作：</td>
							<td>
							    <div style="width:120px">
								<!--下拉框-->
								<div id="actionList">
									
								</div>
								<!--下拉框 end-->
								</div>
							</td>
							<td>
								接口帐号：
								<input id="accountid" name="accountid" type="text" class="ipt05"
									value="" maxlength="40" style="width:70px" />
								显示名称：
								<input id="accountname" name="accountname" type="text" class="ipt05"
									value="" maxlength="40" style="width:70px" />
								&nbsp; 
							</td>
						</tr>
					</table>
				</div>
				<div class="t02">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								时间：<input id="begintime" name="begintime" type="text" size="10"
									value="" class="invokeBoth tw2" />
								<input id="endtime" name="endtime" value="" type="text"
									size="10" class="invokeBoth tw2" />
								<input type="button" value="搜索" id="searchLog" class="allBtn" r-action="query"/>
								<input id="exportLog" type="button" value="导出" class="allBtn" r-action="export"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<input id="hidModuleId" name="moduleid" type="hidden" value="" />
			<input id="hidModuleKey" name="modulekey" type="hidden" value="" />
		</form>
		<!--搜索 end-->
		<!--列表-->
		<div class="allList">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="17%">子系统名称</th>
						<th width="12%">模块名称</th>
						<th width="11%">动作名称</th>
						<th width="11%">接口帐号</th>
						<th width="12%">显示名称</th>
						<th width="12%">IP 地址</th>
						<th width="15%">时间</th>
						<th width="5%">操作</th>
					</tr>
				</thead>
				<tbody id="data_list"></tbody>
			</table>
		</div>
		<!--翻页-->
		<div class="pageNext">
		</div>
		<!--翻页 end-->
	</body>
</html>
