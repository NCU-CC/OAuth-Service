package tw.edu.ncu.cc.oauth.resource.service

import org.junit.ClassRule
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import resource.ServerResource
import spock.lang.Shared
import spock.lang.Specification
import tw.edu.ncu.cc.oauth.data.v1.management.resource.TokenRequestMetaObject
import tw.edu.ncu.cc.oauth.resource.config.RemoteConfig

import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.API_TOKEN_HEADER

class TokenConfirmServiceImplTest1 extends Specification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898 )

    TokenConfirmService tokenConfirmService

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/management/v1/access_tokens/token/token1" )
                        .withHeader( new Header( API_TOKEN_HEADER, "abc123" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                        '''
                        {
                            "id" : "1",
                            "user" : "101502549",
                            "scope" : [ "READ", "WRITE" ],
                            "client_id" : "abc",
                            "last_updated" : "2014-12-15"
                        }
                        '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/management/v1/access_tokens/token/token2" )
                        .withHeader( new Header( API_TOKEN_HEADER, "abc123" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 404 )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/management/v1/access_tokens/token/token3" )
                        .withHeader( new Header( API_TOKEN_HEADER, "abc123" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 403 )
        )
    }

    def setup() {
        tokenConfirmService = new TokenConfirmServiceImpl(
                new RemoteConfig(
                        serverPath: "http://localhost:" + serverResource.port(),
                        apiToken: "abc123"
                ),
                new RestTemplate()
        )
    }

    def "it can get access token from remote server 1"() {
        expect:
            tokenConfirmService.readAccessToken( "token1", new TokenRequestMetaObject(), false ).scope.toList() == [ "READ", "WRITE" ]
    }

    def "it throws exception if token is not found"() {
        when:
            tokenConfirmService.readAccessToken( "token2", new TokenRequestMetaObject(), false )
        then:
            thrown( HttpClientErrorException )
    }

    def "it throws exception if token is forbidden"() {
        when:
            tokenConfirmService.readAccessToken( "token3", new TokenRequestMetaObject(), false )
        then:
            thrown( HttpClientErrorException )
    }

}
