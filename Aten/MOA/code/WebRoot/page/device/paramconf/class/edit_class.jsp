<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title>设备类别</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/device/paramconf/class/edit_class.js"></script>
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
<input type="hidden" id="id" value="${deviceClass.id }"/>
<input type="hidden" id="deviceType" value="${deviceClass.deviceType }"/>
<div class="oppConScroll" style="height:475px">
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="2"><img src="themes/comm/images/frameNav2.gif"/>设备类别</th>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>资产类别</td>
        <td width="550"><div style="width:131px">
          <div id="deviceTypeDiv" name="deviceTypeDiv">
          </div>
        </div></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>类别名称</td>
        <td width="550"><input id="name" type="text" maxlength="50" class="ipt05" value="${deviceClass.name }"/></td>
      </tr>
      <tr>
        <td class="tit">备注</td>
        <td><textarea id="remark" name="remark" class="area01" style="width:288px">${deviceClass.remark }</textarea></td>
      </tr>
      <tr>
        <td colspan="2" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2" class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs">
            <li class="current default" style="cursor:default">
              <div class="lastNone">设备属性配置</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <div id="tab00">
                        <!--设备配置项-->
                        <table width="0" border="0" cellspacing="0" cellpadding="0" style="margin:0 auto">
                          <tr>
                            <td><div style="background:#eee"><b>所有设备属性</b></div>
                              <select id="allProperty" name="allProperty" multiple="multiple" size="15" style="width:188px">
                              </select></td>
                            <td><input type="button" value="添加 &gt;&gt;" id="addOption" class="allBtn"/></td>
                            <td style="background:#eee;padding:0"><div><b>选中的设备属性</b></div>
                              <select id="checkProperty" name="checkProperty"  size="15" style="width:188px">
                              </select></td>
                            <td style="background:#eee;padding:24px 0 0 3px; vertical-align:top;width:66px">
                            <input id="setTop" type="button" value="移至顶部" class="allBtn" style="width:60px; margin:0 0 6px"/>
                            <input id="setUp" type="button" value="上移" class="allBtn" style="width:60px; margin:0 0 6px"/>
                            <input id="setDown" type="button" value="下移" class="allBtn" style="width:60px; margin:0 0 6px"/>
                            <input id="setBottom" type="button" value="移至底部" class="allBtn" style="width:60px; margin:0 0 6px"/>
                            <input id="delete" type="button" value="删除" class="allBtn" style="width:60px"/>
                            </td>
                          </tr>
                        </table>
                        <!--设备配置项 end-->
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
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
