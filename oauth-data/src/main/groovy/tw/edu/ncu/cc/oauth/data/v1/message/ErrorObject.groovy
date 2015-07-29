package tw.edu.ncu.cc.oauth.data.v1.message;

public class ErrorObject {

    def String error_description

    public ErrorObject(){}

    public ErrorObject( String error_description ) {
        this.error_description = error_description;
    }

}
