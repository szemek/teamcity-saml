package jetbrains.buildServer.auth.saml

import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.serverSide.auth.AuthModule
import jetbrains.buildServer.serverSide.auth.LoginConfiguration
import spock.lang.Specification

class AuthenticationSchemePropertiesTest extends Specification {

    Map<String, String> configuration = [:];
    AuthenticationSchemeProperties schemeProperties;

    def setup() {
        AuthModule authModule = Mock() {
            getProperties() >> configuration;
        }
        LoginConfiguration loginConfiguration = Mock() {
            getConfiguredAuthModules(_) >> [authModule]
            isGuestLoginAllowed() >> true
        }
        SBuildServer sBuildServer = Mock() { }
        schemeProperties = new AuthenticationSchemeProperties(sBuildServer, loginConfiguration)
    }

    def "configuration read allow insecure https"() {
        setup:
        configuration[ConfigKey.allowInsecureHttps.toString()] = value
        expect:
        schemeProperties.isAllowInsecureHttps() == expectedValue
        where:
        value   || expectedValue
        null    || true
        "false" || false
        "true"  || true
    }

    def "configuration read hide login dialog"() {
        setup:
        configuration[ConfigKey.hideLoginForm.toString()] = value
        expect:
        schemeProperties.isHideLoginForm() == expectedValue
        where:
        value   || expectedValue
        "false" || false
        "true"  || true
        null    || false
    }

    def "configuration read client settings"() {
        given:
        configuration[ConfigKey.spEntityId.toString()] = 'spEntityId'
        configuration[ConfigKey.acsUrl.toString()] = 'acsUrl'
        configuration[ConfigKey.idpEntityId.toString()] = 'idpEntityId'
        configuration[ConfigKey.idpSsoTargetUrl.toString()] = 'idpSsoTargetUrl'
        configuration[ConfigKey.idpCert.toString()] = 'idpCert'
        expect:
        schemeProperties.getSpEntityId() == 'spEntityId'
        schemeProperties.getAcsUrl() == 'acsUrl'
        schemeProperties.getIdpEntityId() == 'idpEntityId'
        schemeProperties.getIdpSsoTargetUrl() == 'idpSsoTargetUrl'
        schemeProperties.getIdpCert() == 'idpCert'
    }
}
