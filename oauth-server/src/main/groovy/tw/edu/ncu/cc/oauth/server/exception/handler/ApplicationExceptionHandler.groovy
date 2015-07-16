package tw.edu.ncu.cc.oauth.server.exception.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpServerErrorException
import tw.edu.ncu.cc.oauth.data.v1.message.ErrorObject
import tw.edu.ncu.cc.oauth.server.service.log.LogService

@ControllerAdvice
public class ApplicationExceptionHandler {

    @Autowired
    def LogService logService

    @ExceptionHandler( HttpServerErrorException )
    def ResponseEntity httpServerError( HttpServerErrorException e ) {
        new ResponseEntity<>(
                new ErrorObject( e.message ), e.statusCode
        )
    }

    @ExceptionHandler( [ HttpMessageNotReadableException, HttpMediaTypeNotSupportedException ] )
    def ResponseEntity invalidRequestBodyFormat() {
        new ResponseEntity<>(
                new ErrorObject(
                        "expect request in json format"
                ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler( HttpRequestMethodNotSupportedException )
    def ResponseEntity invalidMethod( HttpRequestMethodNotSupportedException e ) {
        new ResponseEntity<>(
                new ErrorObject(
                        ( "method not supported:" + e.method + ", expect:" + Arrays.toString( e.getSupportedMethods() ) ) as String
                ), HttpStatus.METHOD_NOT_ALLOWED
        )
    }

    @ExceptionHandler( Exception )
    def ResponseEntity exceptionHandler( Exception exception ) {
        logService.error( "UNEXPECTED ERROR:", exception )
        return new ResponseEntity<>(
                new ErrorObject(
                        exception.message
                ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

}
