package tw.edu.ncu.cc.oauth.server.exception;

public class OperationInvalidParamException extends RuntimeException {
    public OperationInvalidParamException( String message ) {
        super( message );
    }
}
