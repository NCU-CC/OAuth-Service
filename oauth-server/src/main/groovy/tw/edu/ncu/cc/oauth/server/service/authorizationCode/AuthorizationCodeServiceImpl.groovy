package tw.edu.ncu.cc.oauth.server.service.authorizationCode

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.helper.data.SerialSecret
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCodeSpecifications
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.repository.model.AuthorizationCodeRepository
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

import javax.persistence.metamodel.Attribute

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class AuthorizationCodeServiceImpl implements AuthorizationCodeService {

    @Autowired
    def SecretService secretService

    @Autowired
    def AuthorizationCodeRepository authorizationCodeRepository

    @Override
    AuthorizationCode create( AuthorizationCode authorizationCode ) {
        String code = secretService.generateToken()
        authorizationCode.encryptedCode = secretService.encrypt( code )
        authorizationCodeRepository.save( authorizationCode )
        authorizationCode.code = secretService.encodeSerialSecret( new SerialSecret( authorizationCode.id, code ) )
        authorizationCode
    }

    @Override
    AuthorizationCode revoke( AuthorizationCode authorizationCode ) {
        authorizationCode.revoke()
        authorizationCodeRepository.save( authorizationCode )
    }

    @Override
    AuthorizationCode findUnexpiredByCode( String code, Attribute...attributes = [] ) {
        SerialSecret serialSecret = secretService.decodeSerialSecret( code )
        AuthorizationCode authorizationCode = findUnexpiredById( serialSecret.id as String, attributes )
        if( authorizationCode != null && secretService.matches( serialSecret.secret, authorizationCode.encryptedCode ) ) {
            return authorizationCode
        } else {
            return null
        }
    }

    @Override
    AuthorizationCode findUnexpiredById( String codeId, Attribute...attributes = [] ) {
        authorizationCodeRepository.findOne(
                where( AuthorizationCodeSpecifications.unexpired() )
                        .and( AuthorizationCodeSpecifications.idEquals( codeId as Integer ) )
                        .and( AuthorizationCodeSpecifications.include( attributes ) )
        )
    }

    @Override
    List< AuthorizationCode > findAllUnexpiredByUser( User user, Attribute...attributes = [] ) {
        authorizationCodeRepository.findAll(
                where( AuthorizationCodeSpecifications.unexpired() )
                        .and( AuthorizationCodeSpecifications.userEquals( user ) )
                        .and( AuthorizationCodeSpecifications.include( attributes ) )
        )
    }

    @Override
    List< AuthorizationCode > findAllUnexpiredByClient( Client client, Attribute...attributes = [] ) {
        authorizationCodeRepository.findAll(
                where( AuthorizationCodeSpecifications.unexpired() )
                        .and( AuthorizationCodeSpecifications.clientEquals( client ) )
                        .and( AuthorizationCodeSpecifications.include( attributes ) )
        )
    }

    @Override
    boolean isUnexpiredCodeMatchesClientId( String code, String clientID ) {
        AuthorizationCode authorizationCode = findUnexpiredByCode( code )
        return authorizationCode != null &&
               authorizationCode.client.serialId == clientID
    }

}
