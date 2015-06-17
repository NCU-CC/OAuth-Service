package tw.edu.ncu.cc.oauth.server.operation.authorizedToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthorizedTokenOperations {

    @Autowired
    def AuthorizedTokenRevoke revoke

    @Autowired
    def AuthorizedTokenShow show

}
