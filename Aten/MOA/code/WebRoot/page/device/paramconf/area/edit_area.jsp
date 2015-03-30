<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title>设备类别</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/device/paramconf/area/edit_area.js"></script>
<style>
html {
	overflow:hidden
}
body {
	overflow:hidden
}
</style>
</head>
<body class="oppBd">
<input type="hidden" id="id" value="${areaDeviceCfg.id }"/>
<input type="hidden" id="deviceClassId" value="${areaDeviceCfg.deviceClass.id }"/>
<input type="hidden" id="areaCode" value="${areaDeviceCfg.areaCode}"/>
<input type="hidden" id="deviceTypeCode" value="${areaDeviceCfg.deviceClass.deviceType }" />
<input type="hidden" id="purchaseFlowKey" value="${areaDeviceCfg.purchaseFlowKey }"/>
<input type="hidden" id="useApplyFlowKey" value="${areaDeviceCfg.useApplyFlowKey }"/>
<input type="hidden" id="allocateFlowKey" value="${areaDeviceCfg.allocateFlowKey }"/>
<input type="hidden" id="discardFlowKey" value="${areaDeviceCfg.discardFlowKey }"/>
<input type="hidden" id="dimissionFlowKey" value="${areaDeviceCfg.dimissionFlowKey }"/>

<input type="hidden" id="mainDevFlag" value="${areaDeviceCfg.mainDevFlag }"/>

<div style="height:366px"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="4"><img src="themes/comm/images/frameNav2.gif"/>设备类别</th>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>所属地区</td>
        <td ><div style="width:131px">
            <div id="areaCodeDiv" name="areaCodeS">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>资产类别</td>
        <td><div style="width:131px">
            <div id="deviceTypeSel" name="zclb">
            </div>
          </div></td>
      </tr>
       <tr>
        <td class="tit"><span class="cRed">*</span>设备类别</td>
        <td><div style="width:131px">
          <div id="deviceClassSel" name="deviceClass">
          </div>
        </div></td>
        <td class="tit"><span class="cRed">*</span>是否主设备</td>
        <td><div style="width:131px">
            <div id="zsb" name="zsb">
              <div>true**--是--</div>
              <div isselected="true">false**--否--</div>
            </div>
          </div></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>前缀</td>
        <td colspan="3"><div style="float:left">
            <input id="orderPrefix" onmouseout="viewInfoNo()" type="text" maxlength="50" class="ipt05" value="${areaDeviceCfg.orderPrefix}"/>
          </div>
          <ul class="allInfotip aiLeft">
            <li>用于标识该设备类别的字符，如：英文字母等</li>
          </ul></td>
      </tr>
      <tr>
        <td class="tit">符号</td>
        <td colspan="3"><div style="float:left">
            <input id="separator" type="text" onmouseout="viewInfoNo()" maxlength="50" class="ipt05" value="${areaDeviceCfg.separator}"/>
          </div>
          <ul class="allInfotip aiLeft">
            <li>用于连接前缀与流水号之间的字符，如：下划线等</li>
          </ul></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>流水号位数</td>
        <td colspan="3"><input id="seqNum" onmouseout="viewInfoNo()" type="text" maxlength="1" class="ipt05"  value="${areaDeviceCfg.seqNum}"/></td>
      </tr>
      <tr>
        <td class="tit">预览</td>
        <td colspan="3" id="viewInfo"></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>设备领用流程</td>
        <td><div style="width:131px">
          <div id="sblylc" name="sblylc">
          </div>
        </div></td>
        <td class="tit"><span class="cRed">*</span>设备调拨流程</td>
        <td><div style="width:131px">
          <div id="sbdb" name="sbdb">
          </div>
        </div></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>设备报废流程</td>
        <td width="200"><div style="width:131px">
            <div id="sbbf" name="sbbf">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>离职处理流程</td>
        <td width="200"><div style="width:131px">
          <div id="lzsb" name="lzsb">
          </div>
        </div></td>
      </tr>
      <tr>
        <td class="tit">描述</td>
        <td colspan="3"><textarea id="remark" name="zyjd" class="area01" style="width:70%">${areaDeviceCfg.remark}</textarea></td>
      </tr>
      
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn"/>
  </div>
</div>
</body>
