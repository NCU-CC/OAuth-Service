package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import tw.edu.ncu.cc.oauth.server.helper.OAuthURLBuilder
import tw.edu.ncu.cc.oauth.server.helper.TimeBuilder
import tw.edu.ncu.cc.oauth.server.helper.data.TimeUnit
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.authorizationCode.AuthorizationCodeService
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

    @Value( '${custom.oauth.authCode.expire-seconds}' )
    def long authorizationCodeExpireSeconds


    public OauthAccessAgree() {
        assertNotNull( 'scope' )
        assertNotNull( 'client' )
        assertHasText( 'username' )
    }

    @Override
    @Transactional( isolation = Isolation.SERIALIZABLE )
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

        return OAuthURLBuilder
                .url( client.getCallback() )
                .code( authCode.getCode() )
                .state( state )
                .build()
    }

}
