<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/MyCustomer/CustomerInfo/addConsultRecord.js"></script>
</head>
<body class="crmBd">
<div>
  <table width="100%" border="0" align="left" cellpadding="0"  cellspacing="1">
    <tr>
		<td class="tit"><div><span class="cRed">*</span>客户咨询内容</td>
	</tr>
		<tr>
			<td>
				<div>
					<textarea id="consultContent" class="area01"
							style="width: 560px; height: 180px" alt="consultContent"></textarea>
				</div>
			</td>
	 	</tr>
  </table>
  <div class="btTip"></div>
</div>

<div class="addTool2">
  <input type="button" id="saveBtn"  class="allBtn" value="确定"/>&nbsp;&nbsp;
  <input type="button" id="doclose" class="allBtn" value="取消"/>
</div>
</body>
</html>