package jetbrains.buildServer.auth.saml

import org.assertj.core.util.Files
import org.json.simple.JSONValue
import org.springframework.core.io.DefaultResourceLoader
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset

class SAMLUserTest extends Specification {
    def "should read user details"() {
        setup:
        def user = new SAMLUser('user')

        expect:
        user.id == 'user'
        user.name == null
        user.email == null
    }

    def "should return name if id is not given"() {
        expect:
        new SAMLUser(null, 'name', 'email@domain').id == 'email@domain'
    }
}
