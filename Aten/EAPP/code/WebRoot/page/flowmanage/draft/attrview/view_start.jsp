﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" style="min-width:240px;">
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">基本信息</td></tr>
	<tr>
		<td class="tit"><span class="cReq">*</span>节点名称</td>
		<td id="attr_name"></td>
	</tr>
	<tr>
		<td class="tit">节点说明</td>
		<td><textarea id="attr_description" class="area01"></textarea></td>
	</tr>
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">事件监听</td></tr>
	<tr>
		<td class="tit">离开节点事件</td>
		<td><div id="attr_nodeLeaveEvent"></td>
	</tr>
</table>