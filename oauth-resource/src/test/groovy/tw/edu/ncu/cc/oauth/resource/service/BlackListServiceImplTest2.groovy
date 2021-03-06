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
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedIdObject
import tw.edu.ncu.cc.oauth.resource.config.RemoteConfig

import static org.mockserver.model.StringBody.json

class BlackListServiceImplTest2 extends Specification {

    @Shared @ClassRule
    ServerResource serverResource = new ServerResource( 8898 )

    BlackListService blackListService

    def setupSpec() {
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "POST" )
                        .withPath( "/management/v1/blacklist/users" )
                        .withBody( json( '{ "user_name" : "abc", "reason" : "test" }' )  )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 200 )
                        .withHeaders( new Header( "Content-Type", "application/json" ) )
                        .withBody( '{ "client_id" : "abc", "reason" : "test" }' )
        )
        serverResource.mockServer().when(
                HttpRequest.request()
                        .withMethod( "POST" )
                        .withPath( "/management/v1/blacklist/users" )
                        .withBody( json( '{ "user_name" : "not", "reason" : "test" }' )  )
        ).respond(
                HttpResponse.response()
                        .withStatusCode( 404 )
        )
    }

    def setup() {
        blackListService = new BlackListServiceImpl(
                new RemoteConfig(
                        serverPath: "http://localhost:" + serverResource.port()
                ),
                new RestTemplate()
        )
    }

    def "it can add user to blacklist"() {
        expect:
            blackListService.addUser( new UserRestrictedIdObject(
                    user_name: "abc",
                    reason: "test"
            ) ) != null
    }

    def "it throws exception if add nonexist user to blacklist"() {
        when:
            blackListService.addUser( new UserRestrictedIdObject(
                    user_name: "not",
                    reason: "test"
            ) )
        then:
            thrown( HttpClientErrorException )
    }

}
