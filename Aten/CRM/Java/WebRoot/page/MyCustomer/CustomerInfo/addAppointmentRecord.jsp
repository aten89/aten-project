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
	<script type="text/javascript" src="page/MyCustomer/CustomerInfo/addAppointmentRecord.js"></script>
</head>
<body>
<div>
	<table border="0" width="100%" cellpadding="0" cellspacing="1">
	 	<tr>
			<td class="tit" align="right"><span class="cRed">*</span>预约时间：</td>
			<td><input readonly id="appointmentTime" type="text" class="ipt01" value="" style="width: 125px" /></td>
		</tr>
	 	<tr>
			<td class="tit" align="right"><span class="cRed">*</span>提前多久通知（分钟）：</td>
			<td><input id="warnOpportunity" type="text" maxlength="40" class="ipt05" /></td>
		</tr>
	 	<tr>
			<td class="tit" align="right"><span class="cRed">*</span>提醒方式：</td>
			<td><div id="appointmentTypeSel" name="appointmentTypeSel"></div></td>
		</tr>
	 	<tr>
			<td class="tit" align="right"><span class="cRed">*</span>预约备注：</td>
			<td><input id="remark" type="text" maxlength="40" class="ipt05" /></td>
		</tr>
		<tr></tr>
	 </table>
</div>
	<div class="blank"></div>
	<div class="oppBtnBg tabBottom">
		<input id="saveBtn" type="button" value="保存" class="allBtn" />
		<input id="doclose" type="button" value="取消" class="allBtn" />
	</div>
</body>
</html>