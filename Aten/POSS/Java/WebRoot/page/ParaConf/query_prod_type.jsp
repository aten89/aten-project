<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>
		
		<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="${ermpPath}jqueryui/tree/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="page/ParaConf/query_prod_type.js"></script>
	</head>
	<body class="bdNone">
	<input id="hidModuleRights" type="hidden" value="<eapp:right key='PROD_TYPE'/>"/>
	<!--产品分类-->
	<input id="opType" name="opType" type="hidden" value=""/>
	<input id="curProdTypeID" name="curProdTypeID" type="hidden" value=""/>
	<input id="curProdType" name="curProdType" type="hidden" value=""/>
		<div class="subBk">
		   <table width="100%"  border="0" cellspacing="0" cellpadding="0" id="theObjTable">
			  <tr>
				<th colspan="2" class="sub02">产品分类<b id="titlName"></b></th>
			  </tr>
			  <tr>
				<td class="subLeft">
				 	<div class="tdScaleLeft">
					    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none; table-layout:fixed">
					      <tr>
					        <td style="padding-right:5px">
					            <!--产品分类树-->
								<ul id="tree" class="simpleTree" style="height:340px;width:100%">
									<li class="root" groupid=""><span groupid="">产品分类</span>
										<ul></ul>
									</li>
								</ul>
								<!--产品分类树 end-->
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
					  <input type="button" class="add_btn" id="addSubProdType"/>
					  <input type="button" class="edit_btn" id="editProdType"/>
					  <input type="button" class="del_btn"  id="delProdType"/>
					  <input type="button" class="sort_btn"  id="sortSub"/>
				  </div>
				</div>
				<!--工具栏 end-->
				  <!--内容切换-->
				  <div id="moduleMain">
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
		<!--产品分类 end-->
	</body>
</html>