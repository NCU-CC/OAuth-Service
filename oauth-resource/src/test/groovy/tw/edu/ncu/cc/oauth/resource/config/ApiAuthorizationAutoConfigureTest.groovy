package tw.edu.ncu.cc.oauth.resource.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@WebAppConfiguration
@ContextConfiguration ( loader = SpringApplicationContextLoader.class, classes = ApiAuthorizationAutoConfigure.class )
class ApiAuthorizationAutoConfigureTest extends Specification {

    @Autowired( required = false )
    RemoteConfig remoteConfig

    @Autowired( required = false )
    RestTemplate restTemplate

    def "it would load resources"() {
        expect:
            remoteConfig != null
            restTemplate != null
    }

}
