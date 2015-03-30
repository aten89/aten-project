<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>历史分配查询</title>
	<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
	<script type="text/javascript" src="page/DataManage/allocateQuery/historyAllocateQuery.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='HIS_ALLOCATE_QUERY'/>" />
	<div class="soso">
		<div class="t01">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>分配时间从：</td>
					<td><input readonly id="allocateTimeBegin" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>
					<td>到：</td>
					<td><input readonly id="allocateTimeEnd" type="text" class="invokeBoth soTimeW" value=""/>&nbsp;&nbsp;</td>

					<td>批次号：</td>
					<td><input id="batchNumber" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
					<td>客户名称：</td>
					<td><input id="custName" type="text" maxlength="40" class="ipt05" />&nbsp;&nbsp;</td>
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
					<th width="15%">批次号</th>
					<th width="15%">客户名称</th>
					<th width="5%">性别</th>
					<th width="15%">电话</th>
					<th width="20%">邮箱</th>
					<th width="15%">分配时间</th>
					<th width="10%">分配给</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody id="custList">
			</tbody>
		</table>
	</div>
	<div class="pageNext"></div>
	<!--翻页 end-->
	<!--列表 end-->
</body>
</html>