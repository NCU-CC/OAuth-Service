package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PermissionControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/permissions"

    @Transactional
    def "user can get all permission information"() {
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
    def "user can get permission information"() {
        given:
            def permission = a_permission()
        when:
            def response = JSON(
                    server().perform(
                            get( targetURL + "/${permission.name}" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.name == permission.name
    }

    @Transactional
    def "user can create permission 1"() {
        when:
            server().perform(
                    post( targetURL )
                    .contentType( MediaType.APPLICATION_JSON )
                    .content(
                        """
                        {
                          "name" : "NEWPERMISSIONNAME"
                        }
                        """
                    )
            ).andExpect(
                    status().isOk()
            )
        then:
            server().perform(
                    get( targetURL + "/NEWPERMISSIONNAME" )
            ).andExpect(
                    status().isOk()
            )
    }

    @Transactional
    def "user can create permission 2"() {
        given:
            def permission = a_permission()
        expect:
            server().perform(
                    post( targetURL )
                    .contentType( MediaType.APPLICATION_JSON )
                    .content(
                        """
                        {
                          "name" : "${permission.name}"
                        }
                        """
                    )
            ).andExpect(
                    status().isBadRequest()
            )
    }

    @Transactional
    def "user can delete permission 1"() {
        given:
            def permission = a_permission()
        when:
            def response = JSON(
                    server().perform(
                            delete( targetURL + "/${permission.name}" )
                    ).andExpect(
                            status().isOk()
                    ).andReturn()
            )
        then:
            response.name == permission.name
    }

    @Transactional
    def "user can delete permission 2"() {
        expect:
            server().perform(
                    delete( targetURL + "/PERMISSION_NOT_EXIST" )
            ).andExpect(
                    status().isNotFound()
            )
    }

}
