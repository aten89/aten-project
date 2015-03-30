<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
	<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
	<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
	<script type="text/javascript" src="page/travel/query/query_travel.js"></script>
	<title></title>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='trip_admin_track'/>"/> 
<div class="soso" >
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>单号：</td>
          <td><input id="id" type="text" maxlength="50" class="ipt01" style="width:65px"/>&nbsp;</td>
          <td>出差人员：</td>
          <td><input id="applicantName" type="text" maxlength="50" class="ipt01" readonly style="width:65px"/>
          <input type="button" id="openUserSelect" class="selBtn"/><input id="userId" type="hidden" />&nbsp;</td>
          <td class="soPaddLeft">所属部门：</td>
          <td><div style="width:115px">
            <div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240">
            </div>
          </div></td> 
          <td>出差日期：</td>
          <td>从
            <input type="text" id="startDate" maxlength="100" readonly="readonly" class="invokeBoth"  style="width:65px" />
               到
            <input maxlength="100" id="endDate" readonly="readonly" type="text" class="invokeBoth"  style="width:65px" />
          </td>          
          <td>&nbsp;<input type="button" value="搜索" class="allBtn" id="query"/></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
<!--列表-->
<div class="allList">
<table id="tripList" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
        <th width="8%">单号</th>
        <th width="8%">申请人</th>
        <th width="10%">申请时间</th>
        <th width="12%">所属部门</th>
		<th width="36%">出差日程</th>
		<th width="7%">天数</th>
		<th width="7%">类型</th>
		<th width="7%">状态</th>
		<th width="5%">操作</th>
	</tr>
  </thead>
  <tbody id ="list">
  </tbody>
</table>
</div>
<!--列表 end-->
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
</body>
</html>
