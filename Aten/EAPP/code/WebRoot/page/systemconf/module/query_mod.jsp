<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="page/systemconf/module/query_mod.js"></script>
	</head>
	<body class="bdNone">
	<input type="hidden" id="opType" value=""/>
	<input type="hidden" id="curSubSystemId" value=""/>
	<input type="hidden" id="curSubSystemName" value=""/>
	<input type="hidden" id="curModuleId" value=""/>
	<input type="hidden" id="curModuleName" value=""/>
		<!--系统模块管理-->
		<div class="subBk">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  id="theObjTable" >
				<tr>
					<th colspan="2" class="sub02">系统模块管理 <b id="titlName"></b></th>
				</tr>
				<tr>
					<td class="subLeft">
						<div class="tdScaleLeft">
						    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none;table-layout:fixed">
							  <tr>
							    <td>
							    	<div id="subSystemList"></div>
								</td>
							  </tr>
							  <tr>
							    <td style="padding-right:5px">
								    <!--系统模块树-->
									<ul id="systemModules" class="simpleTree" style="height:320px;width:100%"></ul>
									<!--系统模块树 end-->
							    </td>
							  </tr>
							  <tr>
							    <td>
							    	<div class="mkSearch">
										名称：<input id="searchModuleName" name="" type="text" class="sIpt01" maxlength="50"/> <input type="button" value="搜索" id="searchMd" class="allBtn subsoW" r-action="query"/>
									</div>
							    </td>
							  </tr>
							  <tr>
							    <td>
							    	<div id="searchResults" class="mkSelList" style="height:95px;width:100%"></div>
							    </td>
							  </tr>
							</table>
						</div>	
					</td>
					<td class="subRight" id="moduleMain">
						<!--工具栏-->
						<div class="addTool" id="manageToolBar" style="display: none">
							<div class="t01">
								<input type="button" class="add_btn" id="addSubModule" r-action="add" />
								<input type="button" class="edit_btn" id="modifyModule" r-action="modify" />
								<input type="button" class="del_btn" id="delModule" r-action="delete"/>
								<input type="button" class="sort_btn" id="sortSubModule"  r-action="order"/>
								<input type="button" class="bddz_btn" id="bindModuleAction"  r-action="bindaction"/>
							</div>
						</div>
						<!--工具栏 end-->
						<div id="systemModuleMain">
							<!--从左边开始-->
							<div class="czbks">
								&nbsp;
							</div>
							<!--从左边开始 end-->
						</div>
					</td>
				</tr>
			</table>
		</div>
		<!--系统模块管理 end-->
		
	</body>
</html>