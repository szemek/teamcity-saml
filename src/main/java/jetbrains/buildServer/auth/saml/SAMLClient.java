package jetbrains.buildServer.auth.saml;

import com.onelogin.saml2.Auth;
import com.onelogin.saml2.exception.Error;
import com.onelogin.saml2.exception.SettingsException;
import com.onelogin.saml2.settings.Saml2Settings;
import com.intellij.openapi.util.text.StringUtil;
import com.onelogin.saml2.settings.SettingsBuilder;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SAMLClient {

    private final AuthenticationSchemeProperties properties;
    private static final Logger log = Logger.getLogger(SAMLClient.class);

    public SAMLClient(AuthenticationSchemeProperties properties) {
        this.properties = properties;
    }

    public String getRedirectUrl(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        SAMLClient samlClient = new SAMLClient(properties);

        try {
            Saml2Settings settings = samlClient.settings();
            Auth auth = new Auth(settings, request, response);
            return auth.login(null ,false, false, true, true);
        } catch (Exception error) {
            error.printStackTrace();
        }

        return "/";
    }

    public Saml2Settings settings() throws MalformedURLException {
        Map<String, Object> samlData = new HashMap<>();
        samlData.put("onelogin.saml2.sp.entityid", properties.getSpEntityId());
        samlData.put("onelogin.saml2.sp.assertion_consumer_service.url", new URL(properties.getAcsUrl()));
        samlData.put("onelogin.saml2.security.want_xml_validation", true);
        samlData.put("onelogin.saml2.idp.x509cert", properties.getIdpCert());
        samlData.put("onelogin.saml2.idp.entityid", properties.getIdpEntityId());
        samlData.put("onelogin.saml2.idp.single_sign_on_service.url", properties.getIdpSsoTargetUrl());

        return new SettingsBuilder().fromValues(samlData).build();
    }
}
