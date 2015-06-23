package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenClientObject
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken_
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService

import static tw.edu.ncu.cc.oauth.server.helper.Responder.resource
import static tw.edu.ncu.cc.oauth.server.helper.Responder.respondWith

@RestController
@RequestMapping( value = "management/v1/authorized_tokens" )
public class AuthorizedTokenController {

    @Autowired
    def ConversionService conversionService

    @Autowired
    def RefreshTokenService refreshTokenService

    @RequestMapping( value = "{id}", method = RequestMethod.GET )
    public ResponseEntity getById( @PathVariable( "id" ) final String id ) {
        respondWith(
                resource()
                .pipe {
                    return conversionService.convert(
                            refreshTokenService.findUnexpiredById( id, RefreshToken_.scope ), TokenClientObject.class
                    )
                }
        )
    }

    @RequestMapping( value = "{id}", method = RequestMethod.DELETE )
    public ResponseEntity revokeById( @PathVariable( "id" ) final String id ) {
        respondWith(
            resource()
            .pipe {
                refreshTokenService.findUnexpiredById( id, RefreshToken_.scope )
            }.pipe { RefreshToken refreshToken ->
                conversionService.convert(
                        refreshTokenService.revoke( refreshToken ), TokenClientObject.class
                )
            }
        )
    }

}