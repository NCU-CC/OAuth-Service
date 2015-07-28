package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.API_TOKEN_HEADER

class AccessTokenControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/access_tokens"

    @Transactional
    def "user can get access token info by token"() {
        given:
            def accessToken = a_accessToken()
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/token/" + accessToken.token )
                                    .header( API_TOKEN_HEADER, trusted_apiToken().token )
                                    .param( "ip", "127.0.0.1" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.user == accessToken.user.name
    }

    @Transactional
    def "user cannot get access token info by token if invalid authentication"() {
        given:
            def accessToken = a_accessToken()
        expect:
            server().perform(
                    get( targetURL + "/token/" + accessToken.token )
            ).andExpect(
                    status().isBadRequest()
            )
        and:
            server().perform(
                    get( targetURL + "/token/" + accessToken.token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isBadRequest()
            )
        and:
            server().perform(
                    get( targetURL + "/token/" + accessToken.token )
                            .param( "ip", "127.0.0.1" )
                            .header( API_TOKEN_HEADER, untrusted_apiToken().token )
            ).andExpect(
                    status().isForbidden()
            )

    }

}