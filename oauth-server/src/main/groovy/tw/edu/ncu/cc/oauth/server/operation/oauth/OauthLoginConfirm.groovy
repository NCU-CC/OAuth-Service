package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.security.OpenIdService

import javax.security.auth.login.LoginException
import javax.servlet.http.HttpServletRequest

@Component
class OauthLoginConfirm extends BasicOperation {

    @Autowired
    def OpenIdService openIdService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'request' )
    }

    @Override
    protected handle( Map params, Map model ) {

        HttpServletRequest request = params.request as HttpServletRequest

        openIdService.login( request )

        if( getPreviousRequest( request ) != null ) {
            getPreviousURL( request )
        } else {
            throw new LoginException( "no previous page represented" )
        }
    }

    private static String getPreviousURL( HttpServletRequest request ) {
        return getPreviousRequest( request ).getRedirectUrl()
    }

    private static SavedRequest getPreviousRequest( HttpServletRequest request ) {
        return ( SavedRequest ) request.getSession().getAttribute( "SPRING_SECURITY_SAVED_REQUEST" )
    }

}
