<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01 t_f01">
	  <input type="button" class="add_btn" id="addEntry"/>
	  <input type="button" class="queryAssign_btn" id="queryAssign"/>
	  <input type="button" class="flash_btn" id="refresh"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<div class="soso">
<input id="hidModuleRights" type="hidden" value="<oa:right key='${param.moduleKey}'/>"/>
<input id="type" type="hidden" value="${param.type}"/>
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
          <td>&nbsp;<a href="javascript:void(0)" onclick="fieldDisplaySet()" style="color:red">【定制显示字段】</a> &nbsp;
          <input type="button" value="搜索" class="allBtn" id="query">&nbsp;
          <input id="exportExcel" class="allBtn exlW" type="button" value="导出Excel"/>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>

<!--列表-->
<div id="divTable" class="allList">
<!--
<table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout:auto">
  <thead>
    <tr>
		<th width="12%">帐号</th>
		<th width="12%">姓名</th>
		<th width="10%">工号</th>
		<th width="12%">人员隶属</th>
		<th width="16%">入职部门</th>
		<th width="11%">入职职位</th>
		<th width="11%">入职时间</th>
		<th width="12%">操作</th>
	</tr>
  </thead>
  <tbody id="list">
  </tbody>
</table>
-->
</div>
<!--列表 end-->
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<script type="text/javascript" src="page/hr/staffflow/query/field_info.js"></script>
<script type="text/javascript" src="page/hr/staffflow/query/query_staff.js"></script>
