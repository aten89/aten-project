<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='dis_deal'/>"/>
<!--待办文件-->
<div class="addTool">
	<div class="t01">
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<div class="allList">
  <table width="100%" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr>
			<th width="45%">文件标题</th>
			<th width="17%">起草时间</th>
			<th width="16%">起草人</th>
			<th width="16%">类别</th>
		<!-- <th width="12%">起草部门</th>
			<th width="17%">任务名称</th>
			 -->
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody id="docList">
	 </tbody> 
  </table>
</div>
<div class="blank"></div>
<b>您当前总共有&nbsp;<font color="red" id="totalCount">0</font>&nbsp;个待办任务。</b>
<!--待办文件 end-->
<script type="text/javascript" src="page/doc/dispatch/deal_doc.js"></script>