package tw.edu.ncu.cc.oauth.server.model.userRestricted

import tw.edu.ncu.cc.oauth.server.model.BasicEntity_
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( UserRestricted )
class UserRestricted_ extends BasicEntity_ {

    public static volatile SingularAttribute< UserRestricted, User > user
    public static volatile SingularAttribute< UserRestricted, String > reason

}
