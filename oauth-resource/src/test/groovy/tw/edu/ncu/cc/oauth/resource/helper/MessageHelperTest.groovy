package tw.edu.ncu.cc.oauth.resource.helper

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification

import java.nio.charset.Charset

class MessageHelperTest extends Specification {

    def "it can convert string into error object"() {
        when:
            def error1 = MessageHelper.errorObject( '{ "error_description" : "test" }' )
        then:
            error1.error_description == "test"
        when:
            def error2 = MessageHelper.errorObject( 'hello world' )
        then:
            error2.error_description != null
    }

    def "it can get error description from string"() {
        when:
            def description1 = MessageHelper.errorDescription( '{ "error_description" : "test" }' )
        then:
            description1 == "test"
        when:
            def description2 = MessageHelper.errorDescription( 'hello world' )
        then:
            description2 != null
    }

    def "it can get error description from http status code exception"() {
        when:
            def description1 = MessageHelper.errorDescription( new HttpClientErrorException(
                    HttpStatus.NOT_FOUND, "", '{ "error_description" : "test" }'.bytes, Charset.defaultCharset()
            ) )
        then:
            description1 == "test"
        when:
            def description2 = MessageHelper.errorDescription( new HttpClientErrorException(
                    HttpStatus.NOT_FOUND, "", 'hello world'.bytes, Charset.defaultCharset()
            ) )
        then:
            description2 != null
    }

}
