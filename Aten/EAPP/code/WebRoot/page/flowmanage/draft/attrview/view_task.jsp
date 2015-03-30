<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" style="min-width:240px;">
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">基本信息</td></tr>
	<tr>
		<td class="tit"><span class="cReq">*</span>任务名称</td>
		<td><input type="text" id="attr_name" class="ipt01"/></td>
	</tr>
	<tr>
		<td class="tit">任务类型</td>
		<td>
			<div id="attr_multiType" style="display:none">
				<div>single**单人</div>
				<div>parallel**多人并行</div>
				<div>serial**多人串行</div>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tit">任务说明</td>
		<td><textarea id="attr_description" class="area01"></textarea></td>
	</tr>
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">任务授权</td></tr>
	<tr>
		<td class="tit">授权方式</td>
		<td>
			<div id="attr_assignType" style="display:none">
				<div isselected="true">ACTORS**用户</div>
				<div>ROLES**角色</div>
				<div>EXP**表达式</div>
				<div>ACTION**程序类</div>
			</div>
		</td>
	</tr>

	<tr id="TR_ACTORS">
		<td class="tit"><span class="cReq">*</span>用户帐号<br/>(多个用 , 分开)</td>
		<td><textarea id="attr_assignActors" class="area01" style="height:36px"></textarea></td>
	</tr>
	<tr id="TR_ROLES" style="display:none">
		<td class="tit"><span class="cReq">*</span>角色名称<br/>(多个用 , 分开)</td>
		<td><textarea id="attr_assignRoles" class="area01" style="height:36px"></textarea></td>
	</tr>
	<tr id="TR_EXP" style="display:none">
		<td class="tit"><span class="cReq">*</span>执行表达式</td>
		<td><textarea id="attr_assignExpression" class="area01"></textarea><br/><input onclick="editExpression('attr_assignExpression', 'string')" type="button" value="编辑" class="allBtn" style="margin-top:2px"/></td>
	</tr>
	<tr id="TR_ACTION" style="display:none">
		<td class="tit"><span class="cReq">*</span>执行程序</td>
		<td><div id="attr_assignAction"></td>
	</tr>
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">任务表单</td></tr>
	<tr>
		<td class="tit">绑定类型</td>
		<td id="viewTypeTD"><input type="radio" name="viewType" value="exp" checked onclick="$('#vexpDiv').show();$('#vactionDiv').hide();"/> 表达式&nbsp;&nbsp;
			<input type="radio" name="viewType" value="action" onclick="$('#vexpDiv').hide();$('#vactionDiv').show();"/> 程序类</td>
	</tr>
	<tr id="vexpDiv">
		<td class="tit"><span class="cReq">*</span>执行表达式</td>
		<td><textarea id="attr_viewExpression" class="area01"></textarea><br/><input onclick="editExpression('attr_viewExpression', 'string')" type="button" value="编辑" class="allBtn" style="margin-top:2px"/></td>
	</tr>
	<tr id="vactionDiv" style="display:none">
		<td class="tit"><span class="cReq">*</span>执行程序</td>
		<td><div id="attr_viewAction"></td>
	</tr>
	<tr><td class="tit" colspan="2" style="text-align:center;font-weight:bold;">事件监听</td></tr>
	<tr>
		<td class="tit">任务创建事件</td>
		<td><div id="attr_taskCreateEvent"></td>
	</tr>
	<tr>
		<td class="tit">任务分配事件</td>
		<td><div id="attr_taskNotifyEvent"></td>
	</tr>
	<tr>
		<td class="tit">任务认领事件</td>
		<td><div id="attr_taskAssignEvent"></td>
	</tr>
	<tr>
		<td class="tit">任务开始事件</td>
		<td><div id="attr_taskStartEvent"></td>
	</tr>
	<tr>
		<td class="tit">任务放弃事件</td>
		<td><div id="attr_taskGiveupEvent"></td>
	</tr>
	<tr>
		<td class="tit">任务结束事件</td>
		<td><div id="attr_taskEndEvent"></td>
	</tr>

</table>