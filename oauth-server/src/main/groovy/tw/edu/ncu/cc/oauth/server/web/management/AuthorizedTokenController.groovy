package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenClientObject
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.operation.authorizedToken.AuthorizedTokenOperations

@RestController
@RequestMapping( value = "management/v1/authorized_tokens" )
public class AuthorizedTokenController {

    @Autowired
    def AuthorizedTokenOperations authorizedTokenOperations

    @Autowired
    def ConversionService conversionService

    @RequestMapping( value = "{id}", method = RequestMethod.GET )
    def get( @PathVariable( "id" ) final String id ) {

        def resource = authorizedTokenOperations.show.process( id: id )

        conversionService.convert(
                resource as RefreshToken, TokenClientObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.DELETE )
    def revoke( @PathVariable( "id" ) final String id ) {

        def resource = authorizedTokenOperations.revoke.process( id: id )

        conversionService.convert(
                resource as RefreshToken, TokenClientObject.class
        )
    }

}