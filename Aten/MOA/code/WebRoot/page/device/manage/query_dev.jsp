<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<!--工具栏-->
<div class="addTool sortLine">
<input id="areaCode" type="hidden" value="${param.areaCode }"/>
  <div class="t01 t_f01">
    <input id="register" type="button" class="fillUp_btn" />
    <input id="btnDeviceUse" type="button" class="equipmentReg_btn"  />
    <input id="allocation" type="button" class="equipmentAllotReg_btn" />
    <input id="maintain" type="button" class="equipmentRepair_btn"  />
    <input id="scrap" type="button" class="equipmentScrapReg_btn" />
    <input id="scrapdeal" type="button" class="equipmentScrapDispose_btn"  />
    <input id="leaveDeal" type="button" class="lzclReg_btn" />
 	<input id="refresh" type="button" class="flash_btn"/>
  </div>
  
</div>
<!--工具栏 end-->
<div class="blank" style="height:3px;background:#fff;width:830px"></div>
<!-- 查询条件 begin -->
<div class="soso" style="height:auto">
  <div class="t01"  style="height:auto">
    <table border="0" cellpadding="0" cellspacing="0"  style="table-layout:fixed">
      <tr>
        <td width="60"></td>
        <td width="131"></td>
        <td width="66"></td>
        <td width="131"></td>
        <td width="66"></td>
        <td width="131"></td>
        <td width="66"></td>
        <td width="66"></td>
      </tr>
      <tr>
        <td class="soPaddLeft">设备编号：</td>
        <td><input id="deviceNO" type="text" maxlength="50" class="ipt05 iptSo" /></td>
        <td class="soPaddLeft">设备名称：</td>
        <td><input id="deviceName" type="text" maxlength="50" class="ipt05 iptSo" /></td>       
        <td class="soPaddLeft">设备型号：</td>
        <td><input id="deviceModel" type="text" maxlength="50" class="ipt05 iptSo" /></td>
      </tr>
      <tr>
        <td class="soPaddLeft">设备类别：</td>
        <td><div style="width:131px">
            <div id="deviceClassDiv" name="deviceClassDiv"  popwidth='110' overflow="true" height="240">
            </div>
          </div></td>

        <td class="soPaddLeft">设备状态：</td>
        <td><div style="width:120px">
            <div id="deviceStateSel" name="satus">
              <div isselected="true">**所有...</div>
              <!--  <div>1**正常</div>
              <div>6**拿走</div>
              <div>7**报废未处理</div>
              <div>8**报废已处理</div>
              <div>4**离职回购</div>
              <div>5**离职赠送</div>-->
            </div>
          </div></td>
        <td class="soPaddLeft">领用状态：</td>
        <td><div style="width:131px">
            <div id="deviceUseredSel" name="usered">
              <div isselected="true">**所有...</div>
              <div>0**闲置</div>
              <div>1**已领用</div>
              <div>2**报废未处理</div>
              <div>3**报废已处理</div>
            </div>
          </div></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td class="soPaddLeft">使用部门：</td>
        <td><div style="width:131px">
            <div id="groupIdDiv" name="hidGroupId"  popwidth='110' overflow="true" height="240">
            </div>
          </div></td>
        <td class="soPaddLeft">使用人：</td>
        <td><input id="applicantName" type="text" maxlength="50" class="ipt05 iptSoSleW" readonly="true"/><input id="userId" type="hidden"/><input id="openUserSelect" class="selBtn" type="button" /></td>
        <td class="soPaddLeft">购买日期：</td>
        <td colspan="3">从
          <input id="buyTimeStart" type="text" maxlength="100" readonly="readonly" class="invokeBoth"  style="width:65px" />
          到
          <input id="buyTimeEnd" maxlength="100" readonly="readonly" type="text" class="invokeBoth"  style="width:65px" /></td>
        <td><input id="queryDevice" class="allBtn" type="button" value="搜索"/></td>
      </tr>
    </table>
  </div>
</div>
<!-- 查询条件 end --> 
<!--列表-->
<div class="allList alFix">
  <table id="DeviceList" width="100%" border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr id="tableTH">
        <th width="8%" sort="deviceNO">设备编号</th>
        <th width="10%" sort="deviceName">设备名称</th>
        <th width="8%">设备型号</th>
        <th width="9%">设备类别</th>
        <th width="8%">设备状态</th>
        <th width="9%" sort="buyTime">购买日期</th>
        <th width="8%">领用状态</th>
        <th width="40%">操作</th>
      </tr>
    </thead>
    <tbody>
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
<!--列表 end--> 
<script type="text/javascript" src="page/device/manage/query_dev.js"></script> 
