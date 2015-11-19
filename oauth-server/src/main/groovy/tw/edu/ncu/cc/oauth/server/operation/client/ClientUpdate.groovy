package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService
import tw.edu.ncu.cc.oauth.server.service.user.UserService
import tw.edu.ncu.cc.oauth.server.service.userRestricted.UserRestrictedService

@Component
class ClientUpdate extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def UserRestrictedService userRestrictedService

    @Autowired
    def ClientService clientService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'serialId' )
        validator.required().notNull( 'clientObject' )
    }

    @Override
    protected handle( Map params, Map model ) {
        ClientObject clientObject = params.clientObject as ClientObject
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    clientService.findUndeletedBySerialId( params.serialId as String )
                }
                notNullForbidden { Client client ->
                    clientRestrictedService.isClientRestricted( client ) ? null : client
                }
                notNullNotFound( 'user is not found or restricted' ) { Client client ->
                    if( clientObject.owner != null ) {
                        def user = userService.findByName( clientObject.owner )
                        if( user == null ) {
                            return null
                        } else if ( userRestrictedService.isUserRestricted( user ) ){
                            return null
                        } else {
                            client.owner = user
                        }
                    }
                    return client
                }
                stream { Client client ->
                    client.name = clientObject.name
                    client.url  = clientObject.url
                    client.callback    = clientObject.callback
                    client.description = clientObject.description
                    clientService.update( client )
                }
            }
        }
    }

}
