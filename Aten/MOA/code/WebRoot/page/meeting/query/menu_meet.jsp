<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="page/meeting/query/menu_meet.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>

<title></title>
</head>
<body class="oaBd">
<input id="hidModuleRights" type="hidden" value="<oa:right key='meet_query'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  <td class="tdValign1">
		  <div class="costsNav" id="costsNav" >
			 <h1>会议查询</h1>
			 <div class="mb">
				<ul id="menuList">
				  <li id="StartUp" url="page/meeting/query/query_all.jsp"><span class="costsQdsqd"></span>所有会议查询</li>
				  <li id="ReadyDo" url="page/meeting/query/query_irese.jsp"><span class="costsDbsqd"></span>我预订的会议</li>
				  <li id="Track" url="page/meeting/query/query_iatte.jsp"><span class="costsGzsqd"></span>>我参加过的会议</li>
				  <li id="PigeonHole" url="page/meeting/query/query_instart.jsp"><span class="costsGdsqd"></span>我未开始的会议</li>
				</ul>
			 </div>
		  </div>
	  </td>
	  <td><div class="vBlank"></div></td>
	  <td class="tdValign2">
		  <div class="costsCon" id="costsCon">
			  加载会议查询列表
		  </div>
	  </td>
	  </tr>
	  </table>
  </div>
</div>
</body>
</html>