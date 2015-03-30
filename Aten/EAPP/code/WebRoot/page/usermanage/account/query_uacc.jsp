<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/usermanage/account/query_uacc.js"></script>
<script type="text/javascript" src="page/dialog/dialog.dept.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="add_btn" id="addUserAcc" r-action="add"/>
	  <input type="button" class="allDel_btn" id="delAllAcc" r-action="delete"/>
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
	    <td>所属机构：</td>
		<td>
		   <input id="groupid" name="groupid" type="hidden"/>
		   <input id="groupname" name="groupname" readonly type="text" class="ipt01" style="width:100px"/>
	       <button id="showGroup" class="selBtn"></button> &nbsp;
		</td>
		<td>
		  帐户：<input id="keyword" name="keyword" type="text" maxlength="40" class="ipt05"  style="width:100px"/>
		  锁定状态：
		</td>
		<td>
		  <!--下拉框-->
		  <div id="islockList" style="display:none">		  
			<div>**所有...</div>
			<div>Y**锁定</div>
			<div>N**未锁定</div>
		  </div>
		  <!--下拉框 end-->
		</td>
		<td>&nbsp;是否有效：
        </td>
		<td>
		  <!--下拉框-->
		  <div id="isvalidList" style="display:none">
			<div>**所有...</div>		  
			<div>Y**有效</div>
			<div>N**无效</div>
		  </div>
		  <!--下拉框 end-->
		</td>
		<td>&nbsp;<input id="searchUserAcc" type="button" value="搜索" class="allBtn" r-action="add"/></td>
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
		<th width="12%">用户帐号</th>
		<th width="13%">显示名称</th>
		<th width="7%">锁定状态</th>
		<th width="9%">强制修改密码</th>
		<th width="15%">创建时间</th>
		<th width="10%">失效时间</th>
		<th width="24%">操作</th>
	  </tr>
  </thead>
  <tbody id="data_list">
  </tbody>
</table>
</div>
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>