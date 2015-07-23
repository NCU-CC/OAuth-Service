package tw.edu.ncu.cc.oauth.server.model.authorizationCode

import tw.edu.ncu.cc.oauth.server.model.UserTokenEntity_
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( AuthorizationCode )
class AuthorizationCode_ extends UserTokenEntity_ {

    public static volatile SingularAttribute< AuthorizationCode, String > encryptedCode
    public static volatile SetAttribute< AuthorizationCode, Permission > scope

}
