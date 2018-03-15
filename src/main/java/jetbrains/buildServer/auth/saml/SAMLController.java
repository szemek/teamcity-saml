package jetbrains.buildServer.auth.saml;

import jetbrains.buildServer.controllers.AuthorizationInterceptor;
import jetbrains.buildServer.controllers.BaseController;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SAMLController extends BaseController {

    private final AuthenticationSchemeProperties schemeProperties;
    private final SAMLClient samlClient;
    private static final Logger LOG = Logger.getLogger(SAMLController.class);

    public SAMLController(@NotNull final WebControllerManager webManager,
                                   @NotNull final AuthorizationInterceptor authInterceptor,
                                   @NotNull final AuthenticationSchemeProperties schemeProperties,
                                   @NotNull final SAMLClient samlClient) {
        this.schemeProperties = schemeProperties;
        this.samlClient = samlClient;
        webManager.registerController(PluginConstants.Web.LOGIN_PATH, this);
        authInterceptor.addPathNotRequiringAuth(PluginConstants.Web.LOGIN_PATH);
    }

    @Override
    protected ModelAndView doHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws Exception {
        if (!schemeProperties.isSchemeConfigured()) {
            return null;
        }

        LOG.debug(request.toString());

        return new ModelAndView(new RedirectView(samlClient.getRedirectUrl(request, response)));
    }
}
