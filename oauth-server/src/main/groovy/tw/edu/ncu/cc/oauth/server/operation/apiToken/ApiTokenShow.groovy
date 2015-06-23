package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.apiToken.ApiTokenService

@Component
class ApiTokenShow extends BasicOperation {

    @Autowired
    def ApiTokenService apiTokenService

    public ApiTokenShow() {
        assertHasText( 'token' )
    }

    @Override
    protected def handle( Map params, Map model ) {
        streams {
            notNullStream {
                apiTokenService.findUnexpiredByToken( params.token as String )
            }
        }
    }

}