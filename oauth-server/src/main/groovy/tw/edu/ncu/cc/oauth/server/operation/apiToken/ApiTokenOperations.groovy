package tw.edu.ncu.cc.oauth.server.operation.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ApiTokenOperations {

    @Autowired
    def ApiTokenCreate create

    @Autowired
    def ApiTokenRefresh refresh

    @Autowired
    def ApiTokenRevoke revoke

    @Autowired
    def ApiTokenShow show

}
