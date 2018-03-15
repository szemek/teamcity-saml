package jetbrains.buildServer.auth.saml;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.auth.AuthModule;
import jetbrains.buildServer.serverSide.auth.LoginConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class AuthenticationSchemeProperties {
    private final SBuildServer sBuildServer;
    private final LoginConfiguration loginConfiguration;

    public AuthenticationSchemeProperties(@NotNull final SBuildServer sBuildServer, @NotNull final LoginConfiguration loginConfiguration) {
        this.sBuildServer = sBuildServer;
        this.loginConfiguration = loginConfiguration;
    }

    @Nullable
    public String getSpEntityId() { return getProperty(ConfigKey.spEntityId); }

    @Nullable
    public String getAcsUrl() { return getProperty(ConfigKey.acsUrl); }

    @Nullable
    public String getIdpEntityId() { return getProperty(ConfigKey.idpEntityId); }

    @Nullable
    public String getIdpSsoTargetUrl() { return getProperty(ConfigKey.idpSsoTargetUrl); }

    @Nullable
    public String getIdpCert() { return getProperty(ConfigKey.idpCert); }

    public boolean isHideLoginForm() {
        return Boolean.valueOf(getProperty(ConfigKey.hideLoginForm));
    }

    public boolean isSchemeConfigured() {
        return !loginConfiguration.getConfiguredAuthModules(SAMLAuthenticationScheme.class).isEmpty();
    }

    private String getProperty(ConfigKey key) {
        Map<String, String> properties = getSchemeProperties();
        return properties == null ? null : properties.get(key.toString());

    }

    @Nullable
    private Map<String, String> getSchemeProperties() {
        List<AuthModule<SAMLAuthenticationScheme>> authModules = loginConfiguration.getConfiguredAuthModules(SAMLAuthenticationScheme.class);
        return authModules.isEmpty() ? null : authModules.get(0).getProperties();
    }

    public boolean isAllowInsecureHttps() {
        return Optional.ofNullable(getProperty(ConfigKey.allowInsecureHttps))
                .map(Boolean::valueOf)
                .orElse(true);
    }
}
