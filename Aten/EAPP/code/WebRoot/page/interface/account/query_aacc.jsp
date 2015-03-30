<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/interface/account/query_aacc.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="add_btn" id="addActorAcc" r-action="add"/>
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
		<td>
		  帐户：<input id="keyword" name="accountid" type="text" maxlength="40" class="ipt05"  style="width:100px"/>
		  锁定状态：
		</td>
		<td>
		  <!--下拉框-->
		  <div id="islockLink" style="display:none">
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
		  <div id="isvalidLink" style="display:none">
		    <div>**所有...</div>
			<div>Y**有效</div>
			<div>N**无效</div>
		  </div>
		  <!--下拉框 end-->
		</td>
		<td>&nbsp;<input id="searchActorAcc" name="searchActorAcc" type="button" value="搜索" class="allBtn" r-action="query"/></td>
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
		<th width="18%">接口帐号</th>
		<th width="18%">显示名称</th>
		<th width="10%">锁定状态</th>
		<th width="17%">创建时间</th>
		<th width="12%">失效时间</th>
		<th width="15%">操作</th>
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