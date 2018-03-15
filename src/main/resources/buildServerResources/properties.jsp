<%@ page import="jetbrains.buildServer.auth.saml.ConfigKey" %>
<%@ include file="/include-internal.jsp" %>
<%@ taglib prefix="prop" tagdir="/WEB-INF/tags/props" %>
<div>
    <jsp:include page="/admin/allowCreatingNewUsersByLogin.jsp"/>
</div>
<br/>
<div id="saml_server_settings">
    <div>
        <label for="<%= ConfigKey.spEntityId %>">SP Entity ID:</label><br/>
        <prop:textProperty style="width: 100%;" name="<%= ConfigKey.spEntityId.toString() %>"/><br/>
        <span class="grayNote">Service Provider Entity ID</span>
    </div>
    <div>
        <label for="<%= ConfigKey.acsUrl %>">ACS URL:</label><br/>
        <prop:textProperty style="width: 100%;" name="<%= ConfigKey.acsUrl.toString() %>"/><br/>
        <span class="grayNote">Assertion Consumer Service URL.</span>
    </div>
    <div>
        <label for="<%= ConfigKey.idpEntityId %>">IdP Entity ID:</label><br/>
        <prop:textProperty style="width: 100%;" name="<%= ConfigKey.idpEntityId.toString() %>"/><br/>
        <span class="grayNote">Identity Provider Entity ID.</span>
    </div>
    <div>
        <label for="<%= ConfigKey.idpSsoTargetUrl %>">IdP SSO Target URL:</label><br/>
        <prop:textProperty style="width: 100%;" name="<%= ConfigKey.idpSsoTargetUrl.toString() %>"/><br/>
        <span class="grayNote">Identity Provider Single Sign-On Target URL</span>
    </div>
    <div>
        <label for="<%= ConfigKey.idpCert %>">IdP Certificate:</label><br/>
        <prop:textProperty style="width: 100%;" name="<%= ConfigKey.idpCert.toString() %>"/><br/>
        <span class="grayNote">Identity Provider Certificate</span>
    </div>
</div>
<div>
    <prop:checkboxProperty uncheckedValue="false" name="<%= ConfigKey.hideLoginForm.toString() %>"/>
    <label for="<%= ConfigKey.hideLoginForm %>">Hide login form</label><br/>
    <span class="grayNote">Hide user/password login form on Teamcity login page.</span>
</div>
<div>
    <prop:checkboxProperty uncheckedValue="false" name="<%= ConfigKey.allowInsecureHttps.toString() %>"/>
    <label for="<%= ConfigKey.allowInsecureHttps %>">Insecure https</label><br/>
    <span class="grayNote">Allow insecure https access like invalid certificate</span>
</div>
