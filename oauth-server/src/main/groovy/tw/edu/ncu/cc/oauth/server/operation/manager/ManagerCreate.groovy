package tw.edu.ncu.cc.oauth.server.operation.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.manager.ManagerService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class ManagerCreate extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def ManagerService managerService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'managerObject' )
    }

    @Override
    protected handle( Map params, Map model ) {
        String id = params.managerObject.id as String
        transaction.executeSerializable {
            streams {
                stream {
                    User user = userService.findByName( id )
                    if( user == null ) {
                        managerService.create( new User( name: id ) )
                    } else {
                        managerService.create( user )
                    }
                }
            }
        }
    }

}
