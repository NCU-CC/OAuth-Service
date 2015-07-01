package tw.edu.ncu.cc.oauth.server.operation.clientBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

@Component
class ClientBlackListAdd extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'serialId' )
        validator.required().attribute( 'reason' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullStream {
                    clientService.findUndeletedBySerialId( params.serialId as String )
                }
                notNullStream { Client client ->
                    clientRestrictedService.create( new ClientRestricted(
                            client: client,
                            reason: params.reason as String
                    ) )
                }
            }
        }
    }

}
