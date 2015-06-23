package tw.edu.ncu.cc.oauth.server.operation.manager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.service.manager.ManagerService

@Component
class ManagerIndex extends BasicOperation {

    @Autowired
    def ManagerService managerService

    public ManagerIndex() {
        assertHasText( 'page' )
    }

    @Override
    protected handle( Map params, Map model ) {
        streams {
            notNullStream {
                managerService.findAllManagers( params.page as Pageable )
            }
        }
    }

}