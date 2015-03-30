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
<script type="text/javascript" src="page/device/allocation/edit_allo.js"></script>

</head>
<body class="oppBd">
<input id="id" type="hidden" value="${applyForm.id }" />
<input id="deviceTypeCode" type="hidden" value="${applyForm.deviceType }" />
<input id="moveType" type="hidden" value="${applyForm.moveType }" />
<input type="hidden" id="mainDevCfgs" value="<c:forEach items="${areaDeviceCfgs  }" var="list">${list.areaCode },${list.deviceClass.id }|</c:forEach>" />
<div class="oppConScroll" style="min-height:400px">
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备调拨单</th>
      </tr>
      <tr>
        <td class="tit">调出人</td>
        <td class="field">${applyForm.applicantDisplayName }</td>
        <td class="tit">调出部门</td>
        <td class="field">${applyForm.applyGroupName }</td>
        <td class="tit">申请日期</td>
        <td class="field"><span id="nowDate"><fmt:formatDate value="${applyForm.applyDate }" pattern="yyyy-MM-dd"/></span></td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed">*</span>资产类别</td>
        <td class="field"><div style="width:139px">
            <div id="deviceTypeSel" name="deviceTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>调拨类型</td>
        <td class="field"><div style="width:139px">
            <div id="allotTypeSel" name="allotTypeSel">
            </div>
          </div></td>
        <td class="tit"><span class="cRed">*</span>调拨日期</td>
        <td><input type="text" id="moveDate" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${applyForm.moveDate }" pattern="yyyy-MM-dd"/>" /></td>
      </tr>
      <tr id="trInInfo">
        <td class="tit"><span class="cRed">*</span>调入经办人</td>
        <td>
			<input id="inAccountName" type="text" maxlength="50" class="ipt05" value="${applyForm.inApplicantDisplayName }" style="width:99px" readonly />
   			<input id="inAccountID" type="hidden" value="${applyForm.inAccountID }" />
   			<input id="openInAccountSelect" class="selBtn" type="button"/>
		</td>
        <td class="tit"><span class="cRed">*</span>调入部门</td>
        <td class="field" colspan="3"><span id="inGroupName">${applyForm.inGroupName }</span></td>
      </tr>
      <tr id="sgsm">
        <td class="tit"><span class="cRed">*</span>调拨原因</td>
        <td colspan="5"><textarea id="reason" class="area01">${applyForm.reason }</textarea></td>
      </tr>
      </table>
  </div>
  <!--设备清单-->
  <div id="devListBody">
  <div class="blank" style="height:3px"></div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
          <div class="costsListHead">
            <div style="width:100%"><label  style="float:left">设备清单</label><a id="gmcpAdd" style=" float:right;font-weight:normal; cursor:pointer"><img src="themes/comm/images/customBtn.gif" />选择设备</a></div>
          </div>
        </td>
      </tr>
  </table>
  <c:forEach items="${applyForm.devAllocateLists  }" var="devAllocateList">
  <div class="feesSp fsMar">
    <table cellspacing="0" cellpadding="0" border="0" width="100%" >
      <tr>
        <td class="spNum"><span name="orderNum">${devPurchaseList.displayOrder }</span></td>
        <td colspan="6" class="spOpW opw2">
            <div>
                <a href="javascript:void(0)" onclick="delItem(this)"><img src="themes/comm/images/spDel.gif" />删除</a>
            </div>
        </td>
      </tr>
      <tr>
      	<td height="27" class="spTit" style="width:auto">设备编号：</td>
        <td width="128"><span name="deviceNO">${devAllocateList.device.deviceNO }</span></td>
        <td height="27" class="spTit" style="width:auto">设备名称：</td>
        <td width="150"><span name="deviceName">${devAllocateList.device.deviceName }</span><input type="hidden" name="deviceID" value="${devAllocateList.device.id }" />
        	<input type="hidden" name="listDevBuyType" value="${devAllocateList.device.buyType }"/>
        	<input type="hidden" name="listDevBuyTypeName" value="${devAllocateList.device.buyTypeName }"/>
        	<input type="hidden" name="listAreaName" value="${devAllocateList.device.areaName }"/>
            <input type="hidden" name="listAreaCode" value="${devAllocateList.device.areaCode }"/>
        </td>
        <td width="70" class="spTit">设备型号：</td>
        <td width="180">${devAllocateList.device.deviceModel }</td>
        <td rowspan="4"  class="spOpW">&nbsp;</td>
      </tr>
      <c:if test="${!empty devAllocateList.device.devicePropertyDetails}">
      <tr>
        <td class="spTit" style="width:auto">配置信息：</td>
        <td colspan="5" width="360"><span name="devCfgDesc">${devAllocateList.device.configList }</span></td>
      </tr>
      </c:if>
      <tr>
      	<td class="spTit">设备类别：</td>
        <td><span name="listDeviceClassName">${devAllocateList.device.deviceClass.name }</span><input type="hidden" name="listDeviceClassID" value="${devPurchaseList.device.deviceClass.id }"/></td>
        <td  class="spTit" style="width:136px">调拨前工作所在地：</td>
        <td colspan="3"><span name="areaCodeBef" style="display: none">${devAllocateList.device.deviceCurStatusInfo.areaCode }</span>${devAllocateList.device.deviceCurStatusInfo.areaName }</td>
      </tr>
    </table>
  </div>
  </c:forEach>
  <!--设备清单 end-->
  </div>
</div>
<div class="oppBtnBg">
  <div>
    <input id="saveBtn" type="button" value="提 交" class="allBtn"/>
    <input id="draftBtn" type="button" value="保存为草稿" class="allBtn"/>
    <input id="closeBtn" type="button" value="关 闭" class="allBtn"/>
  </div>
</div>
</body>
