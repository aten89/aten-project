<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/device/paramconf/area/query_area.js"></script>
<!--工具栏-->
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_areacfg'/>"/>
<div class="addTool sortLine">
  <div class="t01">
    <input id="add_deviceflow" type="button" class="add_btn"/>
    <input id="DeviceCon_flash" type="button" class="flash_btn" />
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--列表-->
<div class="allList alFix">
  <table id="deviceConfig" width="100%" border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr>
        <th width="20%">所属区域</th>
        <th width="20%">资产类别</th>
        <th width="20%">设备类别</th>
        <th width="12%">编号预览</th>
        <th width="10%">是否主设备</th>
        <th width="18%">操作</th>
      </tr>
    </thead>
    <tbody>
      
    </tbody>
  </table>
  <div class="pageNext"></div>
	<input id="hidNumPage" type="hidden" value="1"/>
	<input id="hidPageCount" type="hidden" value="15"/>
</div>
<!--列表 end--> 
