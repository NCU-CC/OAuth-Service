package tw.edu.ncu.cc.oauth.server.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.server.repository.model.ClientRestrictedRepository

class ClientRestrcitedRepositoryTest extends SpringSpecification {

    @Autowired
    ClientRestrictedRepository clientRestrcitedRepository

    @Transactional
    def "it can map to exist data"() {
        when:
            def clientRestricted = clientRestrcitedRepository.findOne( 1 )
        then:
            clientRestricted.client.id == 2
    }

}
