package tw.edu.ncu.cc.oauth.server.operation.permission

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PermissionOperations {

    @Autowired
    def PermissionShow show

    @Autowired
    def PermissionIndex index

    @Autowired
    def PermissionCreate create

    @Autowired
    def PermissionDelete delete

}
