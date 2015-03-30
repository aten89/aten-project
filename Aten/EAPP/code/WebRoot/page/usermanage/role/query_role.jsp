<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/usermanage/role/query_role.js"></script>

</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool sortLine">
  <div class="t01">
	  <input type="button" class="add_btn" id="addRBACRole" r-action="add"/>
	  <input type="button" class="flash_btn" id="refreshRBACRole" />
	  <input type="button" id="bindRBACRoleGroup" class="bdjg_btn" r-action="bindgroup"/>
	  <input type="button" id="bindRBACRoleUser" class="bdyf_btn" r-action="binduser"/>
	  <input type="button" id="bindRBACRoleModule" class="bdmk_btn" r-action="bindright"/>
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
			角色名称：<input id="rolename" name="rolename" maxlength="40" type="text" class="ipt05"  style="width:100px"/>
			<input id="searchRole" type="button" value="搜索" class="allBtn"  r-action="query"/>
		</td>
	  </tr>
  </table>
  </div>
</div>
<!--搜索 end-->
<!--列表-->
<div class="allList">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	  <tr>
		<th width="5%">序号</th>
		<th width="18%">角色名称</th>
		<th width="8%">是否有效</th>
		<th width="54%">角色说明</th>
		<th width="15%">操作</th>
	  </tr>
  </thead>
  <tbody id="data_list"></tbody>
</table>
</div>
<!--翻页-->
<div class="pageNext">
</div>
<!--翻页 end-->
<!--列表 end-->
</body>
</html>