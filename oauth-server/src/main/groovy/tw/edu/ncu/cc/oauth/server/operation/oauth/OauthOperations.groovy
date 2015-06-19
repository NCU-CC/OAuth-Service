package tw.edu.ncu.cc.oauth.server.operation.oauth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class OauthOperations {

    @Autowired
    def OauthAccessAgree accessAccessAgree

    @Autowired
    def OauthAccessDisagree accessDisagree

    @Autowired
    def OauthAuthorize authorize

    @Autowired
    def OauthLogin login

    @Autowired
    def OauthLoginConfirm loginConfirm

    @Autowired
    def OauthExchange exchange

}
