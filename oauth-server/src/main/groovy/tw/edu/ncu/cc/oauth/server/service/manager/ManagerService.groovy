package tw.edu.ncu.cc.oauth.server.service.manager

import org.springframework.data.domain.Pageable
import tw.edu.ncu.cc.oauth.server.model.user.User

import javax.persistence.metamodel.Attribute

interface ManagerService {
    User create( User user )
    User delete( User user )
    User findByName( String name )
    User findByName( String name, Attribute...attributes )
    List< User > findAllManagers( Pageable pageable )
}