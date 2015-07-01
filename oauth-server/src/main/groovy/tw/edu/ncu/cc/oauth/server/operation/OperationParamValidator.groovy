package tw.edu.ncu.cc.oauth.server.operation

import tw.edu.ncu.cc.oauth.server.exception.OperationInvalidParamException

import static org.springframework.util.Assert.*

class OperationParamValidator {

    private class Validation {
        def Boolean required
        def String  key
        def Closure assertion
    }

    private Boolean required
    private List validations = []

    def required() {
        required = true
        return this
    }

    def optional() {
        required = false
        return this
    }

    def attribute( key ) {
        validations << new Validation(
                key: key,
                required: required,
                assertion: {  }
        )
    }

    def notNull( key ) {
        validations << new Validation(
                key: key,
                required: required,
                assertion: { value ->
                    notNull( value, "param ${key} should not be null" )
                }
        )
    }

    def hasText( key ) {
        validations << new Validation(
                key: key,
                required: required,
                assertion: { value ->
                    hasText( value as String, "param ${key} should not be null or blank" )
                }
        )
    }

    def isBoolean( key ) {
        validations << new Validation(
                key: key,
                required: required,
                assertion: { value ->
                    isAssignable( Boolean, value.class, "param ${key} should be assignable to boolean " )
                }
        )
    }

    def validate( Map params ) {
        try {
            validations.each { Validation validation ->
                def targetValue = params[ validation.key ]
                if( validation.required ) {
                    notNull( targetValue, "param ${validation.key} should not be null" )
                    validation.assertion.call( targetValue )
                } else {
                    if( targetValue != null ) {
                        validation.assertion.call( targetValue )
                    }
                }
            }
        } catch ( IllegalArgumentException e ) {
            throw new OperationInvalidParamException( e.message )
        }
    }

}
