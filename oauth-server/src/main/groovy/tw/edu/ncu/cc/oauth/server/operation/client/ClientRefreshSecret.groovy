package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ClientRefreshSecret extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'serialId' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullStream {
                    clientService.findUndeletedBySerialId( params.serialId as String )
                }
                notNullStream { Client client ->
                    clientService.refreshSecret( client )
                }
            }
        }
    }

}
