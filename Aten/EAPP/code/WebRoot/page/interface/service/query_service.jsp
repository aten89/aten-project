<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/interface/service/query_service.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="add_btn" id="addFwpz" r-action="add"/>
	  <input type="button" class="flash_btn" id="refresh"/>
	  <input type="button" class="bdjrzh_btn" id="bindJrzh" r-action="bindactor"/>
	  <input type="button" class="bdmk_btn" id="bindMkdz" r-action="bindright"/>
  </div>
</div>
<!--工具栏 end-->
<div class="blank3"></div>
<!--搜索-->
<div class="soso">
  <div class="t01">
  <table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td>
			服务名称：<input id="serviceName" name="serviceName" maxlength="40" type="text" class="ipt05"  style="width:100px"/>
			<input id="searchService" name="" type="button" value="搜索" class="allBtn" r-action="query"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--搜索 end-->
<!--列表-->
<div class="allList">
  <table id="service" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
		<th width="5%">序号</th>
		<th width="18%">服务名称</th>
		<th width="8%">是否有效</th>
		<th width="54%">服务说明</th>
		<th width="15%">操作</th>
	  </tr>
  </thead>
  <tbody id="data_list">
  </tbody>
</table>
</div>
<!--翻页-->
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>