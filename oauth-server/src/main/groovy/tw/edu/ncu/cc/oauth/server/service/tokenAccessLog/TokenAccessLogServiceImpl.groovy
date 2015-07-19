package tw.edu.ncu.cc.oauth.server.service.tokenAccessLog

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.repository.model.TokenAccessLogRepository

@Service
class TokenAccessLogServiceImpl implements TokenAccessLogService {

    @Autowired
    def TokenAccessLogRepository tokenAccessLogRepository

    @Override
    TokenAccessLog create( TokenAccessLog tokenAccessLog ) {
        tokenAccessLogRepository.save( tokenAccessLog )
    }

    @Override
    Page< TokenAccessLog > findAll( Specifications< TokenAccessLog > specifications, Pageable pageable ) {
        tokenAccessLogRepository.findAll( specifications, pageable )
    }

}
