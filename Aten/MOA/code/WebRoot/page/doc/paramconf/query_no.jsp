<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='doc_no'/>" />
<!--文件编号规则   -->
<div class="allList">
  <div class="addTool sortLine">
	<div class="t01">
		<input type="button" class="add_btn" id="addDocNo"/>
		<input type="button" class="flash_btn" id="reflash"/>
	</div>
  </div>
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="0" style="margin:-1px 0 0" id="docNoTab">
	<thead>
	<tr>
		<th width="12%">文件字</th>
		<th width="6%">前缀</th>
		<th width="8%">年份</th>
		<th width="6%">后缀</th>
		<th width="6%">前缀</th>
		<th width="7%">编号</th>
		<th width="6%">后缀</th>
		<th width="23%">预览</th>
		<th width="5%">模板</th>
		<th width="21%">操作</th>
	</tr>
	</thead>
	<tbody>
		<!-- 
		<tr>
			<td>新司字</td>
			<td>（</td>
			<td>2008</td>
			<td>）</td>
			<td>第</td>
			<td>6</td>
			<td>号</td>
			<td>新司字（2008）第6号</td>
			<td><a class="opLink" href="javascript:void(0);">编辑模板</a>&nbsp;&nbsp;<a class="opLink" href="javascript:void(0);">修改</a>&nbsp;&nbsp;<a class="opLink" href="javascript:void(0);">删除</a></td>
		</tr>
		 -->
	</tbody>
 </table>
</div>
<!--文件编号规则    end-->
<script type="text/javascript" src="page/doc/paramconf/query_no.js"></script>