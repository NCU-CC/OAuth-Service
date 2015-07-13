package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserBlackListControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/blacklist/users"

    @Transactional
    def "user can get information of users in blacklist by user attributes 1"() {
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
    def "user can get information of users in blacklist by user attributes 2"() {
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "?user_name=ADMIN" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.size() != 0
    }

    @Transactional
    def "user can get user in blacklist information by username"() {
        given:
            def userRestricted = a_userRestricted()
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/" + userRestricted.user.name )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.reason == userRestricted.reason
    }

    @Transactional
    def "user can add user to blacklist"() {
        given:
            def user = get_user( 1 )
        when:
            def response = JSON(
                    server().perform(
                            post( targetURL )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content(
                                """
                                    {
                                      "user_name" : "${ user.name }",
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
                    get( targetURL + "/" + user.name )
            ).andExpect(
                    status().isOk()
            )
    }

    @Transactional
    def "user can update user information in blacklist"() {
        given:
            def userRestricted = a_userRestricted()
        when:
            def response = JSON(
                    server().perform(
                            put( targetURL + "/" + userRestricted.user.name )
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
                    get( targetURL + "/" + userRestricted.user.name )
            ).andExpect(
                    status().isOk()
            )
    }

    @Transactional
    def "user can remove user information in blacklist"() {
        given:
            def userRestricted = a_userRestricted()
        and:
            server().perform(
                    get( targetURL    + "/" + userRestricted.user.name )
            ).andExpect(
                    status().isOk()
            )
        when:
            server().perform(
                    delete( targetURL + "/" + userRestricted.user.name )
            ).andExpect(
                    status().isOk()
            )
        then:
            server().perform(
                    get( targetURL    + "/" + userRestricted.user.name )
            ).andExpect(
                    status().isNotFound()
            )
    }

}