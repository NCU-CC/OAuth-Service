package tw.edu.ncu.cc.oauth.server.model.clientRestricted

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.client.Client

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( ClientRestricted )
class ClientRestricted_ extends BasicEntity_ {

    public static volatile SingularAttribute< ClientRestricted, Client > client
    public static volatile SingularAttribute< ClientRestricted, String > reason

}
