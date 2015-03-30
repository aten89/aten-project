<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--工具栏-->
<input id="hidModuleRights" type="hidden" value="<oa:right key='holi_deal'/>"/>
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
		<th width="12%">申请人</th>
		<th width="18%">部门</th>
		<th width="13%">表单类型</th>
		<th width="20%">天数</th>
		<th width="20%">到达时间</th>
		<th width="7%"><div class="oprateW1">操作</div></th>
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
<script type="text/javascript" src="page/hr/holidays/deal_holi.js"></script>
