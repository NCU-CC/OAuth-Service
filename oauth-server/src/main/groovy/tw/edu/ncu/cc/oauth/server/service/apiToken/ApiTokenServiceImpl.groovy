package tw.edu.ncu.cc.oauth.server.service.apiToken

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tw.edu.ncu.cc.oauth.server.helper.TimeBuilder
import tw.edu.ncu.cc.oauth.server.helper.data.SerialSecret
import tw.edu.ncu.cc.oauth.server.helper.data.TimeUnit
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiTokenSpecifications
import tw.edu.ncu.cc.oauth.server.repository.model.ApiTokenRepository
import tw.edu.ncu.cc.oauth.server.repository.model.ClientRepository
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

import javax.persistence.metamodel.Attribute

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class ApiTokenServiceImpl implements ApiTokenService {

    @Autowired
    def SecretService secretService

    @Autowired
    def ClientRepository clientRepository

    @Autowired
    def ApiTokenRepository apiTokenRepository

    @Override
    @Transactional
    ApiToken create( ApiToken apiToken ) {
        String token = secretService.generateToken()
        apiToken.encryptedToken = secretService.encrypt( token )
        apiToken.dateExpired = TimeBuilder.now().after( 36, TimeUnit.MONTH ).buildDate()
        apiTokenRepository.save( apiToken )
        apiToken.token = secretService.encodeSerialSecret( new SerialSecret( apiToken.id, token ) )
        apiToken
    }

    @Override
    ApiToken refreshToken( ApiToken apiToken ) {
        String token = secretService.generateToken()
        apiToken.encryptedToken = secretService.encrypt( token )
        apiTokenRepository.save( apiToken )
        apiToken.token = secretService.encodeSerialSecret( new SerialSecret( apiToken.id, token ) )
        apiToken
    }

    @Override
    ApiToken revoke( ApiToken apiToken ) {
        apiToken.revoke()
        apiTokenRepository.save( apiToken )
    }

    @Override
    @Transactional
    ApiToken findUnexpiredByToken( String token, Attribute...attributes = [] ) {
        SerialSecret serialSecret = secretService.decodeSerialSecret( token )
        ApiToken apiToken = findUnexpiredById( serialSecret.id as String, attributes )
        if( apiToken != null && secretService.matches( serialSecret.secret, apiToken.encryptedToken ) ) {
            apiToken.refreshTimeStamp()
            apiTokenRepository.save( apiToken )
        } else {
            return null
        }
    }

    @Override
    ApiToken findUnexpiredById( String id, Attribute...attributes = [] ) {
        apiTokenRepository.findOne(
                where( ApiTokenSpecifications.idEquals( id as Integer ) )
                        .and( ApiTokenSpecifications.unexpired() )
                        .and( ApiTokenSpecifications.include( attributes ) )
        )
    }

}
