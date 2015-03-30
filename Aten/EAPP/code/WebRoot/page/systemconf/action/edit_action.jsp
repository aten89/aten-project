<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--系统新增-->
<div class="addCon tabMid">
<input type="hidden" name="actionid" id="actionid" value="${actInfo.actionID }" />
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
  	<td class="tit">动作代码</td>
	<td><input name="actionkey" id="actionkey" readonly type="text" value="${actInfo.actionKey }" class="ipt01" maxlength="18" /></td>
	<td class="tit">动作名称</td>
	<td><input name="name" id="actoinName" readonly type="text" value="${actInfo.name }" class="ipt01"  maxlength="50"/></td>
	<td class="tit">图标&nbsp;URL</td>
	<td><input type="text" name="logourl" id="logourl" readonly value="${actInfo.logoURL }" class="ipt01" maxlength="200" /></td>
  </tr>
  <tr>
	<td class="tit">TIPS</td>
	<td colspan="5"><input name="tips" id="tips" readonly type="text" value="${actInfo.tips }" class="ipt01" maxlength="50"/></td>
	</tr>
  <tr>
	<td class="tit">描&nbsp;&nbsp;&nbsp;&nbsp;述</td>
	<td colspan="5"><textarea name="description" id="description" readonly class="area01">${actInfo.description }</textarea></td>
  </tr>
</table>
</div>
<div class="addTool2">
	<input type="button" value="保存" id="saveAction" class="allBtn" />
	<input type="button" value="保存并新增" id="saveAddAction" class="allBtn" />
	<input type="reset" value="重置" id="resetAction" class="allBtn" />
</div>
<script type="text/javascript"
	src="page/systemconf/action/edit_action.js"></script>
<!--系统新增-->