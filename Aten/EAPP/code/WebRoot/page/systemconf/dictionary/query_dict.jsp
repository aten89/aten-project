<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>数据字典查询</title>
		<script type="text/javascript" src="jqueryui/tree/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="page/systemconf/dictionary/query_dict.js"></script>
	</head>
	<body class="bdNone">
	<input type="hidden" id="opType" value=""/>
	<input type="hidden" id="curSubSystemId" value=""/>
	<input type="hidden" id="curSubSystemName" value=""/>
	<input type="hidden" id="curDataDictId" value=""/>
	<input type="hidden" id="curDataDictName" value=""/>
	<input type="hidden" id="curDataDictType" value=""/>
		<!--数据字典-->
		<div class="subBk">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="theObjTable">
				<tr>
					<th colspan="2" class="sub02">数据字典 <b id="titlName"></b></th>
				</tr>
				<tr>
					<td class="subLeft">
						<div class="tdScaleLeft">
						    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none;table-layout:fixed">
						      <tr>
						        <td>
						            <!--下拉框-->
									<div id="subSystemList"></div>
									<!--下拉框 end-->
						        </td>
						      </tr>
						      <tr>
						        <td style="padding-right:5px">
						            <!--数据字典-->
									<div id="systemDicTree">
										<ul class="simpleTree" style="height:320px;width:100%"></ul>
									</div>
									<!--数据字典 end-->
						        </td>
						      </tr>
						      <tr>
						        <td>
						            <div class="mkSearch">
										名称：<input id="searchDataDictName" name="" type="text" class="sIpt01" maxlength="50" /> <input id="search" type="button" value="搜索" class="allBtn subsoW" r-action="query"/>
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
						
						<div>
						</div>
					</td>
					<td class="subRight" id="contentMain">
						<!--工具栏-->
						<div class="addTool" id="manageToolBar" style="display:none">
							<div class="t01">
								<input type="button" class="add_btn" id="addDataDict" r-action="add" />
								<input type="button" class="addTm_btn" id="addDataType" r-action="add" />
								<input type="button" class="edit_btn" id="modifyDataDict" r-action="modify" />
								<input type="button" class="del_btn"  id="deleteDataDict" r-action="delete"/>
								<input type="button" class="sort_btn" id="sortDataDict" r-action="order" />
							</div>
						</div>
						<!--工具栏 end-->
						<!--内容切换-->
						<div id="dataDictMain">
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
		<!--数据字典 end-->
	</body>
</html>