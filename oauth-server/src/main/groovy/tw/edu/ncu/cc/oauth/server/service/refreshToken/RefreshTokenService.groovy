package tw.edu.ncu.cc.oauth.server.service.refreshToken

import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.Attribute

interface RefreshTokenService {

    RefreshToken createByAccessToken( RefreshToken refreshToken, AccessToken accessToken )

    RefreshToken findUnexpiredByToken( String token )
    RefreshToken findUnexpiredByToken( String token, Attribute...attributes )
    RefreshToken findUnexpiredById( String tokenId )
    RefreshToken findUnexpiredById( String tokenId, Attribute...attributes )

    List< RefreshToken > findAllUnexpiredByUser( User user )
    List< RefreshToken > findAllUnexpiredByUser( User user, Attribute...attributes )
    List< RefreshToken > findAllUnexpiredByClient( Client client )
    List< RefreshToken > findAllUnexpiredByClient( Client client, Attribute...attributes )

    RefreshToken revoke( RefreshToken refreshToken )
    RefreshToken refreshLastUsedTime( RefreshToken refreshToken )

    boolean isUnexpiredTokenMatchesClientId( String token, String clientID )
}