package tw.edu.ncu.cc.oauth.server.service.tokenAccessLog

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.clientAccessLog.ClientAccessLog
import tw.edu.ncu.cc.oauth.server.model.clientAccessLog.ClientAccessLogSpecifications
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.repository.model.ClientAccessLogRepository
import tw.edu.ncu.cc.oauth.server.repository.model.TokenAccessLogRepository

import static org.springframework.data.jpa.domain.Specifications.where

@Service
@CompileStatic
class TokenAccessLogServiceImpl implements TokenAccessLogService {

    @Autowired
    def TokenAccessLogRepository tokenAccessLogRepository

    @Autowired
    def ClientAccessLogRepository clientAccessLogRepository

    @Override
    TokenAccessLog create( TokenAccessLog tokenAccessLog ) {
        incrementCacheTimes( tokenAccessLog.client, tokenAccessLog.application )
        tokenAccessLogRepository.save( tokenAccessLog )
    }

    ClientAccessLog incrementCacheTimes( Client client, Client application ) {
        ClientAccessLog clientAccessLog = findClientAccessLog( client, application )
        if( clientAccessLog == null ) {
            clientAccessLog = clientAccessLogRepository.save( new ClientAccessLog(
                    client: client,
                    application: application
            ) )
        }
        clientAccessLog.accessTimesPerMonth += 1
        clientAccessLogRepository.save( clientAccessLog )
    }

    ClientAccessLog findClientAccessLog( Client client, Client application ) {
        clientAccessLogRepository.findOne(
                where( ClientAccessLogSpecifications.clientEquals( client ) )
                        .and( ClientAccessLogSpecifications.applicationEquals( application ) )
        )
    }

    @Override
    int findAccessTimesPerMonthByClientAndApplication( Client client, Client application ) {
        ClientAccessLog log = findClientAccessLog( client, application )
        log == null ? 0 : log.accessTimesPerMonth
    }

    @Override
    Page< TokenAccessLog > findAll( Specifications< TokenAccessLog > specifications, Pageable pageable ) {
        tokenAccessLogRepository.findAll( specifications, pageable )
    }

}
