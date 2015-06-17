package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserShow extends BasicOperation {

    @Autowired
    def UserService userService

    public UserShow() {
        assertHasText( 'username' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        streams {
            notNullStream {
                userService.findByName( params.username as String )
            }
        }
    }

}
