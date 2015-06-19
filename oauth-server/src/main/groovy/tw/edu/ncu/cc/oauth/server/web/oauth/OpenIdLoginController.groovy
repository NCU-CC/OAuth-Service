package tw.edu.ncu.cc.oauth.server.web.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import tw.edu.ncu.cc.oauth.server.operation.oauth.OauthOperations

import javax.security.auth.login.LoginException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
public class OpenIdLoginController {

    @Autowired
    def OauthOperations oauthOperations

    @RequestMapping( value = "login_page", method = RequestMethod.GET )
    public void login( HttpServletResponse response ) throws IOException {

        def redirectURL = oauthOperations.login.process()

        response.sendRedirect( redirectURL as String )
    }

    @RequestMapping( value = "login_confirm", method = RequestMethod.GET )
    public void loginConfirm( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        try {

            def previousURL = oauthOperations.loginConfirm.process( request: request )

            response.sendRedirect( previousURL as String )

        } catch ( LoginException e ) {
            response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getMessage() )
        }
    }

}