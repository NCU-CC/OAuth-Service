package tw.edu.ncu.cc.oauth.server.view.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenObject
import tw.edu.ncu.cc.oauth.server.helper.data.SerialSecret
import tw.edu.ncu.cc.oauth.server.model.apiToken.ApiToken
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class ApiToken_ApiTokenObjectConverter implements Converter< ApiToken, ApiTokenObject >{

    @Autowired
    def SecretService secretService

    @Override
    ApiTokenObject convert( ApiToken source ) {
        ApiTokenObject apiTokenObject = new ApiTokenObject()
        apiTokenObject.id = source.id
        apiTokenObject.token = calculateUserSideToken( source )
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
