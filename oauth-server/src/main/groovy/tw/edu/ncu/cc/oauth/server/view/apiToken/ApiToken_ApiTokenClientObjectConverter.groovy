package tw.edu.ncu.cc.oauth.server.view.apiToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.token.ApiTokenClientObject
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
        apiTokenObject.token = secretService.encryptQueryable( source.encryptedToken )
        apiTokenObject.last_updated = source.lastUpdated
        apiTokenObject.last_used = source.lastUsed
        apiTokenObject.date_created = source.dateCreated
        apiTokenObject.client_trusted = source.client.trusted
        apiTokenObject.client_id = source.client.serialId
        apiTokenObject
    }

}
