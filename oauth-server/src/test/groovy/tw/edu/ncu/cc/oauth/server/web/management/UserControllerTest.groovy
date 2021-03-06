package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class UserControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/users"

    def "user can get user's information 1"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/ADMIN1" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.name == "ADMIN1"
    }

    def "user can get user's information 2"() {
        expect:
            server().perform(
                    get( targetURL + "/NOT_EXIST_USER" )
            ).andExpect(
                    status().isNotFound()
            )
    }

    def "user can get user's tokens 1"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/ADMIN1/authorized_tokens" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].user == "ADMIN1"
    }

    def "user can get specified user's tokens 2"() {
        expect:
            server().perform(
                    get( targetURL + "/NOT_EXIST/authorized_tokens" )
            ).andExpect(
                    status().isNotFound()
            )
    }

    def "user can get specified user's clients 1"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/ADMIN1/clients" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response[0].owner == "ADMIN1"
    }

    @Transactional
    def "user can create new user"() {
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL )
                                    .contentType( MediaType.APPLICATION_JSON )
                                    .content(
                                    '''
                                    {
                                      "name" : "jason"
                                    }
                                    '''
                            )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.name == "jason"
    }

    @Transactional
    def "managers can search user by partial username"() {
        when:
            server().perform(
                post( targetURL )
                    .contentType( MediaType.APPLICATION_JSON )
                    .content(
                    '''
                        {
                          "name" : "jason"
                        }
                    '''
                )
            ).andExpect(
                status().isOk()
            )
        and:
            def response = JSON(
                    server().perform(
                        get( targetURL + "?name=jas" )
                    ).andExpect(
                         status().isOk()
                    ).andReturn()
            )
        then:
            response[0].name == "jason"
    }
}