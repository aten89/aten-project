<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/info/approval/start_info.js"></script>
<!--我的草稿-->
<input id="hidModuleRights" type="hidden" value="<oa:right key='info_draft'/>" />
<div class="newTypeBg" style="display:none" id="newType">
	<div class="addTit01"><img id="addInfo1" src="themes/comm/images/add.gif"/></div>
	<div class="addTit01Shadow"></div>
	<div class="newTypeShadow">
		<div class="newType">
		   <ul id="infoLayouts">
		   </ul>
		</div>
	</div>
</div>
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01 t_f01">
		<input id="addInfo" type="button" class="add_btn"/>
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<!--工具栏 end-->
<div class="allList">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" style="margin-top:-1px">
  <thead>
	<tr>
		<th width="51%">标题</th>
		<th width="17%">起稿时间</th>
		<th width="20%">版块</th>
		<!--<th width="11%">类别</th>-->
		<th width="12%">操作</th>
	</tr>
  </thead>
  <tbody id="infoDraftList">
  </tbody>
</table>
</div>
<!--我的草稿 end-->