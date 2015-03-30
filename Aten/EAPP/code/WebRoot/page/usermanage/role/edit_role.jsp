<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="jkzhMain">
<!--接口配置 新增-->
<div class="tabMid">
<div class="addCon">
  <input type="hidden" id="roleID" value="${role.roleID}">
  <input type="hidden" id="hidisValid" value="${role.isValid ? 'Y' : 'N'}">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
	<td class="tit">角色名称</td>
	<td><input id="roleName" readonly maxlength="40" type="text" class="ipt01" value="${role.roleName}"/></td>
	<td  class="tit">是否有效</td>
	
    <td width="270px">
    <!--下拉框-->
    <div id="isValid">
	  <div style="display:none">Y**有效</div>
	  <div style="display:none">N**无效</div>
    </div>
    <!--下拉框 end-->
  </td>
  </tr>
  <tr>
	<td class="tit">角色描述</td>
	<td colspan="3"><textarea id="description" readonly name="textarea" class="area01">${role.description}</textarea></td>
    </tr>
</table>
<div class="addTool2">
  <input id="saveRBACRole" type="button" value="保存" class="allBtn"/>
  <input id="saveRBACRoleAndAdd" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetRBACRole" type="reset" value="重置" class="allBtn"/>
</div>
</div>
</div>
<!--接口配置 新增 end-->
</div>
<script type="text/javascript" src="page/usermanage/role/edit_role.js"></script>