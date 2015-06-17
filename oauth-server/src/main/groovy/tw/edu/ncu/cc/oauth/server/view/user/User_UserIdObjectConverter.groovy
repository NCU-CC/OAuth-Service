package tw.edu.ncu.cc.oauth.server.view.user

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserIdObject
import tw.edu.ncu.cc.oauth.server.model.user.User

@Component
class User_UserIdObjectConverter implements Converter< User, UserIdObject > {

    @Override
    UserIdObject convert( User source ) {
        UserIdObject userObject = new UserIdObject()
        userObject.id = source.id
        userObject.name = source.name
        userObject.date_created = source.dateCreated
        userObject.last_updated = source.lastUpdated
        userObject
    }

}
