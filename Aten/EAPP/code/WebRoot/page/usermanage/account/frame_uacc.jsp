<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>
<script type="text/javascript" src="page/usermanage/account/frame_uacc.js"></script>
</head>
<body class="bdNone">
<input type="hidden" id="hidAction" value="${param.action}" />
<input type="hidden" id="hidAccountId" value="${param.accountId}" />
<input type="hidden" id="hidDisplayName" value="" />
<input type="hidden" id="hidIsLock" value="" />
<div class="addTool tabMid2" id="jkzhTool"> 
	<div class="t01">
		<input type="button" class="add_btn" id="addUserAcc" r-action="add"/>
		<input type="button" class="edit_btn" id="modUserAcc" r-action="modify"/>		
		<!-- <input type="button" class="del_btn" id="delUserAcc"/> -->
		<input type="button" class="tjjs_btn"  id="bindRole" r-action="bindrole"/>
		<input type="button" class="bdjg_btn" id="bindOrg" r-action="bindgroup"/>
        <input type="button" class="mmzz_btn" id="resetPwd" r-action="setpassword"/>			
	</div>	
</div>
<form name="mainForm" id="mainForm" onsubmit="return false;"></form>
</body>
</html>