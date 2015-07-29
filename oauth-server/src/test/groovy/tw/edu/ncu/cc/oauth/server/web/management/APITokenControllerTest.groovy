package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute.getAPI_TOKEN_HEADER

class APITokenControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/api_tokens"

    @Transactional
    def "user can create api token by providing client id"() {
        given:
            def client = a_client()
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL + "?client_id=" + client.serialId )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            server().perform(
                    get( targetURL + "/token/${response.token}"  )
                            .header( API_TOKEN_HEADER, trusted_apiToken().token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isOk()
            )
    }

    @Transactional
    def "user cannot create api token if providing client is in blacklist"() {
        given:
            def client = restricted_client()
        expect:
            server().perform(
                    post( targetURL + "?client_id=" + client.serialId )
            ).andExpect(
                    status().isForbidden()
            ).andReturn()
    }

    @Transactional
    def "user can get api token info by token"() {
        given:
            def apiToken = a_apiToken()
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/token/" + apiToken.token )
                                    .header( API_TOKEN_HEADER, trusted_apiToken().token )
                                    .param( "ip", "127.0.0.1" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.client_id != null
        expect:
            server().perform(
                    get( targetURL + "/token/" + apiToken.token )
            ).andExpect(
                    status().isBadRequest()
            )
        and:
            server().perform(
                    get( targetURL + "/token/" + apiToken.token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isBadRequest()
            )
        and:
            server().perform(
                    get( targetURL + "/token/" + apiToken.token )
                            .header( API_TOKEN_HEADER, untrusted_apiToken().token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isForbidden()
            )
    }

    @Transactional
    def "user cannot get api token info if that api token reach api call limit"() {
        given:
            def apiToken = reach_limit_with_trustedAPI_apiToken()
        server().perform(
                get( targetURL + "/token/" + apiToken.token )
                        .header( API_TOKEN_HEADER, trusted_apiToken().token )
                        .param( "ip", "127.0.0.1" )
        ).andExpect(
                status().isForbidden()
        )
    }

    @Transactional
    def "user can revoke api token info by id"() {
        given:
            def apiToken = untrusted_apiToken()
        when:
            server().perform(
                    delete( targetURL + "/" + apiToken.id )
            ).andExpect(
                    status().isOk()
            )
        then:
            server().perform(
                    get( targetURL + "/token/" + apiToken.token )
                            .header( API_TOKEN_HEADER, trusted_apiToken().token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isNotFound()
            )
    }

    @Transactional
    def "user can refresh api token info by id"() {
        given:
            def apiToken = not_reach_limit_apiToken()
        expect:
            server().perform(
                    get( targetURL + "/token/" + apiToken.token )
                            .header( API_TOKEN_HEADER, trusted_apiToken().token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isOk()
            )
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL + "/" + apiToken.id + "/refresh" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            server().perform(
                    get( targetURL + "/token/" + apiToken.token )
                            .header( API_TOKEN_HEADER, trusted_apiToken().token )
                            .param( "ip", "127.0.0.1" )
            ).andExpect(
                    status().isNotFound()
            )
        and:
            response.token != null
    }

}
