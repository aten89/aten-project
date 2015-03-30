<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
	<head>
		<META content="MSHTML 6.00.2800.1276" name=GENERATOR>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META http-equiv=Content-Style-Type content=text/css>
		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<title>乾元财富企业信息化综合运营平台</title>
		<LINK href="oalogin/login.css" type=text/css rel=stylesheet>
		
		<script language='javascript' src='oalogin/jquery.js'></script>
		<script language="javascript" src="oalogin/login.js"></script>
		<style type="text/css">
			.errors{
				color:red;font-size:14px;font-weight:bold;padding:0px 0 0px 60px;
			}
		</style>

	</head>


	<body LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0 CLASS=bg_1>
		
	<DIV STYLE="display:none;">
		
	</DIV>
	<form:form method="post" id="fm1" name="casLoginFrm" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
		<DIV CLASS=browesPosition>
			<TABLE WIDTH=100% BORDER=0 CELLSPACING=0 CELLPADDING=0 ID=tableheight>
				<TR>
					<TD ID=toptd><input id="macAddr" type="hidden" name="macAddr" /><input id="alertMsg" type="hidden" name="alertMsg" /><input id="confirmMsg" type="hidden" name="confirmMsg" /></TD>
				</TR>
				<TR>
					<TD ID=middletd>
						<DIV CLASS=bg_middle>
							<TABLE ALIGN=CENTER>
								<TR>
									<TD>
										<DIV CLASS=login>
											
											<DIV CLASS=login_R>
                                            	<div class="m1_head">系统登录</div>
											  <ul>
											  <LI>
														<form:input cssClass="TextField" cssErrorClass="TextField" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
													</LI>
													<LI>
														<form:password cssClass="TextField" cssErrorClass="TextField" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
													</LI>
													<LI><form:errors path="*" cssClass="errors" id="status" element="div" /></LI>
													<LI>
														<input type="hidden" name="lt" value="${loginTicket}" />
														<input type="hidden" name="execution" value="${flowExecutionKey}" />
														<input type="hidden" name="_eventId" value="submit" />
														<input src="oalogin/images/userLogin_button.png" type="image"/>
														</LI>
												</ul>
                                                <div class="ml_clear"></div>
                                                <div class="ml_tip">OA、CRM只限内网访问，POSS系统可在公网访问</div>		
                                                <div class="ml_bot">版本号:OA-CRM-POSS1-0-0-30</div>	
												<div class=downTable></div>	
										</DIV>
                                         
										</DIV>
									</TD>
								</TR>
							</TABLE>
						</DIV>
					</TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV STYLE="display:none;">
			<input type="hidden" name="functionName" />
			<input type="hidden" name="screenWidth" />
			<input type="hidden" name="screenHeight" />
			<input id="userNameInputId" type="hidden" name="userNameInputId" />
			<input id="pasdInputId" type="hidden" name="pasdInputId" /><input id="useStyle" type="text" value="BLUE" name="11.56" /><input id="useLanguage" type="text" value="CN" name="11.57" /><a id="version" href="/OAapp/WebObjects/OAapp.woa/wo/com.oa8000.mainapp.Main/bH2eg8SSrmwAoVWaVHHyC0/0.11.58;jsessionid=99778E8081CF873E4B5B7456573B5439"></a><a id="down" href="/OAapp/WebObjects/OAapp.woa/wo/com.oa8000.mainapp.Main/bH2eg8SSrmwAoVWaVHHyC0/0.11.59;jsessionid=99778E8081CF873E4B5B7456573B5439"></a>
		</DIV>
	<input type="hidden" name="wosid" value="bH2eg8SSrmwAoVWaVHHyC0" />
	</form:form>
    <!--<DIV CLASS=login_3>
							<span class="login_3_span">版本号:OA-CRM-POSS1-0-0-30</span>
						</DIV>-->
	</body>
</html>