package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ClientShow extends BasicOperation {

    @Autowired
    def ClientService clientService

    public ClientShow() {
        assertHasText( 'serialId' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                clientService.findUndeletedBySerialId( params.serialId as String )
            }
        }
    }

}
