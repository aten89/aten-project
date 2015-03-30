<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--工具栏-->
<div class="addTool sortLine">
<input id="hidModuleRights" type=hidden value="<oa:right key='holi_type'/>"/>
	<div class="t01 t_f01">
		<input type="button" class="add_btn" id="addFlow"/>
		<input type="button" class="flash_btn" id="refresh"/>
	</div>
</div>
<!--工具栏 end-->
<!--列表-->
<div class="allList">
<table id="holidayType" width="100%" border="0" cellspacing="0" cellpadding="0" id="flowSet">
  <thead>
	<tr>
		<th width="20%">期假名称</th>
		<th width="14%">单次最长天数</th>
		<th width="26%">计算方式</th>
		<th width="28%">说明</th>
		<th width="12%">操作</th>
	</tr>
  </thead>
  <tbody id="holidayFlowList">
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
<script type="text/javascript" src="page/hr/paramconf/query_type.js"></script>
