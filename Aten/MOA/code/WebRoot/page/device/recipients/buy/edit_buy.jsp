<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title>设备信息</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/recipients/buy/edit_buy.js"></script>
<style>
html {
	overflow:hidden
}
</style>
</head>
<body class="oppBd">
<input id="deviceClassOld" type="hidden" value=""/>
<input id="deviceClassId" type="hidden" value=""/>
<input id="optionLists" type="hidden" value=""/>
<input type="hidden" id="purposes" value=""/>
<div class="oppConScroll" style="height:350px">
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="4"><img src="themes/comm/images/frameNav2.gif"/>设备信息</th>
      </tr>
      <tr>
      	<td class="tit"><div style="width:99px">资产类别</div></td>
        <td class="field" width="120px"><span id="deviceTypeName"></span></td>
        <td class="tit"><div style="width:99px"><span class="cRed">*</span>设备类别</div></td>
        <td width="300"><div style="width:139px">
            <div id="deviceClassSel" name="deviceClassSel">
            </div>
          </div></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed">*</span>设备名称</td>
        <td><input id="deviceName" type="text" class="ipt05" /></td>
        <td class="tit"><span class="cRed">*</span>设备型号</td>
        <td><input id="deviceModel" type="text" class="ipt05" /></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed">*</span>购买金额</td>
        <td><input id="price" type="text" class="ipt05" maxlength="20"/>元</td>
        <td class="tit"><span class="cRed">*</span>购买日期</td>
        <td><input id="buyTime" type="text" maxlength="100" readonly="readonly" class="invokeBoth"/></td>
      </tr>
      <tr id="configInfo" style="display:none">
        <td class="tit"><span class="cRed">*</span>配置信息</td>
        <td colspan="3">
          <!--配置信息-->
          <div class="ywjgList">
            <table width="398" border="0" cellspacing="1" cellpadding="0">
              <tr>
                <th width="110">配置项</th>
                <th width="">信息</th>
              </tr>
              <tbody id="optionTab">
              </tbody>
            </table>
          </div>
          <!--配置信息--> 
        </td>
      </tr>
      <tr>
        <td class="tit">设备描述</td>
        <td colspan="3"><textarea id="description" class="area01  awIT"></textarea></td>
      </tr>
    </table>
  </div>
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" onclick="doclose()"/>
  </div>
</div>
</body>
