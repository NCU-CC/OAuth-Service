package tw.edu.ncu.cc.oauth.server.operation.accessToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken_
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.accessToken.AccessTokenService
import tw.edu.ncu.cc.oauth.server.service.tokenAccessLog.TokenAccessLogService

@Component
class AccessTokenShow extends BasicOperation {

    @Autowired
    def AccessTokenService accessTokenService

    @Autowired
    def TokenAccessLogService tokenAccessLogService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'token' )
        validator.optional().attribute( 'ip' )
        validator.optional().attribute( 'application' )
        validator.optional().attribute( 'referer' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                accessTokenService.findUnexpiredByToken( params.token as String, AccessToken_.scope )
            }
            stream { AccessToken accessToken ->
                tokenAccessLogService.create(
                        new TokenAccessLog(
                                tokenType: accessToken.class.simpleName,
                                tokenId: accessToken.id,
                                client: accessToken.client,
                                ip: params.ip,
                                referer: params.referer,
                                application: params.application
                        )
                )
                return accessToken
            }
        }
    }

}
