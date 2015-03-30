<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>企业资源管理系统</title>

<script type="text/javascript" src="page/info/paramconf/sort_layout.js"></script>
</head>
<body class="bdNone">

<input id="hidModuleRights" type="hidden" value="<oa:right key='info_param'/>" />
<form name="frmSort" method="post">
	<!--数据字典 排序-->
	<div class="addTool tabMid2">
		<div class="t01">
		   <input type="button" class="flash_btn" id="opRefresh" />
		</div>
	</div>
	<div class="addCon tabMid">
		<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0" class="tabNone">
		  <tr>
			<td id="kjfsh">
			   <div  class="dzbd">
			   <table align="center" border="0" cellspacing="0" cellpadding="0" class="tabNone">
				  <tr>
					<td width="230" >
					<select id="budgetItems" name="budgetItems" size="20" class="pxSel01" style="height:300px; width:250px">
					</select>
					</td>
					<td width="70">
					   <input id="setTop" type="button" value="移到顶部" class="allBtn pxBtn"/><br />
					   <input id="setUp" type="button" value="上移" class="allBtn pxBtn"  /><br />
					   <input id="setDown" type="button" value="下移" class="allBtn pxBtn" /><br />
					   <input id="setBottom" type="button" value="移到底部" class="allBtn pxBtn"/>
					</td>
				  </tr>
				</table>
			   </div>
			</td>
		  </tr>
		</table>
	</div>
	<div class="addTool2">
	  <input id="saveOrder" type="button" value="保存" class="allBtn"/>
	  <input id="reset" type="button" value="重置" class="allBtn"/>
	</div>
</form>
</body>
</html>