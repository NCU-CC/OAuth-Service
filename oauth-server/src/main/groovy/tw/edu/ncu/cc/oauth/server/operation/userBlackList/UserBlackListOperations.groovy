package tw.edu.ncu.cc.oauth.server.operation.userBlackList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserBlackListOperations {

    @Autowired
    def UserBlackListAdd add

    @Autowired
    def UserBlackListRemove remove

    @Autowired
    def UserBlackListUpdate update

    @Autowired
    def UserBlackListShow show

    @Autowired
    def UserBlackListIndex index

}
