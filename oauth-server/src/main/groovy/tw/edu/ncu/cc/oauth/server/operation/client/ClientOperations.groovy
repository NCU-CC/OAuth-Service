package tw.edu.ncu.cc.oauth.server.operation.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClientOperations {

    @Autowired
    def ClientCreate create

    @Autowired
    def ClientDelete delete

    @Autowired
    def ClientRefreshSecret refreshSecret

    @Autowired
    def ClientShow show

    @Autowired
    def ClientSearch search

    @Autowired
    def ClientShowApiTokens showApiTokens

    @Autowired
    def ClientUpdate update
}
