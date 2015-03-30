<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组管理-->
<form name="groupFrm" action="" onsubmit="return false">
	<div class="addCon">
	 <input id="groupid" name="groupid" type="hidden" value="${group.groupID}"/>
	 <input id="hidBusinessType" name="hidBusinessType" type="hidden" value="${group.businessType}"/>
	 <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1" class="tabNone">
	  <tr>
		<td class="tit">名&nbsp;&nbsp;&nbsp;&nbsp;称</td>
		<td>
		   <input id="groupname" name="groupname" readonly maxlength="40" type="text" class="ipt01" value="${group.groupName}"/>
		</td>
		<td  class="tit">部门类型</td>
		<td>
	        <div id="businessType" name="typeHid" type="single" overflow="true">
	        </div>
		</td>
	  </tr>
	</table>
	</div>
	
	<div class="addTool2">
	  <input id="saveGroup" type="button" value="保存" class="allBtn"/>
	</div>
</form>
<!--群组管理 end-->
<script type="text/javascript" src="page/paraconf/edit_group_ext.js"></script>