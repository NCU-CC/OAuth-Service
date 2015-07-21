package tw.edu.ncu.cc.oauth.resource.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties( "spring.application" )
public class CloudConfig {

    def String name = "unnamed"

}