<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<div class="soso">
<input id="hidModuleRights" type="hidden" value="<oa:right key='staff_arch'/>"/>
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>申请人：</td>
          <td><input id="applicant" type="hidden"/>
	    		<input id="applicantName" readonly type="text" class="ipt01" maxlength="30" style="width:65px"/>
	    		<input type="button" id="openUserSelect" class="selBtn">&nbsp;&nbsp;
	      </td>
	      <td>申请类型：</td>
	      <td><div id="applyTypeDiv" name="applyType" style="display:none"><div>**所有...</div><div>1**入职申请</div><div>2**离职申请</div></div>&nbsp;</td>
          <td>&nbsp;<input type="button" value="搜索" class="allBtn" id="query">
            &nbsp;<input id="refresh" type="button" class="flash_btn"/> 
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<!--列表-->
<div class="allList">
<table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:auto">
  <thead>
    <tr>
		<th width="10%">申请人</th>
		<th width="12%">申请类型</th>
		<th width="13%">时间</th>
		<th width="11%">人员隶属</th>
		<th width="15%">部门</th>
		<th width="10%">员工姓名</th>
		<th width="16%">申请时间</th>
		<th width="7%">状态</th>
		<th width="6%">操作</th>
	</tr>
  </thead>
  <tbody id="list">
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
<script type="text/javascript" src="page/hr/staffflow/arch_staff.js"></script>
