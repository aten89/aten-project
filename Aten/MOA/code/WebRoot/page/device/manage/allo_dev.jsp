<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备调拨单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/device/manage/allo_dev.js"></script>

</head>
<body class="oppBd">
<input type="hidden" id="deviceTypeCode" value="${applyForm.deviceType }" />
<input type="hidden" id="deviceClassCode" value="${applyForm.deviceClass }" />
<input type="hidden" id="allotType" value="${applyForm.moveType }" />
<input type="hidden" id="areaCode" value="${param.areaCode }" />
<input type="hidden" id="buyTime" value="<fmt:formatDate value="${applyForm.devAllocateDevice.device.buyTime}" pattern="yyyy-MM-dd"/>" />
<input type="hidden" id="mainDevCfgs" value="<c:forEach items="${areaDeviceCfgs  }" var="list">${list.areaCode },${list.deviceClass.id }|</c:forEach>" />

<div class="oppConScroll"> 
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备调拨单</th>
      </tr>
      <tr>
      	<td class="tit">资产类别</td>
        <td class="field">${applyForm.deviceTypeName }</td>
        <td class="tit">登记人</td>
        <td class="field">${applyForm.regAccountDisplayName }</td>
        <td class="tit">登记日期</td>
        <td class="field" ><fmt:formatDate value="${applyForm.regTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>调拨类型</td>
        <td class="field"><div style="width:139px">
            <div id="allotTypeSel" name="allotTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>调拨日期</td>
        <td colspan="3" class="field"><input type="text" id="moveDate" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${applyForm.moveDate }" pattern="yyyy-MM-dd"/>" /></td>
      </tr>
	<tr>
        <td class="tit"><span class="cRed">*</span>设备名称</td>
        <td class="field">
			<input id="deviceName" type="text" class="ipt05" style="width:106px" readonly="true" value="${applyForm.devAllocateDevice.device.deviceName }"/><input id="btnSelectDevice" type="button" class="selBtn" />
        	<input id="deviceID" type="hidden"  class="ipt05" value="${applyForm.devAllocateDevice.device.id }"/>
        	<input type="hidden" id="listDevBuyType" value="${applyForm.devAllocateDevice.device.buyType }"/>
        	<input type="hidden" id="listDevBuyTypeName" value="${applyForm.devAllocateDevice.device.buyTypeName }"/>
        	<input type="hidden" id="listAreaName" value="${applyForm.devAllocateDevice.device.areaName }"/>
            <input type="hidden" id="listAreaCode" value="${applyForm.devAllocateDevice.device.areaCode }"/>
            <input type="hidden" id="listDeviceClassName" value="${applyForm.devAllocateDevice.device.deviceClass.name }"/>
            <input type="hidden" id="listDeviceClassID" value="${applyForm.devAllocateDevice.device.deviceClass.id }"/>
        	
		</td>
        <td class="tit">设备编号</td>
        <td class="field"><span id="deviceNO">${applyForm.devAllocateDevice.device.deviceNO }</span></td>
        <td class="tit">设备型号</td>
        <td class="field"><span id="deviceModel">${applyForm.devAllocateDevice.device.deviceModel }</span></td>
	</tr>
	<tbody id="showInfo">
      <tr>
        <td class="tit"><span class="cRed">*</span>调入经办人</td>
        <td>
        	<input id="inAccountName" type="text" maxlength="50" class="ipt05" value="${applyForm.inApplicantDisplayName }" style="width:106px" readonly /><input id="openInAccountSelect" class="selBtn" type="button"/>
   			<input id="inAccountID" type="hidden" value="${applyForm.inAccountID }" />
   			
        </td>
        <td class="tit">调入部门</td>
        <td class="field"><span id="inGroupName">${applyForm.inGroupName }</span></td>
        <td class="tit"><span class="cRed">*</span>调入设备区域</td>
        <td class="field"><div style="width:139px">
            <div id="areaSel" name="areaSel">
            </div>
          </div></td>
      </tr>
     </tbody>
      <tr>
        <td class="tit"><span class="cRed">*</span>调出经办人</td>
        <td>
        	<span id="applicantName">${applyForm.applicantDisplayName }</span>
   			<input id="applicantID" type="hidden" value="${applyForm.applicant }" ></input>
        </td>
        <td class="tit">调出部门</td>
        <td class="field"><span id="applyGroupName">${applyForm.applyGroupName }</span></td>
        <td class="tit">调出设备区域</td>
        <td class="field"><span id="areaCodeBef" style="display:none">${applyForm.devAllocateDevice.areaCodeBef}</span>
        	<span id="areaNameBef">${applyForm.devAllocateDevice.areaNameBef}</span>
        </td>
      <tr>
        <td class="tit">调拨原因</td>
        <td width="600" colspan="5"><textarea id="reason" class="area01">${applyForm.reason }</textarea></td>
      </tr>
      
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="保 存" class="allBtn" onclick="saveInfo()"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn" onclick="doclose()"/>
  </div>
</div>
</body>
