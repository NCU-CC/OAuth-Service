package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
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

    @Value( '${custom.oauth.resource.access-limit-per-month}' )
    def Integer resourceAccessLimitPerMonth

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'token' )
        validator.required().attribute( 'application' )
        validator.optional().attribute( 'ip' )
        validator.optional().attribute( 'referer' )
    }

    @Override
    protected def handle( Map params, Map model ) {
        transaction.execute() {
            streams {
                notNullNotFound {
                    apiTokenService.findUnexpiredByToken( params.token as String )
                }
                notNullForbidden( 'reach api access times limit per month'  ) { ApiToken apiToken ->
                    if( apiToken.client.trusted ) {
                        apiToken
                    } else {
                        int accessTimesPerMonth = tokenAccessLogService.findAccessTimesPerMonthByClientAndApplication(
                                apiToken.client, params.application as Client
                        )
                        accessTimesPerMonth > resourceAccessLimitPerMonth ? null : apiToken
                    }
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
                                    application: params.application,
                                    ip: params.ip,
                                    referer: params.referer
                            )
                    )
                    return apiToken
                }
            }
        }
    }

}
