<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title>设备维修单</title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/device/manage/repa_dev.js"></script>

</head>
<body class="oppBd">
<input type="hidden" id="deviceTypeCode" value="${devRepairForm.deviceTypeCode }" />
<input type="hidden" id="deviceID" value="${param.deviceID }" />
<input type="hidden" id="areaCode" value="${param.areaCode }" />
<input type="hidden" id="buyTime" value="<fmt:formatDate value="${devRepairForm.device.buyTime}" pattern="yyyy-MM-dd"/>" />
<input type="hidden" id="owner" value="${devRepairForm.device.deviceCurStatusInfo.owner }" />
<div class="oppConScroll">
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备维修单</th>
      </tr>
      <tr>
        <td class="tit">设备类型</td>
        <td width="180">${devRepairForm.deviceTypeDisplayName }</td>
        <td class="tit">登记人</td>
        <td class="field">${devRepairForm.regAccountDisplayName }</td>
        <td class="tit">登记日期</td>
        <td class="field"><fmt:formatDate value="${devRepairForm.createTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>设备名称</td>
        <td >
        	<input id="deviceName" type="text" class="ipt05" readonly="true" style="width: 120px" value="${devRepairForm.device.deviceName }"/> 
        	<input id="deviceID" type="hidden"  class="ipt05" value="${devRepairForm.device.id }"/>
        	<input id="btnSelectDevice" type="button" class="selBtn" />
        </td>
        <td class="tit">设备编号</td>
        <td class="field"><span id="deviceNO">${devRepairForm.device.deviceNO }</span> </td>
        <td class="tit"><span class="cRed">*</span>预算费用</td>
        <td><input id="budgetMoney" type="text" class="ipt01" style="width:99px" value=""/>
          元</td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>申请人</td>
        <td> 
        	<input id="applicantName" type="text" maxlength="50" class="ipt05" value="${devRepairForm.accountDisplayName }" style="width:120px" readonly />
   			<input id="userId" type="hidden" value="${devRepairForm.accountID }" />
   			<input id="openUserSelect" class="selBtn" type="button"/>
   		</td>
        <td class="tit">所在部门</td>
        <td class="field"><span id="deptName">${devRepairForm.groupName }</span></td>
        <td class="tit"><span class="cRed">*</span>申请日期</td>
        <td class="field"><input id="applyTime" type="text" maxlength="100" readonly="readonly" class="invokeBoth" value="<fmt:formatDate value="${devRepairForm.applyTime }" pattern="yyyy-MM-dd"/>" /></td>
      </tr>
      <tr>
        <td class="tit">损坏原因</td>
        <td width="600" colspan="5"><textarea id="remark" class="area01"></textarea></td>
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
