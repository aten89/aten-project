<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form name="moduleFrm" action="" onSubmit="return false;">
<input type="hidden" id="hidSubSystemId" value="${module.subSystem.subSystemID}" />
<input type="hidden" id="hidModuleId" value="${module.moduleID}" />
<input type="hidden" id="hidParentModuleIdOld" value="${module.parentModule.moduleID}"/>	
<!--系统模块 修改-->
<div class="addCon">
	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="tabNone">
		<tr>
			<td class="tit">所属系统</td>
			<td id="subSystemName">${module.subSystem.name}</td>
			<td class="tit">父模块</td>
			<td><input type="hidden" id="hidParentModuleId" value="${module.parentModule.moduleID}"/>
				<input id="parentModuleName" readonly type="text" value="${module.parentModule.name}" class="ipt01" style="width:86%"/>
				<button id="showParentModule" class="selBtn"></button></td>
		</tr>
		<tr>
			<td class="tit">模块名称</td>
			<td><input name="Input2" id="moduleName" readonly type="text" value="${module.name}" class="ipt01"  maxlength="50"/></td>
			<td class="tit">模块代码</td>
			<td><input name="Input3" id="moduleKey" readonly type="text" value="${module.moduleKey}" class="ipt01" maxlength="18"/></td>
		</tr>
		<tr>
			<td class="tit">引用模块</td>
			<td colspan="3"><input type="hidden" id="hidQuoteModuleId" value="${module.quoteModule.moduleID}"/>
			<input value="${module.quoteModulePath}" readonly id="quoteModuleName" type="text" class="ipt01" maxlength="200" style="width:94%"/>
			<button id="showQuoteModule" class="selBtn"></button></td>
		</tr>
		<tr>
			<td class="tit">模块地址</td>
			<td colspan="3"><input value="${module.url}" readonly id="moduleUrl" type="text" class="ipt01" maxlength="200"/></td>
		</tr>
		<tr>
			<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
			<td colspan="3"><textarea id="moduleRemark" readonly class="area01">${module.description}</textarea></td>
		</tr>
	</table>
</div>
<div class="addTool2">
	<input id="saveModule" type="button" value="保存" class="allBtn" />
	<input id="saveAddModule" type="button" disabled value="保存并新增" class="allBtn" />
	<input id="resetModule" type="reset" value="重置" class="allBtn" />
</div>
</form>
<script type="text/javascript" src="page/systemconf/module/edit_mod.js"></script>
<script type="text/javascript" src="page/dialog/dialog.module.js"></script>
