<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='trip_deal'/>"/>  
<style>
.over{ cursor:pointer}
</style>
<!--工具栏-->
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
        <th width="10%">单号</th>
        <th width="10%">申请人</th>
        <th width="12%">部门</th>
        <th width="38%">出差日程</th>
		<th width="6%">天数</th>
		<th width="17%">到达时间</th>
		<th width="7%">操作</th>
	</tr>
  </thead>
  <tbody id="tripDealList">
  </tbody>
</table>
</div>
<!--列表 end-->
<script type="text/javascript" src="page/travel/approval/deal_travel.js"></script>
