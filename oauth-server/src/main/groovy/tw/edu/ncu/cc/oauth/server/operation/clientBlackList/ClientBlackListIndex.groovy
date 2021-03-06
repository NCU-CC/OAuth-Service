package tw.edu.ncu.cc.oauth.server.operation.clientBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService

@Component
class ClientBlackListIndex extends BasicOperation {

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'page' )
        validator.optional().attribute( 'client_id' )
        validator.optional().attribute( 'client_name' )
        validator.optional().attribute( 'client_owner' )
        validator.optional().isBoolean( 'client_deleted' )
    }

    @Override
    @Transactional( isolation = Isolation.SERIALIZABLE )
    protected handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                clientRestrictedService.findAll(
                        new ClientIdObject(
                                id:   params.client_id,
                                name: params.client_name,
                                owner:   params.client_owner,
                                deleted: params.client_deleted
                        ),
                        params.page as Pageable
                )
            }
        }
    }

}
