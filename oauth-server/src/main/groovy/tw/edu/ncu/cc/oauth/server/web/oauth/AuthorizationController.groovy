package tw.edu.ncu.cc.oauth.server.web.oauth

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.exception.OAuthSystemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.SessionAttributes
import tw.edu.ncu.cc.oauth.server.helper.OAuthURLBuilder
import tw.edu.ncu.cc.oauth.server.operation.oauth.OauthOperations
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.log.LogService
import tw.edu.ncu.cc.oauth.server.service.permission.PermissionService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@SessionAttributes( [ "state", "scope", "client" ] )
public final class AuthorizationController {

    @Autowired
    def OauthOperations oauthOperations

    @Autowired
    def LogService logService

    @Autowired
    def ClientService clientService;

    @Autowired
    def PermissionService permissionService

    @RequestMapping( value = "oauth/authorize", method = RequestMethod.GET )
    public String authorize( HttpServletRequest  request,
                             HttpServletResponse response,
                             Authentication authentication, ModelMap model ) throws IOException {

        try {

            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest( request );

            oauthOperations.authorize.process(
                    [ oauthRequest: oauthRequest, contextPath: request.contextPath, username: authentication.name ] , model
            )

            return "oauth_approval"

        } catch ( OAuthSystemException e ) {
            response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getMessage() )
        } catch ( OAuthProblemException e ) {
            if( e.getRedirectUri() == null ) {
                response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getMessage() )
            } else {
                response.sendRedirect(
                        OAuthURLBuilder
                                .url( e.getRedirectUri() )
                                .state( e.getState() )
                                .error( e.getError() )
                                .errorDescription( e.getDescription() )
                                .build()
                )
            }
        }

        return null
    }

}