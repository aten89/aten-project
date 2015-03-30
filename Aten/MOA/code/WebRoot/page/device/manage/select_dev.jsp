<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备选择</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/manage/select_dev.js"></script>
<style>
html {
	overflow:hidden
}
</style>
</head>
<body class="oppBd">
<input type="hidden" id="deviceTypeCode" value="${param.deviceTypeCode }" />
<input type="hidden" id="deviceClassCode" value="${param.deviceClassCode }" />
<input type="hidden" id="devAttachFlag" value="${param.devAttachFlag }"/><!-- 控制设备归属的显示与隐藏 -->
<input type="hidden" id="defaultDevAttachFlag" value="${param.defaultDevAttachFlag }" />
<input type="hidden" id="defaultMyDeviceFlag" value="${param.defaultMyDeviceFlag }" />
<input type="hidden" id="disableMyDeviceFlag" value="${param.disableMyDeviceFlag }" />
<div class="oppConScroll" style="height:335px">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td><div class="soso">
          <div class="t01" style="height:auto">
            <table border="0" cellpadding="0" cellspacing="0" >
              <tr>
                <td>
                	<c:if test="${param.devAttachFlag == 'true'}">
	                	<span style="h:background:#ccc;">设备归属：<input name="chkDevAttach" type="checkbox" value="0" id="myDeviceFlag" class="cBox"/><label for="myDeviceFlag">我名下的设备</label>
	                  	<c:if test="${isAdmin}"><input name="chkDevAttach" type="checkbox" value="1" id="unUseFlag" class="cBox"/><label for="unUseFlag">闲置设备</label></c:if></span>
                  	</c:if>设备类别：
                </td>
                <td><div style="width:115px">
                    <div id="deviceClassSel" name="deviceClassSel">
            		</div>
                  </div></td>
                  <td class="soPaddLeft">设备编号：</td>
                <td><input id="deviceNo" type="text" class="ipt05" lang="20"  style="width:80px" />&nbsp;</td>
                <td class="soPaddLeft">设备名称：</td>
                <td><input id="deviceName" type="text" class="ipt05"  style="width:80px" /></td>
                <td>&nbsp;<input id="btnQueryDevices" class="allBtn" type="button" value="搜索"/></td>
              </tr>
            </table>
          </div>
        </div></td>
    </tr>
    <tr>
      <td><div class="allList">
          <table id="" width="100%" border="0" cellspacing="0" cellpadding="0">
            <thead>
              <tr>
                <th width="8"><input id="selectAll" type="checkbox" value="" class="cBox" style="height:13px; width:15px"/></th>
                <th width="70">设备归属</th>
                <th width="90">设备类别</th>
                <th width="50">设备编号</th>
                <th width="80">设备名称</th>
                <th width="60">设备型号</th>
                <th width="80">购买日期</th>
                <th width="60">购买方式</th>
              </tr>
            </thead>
            <tbody id="devicesBody">
            </tbody>
          </table>
          <!-- 翻页 -->
			<div class="pageNext"></div>		
			<input id="hidNumPage" type="hidden" value="1"/>
			<input id="hidPageCount" type="hidden" value="10"/>
		  <!-- 翻页 结束-->
        </div></td>
    </tr>
  </table>
</div>
<div class="oppBtnBg">
  <input id="saveBtn" type="button" value="确 定" class="allBtn" onclick="saveInfo()"/>
  <input id="closeBtn" type="button" value="取 消" class="allBtn" onclick="doclose()"/>
</div>
</body>
