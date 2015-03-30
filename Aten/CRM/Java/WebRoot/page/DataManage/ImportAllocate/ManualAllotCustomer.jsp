<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/DataManage/ImportAllocate/ManualAllotCustomer.js"></script>

<title>选择销售员工</title>
</head>
<body class="tabBd">
	<!--选择销售员工start-->
	<div class="tabScroll" style="overflow-x: hidden">
		<div class="addCon" align="center">
			<table border="0" width="100%" cellpadding="0" cellspacing="1" align="center">
				<tr>
					<td class="tit">销售部门：</td>
		 			<td ><div id="saleGroupSel" name="saleGroupSel"></div></td>
		 		</tr>
				<tr>
					<td class="tit">销售人员：</td>
		 			<td ><div id="saleStaffSel" name="saleStaffSel"></div></td>
				</tr>
			</table>
		</div>
	</div>
	<!--选择销售员工end-->
	<div class="oppBtnBg tabBottom">
		<input class="allBtn" id="save" type="button" value="确定"/>
		<input class="allBtn" id="cancel" type="button" value="取消"/>
	</div>
</body>
</html>

