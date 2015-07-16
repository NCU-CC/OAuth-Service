package tw.edu.ncu.cc.oauth.server.operation.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.manager.ManagerService

@Component
class ManagerDelete extends BasicOperation {

    @Autowired
    def ManagerService managerService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'username' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    managerService.findByName( params.username as String )
                }
                stream { User user ->
                    managerService.delete( user )
                }
            }
        }
    }

}
