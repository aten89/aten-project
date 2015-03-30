<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/flowmanage/draft/dialog_json.js"></script>
</head>
<body class="bdDia">
<div class="dialogBk">
	<textarea id="flowJsonData" class="area01" style="width:633px;height:314px;"></textarea>
</div>
<div class="addTool2" id="but01">
    <input type="button" id="comitBtn" class="allBtn" value="确 定"/>
	<input type="button" id="cancelBtn" class="allBtn" value="取 消"/>
</div>
</body>
</html>