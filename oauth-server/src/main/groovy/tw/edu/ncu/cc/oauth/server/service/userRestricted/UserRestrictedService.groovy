package tw.edu.ncu.cc.oauth.server.service.userRestricted

import org.springframework.data.domain.Pageable
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserIdObject
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted

interface UserRestrictedService {

    UserRestricted create( UserRestricted userRestricted )
    UserRestricted update( UserRestricted userRestricted )
    UserRestricted delete( UserRestricted userRestricted )

    List< UserRestricted > findAll( UserIdObject userObject, Pageable pageable )
    UserRestricted findByUser( User user )

    boolean isUserRestricted( User user )

}