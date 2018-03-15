package jetbrains.buildServer.auth.saml;


import jetbrains.buildServer.users.AuthPropertyKey;
import jetbrains.buildServer.users.PropertyKey;

public interface PluginConstants {
    String SAML_AUTH_SCHEME_NAME = "SAML 2.0";
    String SAML_AUTH_SCHEME_DESCRIPTION = "Authentication via SAML 2.0";

    AuthPropertyKey ID_USER_PROPERTY_KEY = new AuthPropertyKey("HTTP", "teamcity-saml-id", "SAML ID");

    interface Web {
        String LOGIN_PATH = "/saml.html";
        String LOGIN_EXTENSION_PAGE = "saml.jsp";
        String EDIT_SCHEME_PAGE = "properties.jsp";
    }
}
