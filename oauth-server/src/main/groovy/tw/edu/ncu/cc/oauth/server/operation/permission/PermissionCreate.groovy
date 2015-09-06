package tw.edu.ncu.cc.oauth.server.operation.permission

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService

@Component
class PermissionCreate extends BasicOperation {

    @Autowired
    def PermissionService permissionService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'permissionObject' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullBadRequest( 'permission with given name is already exists' ) {
                permissionService.findByName( params.permissionObject.name as String ) == null ? true : null
            }
            notNullNotFound {
                permissionService.create( new Permission( name: params.permissionObject.name ) )
            }
        }
    }

}
