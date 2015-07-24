package tw.edu.ncu.cc.oauth.server.model

import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( TokenEntity )
class TokenEntity_ extends BasicEntity_ {
    public static volatile SingularAttribute< TokenEntity, Client > client
    public static volatile SingularAttribute< TokenEntity, Date > dateExpired
}
