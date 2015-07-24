package tw.edu.ncu.cc.oauth.server.model.tokenAccessLog

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( TokenAccessLog )
class TokenAccessLog_ extends BasicEntity_ {
    public static volatile SingularAttribute< TokenAccessLog, String > tokenType
    public static volatile SingularAttribute< TokenAccessLog, Integer > tokenId
    public static volatile SingularAttribute< TokenAccessLog, Client > client
    public static volatile SingularAttribute< TokenAccessLog, String > ip
    public static volatile SingularAttribute< TokenAccessLog, String > referer
    public static volatile SingularAttribute< TokenAccessLog, String > application
}
