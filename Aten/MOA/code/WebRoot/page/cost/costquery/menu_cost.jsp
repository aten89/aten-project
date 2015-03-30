<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/cost/costquery/menu_cost.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='costquery'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
			<td class="tdValign1">
				  <div class="costsNav" id="costsNav">
					 <h1>费用查询</h1>
					 <div class="mb">
						<ul id="menuList">
						  <li id="CostListSearchCw" url="page/cost/costquery/query_cost.jsp"><span class="costsFycxCw flatIcon_oa1"></span>费用明细查询</li>
						  <li id="ExpenseAccountSearchCw" url="page/cost/costquery/query_reim.jsp"><span class="costsBxdcxCw flatIcon_oa2"></span>报销单查询</li>
						</ul>
					</div>
				  </div>
			</td>
			<td><div class="vBlank"></div></td>
			<td class="tdValign2" style="background:#fff">
				<div class="costsCon" id="costsCon">
					<!--加载预算信息 -->
				</div>
			</td>
			</tr>
		</table>
  </div>
</div>
<!--报销单查询 end-->
</body>
</html>