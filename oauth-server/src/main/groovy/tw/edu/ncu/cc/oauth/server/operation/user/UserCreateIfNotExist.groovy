package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.user.UserObject
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserCreateIfNotExist extends BasicOperation {

    @Autowired
    def UserService userService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'userObject' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        UserObject userObject = params.userObject as UserObject
        transaction.executeSerializable {
            streams {
                stream {
                    User user = userService.findByName( userObject.name )
                    if( user == null ) {
                        user = userService.create( new User( name: userObject.name ) )
                    }
                    user
                }
            }
        }
    }

}
