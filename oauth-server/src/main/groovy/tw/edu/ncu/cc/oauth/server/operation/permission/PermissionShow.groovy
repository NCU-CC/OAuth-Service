package tw.edu.ncu.cc.oauth.server.operation.permission

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService

@Component
class PermissionShow extends BasicOperation {

    @Autowired
    def PermissionService permissionService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'name' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                permissionService.findByName( params.name as String )
            }
        }
    }

}
