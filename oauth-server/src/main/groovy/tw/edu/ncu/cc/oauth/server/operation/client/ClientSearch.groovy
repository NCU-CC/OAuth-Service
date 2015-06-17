package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

@Component
class ClientSearch extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Override
    protected handle( Map params, Map model ) {

        def dto = new ClientIdObject(
                id : params.id as String,
                name : params.name as String,
                owner : params.owner as String,
                isDeleted : params.isDeleted as Boolean
        )

        streams {
            notNullStream {
                clientService.findByDTO( dto )
            }
        }
    }

}
