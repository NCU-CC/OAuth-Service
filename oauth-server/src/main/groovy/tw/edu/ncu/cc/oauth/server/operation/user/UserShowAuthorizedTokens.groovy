package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken_
import tw.edu.ncu.cc.oauth.server.model.user.User
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.refreshToken.RefreshTokenService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

@Component
class UserShowAuthorizedTokens extends BasicOperation {

    @Autowired
    def UserService userService

    @Autowired
    def RefreshTokenService refreshTokenService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().hasText( 'username' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        streams {
            notNullStream {
                userService.findByName( params.username as String )
            }
            notNullStream { User user ->
                refreshTokenService.findAllUnexpiredByUser( user, RefreshToken_.scope )
            }
        }
    }

}
