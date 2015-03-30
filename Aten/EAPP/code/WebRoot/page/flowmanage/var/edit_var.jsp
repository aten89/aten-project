<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--系统新增-->
<div class="addCon tabMid">
<input type="hidden" id="varId" value="${flowVar.varId }" />
<input type="hidden" id="flowClass" value="${flowVar.flowClass }" />
<input type="hidden" id="type" value="${flowVar.type }" />
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
  	<td class="tit">流程类别</td>
	<td><div id="flowClassDiv"></div></td>
	<td class="tit">变量名称</td>
	<td><input id="name" readonly type="text" value="${flowVar.name }" class="ipt01"  maxlength="50"/></td>
	<td class="tit">显示名称</td>
	<td><input type="text" id="displayName" readonly value="${flowVar.displayName }" class="ipt01" maxlength="50" /></td>
  </tr>
  <tr>
	<td class="tit">变量类型</td>
	<td><div id="typeDiv">
		<div>DATATYPE_STRING**DATATYPE_STRING</div>
		<div>DATATYPE_BOOLEAN**DATATYPE_BOOLEAN</div>
		<div>DATATYPE_INT**DATATYPE_INT</div>
		<div>DATATYPE_LONG**DATATYPE_LONG</div>
		<div>DATATYPE_FLOAT**DATATYPE_FLOAT</div>
		<div>DATATYPE_DOUBLE**DATATYPE_DOUBLE</div>
		<div>DATATYPE_DATE**DATATYPE_DATE</div>
	</div></td>
	<td class="tit">不能为空</td>
	<td id="notNullTD">
		<input name="notNull" type="radio" value="true" ${flowVar.notNull ? "checked" : ""}/>是&nbsp;&nbsp;&nbsp;
		<input name="notNull" type="radio" value="false" ${flowVar.notNull ? "" : "checked"}/>否
	</td>
	<td class="tit">是否全局</td>
	<td id="globalFlagTD">
		<input id="globalTrue" name="globalFlag" type="radio" value="true" ${flowVar.globalFlag ? "checked" : ""}/>是&nbsp;&nbsp;&nbsp;
		<input id="globalFalse" name="globalFlag" type="radio" value="false" ${flowVar.globalFlag ? "" : "checked"}/>否
	</td>
  </tr>
</table>
</div>
<div class="addTool2">
	<input type="button" value="保存" id="saveAction" class="allBtn" />
	<input type="button" value="保存并新增" id="saveAddAction" class="allBtn" />
	<input type="reset" value="重置" id="resetAction" class="allBtn" />
</div>
<script type="text/javascript"
	src="page/flowmanage/var/edit_var.js"></script>
<!--系统新增-->