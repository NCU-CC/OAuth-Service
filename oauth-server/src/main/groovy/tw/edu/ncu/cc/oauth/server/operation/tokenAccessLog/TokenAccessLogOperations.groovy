package tw.edu.ncu.cc.oauth.server.operation.tokenAccessLog

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.user.UserCreateIfNotExist
import tw.edu.ncu.cc.oauth.server.operation.user.UserShow
import tw.edu.ncu.cc.oauth.server.operation.user.UserShowAuthorizedTokens
import tw.edu.ncu.cc.oauth.server.operation.user.UserShowOwnedClients

@Component
class TokenAccessLogOperations {

    @Autowired
    def TokenAccessLogIndex index

}
