<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" style="min-width:240px;">
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">基本信息</td></tr>
	<tr>
		<td class="tit"><span class="cReq">*</span>节点名称</td>
		<td><input type="text" id="attr_name" class="ipt01"/></td>
	</tr>
	<tr>
		<td class="tit">节点说明</td>
		<td><textarea id="attr_description" class="area01"></textarea></td>
	</tr>
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">分支条件</td></tr>
	<tr>
		<td class="tit">分支线名</td>
		<td>
			<div id="attr_toLineName" style="display:none"></div>
		</td>
	</tr>
	<tr>
		<td class="tit">执行表达式</td>
		<td><textarea id="attr_toLineExpression" class="area01"></textarea><br/><input onclick="editExpression('attr_toLineExpression', 'boolean')" type="button" value="编辑" class="allBtn" style="margin-top:2px"/></td>
	</tr>
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">事件监听</td></tr>
	<tr>
		<td class="tit">进入节点事件</td>
		<td><div id="attr_nodeEnterEvent"></td>
	</tr>
	<tr>
		<td class="tit">离开节点事件</td>
		<td><div id="attr_nodeLeaveEvent"></td>
	</tr>
</table>