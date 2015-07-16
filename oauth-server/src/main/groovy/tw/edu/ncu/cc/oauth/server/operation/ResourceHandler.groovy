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

    protected void notNullBadRequest( String message = "it is a bad request", Closure closure ) {
        handleNotNull( closure, new HttpServerErrorException( HttpStatus.BAD_REQUEST, message ) )
    }

    protected void notNullNotFound( String message = "required resource is not found", Closure closure ) {
        handleNotNull( closure, new HttpServerErrorException( HttpStatus.NOT_FOUND, message ) )
    }

    protected void notNullForbidden( String message = "required resource is forbidden", Closure closure ) {
        handleNotNull( closure, new HttpServerErrorException( HttpStatus.FORBIDDEN, message ) )
    }

    private void handleNotNull( Closure closure, Throwable throwable ) {
        def currentResource = closure.call( resource.get() )
        if( currentResource == null ) {
            throw throwable
        } else {
            resource.set( currentResource )
        }
    }


}
