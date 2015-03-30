<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组管理-->
<form name="groupFrm" action="" onsubmit="return false">
<div class="addCon">
 <input id="groupid" name="groupid" type="hidden" value="${group.groupID}"/>
 <input id="hidGroupType" name="hidGroupType" type="hidden" value="${group.type}"/>
 <input id="hidPostid" type="hidden" value="${group.managerPost.postID}"/>		
 <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1" class="tabNone">
  <tr>
	<td class="tit">名&nbsp;&nbsp;&nbsp;&nbsp;称</td>
	<td>
	   <input id="groupname" name="groupname" readonly maxlength="40" type="text" class="ipt01" value="${group.groupName}"/>
	</td>
	<td  class="tit">类&nbsp;&nbsp;&nbsp;&nbsp;型</td>
	<td>
        <div id="grouptype"></div>
	</td>
  </tr>
  <tr>
	<td class="tit">上级部门</td>
	<td>
	   <input id="parentid" name="parentid" type="hidden" class="ipt01" value="${group.parentGroup.groupID}"/>
       <input id="oldparentid" name="Input2" type="hidden" class="ipt01" value="${group.parentGroup.groupID}"/>
	   <input id="parentname" name="parentname" readonly type="text" class="ipt01" style="width:86%" value="${group.parentGroup.groupName}"/>
       <button id="showGroup" disabled class="selBtn" onClick=""></button>    
	</td>
	<td class="tit">管理职位</td>
	<td>
	    <div id="postid"></div>
	</td>
  </tr>
  <tr>
	<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
	<td colspan="3"><textarea id="desc" name="textarea" readonly class="area01">${group.description }</textarea></td>
  </tr>
</table>
</div>
<div class="addTool2">
  <input id="saveGroup" type="button" value="保存" class="allBtn"/>
  <input id="saveAddGroup" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetGroup" type="reset" value="重置" class="allBtn"/>
</div>
</form>
<!--群组管理 end-->
<script type="text/javascript" src="page/usermanage/group/edit_group.js"></script>
<script type="text/javascript" src="page/dialog/dialog.dept.js"></script>
