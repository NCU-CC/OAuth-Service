package tw.edu.ncu.cc.oauth.server.service.tokenAccessLog

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog

interface TokenAccessLogService {

    TokenAccessLog create( TokenAccessLog tokenAccessLog )

    int findAccessTimesPerMonthByClientAndApplication( Client client, Client application )

    Page< TokenAccessLog > findAll( Specifications< TokenAccessLog > specifications, Pageable pageable )

}
