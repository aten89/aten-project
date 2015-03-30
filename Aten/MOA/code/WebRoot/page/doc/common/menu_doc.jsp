<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="page/doc/common/menu_doc.js"></script>
<title></title>
</head>
<body class="bdNone">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='normal'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		  <tbody>
		    <tr>
		      <td class="tdValign1">
		      	  <div class="costsNav" id="costsNav">
					 <h1>日常文件审批</h1>
					 <div class="mb">
						<ul id="menuList">
						   <li id="start" url="page/doc/common/start_doc.jsp"><span class="costsQdsqd"></span>起草</li>
						  <li id="deal" url="page/doc/common/deal_doc.jsp"><span class="costsDbsqd"></span>待办</li>
						  <li id="track" url="page/doc/common/track_doc.jsp"><span class="costsGzsqd"></span>跟踪</li>
						  <li id="arch" url="page/doc/common/arch_doc.jsp"><span class="costsGdsqd"></span>归档</li>
						</ul>
					 </div>
				  </div>
		      </td>
		      <td><div class="vBlank"></div></td>
		      <td class="tdValign2">
		      	<div class="costsCon" id="costsCon"></div>
		      </td>
		    </tr>
		  </tbody>
		</table>
  </div>
</div>
<!--归档文件 end-->
</body>
</html>
