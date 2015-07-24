package tw.edu.ncu.cc.oauth.server.model.client

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel


@StaticMetamodel( Client )
class Client_ extends BasicEntity_ {

    public static volatile SingularAttribute< Client, String > name
    public static volatile SingularAttribute< Client, String > serialId
    public static volatile SingularAttribute< Client, String > encryptedSecret
    public static volatile SingularAttribute< Client, String > description
    public static volatile SingularAttribute< Client, String > url
    public static volatile SingularAttribute< Client, String > callback
    public static volatile SingularAttribute< Client, Boolean > deleted
    public static volatile SingularAttribute< Client, User > owner
    public static volatile SetAttribute< Client, ApiToken > apiTokens
    public static volatile SetAttribute< Client, RefreshToken > refreshTokens
    public static volatile SetAttribute< Client, AccessToken > accessTokens
    public static volatile SetAttribute< Client, AuthorizationCode > codes

}
