<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/device/paramconf/check/query_item.js"></script>
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01 t_f01">
    <input id="addDeviceCheckItemBtn" type="button" class="add_btn" />
    <input id="refreshBtn" type="button" class="flash_btn" />
  </div>
</div>
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_check_item'/>" />
<!--工具栏 end-->
<div class="blank3"></div>
<!--列表-->
<div class="allList alFix">
  <table id="" width="100%" border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr>
       	<th width="16%">资产类别</th>
        <th width="16%">设备类别</th>
        <th width="34%">检查项名称</th>
        <th width="16%">备注</th>
        <th width="18%">操作</th>
      </tr>
    </thead>
    <tbody id="deviceCheckItemList">
    </tbody>
  </table>
  <!-- 翻页 -->
	<div class="pageNext"></div>		
	<input id="hidNumPage" type="hidden" value="1"/>
	<input id="hidPageCount" type="hidden" value="10"/>
	<!-- 翻页 结束-->
</div>
<!--列表 end--> 
