package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.apache.oltu.oauth2.common.error.OAuthError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.helper.OAuthProblemBuilder
import tw.edu.ncu.cc.oauth.server.helper.OAuthURLBuilder
import tw.edu.ncu.cc.oauth.server.helper.TimeBuilder
import tw.edu.ncu.cc.oauth.server.helper.data.TimeUnit
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.authorizationCode.AuthorizationCodeService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService
import tw.edu.ncu.cc.oauth.server.service.log.LogService
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class OauthAccessAgree extends BasicOperation {

    @Autowired
    def LogService logService

    @Autowired
    def UserService userService

    @Autowired
    def PermissionService permissionService

    @Autowired
    def AuthorizationCodeService authorizationCodeService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Value( '${custom.oauth.authCode.expire-seconds}' )
    def long authorizationCodeExpireSeconds

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'scope' )
        validator.required().notNull( 'client' )
        validator.required().hasText( 'username' )
        validator.optional().hasText( 'state' )
    }

    @Override
    protected handle( Map params, Map model ) {

        Client client = params.client as Client
        String state  = params.state  as String
        String username  = params.username as String
        Set< Permission > scope = params.scope as Set< Permission >

        logService.info(
                "OAUTH ACCESS AGREE",
                "USER:" + username,
                "CLIENT:" + client.id
        )

        transaction.executeSerializable {

            if( clientRestrictedService.isClientRestricted( client ) ) {
                throw OAuthProblemBuilder
                        .error( OAuthError.CodeResponse.INVALID_REQUEST )
                        .description( "CLIENT IS FORBIDDEN" )
                        .state( state )
                        .build();
            }

            Date expireDate = TimeBuilder
                    .now()
                    .after( authorizationCodeExpireSeconds, TimeUnit.SECOND )
                    .buildDate()

            AuthorizationCode authCode = authorizationCodeService.create( new AuthorizationCode(
                    client: client,
                    user: userService.findByName( username ),
                    scope: scope,
                    dateExpired: expireDate
            ) )

            OAuthURLBuilder
                    .url( client.getCallback() )
                    .code( authCode.getCode() )
                    .state( state )
                    .build()
        }

    }

}
