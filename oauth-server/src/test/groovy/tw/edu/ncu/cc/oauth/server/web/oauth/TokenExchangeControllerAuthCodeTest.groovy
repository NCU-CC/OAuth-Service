package tw.edu.ncu.cc.oauth.server.web.oauth

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TokenExchangeControllerAuthCodeTest extends IntegrationSpecification {

    def targetURL = "/oauth/token"

    def "it should restrict the request of invalid grant_type"() {
        given:
            def authorizationCode = a_authorizationCode()
            def client = a_client()
        expect:
            server().perform(
                    post( targetURL )
                            .param( "grant_type", "error" )
                            .param( "client_id", authorizationCode.client.serialId )
                            .param( "client_secret", client.secret )
                            .param( "code", authorizationCode.code )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    def "it should restrict the request of invalid client"() {
        given:
            def authorizationCode = a_authorizationCode()
            def client = a_client()
        expect:
            server().perform(
                    post( targetURL )
                            .param( "grant_type", "authorization_code" )
                            .param( "client_id", 'GG' )
                            .param( "client_secret", client.secret )
                            .param( "code", authorizationCode.code )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    def "it should restrict the request of invalid auth code"() {
        given:
            def authorizationCode = a_authorizationCode()
            def client = a_client()
        expect:
            server().perform(
                    post( targetURL )
                            .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                            .param( "grant_type", "authorization_code" )
                            .param( "client_id", authorizationCode.client.serialId )
                            .param( "client_secret", client.secret )
                            .param( "code", "INVALID" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    @Transactional
    def "it can exchange access token and refresh token with auth code"() {
        given:
            def authorizationCode = a_authorizationCode()
            def client = a_client()
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL )
                                    .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                                    .param( "grant_type", "authorization_code" )
                                    .param( "client_id", authorizationCode.client.serialId )
                                    .param( "client_secret", client.secret )
                                    .param( "code", authorizationCode.code )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.access_token != null
            response.refresh_token != null
    }

}
