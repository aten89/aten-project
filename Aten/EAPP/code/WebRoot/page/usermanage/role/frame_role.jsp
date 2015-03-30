<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>

<script type="text/javascript" src="page/usermanage/role/frame_role.js"></script>
</head>
<body class="bdNone">
<!--工具栏-->
<div class="addTool tabMid2">
  <div class="t01">
	  <input type="button" id="addRBACRole" class="add_btn" r-action="add"/>
	  <input type="button" id="modifyRBACRole" class="edit_btn" r-action="modify"/>
	  <input type="button" id="bindRBACRoleGroup" class="bdjg_btn" r-action="bindgroup"/>
	  <input type="button" id="bindRBACRoleUser" class="bdyf_btn" r-action="binduser"/>
	  <input type="button" id="bindRBACRoleModule" class="bdmk_btn" r-action="bindright"/>
  </div>
</div>
<!--工具栏 end-->
<form name="contetnMain" id="contetnMain" onsubmit="return false;"></form>
<input type="hidden" id="hidAction" value="${param.action}" />
<input type="hidden" id="hidRoleId" value="${param.roleid}" />
<input type="hidden" id="hidRoleName" value="" />
</body>
</html>