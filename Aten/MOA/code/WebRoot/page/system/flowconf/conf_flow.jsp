<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/jQuery.FillOptions.js"></script>
<script type="text/javascript" src="page/system/flowconf/conf_flow.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleMenus" type="hidden" value="<oa:menu key='flow_config'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
  	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  <td class="tdValign1">
		  <div class="costsNav" id="costsNav">
			 <h1>流程配置</h1>
			 <div class="mb">
				<ul id="menuList">
				  <li id="flowDraft" url="page/system/flowconf/query_draft.jsp"><img src="themes/comm/images/officialBjlc.gif" />流程草稿</li>
				  <li id="IssueFlow" url="page/system/flowconf/query_pub.jsp"><img src="themes/comm/images/officialLclb.gif" />已发布流程</li>
				</ul>
			 </div>
		  </div>
	  </td>
	  <td><div class="vBlank"></div></td>
	  <td class="tdValign2">
		  <div class="costsCon" id="costsCon">
		    <!--流程草稿-->
		    
			<!--流程草稿 end-->
		  </div>
	  </td>
	  </tr>
	  </table>
  </div>
</div>
</body>
</html>