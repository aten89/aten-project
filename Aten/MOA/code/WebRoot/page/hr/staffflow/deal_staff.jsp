<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--工具栏-->
<input id="hidModuleRights" type="hidden" value="<oa:right key='staff_deal'/>"/>
<div class="addTool sortLine">
	<div class="t01">
		<b>我的待办任务</b>
	</div>
</div>
<!--工具栏 end-->
<!--列表-->
<div class="allList">
<table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:auto">
  <thead>
	<tr>
		<th width="12%">申请人</th>
		<th width="12%">申请类型</th>
		<th width="13%">时间</th>
		<th width="13%">人员隶属</th>
		<th width="14%">部门</th>
		<th width="12%">员工姓名</th>
		<th width="18%">到达时间</th>
		<th width="6%">操作</th>
	</tr>
  </thead>
  <tbody id="reimList">
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
<script type="text/javascript" src="page/hr/staffflow/deal_staff.js"></script>
