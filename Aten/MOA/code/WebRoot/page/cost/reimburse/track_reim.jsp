<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<input id="hidModuleRights" type="hidden" value="<oa:right key='rei_track'/>" />
<script type="text/javascript" src="page/cost/reimburse/track_reim.js"></script>
<!--跟踪报销单-->
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
  <table width="100%" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr id="tableTH">
			<th width="10%" sort="id">报销单号</th>
			<th width="16%" sort="applyDate">报销时间</th>
			<th width="8%" sort="applicant">报销人</th>
			<th width="8%">报销金额</th>
			<th width="15%" sort="applyDept">所属部门</th>
			<th width="13%">所在步骤</th>
			<th width="8%">办理人</th>
			<th width="16%">到达时间</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody id="reimTrackList">
	</tbody>
  </table>
  <div class="pageNext"></div>
</div>
<input id="hidNumPage" type="hidden" value="1"/>
<input id="hidPageCount" type="hidden" value="15"/>
<!--跟踪报销单 end-->
