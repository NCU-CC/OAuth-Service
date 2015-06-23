package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ApiTokenCreate extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Autowired
    def ApiTokenService apiTokenService

    public ApiTokenCreate() {
        assertHasText( 'clientSerialId' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                clientService.findUndeletedBySerialId( params.clientSerialId as String )
            }
            notNullStream { Client client ->
                apiTokenService.create( new ApiToken( client: client ) )
            }
        }
    }

}
