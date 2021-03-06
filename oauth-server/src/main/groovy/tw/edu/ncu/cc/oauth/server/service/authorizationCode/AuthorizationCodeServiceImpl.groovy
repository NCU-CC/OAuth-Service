package tw.edu.ncu.cc.oauth.server.service.authorizationCode

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
        authorizationCode.encryptedCode = secretService.generateToken()
        authorizationCodeRepository.save( authorizationCode )
        authorizationCode.code = secretService.encryptOnce( authorizationCode.encryptedCode )
        authorizationCode
    }

    @Override
    AuthorizationCode revoke( AuthorizationCode authorizationCode ) {
        authorizationCode.revoke()
        authorizationCodeRepository.save( authorizationCode )
    }

    @Override
    AuthorizationCode refreshLastUsedTime( AuthorizationCode authorizationCode ) {
        authorizationCode.refreshLastUsedTime()
        authorizationCodeRepository.save( authorizationCode )
    }

    @Override
    AuthorizationCode findUnexpiredByCode( String code, Attribute...attributes = [] ) {
        authorizationCodeRepository.findOne(
                where( AuthorizationCodeSpecifications.unexpired() )
                        .and( AuthorizationCodeSpecifications.encryptedCodeEquals( secretService.decryptOnce( code ) ) )
                        .and( AuthorizationCodeSpecifications.include( attributes ) )
        )
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
