package tw.edu.ncu.cc.oauth.server.operation

import tw.edu.ncu.cc.oauth.server.exception.ResourceNotFoundException


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

    protected void notNullStream( Closure closure ) {
        def currentResource = closure.call( resource.get() )
        if( currentResource == null ) {
            throw new ResourceNotFoundException()
        } else {
            resource.set( currentResource )
        }
    }

}
