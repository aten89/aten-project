<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='flow_draft'/>" />

<script type="text/javascript" src="page/system/flowconf/query_draft.js"></script>
<!-- 工具栏 -->
<div class="addTool">
	<div class="t01 t_f01">
		<input id="btnAddFlowDraft" type="button" class="add_btn" />
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<!-- 搜索 -->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>流程类别：</td>
		<td><div id="flowDraftSel" type="single" overflow="true" name="flowClass"></div></td>
		<td>
			&nbsp;流程名称：<input id="flowName" name="flowName" type="text" maxlength="40" class="ipt05"  style="width:100px"/>
		</td>
		<td>&nbsp;<input id="btnSearch" type="button" value="搜索" class="allBtn"/></td>
	  </tr>
  </table>
  </div>
</div>
<div class="allList">
  <table id="flowDraftList" width="100%"  border="0" align="center" cellpadding="0"  cellspacing="0" style="margin:-1px 0 0">
	<thead>
		<tr>
			<th width="15%">流程类别</th>
			<th width="20%">流程名称</th>
			<th width="15%">版本号</th>
			<th width="26%">流程描述</th>
			<th width="24%">操作</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
  </table>
</div>
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>