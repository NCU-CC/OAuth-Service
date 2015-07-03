package tw.edu.ncu.cc.oauth.server.service.role

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.model.role.Role
import tw.edu.ncu.cc.oauth.server.repository.model.RoleRepository

@Service
class RoleServiceImpl implements RoleService {

    @Autowired
    def RoleRepository roleRepository

    @Override
    Role findByName( String name ) {
        roleRepository.findByName( name )
    }

}
