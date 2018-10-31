package jetbrains.buildServer.auth.saml;

import com.onelogin.saml2.Auth;
import com.onelogin.saml2.settings.Saml2Settings;
import jetbrains.buildServer.controllers.interceptors.auth.HttpAuthenticationResult;
import jetbrains.buildServer.controllers.interceptors.auth.HttpAuthenticationSchemeAdapter;
import jetbrains.buildServer.controllers.interceptors.auth.util.HttpAuthUtil;
import jetbrains.buildServer.serverSide.auth.AuthModuleUtil;
import jetbrains.buildServer.serverSide.auth.ServerPrincipal;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.Optional;


public class SAMLAuthenticationScheme extends HttpAuthenticationSchemeAdapter {

    private static final Logger LOG = Logger.getLogger(SAMLAuthenticationScheme.class);
    private static final boolean DEFAULT_ALLOW_CREATING_NEW_USERS_BY_LOGIN = true;

    private final PluginDescriptor pluginDescriptor;
    private final ServerPrincipalFactory principalFactory;
    private final SAMLClient samlClient;
    private final AuthenticationSchemeProperties properties;

    public SAMLAuthenticationScheme(@NotNull final PluginDescriptor pluginDescriptor,
                                     @NotNull final ServerPrincipalFactory principalFactory,
                                     @NotNull final SAMLClient samlClient,
                                     @NotNull final AuthenticationSchemeProperties properties) {
        this.pluginDescriptor = pluginDescriptor;
        this.principalFactory = principalFactory;
        this.samlClient = samlClient;
        this.properties = properties;
    }

    @NotNull
    @Override
    protected String doGetName() {
        return PluginConstants.SAML_AUTH_SCHEME_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return PluginConstants.SAML_AUTH_SCHEME_DESCRIPTION;
    }

    @Nullable
    @Override
    public String getEditPropertiesJspFilePath() {
        return pluginDescriptor.getPluginResourcesPath(PluginConstants.Web.EDIT_SCHEME_PAGE);
    }

    @Nullable
    @Override
    public Collection<String> validate(@NotNull Map<String, String> properties) {
        final Collection<String> errors = new ConfigurationValidator().validate(properties);
        return errors.isEmpty() ? super.validate(properties) : errors;
    }

    static private String getStringFromAtrributes(String attribute, Map<String, List<String>> attributes) {
        List<String> attributeList = attributes.get(attribute);
        if (attributeList == null) {
            return null;
        }
        if (attributeList.size() < 1) {
            return null;
        }
        return attributeList.get(0);
    }

    @NotNull
    @Override
    public HttpAuthenticationResult processAuthenticationRequest(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Map<String, String> schemeProperties) throws IOException {
        try {
            Saml2Settings settings = samlClient.settings();
            Auth auth = new Auth(settings, request, response);
            auth.processResponse();

            String nameId = auth.getNameId();
            Map<String, List<String>> attributes = auth.getAttributes(); 
            String email = getStringFromAtrributes("email", attributes);
            String firstName = getStringFromAtrributes("first_name", attributes);
            String lastName = getStringFromAtrributes("last_name", attributes);
            String name = null;
            if (firstName != null && lastName != null) {
                name = firstName + " " + lastName;
            } else if (firstName != null) {
                name = firstName;
            } else if (lastName != null) {
                name = lastName;
            }
            SAMLUser user = new SAMLUser(nameId, name, email);
            LOG.info(user);

            boolean allowCreatingNewUsersByLogin = AuthModuleUtil.allowCreatingNewUsersByLogin(schemeProperties, DEFAULT_ALLOW_CREATING_NEW_USERS_BY_LOGIN);
            final Optional<ServerPrincipal> principal = principalFactory.getServerPrincipal(user, allowCreatingNewUsersByLogin);

            if (principal.isPresent()) {
                LOG.debug("Request authenticated. Determined user " + principal.get().getName());
                return HttpAuthenticationResult.authenticated(principal.get(), true)
                        .withRedirect("/");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }

        return sendUnauthorizedRequest(request, response, "Unauthenticated since user could not be found or created.");
    }

    private HttpAuthenticationResult sendUnauthorizedRequest(HttpServletRequest request, HttpServletResponse response, String reason) throws IOException {
        LOG.warn(reason);
        HttpAuthUtil.setUnauthenticatedReason(request, reason);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), reason);
        return HttpAuthenticationResult.unauthenticated();
    }
}
