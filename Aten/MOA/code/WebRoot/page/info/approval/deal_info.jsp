<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<script type="text/javascript" src="page/info/approval/deal_info.js"></script>
<input id="hidModuleRights" type="hidden" value="<oa:right key='info_deal'/>" />
<!--我的草稿-->
<div class="addTool sortLine">
	<div class="t01 t_f01">
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<div class="allList">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" style="margin-top:-1px">
  <thead>
	<tr>
		<th width="49%">标题</th>
		<th width="18%">起稿时间</th>
		<th width="11%">起稿人</th>
		<th width="16%">版块</th>
		<th width="6%">操作</th>
	</tr>
  </thead>
  <tbody id="inforList">
  </tbody>
</table>
</div>
<div class="blank"></div>
<b>您当前总共有&nbsp;<font color="red" id="totalCount">0</font>&nbsp;个待办任务。</b>
<!--我的草稿 end-->
