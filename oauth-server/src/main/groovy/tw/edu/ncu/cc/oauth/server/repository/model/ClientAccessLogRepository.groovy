package tw.edu.ncu.cc.oauth.server.repository.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import tw.edu.ncu.cc.oauth.server.model.clientAccessLog.ClientAccessLog

public interface ClientAccessLogRepository extends JpaRepository< ClientAccessLog, Integer >, JpaSpecificationExecutor< ClientAccessLog > {

    @Modifying
    @Query( "update ClientAccessLog c set c.accessTimesPerMonth = 0" )
    void resetAllClientAccessTimesPerMonth()

}
