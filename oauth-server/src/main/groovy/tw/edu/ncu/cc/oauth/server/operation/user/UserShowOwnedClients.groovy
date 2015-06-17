package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserShowOwnedClients extends BasicOperation {

    @Autowired
    def UserService userService

    public UserShowOwnedClients() {
        assertHasText( 'username' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        streams {
            notNullStream {
                userService.findByName( params.username as String )
            }
            notNullStream { User user ->
                user.clients
            }
        }
    }

}
