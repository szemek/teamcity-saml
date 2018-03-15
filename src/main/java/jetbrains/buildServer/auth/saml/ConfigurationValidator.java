package jetbrains.buildServer.auth.saml;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationValidator {

    private static final Map<ConfigKey, String> EMPTY_KEY_VALUE_MESSAGES;

    static {
        EMPTY_KEY_VALUE_MESSAGES = new HashMap<>();
        EMPTY_KEY_VALUE_MESSAGES.put(ConfigKey.spEntityId, "SP Entity ID should be specified.");
        EMPTY_KEY_VALUE_MESSAGES.put(ConfigKey.acsUrl, "ACS URL should be specified.");
        EMPTY_KEY_VALUE_MESSAGES.put(ConfigKey.idpEntityId, "IdP Entity ID should be specified.");
        EMPTY_KEY_VALUE_MESSAGES.put(ConfigKey.idpSsoTargetUrl, "IdP SSO Target Url secret should be specified.");
        EMPTY_KEY_VALUE_MESSAGES.put(ConfigKey.idpCert, "IdP Certificate should be specified.");
    }

    public Collection<String> validate(@NotNull Map<String, String> properties) {
        return EMPTY_KEY_VALUE_MESSAGES.entrySet().stream()
                .filter(mapping -> StringUtil.isEmptyOrSpaces(properties.get(mapping.getKey().toString())))
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
