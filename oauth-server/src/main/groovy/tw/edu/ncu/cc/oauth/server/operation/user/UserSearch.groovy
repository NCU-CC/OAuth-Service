package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserSearch extends BasicOperation {

    @Autowired
    def UserService userService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'username' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        String username = params.username as String
        streams {
            notNullStream {
                if ( StringUtils.isEmpty( username ) ) {
                    return Collections.emptyList()
                }
                userService.findAllByNameLike( username )
            }
        }
    }

}
