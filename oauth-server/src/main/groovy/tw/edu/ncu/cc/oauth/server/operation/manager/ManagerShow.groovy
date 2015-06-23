package tw.edu.ncu.cc.oauth.server.operation.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.manager.ManagerService

@Component
class ManagerShow extends BasicOperation {

    @Autowired
    def ManagerService managerService

    public ManagerShow() {
        assertHasText( 'username' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                managerService.findByName( params.username as String )
            }
        }
    }

}
