<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/paramconf/area/view_area.js"></script>
<title>设备类别</title>
</head>
<body class="bdNone">
<div class="tabMid">
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="4"><img src="themes/comm/images/frameNav2.gif"/>设备类别</th>
      </tr>
      <tr>
        <td class="tit">所属区域</td>
        <td colspan="3">${areaDeviceCfg.areaName }</td>
      </tr>
       <tr>
        <td class="tit">资产类别</td>
        <td colspan="3">${areaDeviceCfg.deviceTypeName }</td>
      </tr>
      <tr>
        <td class="tit">是否主设备</td>
       <td colspan="3">${areaDeviceCfg.mainDevFlag?"是":"否" }</td>
      </tr>
      
      <tr>
        <td class="tit">设备类别</td>
        <td colspan="3">${areaDeviceCfg.deviceClass.name }</td>
      </tr>
      <tr>
        <td class="tit">前缀</td>
        <td colspan="3">${areaDeviceCfg.orderPrefix }</td>
      </tr>
      <tr>
        <td class="tit">符号</td>
        <td colspan="3">${areaDeviceCfg.separator }</td>
      </tr>
      <tr>
        <td class="tit">流水号位数</td>
        <td colspan="3">${areaDeviceCfg.seqNum }</td>
      </tr>
      <tr>
        <td class="tit">预览</td>
        <td colspan="3">${areaDeviceCfg.viewCode }</td>
      </tr>
      <tr>
        <td class="tit">设备领用流程</td>
        <td class="detailField">${areaDeviceCfg.useApplyFlowName}</td>
        <td class="tit">设备调拨流程</td>
        <td>${areaDeviceCfg.allocateFlowName }</td>
      </tr>
      <tr>
        <td class="tit">设备报废流程</td>
        <td >${areaDeviceCfg.discardFlowName }</td>
         <td class="tit">设备离职流程</td>
        <td >${areaDeviceCfg.dimissionFlowName }</td>
      </tr>
      <tr>
        <td class="tit">描述</td>
        <td colspan="3">${areaDeviceCfg.remark }</td>
      </tr>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
</body>
