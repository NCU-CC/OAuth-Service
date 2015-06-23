package tw.edu.ncu.cc.oauth.server.operation.authorizedToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService

@Component
class AuthorizedTokenShow extends BasicOperation {

    @Autowired
    def RefreshTokenService refreshTokenService

    public AuthorizedTokenShow() {
        assertHasText( 'id' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                refreshTokenService.findUnexpiredById( params.id as String, RefreshToken_.scope )
            }
        }
    }

}
