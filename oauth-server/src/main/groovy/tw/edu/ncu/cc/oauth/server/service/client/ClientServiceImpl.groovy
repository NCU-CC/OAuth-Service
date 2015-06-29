package tw.edu.ncu.cc.oauth.server.service.client

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.client.ClientSpecifications
import tw.edu.ncu.cc.oauth.server.repository.model.ClientRepository
import tw.edu.ncu.cc.oauth.server.service.security.SecretService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

import javax.persistence.metamodel.Attribute

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class ClientServiceImpl implements ClientService {

    @Autowired
    def UserService userService

    @Autowired
    def SecretService secretService

    @Autowired
    def ClientRepository clientRepository

    @Override
    Client create( Client client ) {
        String newSecret = secretService.generateToken()
        client.encryptedSecret = secretService.encrypt( newSecret )
        clientRepository.save( client )
    }

    @Override
    Client refreshSecret( Client client ) {
        String newSecret = secretService.generateToken()
        client.encryptedSecret = secretService.encrypt( newSecret )
        clientRepository.save( client )
    }

    @Override
    Client update( Client client ) {
        clientRepository.save( client )
    }

    @Override
    Client delete( Client client ) {
        client.deleted = true
        clientRepository.save( client )
    }

    @Override
    @Transactional
    void revokeTokens( Client client ) {
        clientRepository.revokeAuthorizationCodes( client )
        clientRepository.revokeAccessTokens( client )
        clientRepository.revokeApiTokens( client )
        clientRepository.revokeRefreshTokens( client )
    }

    @Override
    Client findUndeletedBySerialId( String serialId, Attribute... attributes = [] ) {
        long id = secretService.decodeHashId( serialId )
        clientRepository.findOne(
                where( ClientSpecifications.idEquals( id as Integer ) )
                        .and( ClientSpecifications.undeleted() )
                        .and( ClientSpecifications.include( attributes ) )
        )
    }

    @Override
    boolean isCredentialValid( String serialId, String secret ) {
        Client client = findUndeletedBySerialId( serialId )
        if ( client == null ) {
            return false
        } else {
            return secretService.matches( secret, client.encryptedSecret )
        }
    }

    @Override
    List< Client > findAllByDataObject( ClientIdObject clientObject, Attribute... attributes = [] ) {
        clientRepository.findAll(
                where( ClientSpecifications.attributes(
                        clientObject.id,
                        clientObject.name,
                        StringUtils.isEmpty( clientObject.owner ) ? null : userService.findByName( clientObject.owner ),
                        clientObject.deleted
                ) )
                .and( ClientSpecifications.include( attributes ) )
        )
    }

}
