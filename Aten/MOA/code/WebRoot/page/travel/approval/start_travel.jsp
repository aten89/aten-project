<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<style>
.over{ cursor:pointer}
</style>
<input id="hidModuleRights" type="hidden" value="<oa:right key='trip_start'/>"/>
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01 t_f01">
		<input id="tripAdd" type="button" class="add_btn""/>
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<!--工具栏 end-->
<!--列表-->
<div class="allList">
<table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:auto">
  <thead>
    <tr>
        <th width="58%">出差日程</th>
		<th width="10%">天数</th>
		<th width="20%">申请时间</th>
		<th width="12%">操作</th>
	</tr>
  </thead>
  <tbody id="infoDraftList">
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
<script type="text/javascript" src="page/travel/approval/start_travel.js"></script>
