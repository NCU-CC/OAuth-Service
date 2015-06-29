package tw.edu.ncu.cc.oauth.server.operation.clientBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.clientRestricted.ClientRestricted
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

@Component
class ClientBlackListUpdate extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    public ClientBlackListUpdate() {
        assertHasText( 'serialId' )
        assertNotNull( 'reason' )
    }

    @Override
    @Transactional( isolation = Isolation.SERIALIZABLE )
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                clientService.findUndeletedBySerialId( params.serialId as String )
            }
            notNullStream { Client client ->
                clientRestrictedService.findByClient( client )
            }
            notNullStream { ClientRestricted clientRestricted ->
                clientRestricted.reason = params.reason as String
                clientRestrictedService.update( clientRestricted )
            }
        }
    }

}
