package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest
import org.apache.oltu.oauth2.common.error.OAuthError
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.exception.OAuthSystemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.helper.OAuthProblemBuilder
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.clientRestricted.ClientRestrictedService
import tw.edu.ncu.cc.oauth.server.service.log.LogService
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService

@Component
class OauthAuthorize extends BasicOperation {

    @Autowired
    def LogService logService

    @Autowired
    def ClientService clientService

    @Autowired
    def PermissionService permissionService

    @Autowired
    def ClientRestrictedService clientRestrictedService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'oauthRequest' )
        validator.required().notNull( 'contextPath' )
        validator.required().hasText( 'username' )
    }

    @Override
    protected handle( Map params, Map model ) {

        OAuthAuthzRequest oauthRequest = params.oauthRequest as OAuthAuthzRequest
        String contextPath = params.contextPath as String
        String username    = params.username    as String

        logService.info(
                "OAUTH REQUEST",
                "USER:" + username,
                "SCOPE:" + oauthRequest.scopes.toListString() + ", CLIENT:" + oauthRequest.clientId,
        )

        validateOauthRequest( oauthRequest )

        model[ 'state' ] = oauthRequest.getState() == null ? "" : oauthRequest.getState()
        model[ 'scope' ] = convertToPermissions( oauthRequest.getScopes() )
        model[ 'client' ] = clientService.findUndeletedBySerialId( oauthRequest.getClientId() )
        model[ 'user_id' ] = username
        model[ 'confirm_page' ] = contextPath + "/oauth/confirm"

    }

    public void validateOauthRequest( OAuthAuthzRequest oauthRequest ) throws OAuthSystemException, OAuthProblemException {

        Set<String> scope  = oauthRequest.getScopes();
        String clientState = oauthRequest.getState();
        String clientID    = oauthRequest.getClientId();

        Client client = clientService.findUndeletedBySerialId( clientID )
        if( client == null ) {
            throw OAuthProblemBuilder
                    .error( OAuthError.CodeResponse.INVALID_REQUEST )
                    .description( "CLIENT NOT EXIST" )
                    .state( clientState )
                    .build();
        }

        if( clientRestrictedService.isClientRestricted( client ) ) {
            throw OAuthProblemBuilder
                    .error( OAuthError.CodeResponse.INVALID_REQUEST )
                    .description( "CLIENT IS FORBIDDEN" )
                    .state( clientState )
                    .build();
        }

        if ( ! isScopeExist( scope ) ) {
            throw OAuthProblemBuilder
                    .error( OAuthError.CodeResponse.INVALID_SCOPE )
                    .description( "PERMISSION NOT EXISTS" )
                    .redirectUri( client.callback )
                    .state( clientState )
                    .build();
        }

    }

    private boolean isScopeExist( Set< String > scope ) {
        for( String permission : scope ) {
            if( permissionService.findByName( permission ) == null ) {
                return false
            }
        }
        return true
    }

    private Set< Permission > convertToPermissions( Set< String > scope ) {
        scope.inject( [] as Set< Permission > ) { permissions, permissionName ->
            permissions << permissionService.findByName( permissionName )
        } as Set<Permission>
    }

}
