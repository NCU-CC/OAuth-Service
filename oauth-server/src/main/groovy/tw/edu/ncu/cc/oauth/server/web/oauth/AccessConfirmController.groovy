package tw.edu.ncu.cc.oauth.server.web.oauth

import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.exception.OAuthSystemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.permission.Permission
import tw.edu.ncu.cc.oauth.server.operation.oauth.OauthOperations

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@SessionAttributes( [ "state", "scope", "client" ] )
public final class AccessConfirmController {

    @Autowired
    def OauthOperations oauthOperations

    private SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler()

    @RequestMapping( value = "oauth/confirm", method = RequestMethod.POST )
    public String confirm( @ModelAttribute( "state" )  String state,
                           @ModelAttribute( "scope" )  Set< Permission > scope,
                           @ModelAttribute( "client" ) Client client,
                           @RequestParam( "approval" ) boolean approval,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Authentication authentication ) throws URISyntaxException, OAuthSystemException {
        try {

            logoutHandler.logout( request, null, null )

            def targetURL

            if( approval ) {
                targetURL = oauthOperations.accessAccessAgree.process(
                        state: state, scope: scope, client: client, username: authentication.name
                )
            } else {
                targetURL = oauthOperations.accessDisagree.process(
                        state: state, client: client, username: authentication.name
                )
            }

            return "redirect:" + targetURL

        } catch ( OAuthProblemException e ) {
            response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getMessage() )
        }
        return null
    }

}