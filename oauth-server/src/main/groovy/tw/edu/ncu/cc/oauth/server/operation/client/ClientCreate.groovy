package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.manager.ManagerService
import tw.edu.ncu.cc.oauth.server.service.user.UserService
import tw.edu.ncu.cc.oauth.server.service.userRestricted.UserRestrictedService

@Component
class ClientCreate extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def UserRestrictedService userRestrictedService

    @Autowired
    def ClientService clientService

    @Autowired
    def ManagerService managerService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'clientObject' )
    }

    @Override
    protected handle( Map params, Map model ) {
        ClientObject clientObject = params.clientObject as ClientObject
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    userService.findByName( clientObject.owner )
                }
                notNullForbidden( 'user is forbidden' ) { User user ->
                    userRestrictedService.isUserRestricted( user ) ? null : user
                }
                notNullForbidden( 'only manager can create trusted client' ) { User user ->
                    if( clientObject.trusted ) {
                        managerService.findByName( user.name ) == null ? null : user
                    }
                    return user
                }
                stream { User user ->
                    clientService.create( new Client(
                            name: clientObject.name,
                            description: clientObject.description,
                            url: clientObject.url,
                            callback: clientObject.callback,
                            owner: user,
                            trusted: clientObject.trusted
                    ) )
                }
            }
        }
    }

}
