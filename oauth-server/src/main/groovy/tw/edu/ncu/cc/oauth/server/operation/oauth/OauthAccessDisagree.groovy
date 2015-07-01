package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.apache.oltu.oauth2.common.error.OAuthError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.helper.OAuthURLBuilder
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.log.LogService

@Component
class OauthAccessDisagree extends BasicOperation {

    @Autowired
    def LogService logService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'client' )
        validator.required().hasText( 'username' )
        validator.optional().hasText( 'state' )
    }

    @Override
    protected handle( Map params, Map model ) {

        Client client = params.client as Client
        String state  = params.state as String
        String username  = params.username as String

        logService.info(
                "OAUTH ACCESS DISAGREE",
                "USER:" + username,
                "CLIENT:" + client.id
        )

        return OAuthURLBuilder
                .url( client.getCallback() )
                .error( OAuthError.CodeResponse.ACCESS_DENIED )
                .state( state )
                .build()
    }

}
