<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/cost/reimburse/arch_reim.js"></script>

<input id="hidModuleRights" type="hidden" value="<oa:right key='rei_arch'/>" />
<!--跟踪报销单-->
<div class="soso" style="height:auto">
	<div class="t01">
	   <table border="0" cellspacing="0" cellpadding="0">
		<tr>
	    	<td>单号：</td>
	    	<td><input type="text" name="reiId" id="reiId" class="ipt01" style="width:70px"/>&nbsp;</td>
	    	<td>报销人：</td>
	    	<td><input id="applicant" type="hidden"/>
	    		<input id="applicantName" readonly type="text" class="ipt01" maxlength="30" style="width:65px"/>
	    		<input type="button" id="openUserSelect" class="selBtn">&nbsp;&nbsp;</td>
	    	<td>报销时间：</td>
	    	<td>从 <input id="beginApplyDate" readonly type="text" class="invokeBoth"  style="width:65px"/> 到 
	    		<input id="endApplyDate" readonly type="text" class="invokeBoth"  style="width:65px"/>&nbsp;&nbsp;</td>
	    </tr>
	  </table>
	</div>
	<div style="padding:2px 0 3px 33px">
	 	<table border="0" cellspacing="0" cellpadding="0">
		  <tr>
		  	<td>状态：</td>
	    	<td><div id="passedDiv" name="passed" style="display:none"><div>**所有...</div><div>Y**通过</div><div>N**作废</div></div>&nbsp;</td>
		  	<td>归档时间：<input id="beginArchiveDate" readonly type="text" class="invokeBoth"  style="width:65px"/> 到 
	    	<input id="endArchiveDate" readonly type="text" class="invokeBoth"  style="width:65px"/>&nbsp;</td>
	    	<td>&nbsp;&nbsp;&nbsp;&nbsp;<input id="searchReim" class="allBtn" type="button" value="搜索"/>&nbsp;
	    	<input id="refresh" type="button" class="flash_btn"/></td>
		  </tr>
		</table>
	</div>
</div>

<div class="allList">
  <table width="100%" cellspacing="0" cellpadding="0" border="0">
	<thead>
		<tr id="tableTH">
			<th width="11%" sort="id">报销单号</th>
			<th width="17%" sort="applyDate">报销时间</th>
			<th width="10%" sort="applicant">报销人</th>
			<th width="10%">报销金额</th>
			<th width="23%" sort="applyDept">所属部门</th>
			<th width="17%">入库时间</th>
			<th width="6%">状态</th>
			<th width="6%">操作</th>
		</tr>
	</thead>
	<tbody id="reimArchList">
	</tbody>
  </table>
  <div class="pageNext"></div>
</div>
<input id="hidNumPage" type="hidden" value="1"/>
<input id="hidPageCount" type="hidden" value="15"/>
<!--跟踪报销单 end-->

<!-- 对话框[选择] -->
<div style="display:none">
  <div id="userSelect" style="padding:6px 10px 10px; margin:0">
     <div class="mkdzTip">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
	  <tr>
		<td>机构：</td>
		<td><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
		<td>帐户：<input id="userKeyword" type="text" maxlength="40" class="ipt05" style="width:65px" /> 
			<input id="searchGroupUsers" type="button" class="allBtn" value="检索" style="width:38px"/>
		</td>
	  </tr>
	 </table>
	 </div>
	 <div class="mkdz">
	    <div id="unBindUser" class="bindUser" style="height:280px;width:338px" >
	      <table id="actorUserList" width="100%" border="0" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
				 <th width="15%" nowrap width="30">操作</th>
				 <th width="28%" nowrap>显示名称</th>
				 <th width="20%" nowrap>用户帐号</th>
				 <th width="37%" nowrap>所属机构</th>
			  </tr>
			 </thead>
			 <tbody></tbody>
          </table>
	    </div>
	 </div>  
  </div>   	       
</div>	   