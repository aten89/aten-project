<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<script type="text/javascript" src="page/device/paramconf/assign/query_assign.js"></script>
<!--工具栏-->

<input id="hidModuleRights" type="hidden" value="<oa:right key='device_assgin'/>"/>
<div class="addTool sortLine">
  <div class="t01">
    <input id="assignCon_add" type="button" class="add_btn" />
    <input id="addDelete" type="button" class="del_btn"  />
    <input id="refresh" type="button" class="flash_btn" />
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--列表-->
<div class="allList alFix">
  <table id="assignTab" width="100%" border="0" cellspacing="0" cellpadding="0">
    <thead>
      <tr>
        <th width="5%"><input type="checkbox" name="checkbox" id="assignCheckbox" class="cBox" style="width:13px; height:13px" /></th>
        <th width="14%">所属地区</th>
        <th width="16%">资产类别</th>
        <th width="22%">设备类别</th>
        <th width="13%">备注</th>
        <th width="30%">操作</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</div>
<!--列表 end--> 
<!--设备类别 弹出层 -->
<div class="boxShadow" id="deviceClassDiv2" style="display:none; min-width:158px;_width:158px;" >
  <div class="shadow01">
    <div class="shadow02" >
      <div class="shadow03" >
        <div class="shadow04" >
          <div class="shadowCon">
            <span id="classDiv" style="display:block;height:200px;overflow-y:auto">
            </span>
            <div class="addTool2" style="padding:18px 0 0">
              <input id="saveProblem" type="button" value="确定" class="allBtn"/>
              <input id="selClose" type="button" value="关闭"    class="allBtn"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--设备类别 弹出层 end--> 
