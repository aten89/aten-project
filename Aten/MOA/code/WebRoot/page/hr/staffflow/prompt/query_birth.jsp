<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<div class="soso">
<input id="hidModuleRights" type="hidden" value="<oa:right key='staff_birthday'/>"/>
<input type="hidden" id="expNameAndValue" value=""/>
  <div class="t01">
    <table cellspacing="0" cellpadding="0" border="0" >
      <tbody>
        <tr>
          <td>机构：</td>
		  <td width="135"><div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240"></div></td>
		  <td>
		  	姓名：<input id="userAccountId" type="text" class="ipt05" />
		  </td>
          <td>
          	&nbsp;&nbsp;<input type="button" value="搜索" class="allBtn" id="query">
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
		<th width="12%">姓名</th>
		<th width="12%">工号</th>
		<th width="14%">距生日天数</th>
		<th width="14%">生日日期</th>
		<th width="12%">员工状态</th>
		<th width="30%">所属部门</th>
		<th width="6%">操作</th>
	</tr>
  </thead>
  <tbody id="listData">
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
<script type="text/javascript" src="page/hr/staffflow/prompt/query_birth.js"></script>
