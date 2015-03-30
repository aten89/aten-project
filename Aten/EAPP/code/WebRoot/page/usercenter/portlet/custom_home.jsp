<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="jqueryui/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="jqueryui/jquery.ui.droppable.js"></script>

<script type="text/javascript" src="page/usercenter/portlet/custom_home.js"></script>

</head>
<body class="bdNone" onselectstart="return false;" style="-moz-user-select:none;">
<div class="blank"></div>
<!--门户定制-->
<div class="portletConfig">
<form onsubmit="return false;">
  <div class="portletLeft">
	  <h1 class="headDefault">当前显示模块</h1>
	  <div class="portPadd" id="_customPortletDIV">
		  <div class="portMain" style="width:516px;margin:5px auto">
		  </div>
	  </div>
  </div>
</form>
  <div class="portletRight">
	  <h1 class="headDefault">未定制的模块</h1>
	  <div class="portPadd2">
		  <div id="_noCustomPortletDIV" class="portMain" style="width:100%;height:96%;">
		  </div>
	  </div>
  </div>
</div>
<!--门户定制 end-->
<div class="addTool2">
    <input id="savePortletConfig" type="button" value="保存" class="allBtn" />
</div>
</body>
</html>