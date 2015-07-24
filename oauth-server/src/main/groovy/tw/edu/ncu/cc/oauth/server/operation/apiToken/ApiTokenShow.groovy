package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService
import tw.edu.ncu.cc.oauth.server.service.tokenAccessLog.TokenAccessLogService

@Component
class ApiTokenShow extends BasicOperation {

    @Autowired
    def ApiTokenService apiTokenService

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
    protected def handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                apiTokenService.findUnexpiredByToken( params.token as String )
            }
            stream { ApiToken apiToken ->
                apiTokenService.refreshLastUsedTime( apiToken )
            }
            stream { ApiToken apiToken ->
                tokenAccessLogService.create(
                        new TokenAccessLog(
                                tokenType: apiToken.class.simpleName,
                                tokenId: apiToken.id,
                                client: apiToken.client,
                                ip: params.ip,
                                referer: params.referer,
                                application: params.application
                        )
                )
                return apiToken
            }
        }
    }

}
