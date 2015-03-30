<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<div class="soso">
<input id="hidModuleRights" type="hidden" value="<oa:right key='staff_query'/>"/>
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>类型：</td>
	      <td><div id="typeDiv" name="type" style="display:none"><div>0**在职</div><div>1**离职</div></div>&nbsp;</td>
          <td>机构：</td>
		  <td width="135"><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
		  <td>
		  	姓名：<input id="userAccountId" type="text" class="ipt05" />
		  </td>
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
		<th width="15%">帐号</th>
		<th width="12%">姓名</th>
		<th width="10%">工号</th>
		<th width="12%">人员隶属</th>
		<th width="16%">入职部门</th>
		<th width="18%">入职职位</th>
		<th width="11%">入职时间</th>
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
<script type="text/javascript" src="page/hr/staffflow/query_staff.js"></script>
