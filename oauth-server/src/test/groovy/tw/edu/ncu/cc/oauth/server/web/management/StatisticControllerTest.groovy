package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import specification.IntegrationSpecification
import tw.edu.ncu.cc.oauth.server.repository.model.TokenAccessLogRepository

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class StatisticControllerTest extends IntegrationSpecification {

    def targetURL = "/management/v1/statistic/clients"

    @Autowired
    TokenAccessLogRepository tokenAccessLogRepository

    @Transactional
    def "user can read client's token access log"() {
        given:
            def log = tokenAccessLogRepository.findOne( 1 )
        expect:
            server().perform(
                    get( targetURL + "/" + serialId( log.client.id ) + "/tokens" )
                            .param( "ip", log.ip )
                            .param( "application", log.application )
            ).andExpect(
                    status().isOk()
            ).andReturn()
    }

}
