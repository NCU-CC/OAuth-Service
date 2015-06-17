package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserSearch extends BasicOperation {

    @Autowired
    def UserService userService

    public UserSearch() {
        assertHasText( 'username' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        streams {
            notNullStream {
                userService.findByPartialName( params.username as String )
            }
        }
    }

}
