package tw.edu.ncu.cc.oauth.resource.config

import spock.lang.Specification
import tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute


class RequestConfigTest extends Specification {

    def "it has embedded values"() {
        expect:
            RequestAttribute.ACCESS_TOKEN_PREFIX == "Bearer"
            RequestAttribute.ACCESS_TOKEN_HEADER == "Authorization"
            RequestAttribute.API_TOKEN_HEADER   == "X-NCU-API-TOKEN"
    }

}
