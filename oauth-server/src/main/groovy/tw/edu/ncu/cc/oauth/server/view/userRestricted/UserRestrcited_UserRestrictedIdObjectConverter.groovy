package tw.edu.ncu.cc.oauth.server.view.userRestricted

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserRestrictedIdObject
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted

@Component
class UserRestrcited_UserRestrictedIdObjectConverter implements Converter< UserRestricted, UserRestrictedIdObject >{

    @Override
    UserRestrictedIdObject convert( UserRestricted source ) {
        UserRestrictedIdObject restrictedIdObject = new UserRestrictedIdObject()
        restrictedIdObject.user_name = source.user.name
        restrictedIdObject.reason = source.reason
        restrictedIdObject
    }

}
