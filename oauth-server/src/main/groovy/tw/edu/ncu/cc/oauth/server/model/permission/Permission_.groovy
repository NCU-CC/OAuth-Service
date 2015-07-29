package tw.edu.ncu.cc.oauth.server.model.permission

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( Permission )
class Permission_ extends BasicEntity_ {
    public static volatile SingularAttribute< Permission, String > name
    public static volatile SingularAttribute< Permission, String > description
}
