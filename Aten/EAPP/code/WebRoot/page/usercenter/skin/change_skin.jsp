<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/usercenter/skin/change_skin.js"></script>

</head>
<body class="bdNone">
<input type="hidden" id="currentStyleThemes" value="${sessionUser.styleThemes }" />
<!--皮肤定制-->
<div class="shinCustom" id="shinCustom">
  <h1 class="headDefault">皮肤定制</h1>
  <div class="mb" id="thremList">
      <ul>
		 <li><img src="themes/comm/images/skinFlat.gif" /><input id="flat" name="threm" type="radio" value="flat"/>扁平</li>
		 <li><img src="themes/comm/images/skinOffic.gif" /><input id="blue" name="threm" type="radio" value="blue"/>蓝色</li>
		 <li><img src="themes/comm/images/skinOrange.gif" /><input id="orange" name="threm" type="radio" value="orange"/>橙色</li>
		 <li><img src="themes/comm/images/skinGreen.gif" /><input id="green" name="threm" type="radio" value="green"/>绿色</li>
	  </ul>
  </div>
</div>
<!--皮肤定制 end-->
</body>
</html>
