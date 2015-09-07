package tw.edu.ncu.cc.oauth.server.operation.authorizedToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken_
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class AuthorizedTokenIndex extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def RefreshTokenService refreshTokenService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'user' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullNotFound {
                def user = userService.findByName( params.user as String )
                if( user == null ) {
                    []
                } else {
                    refreshTokenService.findAllUnexpiredByUser( user, RefreshToken_.scope )
                }
            }
        }
    }

}
