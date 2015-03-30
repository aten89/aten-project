<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>
		
		<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="page/usermanage/group/query_group.js"></script>
	</head>
	<body class="bdNone">
	<!--系统模块管理-->
	<input id="opType" name="opType" type="hidden" value=""/>
	<input id="curGroupId" name="curGroupId" type="hidden" value=""/>
	<input id="curGroupName" name="curGroupName" type="hidden" value=""/>
	<input id="curGroupType" name="curGroupType" type="hidden" value=""/>
		<div class="subBk">
		   <table width="100%"  border="0" cellspacing="0" cellpadding="0" id="theObjTable">
			  <tr>
				<th colspan="2" class="sub02">机构管理 <b id="titlName"></b></th>
			  </tr>
			  <tr>
				<td class="subLeft">
				 	<div class="tdScaleLeft">
					    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none; table-layout:fixed">
					      <tr>
					        <td style="padding-right:5px">
					            <!--群组管理树-->
								<ul id="groupTree" class="simpleTree" style="height:340px;width:100%">
									<li class="root" groupid=""><span groupid="">机构</span>
										<ul></ul>
									</li>
								</ul>
								<!--群组管理树 end-->
					        </td>
					      </tr>
					      <tr>
					        <td>
					            <div class="mkSearch">
									名称：<input id="listGroupName" name="listGroupName" maxlength="40" type="text" class="sIpt01"/> <input id="listSearchGroup" name="listSearchGroup" type="button" value="搜索" class="allBtn subsoW" r-action="query"/>
								</div>
					        </td>
					      </tr>
					      <tr>
					        <td>
					            <div id="groupList" class="mkSelList" style="height:95px;width:100%"></div>
					        </td>
					      </tr>
					    </table>
					</div>
				</td>
				<td class="subRight" id="subRight">
				<!--工具栏-->
				<div class="addTool" id="allTool" style="display:none">
				  <div class="t01">
					  <input type="button" class="add_btn" id="addSubGroup" r-action="add"/>
					  <input type="button" class="edit_btn" id="editGroup" r-action="modify"/>
					  <input type="button" class="del_btn" id="delGroup" r-action="delete"/>
					  <input type="button" class="sort_btn" id="sortSub" r-action="order"/>
					  <input type="button" class="tjjs_btn"  id="bindRole" r-action="bindrole"/>
					  <input type="button" class="tjry_btn" id="bindPerson" r-action="binduser"/>
					  <input type="button" class="tjzw_btn" id="bindPost" r-action="bindpost"/>	  
				  </div>
				</div>
				<!--工具栏 end-->
				  <!--内容切换-->
				  <div id="groupModuleMain">
					  <!--从左边开始-->
					  <div class="czbks">
						 &nbsp;
					  </div>
					  <!--从左边开始 end-->
				  </div>
				  <!--内容切换 end-->
		        </td>
			  </tr>
			</table>
		</div>
		<!--系统模块管理 end-->
	</body>
</html>