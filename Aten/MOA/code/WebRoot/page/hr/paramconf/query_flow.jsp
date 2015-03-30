<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--文档模板管理-->
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01 t_f01">
		<input type="button" class="add_btn" id="addFlow"/>
		<input type="button" class="flash_btn" id="refresh"/>
	</div>
</div>
<!--工具栏 end-->
 <input id="hidModuleRights" type=hidden value="<oa:right key='holi_flow'/>"/>
 <div class="allList">
  <table id="holidayFlow" width="100%"  border="0" cellpadding="0"  cellspacing="0" id="flowSet">
	<thead>
		<tr>
			<th width="13%">部门名称</th>
			<th width="13%">请假流程</th>
			<th width="13%">销假流程</th>
			<th width="13%">入职流程</th>
			<th width="13%">离职流程</th>
			<th width="13%">异动流程</th>
			<th width="13%">转正流程</th>
			<th width="9%">操作</th>
		</tr>
	</thead>
	<tbody id="holidayFlowList">
	</tbody>
 </table>
</div>
<!--文档模板管理 end-->
<script type="text/javascript" src="page/hr/paramconf/query_flow.js"></script>