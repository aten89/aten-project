<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>预约弹出框</title>
	<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
	<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
	<script type="text/javascript" src="page/jquery-calendar.js"></script>
	<script type="text/javascript" src="page/MyCustomer/CustomerInfo/editAppointmentRecord.js"></script>
</head>
<body class="bdDia">
<input type="hidden" id="idHid" value="${customerAppointment.id }"/>
<input type="hidden" id="appointmentTypeHid" value="${customerAppointment.appointmentType }"/>
<input type="hidden" id="customerId" value="${param.customerId }"/>
<div>
	<table border="0" width="100%" cellpadding="0" cellspacing="1">
		<tr>
			<td class="tit" align="right"><span class="cRed">*</span>预约时间：</td>
			<td><input readonly id="appointmentTime" type="text" class="ipt01" value="${customerAppointment.appointmentTimeStr }" style="width: 120px" /></td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td class="tit" align="right"><span class="cRed">*</span>提醒方式：</td>
			<td><div id="appointmentTypeSel" name="appointmentTypeSel"></div></td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
	 	<tr>
			<td class="tit" align="right"><span class="cRed">*</span>提前多久通知（分钟）：</td>
			<td><input id="warnOpportunity" type="text" maxlength="40" class="ipt05" value="${customerAppointment.warnOpportunity }"/></td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td class="tit" align="right"><span class="cRed">*</span>预约说明：</td>
			<td><input id="remark" type="text" maxlength="40" class="ipt05" value="${customerAppointment.remark }"/></td>
		</tr>
	 	
	 </table>
</div>
	<div class="blank"></div>
	<div class="oppBtnBg">
		<input id="saveBtn" type="button" value="保存" class="allBtn" />
		<input id="doclose" type="button" value="取消" class="allBtn" />
	</div>
</body>
</html>