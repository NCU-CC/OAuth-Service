package tw.edu.ncu.cc.oauth.server.operation.userBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserIdObject
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.user.UserService
import tw.edu.ncu.cc.oauth.server.service.userRestricted.UserRestrictedService

@Component
class UserBlackListIndex extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def UserRestrictedService userRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'page' )
        validator.optional().attribute( 'user_name' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                userRestrictedService.findAll(
                        new UserIdObject(
                                name: params.user_name
                        ),
                        params.page as Pageable
                )
            }
        }
    }

}
