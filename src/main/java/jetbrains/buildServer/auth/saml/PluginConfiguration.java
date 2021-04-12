package jetbrains.buildServer.auth.saml;

import jetbrains.buildServer.controllers.AuthorizationInterceptor;
import jetbrains.buildServer.groups.UserGroupManager;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.auth.LoginConfiguration;
import jetbrains.buildServer.users.UserModel;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfiguration {

    @Bean
    public AuthenticationSchemeProperties authenticationSchemeProperties(SBuildServer sBuildServer, LoginConfiguration loginConfiguration) {
        return new AuthenticationSchemeProperties(sBuildServer, loginConfiguration);
    }

    @Bean
    public SAMLClient samlClient(AuthenticationSchemeProperties properties) {
        return new SAMLClient(properties);
    }

    @Bean
    public ServerPrincipalFactory serverPrincipalFactory(UserModel userModel, LocalGroupManager localGroupManager) {
        return new ServerPrincipalFactory(userModel, localGroupManager);
    }

    @Bean
    public SAMLController loginController(WebControllerManager webManager,
                                            AuthorizationInterceptor authInterceptor,
                                            AuthenticationSchemeProperties schemeProperties,
                                            SAMLClient samlClient) {
        return new SAMLController(webManager, authInterceptor, schemeProperties, samlClient);
    }

    @Bean
    public LoginViaSAMLLoginPageExtension loginPageExtension(PagePlaces pagePlaces,
                                                       PluginDescriptor pluginDescriptor,
                                                       AuthenticationSchemeProperties schemeProperties) {
        return new LoginViaSAMLLoginPageExtension(pagePlaces, pluginDescriptor, schemeProperties);
    }

    @Bean
    public SAMLAuthenticationScheme samlAuthenticationScheme(LoginConfiguration loginConfiguration,
                                                        PluginDescriptor pluginDescriptor,
                                                        ServerPrincipalFactory principalFactory,
                                                        SAMLClient samlClient,
                                                        AuthenticationSchemeProperties schemeProperties) {
        SAMLAuthenticationScheme authenticationScheme = new SAMLAuthenticationScheme(pluginDescriptor, principalFactory, samlClient, schemeProperties);
        loginConfiguration.registerAuthModuleType(authenticationScheme);
        return authenticationScheme;
    }

    @Bean
    public LocalGroupManager localGroupManager(UserGroupManager userGroupManager) {
        return new LocalGroupManager(userGroupManager);
    }
}
