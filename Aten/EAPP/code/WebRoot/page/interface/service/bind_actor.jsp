<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--与操作帐号绑定-->
<div class="tabMid">
<div class="subFrame">
   <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td class="subLeft02">
		   <!--角色定位-->
		   <div class="mkdzTip">
			  <b>服务：</b><input id="txtSearchService" maxlength="40" type="text" class="ipt05" style="width:65px"/>
		   	  <input id="search_Service" type="button" class="allBtn subsoW" value="检索" />
		   </div>
		   <div class="jsdw">
		   </div>
		   <!--角色定位 end-->
		</td>
		<td class="subRight">
		   <table align="center" style="margin:0 auto" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td>
				   <div  class="dzbd">
				   <table border="0" cellspacing="0" cellpadding="0" >
					  <tr>
						<td valign="top" width="312">
						 <div class="mkdzTip" style="padding:6px 0 0px 10px">已绑定的接口帐号</div>
						 <div class="mkdz">
						    <div class="bindUser" style="height:270px;width:310px">
						       <input id="serviceid" name="serviceID" type="hidden"/>
							   <table  id="bindActorList" width="100%" border="0" cellspacing="0" cellpadding="0">
							   <thead>
								  <tr>
								  	<th nowrap width="30">操作</th>
									<th nowrap>显示名称 </th>
									<th nowrap>接口帐号 </th>
								  </tr>
							   </thead>
							   <tbody></tbody>
							   </table>
							</div>
						    <div class="addTool2" >
							  <input id="saveActorService" type="button" value="保存" class="allBtn"/>
						    </div>
						 </div>
						</td>
						<td align="center" width="15">&nbsp;</td>
						<td valign="top" width="312">
						 <div class="mkdzTip">
						  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="background:none">
						  <tr>
							<!-- <td >
							名称：<input id="actorName" maxlength="40" type="text" class="ipt05" style="width:65px" />
							</td> -->
							<td>
							  关键字：<input id="actorkeyword" maxlength="40" type="text" class="ipt05" style="width:100px" /> <input id="searchActorAccount" type="button" class="allBtn subsoW" value="检索" /></td>
						  </tr>
						 </table>
						 </div>
						 <div class="mkdz">
						    <div class="bindUser" style="height:305px;width:310px">
						      <table id="actorAccountList" width="100%" border="0" cellspacing="0" cellpadding="0">
                                <thead>
                                <tr>
                                  <th nowrap width="30">操作</th>
                                  <th nowrap>显示名称</th>
                                  <th nowrap>接口帐号</th>
                                </tr>
								  </thead>
								  <tbody>
								  </tbody>
                              </table>
						    </div>
						</div>
						</td>
					  </tr>
					</table>
				   </div>
				</td>
			  </tr>
			 </table>
		</td>
	  </tr>
	</table>
</div>
</div>
<!--与操作帐号绑定 end-->
<script type="text/javascript" src="page/interface/service/bind_actor.js"></script>