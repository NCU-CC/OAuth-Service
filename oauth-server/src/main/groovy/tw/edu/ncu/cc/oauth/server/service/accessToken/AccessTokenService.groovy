package tw.edu.ncu.cc.oauth.server.service.accessToken

import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.Attribute

interface AccessTokenService {

    AccessToken create( AccessToken accessToken );
    AccessToken createByAuthorizationCode( AccessToken accessToken, AuthorizationCode authorizationCode );
    AccessToken createByRefreshToken( AccessToken accessToken, RefreshToken refreshToken );

    AccessToken findUnexpiredByToken( String token );
    AccessToken findUnexpiredByToken( String token, Attribute...attributes );
    AccessToken findUnexpiredById( String tokenId );
    AccessToken findUnexpiredById( String tokenId, Attribute...attributes );

    List< AccessToken > findAllUnexpiredByUser( User user );
    List< AccessToken > findAllUnexpiredByUser( User user, Attribute...attributes );
    List< AccessToken > findAllUnexpiredByClient( Client client );
    List< AccessToken > findAllUnexpiredByClient( Client client, Attribute...attributes );

    AccessToken revoke( AccessToken accessToken );
    AccessToken refreshLastUsedTime( AccessToken accessToken );

}