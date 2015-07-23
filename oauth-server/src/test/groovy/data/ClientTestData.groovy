package data

import org.springframework.beans.factory.annotation.Autowired
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

trait ClientTestData extends DomainTestData {

    @Autowired
    private SecretService secretService

    Client restricted_client() {
        new Client(
                id: 2,
                serialId: 'S2',
                name: "APP2",
                encryptedSecret: "SECRET",
                owner: getUsers().findOne( 2 ),
                callback: "http://example.com",
                url: "http://example.com",
                description: "2222",
                deleted: false
        )
    }

    Client new_client(){
        new Client(
                name: "HelloWorld",
                description: "description",
                callback: "abc://123",
                owner: getUsers().findOne( 1 ),
                url: "http://example.com",
                deleted: false
        )
    }

    Client a_client() {
        new Client(
                id: 3,
                serialId: 'S3',
                name: "APP3",
                encryptedSecret: "SECRET",
                owner: getUsers().findOne( 3 ),
                callback: "http://example.com",
                url: "http://example.com",
                description: "3333",
                deleted: false
        )
    }

    Client get_client( int id ) {
        def client = getClients().findOne( id )
        client.getOwner()
        client
    }

}