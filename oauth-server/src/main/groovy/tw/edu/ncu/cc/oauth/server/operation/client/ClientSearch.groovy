package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ClientSearch extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.optional().hasText( 'id' )
        validator.optional().hasText( 'name' )
        validator.optional().hasText( 'owner' )
        validator.optional().isBoolean( 'deleted' )
    }

    @Override
    protected handle( Map params, Map model ) {

        def dto = new ClientIdObject(
                id : params.id as String,
                name : params.name as String,
                owner : params.owner as String,
                deleted: params.deleted as Boolean
        )

        streams {
            notNullStream {
                clientService.findAllByDataObject( dto )
            }
        }
    }

}
