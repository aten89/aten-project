<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='rei_deal'/>" />
<!--待办报销单-->
<div class="soso" style="height:auto">
	<div class="t01">
	   <table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>单号：</td>
	    	<td><input type="text" name="reiId" id="reiId" class="ipt01" style="width:70px"/>&nbsp;</td>
	    	<td>报销时间：</td>
	    	<td>从 <input id="beginApplyDate" readonly type="text" class="invokeBoth"  style="width:65px"/> 到 
	    <input id="endApplyDate" readonly type="text" class="invokeBoth"  style="width:65px"/>&nbsp;&nbsp;</td>
	    	<td>报销人：</td>
	    	<td><input id="applicant" type="hidden"/>
	    		<input id="applicantName" readonly type="text" class="ipt01" maxlength="30" style="width:65px"/>
	    		<input type="button" id="openUserSelect" class="selBtn">&nbsp;&nbsp;</td>
	    	<td>
       		<td><input id="searchReim" class="allBtn" type="button" value="搜索"/>&nbsp;
	    	<input id="refresh" type="button" class="flash_btn"/></td>
		  </tr>
	  </table>
	</div>
</div>
<div class="allList">
  <table width="100%" style="margin:-1px 0 0" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr id="tableTH">
			<th width="10%" sort="id">报销单号</th>
			<th width="16%" sort="applyDate">报销时间</th>
			<th width="10%" sort="applicant">报销人</th>
			<th width="16%" sort="applyDept">所属部门</th>
			<th width="10%">报销金额</th>
			<th width="16%">任务名称</th>
			<th width="16%">收到时间</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody id="reimList">
	</tbody>
	
  </table>
  <div class="pageNext"></div>
</div>
<input id="hidNumPage" type="hidden" value="1"/>
<input id="hidPageCount" type="hidden" value="15"/>

<div class="blank"></div>
<b>您当前总共有&nbsp;<font color="red" id="totalCount">0</font>&nbsp;个待办任务。</b>
<!--待办报销单 end-->
<script type="text/javascript" src="page/cost/reimburse/deal_reim.js"></script>