package tw.edu.ncu.cc.oauth.resource.core

import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject

class ApiCredentialHolder {

    private static Map< String, ApiTokenClientObject > apiTokenMap = new Hashtable<>()
    private static Map< String, TokenObject > accessTokenMap = new Hashtable<>()

    static void addApiToken( String token, ApiTokenClientObject apiToken ) {
        apiTokenMap.put( token, apiToken )
    }

    static void removeApiToken( String token ) {
        apiTokenMap.remove( token )
    }

    static boolean containsApiToken( String token ) {
        apiTokenMap.containsKey( token )
    }

    static ApiTokenClientObject getApiToken( String token ) {
        apiTokenMap.get( token )
    }

    static void addAccessToken( String token,TokenObject accessToken ) {
        accessTokenMap.put( token, accessToken )
    }

    static void removeAccessToken( String token ) {
        accessTokenMap.remove( token )
    }

    static boolean containsAccessToken( String token ) {
        accessTokenMap.containsKey( token )
    }

    static TokenObject getAccessToken( String token ) {
        accessTokenMap.get( token )
    }

}
