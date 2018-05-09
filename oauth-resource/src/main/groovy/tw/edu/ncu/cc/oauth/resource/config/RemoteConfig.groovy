package tw.edu.ncu.cc.oauth.resource.config

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( "ncu.oauth" )
public class RemoteConfig {

    def String serverPath = "https://localhost/oauth"
    def String accessTokenPath = "/management/v1/access_tokens/token"
    def String newServerPath = "https://localhost/oauth"
    def String newAccessTokenPath = "/token/old_info"
    def String apiToken = "unspecified"
    def static String apiTokenPath = "/management/v1/api_tokens/token"
    def static String clientBlackListPath = "/management/v1/blacklist/clients"
    def static String userBlackListPath = "/management/v1/blacklist/users"

}
