package tw.edu.ncu.cc.oauth.server.operation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException

class ResourceHandler {

    private ThreadLocal< Object > resource = new ThreadLocal<>()

    protected def streams( Closure closure ) {
        try {
            closure.call()
            resource.get()
        } finally {
            resource.remove()
        }
    }

    protected void notNullNotFound( String message = "required resource is not found", Closure closure ) {
        def currentResource = closure.call( resource.get() )
        if( currentResource == null ) {
            throw new HttpServerErrorException( HttpStatus.NOT_FOUND, message )
        } else {
            resource.set( currentResource )
        }
    }

    protected void notNullForbidden( String message = "required resource is forbidden", Closure closure ) {
        def currentResource = closure.call( resource.get() )
        if( currentResource == null ) {
            throw new HttpServerErrorException( HttpStatus.FORBIDDEN, message )
        } else {
            resource.set( currentResource )
        }
    }

}
