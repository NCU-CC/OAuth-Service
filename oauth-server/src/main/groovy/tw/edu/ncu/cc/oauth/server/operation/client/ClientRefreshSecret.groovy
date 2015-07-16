package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

@Component
class ClientRefreshSecret extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'serialId' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    clientService.findUndeletedBySerialId( params.serialId as String )
                }
                notNullForbidden { Client client ->
                    clientRestrictedService.isClientRestricted( client ) ? null : client
                }
                stream { Client client ->
                    clientService.refreshSecret( client )
                }
            }
        }
    }

}
