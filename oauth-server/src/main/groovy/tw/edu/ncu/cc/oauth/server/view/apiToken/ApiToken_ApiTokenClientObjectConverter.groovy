package tw.edu.ncu.cc.oauth.server.view.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
import tw.edu.ncu.cc.oauth.server.helper.data.SerialSecret
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class ApiToken_ApiTokenClientObjectConverter implements Converter< ApiToken, ApiTokenClientObject >{

    @Autowired
    def SecretService secretService

    @Override
    ApiTokenClientObject convert( ApiToken source ) {
        ApiTokenClientObject apiTokenObject = new ApiTokenClientObject()
        apiTokenObject.id = source.id
        apiTokenObject.token = calculateUserSideToken( source )
        apiTokenObject.last_updated = source.lastUpdated
        apiTokenObject.client_id = secretService.encodeHashId( source.client.id )
        apiTokenObject
    }

    private String calculateUserSideToken( ApiToken apiToken ) {
        if( apiToken.token != null ) {
            apiToken.token
        }  else {
            secretService.encodeSerialSecret( new SerialSecret( apiToken.id, token( apiToken ) ) )
        }
    }

    private String token( ApiToken apiToken ) {
        secretService.decrypt( apiToken.encryptedToken )
    }

}
