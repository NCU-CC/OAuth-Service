package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.operation.accessToken.AccessTokenOperations

@RestController
@RequestMapping( value = "management/v1/access_tokens" )
public class AccessTokenController {

    @Autowired
    def AccessTokenOperations accessTokenOperations

    @Autowired
    def ConversionService conversionService

    @RequestMapping( value = "token/{token}", method = RequestMethod.GET )
    def get( @PathVariable ( "token" ) final String token ) {

        def resource  = accessTokenOperations.show.process( token: token )

        conversionService.convert(
                resource as AccessToken, TokenObject.class
        )
    }

}