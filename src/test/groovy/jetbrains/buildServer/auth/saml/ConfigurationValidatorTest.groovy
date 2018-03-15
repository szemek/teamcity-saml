package jetbrains.buildServer.auth.saml

import spock.lang.Specification
import spock.lang.Unroll

class ConfigurationValidatorTest extends Specification {

    Map<String, String> configuration = [:];

    def setup() {
        configuration[ConfigKey.spEntityId.toString()] = 'spEntityId'
        configuration[ConfigKey.acsUrl.toString()] = 'acsUrl'
        configuration[ConfigKey.idpEntityId.toString()] = 'idpEntityId'
        configuration[ConfigKey.idpSsoTargetUrl.toString()] = 'idpSsoTargetUrl'
        configuration[ConfigKey.idpCert.toString()] = 'idpCert'
    }

    def "configuration is valid"() {
        expect:
        new ConfigurationValidator().validate(configuration).isEmpty();
    }

    @Unroll
    def "configuration is not valid without #key"() {
        when:
        configuration.remove(key.toString())
        List<String> errors = new ConfigurationValidator().validate(configuration)
        then:
        !errors.isEmpty()
        errors.size() == 1
        errors.get(0) == msg

        where:
        key                          || msg
        ConfigKey.spEntityId         || "SP Entity ID should be specified."
        ConfigKey.acsUrl             || "ACS URL should be specified."
        ConfigKey.idpEntityId        || "IdP Entity ID should be specified."
        ConfigKey.idpSsoTargetUrl    || "IdP SSO Target Url secret should be specified."
        ConfigKey.idpCert            || "IdP Certificate should be specified."
    }

}
