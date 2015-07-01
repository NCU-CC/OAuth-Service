package tw.edu.ncu.cc.oauth.server.operation.accessToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.accessToken.AccessTokenService

@Component
class AccessTokenShow extends BasicOperation {

    @Autowired
    def AccessTokenService accessTokenService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'token' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                accessTokenService.findUnexpiredByToken( params.token as String, AccessToken_.scope )
            }
        }
    }

}
