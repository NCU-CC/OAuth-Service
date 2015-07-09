package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

@Component
class ClientUpdate extends BasicOperation {

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
                notNullNotFound { Client client ->
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
