package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.manage.openid.OpenIDManager
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation

@Component
class OauthLogin extends BasicOperation {

    @Autowired
    def OpenIDManager openIDManager

    @Override
    protected handle( Map params, Map Model ) {
        openIDManager.getURLString()
    }

}
