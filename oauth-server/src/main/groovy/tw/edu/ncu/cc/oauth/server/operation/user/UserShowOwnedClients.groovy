package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.model.user.User_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserShowOwnedClients extends BasicOperation {

    @Autowired
    def UserService userService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'username' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        streams {
            notNullNotFound {
                userService.findByName( params.username as String, User_.clients )
            }
            notNullNotFound { User user ->
                user.clients
            }
        }
    }

}
