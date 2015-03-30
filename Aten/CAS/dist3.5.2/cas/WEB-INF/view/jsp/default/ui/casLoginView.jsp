<jsp:directive.include file="includes/top.jsp" />
	<form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
		<form:errors path="*" id="msg" cssClass="errors" element="div" />
		<table class="box" id="login">
		<!-- <spring:message code="screen.welcome.welcome" /> -->
			<tr>
				<td class="row">
       		<label for="username" class="fl-label"><spring:message code="screen.welcome.label.netid" /></label>
					<c:if test="${not empty sessionScope.openIdLocalId}">
						<strong>${sessionScope.openIdLocalId}</strong>
						<input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
					</c:if>

					<c:if test="${empty sessionScope.openIdLocalId}">
						<spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
						<form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true" />
					</c:if>
				</td>
				<td class="row">
     			<label for="password" class="fl-label"><spring:message code="screen.welcome.label.password" /></label>
					<%--
					NOTE: Certain browsers will offer the option of caching passwords for a user.  There is a non-standard attribute,
					"autocomplete" that when set to "off" will tell certain browsers not to prompt to cache credentials.  For more
					information, see the following web page:
					http://www.geocities.com/technofundo/tech/web/ie_autocomplete.html
					--%>
					<spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
					<form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
          <!--div class="row check">
              <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
              <label for="warn"><spring:message code="screen.welcome.label.warn" /></label>
          </div-->
          </td>
          <td class="row btn-row">
						<input type="hidden" name="lt" value="${loginTicket}" />
						<input type="hidden" name="execution" value="${flowExecutionKey}" />
						<input type="hidden" name="_eventId" value="submit" />

            <input class="btn-submit" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
				</td>
			</tr>
		</table>
	</form:form>
<jsp:directive.include file="includes/bottom.jsp" />