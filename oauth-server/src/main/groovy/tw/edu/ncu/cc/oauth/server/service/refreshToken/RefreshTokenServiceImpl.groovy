package tw.edu.ncu.cc.oauth.server.service.refreshToken

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshTokenSpecifications
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.repository.model.AccessTokenRepository
import tw.edu.ncu.cc.oauth.server.repository.model.RefreshTokenRepository
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

import javax.persistence.metamodel.Attribute

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    def SecretService secretService

    @Autowired
    RefreshTokenRepository refreshTokenRepository

    @Autowired
    AccessTokenRepository accessTokenRepository

    @Override
    RefreshToken createByAccessToken( RefreshToken refreshToken, AccessToken accessToken ) {
        refreshToken.accessToken = accessToken
        refreshToken.client = accessToken.client
        refreshToken.scope = accessToken.scope.collect().toSet()
        refreshToken.user = accessToken.user
        create( refreshToken )
    }

    private RefreshToken create( RefreshToken refreshToken ) {
        refreshToken.encryptedToken = secretService.generateToken()
        refreshTokenRepository.save( refreshToken )
        refreshToken.token = secretService.encryptOnce( refreshToken.encryptedToken )
        refreshToken
    }


    @Override
    RefreshToken revoke( RefreshToken refreshToken ) {
        refreshToken.accessToken.revoke()
        refreshToken.revoke()
        accessTokenRepository.save( refreshToken.accessToken )
        refreshTokenRepository.save( refreshToken )
    }

    @Override
    RefreshToken refreshLastUsedTime( RefreshToken refreshToken ) {
        refreshToken.refreshLastUsedTime()
        refreshTokenRepository.save( refreshToken )
    }

    @Override
    RefreshToken findUnexpiredByToken( String token, Attribute...attributes = [] ) {
        refreshTokenRepository.findOne(
                where( RefreshTokenSpecifications.unexpired() )
                        .and( RefreshTokenSpecifications.encryptedTokenEquals( secretService.decryptOnce( token ) ) )
                        .and( RefreshTokenSpecifications.include( attributes ) )
        )
    }

    @Override
    RefreshToken findUnexpiredById( String tokenId, Attribute...attributes = [] ) {
        refreshTokenRepository.findOne(
                where( RefreshTokenSpecifications.unexpired() )
                        .and( RefreshTokenSpecifications.idEquals( tokenId as Integer ) )
                        .and( RefreshTokenSpecifications.include( attributes ) )
        )
    }

    @Override
    List< RefreshToken > findAllUnexpiredByUser( User user, Attribute... attributes = [] ) {
        refreshTokenRepository.findAll(
                where( RefreshTokenSpecifications.unexpired() )
                        .and( RefreshTokenSpecifications.userEquals( user ) )
                        .and( RefreshTokenSpecifications.include( attributes ) )
        )
    }

    @Override
    List< RefreshToken > findAllUnexpiredByClient( Client client, Attribute...attributes = [] ) {
        refreshTokenRepository.findAll(
                where( RefreshTokenSpecifications.unexpired() )
                        .and( RefreshTokenSpecifications.clientEquals( client ) )
                        .and( RefreshTokenSpecifications.include( attributes ) )
        )
    }


    @Override
    boolean isUnexpiredTokenMatchesClientId( String token, String clientID ) {
        RefreshToken refreshToken = findUnexpiredByToken( token )
        return refreshToken != null &&
               refreshToken.client.serialId == clientID
    }

}
