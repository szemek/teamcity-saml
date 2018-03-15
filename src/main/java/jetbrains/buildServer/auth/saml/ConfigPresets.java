package jetbrains.buildServer.auth.saml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class ConfigPresets {
    private final Map<String, Map<ConfigKey, String>> presets;

    ConfigPresets() {
        presets = new HashMap<>();
        installPreset("onelogin",
            "http://localhost:8111/saml/acs",
            "https://app.onelogin.com/saml/metadata/<onelogin-app-id>",
            "https://app.onelogin.com/trust/saml2/http-post/sso/<onelogin-app-id>",
            "-----BEGIN CERTIFICATE----- ... -----END CERTIFICATE-----");
    }

    private void installPreset(String name, String acsUrl, String idpEntityId, String idpSsoTargetUrl, String idpCert) {
        Map<ConfigKey, String> preset = new HashMap<>();
        preset.put(ConfigKey.acsUrl, acsUrl);
        preset.put(ConfigKey.idpEntityId, idpEntityId);
        preset.put(ConfigKey.idpSsoTargetUrl, idpSsoTargetUrl);
        preset.put(ConfigKey.idpCert, idpCert);
        presets.put(name, preset);
    }

    Optional<String> getPresetKey(String name, ConfigKey key) {
        Map<ConfigKey, String> preset = presets.get(name);
        return preset == null ? Optional.empty() : Optional.ofNullable(preset.get(key));
    }

    Map<ConfigKey, String> getPreset(String name) {
        return presets.getOrDefault(name, Collections.emptyMap());
    }

}
