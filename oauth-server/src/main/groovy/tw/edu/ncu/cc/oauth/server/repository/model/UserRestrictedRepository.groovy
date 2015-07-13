package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import tw.edu.ncu.cc.oauth.server.model.userRestricted.UserRestricted

public interface UserRestrictedRepository extends JpaRepository< UserRestricted, Integer >, JpaSpecificationExecutor< UserRestricted > {


}
