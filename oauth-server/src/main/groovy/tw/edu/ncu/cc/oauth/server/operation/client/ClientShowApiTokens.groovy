package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.client.Client_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ClientShowApiTokens extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'serialId' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                clientService.findUndeletedBySerialId( params.serialId as String, Client_.apiTokens )
            }
            notNullNotFound { Client client ->
                client.apiTokens
            }
        }
    }

}
