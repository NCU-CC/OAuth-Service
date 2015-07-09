package tw.edu.ncu.cc.oauth.server.operation.authorizedToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService

@Component
class AuthorizedTokenRevoke extends BasicOperation {

    @Autowired
    def RefreshTokenService refreshTokenService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'id' )
    }

    @Override
    protected handle( Map params, Map model ) {
        transaction.executeSerializable {
            streams {
                notNullNotFound {
                    refreshTokenService.findUnexpiredById( params.id as String, RefreshToken_.scope )
                }
                notNullNotFound { RefreshToken refreshToken ->
                    refreshTokenService.revoke( refreshToken )
                }
            }
        }
    }

}
