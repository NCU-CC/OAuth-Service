package tw.edu.ncu.cc.oauth.server.web.oauth

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TokenExchangeControllerRefreshTokenTest extends IntegrationSpecification {

    def targetURL = "/oauth/token"

    def "it should restrict the request of invalid client"() {
        given:
            def refreshToken = a_refreshToken()
            def client = a_client()
        expect:
            server().perform(
                    post( targetURL )
                            .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                            .param( "grant_type", "refresh_token" )
                            .param( "client_id", 'GG' )
                            .param( "client_secret", client.secret )
                            .param( "refresh_token", refreshToken.token )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    def "it should restrict the request of invalid refresh token"() {
        given:
            def refreshToken = a_refreshToken()
            def client = a_client()
        expect:
            server().perform(
                    post( targetURL )
                            .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                            .param( "grant_type", "refresh_token" )
                            .param( "client_id", refreshToken.client.serialId )
                            .param( "client_secret", client.secret )
                            .param( "refresh_token", "INVALID" )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    @Transactional
    def "it can exchange access token with refresh token"() {
        given:
            def refreshToken = a_refreshToken()
            def client = a_client()
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL )
                                    .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                                    .param( "grant_type", "refresh_token" )
                                    .param( "client_id", refreshToken.client.serialId )
                                    .param( "client_secret", client.secret )
                                    .param( "refresh_token", refreshToken.token )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.access_token != null
    }

}
