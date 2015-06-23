package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenObject
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService
import tw.edu.ncu.cc.oauth.server.service.client.ClientService

import static tw.edu.ncu.cc.oauth.server.helper.Responder.resource
import static tw.edu.ncu.cc.oauth.server.helper.Responder.respondWith

@RestController
@RequestMapping( value = "management/v1/api_tokens" )
class APITokenController {

    @Autowired
    def ConversionService conversionService

    @Autowired
    def ApiTokenService apiTokenService

    @Autowired
    def ClientService clientService

    @RequestMapping( value = "token/{token}", method = RequestMethod.GET )
    public ResponseEntity getByToken( @PathVariable( "token" ) final String token ) {
        respondWith(
                resource()
                .pipe {
                    conversionService.convert(
                            apiTokenService.findUnexpiredByToken( token ), ApiTokenClientObject.class
                    )
                }
        )
    }

    @RequestMapping( method = RequestMethod.POST )
    public ResponseEntity createByClientId( @RequestParam( "client_id" ) String clientSerialId ) {
        respondWith(
                resource()
                .pipe {
                    clientService.findUndeletedBySerialId( clientSerialId )
                }.pipe { Client client ->
                    conversionService.convert(
                            apiTokenService.create( new ApiToken( client: client ) ), ApiTokenObject.class
                    )
                }
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.DELETE )
    public ResponseEntity revokeById( @PathVariable( "id" ) final String id ) {
        respondWith(
                resource()
                .pipe {
                    apiTokenService.findUnexpiredById( id )
                }.pipe { ApiToken apiToken ->
                    conversionService.convert(
                            apiTokenService.revoke( apiToken ), ApiTokenObject.class
                    )
                }
        )
    }

    @RequestMapping( value = "{id}/refresh", method = RequestMethod.POST )
    public ResponseEntity refreshById( @PathVariable( "id" ) final String id ) {
        respondWith(
                resource()
                .pipe {
                    apiTokenService.findUnexpiredById( id )
                }.pipe { ApiToken apiToken ->
                    conversionService.convert(
                            apiTokenService.refreshToken( apiToken ), ApiTokenObject.class
                    )
                }
        )
    }

}
