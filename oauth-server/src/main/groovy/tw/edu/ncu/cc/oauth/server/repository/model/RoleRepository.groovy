package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import tw.edu.ncu.cc.oauth.server.model.role.Role

interface RoleRepository extends JpaRepository< Role, Integer >, JpaSpecificationExecutor< Role > {

    Role findByName( String name )

}