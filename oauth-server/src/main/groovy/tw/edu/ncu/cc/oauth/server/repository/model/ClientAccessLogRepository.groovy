package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import tw.edu.ncu.cc.oauth.server.model.clientAccessLog.ClientAccessLog
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog

public interface ClientAccessLogRepository extends JpaRepository< ClientAccessLog, Integer >, JpaSpecificationExecutor< ClientAccessLog > {

}
