package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.operation.apiToken.ApiTokenOperations

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping( value = "management/v1/api_tokens" )
class APITokenController {

    @Autowired
    def ApiTokenOperations apiTokenOperations

    @Autowired
    def ConversionService conversionService

    @RequestMapping( value = "token/{token}", method = RequestMethod.GET )
    def get( @PathVariable( "token" ) final String token,
             @RequestParam( value = "ip" ) String ip,
             @RequestParam( value = "referer", required = false ) String referer,
             HttpServletRequest request ) {

        ApiToken apiToken = request.getAttribute( RequestAttribute.API_TOKEN_ATTR ) as ApiToken

        def resource = apiTokenOperations.show.process(
                token: token,
                ip: ip,
                referer: referer,
                application: apiToken.client
        )

        conversionService.convert(
                resource as ApiToken, ApiTokenClientObject.class
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    def create( @RequestParam( "client_id" ) String clientSerialId ) {

        def resource = apiTokenOperations.create.process( clientSerialId: clientSerialId )

        conversionService.convert(
                resource as ApiToken, ApiTokenClientObject.class
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.DELETE )
    def revoke( @PathVariable( "id" ) final String id ) {

        def resource = apiTokenOperations.revoke.process( id: id )

        conversionService.convert(
                resource as ApiToken , ApiTokenClientObject.class
        )
    }

    @RequestMapping( value = "{id}/refresh", method = RequestMethod.POST )
    def refresh( @PathVariable( "id" ) final String id ) {

        def resource = apiTokenOperations.refresh.process( id: id )

        conversionService.convert(
                resource as ApiToken, ApiTokenClientObject.class
        )
    }

}
