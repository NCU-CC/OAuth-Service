package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientObject
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class ClientCreate extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def ClientService clientService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'clientObject' )
    }

    @Override
    protected handle( Map params, Map model ) {
        ClientObject clientObject = params.clientObject as ClientObject
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    userService.findByName( clientObject.owner )
                }
                notNullNotFound { User user ->
                    clientService.create( new Client(
                            name: clientObject.name,
                            description: clientObject.description,
                            url: clientObject.url,
                            callback: clientObject.callback,
                            owner: user
                    ) )
                }
            }
        }
    }

}
