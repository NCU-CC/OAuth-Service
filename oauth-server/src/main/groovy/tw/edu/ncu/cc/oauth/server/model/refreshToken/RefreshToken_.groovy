package tw.edu.ncu.cc.oauth.server.model.refreshToken

import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.permission.Permission

import javax.persistence.metamodel.SetAttribute
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( RefreshToken )
class RefreshToken_ {

    public static volatile SingularAttribute< RefreshToken, String > encryptedToken
    public static volatile SingularAttribute< RefreshToken, AccessToken > accessToken
    public static volatile SetAttribute< RefreshToken, Permission > scope

}
