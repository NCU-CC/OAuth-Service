package tw.edu.ncu.cc.oauth.resource.config

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties( "ncu.oauth" )
public class RemoteConfig {

    def String apiToken = "unspecified"
    def String serverPath = "https://localhost/oauth"
    def static final String accessTokenPath = "/management/v1/access_tokens/token"
    def static final String apiTokenPath    = "/management/v1/api_tokens/token"
    def static final String clientBlackListPath = "/management/v1/blacklist/clients"
    def static final String userBlackListPath   = "/management/v1/blacklist/users"

}
