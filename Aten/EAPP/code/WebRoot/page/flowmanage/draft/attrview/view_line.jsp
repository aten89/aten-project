<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" style="min-width:240px;">
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">基本信息</td></tr>
	<tr>
		<td class="tit"><span class="cReq">*</span>路径名称</td>
		<td><input type="text" id="attr_name" class="ipt01"/></td>
	</tr>
	<tr>
		<td class="tit">执行程序</td>
		<td><div id="attr_action"></td>
	</tr>
	<tr>
		<td class="tit">通过条件<br/>(表达式)</td>
		<td><textarea id="attr_condition" class="area01"></textarea><br/><input onclick="editExpression('attr_condition','boolean')" type="button" value="编辑" class="allBtn" style="margin-top:2px"/></td>
	</tr>
	<tr>
		<td class="tit">路径说明</td>
		<td><textarea id="attr_description" class="area01"></textarea></td>
	</tr>
</table>