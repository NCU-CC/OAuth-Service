package tw.edu.ncu.cc.oauth.server.operation

import org.springframework.validation.BindingResult
import tw.edu.ncu.cc.oauth.server.exception.HttpRequestInvalidBodyException
import tw.edu.ncu.cc.oauth.server.exception.OperationInvalidParamException

import static org.springframework.util.Assert.hasText
import static org.springframework.util.Assert.notNull

abstract class BasicOperation extends ResourceHandler {

    private validations = []

    public def process() {
        process( null, [:], [:] )
    }

    public def process( Map params ) {
        process( null, params, [:] )
    }

    public def process( Map params, Map model ) {
        process( null, params, model )
    }

    public def process( BindingResult bindingResult, Map params, Map model = [:] ) {
        validateBindingResult( bindingResult )
        validateParams( params )
        handle( params, model )
    }

    private static void validateBindingResult( BindingResult bindingResult ) {
        if( bindingResult != null && bindingResult.hasErrors() ) {
            throw new HttpRequestInvalidBodyException(
                    bindingResult.fieldErrors.collect {
                        it.defaultMessage
                    }.join( "\n" )
            )
        }
    }

    private void validateParams( Map params ) {
        try {
            validations.each { Closure closure ->
                closure.call( params )
            }
        } catch ( IllegalArgumentException e ) {
            throw new OperationInvalidParamException( e.message )
        }
    }

    protected void assertNotNull( key ) {
        validations << { Map params ->
            notNull( params[ key ], "param ${key} should not be null" )
        }
    }

    protected void assertHasText( key ) {
        validations << { Map params ->
            hasText( params[ key ] as String, "param ${key} should not be null or blank" )
        }
    }

    protected abstract def handle( Map params, Map model )

}
