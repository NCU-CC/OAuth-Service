package tw.edu.ncu.cc.oauth.server.model.role

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( Role )
class Role_ extends BasicEntity_ {
    public static volatile SingularAttribute< Role, String > name
}
