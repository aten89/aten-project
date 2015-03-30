<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/meeting/menu_meet.js"></script></head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='meet_query'/>" />
<div class="costsBg">
	<div class="costsBgTip">
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
		<tbody><tr>
		<td class="tdValign1">
		<div class="crmSubNav crmSubLevel" id="crmSubNav">
			<h1 style="height:25px">会议预定</h1>
			<div class="mb">
				<ul id="menuList">
                  <li url="page/meeting/order/order_meeting.jsp" class="subFirst"><img src="themes/comm/images/yssqsp_icon.gif"/>会议预定</li>
                  <li class="subFirst subOneMar"><img src="themes/comm/images/xsdcx_icon.gif"/>会议查询</li>
                  <li id="ReadyDo" url="page/meeting/query/query_irese.jsp" class="submb">
                    <div><img src="themes/comm/images/crmNavSubDot.gif"/>我已预定的</div>
                  </li>
                  <li id="PigeonHole" url="page/meeting/query/query_instart.jsp" class="submb">
                    <div><img src="themes/comm/images/crmNavSubDot.gif"/>我要参加的</div>
                  </li>
                  <li id="Track" url="page/meeting/query/query_iatte.jsp" class="submb">
                    <div><img src="themes/comm/images/crmNavSubDot.gif"/>我参加过的</div>
                  </li>
                   <li id="StartUp" url="page/meeting/query/query_all.jsp" class="subLast">
                    <div><img src="themes/comm/images/crmNavSubDot.gif"/>所有的会议</div>
                  </li>
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
		</tbody>
	</table>
	</div>
</div>
</body>
</html>
