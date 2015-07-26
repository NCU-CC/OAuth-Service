package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specifications
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLogSpecifications
import tw.edu.ncu.cc.oauth.server.service.tokenAccessLog.TokenAccessLogService

class TokenAccessLogServiceImplTest extends SpringSpecification {

    @Autowired
    def TokenAccessLogService tokenAccessLogService

    @Transactional
    def "it can create token access log"() {
        given:
            def log = new TokenAccessLog(
                    tokenType: "type",
                    tokenId: 1,
                    client: get_client( 1 ),
                    application: get_client( 3 ),
                    ip: "192.168.0.1",
                    referer: "www.example.com"
            )
        when:
            tokenAccessLogService.create( log )
        then:
            tokenAccessLogService.findAll(
                    Specifications.where(
                            TokenAccessLogSpecifications.applicationEquals( get_client( 3 ).name )
                    ),
                    new PageRequest( 0, 1 )
            ).size() != 0
    }
}
