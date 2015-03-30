<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<style>
.allList td{ padding:3px}
.allList th{ padding:2px 3px 3px 3px}</style>
<input id="hidModuleRights" type="hidden" value="<oa:right key='bud_fymxcw'/>"/>
<input id="budgetItemName" name="budgetItemName" type="hidden" value=""/>
<!--费用明细查询 -->

   <div class="soso" style="height:auto">
		<div class="t01" >
		    <table border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td height="28px">所属部门：</td>
				<td style="padding:1px 0 0">
					<input id="deptName" readonly type="text" class="ipt01" style="width:65px"/>
		            <input type="button" id="openDeptSelect" class="selBtn" /></td>
				<td style="padding-left:10px">费用类别：</td>
				<td><div class="costSelW"><div id="outlayCategoryDiv" name="outlayCategory"></div></div></td>
			  	<td style="padding-left:10px">费用名称：</td><td><div class="costSelW"><div id="outlayNameDiv" name="outlayName"></div></div></td>
			  	<td style="padding-left:10px">报销人：</td><td><input id="applicant" type="hidden"/>
					<input id="applicantName" readonly type="text" class="ipt01"  style="width:65px"/>
		            <input type="button" id="openUserSelect" class="selBtn" /></td>
			  </tr>
			</table>
		</div>
		<div style="padding:2px 0 3px 33px">
			入库时间：从 <input readonly id="beginArchiveDate"  type="text" class="invokeBoth"  style="width:65px"/> 到 
			<input readonly id="endArchiveDate" type="text" class="invokeBoth"  style="width:65px"/>
			<input id="searchLog" class="allBtn" type="button" value="搜索"/>&nbsp;
	    	<input id="refresh" type="button" class="flash_btn"/>&nbsp;&nbsp;
		</div>
	</div>
<div class="allList">
   <table width="100%" cellspacing="0" cellpadding="0" border="0" id="outlayListTab">
	<thead>
		<tr>
			<th width="10%">报销单号</th>
			<th width="10%">报销人</th>
			<th width="10%">入库时间</th>
			<th width="15%">所属部门</th>
			<th width="13%">费用类别</th>
			<th width="13%">费用名称</th>
			<th width="8%">费用金额</th>
			<th width="21%">说明</th>
		</tr>
	</thead>
	<tbody>

	</tbody>
  </table>
</div>
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<!-- 翻页 -->
<div class="pageNext"></div>
<b id="amountShow" style="display:none">当前查询结果总费用金额：</b><b id="amount"></b>
<!-- 翻页 end -->
<!--费用明细查询  end-->

<script type="text/javascript" src="page/cost/costquery/query_cost.js"></script>
