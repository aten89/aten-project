<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<!-- 查询条件 begin -->
<input id="hidModuleRights" type="hidden" value="<oa:right key='device_track'/>"/>
<div class="soso" style="height:auto">
  <div class="t01"  style="height:auto">
    <table border="0" cellpadding="0" cellspacing="0" >
      <tr>
        <td class="soPaddLeft">设备流程类型：</td>
        <td style="width:120px">
             <div id="deviceFormTypeSel" name="deviceFormTypeSel">
            </div>
        </td>
        <td class="soPaddLeft">资产类别：</td>
        <td style="width:120px">
          <div id="deviceTypeSel" name="_sblclx"></div>
        </td>
        <td class="soPaddLeft">申请单编号：</td>
        <td style="width:120px"><input id="formNO" type="text" maxlength="50" class="ipt05 iptSo" /></td>
      </tr>
      <tr>
        <td class="soPaddLeft">申请人：</td>
        <td><input id="applicantName" type="text" maxlength="50" class="ipt05 iptSoSleW" readonly="true"/>
          <input id="applicant" type="hidden"/>
          <button id="openUserSelect" class="selBtn"/></button>&nbsp;
        </td>
        <td class="soPaddLeft">申请日期：</td>
        <td colspan="3">从
          <input id="startApplyTime" type="text" maxlength="100" readonly="readonly" class="invokeBoth"  style="width:65px" />
		  到
		  <input id="endApplyTime" maxlength="100" readonly="readonly" type="text" class="invokeBoth"  style="width:65px" />
          &nbsp;&nbsp;<input id="queryDevice" class="allBtn" type="button" value="搜索"/>
         </td>
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
        <th width="8%">申请单编号</th>
        <th width="10%">设备流程类型</th>
        <th width="8%">资产类别</th>
        <th width="5%">申请人</th>
        <th width="13%">所在部门</th>
        <th width="13%">申请日期</th>
        <th width="10%">所在步骤</th>
        <th width="10%">办理人</th>
        <th width="18%">到达时间</th>
        <th width="5%">操作</th>
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
<script type="text/javascript" src="page/device/apply/track_devi.js"></script> 
