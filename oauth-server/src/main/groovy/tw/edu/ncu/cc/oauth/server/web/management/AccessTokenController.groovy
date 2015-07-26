package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.operation.accessToken.AccessTokenOperations

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping( value = "management/v1/access_tokens" )
public class AccessTokenController {

    @Autowired
    def AccessTokenOperations accessTokenOperations

    @Autowired
    def ConversionService conversionService

    @RequestMapping( value = "token/{token}", method = RequestMethod.GET )
    def get( @PathVariable( "token" ) final String token,
             @RequestParam( value = "ip", required = false ) String ip,
             @RequestParam( value = "referer", required = false ) String referer,
             HttpServletRequest request ) {

        ApiToken apiToken = request.getAttribute( RequestAttribute.API_TOKEN_ATTR ) as ApiToken

        def resource = accessTokenOperations.show.process(
                token: token,
                ip: ip,
                referer: referer,
                application: apiToken.client
        )

        conversionService.convert(
                resource as AccessToken, TokenObject.class
        )
    }

}