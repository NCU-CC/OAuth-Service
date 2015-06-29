package tw.edu.ncu.cc.oauth.server.operation.clientBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ClientBlackListOperations {

    @Autowired
    def ClientBlackListAdd add

    @Autowired
    def ClientBlackListRemove remove

    @Autowired
    def ClientBlackListUpdate update

    @Autowired
    def ClientBlackListShow show

    @Autowired
    def ClientBlackListIndex index

}
