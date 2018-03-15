<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="jetbrains.buildServer.auth.saml.PluginConstants" %>
<%@ include file="/include-internal.jsp" %>
<c:set var="SAMLLoginURL"><%=PluginConstants.Web.LOGIN_PATH%>
</c:set>
<c:if test="${saml_settings.schemeConfigured}">
    <c:if test="${saml_settings.hideLoginForm}">
        <style>
            .loginForm {
                display: none;
            }
        </style>
    </c:if>
    <div>
        <form action="<c:url value='${SAMLLoginURL}'/>" method="GET">
            <input class="btn loginButton" style="margin: auto; display: block" type="submit" name="submitLogin"
                   value="Log in via SAML">
        </form>
    </div>
</c:if>
