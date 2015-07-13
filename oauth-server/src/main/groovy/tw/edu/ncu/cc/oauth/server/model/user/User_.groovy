package tw.edu.ncu.cc.oauth.server.model.user

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.role.Role

import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( User )
class User_ extends BasicEntity_ {
    public static volatile SingularAttribute< User, String > name
    public static volatile SetAttribute< User, Client > clients
    public static volatile SetAttribute< User, AuthorizationCode > codes
    public static volatile SetAttribute< User, AccessToken > accessTokens
    public static volatile SetAttribute< User, Role > roles
}
