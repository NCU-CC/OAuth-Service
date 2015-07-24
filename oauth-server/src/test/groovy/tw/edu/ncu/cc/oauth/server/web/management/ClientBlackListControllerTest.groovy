package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ClientBlackListControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/blacklist/clients"

    @Transactional
    def "user can get information of clients in blacklist by client attributes 1"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.size() != 0
    }

    @Transactional
    def "user can get information of clients in blacklist by client attributes 2"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "?client_name=APP2" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.size() != 0
    }

    @Transactional
    def "user can get information of clients in blacklist by client attributes 3"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "?client_id=&client_owner=&client_name=APP2" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.size() != 0
    }

    @Transactional
    def "user can get client in blacklist information by serial id"() {
        given:
            def clientRestricted = a_clientRestricted()
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/" + clientRestricted.client.serialId )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.reason == clientRestricted.reason
    }

    @Transactional
    def "user can add client to blacklist"() {
        given:
            def client = get_client( 1 )
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content(
                                """
                                    {
                                      "client_id" : "${ client.serialId }",
                                      "reason" : "other reason"
                                    }
                                """
                            )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.reason == "other reason"
        and:
            server().perform(
                    get( targetURL + "/" + client.serialId )
            ).andExpect(
                    status().isOk()
            )
    }

    @Transactional
    def "user cannot add client which is already restricted to blacklist "() {
        given:
            def client = a_clientRestricted().client
        expect:
            server().perform(
                    post( targetURL )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content(
                                """
                                    {
                                      "client_id" : "${ client.serialId }",
                                      "reason" : "other reason"
                                    }
                                """
                    )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    @Transactional
    def "user can update client information in blacklist"() {
        given:
            def clientRestricted = a_clientRestricted()
        when:
            def response = JSON(
                    server().perform(
                            put( targetURL + "/" + clientRestricted.client.serialId )
                                    .contentType( MediaType.APPLICATION_JSON )
                                    .content(
                                    """
                                    {
                                      "reason" : "another reason"
                                    }
                                """
                            )
                    ).andReturn()
            )
        then:
            response.reason == "another reason"
        and:
            server().perform(
                    get( targetURL + "/" + clientRestricted.client.serialId )
            ).andExpect(
                    status().isOk()
            )
    }

    @Transactional
    def "user can remove client information in blacklist"() {
        given:
            def clientRestricted = a_clientRestricted()
        and:
            server().perform(
                    get( targetURL    + "/" + clientRestricted.client.serialId )
            ).andExpect(
                    status().isOk()
            )
        when:
            server().perform(
                    delete( targetURL + "/" + clientRestricted.client.serialId )
            ).andExpect(
                    status().isOk()
            )
        then:
            server().perform(
                    get( targetURL    + "/" + clientRestricted.client.serialId )
            ).andExpect(
                    status().isNotFound()
            )
    }

}