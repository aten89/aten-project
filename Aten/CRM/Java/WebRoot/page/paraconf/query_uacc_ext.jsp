<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title></title>
	<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
	<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
	<script type="text/javascript" src="page/paraconf/query_uacc_ext.js"></script> 
</head>

<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<eapp:right key='C_SERVICE_TO_SALE'/>" />
	<!--工具栏-->
	
	<div class="addTool sortLine">
	  <div class="t01">
		  <input type="button" class="edit_btn" id="editAllAcc"/>
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
			<td>销售部：
	        </td>
			<td>
			  <!--下拉框-->
			  <div id="saleDeportList" type="single" overflow="true" name="saleDeportList">
			  </div>
			  <!--下拉框 end-->
			</td>
			<td>&nbsp;<input id="searchUserAcc" type="button" value="搜索" class="allBtn"/></td>
		  </tr>
	  </table>
	  </div>
	</div>
	<!--搜索 end-->

	<!--列表-->
	<div class="allList">
	  <table id="tabList" width="100%" border="0" cellspacing="0" cellpadding="0">
		  <thead>
			  <tr>
				<th width="5%">序号</th>
				<th width="5%" class="qx"><input id="checkbox" type="checkbox" name="checkbox" value="checkbox" onclick="checkAll();"/></th>
				<th width="15%">用户帐号</th>
				<th width="15%">显示名称</th>
				<th width="15%">客服帐号</th>
				<th width="45">客服姓名</th>
			  </tr>
		  </thead>
		  <tbody id="data_list">
	  	  </tbody>
	  </table>
	</div>
	<!--翻页-->
	<div class="pageNext" style="display:none">
	</div>
	<!--翻页 end-->
	<!--列表 end-->
</body>
</html>