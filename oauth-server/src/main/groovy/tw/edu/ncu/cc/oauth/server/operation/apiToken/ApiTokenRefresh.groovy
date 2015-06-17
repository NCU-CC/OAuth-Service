package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService

@Component
class ApiTokenRefresh extends BasicOperation {

    @Autowired
    def ApiTokenService apiTokenService

    public ApiTokenRefresh() {
        assertHasText( 'id' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                apiTokenService.findUnexpiredById( params.id as String )
            }
            notNullStream { ApiToken apiToken ->
                apiTokenService.refreshToken( apiToken )
            }
        }
    }

}
