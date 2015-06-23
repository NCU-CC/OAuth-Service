package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ClientUpdate extends BasicOperation {

    @Autowired
    def ClientService clientService

    public ClientUpdate() {
        assertHasText( 'serialId' )
        assertNotNull( 'clientObject' )
    }

    @Override
    protected handle( Map params, Map model ) {
        ClientObject clientObject = params.clientObject as ClientObject
        streams {
            notNullStream {
                clientService.findUndeletedBySerialId( params.serialId as String )
            }
            notNullStream { Client client ->
                client.name = clientObject.name
                client.url  = clientObject.url
                client.callback    = clientObject.callback
                client.description = clientObject.description
                clientService.update( client )
            }
        }
    }

}