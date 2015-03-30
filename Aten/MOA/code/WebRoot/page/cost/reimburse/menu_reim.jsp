<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/cost/reimburse/menu_reim.js"></script>
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='reimburse'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  <td class="tdValign1">
		  <div class="costsNav" id="costsNav" >
			 <h1>费用报销</h1>
			 <div class="mb">
				<ul id="menuList">
				  <li id="StartUp" url="page/cost/reimburse/start_reim.jsp"><span class="costsQdsqd"></span>启动</li>
				  <li id="ReadyDo" url="page/cost/reimburse/deal_reim.jsp"><span class="costsDbsqd"></span>待办</li>
				  <li id="Track" url="page/cost/reimburse/track_reim.jsp"><span class="costsGzsqd"></span>跟踪</li>
				  <li id="PigeonHole" url="page/cost/reimburse/arch_reim.jsp"><span class="costsGdsqd"></span>归档</li>
				</ul>
			 </div>
		  </div>
	  </td>
	  <td><div class="vBlank"></div></td>
	  <td class="tdValign2">
		  <div class="costsCon" id="costsCon" style="margin-left:0; white-space:nowrap">
			  加载报销单
		  </div>
	  </td>
	  </tr>
	  </table>
  </div>
</div>
</body>
</html>