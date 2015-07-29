package tw.edu.ncu.cc.oauth.resource.test

import org.springframework.mock.web.MockHttpServletRequest
import spock.lang.Specification
import tw.edu.ncu.cc.oauth.resource.core.ApiCredentialHolder

import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.*

class ApiAuthMockMvcRequestPostProcessorsTest extends Specification {

    def "it can add api token to mock request and credential holder"() {
        given:
            def mockrequest = Mock( MockHttpServletRequest )
        when:
            ApiAuthMockMvcRequestPostProcessors
                    .apiToken( "test-api-token" )
                    .clientId( "app" )
                    .clientTrusted()
                    .postProcessRequest( mockrequest )
        then:
            1 * mockrequest.addHeader( API_TOKEN_HEADER, "test-api-token" )
        when:
            def apiToken = ApiCredentialHolder.getApiToken( "test-api-token" )
        then:
            apiToken.client_id == "app"
            apiToken.client_trusted
    }

    def "it can add oauth token to mock request and crdential holder"() {
        given:
            def mockrequest = Mock( MockHttpServletRequest )
        when:
            ApiAuthMockMvcRequestPostProcessors
                    .accessToken( "test-access-token" )
                    .id( "abc" )
                    .user( "jason" )
                    .clientId( "app" )
                    .scope( [ "read" ] as String[] )
                    .postProcessRequest( mockrequest )
        then:
            1 * mockrequest.addHeader( ACCESS_TOKEN_HEADER, ACCESS_TOKEN_PREFIX + " test-access-token" )
        when:
            def accessToken = ApiCredentialHolder.getAccessToken( "test-access-token" )
        then:
            accessToken.id == "abc"
            accessToken.user == "jason"
            accessToken.client_id == "app"
            accessToken.scope.toList() == [ "read" ]
    }

}
