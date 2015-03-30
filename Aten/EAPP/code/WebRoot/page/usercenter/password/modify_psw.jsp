<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/usercenter/password/modify_psw.js"></script>
</head>
<body class="bdNone" style="min-width: 480px;">
<div class="blank" style="height:30px"></div>
<div id="jkzhMain">
<!--密码修改-->
<div class="addCon">
  <table width="360"  border="0" align="center" cellpadding="0"  cellspacing="1" style="margin:0 auto">
  <tr>
	<th colspan="2">
	密码修改
	</th>
  </tr>
  <tr>
	<td class="tit">输入旧密码</td>
	<td><input id="oldPwd" name="Input" type="password" class="ipt01" maxlength="32"/></td>
  </tr>
  <tr>
	<td class="tit">输入新密码</td>
	<td><input id="newPwd" name="Input" type="password" class="ipt01" maxlength="32" /></td>
  </tr>
  <tr>
	<td class="tit">新密码确认</td>
	<td><input id="newPwdAg" name="Input" type="password" class="ipt01" maxlength="32"/></td>
  </tr>
</table>
</div>
<div class="addTool2">
    <input id="savePwd" name="" type="button" value="保存" class="allBtn" />
	<input type="button" value="重置" class="allBtn" id="reset" />
</div>
<!--密码修改 end-->
</div>
</body>
</html>
