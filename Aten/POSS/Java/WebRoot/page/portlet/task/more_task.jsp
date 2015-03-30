<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript" src="page/portlet/task/more_task.js"></script>
</head>
<body class="bdNone" >

   <!--列表-->
<div class="allList">
  <div class="addTool sortLine"><div class="t01">
	<input id="refresh" type="button" class="flash_btn"/>
  </div></div>
  <table id="zxt" width="100%" style="margin:-1px 0 0" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
	  	<th width="6%">序号</th>
	    <th width="30%">任务描述</th>
	    <th width="18%">流程类别</th>
		<th width="20%">任务名称</th>
		<th width="20%">收到时间</th>
		<th width="6%">操作</th>
	  </tr>
  </thead>
  <tbody id="taskList">
  </tbody>
</table>
</div>
<!--翻页 end-->
<!--列表 end-->
<div class="pageNext">
</div>
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="16"/> 

</body>
</html>