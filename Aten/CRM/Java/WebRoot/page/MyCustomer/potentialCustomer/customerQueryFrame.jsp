<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>潜在客户查询框架页面</title>
	<script type="text/javascript" src="page/MyCustomer/potentialCustomer/customerQueryFrame.js"></script>
</head>
<body class="bdNone">
	<input id="FrameHidModuleRights" type="hidden" value="<eapp:menu key='POTENTIAL_CUST'/>" />
	<div class="costsBg">
		<div class="costsBgTip">
		    <table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
					<tr>
						<td width="131" valign="top" class="tdValign1">
							<div class="costsNav" id="crmSubNav" >
								<h1>资料类型</h1>
								<div class="mb">
									<ul id="menuList">
										<li name="first" url="page/MyCustomer/potentialCustomer/customerQueryList.jsp?status=3" class="subFirst" id="workorderLi"><span class="costsDbsqd flatIcon_c2"></span>潜在客户</li>
										<li name="first" url="page/MyCustomer/potentialCustomer/customerQueryList.jsp?status=1" class="subFirst" id="workorderEvaluateLi"><span class="costsQdsqd flatIcon_c4"></span>审批中客户</li>
		                			</ul>
								</div>
							</div>
						</td>
						<td><div class="vBlank"></div></td>
						<td class="crmFrameBg" width="100%" valign="top">
							<div class="crmFrameHead flatHide"><img src="themes/comm/images/frameNav2.gif"/><span id="crmFrameHead">所有工单</span></div>
							<div class="blank2 flatHide"></div>
							<div id="costsCon" class="costsCon"></div>
						</td>
					</tr>
					<span id="delay_moni_desc" style="display:none"><font color="red">(查询出即将超时和已经超时的工单，误差在10分钟以内)</font></span>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
