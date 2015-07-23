package tw.edu.ncu.cc.oauth.server.view.accessToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.server.helper.ScopeHelper
import tw.edu.ncu.cc.oauth.server.model.accessToken.AccessToken
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class AccessToken_TokenObjectConverter implements Converter< AccessToken, TokenObject > {

    @Autowired
    def SecretService secretService

    @Override
    TokenObject convert( AccessToken source ) {
        TokenObject tokenObject = new TokenObject()
        tokenObject.id = source.id
        tokenObject.client_id = source.client.serialId
        tokenObject.user = source.user.name
        tokenObject.scope = ScopeHelper.toStringArray( source.scope )
        tokenObject.last_updated = source.lastUpdated
        tokenObject
    }

}
