<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='bud_bxdcxcw'/>"/>
<input id="budgetItemName" name="budgetItemName" type="hidden" value=""/>
<!--报销单查询  -->

	<div class="soso" style="height:auto">
		<div class="t01">
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td>所属部门：</td>
				<td style="padding:1px 0 0"><input id="deptName" readonly type="text" class="ipt01" style="width:65px"/>
		            <input type="button" id="openDeptSelect" class="selBtn" /></td>
				<td  style="padding-left:20px">报销人：<input id="applicant" type="hidden"/>
				<input id="applicantName" readonly type="text" class="ipt01"  style="width:90px"/><input type="button" id="openUserSelect" class="selBtn" />
               &nbsp;&nbsp;
			    报销单号：<input id="reimId" type="text" class="ipt01"  style="width:90px"/>&nbsp;&nbsp;</td>
			  </tr>
			  	
			</table>
		</div>
		<div style="padding:2px 0 3px 33px">
			<table border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td>入库日期：从 <input readonly id="beginArchiveDate" name="Input2" type="text" class="invokeBoth"  style="width:65px"/> 到 
				<input readonly id="endArchiveDate" name="Input2" type="text" class="invokeBoth"  style="width:65px"/>&nbsp;</td>
				<td>状态：</td>
	    		<td><div style="width:79px"><div id="passedDiv" name="passed" style="display:none"><div>**所有...</div><div>Y**通过</div><div>N**作废</div></div></div></td>
				<td><input id="searchLog" class="allBtn" type="button" value="搜索"/>&nbsp;
	    		<input id="refresh" type="button" class="flash_btn"/></td>
			  </tr>
			</table>
		</div>
	</div>
	
	<div class="allList">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" id="expenseAccountTab">
	<thead>
		<tr>
			<th width="15%">报销单号</th>
			<th width="16%">报销时间</th>
			<th width="15%">报销人</th>
			<th width="22%">所属部门</th>
			<th width="16%">入库时间</th>
			<th width="6%">状态</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
  </table>
</div>
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<!-- 翻页 -->
<div class="pageNext">
</div>
<!-- 翻页 end -->
<!--报销单查询   end-->
<script type="text/javascript" src="page/cost/costquery/query_reim.js"></script>