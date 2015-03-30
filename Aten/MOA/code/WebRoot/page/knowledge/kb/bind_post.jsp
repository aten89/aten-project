<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="page/knowledge/kb/bind_post.js"></script>
<!--绑定人员-->
<div class="tabMid">
<div class="subFrame" style="height:418px">
 <div class="infoTip" style="height:18px; padding-top:9px"><b id="titleName"></b><b>的授权</b></div>
	<div  class="dzbd">
		<table border="0" cellspacing="0" cellpadding="0" >
		  <tr>
			<td valign="top">
			 <div class="mkdzTip" style="padding:6px 0 0 10px;font-weight:normal">已绑定的职位</div>
			 <div class="mkdz">
			    <div class="bindUser">
				   <table  id="bindPostList" width="100%" border="0" cellspacing="0" cellpadding="0">
				   <thead>
					   <tr>
					     <th width="40px">操作</th>
						 <th width="110px">职位</th>
						 <th>
                         <div class="klRight" style="width:250px">
                             <span>知识查询</span>
                             <span>知识管理</span>
                             <span>授权管理</span>
                             <span>分类管理</span>
                         </div>
                         </th>
					  </tr>
				   </thead>
				   <tbody>				
				   </tbody>
				   </table>
				</div>
			    <div class="addTool2" >
				  <input id="saveUserRoles" type="button" value="保存" class="allBtn"/>
			    </div>
			 </div>
			</td>
			<td align="center" width="15">&nbsp;</td>
			<td valign="top" width="268">
				<div class="mkdzTip" style="padding:6px 0 0 10px">请选择职位</div>
				<div class="mkdz">
				<!--群组管理树-->
				<ul id="postTree" class="simpleTree" style="height:297px; width:261px">
					<li class="root" postid=""><span postid="">职位</span>
						<ul></ul>
					</li>
				</ul>
				<!--群组管理树 end-->
				</div>
			</td>
		  </tr>
		</table>
	</div>
</div>
</div>
<!--绑定人员 end-->
