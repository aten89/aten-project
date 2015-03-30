<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组 添加角色-->
<div class="addCon">
<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0" class="tabNone">
  <tr>
	<td valign="top">
	   <div  class="dzbd">
		 <table align="center" border="0" cellspacing="0" cellpadding="0" class="tabNone">
		  <tr>
			<td>
			   
			   <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabNone">
				  <tr>
					<td valign="top" width="255" style="height:305px; padding:0px 0 0">
					  <div class="mkdzTip" style="padding:6px 0 0 10px">已添加职位</div>
					   <div class="mkdz" style="border:none">
					   <select id="bindedPost" name="" size="1" multiple="multiple" class="dzbdSel01" style="height:260px; width:255px">
					   </select>
					   </div>
					</td>
					<td style="min-width:10px">
					   &nbsp;<input id="addBind" name="" type="button" value="添加&lt;&lt;" class="allBtn"/><br />
					   &nbsp;<input id="delBind" name="" type="button" value="删除&gt;&gt;" class="allBtn" style="margin:6px 0px"/>
					</td>
					<td valign="top" width="255"  style="height:305px; padding:0px 0 0">
						<div class="mkdzTip">未添加职位：<input id="postname" name="postname" maxlength="40" type="text" class="ipt01" style="width:100px" value=""/>&nbsp;<input id="searchPost" type="button" value="搜索" class="allBtn subsoW" /></div>
						<div class="mkdz" style="border:none">
							<select id="post" name="" size="1" multiple="multiple" class="dzbdSel01" style="height:260px; width:255px">
							</select>
						</div>
					</td>
				  </tr>
				</table>
			   
			</td>
		  </tr>
		 </table>
	   </div>
	</td>
  </tr>
 </table>
 <div class="addTool2">
	<input id="saveBind" name="" type="button" value="保存" class="allBtn"/>
 </div>
</div>
<!--群组 添加角色 end-->
<script type="text/javascript" src="page/usermanage/group/bind_post.js"></script>