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
<script type="text/javascript" src="page/EquipmentManagement/EquipmentInformation/EquipmentInfoRepair.js"></script>
<style>
html {
	overflow:hidden
}
body {
	overflow:hidden
}
</style>
</head>
<body class="bdNone">
<div class="tabMid">
  <!--故障编辑 end-->
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="6"><img src="themes/comm/images/frameNav2.gif"/>设备维修单</th>
      </tr>
      <tr>
        <td class="tit">设备类型</td>
        <td width="180">${form.device.deviceClass.name }</td>
        <td class="tit">登记人</td>
        <td class="field">${form.regAccountDisplayName }</td>
        <td class="tit">登记日期</td>
        <td class="field"><fmt:formatDate value="${form.createTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>设备名称</td>
        <td >
        	${form.device.deviceName }
        </td>
        <td class="tit">设备编号</td>
        <td class="field"><span id="deviceNO">${form.device.deviceNO }</span> </td>
        <td class="tit"><span class="cRed">*</span>预算费用</td>
        <td><fmt:formatNumber value="${form.budgetMoney }" pattern="0.00"/>元</td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed">*</span>申请人</td>
        <td class="field"> 
        	${form.accountDisplayName }
   		</td>
        <td class="tit">所在部门</td>
        <td class="field"><span id="deptName">${form.groupName }</span></td>
        <td class="tit"><span class="cRed">*</span>申请日期</td>
        <td class="field"><fmt:formatDate value="${form.applyTime }" pattern="yyyy-MM-dd"/></td>
      </tr>
      <tr>
        <td class="tit">损坏原因</td>
        <td width="600" colspan="5">${form.remark }</td>
      </tr>
    </table>
  </div>
  <!--故障编辑 end-->
</div>
</body>
