package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client_
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

class ClientRestrictedServiceImplTest extends SpringSpecification {

    @Autowired
    def ClientService clientService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Transactional
    def "it can restrict a client"() {
        given:
            def client = a_client()
        when:
            clientRestrictedService.create(
                new ClientRestricted(
                        client: client,
                        reason: 'none'
                )
            )
        then:
            clientRestrictedService.findByClient( client ) != null
        when:
            def managedClient = clientService.findUndeletedBySerialId(
                    client.serialId, Client_.apiTokens, Client_.accessTokens , Client_.codes, Client_.refreshTokens
            )
        then:
            managedClient.accessTokens.size() == 0
            managedClient.apiTokens.size() == 0
            managedClient.codes.size() == 0
            managedClient.refreshTokens.size() == 0
    }

    @Transactional
    def "it can update client restrict information"() {
        given:
            def clientRestricted = getClientRestrciteds().findOne( 1 )
        and:
            def originReason = clientRestricted.reason
        when:
            clientRestricted.reason = originReason + "++"
        and:
            clientRestrictedService.update( clientRestricted )
        then:
            getClientRestrciteds().findOne( 1 ).reason != originReason
    }

    @Transactional
    def "it can unrestrict a client"() {
        given:
            def clientRestricted = getClientRestrciteds().findOne( 1 )
        when:
            clientRestrictedService.delete( clientRestricted )
        then:
            getClientRestrciteds().findOne( 1 ) == null
    }

    @Transactional
    def "it can find all restricted clients by some attributes"() {
        when:
            def results = clientRestrictedService.findAll( new ClientIdObject(), new PageRequest( 0, 3 ) )
        then:
            results.size() != 0
    }

    @Transactional
    def "it can check if a client is restricted"() {
        expect:
            ! clientRestrictedService.isClientRestricted( get_client( 1 ) )
            clientRestrictedService.isClientRestricted( get_client( 2 ) )
    }

}
