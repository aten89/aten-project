<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='doc_class'/>" />
<!--文档模板管理-->

<!--工具栏-->
<div class="addTool">
	<div class="t01">
		<input type="button" class="add_btn" id="addDocclassFlow"/>
		<input id="btnSort" type="button" class="sort_btn"/> 
		<input type="button" class="flash_btn" id="reflash"/>
	</div>
</div>
<!--工具栏 end-->
 
 <div class="allList">
  <table width="100%"  border="0" cellpadding="0"  cellspacing="0" id="docclassTab">
	<thead>
		<tr>
			<th width="16%">分类名称</th>
			<th width="15%">流程类别</th>
			<th width="15%">文件类别</th>
			<!--  th width="14%">可否指定</th-->
			<th width="19%">说明</th>
			<th width="5%">模板</th>
			<th width="30%">操作</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
 </table>
</div>
<!--文档模板管理 end-->
<script type="text/javascript" src="page/doc/paramconf/query_class.js"></script>