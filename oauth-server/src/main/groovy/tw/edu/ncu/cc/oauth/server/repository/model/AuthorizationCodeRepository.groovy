package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import tw.edu.ncu.cc.oauth.server.model.authorizationCode.AuthorizationCode

public interface AuthorizationCodeRepository extends JpaRepository< AuthorizationCode, Integer >, JpaSpecificationExecutor< AuthorizationCode > {


}