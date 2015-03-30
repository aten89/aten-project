<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--工具栏-->
<input id="hidModuleRights" type="hidden" value="<oa:right key='staff_start'/>"/>
<div class="newTypeBg" style="display:none" id="newType">
	<div class="addTit01"><img id="toAdd1" src="themes/comm/images/add.gif"/></div>
	<div class="addTit01Shadow"></div>
	<div class="newTypeShadow">
		<div class="newType">
		   <ul id="applyTypeSel">
		     <li id="addEntry" val="1">入职申请</li>
		     <li id="addResign" val="2">离职申请</li>
		     <li id="addMyResign" val="3">个人离职申请</li>
		   </ul>
		</div>
	</div>
</div>
<div class="addTool sortLine">
	<div class="t01 t_f01">
		<input id="toAdd" type="button" class="add_btn"/>
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<!--工具栏 end-->
<!--列表-->
<div class="allList">
<table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:auto">
  <thead>
    <tr>
		<th width="13%">申请类型</th>
		<th width="15%">时间</th>
		<th width="15%">人员隶属</th>
		<th width="15%">部门</th>
		<th width="14%">员工姓名</th>
		<th width="16%">申请时间</th>
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
<script type="text/javascript" src="page/hr/staffflow/start_staff.js"></script>
