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

    @NotNull
    @Override
    public HttpAuthenticationResult processAuthenticationRequest(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Map<String, String> schemeProperties) throws IOException {
        try {
            Saml2Settings settings = samlClient.settings();
            Auth auth = new Auth(settings, request, response);
            auth.processResponse();

            String nameId = auth.getNameId();
            SAMLUser user = new SAMLUser(nameId);
            LOG.info(user);

            boolean allowCreatingNewUsersByLogin = AuthModuleUtil.allowCreatingNewUsersByLogin(schemeProperties, DEFAULT_ALLOW_CREATING_NEW_USERS_BY_LOGIN);
            final Optional<ServerPrincipal> principal = principalFactory.getServerPrincipal(user, allowCreatingNewUsersByLogin);

            if (principal.isPresent()) {
                LOG.debug("Request authenticated. Determined user " + principal.get().getName());
                return HttpAuthenticationResult.authenticated(principal.get(), true)
                        .withRedirect("/");
            }

            return sendUnauthorizedRequest(request, response, "Unauthenticated since user could not be found or created.");
        } catch (com.onelogin.saml2.exception.Error e) {
            //thrown if this is not a SAML request (no "SAMLResponse" request parameter)
            if (LOG.isDebugEnabled()) {
                LOG.debug("Got error while attempting SAML authentication, ignoring SAML module. Error: " + e.getMessage(), e);
            }
            return HttpAuthenticationResult.notApplicable();
        } catch (com.onelogin.saml2.exception.SettingsException e) {
            LOG.warn("Error initializing SAML authentication: " + e.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error initializing SAML authentication: " + e.getMessage(), e);
            }
            return HttpAuthenticationResult.notApplicable();
        } catch (Exception e) {
            LOG.warn("Error processing SAML request: " + e.toString());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error processing SAML request: " + e.toString(), e);
            }
            return sendUnauthorizedRequest(request, response, "Error processing SAML request");
        }
    }

    private HttpAuthenticationResult sendUnauthorizedRequest(HttpServletRequest request, HttpServletResponse response, String reason) throws IOException {
        LOG.warn(reason);
        HttpAuthUtil.setUnauthenticatedReason(request, reason);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), reason);
        return HttpAuthenticationResult.unauthenticated();
    }
}
