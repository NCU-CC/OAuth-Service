package tw.edu.ncu.cc.oauth.server.service.tokenAccessLog

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog

interface TokenAccessLogService {

    TokenAccessLog create( TokenAccessLog tokenAccessLog )

    Page< TokenAccessLog > findAll( Specifications< TokenAccessLog > specifications, Pageable pageable )

}
