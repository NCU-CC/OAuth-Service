package tw.edu.ncu.cc.oauth.server.web.oauth

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest
import org.apache.oltu.oauth2.common.error.OAuthError
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.exception.OAuthSystemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.SessionAttributes
import tw.edu.ncu.cc.oauth.server.helper.OAuthProblemBuilder
import tw.edu.ncu.cc.oauth.server.helper.OAuthURLBuilder
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.log.LogService
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@SessionAttributes( [ "state", "scope", "client" ] )
public final class AuthorizationController {

    @Autowired
    def LogService logService

    @Autowired
    def ClientService clientService;

    @Autowired
    def PermissionService permissionService

    @RequestMapping( value = "oauth/authorize", method = RequestMethod.GET )
    public String authorize( HttpServletRequest  request,
                             HttpServletResponse response,
                             Authentication authentication, ModelMap model ) throws OAuthProblemException, OAuthSystemException, IOException {

        try {

            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest( request );

            logService.info(
                    "OAUTH REQUEST",
                    "USER:" + authentication.name,
                    "SCOPE:" + oauthRequest.scopes.toListString() + ", CLIENT:" + oauthRequest.clientId,
            )

            validateOauthRequest( oauthRequest );

            model.addAttribute( "state", oauthRequest.getState() == null ? "" : oauthRequest.getState() );
            model.addAttribute( "scope", convertToPermissions( oauthRequest.getScopes() ) );
            model.addAttribute( "client", clientService.findUndeletedBySerialId( oauthRequest.getClientId() ) );
            model.addAttribute( "user_id", authentication.getName() );
            model.addAttribute( "confirm_page", request.getContextPath() + "/oauth/confirm" );

            return "oauth_approval";

        } catch ( OAuthSystemException e ) {
            response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getMessage() );
        } catch ( OAuthProblemException e ) {
            if( e.getRedirectUri() == null ) {
                response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getMessage() );
            } else {
                response.sendRedirect(
                        OAuthURLBuilder
                                .url( e.getRedirectUri() )
                                .state( e.getState() )
                                .error( e.getError() )
                                .errorDescription( e.getDescription() )
                                .build()
                );
            }
        }

        return null;
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