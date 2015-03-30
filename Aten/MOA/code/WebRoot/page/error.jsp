<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="base_path.jsp"></jsp:include>
<title>错误信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body">
    <div class="blank" style="height:60px"></div>
	<div class="error">
		  <div style="width:318px; height:168px; position:absolute; top:38px;left:298px;overflow:auto;">${REQUEST_ERROR_MSG }</div>
		  <a href="logout?act=exit" style="position:absolute; top:215px; left:538px;"></a>
	</div>
</body>
</html>
