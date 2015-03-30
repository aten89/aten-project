<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<!-- 查询条件 begin -->
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_start'/>"/>
<div class="soso" style="height:auto">
  <div class="t01"  style="height:auto">
    <table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="soPaddLeft">设备流程类型：</td>
        <td style="width:150px">
            <div id="deviceFormTypeSel" name="_sblclx"></div>
        </td>
        <td><input id="queryDevice" class="allBtn" type="button" value="搜索"/></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table>
  </div>
</div>
<!-- 查询条件 end --> 
<!--列表-->
<div class="allList alFix">
  <table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0">
    <thead id="tableTH">
      <tr>
        <th width="30%">设备流程类型</th>
        <th width="30%">资产类别</th>
        <th width="28%">申请日期</th>
        <th width="12%">操作</th>
      </tr>
    </thead>
    <tbody>
     
    </tbody>
  </table>
</div>
<!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end--> 
<script type="text/javascript" src="page/device/apply/start_devi.js"></script> 
