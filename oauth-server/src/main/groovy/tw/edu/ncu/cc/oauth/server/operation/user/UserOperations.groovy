package tw.edu.ncu.cc.oauth.server.operation.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserOperations {

    @Autowired
    def UserCreateIfNotExist createIfNotExist

    @Autowired
    def UserShow show

    @Autowired
    def UserSearch search

    @Autowired
    def UserShowOwnedClients showOwnedClients

    @Autowired
    def UserShowAuthorizedTokens showAuthorizedTokens

}
