package tw.edu.ncu.cc.oauth.data.v1.management.token

public class TokenObject {

    def String id
    def String user
    def String client_id
    def String[] scope
    def Date last_updated
    def Date last_used
    def Date date_created

}
