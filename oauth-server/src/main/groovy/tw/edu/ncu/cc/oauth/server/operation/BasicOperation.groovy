package tw.edu.ncu.cc.oauth.server.operation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import tw.edu.ncu.cc.oauth.server.exception.HttpRequestInvalidBodyException

abstract class BasicOperation extends ResourceHandler {

    @Autowired
    protected OperationTransaction transaction

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
        def validator = new OperationParamValidator()
        validate( validator )
        validator.validate( params )
    }

    protected def validate( OperationParamValidator validator ){}

    protected abstract def handle( Map params, Map model )

}
