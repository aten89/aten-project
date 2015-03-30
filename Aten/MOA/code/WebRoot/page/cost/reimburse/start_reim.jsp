<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='rei_start'/>" />
<!--启动报销单-->
<div class="allList">
  <div class="addTool"><div class="t01 t_f01">
	<input id="toSave" type="button" class="add_btn"/>
	<input id="refresh" type="button" class="flash_btn"/>
  </div></div>
  <table width="100%" style="margin:-1px 0 0" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr>
			<th width="16%">填单日期</th>
			<th width="14%">受款人</th>
			<th width="10%">报销金额</th>
			<th width="50%">事由</th>
			<th width="10%">操作</th>
		</tr>
	</thead>
	<tbody id="draftLists">  
	</tbody>
  </table>
</div>
<!--启动报销单 end-->
<script type="text/javascript" src="page/cost/reimburse/start_reim.js"></script>