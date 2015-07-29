package tw.edu.ncu.cc.oauth.server.model.clientAccessLog

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( ClientAccessLog )
class ClientAccessLog_ extends BasicEntity_ {
    public static volatile SingularAttribute< ClientAccessLog, Client > client
    public static volatile SingularAttribute< ClientAccessLog, Client > application
    public static volatile SingularAttribute< ClientAccessLog, Integer > accessTimesPerMonth
}
