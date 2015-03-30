<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<style>
	.file {position:absolute;top:85px;right:200px;+top:0;+right:73px;width:0px;height:30px;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;}
#imgDiv img{border:1px solid #eee; padding:2px}
</style>
<script type="text/javascript" src="page/address/alladdr/upload_hd.js"></script>

</head>
<body style="background:none; margin:0; padding:0; overflow:hidden" class="bdNone">
	<form id="formHD" name="formHD" action="m/addrlist?act=uploadhd" method="post" enctype="multipart/form-data" onsubmit="">		
		<div id="imgDiv" style="float:left"></div>	
		<input type="hidden" id="code" name="code" value="${code }" />
		<input type="hidden" id="msg" name="msg" value="${msg }" />
		<input type="hidden" id="tempImgPath" name="tempImgPath" value="${tempImgPath }" />
		<input type="button" onmouseover="fclick(fileHD);" onclick="$('#fileHD').click()" value="浏览" class="allBtn" title="单击可更换头像" style="float:left;margin:88px 0 0 6px"/>
		<input type="file" name="fileHD" id="fileHD" onchange="postFile()" class="file" />
	</form>
</body>
</html>
