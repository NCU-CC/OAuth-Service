package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService

@Component
class ApiTokenRefresh extends BasicOperation {

    @Autowired
    def ApiTokenService apiTokenService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'id' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    apiTokenService.findUnexpiredById( params.id as String )
                }
                stream { ApiToken apiToken ->
                    apiTokenService.refreshToken( apiToken )
                }
            }
        }
    }

}
