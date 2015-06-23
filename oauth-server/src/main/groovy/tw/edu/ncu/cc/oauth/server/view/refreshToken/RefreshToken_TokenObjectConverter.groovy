package tw.edu.ncu.cc.oauth.server.view.refreshToken

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import tw.edu.ncu.cc.oauth.data.v1.management.token.TokenObject
import tw.edu.ncu.cc.oauth.server.helper.ScopeHelper
import tw.edu.ncu.cc.oauth.server.model.refreshToken.RefreshToken
import tw.edu.ncu.cc.oauth.server.service.security.SecretService

@Component
class RefreshToken_TokenObjectConverter implements Converter< RefreshToken, TokenObject > {

    @Autowired
    def SecretService secretService

    @Override
    TokenObject convert( RefreshToken source ) {
        TokenObject tokenObject = new TokenObject()
        tokenObject.id = source.id
        tokenObject.client_id = secretService.encodeHashId( source.client.id )
        tokenObject.user = source.user.name
        tokenObject.scope = ScopeHelper.toStringArray( source.scope )
        tokenObject.last_updated = source.lastUpdated
        return tokenObject
    }

}
