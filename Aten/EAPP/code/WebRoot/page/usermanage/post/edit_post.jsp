<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组管理-->
<form name="postFrm" action="" onsubmit="return false">
<div class="addCon">
 <input id="postid" name="postid" type="hidden" class="ipt01" value="${post.postID}"/>
 <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1" class="tabNone">
  <tr>
	<td class="tit">名&nbsp;&nbsp;&nbsp;&nbsp;称</td>
	<td colspan="3">
	   <input id="postname" name="postname" readonly maxlength="40" type="text" class="ipt01" value="${post.postName}"/>
	</td>
  </tr>
  <tr>
	<td class="tit">上级职位</td>
	<td>
	   <input id="parentid" name="parentid" type="hidden" class="ipt01" value="${post.parentPost.postID}"/>
       <input id="oldparentid" name="Input2" type="hidden" class="ipt01" value="${post.parentPost.postID}"/>
	   <input id="parentname" name="parentname" readonly type="text" class="ipt01" style="width:85%" value="${post.parentPost.postName}"/>
       <button id="showPost" disabled class="selBtn"></button> 
	</td>
	<td class="tit">所属机构</td>
	<td>
	   <input id="groupid" name="groupid" type="hidden" value="${post.group.groupID}"/>
       <input id="oldgroupid" name="oldgroupid" type="hidden" value="${post.group.groupID}"/>
	   <input id="groupname" name="groupname" readonly type="text" class="ipt01" style="width:85%" value="${post.group.groupName}"/>
       <button id="showGroup" disabled class="selBtn"></button> 
	</td>
  </tr>
  <tr>
	<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
	<td colspan="3"><textarea id="desc" name="textarea" readonly class="area01">${post.description }</textarea></td>
  </tr>
</table>
</div>
<div class="addTool2">
  <input id="savePost" type="button" value="保存" class="allBtn"/>
  <input id="saveAddPost" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetPost" type="reset" value="重置" class="allBtn"/>
</div>
</form>
<!--群组管理 end-->
<script type="text/javascript" src="page/usermanage/post/edit_post.js"></script>
<script type="text/javascript" src="page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="page/dialog/dialog.post.js"></script>
