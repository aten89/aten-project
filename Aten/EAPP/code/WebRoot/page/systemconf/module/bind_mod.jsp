<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<form action="" onSubmit="return false;">
<!--系统模块 动作绑定-->
<div class="addCon">
	<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0" class="tabNone">
	  <tr>
		<td valign="top">
		   <div  class="dzbd">
			 <table  align="center" border="0" cellspacing="0" cellpadding="0" class="tabNone" style="margin:0 auto">
			  <tr>
				<td valign="top">
				   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabNone">
					  <tr>
						<td valign="top" width="255" style="height:305px; padding:0px 0 0">
						 <div class="mkdzTip" style="padding:3px 0 3px 10px">已绑定动作</div>
						 <div class="mkdz" style=" border:none">
						 <select id="includeActions" size="1" multiple="multiple" class="dzbdSel01" style="height:260px; width:255px">
<c:forEach var="act" items="${actions}" >						
						<option value="${act.actionID}">${act.name}</option>
</c:forEach>
						</select>
						 </div>
						</td>
						<td style="min-width:10px">
						   &nbsp;<input id="btnAddAction" type="button" value="添加&lt;&lt;" class="allBtn"/><br />
						   &nbsp;<input id="btnDelAction" type="button" value="删除&gt;&gt;" class="allBtn" style="margin:6px 0"/>
						</td>
						<td valign="top" width="255"  style="height:305px; padding:0px 0 0">
						<div class="mkdzTip">未绑定动作：<input id="txtFilterAction" name="txtFilterAction" type="text" class="ipt01"  style="width:100px" value=""/> <input id="searchAction" type="button" value="搜索" class="allBtn subsoW" /></div>
						<div class="mkdz" style=" border:none">
						<select id="excludeActions" size="1" multiple="multiple" class="dzbdSel01"  style="height:260px; width:255px">
						</select>
						<input type="hidden" id="hidExcludeActions" value=""/>
						</div>
						</td>
					  </tr>
					</table>
				</td>
			  </tr>
			 </table>
		   </div>
		</td>
	  </tr>
	 </table>
</div>
<div class="addTool2" >
	<input id="saveBindActions" type="button" value="保存" class="allBtn"/>
</div>
</form>
<!--系统模块 动作绑定 end-->
<script type="text/javascript" src="page/systemconf/module/bind_mod.js"></script>