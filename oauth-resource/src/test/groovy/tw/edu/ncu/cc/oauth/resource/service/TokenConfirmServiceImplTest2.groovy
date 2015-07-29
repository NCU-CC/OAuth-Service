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

import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.getAPI_TOKEN_HEADER

class TokenConfirmServiceImplTest2 extends Specification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898 )

    TokenConfirmService tokenConfirmService

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/management/v1/api_tokens/token/token1" )
                        .withHeader( new Header( API_TOKEN_HEADER, "abc123" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody(
                        '''
                        {
                            "id" : "123",
                            "client_id" : "abc",
                            "last_updated" : "2014-12-15"
                        }
                        '''
                )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/management/v1/api_tokens/token/token2" )
                        .withHeader( new Header( API_TOKEN_HEADER, "abc123" ) )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 404 )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "GET" )
                        .withPath( "/management/v1/api_tokens/token/token3" )
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

    def "it can get api token from remote server 1"() {
        expect:
            tokenConfirmService.readApiToken( "token1", new TokenRequestMetaObject() ).client_id == "abc"
    }

    def "it throws exception if token is not found"() {
        when:
            tokenConfirmService.readApiToken( "token2", new TokenRequestMetaObject() ) == null
        then:
            thrown( HttpClientErrorException )
    }

    def "it throws exception if token is forbidden"() {
        when:
            tokenConfirmService.readApiToken( "token3", new TokenRequestMetaObject() ) == null
        then:
            thrown( HttpClientErrorException )
    }

}
