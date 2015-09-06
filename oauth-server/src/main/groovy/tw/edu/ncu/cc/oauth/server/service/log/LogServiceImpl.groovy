package tw.edu.ncu.cc.oauth.server.service.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class LogServiceImpl implements LogService {

    @Value( '${spring.application.name}' )
    def String applicationName

    private Logger logger = LoggerFactory.getLogger( this.getClass() )

    @Override
    def info( String action, String operator ) {
        logger.info( formatMessage( action, operator, null ) )
    }

    @Override
    def info( String action, String operator, String extra ) {
        logger.info( formatMessage( action, operator, extra ) )
    }

    @Override
    def error( String action, String operator, String extra, Throwable throwable ) {
        logger.error( formatMessage( action, operator, extra ), throwable )
    }

    def formatMessage( String action, String operator, String extra ) {
        if( extra == null ) {
            String.format( "[ %s ] [ %s ] [ %s ]", applicationName, action, operator )
        } else {
            String.format( "[ %s ] [ %s ] [ %s ] [ %s ]", applicationName, action, operator, extra )
        }
    }

}
