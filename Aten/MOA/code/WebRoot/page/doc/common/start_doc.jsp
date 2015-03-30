<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='nor_start'/>"/>
<!--起草公文-->
<div class="newTypeBg" style="display:none" id="newType">
	<div class="addTit01"><img id="addOfficeDoc1" src="themes/comm/images/add.gif"/></div>
	<div class="addTit01Shadow"></div>
	<div class="newTypeShadow">
		<div class="newType">
		   <ul id="officeDocLayouts">
		   </ul>
		</div>
	</div>
</div>
<!--工具栏-->
<div class="addTool sortLine">
	<div class="t01">
		<input id="addOfficeDoc" type="button" class="add_btn"/>
		<input id="refresh" type="button" class="flash_btn"/>
	</div>
</div>
<!--工具栏 end-->
<div class="allList">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" style="margin-top:-1px">
		<thead>
			<tr>
				<th width="42%">文件标题</th>
				<th width="16%">起草时间</th>
				<th width="16%">起草部门</th>
				<th width="14%">类别</th>
				<th width="12%">操作</th>
			</tr>
		</thead>
	  <tbody id="officeDocDraftList">
 	  </tbody>
	</table>
</div>
<!--起草公文 end-->
<script type="text/javascript" src="page/doc/common/start_doc.js"></script>