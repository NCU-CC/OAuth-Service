package tw.edu.ncu.cc.oauth.server.exception;

public class HttpRequestInvalidBodyException extends RuntimeException {

    public HttpRequestInvalidBodyException( String message ) {
        super( message );
    }
}
