package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.security.OpenIdService

@Component
class OauthLogin extends BasicOperation {

    @Autowired
    def OpenIdService openIdService

    @Override
    protected handle( Map params, Map model ) {

        openIdService.getLoginPath()
    }

}
