package tw.edu.ncu.cc.oauth.server.web.management

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.TypeDescriptor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenLogObject
import tw.edu.ncu.cc.oauth.server.helper.ResponseHelper
import tw.edu.ncu.cc.oauth.server.model.tokenAccessLog.TokenAccessLog
import tw.edu.ncu.cc.oauth.server.operation.tokenAccessLog.TokenAccessLogOperations

@RestController
@RequestMapping( value = "management/v1/statistic" )
class StatisticController {

    @Autowired
    def TokenAccessLogOperations tokenAccessLogOperations

    @Autowired
    def ConversionService conversionService

    @RequestMapping( value = "clients/{client_id}/tokens", method = RequestMethod.GET )
    def index( @PathVariable ( "client_id" ) String client_id,
             @RequestParam( value = "ip", required = false ) String ip,
             @RequestParam( value = "token_type", required = false ) String tokenType,
             @RequestParam( value = "token_id", required = false ) Integer tokenId,
             @RequestParam( value = "application", required = false ) String application,
             @RequestParam( value = "start_date", required = false ) Date startDate,
             @RequestParam( value = "end_date", required = false ) Date endDate,
             @PageableDefault( size = 50 ) Pageable pageable) {

        def page  = tokenAccessLogOperations.index.process(
                clientSerialId: client_id,
                tokenType: tokenType,
                tokenId: tokenId,
                page: pageable,
                ip: ip,
                application: application,
                startDate: startDate,
                endDate: endDate
        ) as Page< TokenAccessLog >

        def logs = conversionService.convert(
                page.content as List< TokenAccessLog >,
                TypeDescriptor.collection( List.class, TypeDescriptor.valueOf( TokenAccessLog.class ) ),
                TypeDescriptor.array( TypeDescriptor.valueOf( TokenLogObject.class ) )
        ) as TokenLogObject[]

        ResponseHelper.index( logs, page )
    }


}
