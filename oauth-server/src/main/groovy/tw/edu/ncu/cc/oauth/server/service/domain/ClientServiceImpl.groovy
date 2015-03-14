package tw.edu.ncu.cc.oauth.server.service.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tw.edu.ncu.cc.oauth.server.domain.Client
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Service
@Transactional
class ClientServiceImpl implements ClientService {

    @Autowired
    def SecretService secretService

    @Override
    Client create( Client client ) {
        return generateSecretAndSave( client )
    }

    @Override
    Client refreshSecret( Client client ) {
        return generateSecretAndSave( client )
    }

    private Client generateSecretAndSave( Client client ) {
        String newSecret = secretService.generateToken()
        client.secret = secretService.encodeSecret( newSecret )
        client.save( failOnError: true, flush: true )
        client.discard()
        client.secret = newSecret
        return client
    }

    @Override
    Client update( Client client ) {
        return client.save( failOnError: true )
    }

    @Override
    Client delete( Client client ) {
        client.delete()
        return client
    }

    @Override
    Client readByID( String id, Map fetchOption = [:] ) {
        return Client.findById( id as long, [ fetch: fetchOption ] )
    }

    @Override
    boolean isIdSecretValid( String id, String secret ) {
        Client client = readByID( id )
        if( client == null ) {
            return false
        } else {
            return secretService.matchesSecret( secret, client.secret )
        }
    }

}