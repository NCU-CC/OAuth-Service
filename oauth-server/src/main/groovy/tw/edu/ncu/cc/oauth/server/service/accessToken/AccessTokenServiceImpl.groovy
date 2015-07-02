package tw.edu.ncu.cc.oauth.server.service.accessToken

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.helper.data.SerialSecret
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessTokenSpecifications
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.repository.model.AccessTokenRepository
import tw.edu.ncu.cc.oauth.server.repository.model.RefreshTokenRepository
import tw.edu.ncu.cc.oauth.server.service.authorizationCode.AuthorizationCodeService
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

import javax.persistence.metamodel.Attribute

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class AccessTokenServiceImpl implements AccessTokenService {

    @Autowired
    def AccessTokenRepository accessTokenRepository

    @Autowired
    def SecretService secretService

    @Autowired
    def RefreshTokenRepository refreshTokenRepository

    @Autowired
    def AuthorizationCodeService authorizationCodeService

    @Override
    AccessToken create( AccessToken accessToken ) {
        String token = secretService.generateToken()
        accessToken.encryptedToken = secretService.encrypt( token )
        accessTokenRepository.save( accessToken )
        accessToken.token = secretService.encodeSerialSecret( new SerialSecret( accessToken.id, token ) )
        accessToken
    }

    @Override
    AccessToken createByAuthorizationCode( AccessToken accessToken, AuthorizationCode authorizationCode ) {
        authorizationCodeService.revoke( authorizationCode )
        accessToken.client = authorizationCode.client
        accessToken.scope = authorizationCode.scope.collect().toSet()
        accessToken.user = authorizationCode.user
        return create( accessToken )
    }

    @Override
    AccessToken createByRefreshToken( AccessToken accessToken, RefreshToken refreshToken ) {
        accessToken.client = refreshToken.client
        accessToken.scope = refreshToken.scope.collect().toSet()
        accessToken.user = refreshToken.user
        create( accessToken )
        revoke( refreshToken.accessToken )
        refreshToken.accessToken = accessToken
        refreshTokenRepository.save( refreshToken )
        accessToken
    }

    @Override
    AccessToken revoke( AccessToken accessToken ) {
        accessToken.revoke()
        accessTokenRepository.save( accessToken )
    }

    @Override
    AccessToken findUnexpiredByToken( String token, Attribute...attributes = [] ) {
        SerialSecret serialSecret = secretService.decodeSerialSecret( token )
        AccessToken accessToken = findUnexpiredById( serialSecret.id as String, attributes )
        if( accessToken != null && secretService.matches( serialSecret.secret, accessToken.encryptedToken ) ) {
            accessToken.refreshTimeStamp()
            accessTokenRepository.save( accessToken )
        } else {
            return null
        }
    }

    @Override
    AccessToken findUnexpiredById( String tokenId, Attribute...attributes = [] ) {
        accessTokenRepository.findOne(
                where( AccessTokenSpecifications.unexpired() )
                        .and( AccessTokenSpecifications.idEquals( tokenId as Integer ) )
                        .and( AccessTokenSpecifications.include( attributes ) )
        )
    }

    @Override
    List< AccessToken > findAllUnexpiredByUser( User user, Attribute...attributes= [] ) {
        accessTokenRepository.findAll(
                where( AccessTokenSpecifications.unexpired() )
                        .and( AccessTokenSpecifications.userEquals( user ) )
                        .and( AccessTokenSpecifications.include( attributes ) )
        )
    }

    @Override
    List< AccessToken > findAllUnexpiredByClient( Client client, Attribute...attributes = [] ) {
        accessTokenRepository.findAll(
                where( AccessTokenSpecifications.unexpired() )
                        .and( AccessTokenSpecifications.clientEquals( client ) )
                        .and( AccessTokenSpecifications.include( attributes ) )
        )
    }

}
