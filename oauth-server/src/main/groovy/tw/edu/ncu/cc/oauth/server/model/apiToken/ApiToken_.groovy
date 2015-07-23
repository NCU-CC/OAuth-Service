package tw.edu.ncu.cc.oauth.server.model.apiToken

import tw.edu.ncu.cc.oauth.server.model.TokenEntity_

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( ApiToken )
class ApiToken_ extends TokenEntity_ {

    public static volatile SingularAttribute< ApiToken, String > encryptedToken

}
