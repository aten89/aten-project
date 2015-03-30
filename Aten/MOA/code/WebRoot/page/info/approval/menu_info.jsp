<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/info/approval/menu_info.js"></script>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='info_appr'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  <td class="tdValign1">
		  <div class="costsNav" id="costsNav" >
			 <h1>信息审批</h1>
			 <div class="mb">
				<ul id="menuList">
				  <li id="StartUp" url="page/info/approval/start_info.jsp"><span class="costsQdsqd"></span>起草</li>
				  <li id="ReadyDo" url="page/info/approval/deal_info.jsp"><span class="costsDbsqd"></span>待办</li>
				  <li id="Track" url="page/info/approval/track_info.jsp"><span class="costsGzsqd"></span>跟踪</li>
				  <li id="PigeonHole" url="page/info/approval/arch_info.jsp"><span class="costsGdsqd"></span>归档</li>
				</ul>
			 </div>
		  </div>
	  </td>
	  <td><div class="vBlank"></div></td>
	  <td class="tdValign2">
		  <div class="costsCon" id="costsCon">
			  加载信息审批
		  </div>
	  </td>
	  </tr>
	  </table>
  </div>
</div>
</body>
</html>