package tw.edu.ncu.cc.oauth.server.operation.tokenAccessLog

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import tw.edu.ncu.cc.oauth.server.model.client.Client
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLogSpecifications
import tw.edu.ncu.cc.oauth.server.operation.BasicOperation
import tw.edu.ncu.cc.oauth.server.operation.OperationParamValidator
import tw.edu.ncu.cc.oauth.server.service.client.ClientService
import tw.edu.ncu.cc.oauth.server.service.tokenAccessLog.TokenAccessLogService
import tw.edu.ncu.cc.oauth.server.service.user.UserService

import static org.springframework.data.jpa.domain.Specifications.where

@Component
class TokenAccessLogIndex extends BasicOperation {

    @Autowired
    def ClientService clientService

    @Autowired
    def TokenAccessLogService tokenAccessLogService

    @Override
    protected validate( OperationParamValidator validator ) {
        validator.required().notNull( 'page' )
        validator.required().hasText( 'clientSerialId' )
        validator.optional().attribute( 'application' )
        validator.optional().attribute( 'startDate' )
        validator.optional().attribute( 'endDate' )
        validator.optional().attribute( 'tokenType' )
        validator.optional().attribute( 'tokenId' )
        validator.optional().attribute( 'ip' )
    }

    @Override
    protected handle( Map params, Map Model ) {
        streams {
            notNullNotFound( 'client not exist' ) {
                clientService.findUndeletedBySerialId( params.clientSerialId as String )
            }
            stream { Client client ->
                Specifications< TokenAccessLog > specifications = where( TokenAccessLogSpecifications.clientEquals( client ) )
                String application = params.application as String
                if( ! StringUtils.isEmpty( application ) ) {
                    specifications = specifications.and( TokenAccessLogSpecifications.applicationEquals( application  ) )
                }
                if( ! StringUtils.isEmpty( params.tokenType ) ) {
                    specifications = specifications.and( TokenAccessLogSpecifications.tokenTypeEquals( params.tokenType as String ) )
                    if( ! StringUtils.isEmpty( params.tokenId ) ) {
                        specifications = specifications.and( TokenAccessLogSpecifications.tokenIdEquals( params.tokenId as Integer ) )
                    }
                }
                if( ! StringUtils.isEmpty( params.ip ) ) {
                    specifications = specifications.and( TokenAccessLogSpecifications.ipMatches( params.ip as String ) )
                }
                if( params.startDate != null && params.endDate != null ) {
                    specifications = specifications.and( TokenAccessLogSpecifications.dateBetween(
                        params.startDate as Date, params.endDate as Date
                    ) )
                }
                specifications
            }
            stream { Specifications< TokenAccessLog > specifications ->
                tokenAccessLogService.findAll( specifications, params.page as Pageable )
            }
        }
    }

}
