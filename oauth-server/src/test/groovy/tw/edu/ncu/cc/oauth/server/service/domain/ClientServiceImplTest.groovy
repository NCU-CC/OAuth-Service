package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import specification.SpringSpecification
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client_
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

class ClientServiceImplTest extends SpringSpecification {

    @Autowired
    ClientService clientService

    @Transactional
    def "it can create client"() {
        given:
            def client = new_client()
        when:
            def createdClient = clientService.create( client )
        then:
            createdClient.name       == client.name
            createdClient.owner.name == client.owner.name
    }

    @Transactional
    def "it can update exist client"() {
        given:
            def managedClient = clientService.findUndeletedBySerialId( a_client().serialId )
        when:
            managedClient.name = 'newname'
        and:
            clientService.update( managedClient )
        then:
            clientService.findUndeletedBySerialId( managedClient.serialId ).name == 'newname'
    }

    @Transactional
    def "it can delete client"() {
        given:
            def client = new_client()
        and:
            def createdClient = clientService.create( client )
        when:
            clientService.delete( createdClient )
        then:
            clientService.findUndeletedBySerialId( createdClient.serialId ) == null
    }

    def "it can validate the client id and secret"() {
        given:
            def client = a_client()
        expect:
            clientService.isCredentialValid( client.serialId, client.secret )
            ! clientService.isCredentialValid( client.serialId, "SECR" )
    }

    @Transactional
    def "it can refresh client secret"() {
        given:
            def client = new_client()
        and:
            def createdClient = clientService.create( client )
            def createdClientSerialId = createdClient.serialId
        and:
            def originEncryptedSecret = clientService.findUndeletedBySerialId( createdClientSerialId ).encryptedSecret
        when:
            clientService.refreshSecret( clientService.findUndeletedBySerialId( createdClientSerialId ) )
        then:
            clientService.findUndeletedBySerialId( createdClientSerialId ).encryptedSecret != originEncryptedSecret
    }

    @Transactional
    def "it can find clients by partial name"() {
        given:
            def client = new_client()
        and:
            clientService.create(client)

            def dto = new ClientIdObject(
                    name : client.name.substring( client.name.length() - 1 ),
                    deleted: false
            )
        when:
            def results = clientService.findAllByDataObject( dto )
        then:
            results.size() == 1
            results[0].name == client.name
    }

    @Transactional
    def "it can find clients by partial name, full id and full owner name"() {
        given:
            def client = new_client()
        and:
            def createdClient = clientService.create( client )

            def dto = new ClientIdObject(
                    name : createdClient.name.substring( createdClient.name.length() - 1 ),
                    id : createdClient.serialId,
                    owner : createdClient.owner.name,
                    deleted: false
            )

        when:
            def results = clientService.findAllByDataObject(dto)
        then:
            results.size() == 1
            results[0].name == createdClient.name
            results[0].id == createdClient.id
            results[0].owner == createdClient.owner
    }

    @Transactional
    def "it should find nothing because client is deleted"() {
        given:
            def client = new_client()
            client.deleted = true
        and:
            def createdClient = clientService.create(client)

            def dto = new ClientIdObject(
                    name : createdClient.name.substring( createdClient.name.length() - 1 ),
                    id : createdClient.serialId,
                    owner : createdClient.owner.name,
                    deleted: false
            )

        when:
            def results = clientService.findAllByDataObject( dto )
        then:
            results.isEmpty()
    }

    @Transactional
    def "it can revoke all tokens of client"() {
        given:
            def client = get_client( 1 )
        when:
            clientService.revokeTokens( client )
        and:
            def managedClient = clientService.findUndeletedBySerialId(
                    client.serialId, Client_.apiTokens, Client_.accessTokens , Client_.codes, Client_.refreshTokens
            )
        then:
            managedClient.accessTokens.size() == 0
            managedClient.apiTokens.size() == 0
            managedClient.codes.size() == 0
            managedClient.refreshTokens.size() == 0
    }

}
