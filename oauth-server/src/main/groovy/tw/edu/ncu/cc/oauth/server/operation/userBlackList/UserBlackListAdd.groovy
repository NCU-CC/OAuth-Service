package tw.edu.ncu.cc.oauth.server.operation.userBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.user.UserService
import tw.edu.ncu.cc.oauth.server.service.userRestricted.UserRestrictedService

@Component
class UserBlackListAdd extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def UserRestrictedService userRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'username' )
        validator.required().attribute( 'reason' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    userService.findByName( params.username as String )
                }
                notNullBadRequest( 'it has already been restricted' ) { User user ->
                    userRestrictedService.isUserRestricted( user ) ? null : user
                }
                notNullNotFound { User user ->
                    userRestrictedService.create( new UserRestricted(
                            user: user,
                            reason: params.reason as String
                    ) )
                }
            }
        }
    }

}
