package tw.edu.ncu.cc.oauth.data.v1.management.token;

import tw.edu.ncu.cc.oauth.data.v1.management.client.ClientIdObject;

public class TokenClientObject {

    def String id
    def String user
    def String[] scope
    def Date last_updated
    def Date last_used
    def Date date_created
    def ClientIdObject client

}
