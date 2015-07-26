package tw.edu.ncu.cc.oauth.resource.test

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.request.RequestPostProcessor
import tw.edu.ncu.cc.oauth.data.v1.attribute.RequestAttribute
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.resource.core.ApiCredentialHolder

class ApiTokenRequestPostProcessor implements RequestPostProcessor {

    private String apiToken
    private String clientId

    ApiTokenRequestPostProcessor( String apiToken ) {
        this.apiToken = apiToken
    }

    ApiTokenRequestPostProcessor clientId( String clientId ) {
        this.clientId = clientId
        this
    }

    @Override
    MockHttpServletRequest postProcessRequest( MockHttpServletRequest request ) {
        request.addHeader( RequestAttribute.API_TOKEN_HEADER, apiToken )
        ApiCredentialHolder.addApiToken( apiToken, new ApiTokenClientObject(
                last_updated: new Date(),
                client_id: clientId
        ) )
        request
    }

}
