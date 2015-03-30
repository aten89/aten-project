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
<script type="text/javascript" src="page/systemconf/moduleservice/edit_mser.js"></script>
</head>
<body class="bdNone">
<form action="?" id="mainForm" onsubmit="return false;">
<!--模块动作-->
<div class="subFrame">
	<table width="100%" align="center" border="0" cellspacing="6" cellpadding="0">
	  <tr>
		<td valign="top">
			 <table align="center" style="margin:0 auto" border="0" cellspacing="0" cellpadding="0" >
			  <tr>
				<td>
				   <div  class="dzbd">
				   <table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td valign="top" width="250">
						 <div class="mkdzTip">
						 <table border="0" width="100%" cellspacing="0" cellpadding="0" style="background:none">
						  <tr>
							<td width="27%">子系统：</td>
							<td width="73%">
								<div id="subSystemList">
								</div>
							</td>
						  </tr>
						 </table>
						 </div>
						 <div class="optionLine">子系统模块</div>
						 <div class="mkdz">
							 <!--系统模块树-->
							 <div id="systemModulesTree">
								<ul class="simpleTree" style="height:300px; width:243px">
								</ul>
							 </div>
							 <!--系统模块树 end-->
						 </div>
						</td>
						<td align="center" width="30">&nbsp;</td>
						<td valign="top" width="330">
						<div class="mkdzTip mkdzH" style="padding:7px 0 0px 10px">动作设置</div>
						<div class="allList" >
						  <div class="dzScroll" style="height:327px; width:328px;overflow-x:auto;">
							  <table width="100%" id="moduleActions" border="0" cellspacing="0" cellpadding="0">
							  <thead>
								  <tr>
									<th width="40%">动作</th>
									<th width="20%" class="qx"><input id="validcheckbox" type="checkbox" value="checkbox" onclick="checkAllValid();" class="cBox02"/> 启用</th>
									<th width="20%" class="qx"><input id="httpcheckbox" type="checkbox" value="checkbox" onclick="checkAllHttp();" class="cBox02"/> HTTP</th>
									<th width="20%" class="qx"><input id="rpccheckbox" type="checkbox" value="checkbox" onclick="checkAllRpc();" class="cBox02"/> RPC</th>
								  </tr>
							  </thead>
							  <tbody>
							  </tbody>
							</table>
						  </div>
						</div>
						</td>
					  </tr>
					</table>
				   </div>
				</td>
			  </tr>
			 </table>
		</td>
	  </tr>
	 </table>
	 <div class="blank" style="height:12px"></div>
</div>
<!--模块动作 end-->
<div class="addTool2" >
<input id="saveModuleActions" type="button" value="保存" class="allBtn" r-action="modify"/>
<input id="resetModuleActions" type="button" value="重置" class="allBtn"/>
</div>
<input id="hidActionOptionsBak" type="hidden" value="" />
<input id="hidModuleId" name="moduleID" type="hidden" value="" />
</form>
</body>
</html>