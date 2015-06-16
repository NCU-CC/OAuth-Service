package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import tw.edu.ncu.cc.oauth.server.model.role.Role
import tw.edu.ncu.cc.oauth.server.model.user.User

public interface UserRepository extends JpaRepository< User, Integer >, JpaSpecificationExecutor< User > {

    User findByName( String name )

    @Query( "from User u where ?1 member of u.roles" )
    List< User > findByRolePaged( Role role, Pageable pageable )

}
