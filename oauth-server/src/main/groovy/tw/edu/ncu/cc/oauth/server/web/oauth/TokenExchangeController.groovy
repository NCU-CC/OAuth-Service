package tw.edu.ncu.cc.oauth.server.web.oauth

import org.apache.oltu.oauth2.common.error.OAuthError
import org.apache.oltu.oauth2.common.exception.OAuthProblemException
import org.apache.oltu.oauth2.common.exception.OAuthSystemException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tw.edu.ncu.cc.oauth.server.operation.oauth.OauthOperations

import javax.servlet.http.HttpServletRequest

import static org.springframework.http.HttpHeaders.CACHE_CONTROL
import static org.springframework.http.HttpHeaders.PRAGMA

@RestController
public class TokenExchangeController {

    private HttpHeaders headers

    @Autowired
    def OauthOperations oauthOperations

    public TokenExchangeController() {
        headers = new HttpHeaders()
        headers.add( PRAGMA, "no-cache" )
        headers.add( CACHE_CONTROL, "no-store" )
    }

    @RequestMapping( value = "oauth/token", method = RequestMethod.POST )
    public ResponseEntity exchangeToken( HttpServletRequest request ) throws IOException {
        try {
            buildSuccessResponse( request )
        } catch ( OAuthSystemException ignore ) {
            buildServerError()
        } catch ( OAuthProblemException e ) {
            buildFailResponse( e )
        }
    }

    private def buildSuccessResponse( HttpServletRequest request ) {
        String body = oauthOperations.exchange.process( request: request ) as String
        return new ResponseEntity<>(
                body, headers, HttpStatus.OK
        )
    }

    private def buildServerError() {
        String body = buildErrorMessage( OAuthProblemException.error(
                OAuthError.CodeResponse.SERVER_ERROR )
        )
        return new ResponseEntity<>(
                body, headers, HttpStatus.BAD_REQUEST
        )
    }

    private def buildFailResponse( OAuthProblemException e ) {
        String body = buildErrorMessage( e )
        return new ResponseEntity<>(
                body, headers, HttpStatus.BAD_REQUEST
        )
    }

    private static String buildErrorMessage( OAuthProblemException e ) {
        return String.format(
                "{\"error\":\"%s\",\"error_description\":\"%s\"}",
                e.getError(),
                e.getDescription()
        )
    }

}
