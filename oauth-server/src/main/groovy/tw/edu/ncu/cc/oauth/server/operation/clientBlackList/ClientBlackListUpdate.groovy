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
class ClientBlackListUpdate extends BasicOperation {

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
                notNullNotFound {
                    clientService.findUndeletedBySerialId( params.serialId as String )
                }
                notNullNotFound { Client client ->
                    clientRestrictedService.findByClient( client )
                }
                stream { ClientRestricted clientRestricted ->
                    clientRestricted.reason = params.reason as String
                    clientRestrictedService.update( clientRestricted )
                }
            }
        }
    }

}
