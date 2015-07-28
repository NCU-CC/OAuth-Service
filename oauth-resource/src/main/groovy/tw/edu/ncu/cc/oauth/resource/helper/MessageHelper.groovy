package tw.edu.ncu.cc.oauth.resource.helper

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.client.HttpStatusCodeException
import tw.edu.ncu.cc.oauth.data.v1.message.ErrorObject


class MessageHelper {

    static ErrorObject errorObject( String errorMessageBody ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper()
            objectMapper.readValue( errorMessageBody, ErrorObject )
        } catch ( Exception ignore ) {
            new ErrorObject( "unknwon reason because response is unresolvable" )
        }
    }

    static String errorDescription( String errorMessageBody ) {
        errorObject( errorMessageBody ).error_description
    }

    static String errorDescription( HttpStatusCodeException e ) {
        errorDescription( e.responseBodyAsString )
    }

}
