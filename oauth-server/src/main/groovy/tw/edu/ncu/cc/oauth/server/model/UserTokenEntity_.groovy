package tw.edu.ncu.cc.oauth.server.model

import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

@StaticMetamodel( UserTokenEntity )
class UserTokenEntity_ extends TokenEntity_ {
    public static volatile SingularAttribute< UserTokenEntity, User > user
}
