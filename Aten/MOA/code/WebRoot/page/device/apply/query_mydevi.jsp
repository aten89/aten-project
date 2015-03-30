<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!--工具栏-->
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_myuse'/>"/>
<div class="addTool sortLine">
  <div class="t01 t_f01">
    <input id="btnDeviceUse" type="button" class="equipmentUse_btn" />
    <input id="equipAllot" type="button" class="equipmentAllot_btn" />
    <input id="equipScrap" type="button" class="equipmentScrap_btn" />
    <input id="equipmentLeaveDealAdd" type="button" class="lzcl_btn"/>
  	<input id="refreshBtn" type="button" class="flash_btn""/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--列表-->
<div class="allList alFix">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr>
        <th><input id="selectAll" type="checkbox" value="" class="cBox" style="height:13px; width:15px"/></th>
        <th width="9%">设备编号</th>
        <th width="12%">设备名称</th>
        <th width="10%">设备类别</th>
        <th width="10%">资产类别</th>
        <th width="9%">所属地区</th>
        <th width="10%">购买方式</th>
        <th width="12%">领用日期</th>
        <th width="28%">操作</th>
      </tr>
    </thead>
    <tbody id="deviceList">
    </tbody>
  </table>
  	<!-- 翻页 -->
	<div class="pageNext"></div>		
	<input id="hidNumPage" type="hidden" value="1"/>
	<input id="hidPageCount" type="hidden" value="15"/>
	<!-- 翻页 结束-->
</div>
<!--列表 end--> 
<script type="text/javascript" src="page/device/apply/query_mydevi.js"></script> 
