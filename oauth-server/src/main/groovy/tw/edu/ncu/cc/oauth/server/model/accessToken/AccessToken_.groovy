package tw.edu.ncu.cc.oauth.server.model.accessToken

import tw.edu.ncu.cc.oauth.server.model.UserTokenEntity_
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( AccessToken )
class AccessToken_ extends UserTokenEntity_ {

    public static volatile SingularAttribute< AccessToken, String > encryptedToken
    public static volatile SetAttribute< AccessToken, Permission > scope

}
