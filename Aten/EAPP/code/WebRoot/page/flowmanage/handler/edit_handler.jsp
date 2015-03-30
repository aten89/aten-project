<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--系统新增-->
<div class="addCon tabMid">
<input type="hidden" id="handId" value="${flowHandler.handId }" />
<input type="hidden" id="flowClass" value="${flowHandler.flowClass }" />
<input type="hidden" id="type" value="${flowHandler.type }" />
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
  	<td class="tit">流程类别</td>
	<td><div id="flowClassDiv"></div></td>
	<td class="tit">显示名称</td>
	<td><input id="name" readonly type="text" value="${flowHandler.name }" class="ipt01"  maxlength="50"/></td>
	<td class="tit">是否全局</td>
	<td id="globalFlagTD">
		<input id="globalTrue" name="globalFlag" type="radio" value="true" ${flowHandler.globalFlag ? "checked" : ""}/>是&nbsp;&nbsp;&nbsp;
		<input id="globalFalse" name="globalFlag" type="radio" value="false" ${flowHandler.globalFlag ? "" : "checked"}/>否
	</td>
  </tr>
  <tr>
	<td class="tit">程序类型</td>
	<td><div id="typeDiv">
		<div>ACTION**ACTION</div>
		<div>ACTION_TASKEVENT**ACTION_TASKEVENT</div>
		<div>ASSIGN**ASSIGN</div>
		<div>MUTIASSIGN**MUTIASSIGN</div>
		<div>DECISION**DECISION</div>
		<div>VIEW**VIEW</div>
	</div></td>

	<td class="tit">程序类名</td>
	<td colspan="3"><input id="handlerClass" readonly type="text" value="${flowHandler.handlerClass }" class="ipt01"  maxlength="100"/></td>
  </tr>
</table>
</div>
<div class="addTool2">
	<input type="button" value="保存" id="saveAction" class="allBtn" />
	<input type="button" value="保存并新增" id="saveAddAction" class="allBtn" />
	<input type="reset" value="重置" id="resetAction" class="allBtn" />
</div>
<script type="text/javascript"
	src="page/flowmanage/handler/edit_handler.js"></script>
<!--系统新增-->