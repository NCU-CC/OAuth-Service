package tw.edu.ncu.cc.oauth.server.operation.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ManagerOperations {

    @Autowired
    def ManagerCreate create

    @Autowired
    def ManagerDelete delete

    @Autowired
    def ManagerIndex index

    @Autowired
    def ManagerShow show

}
